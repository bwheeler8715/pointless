package com.bwheeler8715.pointless.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    private final String id;
    private final String organizerCode;
    private final String name;
    private final List<Voter> voters;
    private final Voting voting;
    private final OffsetDateTime created;
    private OffsetDateTime lastUpdated;

    public Room(String id,
                String organizerCode,
                String name) {
        this.id = id;
        this.organizerCode = organizerCode;
        this.name = name;
        this.voters = new CopyOnWriteArrayList<>();
        this.voting = new Voting();
        this.created = OffsetDateTime.now();
        this.lastUpdated = OffsetDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getOrganizerCode() {
        return organizerCode;
    }

    public String getName() {
        return name;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public void addVoter(Voter voter) {
        this.voters.add(voter);
    }

    public void removeVoter(String voterId) {
        this.voters.removeIf(voter -> voter.getId().equals(voterId));
    }

    public Voting getVoting() {
        return voting;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}