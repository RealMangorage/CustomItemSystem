package org.mangorage.customitem.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ItemRegistry implements Listener {
    private static final ItemRegistry REGISTRY = new ItemRegistry();
    public static ItemRegistry getRegistry() {
        return REGISTRY;
    }
    public static List<String> getKeys() {
        return REGISTRY.ITEMS.keySet().stream().toList();
    }


    private final Map<String, RegistryObject<? extends CustomItem>> ITEMS = new HashMap<>();

    public <T extends CustomItem> RegistryObject<T> registerItem(String ID, Supplier<T> customItem) {
        if (ITEMS.containsKey(ID))
            throw new IllegalStateException("Already have an Item with ID of %s registered...".formatted(ID));
        RegistryObject<T> object = RegistryObject.create(ID, customItem.get());
        if (object.getID() != ID)
            throw new IllegalStateException("Cannot register item. Mismatched ID from registering item. Expecting %s, got %s".formatted(ID, object.getID()));
        ITEMS.put(ID, object);
        return object;
    }

    public void register(Plugin plugin) {
        ITEMS.forEach((k, v) -> Bukkit.getPluginManager().registerEvents(v.get(), plugin));
    }

    public boolean giveItem(Player player, String itemID) {
        RegistryObject<? extends CustomItem> itemRO = ITEMS.get(itemID);
        if (itemRO == null) return false;

        player.getInventory().addItem(itemRO.get().create());

        return true;
    }
}
