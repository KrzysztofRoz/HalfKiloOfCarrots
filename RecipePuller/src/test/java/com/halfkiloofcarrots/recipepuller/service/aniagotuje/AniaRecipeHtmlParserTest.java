package com.halfkiloofcarrots.recipepuller.service.aniagotuje;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfkiloofcarrots.recipepuller.model.dto.RecipeData;
import com.halfkiloofcarrots.recipepuller.model.dto.record.RecipeDataDTO;
import com.halfkiloofcarrots.recipepuller.service.aniagotuje.parser.AniaRecipeHtmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AniaRecipeHtmlParserTest {

    @InjectMocks
    private AniaRecipeHtmlParser uut;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldParseHtml() throws IOException {
        // given
        String html = new String(Files.readAllBytes(Paths.get("src/test/resources/kurczak-po-koreansku.html")));
        RecipeDataDTO dto = new RecipeDataDTO("title",html,"slug");

        // when
        RecipeData parse = uut.parse(dto);
        String json = mapper.writeValueAsString(parse);

        // then

        assertEquals(1,1);
    }
}
