package fr.ribesg.bukkit.pure;

import fr.ribesg.bukkit.pure.util.HashUtils;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static void main(final String[] args) throws IOException {/*
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
        }*/
        try (final DirectoryStream<Path> s = Files.newDirectoryStream(Paths.get("jars"))) {
            for (final Path p : s) {
                System.out.println(p.getFileName() + "\n\t" + HashUtils.hashSha256(p));
            }
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
        if (id == null || id.isEmpty()) {
            Pure.getPluginLogger().severe("Parameters are required for the Pure world generator.");
            return null;
        }

        final String[] split = id.split(",");
        if (split.length > 2) {
            Pure.getPluginLogger().severe("Invalid id: " + id);
            return null;
        }

        final MCVersion version;
        final Environment environment;
        try {
            version = MCVersion.valueOf(split[0].toUpperCase());
        } catch (final IllegalArgumentException e) {
            Pure.getPluginLogger().severe("Invalid MC version String: " + split[0].toUpperCase());
            return null;
        }
        if (split.length > 1) {
            try {
                environment = Environment.valueOf(split[1].toUpperCase());
            } catch (final IllegalArgumentException e) {
                Pure.getPluginLogger().severe("Invalid Bukkit Environment String: " + split[1].toUpperCase());
                return null;
            }
        } else {
            environment = null;
        }

        try {
            MCJarHandler.require(version);
        } catch (final IOException e) {
            Pure.getPluginLogger().severe("Failed to install MC Version " + version);
            Pure.getPluginLogger().throwing(Pure.class.getCanonicalName(), "getDefaultWorldGenerator", e);
            return null;
        }

        return version.getChunkGenerator(environment);
    }
}
