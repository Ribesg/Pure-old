package fr.ribesg.bukkit.pure.minecraft.r1_7_10;

import fr.ribesg.bukkit.pure.Pure;
import net.minecraft.server.r1_7_10.aji;
import net.minecraft.server.r1_7_10.aor;
import net.minecraft.server.r1_7_10.mt;
import net.minecraft.server.r1_7_10.sa;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyWorldServer extends mt /* WorldServer */ {

    @SuppressWarnings("all") // No warning name for "variable is never assigned"
    private World world;

    /*
     * The second argument of the super constructor seems to be @NotNull.
     * There is no @SuppressWarnings rule specifically for this, so all it is.
     */
    @SuppressWarnings("all")
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
        if (blockState instanceof CreatureSpawner) {
            return new NmsProxyTileMobSpawner((CreatureSpawner) blockState);
        } else if (blockState instanceof Chest) {
            return new NmsProxyTileChest((Chest) blockState);
        } else {
            Pure.getPluginLogger().warning("NMSProxyWorldServer missing: " + blockState.getClass().getName());
        }
        return null;
    }

    @Override
    public void e(final int x, final int y, final int z, final aji paramaji) {
        // NOP
    }

    @Override
    public boolean d(final sa arg0) {
        // NOP
        return false;
    }
}