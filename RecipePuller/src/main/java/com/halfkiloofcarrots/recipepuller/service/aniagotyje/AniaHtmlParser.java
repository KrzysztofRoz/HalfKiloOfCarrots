package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class AniaHtmlParser {

    public String parse(String html) {
        String fixedHtml = html.replaceAll("\\\"", "\"");
        Document parse = Jsoup.parse(fixedHtml);

        List<String> ingredients = parse.getElementsByAttributeValue("itemprop", "recipeIngredient")
                .stream()
                .map(Element::text)
                .toList();

        return null;
    }
}
