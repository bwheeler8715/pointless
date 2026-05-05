package com.bwheeler8715.pointless.database;

import com.bwheeler8715.pointless.core.InMemoryRoomDAO;
import com.bwheeler8715.pointless.domain.Room;
import com.bwheeler8715.pointless.domain.Voter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class InMemoryRoomDAOTest {

    private InMemoryRoomDAO underTest;

    @BeforeEach
    void setUp() {
        underTest = new InMemoryRoomDAO();
    }

    @Test
    void testPurgeRooms() {
        Room oldRoom = new Room("old", "org1", "Old Room");
        oldRoom.setLastUpdated(OffsetDateTime.now().minusHours(9));
        underTest.createRoom(oldRoom);

        Room newRoom = new Room("new", "org2", "New Room");
        newRoom.setLastUpdated(OffsetDateTime.now().minusHours(7));
        underTest.createRoom(newRoom);

        underTest.purgeRooms();

        assertNull(underTest.getRoomById("old"), "Old room should be purged");
        assertNotNull(underTest.getRoomById("new"), "New room should not be purged");
    }

    @Test
    void testUpdateRoomUpdatesTimestamp() throws InterruptedException {
        Room room = new Room("id1", "org1", "Room 1");
        OffsetDateTime initialLastUpdated = room.getLastUpdated();
        underTest.createRoom(room);

        // Ensure some time passes to distinguish timestamps
        Thread.sleep(10);
        underTest.updateRoom("id1", r -> r.getVoting().setCurrentTickerNumber("T-123"));

        Room updated = underTest.getRoomById("id1");
        assertNotNull(updated);
        // This will likely fail if updateRoom doesn't set lastUpdated
        assertEquals("T-123", updated.getVoting().getCurrentTickerNumber());
        assertNotNull(updated.getLastUpdated());
        org.junit.jupiter.api.Assertions.assertTrue(updated.getLastUpdated().isAfter(initialLastUpdated), "Last updated should be updated");
    }

    @Test
    void testSaveAndGetRoom() {
        Room room = new Room("id1", "org1", "Room 1");
        underTest.createRoom(room);

        Room retrieved = underTest.getRoomById("id1");
        assertNotNull(retrieved);
        assertEquals("Room 1", retrieved.getName());
    }

    @Test
    void testFindRoomByName() {
        Room room1 = new Room("id1", "org1", "Room 1");
        Room room2 = new Room("id2", "org2", "Room 2");
        underTest.createRoom(room1);
        underTest.createRoom(room2);

        Room found = underTest.findRoomByName("Room 2");
        assertNotNull(found);
        assertEquals("id2", found.getId());

        Room notFound = underTest.findRoomByName("Non Existent");
        assertNull(notFound);
    }

    @Test
    void testConcurrentUpdates() throws InterruptedException {
        String roomId = "test-room";
        underTest.createRoom(new Room(roomId, "org", "Test Room"));

        int numThreads = 100;
        int updatesPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < updatesPerThread; j++) {
                        String voterName = "Voter-" + threadId + "-" + j;
                        underTest.updateRoom(roomId, room -> {
                            room.getVoters().add(new Voter("1", voterName, "1"));
                        });
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        Room room = underTest.getRoomById(roomId);
        assertEquals(numThreads * updatesPerThread, room.getVoters().size(), "All concurrent updates should be preserved");
    }

    @Test
    void testUpdateNonExistentRoom() {
        // Should not throw exception and should not create a room
        underTest.updateRoom("non-existent", room -> room.getVoting().setVotingOpen(true));
        assertNull(underTest.getRoomById("non-existent"));
    }
}
