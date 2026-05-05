package com.bwheeler8715.pointless.mapper;

import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.json.RoomJson;
import com.bwheeler8715.pointless.json.VoterJson;
import org.springframework.stereotype.Component;

@Component
public class RoomJsonMapper {
    public RoomJson mapToRoomJson(Room room) {
        RoomJson json = new RoomJson();
        json.setId(room.getId());
        json.setName(room.getName());
        json.setVoters(room.getVoters().stream()
                .map(voter -> {
                    VoterJson voterJson = new VoterJson();
                    voterJson.setId(voter.getId());
                    voterJson.setName(voter.getName());
                    voterJson.setRoomId(voter.getRoomId());
                    voterJson.setVote(voter.getVote());
                    return voterJson;
                }).toList());
        json.setVotingOpen(room.getVoting().isVotingOpen());
        json.setCurrentTickerNumber(room.getVoting().getCurrentTickerNumber());
        json.setCurrentTicketSummary(room.getVoting().getCurrentTicketSummary());
        json.setVoteCounts(room.getVoting().getVoteCounts());
        json.setAverageVote(room.getVoting().getAverageVote());
        return json;
    }
}
