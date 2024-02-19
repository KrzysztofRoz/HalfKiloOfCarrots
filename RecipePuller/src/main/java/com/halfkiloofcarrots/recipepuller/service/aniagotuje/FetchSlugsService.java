package com.halfkiloofcarrots.recipepuller.service.aniagotuje;

import com.halfkiloofcarrots.recipepuller.model.dto.record.TagDto;
import com.halfkiloofcarrots.recipepuller.model.dto.record.TagListDto;
import com.halfkiloofcarrots.recipepuller.util.SleepUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchSlugsService {

    private final AniaProxy proxy;

    @SneakyThrows
    public Set<String> fetchSlugs() {
        int pageNumber = 0;
        Set<TagDto> tagsList = new HashSet<>();
        boolean isLast = false;
        while (!isLast) {
            TagListDto dto = proxy.getRecipeByTag(pageNumber++);
            tagsList.addAll(dto.content());
            log.info("Current page slug: {} ", dto.content());
            SleepUtil.sleep();
            isLast = dto.last();
        }
        return tagsList.stream()
                .map(TagDto::slug)
                .collect(Collectors.toSet());
    }
}
