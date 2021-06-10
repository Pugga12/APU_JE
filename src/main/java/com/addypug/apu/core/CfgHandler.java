package com.addypug.apu.core;

import io.github.cdimascio.dotenv.Dotenv;


public class CfgHandler {
    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}
