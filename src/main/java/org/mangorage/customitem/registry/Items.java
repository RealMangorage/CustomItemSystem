package org.mangorage.customitem.registry;

import org.bukkit.plugin.Plugin;
import org.mangorage.customitem.core.CustomItem;
import org.mangorage.customitem.core.ItemRegistry;
import org.mangorage.customitem.core.RegistryObject;
import org.mangorage.customitem.items.HyperMinerDrill;

public class Items {
    private static final ItemRegistry ITEMS = ItemRegistry.getRegistry();
    public static final RegistryObject<CustomItem> CUSTOM_ITEM = ITEMS.registerItem("DIVAN_DRILL", HyperMinerDrill::new);

    public static void register(Plugin plugin) {
        ITEMS.register(plugin);
    }
}
