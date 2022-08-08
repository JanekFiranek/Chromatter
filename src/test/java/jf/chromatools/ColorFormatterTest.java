package jf.chromatools;

import org.junit.jupiter.api.Test;

class ColorFormatterTest {

    @Test
    void formatColor() {
        ColorFormatter formatter = new ColorFormatter(colors);
        System.out.println(formatter.color("&llmfao&#100000Whatever &athat means.&r", false));
    }
}