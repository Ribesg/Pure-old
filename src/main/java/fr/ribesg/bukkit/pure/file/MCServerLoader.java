package fr.ribesg.bukkit.pure.file;

import fr.ribesg.bukkit.pure.Pure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Loads and relocate classes of the Minecraft Server.
 *
 * @author Ribesg
 */
public final class MCServerLoader {

    private static final Logger LOGGER = Pure.getPluginLogger();

    /*
     * TODO: Everything! This should use Jar Jar Links things and then some Classloader things
     */

    public static void load(final MCServerVersion version) throws IOException {
        LOGGER.entering(MCServerLoader.class.getName(), "load");

        final String folder = Pure.getFolder().getAbsolutePath();
        final String[] split = version.getUrl().toString().split("/");
        final String inputJarName = split[split.length - 1];
        final Path inputJarPath = Paths.get(folder, "jars", inputJarName);
        if (!Files.exists(inputJarPath)) {
            FileUtils.download(Paths.get(folder, "jars"), version.getUrl(), inputJarName);
        }
        FileUtils.relocateJarContent(inputJarPath, Paths.get(folder, "jars", inputJarName + ".remapped"), "**", version.name().toLowerCase() + ".@1");

        LOGGER.exiting(MCServerLoader.class.getName(), "load");
    }
}
