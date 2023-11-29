package com.halfkiloofcarrots.recipepuller.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TagDto(String slug) {
}
