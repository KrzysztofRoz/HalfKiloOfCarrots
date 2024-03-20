package com.halfkiloofcarrots.recipepuller.repository;

import com.halfkiloofcarrots.recipepuller.model.entity.RecipeDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeDataRepository extends JpaRepository<RecipeDataEntity, Long> {

    Optional<RecipeDataEntity> findBySlug(String slug);
}
