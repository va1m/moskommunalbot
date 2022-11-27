package com.va1m.moskommunalbot;

import java.net.URI;

/** Provides the application settings */
public class Settings {

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
     * Returns database connection string read from system property.
     * @throws IllegalStateException if the parameters are not found
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String getDbUrl() {

        // postgres://usr:psw@host:port/path
        final var dbUrl = System.getProperty("dburl");
        if (dbUrl == null) {
            throw new IllegalStateException("DB connection URL is not found. "
                + "Please set it up in the system property 'dburl' in the format 'jdbc:sqlite:/path/to/moskommunalbot.sqlite'.");
        }

        // Throw an exception if dbUrl is invalid
        URI.create(dbUrl);

        return dbUrl;
    }
}
