package com.halfkiloofcarrots.recipepuller.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record TagListDto(List<TagDto> content, Boolean last) {
}
