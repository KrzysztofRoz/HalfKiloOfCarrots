package com.halfkiloofcarrots.recipepuller.service.aniagotuje;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class AniaApiStub {

    private final FetchRecipeService recipeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 10000000)
    public void fetchUpdatedRecipes() throws IOException {
        String slugsString = loadSlugs("SlugErrorJson.json");
        Set<String> slugs = objectMapper.readValue(slugsString, new TypeReference<>() {
        });
        List<RecipeData> recipes = recipeService.fetchRecipe(slugs);
        String recipesJson = objectMapper.writeValueAsString(recipes);
        log.info("Fetched from fetchUpdatedRecipes: " + recipesJson);
    }

    @SneakyThrows
    public String loadSlugs(String json) {
        return new ClassPathResource(json)
                .getContentAsString(Charset.defaultCharset());
    }
}
