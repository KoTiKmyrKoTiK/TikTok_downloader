package org.example.config;


import org.example.service.BOT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    @Autowired
    BOT bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        var botApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            botApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}