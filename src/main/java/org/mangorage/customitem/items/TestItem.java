package org.mangorage.customitem.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.mangorage.customitem.core.CustomItem;
import org.mangorage.customitem.registry.Items;

public class TestItem extends CustomItem {

    public TestItem() {
        super(Items.CUSTOM_ITEM);
    }

    @Override
    public ItemStack create() {
        ItemStack stack = new ItemStack(Material.DIAMOND_AXE, 1);

        stack.editMeta(itemMeta -> {
            var nbt = itemMeta.getPersistentDataContainer();
            var data = itemMeta.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
            data.set(ID_KEY, PersistentDataType.STRING, getID());
            nbt.set(EXTRA_DATA_KEY, PersistentDataType.TAG_CONTAINER, data);
        });

        return stack;
    }

    public void onInteract(PlayerInteractEvent event, ItemStack stack) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            event.getPlayer().sendMessage(Component.text("Left clicked Item -> %s".formatted(getID())));
        }
    }

    @Override
    public void onEvent(Event event, ItemStack itemStack) {
        if (event instanceof PlayerInteractEvent playerInteractEvent)
            onInteract(playerInteractEvent, itemStack);
    }
}
