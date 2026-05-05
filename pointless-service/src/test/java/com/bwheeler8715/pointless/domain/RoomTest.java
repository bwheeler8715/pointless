package com.bwheeler8715.pointless.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoomTest {

    @Test
    void testRoomCreation() {
        String id = "room-123";
        String organizerCode = "org-456";
        String name = "Test Room";

        Room room = new Room(id, organizerCode, name);

        assertEquals(id, room.getId());
        assertEquals(organizerCode, room.getOrganizerCode());
        assertEquals(name, room.getName());
        assertNotNull(room.getVoters());
        assertTrue(room.getVoters().isEmpty());
        assertNotNull(room.getVoting());
        assertFalse(room.getVoting().isVotingOpen());
        assertNotNull(room.getCreated());
        assertNotNull(room.getLastUpdated());
    }

    @Test
    void testAddAndRemoveVoter() {
        Room room = new Room("id", "code", "name");
        Voter voter = new Voter("voter-1", "Voter 1", "id");

        room.addVoter(voter);
        assertEquals(1, room.getVoters().size());
        assertEquals(voter, room.getVoters().get(0));

        room.removeVoter("voter-1");
        assertTrue(room.getVoters().isEmpty());
    }

    @Test
    void testSetLastUpdated() {
        Room room = new Room("id", "code", "name");
        OffsetDateTime now = OffsetDateTime.now();
        room.setLastUpdated(now);
        assertEquals(now, room.getLastUpdated());
    }
}
