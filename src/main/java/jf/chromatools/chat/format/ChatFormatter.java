package jf.chromatools.chat.format;

import jf.chromatools.chat.FormattedMessage;

import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public abstract class ChatFormatter {
    private final Pattern pattern;
    private final int priority;

    protected ChatFormatter(final String pattern, final int priority) {
        this.pattern = Pattern.compile(pattern);
        this.priority = priority;
    }

    public static Set<ChatFormatter> defaultFormatters() {
        return Set.of(new CodeFormatter(), new GradientFormatter(), new HexFormatter(),
                new HoverFormatter(), new LinkFormatter(),
                new SuggestFormatter(), new CommandFormatter());
    }

    public static Set<ChatFormatter> hoverFormatters() {
        return Set.of(new CodeFormatter(), new GradientFormatter(), new HexFormatter());
    }

    public static Set<ChatFormatter> motdFormatters() {
        return Set.of(new CommandFormatter(), new GradientFormatter(), new HexFormatter(), new LinkFormatter(), new HoverFormatter());
    }

    public int getPriority() {
        return this.priority;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public abstract void format(final MatchResult match, final FormattedMessage message);
}
