package com.halfkiloofcarrots.recipepuller.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "recipe_data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_data_id_seq")
    @SequenceGenerator(name = "recipe_data_id_seq", sequenceName = "recipe_data_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "recipe_header_title", nullable = false)
    private String recipeHeaderTitle;

    @Column(name = "methodology")
    private String methodology;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "recipe_data_id")
    private List<HeaderImageEntity> headerImages;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "recipe_data_id")
    private List<IngredientEntity> ingredients;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "recipe_data_id")
    private List<BasicInfoEntity> basicInfoList;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "recipe_data_id")
    private List<ContentStepEntity> contentSteps;
}
