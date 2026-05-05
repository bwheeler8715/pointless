package com.bwheeler8715.pointless.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdGeneratorTest {

    @Test
    void testGenerateIdDefaultLength() {
        String id = IdGenerator.generateId();
        assertNotNull(id);
        assertEquals(15, id.length());
    }

    @Test
    void testGenerateIdCustomLength() {
        int length = 10;
        String id = IdGenerator.generateId(length);
        assertNotNull(id);
        assertEquals(length, id.length());
    }

    @Test
    void testGenerateIdUniqueness() {
        String id1 = IdGenerator.generateId();
        String id2 = IdGenerator.generateId();
        assertNotEquals(id1, id2);
    }

    @Test
    void testGenerateIdAllowedCharacters() {
        String id = IdGenerator.generateId(100);
        String allowed = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (char c : id.toCharArray()) {
            assertTrue(allowed.indexOf(c) != -1, "Character " + c + " is not allowed");
        }
    }
}
