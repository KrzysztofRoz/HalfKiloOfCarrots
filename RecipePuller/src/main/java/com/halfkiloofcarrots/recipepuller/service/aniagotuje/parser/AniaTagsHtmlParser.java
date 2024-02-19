package com.halfkiloofcarrots.recipepuller.service.aniagotuje.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

@Deprecated
public class AniaTagsHtmlParser {

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
