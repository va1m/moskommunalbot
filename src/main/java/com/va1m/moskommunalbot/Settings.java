package com.va1m.moskommunalbot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URI;

/** Provides the application settings */
public class Settings {

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class Db {
        private final String url;
        private final String user;
        private final String psw;
    }

    /**
     * Returns the bot's token read from system property.
     * @throws IllegalStateException if the token is not found
     */
    public String getToken() {
        final var token = System.getProperty("token");
        if (token == null) {
            throw new IllegalStateException("Token is not found. "
                + "Please set it up in the system property 'token'.");
        }
        return token;
    }

    /**
     * Returns database connection parameters read from system property.
     * @throws IllegalStateException if the parameters are not found
     */
    public Db getDbSettings() {
        // postgres://usr:psw@host:port/path
        final var dbUrl = System.getProperty("dburl");
        if (dbUrl == null) {
            throw new IllegalStateException("DB connection URL is not found. "
                + "Please set it up in the system property 'dburl' in the format 'postgresql://usr:psw@host:port/path'.");
        }

        final var dbUri = URI.create(dbUrl);
        final var credentials = dbUri.getUserInfo().split(":");
        var connectionUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        connectionUrl += isNotDebugMode() ? "?sslmode=require" : "";

        return Db.of(connectionUrl, credentials[0], credentials[1]);
    }

    /** Checks if the application has been started with "debug=true" system property */
    private static boolean isNotDebugMode() {
        final var defaultValue = "false";
        final var debug = System.getProperty("debug", defaultValue);
        return debug.equals(defaultValue);
    }
}
