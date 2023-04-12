package tech.firanek.chromatter.chat;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ChatCode implements Consumer<TextComponent> {
    private static final List<ChatCode> constants = Arrays.asList(
            new ChatCode(0x000000, "0"),
            new ChatCode(0x0000AA, "1"),
            new ChatCode(0x00AA00, "2"),
            new ChatCode(0x00AAAA, "3"),
            new ChatCode(0xAA0000, "4"),
            new ChatCode(0xAA00AA, "5"),
            new ChatCode(0xFFAA00, "6"),
            new ChatCode(0xAAAAAA, "7"),
            new ChatCode(0x555555, "8"),
            new ChatCode(0x5555FF, "9"),
            new ChatCode(0x55FF55, "a"),
            new ChatCode(0x55FFFF, "b"),
            new ChatCode(0xFF5555, "c"),
            new ChatCode(0xFF55FF, "d"),
            new ChatCode(0xFFFF55, "e"),
            new ChatCode(0xFFFFFF, "f"),
            new ChatCode("k", t -> t.setObfuscated(true)),
            new ChatCode("l", t -> t.setBold(true)),
            new ChatCode("m", t -> t.setStrikethrough(true)),
            new ChatCode("n", t -> t.setUnderlined(true)),
            new ChatCode("o", t -> t.setItalic(true)),
            new ChatCode("r", t -> {
            }));

    @Getter
    private final Color color;
    @Getter
    private final String code;
    private final Consumer<TextComponent> format;

    private ChatCode(int rgbValue, String code) {
        this.color = new Color(rgbValue);
        this.code = code;
        this.format = text -> text.setColor(ChatColor.getByChar(code.charAt(0)));
    }

    private ChatCode(String code, Consumer<TextComponent> format) {
        this.color = null;
        this.code = code;
        this.format = format;
    }

    public ChatCode(final int rgbValue) {
        this.color = new Color(rgbValue);
        this.code = null;
        this.format = text -> text.setColor(ChatColor.of(this.color));
    }

    public ChatCode(final String rgbHex) {
        this(Integer.valueOf((rgbHex.startsWith("#") ? rgbHex.substring(1) : rgbHex).toLowerCase(), 16));
    }

    public static ChatCode getConstant(final String code) {
        final String s = (code.startsWith("&") ? code.substring(1) : code).toLowerCase();
        return ChatCode.constants.stream().filter(n -> n.code.equals(s)).findFirst().orElse(null);
    }

    public static ChatCode findClosestConstant(final ChatCode rgb) {
        return ChatCode.constants.stream()
                .filter(ChatCode::isColor)
                .min(Comparator.comparingDouble(n -> getDistance(rgb, n))).orElse(null);
    }

    public static double getDistance(ChatCode a, ChatCode c) {
        final Color c1 = a.getColor();
        final Color c2 = c.getColor();
        int red1 = c1.getRed();
        int red2 = c2.getRed();
        int rmean = (red1 + red2) >> 1;
        int r = red1 - red2;
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
    }

    public static ChatCode calculateGradient(final ChatCode from, final ChatCode to, final float p) {
        final Color a = to.getColor();
        final Color b = from.getColor();
        return new ChatCode(new Color(
                (int) (a.getRed() * p + b.getRed() * (1 - p)),
                (int) (a.getGreen() * p + b.getGreen() * (1 - p)),
                (int) (a.getBlue() * p + b.getBlue() * (1 - p))
        ).getRGB());
    }

    public String format(final boolean legacy) {
        if (this.code != null) {
            return '§' + this.code;
        } else {
            if (legacy) {
                return '§' + ChatCode.findClosestConstant(this).getCode();
            } else {
                return net.md_5.bungee.api.ChatColor.of(this.getColor()).toString();
            }
        }
    }

    @Override
    public String toString() {
        return this.format(true);
    }

    public boolean isColor() {
        return this.color != null;
    }

    @Override
    public void accept(final TextComponent textComponent) {
        this.format.accept(textComponent);
    }
}
