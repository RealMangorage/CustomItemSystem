package org.mangorage.customitem.core;

import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public abstract class CustomItem implements Listener {
    private final Supplier<RegistryObject<? extends CustomItem>> RO;

    public CustomItem(Supplier<RegistryObject<? extends CustomItem>> RO) {
        this.RO = RO;
    }

    public String getID() {
        return RO.get().getID();
    }

    public void setID(ReadWriteItemNBT tag) {
        var extra = tag.getOrCreateCompound(Constants.EXTRA_DATA_TAG);
        extra.setString("id", getID());
    }

    public boolean isSame(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getAmount() <= 0) return false;
        return getID().equals(Utils.getID(stack));
    }

    abstract public ItemStack create();

}
