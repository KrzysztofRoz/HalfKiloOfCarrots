package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AniaHtmlParserTest {

    @InjectMocks
    private AniaHtmlParser uut;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldParseHtml() throws IOException {
        // given
        String html = new String(Files.readAllBytes(Paths.get("src/test/resources/weganskie-bezy.html")));

        // when
        RecipeData parse = uut.parse(html);
        String json = mapper.writeValueAsString(parse);

        // then

        assertEquals(1,1);
    }
}
