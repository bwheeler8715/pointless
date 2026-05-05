package com.bwheeler8715.pointless.domain;

import java.util.Map;

public class Voting {
    private boolean votingOpen;
    private String currentTickerNumber;
    private String currentTicketSummary;
    private Map<String, Integer> voteCounts;
    private Double averageVote;

    public Voting() {
        this.votingOpen = false;
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
