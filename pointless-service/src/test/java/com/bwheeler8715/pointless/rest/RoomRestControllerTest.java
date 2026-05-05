package com.bwheeler8715.pointless.rest;

import com.bwheeler8715.pointless.core.RoomEventStreamService;
import com.bwheeler8715.pointless.core.RoomService;
import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.domain.Voter;
import com.bwheeler8715.pointless.mapper.RoomJsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomRestController.class)
class RoomRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private RoomEventStreamService roomEventStreamService;

    @MockitoBean
    private RoomJsonMapper roomJsonMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateRoom() throws Exception {
        Room room = new Room("room-id", "org-code", "Test Room");
        when(roomService.createRoom("Test Room")).thenReturn(room);

        Map<String, String> request = new HashMap<>();
        request.put("name", "Test Room");

        mockMvc.perform(post("/api/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value("room-id"))
                .andExpect(jsonPath("$.organizerCode").value("org-code"));

        verify(roomService).createRoom("Test Room");
    }

    @Test
    void testJoinRoom() throws Exception {
        Voter voter = new Voter("voter-id", "Voter Name", "room-id");
        when(roomService.getRoom("room-id", "Test Room")).thenReturn(new Room("room-id", "org-code", "Test Room"));
        when(roomService.joinRoom("room-id", "Test Room", "Voter Name")).thenReturn(voter);

        Map<String, String> request = new HashMap<>();
        request.put("id", "room-id");
        request.put("name", "Test Room");
        request.put("voter", "Voter Name");

        mockMvc.perform(post("/api/room/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("voter-id"))
                .andExpect(jsonPath("$.name").value("Voter Name"))
                .andExpect(jsonPath("$.roomId").value("room-id"));

        verify(roomService).joinRoom("room-id", "Test Room", "Voter Name");
    }

    @Test
    void testLeaveRoom() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("roomId", "room-id");
        request.put("id", "voter-id");

        mockMvc.perform(post("/api/room/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(roomService).leaveRoom("room-id", "voter-id");
    }

    @Test
    void testOpenVoting_Success() throws Exception {
        Room room = new Room("room-id", "org-code", "Test Room");
        when(roomService.getRoom("room-id", null)).thenReturn(room);

        Map<String, String> request = new HashMap<>();
        request.put("roomId", "room-id");
        request.put("currentTickerNumber", "TICK-1");
        request.put("currentTicketSummary", "Summary");
        request.put("organizerCode", "org-code");

        mockMvc.perform(post("/api/room/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(roomService).openVoting("room-id", "TICK-1", "Summary");
    }

    @Test
    void testOpenVoting_Unauthorized() throws Exception {
        Room room = new Room("room-id", "org-code", "Test Room");
        when(roomService.getRoom("room-id", null)).thenReturn(room);

        Map<String, String> request = new HashMap<>();
        request.put("roomId", "room-id");
        request.put("organizerCode", "wrong-code");

        mockMvc.perform(post("/api/room/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(roomService, never()).openVoting(anyString(), anyString(), anyString());
    }

    @Test
    void testCloseVoting_Success() throws Exception {
        Room room = new Room("room-id", "org-code", "Test Room");
        when(roomService.getRoom("room-id", null)).thenReturn(room);

        Map<String, String> request = new HashMap<>();
        request.put("roomId", "room-id");
        request.put("organizerCode", "org-code");

        mockMvc.perform(post("/api/room/close")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(roomService).closeVoting("room-id");
    }

    @Test
    void testVote() throws Exception {
        when(roomService.getRoom("room-id", null)).thenReturn(new Room("room-id", "org-code", "Test Room"));

        Map<String, String> request = new HashMap<>();
        request.put("roomId", "room-id");
        request.put("voterId", "voter-id");
        request.put("vote", "5");

        mockMvc.perform(post("/api/room/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(roomService).vote("room-id", "voter-id", "5");
    }
}
