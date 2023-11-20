package com.halfkiloofcarrots.recipepuller.service.aniagotyje;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// https://api.aniagotuje.pl/client/categories/TAG?slug=maka-pszenna
@FeignClient(value = "ania-recipe-client")
public interface AniaProxy {

    @GetMapping("/client/post/{recipeName}")
     RecipeDataDTO getRecipe(@PathVariable String recipeName);
}
