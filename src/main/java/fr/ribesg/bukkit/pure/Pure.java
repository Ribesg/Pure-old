package fr.ribesg.bukkit.pure;

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

    private static final Level LOG_LEVEL = Level.ALL;

    public static void main(final String[] args) throws IOException {
        Pure.getPluginLogger().setLevel(Pure.LOG_LEVEL);
        final ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Pure.LOG_LEVEL);
        Pure.getPluginLogger().addHandler(handler);
        MCJarHandler.require(MCVersion.RELEASE_1_7_10);
    }

    /**
     * Static Pure Logger accessor.
     *
     * @return the Logger of the Pure Bukkit plugin
     */
    public static Logger getPluginLogger() {
        if (Pure.instance == null) {
            return Logger.getLogger("Pure");
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
