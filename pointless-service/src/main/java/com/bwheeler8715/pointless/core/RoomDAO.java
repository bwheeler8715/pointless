package com.bwheeler8715.pointless.core;

import com.bwheeler8715.pointless.domain.Room;

import java.util.function.Consumer;

public interface RoomDAO {

    Room getRoomById(String id);

    Room findRoomByName(String name);

    void createRoom(Room room);

    void updateRoom(String id, Consumer<Room> updater);
}