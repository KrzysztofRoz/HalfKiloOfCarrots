package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class AniaServiceScheduler {

    private final AniaScheduler aniaScheduler;
    private final AniaProxy proxy;
    private final AniaRecipeHtmlParser parser;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 10000000)
    public void fetchUpdatedRecipes() throws IOException {
        String slugsString = new String(Files.readAllBytes(Paths.get("C:\\Users\\Brungold\\Desktop\\JAVA\\NewHalfKiloOfCarrots\\HalfKiloOfCarrots\\RecipePuller\\src\\main\\resources\\SlugErrorJson.json")));
        Set<String> recipes = objectMapper.readValue(slugsString, new TypeReference<Set<String>>() {
        });
//        log.info("" + recipes);
        List<RecipeData> allRecipes = getAllRecipes(recipes);
        String recipesJson = objectMapper.writeValueAsString(allRecipes);
        //TODO fix methodology - make it optional (breaks on kotleciki-z-sezamem-i-mizuna)
        log.info("Fetched from fetchUpdatedRecipes: " + recipesJson);
    }

    @SneakyThrows
    private List<RecipeData> getAllRecipes(Set<String> slugsSet)  {
        Set<String> errorSlugs = new HashSet<>();
        List<RecipeData> recipeData = slugsSet
                .stream()
                .map(s -> {
                    sleep();
                    return proxy.getRecipe(s);
                })
                .map(dto -> {
                    try {
                        return parser.parse(dto);
                    } catch (RuntimeException exception) {
                        exception.printStackTrace();
                        errorSlugs.add(dto.slug());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();
        String errorSlugsJson = objectMapper.writeValueAsString(errorSlugs);
        log.info(errorSlugsJson);
        return recipeData;
    }
    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
