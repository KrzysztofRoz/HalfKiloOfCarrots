package com.halfkiloofcarrots.recipepuller.service.aniagotuje;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import com.halfkiloofcarrots.recipepuller.model.dto.record.RecipeDataDTO;
import com.halfkiloofcarrots.recipepuller.service.aniagotuje.parser.AniaRecipeHtmlParser;
import com.halfkiloofcarrots.recipepuller.util.SleepUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchRecipeService {

    private final AniaProxy proxy;
    private final AniaRecipeHtmlParser parser;

    public List<RecipeData> fetchRecipe(Set<String> slugs) {
        return getAllRecipes(slugs);
    }

    @SneakyThrows
    private List<RecipeData> getAllRecipes(Set<String> slugsSet) {
        return slugsSet
                .stream()
                .peek(slug -> SleepUtil.sleep())
                .map(this::getRecipe)
                .map(this::parseRecipe)
                .filter(Objects::nonNull)
                .toList();
    }

    private RecipeData parseRecipe(RecipeDataDTO recipe) {
        try {
            return parser.parse(recipe);
        } catch (RuntimeException exception) {
            log.error("Parsing error: {}", recipe.slug(), exception);
            return null;
        }
    }

    private RecipeDataDTO getRecipe(String slug) {
        return proxy.getRecipe(slug);
    }
}
