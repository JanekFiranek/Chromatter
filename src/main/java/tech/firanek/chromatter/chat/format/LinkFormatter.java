package tech.firanek.chromatter.chat.format;

import net.md_5.bungee.api.chat.ClickEvent;
import tech.firanek.chromatter.chat.FormattedMessage;

import java.util.regex.MatchResult;

public class LinkFormatter extends ChatFormatter {
    public LinkFormatter() {
        super("&url\\[(.+?)\\]", 8);
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        final String url = match.group(1).matches("https?://(.+)") ? match.group(1) : "https://" + match.group(1);
        message.getFormattingOptions().add(text -> text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
    }
}
