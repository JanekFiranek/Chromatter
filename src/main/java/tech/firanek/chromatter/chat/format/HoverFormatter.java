package tech.firanek.chromatter.chat.format;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import tech.firanek.chromatter.chat.FormattedMessage;

import java.util.regex.MatchResult;

public class HoverFormatter extends ChatFormatter {

    private final boolean enableHex;

    public HoverFormatter(final boolean enableHex) {
        super("&hover\\[(.+?)\\]", 10);
        this.enableHex = enableHex;
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        BaseComponent[] components = new FormattedMessage(match.group(1), ChatFormatter.hoverFormatters(this.enableHex)).getTextComponents();
        message.getFormattingOptions().add(text -> text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(components))));
    }
}
