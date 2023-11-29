package com.halfkiloofcarrots.recipepuller.service.aniagotyje;


import com.halfkiloofcarrots.recipepuller.model.dto.TagDto;
import com.halfkiloofcarrots.recipepuller.model.dto.TagListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AniaScheduler {

    private final AniaProxy proxy;

    //  TODO
    // map to Json String list -> zapisać do pliku i nie strzelać za każdym razem

    // strzał po przepisy
    @Scheduled(fixedDelay = 10000)
    public void fetchRecipe() throws IOException {
//        RecipeDataDTO obj =proxy.getRecipe("zeberka-w-kapuscie");
//        log.info("Data: {}", obj);
        Document aniaTag = Jsoup.parse(new URL("https://aniagotuje.pl/"), 20000);
        List<String> tagsListAnia = AniaTagsHtmlParser.parseTags(aniaTag.html());
        List<List<TagDto>> listOfTags = tagsListAnia
                .stream()
                .map(tag -> fetchSlugs(tag))
                .toList();
        log.info("Tag list: {}", listOfTags);
    }

    private List<TagDto> fetchSlugs(String tag) {
        Integer pageNumber = 0;
        List<TagDto> tagsList = new ArrayList<>();
        while (true) {
            TagListDto dto = proxy.getRecipeByTag(tag, pageNumber++);
            tagsList.addAll(dto.content());
            log.info("Current page slug: {} ", dto.content());
            sleep();

            if (dto.last()) {
                break;
            }
        }
        return tagsList;
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
