package jf.chromater.chat.tokens;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public record FormattingToken(int start, int end, MatchResult result, Pattern pattern) implements ChatToken {

}
