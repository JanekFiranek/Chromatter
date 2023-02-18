package jf.chromatter;

public class ServerPlatform {

    private static final PlatformName PLATFORM_NAME;

    static {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        if (loader.getDefinedPackage("net.md_5.bungee.protocol") != null) {
            PLATFORM_NAME = PlatformName.BUNGEE;
        } else if (loader.getDefinedPackage("com.destroystokyo.paper") != null) {
            PLATFORM_NAME = PlatformName.PAPER;
        } else if (loader.getDefinedPackage("org.spigotmc.event") != null) {
            PLATFORM_NAME = PlatformName.SPIGOT;
        } else if (loader.getDefinedPackage("org.bukkit.craftbukkit") != null) {
            PLATFORM_NAME = PlatformName.BUKKIT;
        } else {
            PLATFORM_NAME = PlatformName.OTHER;
        }

    }

    public static PlatformName getPlatformName() {
        return PLATFORM_NAME;
    }

    public enum PlatformName {
        BUNGEE, BUKKIT, SPIGOT, PAPER, OTHER
    }
}
