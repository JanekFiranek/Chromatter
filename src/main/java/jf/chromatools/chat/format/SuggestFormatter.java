package jf.chromatools.chat.format;

import jf.chromatools.chat.FormattedMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Entity;

import java.util.regex.MatchResult;

public class SuggestFormatter extends ChatFormatter{

    public SuggestFormatter() {
        super("&suggest\\[(.+?)\\]", 1);
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        message.getFormattingOptions().add(text -> text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, match.group(1))));
    }
}
