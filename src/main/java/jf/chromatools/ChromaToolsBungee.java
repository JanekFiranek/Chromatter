package jf.chromatools;

import jf.chromatools.chat.ChatCode;
import jf.chromatools.chat.format.ChatFormatter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ChromaToolsBungee extends Plugin {

    @Override
    public void onEnable() {
        final File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try (final FileOutputStream stream = new FileOutputStream(configFile)) {
                this.getResourceAsStream(configFile.getName()).transferTo(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            final Map<String, ChatCode> colorMap = new HashMap<>();
            final Configuration customColors = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile).getSection("custom_colors");
            for (final String key : customColors.getKeys()) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
