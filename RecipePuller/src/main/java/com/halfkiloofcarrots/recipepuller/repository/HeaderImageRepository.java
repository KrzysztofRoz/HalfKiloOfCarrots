package com.halfkiloofcarrots.recipepuller.repository;

import com.halfkiloofcarrots.recipepuller.model.entity.HeaderImageEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.RecipeDataEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeaderImageRepository extends JpaRepository<HeaderImageEntity, Long> {

    @Modifying
    @Transactional
    void removeByRecipeDataIdIsNull();
}
