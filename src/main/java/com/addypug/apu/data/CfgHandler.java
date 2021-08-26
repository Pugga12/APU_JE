package com.addypug.apu.data;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.annotations.DeprecatedSince;
import net.dv8tion.jda.annotations.ForRemoval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CfgHandler {
    static Logger logger = LoggerFactory.getLogger(CfgHandler.class);
    private static final Dotenv dotenv = Dotenv.load();
    public static String valString(String key) {
        logger.debug("Retrieving String value from .env file with key " + key);
        return dotenv.get(key);
    }
    public static Boolean valBool(String key) {
        logger.debug("Retrieving Boolean value from .env file with key " + key);
        return Boolean.parseBoolean(dotenv.get(key));
    }
    public static Integer valInt(String key) {
        logger.debug("Retrieving Integer value from .env file with key " + key);
        return Integer.parseInt(dotenv.get(key));
    }
}