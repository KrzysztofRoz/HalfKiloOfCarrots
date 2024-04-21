package com.halfkiloofcarrots.recipepuller.repository;

import com.halfkiloofcarrots.recipepuller.model.entity.IngredientEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {

    @Modifying
    @Transactional
    void removeByRecipeDataIdIsNull();
}
