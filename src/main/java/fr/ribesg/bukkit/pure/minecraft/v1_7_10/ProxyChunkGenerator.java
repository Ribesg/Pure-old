package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import fr.ribesg.bukkit.pure.minecraft.AbstractProxyChunkGenerator;
import org.bukkit.World;
import org.bukkit.World.Environment;

import java.util.Random;

/**
 * @author Ribesg
 */
public class ProxyChunkGenerator extends AbstractProxyChunkGenerator {

    public ProxyChunkGenerator(final Environment environment) {
        super(new ProxyBlockPopulator(), environment);
        this.environment = environment;
    }

    @Override
    protected boolean initializeNms() {
        return false;
    }

    @Override
    protected short[][] generateChunk(final World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        return new short[0][];
    }
}
