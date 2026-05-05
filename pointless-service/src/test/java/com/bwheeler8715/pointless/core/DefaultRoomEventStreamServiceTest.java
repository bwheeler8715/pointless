package com.bwheeler8715.pointless.core;

import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.json.RoomJson;
import com.bwheeler8715.pointless.mapper.RoomJsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultRoomEventStreamServiceTest {

    @Mock
    private RoomService roomService;

    @Mock
    private RoomJsonMapper roomJsonMapper;

    private DefaultRoomEventStreamService service;

    @BeforeEach
    void setUp() {
        service = new DefaultRoomEventStreamService(roomService, roomJsonMapper, 60000L);
    }

    @Test
    void testRegister() {
        SseEmitter emitter = service.register("room-1");
        assertNotNull(emitter);
    }

    @Test
    void testBroadcastUpdate() throws IOException {
        String roomId = "room-1";
        Room room = new Room(roomId, "org-code", "Test Room");
        RoomJson roomJson = new RoomJson();
        roomJson.setId(roomId);

        when(roomService.getRoom(roomId, null)).thenReturn(room);
        when(roomJsonMapper.mapToRoomJson(room)).thenReturn(roomJson);

        SseEmitter emitter = service.register(roomId);
        
        // We can't easily mock SseEmitter.send because it's a final method in some versions or just hard to mock.
        // But we can call broadcastUpdate and ensure no exceptions are thrown.
        service.broadcastUpdate(roomId);

        verify(roomService).getRoom(roomId, null);
        verify(roomJsonMapper).mapToRoomJson(room);
    }

    @Test
    void testPurgeConnections() {
        String roomId = "room-1";
        service.register(roomId);
        
        when(roomService.getRoom(roomId, null)).thenReturn(mock(Room.class));
        
        service.purgeConnections();
        
        service.broadcastUpdate(roomId);
        verify(roomService, times(2)).getRoom(any(), any());
    }
}
