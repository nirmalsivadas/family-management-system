package com.family_management_system.family_service.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
  static {
    Dotenv dotenv = Dotenv.configure()
        .ignoreIfMissing()
        .load();

    // Load environment variables from .env file
    if (dotenv != null) {
      String[] keys = { "JWT_SECRET_KEY", "JWT_EXPIRATION", "FRONTEND_URL", "DB_URL", "DB_USERNAME", "DB_PASSWORD" };
      for (String key : keys) {
        String value = dotenv.get(key);
        if (value != null && System.getenv(key) == null) {
          System.setProperty(key, value);
        }
      }
    }
  }
}
