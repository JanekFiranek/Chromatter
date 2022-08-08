package jf.chromatools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class TextListener implements Listener {

    private final FileConfiguration config;
    private final ColorFormatter formatter;
    private boolean enableHex;

    public TextListener(final FileConfiguration config, final ColorFormatter formatter) {
        this.config = config;
        this.formatter = formatter;
        this.enableHex = config.getBoolean("enable_hex");
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        if(event.getPlayer().hasPermission(this.getConfig("permissions.chat_color"))) {
            event.setMessage(this.formatter.color(event.getMessage(), this.enableHex));
        }
    }

    @EventHandler
    public void onSignEdit(final SignChangeEvent event) {
        if(event.getPlayer().hasPermission(this.getConfig("permissions.sign_color"))) {
            for(int i = 0; i < event.getLines().length; i++) {
                final int j = i;
                Optional.ofNullable(event.getLine(i)).ifPresent(line -> event.setLine(j, this.formatter.color(line, this.enableHex)));
            }
        }
    }

    private String getConfig(final String key) {
        return Optional.ofNullable(this.config.getString(key)).orElseThrow(() -> new RuntimeException("Config key " + key + " is null!"));
    }
}
