package com.halfkiloofcarrots.recipepuller.model.entity;

import com.halfkiloofcarrots.recipepuller.model.dto.RecipeHeader;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "recipe_data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDataEntity {
    // GenerationType.SEQUENC or IDENTITY ??
    // konstruktor

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    //@Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String recipeHeaderTitle;

    private String methodology;

}
