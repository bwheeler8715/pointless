package com.bwheeler8715.pointless.core;

import com.bwheeler8715.pointless.domain.Room;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class InMemoryRoomDAO implements RoomDAO {
    private final Map<String, Room> rooms;

    public InMemoryRoomDAO() {
        this.rooms = new ConcurrentHashMap<>();
    }

    @Override
    public Room getRoomById(String id) {
        return rooms.get(id);
    }

    @Override
    public Room findRoomByName(String name) {
        return rooms.values().stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void createRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    @Override
    public void updateRoom(String id, Consumer<Room> updater) {
        rooms.computeIfPresent(id, (k, room) -> {
            updater.accept(room);
            room.setLastUpdated(OffsetDateTime.now());
            return room;
        });
    }

    @Scheduled(cron = "${pointless.purge-rooms-cron}")
    public void purgeRooms() {
        OffsetDateTime threshold = OffsetDateTime.now().minusHours(8);
        rooms.values().removeIf(room -> room.getLastUpdated().isBefore(threshold));
    }
}