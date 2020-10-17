package com.va1m.moskommunalbot.interaction.processors;

/** Exception for the case when user enters invalid data */
public class InvalidInputException extends RuntimeException {

    /** Constructor with error message */
    public InvalidInputException(String msg) {
        super(msg);
    }
}
