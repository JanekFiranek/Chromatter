package jf.chromater.chat.format;

import jf.chromater.chat.ChatCode;
import jf.chromater.chat.FormattedMessage;
import jf.chromater.chat.tokens.ChatToken;
import jf.chromater.chat.tokens.FormattingToken;
import jf.chromater.chat.tokens.TextToken;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.Queue;
import java.util.regex.MatchResult;

public class GradientFormatter extends ChatFormatter {

    private final boolean enableHex;

    public GradientFormatter(final boolean enableHex) {
        super("&#([0-9a-fA-F]{6})->#([0-9a-fA-F]{6})", -10);
        this.enableHex = enableHex;
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
                final ChatCode hex = ChatCode.calculateGradient(from, to, (float) i / (float) text.length());
                component.setColor(this.enableHex ? ChatColor.of(hex.getColor()) : ChatColor.of(ChatCode.findClosestConstant(hex).getCode()));
                message.getFormattingOptions().forEach(n -> n.accept(component));
                component.setText("" + text.charAt(i));
                components.add(component);
            }
        }
    }
}
