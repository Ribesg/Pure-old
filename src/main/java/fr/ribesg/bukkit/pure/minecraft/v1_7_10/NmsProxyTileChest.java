package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import net.minecraft.server.release_1_7_10.aow;
import org.bukkit.block.Chest;

/**
 * @author Ribesg
 */
public class NmsProxyTileChest extends aow {

    private final Chest chest;

    public NmsProxyTileChest(final Chest chest) {
        this.chest = chest;
    }

    // TODO Override things?
}
