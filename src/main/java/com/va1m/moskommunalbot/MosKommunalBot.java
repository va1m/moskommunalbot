package com.va1m.moskommunalbot;

import static java.util.Optional.ofNullable;

import com.va1m.moskommunalbot.interaction.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

/** Contains the bot's logic */
@Singleton
public class MosKommunalBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MosKommunalBot.class);

    private final InteractionService interactionService;
    private final Settings settings;

    /** Constructor */
    @Inject
    public MosKommunalBot(InteractionService interactionService, Settings settings) {
        this.interactionService = interactionService;
        this.settings = settings;
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        final var inputMessage = update.getMessage();
        if (!update.hasMessage() || !inputMessage.hasText()) {
            return;
        }

        final var user = ofNullable(inputMessage.getFrom())
            .map(User::getUserName)
            .orElse("anonymous");

        final var chatId = inputMessage.getChatId();
        final var input = inputMessage.getText();

        final var msg = interactionService.processState(chatId, input);

        LOGGER.trace("{} says: '{}', query: '{}'", user, input, msg.getText());

        final var outputMessage = SendMessage.builder()
            .chatId(chatId.toString())
            .parseMode(ParseMode.MARKDOWN)
            .disableWebPagePreview(true)
            .text(msg.getText())
            .replyMarkup(buildButtons(msg.getButtonTexts()))
            .build();

        try {
            // Call method to send the message
            execute(outputMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Couldn't ask user {}", user, e);
        }
    }

    private static ReplyKeyboard buildButtons(String[] texts) {
        if (texts == null || texts.length == 0) {
            // Hide buttons from a previous step if buttons for current step are not needed
            return ReplyKeyboardRemove.builder()
                .removeKeyboard(true)
                .build();
        }

        final var row = Stream.of(texts)
            .map(KeyboardButton::new)
            .collect(KeyboardRow::new, KeyboardRow::add, KeyboardRow::addAll);

        // Create a keyboard with buttons
        return ReplyKeyboardMarkup.builder()
            .selective(true)
            .resizeKeyboard(true)
            .selective(true)
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .keyboard(List.of(row))
            .build();
    }

    @Override
    public String getBotUsername() {
        return "moskommunalbot";
    }

    @Override
    public String getBotToken() {
        return settings.getToken();
    }
}
