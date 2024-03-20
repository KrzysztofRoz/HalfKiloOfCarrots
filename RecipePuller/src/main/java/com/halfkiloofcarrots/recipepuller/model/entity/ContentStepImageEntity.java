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
@Table(name = "content_step_image")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentStepImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_step_image_id_seq")
    @SequenceGenerator(name = "content_step_image_id_seq", sequenceName = "content_step_image_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "image_source")
    private String imageSource;

}
