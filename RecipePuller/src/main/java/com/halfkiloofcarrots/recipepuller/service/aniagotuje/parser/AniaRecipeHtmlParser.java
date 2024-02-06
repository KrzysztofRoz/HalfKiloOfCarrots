package com.halfkiloofcarrots.recipepuller.service.aniagotuje.parser;

import com.halfkiloofcarrots.recipepuller.model.dto.RecipeContentStep;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import com.halfkiloofcarrots.recipepuller.model.dto.record.RecipeDataDTO;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        List<RecipeContentStep> contentSteps = parseContentSteps(parsedDocument);
        return RecipeData.builder()
                .slug(DTO.slug())
                .ingredients(ingredients)
                .basicInfoMap(basicInfoMap)
                .methodology(methodology)
                .contentSteps(contentSteps)
                .build();
    }

    private String parseMethodology(Document parsedDocument) {
        String[] methodologyAndContentArray = parseMethodologyAndContentFromHtml(parsedDocument);
        if (methodologyAndContentArray.length < 2) {
            return "";
        } else {
            return Jsoup.parse(methodologyAndContentArray[0]).text();
        }
    }

    private List<RecipeContentStep> parseContentSteps(Document parsedDocument) {
        List<RecipeContentStep> parsedNewContent = parseNewContent(parsedDocument);
        if (!CollectionUtils.isEmpty(parsedNewContent)) {
            return parsedNewContent;
        }
        return parseOldContent(parsedDocument);
    }

    private Map<String, String> parseBasicInfo(Document parsedDocument) {
        List<String> ingredients = Arrays.asList(Jsoup.parse(parsedDocument.getElementsByClass("recipe-info").toString().replace("<br>", "#_#")).getElementsByClass("recipe-info").text().split("#_#"));
        if (ingredients.isEmpty()) {
            return Collections.emptyMap();
        }
        return ingredients.stream()
                .filter(Objects::nonNull)
                .filter(value -> !Strings.isBlank(value))
                .map(String::trim)
                .map(this::splitKeyAndValue)
                .filter(entry -> !"Error".equals(entry.getKey()))
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
        if (keyAndValue.length < 2) {
            return Map.entry("Error", recipeInfoElement);
        }
        String key = keyAndValue[0].trim();
        String value = keyAndValue[1].trim();
        return Map.entry(key, value);
    }

    private List<RecipeContentStep> parseNewContent(Document parsedDocument) {
        if (parsedDocument.getElementsByClass("steps").isEmpty()) {
            return Collections.emptyList();
        }
        return parsedDocument.getElementsByClass("step")
                .stream()
                .map(this::parseSingleContentStep)
                .toList();
    }

    private RecipeContentStep parseSingleContentStep(Element element) {
        String title = element.getElementsByClass("step-name").first().text();
        String instruction = element.getElementsByClass("step-text").first().text();
        List<String> images = parseImages(element);
        return RecipeContentStep.builder()
                .title(title)
                .instruction(instruction)
                .images(images)
                .build();
    }

    private List<String> parseImages(Element element) {
        return element.getElementsByTag("img").stream()
                .map(e -> e.attr("src"))
                .toList();
    }

    private List<RecipeContentStep> parseOldContent(Document parsedDocument) {
        String[] methodologyAndContentArray = parseMethodologyAndContentFromHtml(parsedDocument);
        if (methodologyAndContentArray.length < 2) {
            return Collections.emptyList();
        } else {
            String instruction = Jsoup.parse(methodologyAndContentArray[1]).text();
            RecipeContentStep contentStep = RecipeContentStep.builder().instruction(instruction).build();
            return Collections.singletonList(contentStep);
        }
    }

    //TODO handle content in html without h3 tags (pasztet-jaglany-z-warzywami)
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
