package com.halfkiloofcarrots.recipepuller.service.aniagotyje;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import com.halfkiloofcarrots.recipepuller.model.dto.TagDto;
import com.halfkiloofcarrots.recipepuller.model.dto.TagListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AniaScheduler {

    private final AniaProxy proxy;
    private final AniaRecipeHtmlParser parser;
    private ObjectMapper objectMapper = new ObjectMapper();

    //  TODO
    // map to Json String list -> zapisać do pliku i nie strzelać za każdym razem

    // strzał po przepisy
//    @Scheduled(fixedDelay = 10000)
    public void fetchSlugs() throws IOException {
//        RecipeDataDTO obj =proxy.getRecipe("zeberka-w-kapuscie");
//        log.info("Data: {}", obj);
        Document aniaTag = Jsoup.parse(new URL("https://aniagotuje.pl/"), 20000);
        List<String> tagsListAnia = AniaTagsHtmlParser.parseTags(aniaTag.html());
        Set<String> listOfTags = tagsListAnia
                .stream()
                .map(tag -> fetchSlugs(tag))
                .flatMap(List::stream)
                .map(TagDto::slug)
                .collect(Collectors.toSet());
        String slugsJson = objectMapper.writeValueAsString(listOfTags);
        log.info("Tag list: {}", slugsJson);
    }

    @Scheduled(fixedDelay = 10000)
    public void fetchUpdatedRecipes() throws IOException {
        String slugsString = new String(Files.readAllBytes(Paths.get("C:\\Projects\\HalfKiloOfCarrots\\HalfKiloOfCarrots\\RecipePuller\\src\\main\\resources\\SlugsJson.json")));
        Set<String> recipes = objectMapper.readValue(slugsString, new TypeReference<Set<String>>() {
        });
//        log.info("" + recipes);
        List<RecipeData> allRecipes = getAllRecipes(recipes);
        String recipesJson = objectMapper.writeValueAsString(allRecipes);
        //TODO fix methodology - make it optional (breaks on kotleciki-z-sezamem-i-mizuna)
        log.info("" + recipesJson);
    }

    private List<TagDto> fetchSlugs(String tag) {
        Integer pageNumber = 0;
        List<TagDto> tagsList = new ArrayList<>();
        boolean isLast = false;
        while (!isLast) {
            TagListDto dto = proxy.getRecipeByTag(tag, pageNumber++);
            tagsList.addAll(dto.content());
            log.info("Current page slug: {} ", dto.content());
            sleep();
            isLast = dto.last();
        }
        return tagsList;
    }

    private List<RecipeData> getAllRecipes(Set<String> slugsSet) {
        int setLimiter = 10;
        return slugsSet
                .stream()
                .limit(setLimiter)
                .map(s -> {
                    sleep();
                    return proxy.getRecipe(s);
                })
                .map(dto -> parser.parse(dto.body()))
                .toList();
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
