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
        aji.p(); // Block.registerBlocks();
        adb.l(); // Item.registerItems();
    }

    private apu nmsGenerator;   // IChunkProvider
    private ahb nmsWorld;       // World

    public ProxyChunkGenerator(final Environment environment) {
        super(new ProxyBlockPopulator(), environment);
        this.environment = environment;
    }

    @Override
    protected boolean initializeNms(final World world) {
        try {
            /*
             * Create an instance of NmsProxyWorldServer then:
             * - Set field world             of NmsProxyWorldServer to the provided Bukkit world
             * - Set field rand (s)          of World               to a new Random instance
             * - Set field worldAccesses (u) of World               to a new ArrayList instance
             * Notes:
             * - NmsProxyServer extends WorldServer (mt) which extends World (ahb)
             */
            this.nmsWorld = ReflectionUtils.newInstance(NmsProxyWorldServer.class);
            ReflectionUtils.set(this.nmsWorld.getClass(), this.nmsWorld, "world", world);
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "s", new Random());
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "u", new ArrayList());

            /*
             * Create an instance of NmsDummyMapStorage then:
             * - Set field mapStorage (z) of World to the newly created dummy MapStorage
             * Notes:
             * - NmsDummyMapStorage extends MapStorage (azq)
             */
            final NmsDummyMapStorage mapStorage = ReflectionUtils.newInstance(NmsDummyMapStorage.class);
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "z", mapStorage);

            /*
             * Create an instance of WorldInfo then:
             * - Set field randomSeed (a)         of WorldInfo to the seed of our Bukkit world
             * - Set field terrainType (b)        of WorldInfo to the converted Bukkit world worldType
             * - Set field mapFeaturesEnabled (t) of WorldInfo to the canGenerateStructure parameter of our Bukkit world
             * - Set field worldInfo (x)          of World     to the newly created WorldInfo instance
             * Notes:
             * - (ahm)           is the obfuscated class  name of WorldType
             * - (ahm.a(String)) is the obfuscated method name of WorldType.parseWorldType(String)
             * - (ays)           is the obfuscated class  name of WorldInfo
             */
            final ays nmsWorldData = ReflectionUtils.newInstance(ays.class);
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "a", world.getSeed());
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "b", ahm.a(world.getWorldType().getName()));
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "t", world.canGenerateStructures());
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "x", nmsWorldData);

            /*
             * Create an instance of NmsDummyChunkProvider then:
             * - Set field chunkProvider (v) of World to the newly created dummy ChunkProvider instance
             * Notes:
             * - NmsDummyChunkProvider implements IChunkProvider (apu)
             */
            final NmsDummyChunkProvider nmsChunkProvider = new NmsDummyChunkProvider();
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "v", nmsChunkProvider);

            /*
             * Gets a new WorldProvider matching the passed Environment then:
             * - Registers our World instance with the WorldProvider
             * - Set field provider (t) of World to the newly created WorldProvider instance
             * - Create a ChunkGenerator instance using the WorldProvider
             * Notes:
             * - (aqo)        is the obfuscated class  name of WorldProvider
             * - (aqo.a(int)) is the obfuscated method name of WorldProvider.getProviderForDimension(int)
             * - (aqo.a(ahb)) is the obfuscated method name of WorldProvider.registerWorld(World)
             * - (aqo.c())    is the obfuscated method name of WorldProvider.createChunkGenerator()
             */
            final aqo nmsWorldProvider = aqo.a(this.environment.getId());
            nmsWorldProvider.a(this.nmsWorld);
            ReflectionUtils.set(this.nmsWorld.getClass().getSuperclass().getSuperclass(), this.nmsWorld, "t", nmsWorldProvider);
            this.nmsGenerator = nmsWorldProvider.c();

            // Here wa "transfer" some of the NMS objects we created to our BlockPopulator
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
