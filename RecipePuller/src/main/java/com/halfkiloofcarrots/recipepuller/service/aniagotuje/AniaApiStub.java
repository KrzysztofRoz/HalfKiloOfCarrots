package com.halfkiloofcarrots.recipepuller.service.aniagotuje;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import com.halfkiloofcarrots.recipepuller.model.entity.BasicInfoEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.ContentStepEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.ContentStepImageEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.HeaderImageEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.IngredientEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.RecipeDataEntity;
import com.halfkiloofcarrots.recipepuller.repository.RecipeDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AniaApiStub {

    private final FetchRecipeService recipeService;
    private final FetchSlugsService slugsService;
    private final RecipeDataRepository recipeDataRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 10000000)
    public void fetchUpdatedRecipes() throws IOException {

//        Set<String> strings = slugsService.fetchSlugs();
//        String jsonSlug = objectMapper.writeValueAsString(strings);
        String slugsString = loadSlugs("SlugErrorJson.json");
        Set<String> slugs = objectMapper.readValue(slugsString, new TypeReference<>() {
        });
        List<RecipeData> recipes = recipeService.fetchRecipe(slugs);
        recipes.forEach(this::createOrUpdate);
        String recipesJson = objectMapper.writeValueAsString(recipes);
        log.info("Fetched from fetchUpdatedRecipes: " + recipesJson);
    }

    @SneakyThrows
    public String loadSlugs(String json) {
        return new ClassPathResource(json)
                .getContentAsString(Charset.defaultCharset());
    }

    // TODO move this private methods to separate class
    private void createOrUpdate(RecipeData recipeData) {
        Optional<RecipeDataEntity> entity = recipeDataRepository.findBySlug(recipeData.getSlug());
        if (entity.isPresent()) {
            List<HeaderImageEntity> headerImages = getHeaderImages(recipeData);
            List<IngredientEntity> ingredients = getIngredients(recipeData);
            List<BasicInfoEntity> basicInfoList = getBasicInfoList(recipeData);
            List<ContentStepEntity> contentSteps = getContentSteps(recipeData);

            RecipeDataEntity toUpdateEntity = entity.get();
            toUpdateEntity.setRecipeHeaderTitle(recipeData.getHeader().getTitle());
            toUpdateEntity.setHeaderImages(headerImages);
            toUpdateEntity.setIngredients(ingredients);
            toUpdateEntity.setBasicInfoList(basicInfoList);
            toUpdateEntity.setContentSteps(contentSteps);
            recipeDataRepository.save(toUpdateEntity);
        } else {
            RecipeDataEntity newEntity = toEntity(recipeData);
            recipeDataRepository.save(newEntity);
        }
    }

    private RecipeDataEntity toEntity(RecipeData recipeData) {
        return RecipeDataEntity.builder()
                .slug(recipeData.getSlug())
                .recipeHeaderTitle(recipeData.getHeader().getTitle())
                .methodology(recipeData.getMethodology())
                .headerImages(getHeaderImages(recipeData))
                .ingredients(getIngredients(recipeData))
                .basicInfoList(getBasicInfoList(recipeData))
                .contentSteps(getContentSteps(recipeData))
                .build();
    }

    private List<HeaderImageEntity> getHeaderImages(RecipeData recipeData) {
        return recipeData.getHeader().getImages().stream()
                .map(imageSource -> HeaderImageEntity.builder().imageSource(imageSource).build()).toList();
    }

    private List<IngredientEntity> getIngredients(RecipeData recipeData) {
        return recipeData.getIngredients().stream()
                .map(ingredient -> IngredientEntity.builder().value(ingredient).build()).toList();
    }

    private List<BasicInfoEntity> getBasicInfoList(RecipeData recipeData) {
        return recipeData.getBasicInfoMap().entrySet().stream()
                .map(entry -> BasicInfoEntity.builder().label(entry.getKey()).value(entry.getValue()).build()).toList();
    }

    private List<ContentStepEntity> getContentSteps(RecipeData recipeData) {
        return recipeData.getContentSteps().stream()
                .map(contentStep -> {
                    List<ContentStepImageEntity> contentStepImages = Optional.ofNullable(contentStep.getImages()).orElse(Collections.emptyList()).stream()
                            .map(imageSource -> ContentStepImageEntity.builder().imageSource(imageSource).build()).toList();
                    return ContentStepEntity.builder()
                            .title(contentStep.getTitle())
                            .instruction(contentStep.getInstruction())
                            .contentStepImages(contentStepImages)
                            .build();
                })
                .toList();
    }
}
