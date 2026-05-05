package com.bwheeler8715.pointless.core;

import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.domain.Voter;
import com.bwheeler8715.pointless.domain.Voting;
import com.bwheeler8715.pointless.exception.RoomAlreadyExistsException;
import com.bwheeler8715.pointless.exception.RoomNotFoundException;
import com.bwheeler8715.pointless.exception.VoterNotFoundException;
import com.bwheeler8715.pointless.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DefaultRoomService implements RoomService {

    private final RoomDAO roomDAO;

    public DefaultRoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Override
    public Room getRoom(String roomId, String name) {
        Room room = null;
        if (name != null) {
            room = roomDAO.findRoomByName(name);
        }
        if (room == null) {
            room = roomDAO.getRoomById(roomId);
        }
        if (room == null) {
            throw new RoomNotFoundException();
        }
        return room;
    }

    @Override
    public Room createRoom(String name) {
        Room roomByName = roomDAO.findRoomByName(name);
        if (roomByName != null) {
            throw new RoomAlreadyExistsException();
        }

        String id = IdGenerator.generateId();
        String organizerCode = IdGenerator.generateId();
        Room room = new Room(
                id,
                organizerCode,
                name
        );
        roomDAO.createRoom(room);
        return room;
    }

    @Override
    public Voter joinRoom(String roomId, String roomName, String voterName) {
        Voter voter = new Voter(IdGenerator.generateId(), voterName, roomId);
        roomDAO.updateRoom(roomId, r -> r.addVoter(voter));
        return voter;
    }

    @Override
    public void leaveRoom(String roomId, String voterId) {
        roomDAO.updateRoom(roomId, room -> room.removeVoter(voterId));
    }

    @Override
    public void openVoting(String roomId, String ticketNumber, String ticketSummary) {
        roomDAO.updateRoom(roomId, room -> {
            clearVotes(room);
            Voting voting = room.getVoting();
            voting.setVotingOpen(true);
            voting.setCurrentTickerNumber(ticketNumber);
            voting.setCurrentTicketSummary(ticketSummary);
        });
    }

    @Override
    public void closeVoting(String roomId) {
        roomDAO.updateRoom(roomId, room -> {
            room.getVoting().setVotingOpen(false);
            calculateResults(room);
        });
    }

    @Override
    public void vote(String roomId, String voterId, String vote) {
        roomDAO.updateRoom(roomId, room -> {
            Voter voter = room.getVoters().stream()
                    .filter(v -> v.getId().equals(voterId))
                    .findFirst()
                    .orElseThrow(VoterNotFoundException::new);
            voter.setVote(vote);
        });
    }

    @Override
    public void clearVotes(Room room) {
        room.getVoters().forEach(voter -> voter.setVote(null));
    }

    @Override
    public void calculateResults(Room room) {
        Map<String, Integer> voteCounts = new HashMap<>();
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        AtomicInteger count = new AtomicInteger();

        room.getVoters().forEach(voter -> {
            if (voter.getVote() != null) {
                voteCounts.merge(voter.getVote(), 1, Integer::sum);
                double vote = Double.parseDouble(voter.getVote());
                sum.updateAndGet(v -> v + vote);
                count.getAndIncrement();
            }
        });

        Voting voting = room.getVoting();
        voting.setVoteCounts(voteCounts);
        voting.setAverageVote(count.get() > 0 ? sum.get() / count.get() : 0);
    }
}