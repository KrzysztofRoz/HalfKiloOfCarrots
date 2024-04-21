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
@Table(name = "header_image")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeaderImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "header_image_id_seq")
    @SequenceGenerator(name = "header_image_id_seq", sequenceName = "header_image_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "image_source")
    private String imageSource;

    @Column(name = "recipe_data_id")
    private Long recipeDataId;
}
