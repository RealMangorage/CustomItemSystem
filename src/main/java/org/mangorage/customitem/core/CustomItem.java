package org.mangorage.customitem.core;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.mangorage.customitem.core.pdc.NBTPDC;

import java.util.function.Supplier;

public abstract class CustomItem {
    public static final String NAMESPACE_ID = "customitem";
    private final Supplier<RegistryObject<? extends CustomItem>> RO;

    public CustomItem(Supplier<RegistryObject<? extends CustomItem>> RO) {
        this.RO = RO;
    }

    public String getID() {
        return RO.get().getID();
    }

    public void setID(NBTPDC PDC) {
        var extra = PDC.createNewPDC();
        extra.putString("id", getID());
        PDC.putCompound("extradata", extra);
    }
    abstract public ItemStack create();

    abstract public void onEvent(Event event, ItemStack itemStack);

}
