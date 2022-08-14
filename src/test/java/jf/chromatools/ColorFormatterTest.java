package jf.chromatools;

import jf.chromatools.chat.FormattedMessage;
import jf.chromatools.chat.format.ChatFormatter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ColorFormatterTest {

    @Test
    void formatColor() {
        FormattedMessage message = new FormattedMessage("&l&url[google.pl]Join us and collect &5&lEXTRA &eCOINS&7!", ChatFormatter.defaultFormatters());
        Arrays.stream(message.getTextComponents())
                .forEach(System.out::println);

    }
}