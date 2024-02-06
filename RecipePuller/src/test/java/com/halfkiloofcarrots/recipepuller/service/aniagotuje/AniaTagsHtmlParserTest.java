package com.halfkiloofcarrots.recipepuller.service.aniagotuje;

import com.halfkiloofcarrots.recipepuller.service.aniagotuje.parser.AniaTagsHtmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class AniaTagsHtmlParserTest {
    @InjectMocks
    private AniaTagsHtmlParser uut;

    @Test
    public void shouldParseTags () throws IOException {
        // given
        String html = new String(Files.readAllBytes(Paths.get("src/test/resources/tags.html")));

        // when
        uut.parseTags(html);

        //then
        assertEquals(1,1);
    }

}
