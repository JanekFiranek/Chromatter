package jf.chromatools.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ChatCode implements Consumer<TextComponent> {
    private static final List<ChatCode> constants = Arrays.asList(
            new ChatCode(0x000000, "0", true),
            new ChatCode(0x0000AA, "1", true),
            new ChatCode(0x00AA00, "2", true),
            new ChatCode(0x00AAAA, "3", true),
            new ChatCode(0xAA0000, "4", true),
            new ChatCode(0xAA00AA, "5", true),
            new ChatCode(0xFFAA00, "6", true),
            new ChatCode(0xAAAAAA, "7", true),
            new ChatCode(0x555555, "8", true),
            new ChatCode(0x5555FF, "9", true),
            new ChatCode(0x55FF55, "a", true),
            new ChatCode(0x55FFFF, "b", true),
            new ChatCode(0xFF5555, "c", true),
            new ChatCode(0xFF55FF, "d", true),
            new ChatCode(0xFFFF55, "e", true),
            new ChatCode(0xFFFFFF, "f", true),
            new ChatCode(0, "k", false, t -> t.setObfuscated(true)),
            new ChatCode(0, "l", false, t -> t.setBold(true)),
            new ChatCode(0, "m", false, t -> t.setStrikethrough(true)),
            new ChatCode(0, "n", false, t -> t.setUnderlined(true)),
            new ChatCode(0, "o", false, t -> t.setItalic(true)),
            new ChatCode(0, "r", false, t -> {
            }));
    private static final ChatCode reset = ChatCode.getConstant("r");

    private final int rgbValue;
    private final String code;
    private final boolean isColor;
    private final Consumer<TextComponent> format;

    private ChatCode(int rgbValue, String code, boolean isColor) {
        this.rgbValue = rgbValue;
        this.code = code;
        this.isColor = isColor;
        this.format = text -> text.setColor(ChatColor.getByChar(code.charAt(0)));
    }

    private ChatCode(int rgbValue, String code, boolean isColor, Consumer<TextComponent> format) {
        this.rgbValue = rgbValue;
        this.code = code;
        this.isColor = isColor;
        this.format = format;
    }

    public ChatCode(final int rgbValue) {
        this.rgbValue = rgbValue;
        this.code = null;
        this.isColor = true;
        this.format = text -> text.setColor(ChatColor.of(new Color(this.rgbValue)));
    }

    public ChatCode(final String rgbHex) {
        this(Integer.valueOf(rgbHex.toLowerCase(), 16));
    }

    public static ChatCode getConstant(final String code) {
        final String s = (code.startsWith("&") ? code.substring(1) : code).toLowerCase();
        return ChatCode.constants.stream().filter(n -> n.code.equals(s)).findFirst().orElse(null);
    }

    public static ChatCode findClosestConstant(final ChatCode rgb) {
        return ChatCode.constants.stream()
                //  .peek(n -> Bukkit.getLogger().info(n.code + " isColor? " + n.isColor))
                .filter(n -> n.isColor)
                //.peek(n -> Bukkit.getLogger().info("Distance from " + n.rgbValue + " to " + rgb.rgbValue + " is " + getDistance(n, rgb)))
                .min(Comparator.comparingDouble(n -> getDistance(rgb, n))).orElse(null);
    }

    public static double getDistance(ChatCode a, ChatCode c) {
        final Color c1 = new Color(a.rgbValue);
        final Color c2 = new Color(c.rgbValue);
        int red1 = c1.getRed();
        int red2 = c2.getRed();
        int rmean = (red1 + red2) >> 1;
        int r = red1 - red2;
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
    }

    public static ChatColor calculateGradient(final ChatCode from, final ChatCode to, final float p) {
        final Color a = new Color(to.getRgbValue());
        final Color b = new Color(from.getRgbValue());
        return ChatColor.of(new Color(
                (int) (a.getRed() * p + b.getRed() * (1 - p)),
                (int) (a.getGreen() * p + b.getGreen() * (1 - p)),
                (int) (a.getBlue() * p + b.getBlue() * (1 - p))
        ));
    }

    public String getCode() {
        return this.code;
    }



    /*
    private static void removeColors(final FormattedMessage message) {
        message.getFormattingOptions().removeIf(n -> {
            if(n instanceof ChatCode chatCode) {
                return chatCode.isColor;
            } else {
                return false;
            }
        });
    }

     */

    public int getRgbValue() {
        return this.rgbValue;
    }

    public String format(final boolean legacy) {
        if (this.code != null) {
            //  Bukkit.getLogger().info("My code is " + this.code);
            return '\u00A7' + this.code;
        } else {
            if (legacy) {
                //  Bukkit.getLogger().info("It's a legacy client");
                return '\u00A7' + ChatCode.findClosestConstant(this).getCode();
            } else {
                //  Bukkit.getLogger().info("Sending rgb value!");
                return net.md_5.bungee.api.ChatColor.of(new Color(this.rgbValue)).toString();
            }
        }
    }

    @Override
    public String toString() {
        return this.format(true);
    }

    @Override
    public void accept(TextComponent textComponent) {
        this.format.accept(textComponent);
    }


    public boolean isColor() {
        return this.isColor;
    }
}
