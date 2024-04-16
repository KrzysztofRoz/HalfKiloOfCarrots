package com.halfkiloofcarrots.recipepuller.repository;

import com.halfkiloofcarrots.recipepuller.model.entity.ContentStepEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.HeaderImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentStepRepository extends JpaRepository<ContentStepEntity, Long> {

    @Modifying
    @Transactional
    void removeByRecipeDataIdIsNull();
}
