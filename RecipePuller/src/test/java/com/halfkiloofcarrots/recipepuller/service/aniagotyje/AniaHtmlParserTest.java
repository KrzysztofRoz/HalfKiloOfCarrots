package com.halfkiloofcarrots.recipepuller.service.aniagotyje;

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

    @Test
    void shouldParseHtml() throws IOException {
        // given
        String html = new String(Files.readAllBytes(Paths.get("src/test/resources/zeberka-w-kapuscie.html")));

        // when
        uut.parse(html);

        // then

    }
}
