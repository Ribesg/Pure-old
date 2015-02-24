package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import net.minecraft.server.release_1_7_10.agq;
import net.minecraft.server.release_1_7_10.ahb;
import net.minecraft.server.release_1_7_10.apj;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyTileMobSpawner extends apj {

    private final CreatureSpawner creatureSpawner;

    public class NMSProxyMobSpawner extends agq {

        public ahb a() {
            return null;
        }

        public void a(final int arg0) {

        }

        public int b() {
            return 0;
        }

        public int c() {
            return 0;
        }

        public int d() {
            return 0;
        }

        @Override
        public void a(String mob) {
            mob = mob.toUpperCase();
            if (mob.equals("CAVESPIDER")) {
                mob = "CAVE_SPIDER";
            }
            NmsProxyTileMobSpawner.this.creatureSpawner.setSpawnedType(EntityType.valueOf(mob));
        }
    }

    public NmsProxyTileMobSpawner(final CreatureSpawner creatureSpawner) {
        this.creatureSpawner = creatureSpawner;
    }

    @Override
    public agq a() {
        return new NMSProxyMobSpawner();
    }
}
