package jf.chromatools.chat;

import jf.chromatools.chat.format.ChatFormatter;
import jf.chromatools.chat.tokens.ChatToken;
import jf.chromatools.chat.tokens.FormattingToken;
import jf.chromatools.chat.tokens.TextToken;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FormattedMessage {
    private final Queue<ChatToken> tokens;
    private final List<TextComponent> components;
    private final Set<Consumer<TextComponent>> formattingOptions;
    private final Map<Pattern, ChatFormatter> format;

    public FormattedMessage(final String message, final Set<ChatFormatter> format) {
        this.format = new HashMap<>();
        this.tokens = new PriorityQueue<>(Comparator.comparingInt(ChatToken::index));
        this.components = new ArrayList<>();
        this.formattingOptions = new HashSet<>();
        format.forEach(n -> this.format.put(n.getPattern(), n));

        AtomicReference<String> atomicMessage = new AtomicReference<>(message);
        format.stream().sorted(Comparator.comparingInt(ChatFormatter::getPriority).reversed())
                .forEach(f -> {
                    f.getPattern().matcher(atomicMessage.get())
                            .results()
                            .peek(n -> Bukkit.getLogger().info(atomicMessage.get()))
                            .peek(n -> atomicMessage.set(atomicMessage.get().replace(n.group(), "")))
                            .peek(n -> Bukkit.getLogger().info("Replaced with " + atomicMessage.get()))
                            .map(n -> new FormattingToken(n, f.getPattern()))
                            .forEach(tokens::add);
                });

        StringBuilder t = new StringBuilder();
        Bukkit.getLogger().info("Formatting tokens: ");
        tokens.stream()
                .filter(n -> n instanceof  FormattingToken)
                .map(n -> (FormattingToken) n)
                .forEach(n -> {
                    Bukkit.getLogger().info("Token start: " + n.result().start() + " end: " + n.result().end() + " content: " + n.result().group());
                });
        Bukkit.getLogger().info("Text tokens: ");
        for (int i = 0; i < message.length(); i++) {
            final int j = i;
            char c = message.charAt(i);
            if (tokens.stream()
                    .filter(n -> n instanceof FormattingToken)
                    .map(n -> (FormattingToken) n)
                    .map(FormattingToken::result).anyMatch(n -> j >= n.start() && j < n.end())) {
                if (!t.isEmpty()) {
                    this.tokens.add(new TextToken(j, t.toString()));
                    t = new StringBuilder();
                }
            } else {
                t.append(c);
            }
        }
        if (!t.isEmpty()) {
            this.tokens.add(new TextToken(message.length(), t.toString()));
        }
        tokens.stream()
                .filter(n -> n instanceof TextToken)
                .map(n -> (TextToken) n)
                .forEach(n -> {
                    Bukkit.getLogger().info("Token start: " + (n.index() - n.text().length()) + " end: " + n.index() + " text: " + n.text());
                });

        while (!this.tokens.isEmpty()) {
            this.nextStep();
        }

    }

    public void nextStep() {
        final ChatToken token = this.tokens.remove();
        if (token instanceof TextToken textToken) {
            final TextComponent component = new TextComponent();
            component.setText(textToken.text());
            this.formattingOptions.forEach(n -> n.accept(component));
            this.components.add(component);
        } else if (token instanceof FormattingToken formattingToken) {
            this.format.get(formattingToken.pattern()).format(formattingToken.result(), this);
        }
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

    /*
    public void debug() {
        System.out.println("FormattedMessage details: ");
        System.out.println(this.components);
        while(!this.tokens.isEmpty()) {
            ChatToken token = this.tokens.remove();
            System.out.println("Token, class " + token.getClass().getSimpleName() + ", index " + token.index());
            if (token instanceof TextToken textToken) {
                System.out.println("text: " + textToken.text());
            } else if(token instanceof FormattingToken formatToken) {
                System.out.println("pattern: " + formatToken.pattern() + " matched: " + formatToken.result().group());
            }
        }
    }

     */

    public TextComponent[] getTextComponents() {
        return this.components.toArray(TextComponent[]::new);
    }


}
