package com.va1m.moskommunalbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a message sending to user.
 * The message can have text and buttons displaying under input field in messenger.
 */
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
@Getter
public class InteractionMessage {

    /** Message text */
    private final String text;

    /** Texts for buttons */
    private String[] buttonTexts;
}
