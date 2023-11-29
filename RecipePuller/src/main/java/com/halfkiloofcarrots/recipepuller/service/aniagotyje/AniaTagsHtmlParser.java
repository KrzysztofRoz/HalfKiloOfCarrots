package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public class AniaTagsHtmlParser {

    // TODO
    // sprawdzenie czy będzie działą
    public static List<String> parseTags(String html) {
        Document parsedDocument = Jsoup.parse(html);
        return parsedDocument
                .getElementsByClass("tags")
                .get(0).getElementsByAttribute("href")
                .stream()
                .map(element -> element.attr("href"))
                .map(element -> element.replace("/tag/", ""))
                .toList();
    }
}
