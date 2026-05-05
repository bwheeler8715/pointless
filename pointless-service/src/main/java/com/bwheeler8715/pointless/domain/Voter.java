package com.bwheeler8715.pointless.domain;

public class Voter {
    private final String id;
    private final String name;
    private final String roomId;
    private String vote;

    public Voter(String id, String name, String roomId) {
        this.id = id;
        this.name = name;
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}