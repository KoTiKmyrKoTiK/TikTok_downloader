package org.example.service;


import org.example.config.BotConfig;
import org.example.downloader.TiktokVideoDownloader;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.*;

@Component
public class BOT extends TelegramLongPollingBot {
    @Autowired
    BotConfig config = new BotConfig();
    String defaultPath = "downloads\\video%d_count=%d.mp4";

    TiktokVideoDownloader downloader = new TiktokVideoDownloader();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var chatId = update.getMessage().getChatId();
            var text = update.getMessage().getText();
            if (text.contains("https") && text.contains("tiktok.com")) {
                downloader.downloadVideo(text, chatId);
                send(chatId);
            }
        }
    }

    private void send(long chatId) {
        var inputFile = new InputFile();
        inputFile.setMedia(new File(String.format(defaultPath, chatId, downloader.getCount(chatId))));

        SendVideo video = new SendVideo();
        video.setChatId(chatId);
        video.setVideo(inputFile);

        try {
            execute(video);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //IN development
    public void getPhotos(String link) throws IOException {
        var doc = Jsoup.connect(link)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .timeout(0)
                .get();

        var links = doc.select("ul> slide__list > flex-container");
        System.out.println(links);
    }


    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
