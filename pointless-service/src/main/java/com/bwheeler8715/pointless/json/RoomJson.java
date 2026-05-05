package com.bwheeler8715.pointless.json;

import java.util.List;
import java.util.Map;

public class RoomJson {
    private String id;
    private String name;
    private List<VoterJson> voters;
    private boolean votingOpen;
    private String currentTickerNumber;
    private String currentTicketSummary;
    private Map<String, Integer> voteCounts;
    private Double averageVote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VoterJson> getVoters() {
        return voters;
    }

    public void setVoters(List<VoterJson> voters) {
        this.voters = voters;
    }

    public boolean isVotingOpen() {
        return votingOpen;
    }

    public void setVotingOpen(boolean votingOpen) {
        this.votingOpen = votingOpen;
    }

    public String getCurrentTickerNumber() {
        return currentTickerNumber;
    }

    public void setCurrentTickerNumber(String currentTickerNumber) {
        this.currentTickerNumber = currentTickerNumber;
    }

    public String getCurrentTicketSummary() {
        return currentTicketSummary;
    }

    public void setCurrentTicketSummary(String currentTicketSummary) {
        this.currentTicketSummary = currentTicketSummary;
    }

    public Map<String, Integer> getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(Map<String, Integer> voteCounts) {
        this.voteCounts = voteCounts;
    }

    public Double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(Double averageVote) {
        this.averageVote = averageVote;
    }
}