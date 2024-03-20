package org.mangorage.customitem.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        RegistryObject<T> object = RegistryObject.create(ID, customItem.get());
        if (object.getID() != ID)
            throw new IllegalStateException("Cannot register item. Mismatched ID from registering item. Expecting %s, got %s".formatted(ID, object.getID()));
        ITEMS.put(ID, object);
        return object;
    }

    public void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean giveItem(Player player, String itemID) {
        RegistryObject<? extends CustomItem> itemRO = ITEMS.get(itemID);
        if (itemRO == null) return false;

        player.getInventory().addItem(itemRO.get().create());

        return true;
    }

    public String getID(ItemStack stack) {
        var nbt = stack.getItemMeta().getPersistentDataContainer();
        if (nbt.has(CustomItem.EXTRA_DATA_KEY, PersistentDataType.TAG_CONTAINER)) {
            var data = nbt.get(CustomItem.EXTRA_DATA_KEY, PersistentDataType.TAG_CONTAINER);
            if (data.has(CustomItem.ID_KEY, PersistentDataType.STRING)) {
                return data.get(CustomItem.ID_KEY, PersistentDataType.STRING);
            }
        }
        return null;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        String ID = getID(event.getItem());
        if (ID == null) return;
        RegistryObject<? extends CustomItem> itemRO = ITEMS.get(ID);
        if (itemRO != null) {
            itemRO.get().onEvent(event, event.getItem());
        }
    }
}
