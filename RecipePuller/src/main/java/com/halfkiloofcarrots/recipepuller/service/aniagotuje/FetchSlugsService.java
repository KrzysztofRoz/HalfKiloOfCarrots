package com.halfkiloofcarrots.recipepuller.service.aniagotuje;

import com.halfkiloofcarrots.recipepuller.model.dto.record.TagDto;
import com.halfkiloofcarrots.recipepuller.model.dto.record.TagListDto;
import com.halfkiloofcarrots.recipepuller.service.aniagotuje.parser.AniaTagsHtmlParser;
import com.halfkiloofcarrots.recipepuller.util.SleepUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchSlugsService {

    private final AniaProxy proxy;

    @SneakyThrows
    public Set<String> fetchSlugs() {
        Document aniaTag = Jsoup.parse(new URL("https://aniagotuje.pl/"), 20000);
        List<String> tagsListAnia = AniaTagsHtmlParser.parseTags(aniaTag.html());
        return tagsListAnia
                .stream()
                .map(this::fetchSlugs)
                .flatMap(List::stream)
                .map(TagDto::slug)
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    private List<TagDto> fetchSlugs(String tag) {
        int pageNumber = 0;
        List<TagDto> tagsList = new ArrayList<>();
        boolean isLast = false;
        while (!isLast) {
            TagListDto dto = proxy.getRecipeByTag(tag, pageNumber++);
            tagsList.addAll(dto.content());
            log.info("Current page slug: {} ", dto.content());
            SleepUtil.sleep();
            isLast = dto.last();
        }
        return tagsList;
    }
}
