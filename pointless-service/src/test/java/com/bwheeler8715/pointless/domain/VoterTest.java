package com.bwheeler8715.pointless.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VoterTest {

    @Test
    void testVoterCreation() {
        String id = "voter-1";
        String name = "John Doe";
        String roomId = "room-1";

        Voter voter = new Voter(id, name, roomId);

        assertEquals(id, voter.getId());
        assertEquals(name, voter.getName());
        assertEquals(roomId, voter.getRoomId());
        assertNull(voter.getVote());
    }

    @Test
    void testSetVote() {
        Voter voter = new Voter("id", "name", "roomId");
        voter.setVote("5");
        assertEquals("5", voter.getVote());
    }
}
