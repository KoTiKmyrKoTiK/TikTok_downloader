package org.example.formatter;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
@Slf4j
public class TiktokLinkFormatter {

    public String format(String link) {
        return getIdFromLink(link);
    }

    private String getIdFromLink(String shareLink) {
        var clearLink = "";
        if (shareLink.matches("https://vm\\.tiktok\\.com/[A-Za-z0-9]+")) {
            log.info("LINK RESPONDS TO MOBILE DEVICE LINK PATTERN");
            try {
                clearLink = makeAndroidLink(shareLink);
            } catch (IOException e) {
                throw new RuntimeException("An error has occurred while", e);
            }
        } else if (shareLink.matches("https://www\\.tiktok\\.com/@([A-Za-z0-9]+(\\.[A-Za-z0-9]+)+)/video/[0-9]+\\?([A-Za-z]+(_[A-Za-z]+)+)=[0-9]+")
                || shareLink.matches("https://www\\.tiktok\\.com/@[A-Za-z0-9]+/video/[0-9]+\\?([A-Za-z]+(_[A-Za-z]+)+)=[0-9]+&sender_device=[A-Za-z]+")
                || shareLink.matches("https://www\\.tiktok\\.com/@([A-Za-z0-9]+(_[A-Za-z0-9]+)+)/video/[0-9]+\\?([A-Za-z]+(_[A-Za-z]+)+)=1&([A-Za-z]+(_[A-Za-z]+)+)=[A-Za-z]+&([A-Za-z]+(_[A-Za-z]+)+)=[0-9]+")
                || shareLink.matches("https://www\\.tiktok\\.com/@([A-Za-z0-9]+(\\.[A-Za-z0-9]+)+)/video/[0-9]+\\?([A-Za-z]+(_[A-Za-z]+)+)=1&([A-Za-z]+(_[A-Za-z]+)+)=[A-Za-z]+")) {
            clearLink = makePCLink(shareLink);
        } else if (shareLink.matches("https://www\\.tiktok\\.com/@[A-Za-z0-9]+/video/[0-9]+")) {
            clearLink = shareLink;
        }

        return clearLink.split("/")[clearLink.split("/").length - 1];
    }

    private String makePCLink(String link) {
        System.out.println(link.split("\\?")[0]);
        return link.split("\\?")[0];
    }

    private String makeAndroidLink(String link) throws IOException {
        var doc = Jsoup.connect(link)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .timeout(0)
                .get();


        var elements = doc.select("html > head > link").attr("rel", "canonical");


        String gottenLink = "";
        for (Element element : elements) {
            if (
//                    element.attr("href").matches("https://www\\.tiktok\\.com/@([A-Za-z0-9]+(_[A-Za-z0-9]+)+)/video/[0-9]+")
//                            || element.attr("href").matches("https://www\\.tiktok\\.com/@\\.([A-Za-z0-9]+(/[A-Za-z0-9]+)+)")
//                            || element.attr("href").matches("https://www\\.tiktok\\.com/@[A-Za-z0-9]+/video/[0-9]+")
                    element.attr("href").matches("https://www\\.tiktok\\.com/@([A-Za-z0-9]+(\\.[A-Za-z0-9]+)+)/video/[0-9]+")
            ) {
                gottenLink = element.attr("href");
                System.out.println("GOTTEN LINK:" +gottenLink);
                break;
            }
        }
        return gottenLink;
    }

}
