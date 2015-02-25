package fr.ribesg.bukkit.pure;

import fr.ribesg.bukkit.pure.util.FileUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Loads and relocate classes of the Minecraft Server.
 *
 * @author Ribesg
 */
public final class MCJarHandler {

    private static final Logger LOGGER = Pure.getPluginLogger();

    private static final Map<MCVersion, Boolean> LOADED = new EnumMap<>(MCVersion.class);

    static {
        for (final MCVersion v : MCVersion.values()) {
            MCJarHandler.LOADED.put(v, false);
        }
    }

    /**
     * Requires a Minecraft Server Version.
     *
     * This method will first check if the required version exists on the disk.
     * If it doesn't exist it will download the original Minecraft Server jar
     * file from Mojang source then remap the jar classes into a new .remapped.jar
     * file.
     * The method will then initialize a {@link ClassLoader} for this jar file
     * and load all the classes in the jar file.
     *
     * @param version the required version
     *
     * @throws IOException if anything goes wrong
     */
    public static void require(final MCVersion version) throws IOException {
        LOGGER.entering(MCJarHandler.class.getName(), "require");

        if (!MCJarHandler.LOADED.get(version)) {
            // Find (and eventually create) our plugin's folder subfolder containing jar files (plugin/Pure/jars)
            final Path jarContainerPath = Paths.get(Pure.getFolder().getAbsolutePath(), "jars");
            if (!Files.isDirectory(jarContainerPath)) {
                Files.createDirectories(jarContainerPath);
            }

            // Find jar file name from the Minecraft jar URL
            final String[] split = version.getUrl().toString().split("/");
            final String inputJarName = split[split.length - 1];

            // Now we build the final (future?) Path of both our jar file and its remapped version on the file system
            final Path jarPath = jarContainerPath.resolve(inputJarName);
            final Path remappedJarPath = jarContainerPath.resolve(inputJarName.substring(0, inputJarName.length() - 4) + ".remapped.jar"); // -4 == ".jar"

            // Download the Vanilla jar file if it doesn't exist
            if (!Files.exists(jarPath)) {
                FileUtils.download(jarContainerPath, version.getUrl(), inputJarName, version.getHash());
            }

            // Remove old remapped jar if it exists
            Files.deleteIfExists(remappedJarPath);

            // Relocate the jar classes packages and put that into our remapped jar file
            FileUtils.relocateJarContent(jarPath, remappedJarPath, version.name().toLowerCase());

            // Load the remapped jar using our current classloader
            try {
                final Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURL.setAccessible(true);
                addURL.invoke(ClassLoader.getSystemClassLoader(), remappedJarPath.toFile().toURI().toURL());
            } catch (final ReflectiveOperationException e) {
                LOGGER.severe("Failed to load classes of " + remappedJarPath);
                LOGGER.throwing(MCJarHandler.class.getCanonicalName(), "require", e);
            }
        }

        LOGGER.exiting(MCJarHandler.class.getName(), "require");
    }
}
