package jf.chromatools.chat.format;

import jf.chromatools.chat.FormattedMessage;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.regex.MatchResult;

public class LinkFormatter extends ChatFormatter {


    public LinkFormatter() {
        super("&font\\[(.+?)\\]", 8);
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        message.getFormattingOptions().add(text -> text.setFont(match.group(1)));
    }
}
