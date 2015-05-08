package fr.ribesg.bukkit.pure.minecraft.r1_7_10;

import net.minecraft.server.r1_7_10.adb;
import net.minecraft.server.r1_7_10.add;
import net.minecraft.server.r1_7_10.aow;
import net.minecraft.server.r1_7_10.dh;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Ribesg
 */
public class NmsProxyTileChest extends aow /* TileEntityChest */ {

    private final Inventory inv;

    public NmsProxyTileChest(final Chest chest) {
        this.inv = chest.getBlockInventory();
    }

    /*
     * Note that (aow) implements (rb).
     * (aow)           is the obfuscated class  name of TileEntityChest
     * (rb)            is the obfuscated class  name of IInventory
     * (add)           is the obfuscated class  name of ItemStack
     * (rb.a(int,add)) is the obfuscated method name of IInventory.setInventorySlotContents(int, ItemStack)
     * (adb)           is the obfuscated class  name of Item
     * (adb.b(adb))    is the obfuscated method name of Item.getIdFromItem(Item)
     * (add.b())       is the obfuscated method name of ItemStack.getItem()
     * (add.b)         is the obfuscated field  name of ItemStack.stackSize
     * (add.k())       is the obfuscated method name of ItemStack.getItemDamage()
     * (dh)            is the obfuscated class  name of NBTTagCompound
     * (add.d)         is the obfuscated field  name of ItemStack.stackTagCompound
     */
    @Override
    public void a(final int i, final add addArg) {
        @SuppressWarnings("deprecation")
        final ItemStack item = new ItemStack(
            adb.b(addArg.b()),
            addArg.b,
            (short) addArg.k()
        );

        final dh itemNbt = addArg.d;
        // TODO Enchantments & other generated NBT

        this.inv.setItem(i, item);
    }
}
