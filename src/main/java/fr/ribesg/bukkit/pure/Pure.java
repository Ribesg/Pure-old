package fr.ribesg.bukkit.pure;

import fr.ribesg.bukkit.pure.file.MCServerHandler;
import fr.ribesg.bukkit.pure.file.MCServerVersion;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is Pure.
 *
 * @author Ribesg
 */
public final class Pure extends JavaPlugin {

    public static void main(final String[] args) throws IOException {
        Pure.getPluginLogger().setLevel(Level.ALL);
        Pure.getPluginLogger().addHandler(new ConsoleHandler());
        MCServerHandler.require(MCServerVersion.RELEASE_1_7_10);
    }

    /**
     * Static Pure Logger accessor.
     *
     * @return the Logger of the Pure Bukkit plugin
     */
    public static Logger getPluginLogger() {
        if (Pure.instance == null) {
            return Logger.getGlobal();
        }
        return Pure.instance.getLogger();
    }

    /**
     * Static Pure data folder accessor.
     *
     * @return the data folder of the Pure Bukkit plugin
     */
    public static File getFolder() {
        if (Pure.instance == null) {
            return new File("");
        }
        return Pure.instance.getDataFolder();
    }

    /**
     * Private instance, to be used by static accessors.
     */
    private static Pure instance = null;

    @Override
    public void onEnable() {
        Pure.instance = this;
    }

    @Override
    public void onDisable() {
        Pure.instance = null;
    }
}
