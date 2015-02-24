package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import fr.ribesg.bukkit.pure.minecraft.AbstractProxyChunkGenerator;
import fr.ribesg.bukkit.pure.util.BiomeUtils;
import fr.ribesg.bukkit.pure.util.ReflectionUtils;
import net.minecraft.server.release_1_7_10.*;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyChunkGenerator extends AbstractProxyChunkGenerator {

    static {
        aji.p(); // Initialize Blocks
        adb.l(); // Initialize Items
    }

    private apu nmsGenerator;
    private ahb nmsWorld;

    public ProxyChunkGenerator(final Environment environment) {
        super(new ProxyBlockPopulator(), environment);
        this.environment = environment;
    }

    @Override
    protected boolean initializeNms(final World world) {
        try {
            this.nmsWorld = ReflectionUtils.newInstance(NmsProxyWorldServer.class);
            ReflectionUtils.set(this.nmsWorld.getClass(), this.nmsWorld, "world", world);
            final ays nmsWorldData = ReflectionUtils.newInstance(ays.class);
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "s", new Random());
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "u", new ArrayList());
            final NmsDummyChunkProvider nmsChunkProvider = new NmsDummyChunkProvider();
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "v", nmsChunkProvider);
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "z", ReflectionUtils.newInstance(NmsDummyPersistentCollection.class));
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "x", nmsWorldData);
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "a", world.getSeed());
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "b", ahm.a(world.getWorldType().getName()));
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "t", world.canGenerateStructures());
            final aqo nmsWorldProvider = aqo.a(this.environment.getId());
            nmsWorldProvider.a(this.nmsWorld);
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "t", nmsWorldProvider);
            this.nmsGenerator = nmsWorldProvider.c();
            ((ProxyBlockPopulator) this.blockPopulator).nmsGenerator = this.nmsGenerator;
            ((ProxyBlockPopulator) this.blockPopulator).nmsWorld = this.nmsWorld;
            ((ProxyBlockPopulator) this.blockPopulator).nmsChunkProvider = nmsChunkProvider;
        } catch (final ReflectiveOperationException e) {
            e.printStackTrace(); // TODO Logging
            return false;
        }
        this.nmsInitialized = true;
        return true;
    }

    @Override
    protected short[][] generateChunk(final World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        // generation
        final apx nmsChunk = this.nmsGenerator.c(x, z);
        final apz[] nmsChunkSections = nmsChunk.i();

        // set biomes
        int cursorX;
        int cursorZ;
        final byte[] biomeBytes = nmsChunk.m();
        for (cursorX = 0; cursorX < 16; cursorX++) {
            for (cursorZ = 0; cursorZ < 16; cursorZ++) {
                biomes.setBiome(cursorX, cursorZ, Biome.valueOf(BiomeUtils.translateBiomeName(ahu.d(biomeBytes[(cursorZ << 4) | cursorX]).af)));
            }
        }

        // set result
        final int maxHeight = world.getMaxHeight();
        final short[][] result = new short[maxHeight / 16][];
        apz nmsChunkSection;
        for (int i = 0; i < result.length; i++) {
            nmsChunkSection = nmsChunkSections[i];
            if (nmsChunkSection == null) {
                continue;
            }
            result[i] = new short[16 * 16 * 16];
            final byte[] idArray = nmsChunkSection.g();
            for (int j = 0; j < idArray.length; j++) {
                result[i][j] = idArray[j];
            }
        }

        // block populator
        ((ProxyBlockPopulator) this.blockPopulator).addChunk(nmsChunk);

        return result;
    }
}
