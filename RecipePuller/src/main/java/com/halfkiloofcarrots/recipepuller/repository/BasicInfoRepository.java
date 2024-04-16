package com.halfkiloofcarrots.recipepuller.repository;

import com.halfkiloofcarrots.recipepuller.model.entity.BasicInfoEntity;
import com.halfkiloofcarrots.recipepuller.model.entity.HeaderImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicInfoRepository extends JpaRepository<BasicInfoEntity, Long> {

    @Modifying
    @Transactional
    void removeByRecipeDataIdIsNull();
}
