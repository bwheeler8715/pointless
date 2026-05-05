package com.bwheeler8715.pointless.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VotingTest {

    @Test
    void testVotingInitialState() {
        Voting voting = new Voting();
        assertFalse(voting.isVotingOpen());
        assertNull(voting.getCurrentTickerNumber());
        assertNull(voting.getCurrentTicketSummary());
        assertNull(voting.getVoteCounts());
        assertNull(voting.getAverageVote());
    }

    @Test
    void testSettersAndGetters() {
        Voting voting = new Voting();

        voting.setVotingOpen(true);
        assertTrue(voting.isVotingOpen());

        voting.setCurrentTickerNumber("TICKET-123");
        assertEquals("TICKET-123", voting.getCurrentTickerNumber());

        voting.setCurrentTicketSummary("New feature description");
        assertEquals("New feature description", voting.getCurrentTicketSummary());

        Map<String, Integer> counts = new HashMap<>();
        counts.put("5", 2);
        voting.setVoteCounts(counts);
        assertEquals(counts, voting.getVoteCounts());

        voting.setAverageVote(5.0);
        assertEquals(5.0, voting.getAverageVote());
    }
}
