package jf.chromatools.chat.tokens;

public record TextToken(int index, String text) implements ChatToken {

    @Override
    public int start() {
        return this.index - text.length();
    }

    @Override
    public int end() {
        return this.index;
    }
}
