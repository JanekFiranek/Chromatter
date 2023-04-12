package tech.firanek.chromatter.chat.format;

import net.md_5.bungee.api.chat.ClickEvent;
import tech.firanek.chromatter.chat.FormattedMessage;

import java.util.regex.MatchResult;

public class CommandFormatter extends ChatFormatter {

    public CommandFormatter() {
        super("&cmd\\[(.+?)\\]", 3);
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        message.getFormattingOptions().add(text -> text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, match.group(1))));
    }
}
