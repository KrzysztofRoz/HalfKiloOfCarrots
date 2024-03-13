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
@Table(name = "content_step")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentStepEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_step_id_seq")
    @SequenceGenerator(name = "content_step_image_id_seq", sequenceName = "content_step_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "instruction")
    private String instruction;

}
