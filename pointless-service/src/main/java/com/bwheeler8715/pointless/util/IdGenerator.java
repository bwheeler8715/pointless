package com.bwheeler8715.pointless.util;

import java.security.SecureRandom;
import java.util.Random;

public final class IdGenerator {

    private static final String ALPHABET = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int ID_LENGTH = 15;
    private static final Random RANDOM = new SecureRandom();

    public static String generateId() {
        return generateId(ID_LENGTH);
    }

    public static String generateId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}