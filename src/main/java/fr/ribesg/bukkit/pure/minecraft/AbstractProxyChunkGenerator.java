package fr.ribesg.bukkit.pure.minecraft;

import fr.ribesg.bukkit.pure.Pure;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Ribesg
 */
public abstract class AbstractProxyChunkGenerator extends ChunkGenerator {

    private static final Logger LOGGER = Pure.getPluginLogger();

    protected BlockPopulator blockPopulator;
    protected Environment    environment;
    protected boolean        nmsInitialized;

    protected AbstractProxyChunkGenerator(final BlockPopulator blockPopulator, final Environment environment) {
        this.blockPopulator = blockPopulator;
        this.environment = environment;
        this.nmsInitialized = false;
    }

    @Override
    public short[][] generateExtBlockSections(final World world, final Random random, final int x, final int z, final ChunkGenerator.BiomeGrid biomes) {
        if (!this.nmsInitialized && !this.initializeNms(world)) {
            return null;
        }

        return this.generateChunk(world, random, x, z, biomes);
    }

    protected abstract boolean initializeNms(final World world);

    protected abstract short[][] generateChunk(final World world, final Random random, final int x, final int z, final ChunkGenerator.BiomeGrid biomes);

    @Override
    public List<BlockPopulator> getDefaultPopulators(final World world) {
        if (!this.nmsInitialized && !this.initializeNms(world)) {
            return null;
        }
        return Arrays.asList(this.blockPopulator);
    }
}
