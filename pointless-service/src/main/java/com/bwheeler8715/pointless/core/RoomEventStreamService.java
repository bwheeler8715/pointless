package com.bwheeler8715.pointless.core;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface RoomEventStreamService {

    SseEmitter register(String roomId);

    void removeEmitter(String roomId, SseEmitter emitter);

    void broadcastUpdate(String roomId);
}