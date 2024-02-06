package com.halfkiloofcarrots.recipepuller.model.dto.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record TagListDto(List<TagDto> content, Boolean last) {
}
