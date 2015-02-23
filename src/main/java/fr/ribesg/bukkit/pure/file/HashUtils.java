package fr.ribesg.bukkit.pure.file;

import fr.ribesg.bukkit.pure.Pure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Used to hash things.
 *
 * @author Ribesg
 */
public final class HashUtils {

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
            final byte[] buffer = new byte[HashUtils.BUFFER_SIZE];
            long lastIndex = 0;
            final long endIndex = file.length();
            int readSize;
            while (lastIndex < endIndex) {
                readSize = (int) (((endIndex - lastIndex) >= HashUtils.BUFFER_SIZE) ? HashUtils.BUFFER_SIZE : (endIndex - lastIndex));
                file.read(buffer, 0, readSize);
                digest.update(buffer, 0, readSize);
                lastIndex += readSize;
            }

            LOGGER.exiting(FileUtils.class.getName(), "hashSha256");

            return HashUtils.bytesToHex(digest.digest());
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
            final int v = bytes[j] & 0xFF;
            result[j * 2] = HashUtils.HEXADECIMAL_CHARACTERS[v >>> 4];
            result[j * 2 + 1] = HashUtils.HEXADECIMAL_CHARACTERS[v & 0x0F];
        }
        return new String(result);
    }
}
