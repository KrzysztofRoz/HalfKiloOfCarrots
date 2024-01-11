package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeDataDTO;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AniaRecipeHtmlParser {

    public RecipeData parse(RecipeDataDTO DTO) {
        String fixedHtml = DTO.body().replace("\\\"", "\"");
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
        if(methodologyAndContentArray.length < 2) {
            return "";
        } else {
            return Jsoup.parse(methodologyAndContentArray[0]).text();
        }
    }

    private String parseContent(Document parsedDocument) {
        String[] methodologyAndContentArray = parseMethodologyAndContentFromHtml(parsedDocument);
        if(methodologyAndContentArray.length < 2) {
            return "";
        } else {
            return Jsoup.parse(methodologyAndContentArray[1]).text();
        }
    }

    private Map<String, String> parseBasicInfo(Document parsedDocument) {
        List<String> ingredients = parseIngredients(parsedDocument.getElementsByClass("recipe-info"));
        if(ingredients.isEmpty()){
            return Collections.emptyMap();
        }
        return Arrays.stream(ingredients
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
        String[] methodologyAndContentHtml = html.split(h2);
        if (methodologyAndContentHtml.length < 2) {
            return new String[]{};
        }
        return methodologyAndContentHtml[1].split(h3);
    }
}
