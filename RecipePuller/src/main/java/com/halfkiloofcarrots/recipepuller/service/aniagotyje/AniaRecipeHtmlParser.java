package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AniaRecipeHtmlParser {

    public RecipeData parse(String html) {
        String fixedHtml = html.replace("\\\"", "\"");
        Document parsedDocument = Jsoup.parse(fixedHtml);
        List<String> ingredients = parseIngredients(parsedDocument.getElementsByAttributeValue("itemprop", "recipeIngredient"));
        Map<String, String> basicInfoMap = parseBasicInfo(parsedDocument);
        String methodology = parseMethodology(parsedDocument);
        String content = parseContent(parsedDocument);
        return RecipeData.builder()
                .ingredients(ingredients)
                .basicInfoMap(basicInfoMap)
                .methodology(methodology)
                .content(content)
                .build();
    }

    private String parseMethodology(Document parsedDocument) {
        String[] methodologyAndContentArray = parseMethodologyAndContentFromHtml(parsedDocument);
        return Jsoup.parse(methodologyAndContentArray[0]).text();
    }

    private String parseContent(Document parsedDocument) {
        String[] methodologyAndContentArray = parseMethodologyAndContentFromHtml(parsedDocument);
        return  Jsoup.parse(methodologyAndContentArray[1]).text();
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

    private String[] parseMethodologyAndContentFromHtml(Document parsedDocument) {
        String html = parsedDocument.toString();
        String h2 = parsedDocument.getElementsByTag("h2").toString();
        String h3 = parsedDocument.getElementsByTag("h3").toString();
        String methodologyAndContentHtml = html.split(h2)[1];
        return methodologyAndContentHtml.split(h3);
    }
}
