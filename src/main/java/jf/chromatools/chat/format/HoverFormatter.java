package jf.chromatools.chat.format;

import jf.chromatools.chat.FormattedMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.regex.MatchResult;

public class HoverFormatter extends ChatFormatter {

    public HoverFormatter() {
        super("&hover\\[(.+?)\\]", -10);
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        BaseComponent[] components = new FormattedMessage(match.group(1), ChatFormatter.hoverFormatters()).getTextComponents();
        message.getFormattingOptions().add(text -> text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(components))));
    }
}
