package com.bwheeler8715.pointless.core;

import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.domain.Voter;

public interface RoomService {

    Room getRoom(String roomId, String name);

    Room createRoom(String name);

    Voter joinRoom(String roomId, String roomName, String voterName);

    void leaveRoom(String roomId, String voterId);

    void openVoting(String roomId, String ticketNumber, String ticketSummary);

    void closeVoting(String roomId);

    void vote(String roomId, String voterId, String vote);

    void clearVotes(Room room);

    void calculateResults(Room room);
}