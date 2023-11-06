package com.halfkiloofcarrots.recipepuller.service.aniagotyje;


import com.halfkiloofcarrots.recipepuller.dto.RecipeDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AniaScheduler {

    private final AniaProxy proxy;

    @Scheduled(fixedDelay = 10000)
    public void fetchRecipe() {
        RecipeDataDTO obj =proxy.getRecipe("zeberka-w-kapuscie");
        log.info("Data: {}", obj);
    }
}
