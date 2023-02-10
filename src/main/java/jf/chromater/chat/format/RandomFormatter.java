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
import java.util.Random;
import java.util.regex.MatchResult;

public class RandomFormatter extends ChatFormatter {
    private final Random r = new Random();
    private final boolean enableHex;

    public RandomFormatter(final boolean enableHex) {
        super("&random", 10);
        this.enableHex = enableHex;
    }

    @Override
    public void format(MatchResult match, FormattedMessage message) {
        final Queue<ChatToken> tokens = message.getTokens();
        while (tokens.peek() instanceof FormattingToken) {
            message.nextStep();
        }
        if (tokens.peek() instanceof TextToken textToken) {
            tokens.remove();
            final String text = textToken.getText();
            final List<TextComponent> components = message.getComponents();
            for (int i = 0; i < text.length(); i++) {
                TextComponent component = new TextComponent();
                final ChatCode hex = new ChatCode(r.nextInt(0xffffff));
                component.setColor(this.enableHex ? ChatColor.of(hex.getColor()) : ChatColor.of(ChatCode.findClosestConstant(hex).getCode()));
                message.getFormattingOptions().forEach(n -> n.accept(component));
                component.setText(Character.toString(text.charAt(i)));
                components.add(component);
            }
        }
    }
}
