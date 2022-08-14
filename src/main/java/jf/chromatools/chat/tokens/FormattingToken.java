package jf.chromatools.chat.tokens;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public record FormattingToken(MatchResult result, Pattern pattern) implements ChatToken {
    @Override
    public int index() {
        return result.end();
    }
}
