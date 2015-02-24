package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import fr.ribesg.bukkit.pure.util.HashUtils;
import net.minecraft.server.release_1_7_10.ahb;
import net.minecraft.server.release_1_7_10.apu;
import net.minecraft.server.release_1_7_10.apx;
import net.minecraft.server.release_1_7_10.apz;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyBlockPopulator extends BlockPopulator {

    /*package */ apu                   nmsGenerator;
    /*package */ ahb                   nmsWorld;
    /*package */ NmsDummyChunkProvider nmsChunkProvider;

    private final Map<Long, apx> nmsChunks = new HashMap<>();

    public void addChunk(final apx chunk) {
        this.nmsChunks.put(HashUtils.toLong(chunk.g, chunk.h), chunk);
    }

    @SuppressWarnings("deprecation")
    public void populate(final World world, final Random random, final Chunk chunk) {
        apx nmsChunk = this.nmsChunks.remove(HashUtils.toLong(chunk.getX(), chunk.getZ()));
        if (nmsChunk == null) {
            nmsChunk = this.nmsGenerator.c(chunk.getX(), chunk.getZ());
        }
        final apz[] nmsChunkSections = nmsChunk.i();
        apz nmsChunkSection;
        for (int i = 0; i < nmsChunkSections.length; i++) {
            nmsChunkSection = nmsChunkSections[i];
            if (nmsChunkSection == null) {
                continue;
            }

            int meta;
            for (int y = 0; y < 16; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        meta = nmsChunkSection.b(x, y, z);
                        if (meta == 0) {
                            continue;
                        }
                        chunk.getBlock(x, (i * 16) + y, z).setData((byte) meta);
                    }
                }
            }
        }
        this.nmsChunkProvider.chunk = nmsChunk;
        this.nmsGenerator.a(null, chunk.getX(), chunk.getZ());
        this.nmsChunkProvider.chunk = null;
    }
}
