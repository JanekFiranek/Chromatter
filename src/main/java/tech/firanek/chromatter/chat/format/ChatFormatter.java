package tech.firanek.chromatter.chat.format;

import lombok.Getter;
import tech.firanek.chromatter.chat.ChatCode;
import tech.firanek.chromatter.chat.FormattedMessage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public abstract class ChatFormatter {
    private static Map<String, ChatCode> colorMap = Collections.emptyMap();
    @Getter
    private final Pattern pattern;
    @Getter
    private final int priority;

    protected ChatFormatter(final String pattern, final int priority) {
        this.pattern = Pattern.compile(pattern);
        this.priority = priority;
    }

    public static Set<ChatFormatter> defaultFormatters(final boolean enableHex) {
        return Set.of(new CodeFormatter(), new GradientFormatter(enableHex), new HexFormatter(enableHex),
                new HoverFormatter(enableHex), new LinkFormatter(),
                new SuggestFormatter(), new CommandFormatter(), new RandomFormatter(enableHex), new CustomColorFormatter(colorMap, enableHex));
    }

    public static Set<ChatFormatter> hoverFormatters(final boolean enableHex) {
        return Set.of(new CodeFormatter(), new GradientFormatter(enableHex), new HexFormatter(enableHex), new RandomFormatter(enableHex), new CustomColorFormatter(colorMap, enableHex));
    }

    public static Set<ChatFormatter> motdFormatters(final boolean enableHex) {
        return Set.of(new CommandFormatter(), new GradientFormatter(enableHex), new HexFormatter(enableHex),
                new LinkFormatter(), new HoverFormatter(enableHex), new RandomFormatter(enableHex), new CustomColorFormatter(colorMap, enableHex));
    }

    public static void setColorMap(final Map<String, ChatCode> c) {
        colorMap = c;
    }

    public abstract void format(final MatchResult match, final FormattedMessage message);
}
