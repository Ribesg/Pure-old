package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import net.minecraft.server.release_1_7_10.aji;
import net.minecraft.server.release_1_7_10.aor;
import net.minecraft.server.release_1_7_10.mt;
import net.minecraft.server.release_1_7_10.sa;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyWorldServer extends mt /* WorldServer */ {

    private World world;

    public NmsProxyWorldServer() {
        super(null, null, null, 0, null, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public aji a(final int x, final int y, final int z) {
        return aji.e(this.world.getBlockAt(x, y, z).getTypeId());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean d(final int x, final int y, final int z, final aji nmsBlock, final int meta, final int arg5) {
        final Block block = this.world.getBlockAt(x, y, z);
        block.setTypeId(aji.b(nmsBlock));
        block.setData((byte) meta);
        return true;
    }

    @Override
    public int f(final int x, final int z) {
        return this.world.getHighestBlockYAt(x, z);
    }

    @Override
    public aor o(final int x, final int y, final int z) {
        final Block block = this.world.getBlockAt(x, y, z);
        if (block.getType() == Material.AIR) {
            return null;
        }
        final BlockState blockState = block.getState();
        if (blockState == null) {
            return null;
        }
        // todo: we need support for a chest
        if (blockState instanceof CreatureSpawner) {
            return new NmsProxyTileMobSpawner((CreatureSpawner) blockState);
        } else {
            System.out.println("NMSProxyWorldServer missing: " + blockState.getClass().getName());
        }
        return null;
    }

    @Override
    public void e(final int x, final int y, final int z, final aji paramaji) {
        // dummy - block updates?
    }

    @Override
    public boolean d(final sa arg0) {
        // dummy
        return false;
    }
}
