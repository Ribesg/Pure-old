package fr.ribesg.bukkit.pure.file;

import com.tonicsystems.jarjar.Main;
import fr.ribesg.bukkit.pure.Pure;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Used to download things.
 *
 * @author Ribesg
 */
public final class FileUtils {

    private static final Logger LOGGER = Pure.getPluginLogger();

    /**
     * Downloads a file.
     *
     * @param destinationFolder the folder in which the file will be saved
     * @param sourceUrl         the URL to the file to download
     * @param wantedFileName    the final name of the saved file
     * @return the path to the downloaded file
     */
    public static Path download(final Path destinationFolder, final URL sourceUrl, final String wantedFileName) throws IOException {
        FileUtils.LOGGER.entering(FileUtils.class.getName(), "download");

        if ((!Files.exists(destinationFolder) || !Files.isDirectory(destinationFolder)) && !destinationFolder.toFile().mkdirs()) {
            throw new IOException("Folder " + destinationFolder.toString() + " doesn't exist and cannot be created");
        }
        final File finalFile = new File(destinationFolder.toFile(), wantedFileName);
        try (
            final InputStream is = sourceUrl.openStream();
            final ReadableByteChannel source = Channels.newChannel(is);
            final FileOutputStream out = new FileOutputStream(finalFile)
        ) {
            FileUtils.LOGGER.fine("Downloading " + sourceUrl + "...");
            out.getChannel().transferFrom(source, 0, Long.MAX_VALUE);
            FileUtils.LOGGER.fine("Done!");
        }

        FileUtils.LOGGER.exiting(FileUtils.class.getName(), "download");

        return finalFile.toPath();
    }

    /**
     * Relocates classes in a jar file, building a new modified jar.
     *
     * @param inputJar  input jar file
     * @param outputJar output jar file
     * @param pattern   source package in input jar file
     * @param result    destination package in output jar file
     * @throws IOException if anything goes wrong
     */
    public static void relocateJarContent(final Path inputJar, final Path outputJar, final String pattern, final String result) throws IOException {
        FileUtils.LOGGER.entering(FileUtils.class.getName(), "relocateJarContent");

        final String rulesFilePath = inputJar.toAbsolutePath().toString() + ".tmp";

        // Create JarJar rules file
        final File rulesFile = Paths.get(rulesFilePath).toFile();
        if (rulesFile.exists()) {
            if (!rulesFile.delete()) {
                throw new IOException("Failed to remove old rules file " + rulesFilePath);
            }
        }
        if (!rulesFile.createNewFile()) {
            throw new IOException("Failed to create rules file " + rulesFilePath);
        }

        // Write rule
        try (final BufferedWriter writer = Files.newBufferedWriter(rulesFile.toPath())) {
            writer.write("rule " + pattern + " " + result);
        }

        // Execute JarJar
        try {
            FileUtils.LOGGER.fine("Executing JarJar...");
            Main.main(new String[] {
                "process",
                rulesFilePath,
                inputJar.toString(),
                outputJar.toString()
            });
            FileUtils.LOGGER.fine("Done!");

            FileUtils.LOGGER.exiting(FileUtils.class.getName(), "relocateJarContent");
        } catch (final Exception e) {
            throw new IOException("Failed to execute JarJar", e);
        } finally {
            if (!rulesFile.delete()) {
                FileUtils.LOGGER.warning("Failed to remove rules file after execution");
            }
        }
    }
}
