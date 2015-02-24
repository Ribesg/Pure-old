package fr.ribesg.bukkit.pure.util;

import com.tonicsystems.jarjar.Main;
import fr.ribesg.bukkit.pure.Pure;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Used to download things.
 *
 * @author Ribesg
 */
public final class FileUtils {

    private static final Logger LOGGER = Pure.getPluginLogger();

    private static final int MAX_DOWNLOAD_ATTEMPS = 5;

    /**
     * Downloads a file.
     *
     * @param destinationFolder the folder in which the file will be saved
     * @param sourceUrl         the URL to the file to download
     * @param wantedFileName    the final name of the saved file
     *
     * @return the path to the downloaded file
     */
    public static Path download(final Path destinationFolder, final URL sourceUrl, final String wantedFileName, final String wantedHash) throws IOException {
        LOGGER.entering(FileUtils.class.getName(), "download");

        if ((!Files.exists(destinationFolder) || !Files.isDirectory(destinationFolder)) && !destinationFolder.toFile().mkdirs()) {
            throw new IOException("Folder " + destinationFolder.toString() + " doesn't exist and cannot be created");
        }
        final File finalFile = new File(destinationFolder.toFile(), wantedFileName);

        int attempt = 1;
        while (true) {
            try (
                final InputStream is = sourceUrl.openStream();
                final ReadableByteChannel source = Channels.newChannel(is);
                final FileOutputStream out = new FileOutputStream(finalFile)
            ) {
                LOGGER.fine("Downloading " + sourceUrl + " ...");
                out.getChannel().transferFrom(source, 0, Long.MAX_VALUE);
                LOGGER.fine("Done! Checking hash...");
                final String hash = HashUtils.hashSha256(finalFile.toPath());
                if (hash.equals(wantedHash)) {
                    LOGGER.fine("The downloaded file is correct!");
                    break;
                } else {
                    LOGGER.warning("The downloaded file is incorrect!");
                    throw new IOException("Download file hash doesn't match awaited hash\nAwaited: " + wantedHash + "\nReceived: " + hash);
                }
            } catch (final IOException e) {
                LOGGER.warning("Attempt n°" + attempt + " failed!");
                if (attempt == FileUtils.MAX_DOWNLOAD_ATTEMPS) {
                    throw new IOException("Failed to download file", e);
                } else {
                    LOGGER.throwing(FileUtils.class.getName(), "download", e);
                }
            }
            attempt++;
        }

        LOGGER.exiting(FileUtils.class.getName(), "download");

        return finalFile.toPath();
    }

    /**
     * Relocates classes in a jar file, building a new modified jar.
     *
     * @param inputJar  input jar file
     * @param outputJar output jar file
     * @param prefix    prefix for all packages
     *
     * @throws IOException if anything goes wrong
     */
    public static void relocateJarContent(final Path inputJar, final Path outputJar, final String prefix) throws IOException {
        LOGGER.entering(FileUtils.class.getName(), "relocateJarContent");

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

        // Generate and write rules
        try (
            final ZipFile zipFile = new ZipFile(inputJar.toFile());
            final BufferedWriter writer = Files.newBufferedWriter(rulesFile.toPath())
        ) {
            final Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            ZipEntry entry;
            String entryName;
            while (enumeration.hasMoreElements()) {
                entry = enumeration.nextElement();
                entryName = entry.getName();
                if (!entryName.contains("META-INF") && !entry.isDirectory() &&
                    !entryName.contains("/") && entryName.endsWith(".class")) {
                    writer.write("rule " + entryName.replace(".class", "") + " net.minecraft.server." + prefix + ".@0\n");
                }
            }
            writer.write("rule **.* @1." + prefix + ".@2\n");
        }

        // Execute JarJar
        try {
            LOGGER.fine("Executing JarJar...");
            Main.main(new String[] {
                "process",
                rulesFilePath,
                inputJar.toString(),
                outputJar.toString()
            });
            LOGGER.fine("Done!");

            LOGGER.exiting(FileUtils.class.getName(), "relocateJarContent");
        } catch (final Exception e) {
            throw new IOException("Failed to execute JarJar", e);
        } finally {
            if (!rulesFile.delete()) {
                LOGGER.warning("Failed to remove rules file after execution");
            }
        }
    }
}
