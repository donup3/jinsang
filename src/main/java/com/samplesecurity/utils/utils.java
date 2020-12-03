package com.samplesecurity.utils;

import java.util.UUID;

public class utils {
    public static String UUIDGenerator() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
