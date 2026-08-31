package com.family_management_system.config_server.config;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvConfig {
    private static final String[] KEYS = {
            "JWT_SECRET_KEY",
            "JWT_EXPIRATION",
            "FRONTEND_URL",
            "DB_URL",
            "DB_USERNAME",
            "DB_PASSWORD",
            "EMAIL_HOST",
            "EMAIL_PORT",
            "EMAIL_USERNAME",
            "EMAIL_PASSWORD",
            "KAFKA_BOOTSTRAP_SERVERS"
    };

    private EnvConfig() {
    }

    public static void load() {
        for (String directory : new String[]{".", ".."}) {
            Dotenv dotenv = Dotenv.configure()
                    .directory(directory)
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            for (String key : KEYS) {
                String value = dotenv.get(key);
                if (value != null && System.getenv(key) == null && System.getProperty(key) == null) {
                    System.setProperty(key, value);
                }
            }
        }
    }
}
