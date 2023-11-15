package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AniaHtmlParser {

    public String parse(String html) {
        String fixedHtml = html.replaceAll("\\\"", "\"");
        Document parsedDocument = Jsoup.parse(fixedHtml);
        List<String> ingredients = parseIngredients(parsedDocument.getElementsByAttributeValue("itemprop", "recipeIngredient"));
        Map<String, String> basicInfoMap = parseBasicInfo(parsedDocument);
        String methodology = parseMethodology(parsedDocument);
        String content = parseContent(parsedDocument);
        return null;
    }

    private String parseContent(Document parsedDocument) {
        String text = parsedDocument.text();
        String h2 = parsedDocument.getElementsByTag("h2").text();
        String h3 = parsedDocument.getElementsByTag("h3").text();
        String[] split = text.split(h2);
        String[] split2 = split[1].split(h3);
        return split2[0];
    }

    private String parseMethodology(Document parsedDocument) {
        String text = parsedDocument.text();
        String h2 = parsedDocument.getElementsByTag("h2").text();
        String h3 = parsedDocument.getElementsByTag("h3").text();
        String[] split = text.split(h2);
        String[] split2 = split[1].split(h3);
        return split2[1];
    }

    private Map<String, String> parseBasicInfo(Document parsedDocument) {
        return Arrays.stream(parseIngredients(parsedDocument.getElementsByClass("recipe-info"))
                        .get(0)
                        .split("\\\\n"))
                .filter(Objects::nonNull)
                .filter(value -> !Strings.isBlank(value))
                .map(String::trim)
                .map(this::splitKeyAndValue)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<String> parseIngredients(Elements parsedDocument) {
        return parsedDocument
                .stream()
                .map(Element::text)
                .toList();
    }

    private Map.Entry<String, String> splitKeyAndValue(String recipeInfoElement) {
        String[] keyAndValue = recipeInfoElement.split(":");
        String key = keyAndValue[0].trim();
        String value = keyAndValue[1].trim();
        return Map.entry(key, value);
    }
}
