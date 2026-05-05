package com.bwheeler8715.pointless.rest;

import com.bwheeler8715.pointless.exception.RoomAlreadyExistsException;
import com.bwheeler8715.pointless.exception.RoomNotFoundException;
import com.bwheeler8715.pointless.exception.UnauthorizedOrganizerException;
import com.bwheeler8715.pointless.exception.VoterNotFoundException;
import com.bwheeler8715.pointless.json.ErrorResponseJson;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestRestControllerAdviceTest {

    private RestControllerAdvice underTest;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        underTest = new RestControllerAdvice();
        request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/test");
    }

    @Test
    void handleRoomAlreadyExistsException() {
        ResponseEntity<ErrorResponseJson> response = (ResponseEntity<ErrorResponseJson>) underTest.handleRoomAlreadyExistsException(new RoomAlreadyExistsException(), request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("A room with that name already exists.", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleRoomNotFoundException() {
        ResponseEntity<ErrorResponseJson> response = (ResponseEntity<ErrorResponseJson>) underTest.handleRoomNotFoundException(new RoomNotFoundException(), request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room not found.", response.getBody().getMessage());
    }

    @Test
    void handleVoterNotFoundException() {
        ResponseEntity<ErrorResponseJson> response = (ResponseEntity<ErrorResponseJson>) underTest.handleVoterNotFoundException(new VoterNotFoundException(), request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Voter not found.", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorizedOrganizerException() {
        ResponseEntity<ErrorResponseJson> response = (ResponseEntity<ErrorResponseJson>) underTest.handleUnauthorizedOrganizerException(new UnauthorizedOrganizerException(), request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("You are not permitted to perform this action.", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgumentException() {
        ResponseEntity<ErrorResponseJson> response = (ResponseEntity<ErrorResponseJson>) underTest.handleIllegalArgumentException(new IllegalArgumentException("Invalid arg"), request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid arg", response.getBody().getMessage());
    }

    @Test
    void handleGeneralException() {
        ResponseEntity<ErrorResponseJson> response = (ResponseEntity<ErrorResponseJson>) underTest.handleException(new Exception("Fail"), request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody().getMessage());
    }
}
