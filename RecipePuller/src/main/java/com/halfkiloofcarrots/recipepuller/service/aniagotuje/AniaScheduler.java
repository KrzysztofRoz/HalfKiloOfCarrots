package com.halfkiloofcarrots.recipepuller.service.aniagotuje;


import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AniaScheduler {

    private final FetchSlugsService slugsService;
    private final FetchRecipeService recipeService;

//    @Scheduled(fixedDelay = 10000)
    public void fetchSlugsAndRecipes() {
        Set<String> slugs = slugsService.fetchSlugs();
        List<RecipeData> recipes = recipeService.fetchRecipe(slugs);
    }
}
