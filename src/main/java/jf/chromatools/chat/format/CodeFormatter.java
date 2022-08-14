package jf.chromatools.chat.format;

import jf.chromatools.chat.ChatCode;
import jf.chromatools.chat.FormattedMessage;

import java.util.regex.MatchResult;

public class CodeFormatter extends ChatFormatter {
    private static final ChatCode reset = ChatCode.getConstant("r");

    public CodeFormatter() {
        super("&[\\dA-FK-Ra-fk-r]", 0);
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        final ChatCode code = ChatCode.getConstant(match.group());
        if (code.isColor() || code.equals(reset)) {
            message.getFormattingOptions().clear();
        }
        message.getFormattingOptions().add(code);
    }
}
