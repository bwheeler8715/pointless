package com.bwheeler8715.pointless.mapper;

import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.domain.Voter;
import com.bwheeler8715.pointless.json.RoomJson;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RoomJsonMapperTest {

    private final RoomJsonMapper mapper = new RoomJsonMapper();

    @Test
    void testMapToRoomJson() {
        Room room = new Room("room-1", "org-123", "Test Room");
        room.getVoting().setVotingOpen(true);
        room.getVoting().setCurrentTickerNumber("TICK-1");
        room.getVoting().setCurrentTicketSummary("Summary");
        room.getVoting().setVoteCounts(Map.of("1", 2, "2", 1));
        room.getVoting().setAverageVote(1.33);

        Voter voter = new Voter("voter-1", "Voter 1", "room-1");
        voter.setVote("1");
        room.addVoter(voter);

        RoomJson json = mapper.mapToRoomJson(room);

        assertEquals("room-1", json.getId());
        assertEquals("Test Room", json.getName());
        assertTrue(json.isVotingOpen());
        assertEquals("TICK-1", json.getCurrentTickerNumber());
        assertEquals("Summary", json.getCurrentTicketSummary());
        assertEquals(Map.of("1", 2, "2", 1), json.getVoteCounts());
        assertEquals(1.33, json.getAverageVote());

        assertEquals(1, json.getVoters().size());
        assertEquals("voter-1", json.getVoters().get(0).getId());
        assertEquals("Voter 1", json.getVoters().get(0).getName());
        assertEquals("room-1", json.getVoters().get(0).getRoomId());
        assertEquals("1", json.getVoters().get(0).getVote());
    }
}
