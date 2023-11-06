package com.halfkiloofcarrots.recipepuller.service;
import com.halfkiloofcarrots.recipepuller.dto.RecipeDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//https://api.aniagotuje.pl/client/post/zeberka-w-kapuscie
@FeignClient(value = "ania-recipe-client")
public interface AniaProxy {

    @GetMapping("/client/post/{recipeName}")
     RecipeDataDTO getRecipe(@PathVariable String recipeName);
}
