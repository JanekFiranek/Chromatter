package jf.chromatools.chat.format;

import jf.chromatools.chat.ChatCode;
import jf.chromatools.chat.FormattedMessage;

import java.util.regex.MatchResult;

public class HexFormatter extends ChatFormatter {
    private final boolean enableHex;

    public HexFormatter(final boolean enableHex) {
        super("&#([0-9a-fA-F]{6})(?!->#[0-9a-fA-F]{6})", 0);
        this.enableHex = enableHex;
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        message.getFormattingOptions().clear();
        final ChatCode hex = new ChatCode(match.group(1));
        message.getFormattingOptions().add(this.enableHex ? hex : ChatCode.findClosestConstant(hex));
    }
}
