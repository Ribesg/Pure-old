package fr.ribesg.bukkit.pure.file;

import fr.ribesg.bukkit.pure.Pure;

import java.io.IOException;
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
public final class MCServerHandler {

    private static final Logger LOGGER = Pure.getPluginLogger();

    /**
     * Contains existing {@link ClassLoader}s for loaded Minecraft Versions.
     */
    private static final Map<MCServerVersion, ClassLoader> CLASSLOADERS = new EnumMap<>(MCServerVersion.class);

    /**
     * Gets a ClassLoader matching the provided Minecraft Version.
     *
     * @param version the Minecraft version
     *
     * @return a ClassLoader matching the provided Minecraft Version
     *
     * @throws IOException if anything wrong occurs
     */
    public static ClassLoader getClassLoader(final MCServerVersion version) throws IOException {
        LOGGER.entering(MCServerHandler.class.getName(), "getClassLoader");

        ClassLoader result = MCServerHandler.CLASSLOADERS.get(version);
        if (result == null) {
            MCServerHandler.require(version);
            result = MCServerHandler.CLASSLOADERS.get(version);
        }

        LOGGER.exiting(MCServerHandler.class.getName(), "getClassLoader");
        return result;
    }

    /**
     * Requires a Minecraft Server Version.
     *
     * This method will first check if the required version exists on the disk.
     * If it doesn't exist it will download the original Minecraft Server jar
     * file from Mojang source then remap the jar classes into a new .remapped.jar
     * file.
     * The method will then initialize and save a {@link ClassLoader} for this
     * jar file for easy access with the {@link #getClassLoader} method.
     *
     * @param version the required version
     *
     * @throws IOException if anything goes wrong
     */
    public static void require(final MCServerVersion version) throws IOException {
        LOGGER.entering(MCServerHandler.class.getName(), "require");

        if (MCServerHandler.CLASSLOADERS.get(version) == null) {
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

            // Relocate the jar classes packages and put that into our remapped jar file
            FileUtils.relocateJarContent(jarPath, remappedJarPath, "**", version.name().toLowerCase() + ".@1");

            // Load the remapped jar using our current classloader
            final URL[] urls = {new URL("jar:file:" + remappedJarPath.toString() + "!/")};
            final ClassLoader jarClassLoader = URLClassLoader.newInstance(urls);
            MCServerHandler.CLASSLOADERS.put(version, jarClassLoader);
        }

        LOGGER.exiting(MCServerHandler.class.getName(), "require");
    }
}
