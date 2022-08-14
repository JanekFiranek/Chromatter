package jf.chromatools.chat.format;

import jf.chromatools.chat.ChatCode;
import jf.chromatools.chat.FormattedMessage;
import jf.chromatools.chat.tokens.ChatToken;
import jf.chromatools.chat.tokens.FormattingToken;
import jf.chromatools.chat.tokens.TextToken;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.Queue;
import java.util.regex.MatchResult;

public class GradientFormatter extends ChatFormatter {

    public GradientFormatter() {
        super("&#([0-9a-fA-F]{6})->#([0-9a-fA-F]{6})", -10);
    }

    @Override
    public void format(final MatchResult match, final FormattedMessage message) {
        message.getFormattingOptions().clear();
        final ChatCode from = new ChatCode(match.group(1));
        final ChatCode to = new ChatCode(match.group(2));
        final Queue<ChatToken> tokens = message.getTokens();
        while (tokens.peek() instanceof FormattingToken) {
            message.nextStep();
        }
        if (tokens.peek() instanceof TextToken textToken) {
            tokens.remove();
            final String text = textToken.text();
            final List<TextComponent> components = message.getComponents();
            for (int i = 0; i < text.length(); i++) {
                final TextComponent component = new TextComponent();
                component.setColor(ChatCode.calculateGradient(from, to, (float) i / (float) text.length()));
                message.getFormattingOptions().forEach(n -> n.accept(component));
                component.setText("" + text.charAt(i));
                components.add(component);
            }
        }
    }
}
