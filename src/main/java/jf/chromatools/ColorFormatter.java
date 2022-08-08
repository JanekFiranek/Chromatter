package jf.chromatools;

import jf.chromatools.chat.ChatCode;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ColorFormatter {
    private final Map<String, ChatCode> colors;

    public ColorFormatter(final Logger logger, final FileConfiguration config) {
        this.colors = new HashMap<>();
        final ConfigurationSection colorSection = config.getConfigurationSection("custom_colors");
        try {
            for (final String key : Objects.requireNonNull(colorSection).getKeys(false)) {
                this.colors.put(key, new ChatCode(Integer.parseInt(key, 16)));
            }
        } catch(NullPointerException e) {
            logger.severe("Invalid custom color configuration!");
        } catch(NumberFormatException e) {
            logger.severe("Invalid hex color format in configuration.");
        }
    }

    public ColorFormatter(final Logger logger, final Configuratio)

    public String color(String replaced, boolean legacy) {
        final Set<ChatCode> formatting = new HashSet<>();
        Map<Integer, Object> parsedText = new HashMap<>();
        List<MatchResult> codes = Stream.of(
                                "&<(.+?)>",
                        "&[\\dA-FK-Ra-fk-r]",
                        "&#([0-9a-fA-F]{6})->#([0-9a-fA-F]{6})",
                        "&#([0-9a-fA-F]{6})(?!->#[0-9a-fA-F]{6})"
                ).flatMap(n -> new Scanner(replaced).findAll(n))
                .sorted(Comparator.comparingInt(MatchResult::start))
                .peek(n -> parsedText.put(n.start(), n))
                .toList();

        codes.stream().map(n -> n.group() + " from index " + n.start() + " to " + n.end()).forEach(System.out::println);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < replaced.length(); i++) {
            final int j = i;
            char c = replaced.charAt(i);
            if (codes.stream().anyMatch(n -> j >= n.start() && j < n.end())) {
                if (sb.length() != 0) {
                    parsedText.put(i - sb.length(), sb.toString());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(c);
            }
        }

        AtomicReference<MatchResult> gradient = new AtomicReference<>(null);
        final StringBuilder text = new StringBuilder();
        parsedText.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(c -> {
                    Object element = c.getValue();
                    if(element instanceof MatchResult chatFormat) {
                        if(chatFormat.group().substring(1).matches("<.+?>")) {
                            Optional.ofNullable(this.colors.get(chatFormat.group(1))).ifPresent(code -> {
                                text.append(code);
                                gradient.set(null);
                            });
                        } else {
                            switch (chatFormat.group().length()) {
                                case 2 -> {
                                    ChatCode code = ChatCode.getConstant(chatFormat.group());
                                    if (code.isColor()) {
                                        text.append(code);
                                        gradient.set(null);
                                    } else {
                                        if (code.equals(ChatCode.getConstant("r"))) {
                                            formatting.clear();
                                            gradient.set(null);
                                        }
                                        formatting.add(code);
                                        text.append(code);
                                    }
                                }
                                case 8 -> {
                                    ChatCode rgbCode = new ChatCode(Integer.parseInt(chatFormat.group(1), 16));
                                    text.append(rgbCode.format(legacy));
                                    text.append(formatSet(formatting));
                                }
                                case 17 -> {
                                    gradient.set(chatFormat);
                                }
                            }
                        }
                    } else if(element instanceof String chatText) {
                        Optional.ofNullable(gradient.get()).ifPresentOrElse(g -> {
                            final ChatCode from = new ChatCode(Integer.parseInt(g.group(1), 16));
                            final ChatCode to = new ChatCode(Integer.parseInt(g.group(2), 16));
                            for (int i = 0; i < chatText.length(); i++) {
                                text.append(ChatCode.formatGradient(from, to, (float) i / (float) chatText.length()).format(legacy));
                                text.append(formatSet(formatting));
                                text.append(chatText.charAt(i));
                            }
                        }, () -> {
                            text.append(chatText);
                        });
                    }
                });
        return text.toString();
    }

    private String formatSet(final Set<ChatCode> set) {
        return set.stream().map(ChatCode::toString).collect(Collectors.joining());
    }
}
