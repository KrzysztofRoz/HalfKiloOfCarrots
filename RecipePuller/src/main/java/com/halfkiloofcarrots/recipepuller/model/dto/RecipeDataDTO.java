package com.halfkiloofcarrots.recipepuller.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecipeDataDTO(
     String metaTitle, String body, String slug
) {


}
