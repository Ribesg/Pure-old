package fr.ribesg.bukkit.pure;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Lists Minecraft versions, with hashes of and links to their jar files.
 *
 * @author Ribesg
 */
public enum MCVersion {

    /**
     * Minecraft 1.8 released 2014-09-02.
     */
    RELEASE_1_8(
        "40E23F3823D6F0E3CBADC491CEDB55B8BA53F8AB516B68182DDD1536BABEB291",
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.8/minecraft_server.1.8.jar"
    ),

    /**
     * Minecraft 1.7.10 released 2014-06-26.
     */
    RELEASE_1_7_10(
        "C70870F00C4024D829E154F7E5f4E885B02DD87991726A3308D81f513972f3FC",
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.7.10/minecraft_server.1.7.10.jar"
    ),

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */;

    /**
     * Jar hash (SHA-256)
     */
    private final String hash;

    /**
     * Jar location
     */
    private final URL url;

    /**
     * Builds a MCVersion enum value.
     *
     * @param hash the jar hash (SHA-256)
     * @param url  the jar location
     */
    private MCVersion(final String hash, final String url) {
        this.hash = hash;
        try {
            this.url = new URL(url);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Gets the jar hash (SHA-256).
     *
     * @return the jar hash (SHA-256)
     */
    public String getHash() {
        return this.hash;
    }

    /**
     * Gets the URL.
     *
     * @return the URL
     */
    public URL getUrl() {
        return this.url;
    }
}
