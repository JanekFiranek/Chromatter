package jf.chromatools.chat.format;

import jf.chromatools.chat.ChatCode;
import jf.chromatools.chat.FormattedMessage;

import java.util.regex.MatchResult;

public class HexFormatter extends ChatFormatter {

    public HexFormatter() {
        super("&#([0-9a-fA-F]{6})(?!->#[0-9a-fA-F]{6})", 0);
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        message.getFormattingOptions().clear();
        message.getFormattingOptions().add(new ChatCode(match.group(1)));
    }
}
