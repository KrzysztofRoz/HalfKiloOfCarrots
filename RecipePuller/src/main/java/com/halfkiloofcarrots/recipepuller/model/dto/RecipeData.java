package com.halfkiloofcarrots.recipepuller.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeData {

    private String slug;
    private List<String> ingredients;
    private Map<String, String> basicInfoMap;
    private String methodology;
    private List<RecipeContentStep> contentSteps;
}
