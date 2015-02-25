package fr.ribesg.bukkit.pure;

import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * This is Pure.
 *
 * @author Ribesg
 */
public final class Pure extends JavaPlugin {

    private static final Level LOG_LEVEL = Level.FINE;

    /**
     * Download and remap all known MC version.
     */
    public static void main(final String[] args) throws IOException {
        Pure.getPluginLogger().setLevel(Pure.LOG_LEVEL);
        final ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Pure.LOG_LEVEL);
        handler.setFormatter(new Formatter() {
            @Override
            public String format(final LogRecord record) {
                return this.formatMessage(record) + '\n';
            }
        });
        Pure.getPluginLogger().addHandler(handler);

        for (final MCVersion v : MCVersion.values()) {
            MCJarHandler.require(v);
        }
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

    @Override
    public ChunkGenerator getDefaultWorldGenerator(final String worldName, final String id) {
        final String[] split = id.split(",");
        final MCVersion version;
        final Environment environment;
        if (split.length > 0 && split.length < 3) {
            version = MCVersion.valueOf(split[0].toUpperCase());
        } else {
            Pure.getPluginLogger().severe("Invalid id");
            return null;
        }
        if (split.length > 1) {
            environment = Environment.valueOf(split[1].toUpperCase());
        } else {
            environment = null;
        }
        return version.getChunkGenerator(environment);
    }
}
