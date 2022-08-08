package jf.chromatools;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChromaToolsBukkit extends JavaPlugin {

    private ColorFormatter formatter;
    public ColorFormatter getColorFormatter(){ return this.formatter; }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.formatter = new ColorFormatter(this.getLogger(), this.getConfig());
       // this.getServer().getPluginManager().registerEvents(new TextListener(this.getConfig(), formatter), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
