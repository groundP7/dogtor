package com.ground.dogtor.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UniqueFileNameGenarator {
    public static String generateUniqueFileName(long userId, String extension) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return userId + "_" + timestamp + "_" + System.nanoTime() + extension;
    }
}
