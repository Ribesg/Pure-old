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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Used to download and hash things.
 *
 * @author Ribesg
 */
public final class FileUtils {

    private static final Logger LOGGER = Pure.getPluginLogger();

    /**
     * Buffer for file reading. Set to 1 Mo.
     */
    private static final int BUFFER_SIZE = 1024 * 1024 * 1024;

    /**
     * An array of hexadecimal characters.
     */
    private static final char[] HEXADECIMAL_CHARACTERS = "0123456789ABCDEF".toCharArray();

    /**
     * Downloads a file.
     *
     * @param destinationFolder the folder in which the file will be saved
     * @param sourceUrl         the URL to the file to download
     * @param wantedFileName    the final name of the saved file
     * @return the path to the downloaded file
     */
    public static Path download(final Path destinationFolder, final URL sourceUrl, final String wantedFileName) throws IOException {
        LOGGER.entering(FileUtils.class.getName(), "download");

        if ((!Files.exists(destinationFolder) || !Files.isDirectory(destinationFolder)) && !destinationFolder.toFile().mkdirs()) {
            throw new IOException("Folder " + destinationFolder.toString() + " doesn't exist and cannot be created");
        }
        final File finalFile = new File(destinationFolder.toFile(), wantedFileName);
        try (
            final InputStream is = sourceUrl.openStream();
            final ReadableByteChannel source = Channels.newChannel(is);
            final FileOutputStream out = new FileOutputStream(finalFile)
        ) {
            LOGGER.fine("Downloading " + sourceUrl + "...");
            out.getChannel().transferFrom(source, 0, Long.MAX_VALUE);
            LOGGER.fine("Done!");
        }

        LOGGER.exiting(FileUtils.class.getName(), "download");

        return finalFile.toPath();
    }

    /**
     * Hashes a file using the SHA-256 algorithm.
     *
     * @param filePath a file
     */
    public static String hashSha256(final Path filePath) throws IOException {
        LOGGER.entering(FileUtils.class.getName(), "hashSha256");

        try (
            final RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")
        ) {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] buffer = new byte[BUFFER_SIZE];
            long lastIndex = 0;
            final long endIndex = file.length();
            int readSize;
            while (lastIndex < endIndex) {
                readSize = (int) (((endIndex - lastIndex) >= BUFFER_SIZE) ? BUFFER_SIZE : (endIndex - lastIndex));
                file.read(buffer, 0, readSize);
                digest.update(buffer, 0, readSize);
                lastIndex += readSize;
            }

            LOGGER.exiting(FileUtils.class.getName(), "hashSha256");

            return FileUtils.bytesToHex(digest.digest());
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to encrypt using SHA-256", e);
        }
    }

    /**
     * Converts a byte array to an hexadecimal representation of it.
     *
     * @param bytes a byte array
     * @return a String representing the byte array
     */
    private static String bytesToHex(final byte[] bytes) {
        final char[] result = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            result[j * 2] = HEXADECIMAL_CHARACTERS[v >>> 4];
            result[j * 2 + 1] = HEXADECIMAL_CHARACTERS[v & 0x0F];
        }
        return new String(result);
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
    public static void relocateJarContent(final Path inputJar, final Path outputJar, String pattern, String result) throws IOException {
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

        // Write rule
        try (final BufferedWriter writer = Files.newBufferedWriter(rulesFile.toPath())) {
            writer.write("rule " + pattern + " " + result);
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
