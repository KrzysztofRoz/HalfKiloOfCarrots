package com.halfkiloofcarrots.recipepuller.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeContentStep {
    private String title;
    private String instruction;
    private List<String> images;
}
