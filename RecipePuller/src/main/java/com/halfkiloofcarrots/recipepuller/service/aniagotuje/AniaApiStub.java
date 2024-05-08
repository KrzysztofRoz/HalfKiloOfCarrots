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
import com.halfkiloofcarrots.recipepuller.repository.BasicInfoRepository;
import com.halfkiloofcarrots.recipepuller.repository.ContentStepRepository;
import com.halfkiloofcarrots.recipepuller.repository.HeaderImageRepository;
import com.halfkiloofcarrots.recipepuller.repository.IngredientRepository;
import com.halfkiloofcarrots.recipepuller.repository.RecipeDataRepository;
import jakarta.transaction.Transactional;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class AniaApiStub {

    private final FetchRecipeService recipeService;
    private final FetchSlugsService slugsService;
    private final RecipeDataRepository recipeDataRepository;
    private final BasicInfoRepository basicInfoRepository;
    private final ContentStepRepository contentStepRepository;
    private final HeaderImageRepository headerImageRepository;
    private final IngredientRepository ingredientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 10000000)

    public void fetchUpdatedRecipes() throws IOException {

        Set<String> strings = slugsService.fetchSlugs();
        String slugsString = objectMapper.writeValueAsString(strings);
//        String slugsString = loadSlugs("Slugs.json");
        Set<String> slugs = objectMapper.readValue(slugsString, new TypeReference<>() {
        });
        List<RecipeData> recipes = recipeService.fetchRecipe(slugs);
        recipes.forEach(this::createOrUpdate);
        String recipesJson = objectMapper.writeValueAsString(recipes);
        log.info("Fetched from fetchUpdatedRecipes: " + recipesJson);

        clearOldEntities();
    }


    void clearOldEntities() {
        basicInfoRepository.removeByRecipeDataIdIsNull();
        contentStepRepository.removeByRecipeDataIdIsNull();
        headerImageRepository.removeByRecipeDataIdIsNull();
        ingredientRepository.removeByRecipeDataIdIsNull();
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
            Long recipeDataId = entity.get().getId();
            List<HeaderImageEntity> headerImages = getHeaderImages(recipeData, recipeDataId);
            List<IngredientEntity> ingredients = getIngredients(recipeData, recipeDataId);
            List<BasicInfoEntity> basicInfoList = getBasicInfoList(recipeData, recipeDataId);
            List<ContentStepEntity> contentSteps = getContentSteps(recipeData, recipeDataId);

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
                .headerImages(getHeaderImages(recipeData, null))
                .ingredients(getIngredients(recipeData, null))
                .basicInfoList(getBasicInfoList(recipeData, null))
                .contentSteps(getContentSteps(recipeData, null))
                .build();
    }

    private List<HeaderImageEntity> getHeaderImages(RecipeData recipeData, Long recipeDataId) {
        return recipeData.getHeader().getImages().stream()
                .map(imageSource -> HeaderImageEntity.builder()
                        .recipeDataId(recipeDataId)
                        .imageSource(imageSource)
                        .build())
                .toList();
    }

    private List<IngredientEntity> getIngredients(RecipeData recipeData, Long recipeDataId) {
        return recipeData.getIngredients().stream()
                .map(ingredient -> IngredientEntity.builder()
                        .recipeDataId(recipeDataId)
                        .value(ingredient)
                        .build())
                .toList();
    }

    private List<BasicInfoEntity> getBasicInfoList(RecipeData recipeData, Long recipeDataId) {
        return recipeData.getBasicInfoMap().entrySet().stream()
                .map(entry -> BasicInfoEntity.builder()
                        .recipeDataId(recipeDataId)
                        .label(entry.getKey())
                        .value(entry.getValue())
                        .build())
                .toList();
    }

    private List<ContentStepEntity> getContentSteps(RecipeData recipeData, Long recipeDataId) {
        return recipeData.getContentSteps().stream()
                .map(contentStep -> {
                    List<ContentStepImageEntity> contentStepImages = Optional.ofNullable(contentStep.getImages())
                            .orElse(Collections.emptyList()).stream()
                            .map(imageSource -> ContentStepImageEntity.builder()
                                    .imageSource(imageSource)
                                    .build())
                            .toList();
                    return ContentStepEntity.builder()
                            .recipeDataId(recipeDataId)
                            .title(contentStep.getTitle())
                            .instruction(contentStep.getInstruction())
                            .contentStepImages(contentStepImages)
                            .build();
                })
                .toList();
    }
}
