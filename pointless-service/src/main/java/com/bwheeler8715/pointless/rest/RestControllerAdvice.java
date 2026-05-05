package com.bwheeler8715.pointless.rest;

import com.bwheeler8715.pointless.exception.RoomAlreadyExistsException;
import com.bwheeler8715.pointless.exception.RoomNotFoundException;
import com.bwheeler8715.pointless.exception.UnauthorizedOrganizerException;
import com.bwheeler8715.pointless.exception.VoterNotFoundException;
import com.bwheeler8715.pointless.json.ErrorResponseJson;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;

@ControllerAdvice
public class RestControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerAdvice.class);

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<?> handleRoomAlreadyExistsException(RoomAlreadyExistsException e, HttpServletRequest request) {
        LOGGER.error("Room already exists", e);

        ErrorResponseJson response = mapToErrorResponseJson(HttpStatus.CONFLICT, "A room with that name already exists.", request);
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return mapToSseResponse(response);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<?> handleRoomNotFoundException(RoomNotFoundException e, HttpServletRequest request) {
        LOGGER.error("Room not found", e);

        ErrorResponseJson response = mapToErrorResponseJson(HttpStatus.NOT_FOUND, "Room not found.", request);
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return mapToSseResponse(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(VoterNotFoundException.class)
    public ResponseEntity<?> handleVoterNotFoundException(VoterNotFoundException e, HttpServletRequest request) {
        LOGGER.error("Voter not found", e);

        ErrorResponseJson response = mapToErrorResponseJson(HttpStatus.NOT_FOUND, "Voter not found.", request);
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return mapToSseResponse(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UnauthorizedOrganizerException.class)
    public ResponseEntity<?> handleUnauthorizedOrganizerException(UnauthorizedOrganizerException e, HttpServletRequest request) {
        LOGGER.error("Unauthorized organizer", e);

        ErrorResponseJson response = mapToErrorResponseJson(HttpStatus.UNAUTHORIZED, "You are not permitted to perform this action.", request);
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return mapToSseResponse(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleAsyncNotUsable() {
        LOGGER.debug("Async request not usable");
        // The client is gone, so there is no one to send a response to anyway.
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        LOGGER.error("Illegal argument", e);

        ErrorResponseJson response = mapToErrorResponseJson(HttpStatus.BAD_REQUEST, e.getMessage(), request);
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return mapToSseResponse(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        LOGGER.error("Unexpected error", e);

        ErrorResponseJson response = mapToErrorResponseJson(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return mapToSseResponse(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public ErrorResponseJson mapToErrorResponseJson(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponseJson response = new ErrorResponseJson();
        response.setStausCode(status.value());
        response.setStatus(status.getReasonPhrase());
        response.setMessage(message);
        response.setPath(request.getServletPath());
        response.setTimestamp(OffsetDateTime.now());
        return response;
    }

    private ResponseEntity<?> mapToSseResponse(ErrorResponseJson response) {
        String jsonData;
        try {
            jsonData = new ObjectMapper().writeValueAsString(response);
        } catch (Exception e) {
            jsonData = "{\"message\":\"Internal Error\"}";
        }

        String sseFormatted = "event: error\ndata: " + jsonData + "\n\n";
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(sseFormatted);
    }
}