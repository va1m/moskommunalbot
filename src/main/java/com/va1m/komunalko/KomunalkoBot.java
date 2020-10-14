package com.va1m.komunalko;

import com.va1m.komunalko.services.ColdWaterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

/** Contains the bot's logic */
public class KomunalkoBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(KomunalkoBot.class);

    private final ColdWaterService coldWaterService;

    private final String token;

    /** Constructor */
    public KomunalkoBot(String token) {
        this.token = token;

        // TODO: use injection
        coldWaterService = new ColdWaterService();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            // TODO: Calc parts of the message for cold, hot, disposed water and for electricity
            final var coldWaterCalculationText = coldWaterService.calc(LocalDate.now(), 100);

            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                LOGGER.error("", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "komunalkobot";
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
