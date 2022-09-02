package jf.chromater;

import jf.chromater.chat.ChatCode;
import jf.chromater.chat.format.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class ChromaterBukkit extends JavaPlugin {

    //MockBukkit integration
    private ChromaterBukkit(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    public ChromaterBukkit() {
        super();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        final Map<String, ChatCode> colorMap = new HashMap<>();
        final ConfigurationSection customColors = Objects.requireNonNull(this.getConfig().getConfigurationSection("custom_colors"));
        for (final String key : customColors.getKeys(false)) {
            try {
                colorMap.put(key, new ChatCode(Objects.requireNonNull(customColors.getString(key))));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                this.getLogger().severe("Error parsing custom color " + key + " due to improper hex format.");
            }
        }
        ChatFormatter.setColorMap(colorMap);
        //lol
        this.getLogger().info("Loaded " + colorMap.size() + " custom colo" + (Locale.getDefault().equals(Locale.UK) ? "u" : "") + "rs from config.");
    }
}
