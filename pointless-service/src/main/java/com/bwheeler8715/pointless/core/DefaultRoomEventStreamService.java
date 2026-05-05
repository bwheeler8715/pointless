package com.bwheeler8715.pointless.core;

import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.mapper.RoomJsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class DefaultRoomEventStreamService implements RoomEventStreamService {

    private final RoomService roomService;
    private final RoomJsonMapper roomJsonMapper;
    private final Long TIMEOUT;
    private final Map<String, List<SseEmitter>> emitters;

    public DefaultRoomEventStreamService(RoomService roomService,
                                         RoomJsonMapper roomJsonMapper,
                                         @Value("${pointless.sse-timeout}") Long timeout) {
        this.roomService = roomService;
        this.roomJsonMapper = roomJsonMapper;
        this.TIMEOUT = timeout;
        this.emitters = new ConcurrentHashMap<>();
    }

    @Override
    public SseEmitter register(String roomId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // Remove emitter when connection is closed or timed out
        emitter.onCompletion(() -> removeEmitter(roomId, emitter));
        emitter.onTimeout(() -> removeEmitter(roomId, emitter));
        emitter.onError((e) -> removeEmitter(roomId, emitter));

        return emitter;
    }

    @Override
    public void removeEmitter(String roomId, SseEmitter emitter) {
        List<SseEmitter> roomEmitters = emitters.get(roomId);
        if (roomEmitters != null) {
            roomEmitters.remove(emitter);
            if (roomEmitters.isEmpty()) {
                emitters.remove(roomId);
            }
        }
    }

    @Override
    public void broadcastUpdate(String roomId) {
        Room room = roomService.getRoom(roomId, null);

        List<SseEmitter> roomEmitters = emitters.get(roomId);
        if (roomEmitters != null) {
            for (SseEmitter emitter : roomEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("room-update")
                            .data(roomJsonMapper.mapToRoomJson(room))
                    );
                } catch (Exception e) {
                    removeEmitter(roomId, emitter);
                }
            }
        }
    }

    @Scheduled(cron = "${pointless.purge-connections-cron}")
    public void purgeConnections() {
        emitters.forEach((roomId, emitters) -> {
            Room room = roomService.getRoom(roomId, null);
            if (room == null) {
                emitters.clear();
            }
        });
    }
}