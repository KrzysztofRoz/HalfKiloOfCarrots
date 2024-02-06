package com.halfkiloofcarrots.recipepuller.model.dto.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecipeDataDTO(
     String title, String body, String slug
) {


}
