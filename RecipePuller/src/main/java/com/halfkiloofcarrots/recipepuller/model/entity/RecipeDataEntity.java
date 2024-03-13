package com.halfkiloofcarrots.recipepuller.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_data_id_seq")
    @SequenceGenerator(name = "recipe_data_id_seq", sequenceName = "recipe_data_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "image_source", nullable = false)
    private String imageSource;

    @Column(name = "recipe_data_id")
    private String recipeDataId;

}
