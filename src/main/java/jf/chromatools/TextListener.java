package jf.chromatools;

import jf.chromatools.chat.FormattedMessage;
import jf.chromatools.chat.format.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class TextListener implements Listener {

    private final FileConfiguration config;
    private final boolean enableHex;

    public TextListener(final FileConfiguration config) {
        this.config = config;
        this.enableHex = config.getBoolean("enable_hex");
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        FormattedMessage message = new FormattedMessage(event.getMessage(), ChatFormatter.defaultFormatters(false));
        Bukkit.spigot().broadcast(message.getTextComponents());
        //   event.setMessage(this.formatter.color(event.getMessage(), this.enableHex));

    }


    private String getConfig(final String key) {
        return Optional.ofNullable(this.config.getString(key)).orElseThrow(() -> new RuntimeException("Config key " + key + " is null!"));
    }
}
