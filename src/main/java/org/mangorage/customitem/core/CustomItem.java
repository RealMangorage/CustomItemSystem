package org.mangorage.customitem.core;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public abstract class CustomItem {
    public static final NamespacedKey EXTRA_DATA_KEY = new NamespacedKey("customitem", "extradata");
    public static final NamespacedKey ID_KEY = new NamespacedKey("customitem", "id");
    private final String ID;

    public CustomItem(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    abstract public ItemStack create();

    abstract public void onEvent(Event event, ItemStack itemStack);

}
