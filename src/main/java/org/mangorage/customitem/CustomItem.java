package org.mangorage.customitem;

import org.bukkit.plugin.java.JavaPlugin;
import org.mangorage.customitem.core.GiveCustomItemCommand;
import org.mangorage.customitem.registry.Items;

public final class CustomItem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        var cmd = getCommand("givecustom");

        if (cmd != null) {
            var GCI = new GiveCustomItemCommand();
            cmd.setExecutor(GCI);
            cmd.setTabCompleter(GCI);
        }

        Items.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
