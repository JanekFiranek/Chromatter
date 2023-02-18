package jf.chromatter.chat.tokens;

import lombok.Value;

@Value
public class TextToken implements ChatToken {
    int index;
    String text;

    @Override
    public int getStart() {
        return this.index - text.length();
    }

    @Override
    public int getEnd() {
        return this.index;
    }
}
