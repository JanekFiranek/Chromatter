package tech.firanek.chromatter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import tech.firanek.chromatter.chat.ChatCode;
import tech.firanek.chromatter.chat.format.ChatFormatter;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class ChromatterBukkit extends JavaPlugin {

    //MockBukkit integration
    private ChromatterBukkit(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    public ChromatterBukkit() {
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
        //for the bri'ish
        this.getLogger().info("Loaded " + colorMap.size() + " custom colo" + (Locale.getDefault().equals(Locale.UK) ? "u" : "") + "rs from config.");
    }
}
