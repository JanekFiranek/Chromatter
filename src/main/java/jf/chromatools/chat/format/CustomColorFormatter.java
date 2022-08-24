package jf.chromatools.chat.format;

import jf.chromatools.chat.ChatCode;
import jf.chromatools.chat.FormattedMessage;

import java.util.Map;
import java.util.regex.MatchResult;

public class CustomColorFormatter extends ChatFormatter {

    private final Map<String, ChatCode> codes;
    private final boolean enableHex;


    public CustomColorFormatter(final Map<String, ChatCode> codes, final boolean enableHex) {
        super("&\\[(.+?)\\]", 1);
        this.codes = codes;
        this.enableHex = enableHex;
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        final ChatCode code = codes.get(match.group(1));
        if (code != null) {
            message.getFormattingOptions().add(this.enableHex ? code : ChatCode.findClosestConstant(code));
        }
    }
}
