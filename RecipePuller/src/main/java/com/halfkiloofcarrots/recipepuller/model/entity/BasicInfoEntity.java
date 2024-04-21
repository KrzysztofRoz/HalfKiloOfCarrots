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
@Table(name = "basic_info")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "basic_info_id_seq")
    @SequenceGenerator(name = "basic_info_id_seq", sequenceName = "basic_info_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "value")
    private String value;

    @Column(name = "recipe_data_id")
    private Long recipeDataId;
}
