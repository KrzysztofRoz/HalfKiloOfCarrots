package com.halfkiloofcarrots.recipepuller.service.aniagotuje;
import com.halfkiloofcarrots.recipepuller.model.dto.record.RecipeDataDTO;
import com.halfkiloofcarrots.recipepuller.model.dto.record.TagListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ania-recipe-client")
public interface AniaProxy {

    @GetMapping("/client/post/{recipeName}")
     RecipeDataDTO getRecipe(@PathVariable String recipeName);

    @GetMapping("/client/posts/search?categories=&diets=&occasions=&tags={tag}&page={pageNumber}&size=10&sort=publish,desc")
    TagListDto getRecipeByTag(@PathVariable String tag,
                              @PathVariable Integer pageNumber);
}
