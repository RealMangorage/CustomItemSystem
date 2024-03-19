package org.mangorage.customitem;

import org.bukkit.plugin.java.JavaPlugin;
import org.mangorage.customitem.core.GiveCustomItemCommand;
import org.mangorage.customitem.core.ItemRegistry;
import org.mangorage.customitem.items.TestItem;

public final class CustomItem extends JavaPlugin {
    private static ItemRegistry ITEMS = ItemRegistry.getRegistry();

    static {
        ITEMS.registerItem("test", TestItem::new);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        var cmd = getCommand("givecustom");

        if (cmd != null) {
            var GCI = new GiveCustomItemCommand();
            cmd.setExecutor(GCI);
            cmd.setTabCompleter(GCI);
        }

        ITEMS.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
