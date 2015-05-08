package fr.ribesg.bukkit.pure;

import fr.ribesg.bukkit.pure.util.FileUtils;
import fr.ribesg.bukkit.pure.util.HashUtils;

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
     * @param version   the required version
     * @param checkHash if we check for file hashes or not
     *
     * @throws IOException if anything goes wrong
     */
    public static void require(final MCVersion version, final boolean checkHash) throws IOException {
        LOGGER.entering(MCJarHandler.class.getName(), "require");

        if (!MCJarHandler.LOADED.get(version)) {
            LOGGER.info("Minecraft Version " + version.name() + " required for World Generation");
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

            // Download the Vanilla jar file
            if (!Files.exists(jarPath)) {
                // Doesn't exist, just download it
                LOGGER.info("Downloading file " + version.getUrl() + " ...");
                FileUtils.download(jarContainerPath, version.getUrl(), inputJarName, checkHash ? version.getVanillaHash() : null);
                LOGGER.info("Done downloading file!");
            } else if (checkHash) {
                // Already exists, check hash and redownload it if needed
                LOGGER.info("Hashing existing jar to make sure it's correct...");
                final String hash = HashUtils.hashSha256(jarPath);
                if (version.getVanillaHash().equals(hash)) {
                    LOGGER.info("Vanilla jar hash correct!");
                } else {
                    LOGGER.info("Invalid hash, redownloading file " + version.getUrl() + " ...");
                    Files.delete(jarPath);
                    FileUtils.download(jarContainerPath, version.getUrl(), inputJarName, version.getVanillaHash());
                }
            }

            // Relocate the jar classes packages and put that into our remapped jar file
            if (!Files.exists(remappedJarPath)) {
                // Doesn't exist, just relocate it
                LOGGER.info("Relocating jar file classes (this takes time!)...");
                FileUtils.relocateJarContent(jarPath, remappedJarPath, version, checkHash);
                LOGGER.info("Done reloacting jar file classes!");
            } else if (checkHash) {
                // Already exists, check hash and remap if needed
                LOGGER.info("Hashing existing remapped jar to make sure it's correct...");
                final String hash = HashUtils.hashSha256(remappedJarPath);
                if (version.getRemappedHash().equals(hash)) {
                    LOGGER.info("Remapped jar hash correct!");
                } else {
                    LOGGER.info("Invalid hash, remapping file...");
                    Files.delete(remappedJarPath);
                    FileUtils.relocateJarContent(jarPath, remappedJarPath, version, true);
                }
            }

            // Load the remapped jar using our current classloader
            try {
                final Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURL.setAccessible(true);
                addURL.invoke(ClassLoader.getSystemClassLoader(), remappedJarPath.toFile().toURI().toURL());
                LOGGER.info("Minecraft Version " + version.name() + " ready!");
            } catch (final ReflectiveOperationException e) {
                LOGGER.severe("Failed to load classes of " + remappedJarPath);
                LOGGER.throwing(MCJarHandler.class.getCanonicalName(), "require", e);
            }
        }

        LOGGER.exiting(MCJarHandler.class.getName(), "require");
    }
}
