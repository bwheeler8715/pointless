package com.bwheeler8715.pointless.service;

import com.bwheeler8715.pointless.core.DefaultRoomService;
import com.bwheeler8715.pointless.core.RoomDAO;
import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.domain.Voter;
import com.bwheeler8715.pointless.exception.RoomAlreadyExistsException;
import com.bwheeler8715.pointless.exception.RoomNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultRoomServiceTest {

    private RoomDAO roomDAO;
    private DefaultRoomService underTest;

    @BeforeEach
    void setUp() {
        roomDAO = mock(RoomDAO.class);
        underTest = new DefaultRoomService(roomDAO);
    }

    @Test
    void testCreateRoom() {
        // Given
        String roomName = "Test Room";
        when(roomDAO.findRoomByName(roomName)).thenReturn(null);

        // When
        Room createdRoom = underTest.createRoom(roomName);

        // Then
        assertNotNull(createdRoom);
        assertEquals(roomName, createdRoom.getName());
        assertNotNull(createdRoom.getId());
        assertNotNull(createdRoom.getOrganizerCode());

        // Verify DAO call
        ArgumentCaptor<Room> roomCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomDAO).createRoom(roomCaptor.capture());

        Room savedRoom = roomCaptor.getValue();
        assertEquals(createdRoom.getId(), savedRoom.getId());
        assertEquals(createdRoom.getOrganizerCode(), savedRoom.getOrganizerCode());
        assertEquals(createdRoom.getName(), savedRoom.getName());
    }

    @Test
    void testCreateRoomAlreadyExists() {
        String roomName = "Existing Room";
        when(roomDAO.findRoomByName(roomName)).thenReturn(new Room("1", "code", roomName));

        assertThrows(RoomAlreadyExistsException.class, () -> underTest.createRoom(roomName));
    }

    @Test
    void testGetRoomById() {
        Room room = new Room("id1", "code", "Name");
        when(roomDAO.getRoomById("id1")).thenReturn(room);

        Room result = underTest.getRoom("id1", null);
        assertEquals(room, result);
    }

    @Test
    void testGetRoomByName() {
        Room room = new Room("id1", "code", "Name");
        when(roomDAO.findRoomByName("Name")).thenReturn(room);

        Room result = underTest.getRoom("id1", "Name");
        assertEquals(room, result);
    }

    @Test
    void testGetRoomNotFound() {
        when(roomDAO.getRoomById("any")).thenReturn(null);
        assertThrows(RoomNotFoundException.class, () -> underTest.getRoom("any", null));
    }

    @Test
    void testJoinRoom() {
        String roomId = "room1";
        String roomName = "Room 1";
        String voterName = "John";

        Voter voter = underTest.joinRoom(roomId, roomName, voterName);

        assertNotNull(voter);
        assertEquals(voterName, voter.getName());
        assertEquals(roomId, voter.getRoomId());
        assertNotNull(voter.getId());

        verify(roomDAO).updateRoom(eq(roomId), any());
    }

    @Test
    void testLeaveRoom() {
        underTest.leaveRoom("room1", "voter1");
        verify(roomDAO).updateRoom(eq("room1"), any());
    }

    @Test
    void testOpenVoting() {
        underTest.openVoting("room1", "T-1", "Summary");
        verify(roomDAO).updateRoom(eq("room1"), any());
    }

    @Test
    void testCloseVoting() {
        underTest.closeVoting("room1");
        verify(roomDAO).updateRoom(eq("room1"), any());
    }

    @Test
    void testVote() {
        underTest.vote("room1", "voter1", "5");
        verify(roomDAO).updateRoom(eq("room1"), any());
    }

    @Test
    void testCalculateResults() {
        Room room = new Room("id", "code", "name");
        Voter v1 = new Voter("1", "v1", "id");
        v1.setVote("5");
        Voter v2 = new Voter("2", "v2", "id");
        v2.setVote("8");
        Voter v4 = new Voter("4", "v4", "id");
        v4.setVote(null); // Should be ignored

        room.addVoter(v1);
        room.addVoter(v2);
        room.addVoter(v4);

        underTest.calculateResults(room);

        assertEquals(6.5, room.getVoting().getAverageVote());
        assertEquals(2, room.getVoting().getVoteCounts().size());
        assertEquals(1, room.getVoting().getVoteCounts().get("5"));
        assertEquals(1, room.getVoting().getVoteCounts().get("8"));
    }

    @Test
    void testClearVotes() {
        Room room = new Room("id", "code", "name");
        Voter v1 = new Voter("1", "v1", "id");
        v1.setVote("5");
        room.addVoter(v1);

        underTest.clearVotes(room);

        assertNull(v1.getVote());
    }
}
