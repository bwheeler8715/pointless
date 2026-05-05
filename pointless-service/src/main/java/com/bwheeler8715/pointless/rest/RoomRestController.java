package com.bwheeler8715.pointless.rest;

import com.bwheeler8715.pointless.core.RoomEventStreamService;
import com.bwheeler8715.pointless.core.RoomService;
import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.domain.Voter;
import com.bwheeler8715.pointless.exception.UnauthorizedOrganizerException;
import com.bwheeler8715.pointless.json.CloseVotingRequestJson;
import com.bwheeler8715.pointless.json.GetRoomRequestJson;
import com.bwheeler8715.pointless.json.JoinRoomRequestJson;
import com.bwheeler8715.pointless.json.NewRoomRequestJson;
import com.bwheeler8715.pointless.json.NewRoomResponseJson;
import com.bwheeler8715.pointless.json.OpenVotingRequestJson;
import com.bwheeler8715.pointless.json.RoomJson;
import com.bwheeler8715.pointless.json.VoteRequestJson;
import com.bwheeler8715.pointless.json.VoterJson;
import com.bwheeler8715.pointless.mapper.RoomJsonMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(
        value = "/api/room",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class RoomRestController {

    private final RoomService roomService;
    private final RoomEventStreamService roomEventStreamService;
    private final RoomJsonMapper roomJsonMapper;

    public RoomRestController(RoomService roomService,
                              RoomEventStreamService roomEventStreamService,
                              RoomJsonMapper roomJsonMapper) {
        this.roomService = roomService;
        this.roomEventStreamService = roomEventStreamService;
        this.roomJsonMapper = roomJsonMapper;
    }

    @PostMapping(path = "/get")
    public ResponseEntity<RoomJson> getRoom(@RequestBody GetRoomRequestJson request) {
        Room room = roomService.getRoom(request.getId(), request.getName());
        return ResponseEntity.ok(roomJsonMapper.mapToRoomJson(room));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<NewRoomResponseJson> createRoom(@RequestBody NewRoomRequestJson request) {
        Room room = roomService.createRoom(request.getName());

        NewRoomResponseJson response = new NewRoomResponseJson();
        response.setRoomId(room.getId());
        response.setOrganizerCode(room.getOrganizerCode());

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/join")
    public ResponseEntity<VoterJson> joinRoom(@RequestBody JoinRoomRequestJson request) {
        Room room = roomService.getRoom(request.getId(), request.getName());
        Voter voter = roomService.joinRoom(room.getId(), room.getName(), request.getVoter());

        VoterJson response = new VoterJson();
        response.setId(voter.getId());
        response.setName(voter.getName());
        response.setRoomId(voter.getRoomId());

        roomEventStreamService.broadcastUpdate(room.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/leave")
    public ResponseEntity<Void> leaveRoom(@RequestBody VoterJson request) {
        roomService.leaveRoom(request.getRoomId(), request.getId());

        roomEventStreamService.broadcastUpdate(request.getRoomId());

        return ResponseEntity.accepted().build();
    }

    @PostMapping(path = "/open")
    public ResponseEntity<Void> openVoting(@RequestBody OpenVotingRequestJson request) {
        Room room = roomService.getRoom(request.getRoomId(), null);

        if (room.getOrganizerCode().equals(request.getOrganizerCode())) {
            roomService.openVoting(request.getRoomId(), request.getCurrentTickerNumber(), request.getCurrentTicketSummary());
        } else {
            throw new UnauthorizedOrganizerException();
        }

        roomEventStreamService.broadcastUpdate(room.getId());

        return ResponseEntity.accepted().build();
    }

    @PostMapping(path = "/close")
    public ResponseEntity<Void> closeVoting(@RequestBody CloseVotingRequestJson request) {
        Room room = roomService.getRoom(request.getRoomId(), null);

        if (room.getOrganizerCode().equals(request.getOrganizerCode())) {
            roomService.closeVoting(request.getRoomId());
        } else {
            throw new UnauthorizedOrganizerException();
        }

        roomEventStreamService.broadcastUpdate(room.getId());

        return ResponseEntity.accepted().build();
    }

    @PostMapping(path = "/vote")
    public ResponseEntity<Void> vote(@RequestBody VoteRequestJson request) {
        Room room = roomService.getRoom(request.getRoomId(), null);

        roomService.vote(request.getRoomId(), request.getVoterId(), request.getVote());

        roomEventStreamService.broadcastUpdate(room.getId());

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/{roomId}/stream", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> streamVotes(@PathVariable("roomId") String roomId) {
        SseEmitter emitter = roomEventStreamService.register(roomId);

        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .header("Cache-Control", "no-cache")
                .body(emitter);
    }
}