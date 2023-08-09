package org.example.downloader;

import lombok.extern.log4j.Log4j2;
import org.example.custom.DefaultHashMap;
import org.example.formatter.TiktokLinkFormatter;

import java.io.*;

import java.net.URL;
import java.util.Map;

@Log4j2
public class TiktokVideoDownloader {
    TiktokLinkFormatter formatter = new TiktokLinkFormatter();
    String patternLink = "https://tikcdn.io/ssstik/";
    String defaultPath = "downloads\\video";

    Map<Long, Integer> counts = new DefaultHashMap<>(1);

    public TiktokVideoDownloader() {

    }
    public void downloadVideo(String shareLink, long chatId) {
        counts.put(chatId, counts.get(chatId) + 1);


        String downloadLink = formatter.format(shareLink);
        String id = downloadLink.split("/")[downloadLink.split("/").length - 1];

        File downloadFile = createDownloadFile(chatId, counts.get(chatId));

        try (InputStream inputStream = new URL(patternLink + id).openStream();
             OutputStream outputStream = new FileOutputStream(downloadFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("Video was not downloaded");
            throw new RuntimeException(e);
        }
        log.info("Video downloaded successfully");
    }

    private File createDownloadFile(long chatId, long count) {
        String fileName = String.format("%d_count=%d.mp4", chatId, count);
        return new File(defaultPath + fileName);
    }

    public int getCount(long chatId) {
        return counts.get(chatId);
    }
}
