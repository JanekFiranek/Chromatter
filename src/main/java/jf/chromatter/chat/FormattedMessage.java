package jf.chromatter.chat;

import jf.chromatter.chat.format.ChatFormatter;
import jf.chromatter.chat.tokens.ChatToken;
import jf.chromatter.chat.tokens.FormattingToken;
import jf.chromatter.chat.tokens.TextToken;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FormattedMessage {
    private final Queue<ChatToken> tokens;

    private final List<TextComponent> components;
    private final Set<Consumer<TextComponent>> formattingOptions;
    private final Map<Pattern, ChatFormatter> format;

    public FormattedMessage(final String message, final Set<ChatFormatter> format) {
        this.format = new HashMap<>();
        this.tokens = new PriorityQueue<>(Comparator.comparingInt(ChatToken::getEnd));
        this.components = new ArrayList<>();
        this.formattingOptions = new HashSet<>();
        format.forEach(n -> this.format.put(n.getPattern(), n));

        final Matcher tokenMatcher = this.combinePatterns(this.format.values()).matcher(message);
        List<MatchResult> results = tokenMatcher.results().toList();
        List<ChatFormatter> formatters = format.stream().sorted(Comparator.comparingInt(ChatFormatter::getPriority).reversed()).toList();

        for (MatchResult result : results) {
            for (ChatFormatter formatter : formatters) {
                Matcher resultMatcher = formatter.getPattern().matcher(result.group());
                if (resultMatcher.find()) {
                    this.tokens.add(new FormattingToken(result.start(), result.end(), resultMatcher.toMatchResult(), formatter.getPattern()));
                    break;
                }

            }
        }

        /*
        This loop goes through all characters, searching if any of characters is within bounds of a token.
        This section could use a cleanup
         */
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            boolean matched = false;
            for (ChatToken n : tokens) {
                if (i >= n.getStart() && i < n.getEnd()) {
                    if (!t.isEmpty()) {
                        this.tokens.add(new TextToken(i, t.toString()));
                        t = new StringBuilder();
                    }
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                t.append(c);
            }
        }
        if (!t.isEmpty()) {
            this.tokens.add(new TextToken(message.length(), t.toString()));
        }

        while (!this.tokens.isEmpty()) {
            this.nextStep();
        }
    }

    public void nextStep() {
        //TODO untangle this code a bit
        final ChatToken token = this.tokens.remove();
        if (token instanceof TextToken textToken) {
            final TextComponent component = new TextComponent();
            component.setText(textToken.getText());
            this.formattingOptions.forEach(n -> n.accept(component));
            this.components.add(component);
        } else if (token instanceof FormattingToken formattingToken) {
            this.format.get(formattingToken.getPattern()).format(formattingToken.getResult(), this);
        }
    }

    private Pattern combinePatterns(final Collection<ChatFormatter> formatters) {
        return Pattern.compile(formatters.stream().sorted(Comparator.comparingInt(ChatFormatter::getPriority).reversed())
                .map(ChatFormatter::getPattern)
                .map(Pattern::pattern)
                .collect(Collectors.joining("|")));
    }

    public Queue<ChatToken> getTokens() {
        return this.tokens;
    }

    public List<TextComponent> getComponents() {
        return this.components;
    }

    public Set<Consumer<TextComponent>> getFormattingOptions() {
        return this.formattingOptions;
    }

    public TextComponent[] getTextComponents(final String[] replaced, final String[] replacements) {
        if (replaced.length != replacements.length) {
            throw new IllegalArgumentException("Replaced strings' array length doesn't match replacements' array!");
        }
        final List<TextComponent> componentsCopy = this.components.stream().map(TextComponent::duplicate).toList();
        for (int i = 0; i < replaced.length; i++) {
            for (final TextComponent component : componentsCopy) {
                component.setText(component.getText().replace(replaced[i], replacements[i]));
            }
        }

        return componentsCopy.toArray(TextComponent[]::new);
    }

    public TextComponent[] getTextComponents() {
        return this.components.toArray(TextComponent[]::new);
    }
}
