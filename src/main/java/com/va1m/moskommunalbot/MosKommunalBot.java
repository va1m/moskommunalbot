package com.va1m.moskommunalbot;

import static java.util.Optional.ofNullable;

import com.google.inject.Inject;
import com.va1m.moskommunalbot.interaction.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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

/** Contains the bot's logic */
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
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        final var user = ofNullable(update.getMessage().getFrom())
            .map(User::getUserName)
            .orElse("anonymous");

        final var chatId = update.getMessage().getChatId();
        final var input = update.getMessage().getText();

        final var msg = interactionService.processState(chatId, input);

        LOGGER.trace("{} says: '{}', query: '{}'", user, input, msg.getText());

        SendMessage message = new SendMessage()
            .setChatId(chatId)
            .enableMarkdown(true)
            .disableWebPagePreview()
            .setText(msg.getText())
            .setReplyMarkup(buildButtons(msg.getButtonTexts()));

        try {
            // Call method to send the message
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Couldn't ask user {}", user, e);
        }
    }

    private static ReplyKeyboard buildButtons(String[] texts) {
        if (texts == null || texts.length == 0) {
            // Hide buttons from a previous step if for current step buttons are not needed
            return new ReplyKeyboardRemove();
        }

        final var row = Stream.of(texts)
            .map(KeyboardButton::new)
            .collect(KeyboardRow::new, KeyboardRow::add, KeyboardRow::addAll);

        // Create a keyboard with buttons
        return new ReplyKeyboardMarkup()
            .setSelective(true)
            .setResizeKeyboard(true)
            .setOneTimeKeyboard(true)
            .setKeyboard(List.of(row));
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
