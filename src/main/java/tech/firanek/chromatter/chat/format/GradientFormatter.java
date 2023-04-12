package tech.firanek.chromatter.chat.format;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import tech.firanek.chromatter.chat.ChatCode;
import tech.firanek.chromatter.chat.FormattedMessage;
import tech.firanek.chromatter.chat.tokens.ChatToken;
import tech.firanek.chromatter.chat.tokens.FormattingToken;
import tech.firanek.chromatter.chat.tokens.TextToken;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

public class GradientFormatter extends ChatFormatter {

    private final boolean enableHex;

    public GradientFormatter(final boolean enableHex) {
        super("&(#[0-9a-fA-F]{6}->.*#[0-9a-fA-F]{6})", -10);
        this.enableHex = enableHex;
    }

    //TODO document this part
    @Override
    public void format(final MatchResult match, final FormattedMessage message) {
        message.getFormattingOptions().clear();
        final String matched = match.group(1);
        final String[] hexTokens = matched.split("->");
        final Queue<ChatToken> tokens = message.getTokens();
        while (tokens.peek() instanceof FormattingToken) {
            message.nextStep();
        }
        if (tokens.peek() instanceof TextToken textToken) {
            if (Arrays.stream(hexTokens).allMatch(n -> n.matches("#[0-9a-fA-F]{6}"))) {
                tokens.remove();
                LinkedList<ChatCode> colors = Arrays.stream(hexTokens).map(ChatCode::new).collect(Collectors.toCollection(LinkedList::new));
                final String text = textToken.getText();
                final int sections = colors.size() - 1;
                final int sectionLength = text.length() / sections;
                for (int i = 0; i < sections; i++) {
                    ChatCode from = colors.removeFirst();
                    ChatCode to = colors.peekFirst();
                    int start = i * sectionLength;
                    int end = (i + 1) * sectionLength;
                    if (i == sections - 1) {
                        end = text.length();
                    }
                    final String section = text.substring(start, end);
                    final List<TextComponent> components = message.getComponents();
                    for (int c = 0; c < section.length(); c++) {
                        final TextComponent component = new TextComponent();
                        final ChatCode hex = ChatCode.calculateGradient(from, to, (float) c / (float) section.length());
                        component.setColor(this.enableHex ? ChatColor.of(hex.getColor()) : ChatColor.of(ChatCode.findClosestConstant(hex).getCode()));
                        message.getFormattingOptions().forEach(n -> n.accept(component));
                        component.setText(Character.toString(text.charAt(c)));
                        components.add(component);
                    }
                }
            }
        }

    }
}
