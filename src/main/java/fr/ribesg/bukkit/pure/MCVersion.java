package fr.ribesg.bukkit.pure;

import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;

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
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.8/minecraft_server.1.8.jar",
        fr.ribesg.bukkit.pure.minecraft.v1_8.ProxyChunkGenerator.class
    ),

    /**
     * Minecraft 1.7.10 released 2014-06-26.
     */
    RELEASE_1_7_10(
        "C70870F00C4024D829E154F7E5F4E885B02DD87991726A3308D81F513972F3FC",
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.7.10/minecraft_server.1.7.10.jar",
        fr.ribesg.bukkit.pure.minecraft.v1_7_10.ProxyChunkGenerator.class
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
     * Proxy ChunkGenerator class
     */
    private final Class<? extends ChunkGenerator> chunkGeneratorClass;

    /**
     * Builds a MCVersion enum value.
     *
     * @param hash the jar hash (SHA-256)
     * @param url  the jar location
     */
    private MCVersion(final String hash, final String url, final Class<? extends ChunkGenerator> chunkGeneratorClass) {
        this.hash = hash;
        try {
            this.url = new URL(url);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        this.chunkGeneratorClass = chunkGeneratorClass;
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

    /**
     * Gets a new instance of a Chunk Generator matching this version.
     *
     * @param environment the required environment
     *
     * @return the Chunk Generator instance
     */
    public ChunkGenerator getChunkGenerator(final Environment environment) {
        try {
            try {
                return this.chunkGeneratorClass.getDeclaredConstructor(Environment.class).newInstance(environment);
            } catch (final NoSuchMethodException e1) {
                try {
                    return this.chunkGeneratorClass.getDeclaredConstructor().newInstance();
                } catch (final NoSuchMethodException e2) {
                    throw new RuntimeException("Associated proxy ChunkGenerator class has no valid constructor ("
                                               + this.chunkGeneratorClass.getCanonicalName() + ')');
                }
            }
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException("Failed to call associated proxy ChunkGenerator class constructor");
        }
    }
}
