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

public final class ItemRegistry implements Listener {
    private static final ItemRegistry REGISTRY = new ItemRegistry();
    public static ItemRegistry getRegistry() {
        return REGISTRY;
    }

    public static List<String> getKeys() {
        return REGISTRY.ITEMS.keySet().stream().toList();
    }

    private final Map<String, CustomItem> ITEMS = new HashMap<>();

    public void registerItem(String ID, Function<String, ? extends CustomItem> customItem) {
        this.ITEMS.put(ID, customItem.apply(ID));
    }

    public void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean giveItem(Player player, String itemID) {
        CustomItem item = ITEMS.get(itemID);
        if (item == null) return false;

        player.getInventory().addItem(item.create());

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
        CustomItem item = ITEMS.get(ID);
        if (item != null) {
            item.onEvent(event, event.getItem());
        }
    }
}
