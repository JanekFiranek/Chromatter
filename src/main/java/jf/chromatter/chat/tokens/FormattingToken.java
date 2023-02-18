package jf.chromatter.chat.tokens;

import lombok.Value;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Value
public class FormattingToken implements ChatToken {
    int start;
    int end;
    MatchResult result;
    Pattern pattern;

}
