package com.halfkiloofcarrots.recipepuller.service.aniagotyje;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeDataDTO;
import com.halfkiloofcarrots.recipepuller.model.dto.TagDto;
import com.halfkiloofcarrots.recipepuller.model.dto.TagListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// https://api.aniagotuje.pl/client/categories/TAG?slug=maka-pszenna

//https://api.aniagotuje.pl
// /client/posts/search?categories=&diets=&occasions=&tags=cukier&page=1&size=2000&sort=publish,desc
@FeignClient(value = "ania-recipe-client")
public interface AniaProxy {

    @GetMapping("/client/post/{recipeName}")
     RecipeDataDTO getRecipe(@PathVariable String recipeName);

    @GetMapping("/client/posts/search?categories=&diets=&occasions=&tags={tag}&page={pageNumber}&size=10&sort=publish,desc")
    TagListDto getRecipeByTag(@PathVariable String tag,
                              @PathVariable Integer pageNumber);
}
