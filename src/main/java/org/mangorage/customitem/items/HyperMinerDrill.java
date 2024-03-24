package org.mangorage.customitem.items;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.mangorage.customitem.core.CustomItem;
import org.mangorage.customitem.core.Utils;
import org.mangorage.customitem.registry.Items;

import java.text.DecimalFormat;
import java.util.List;

public class HyperMinerDrill extends CustomItem {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,###");

    public HyperMinerDrill() {
        super(() -> Items.CUSTOM_ITEM);
    }

    public void updateLore(ItemStack stack) {
        var nbt = NBT.readNbt(stack);
        var blocks = 0;
        if (nbt.hasTag("blocks", NBTType.NBTTagInt)) {
            blocks = nbt.getInteger("blocks");
        }

        stack.lore(
                List.of(
                        Component.text()
                                .append(
                                        Component.text("Left Click Ability: ")
                                                .decorate(TextDecoration.BOLD)
                                                .color(NamedTextColor.GOLD)

                                )
                                .append(
                                        Component.text("Break Blocks")
                                                .decorate(TextDecoration.BOLD)
                                                .color(NamedTextColor.BLUE)
                                )
                                .build(),

                        Component.text("Breaks blocks. Enchants work.")
                                .color(NamedTextColor.GRAY),

                        Component.empty(),
                        Component.text()
                                .append(
                                        Component.text(FORMAT.format(blocks))
                                                .color(NamedTextColor.GOLD)
                                )
                                .append(
                                        Component.text(" Blocks Broken!")
                                                .color(NamedTextColor.GRAY)
                                )
                                .build(),

                        Component.empty(),
                        Component.empty(),

                        Component.text()
                                .append(
                                        Component.text("Right Click Ability: ")
                                                .decorate(TextDecoration.BOLD)
                                                .color(NamedTextColor.GOLD)

                                )
                                .append(
                                        Component.text("Teleport")
                                                .decorate(TextDecoration.BOLD)
                                                .color(NamedTextColor.BLUE)
                                )
                                .build(),

                        Component.text("Teleports you upto 5 Blocks")
                                .color(NamedTextColor.GRAY),

                        Component.empty(),
                        Component.empty(),

                        Component.text("SPECIAL")
                                .decorate(TextDecoration.BOLD)
                                .color(NamedTextColor.RED)
                )
        );

    }

    @Override
    public ItemStack create() {
        ItemStack stack = new ItemStack(Material.PRISMARINE_SHARD, 1);

        NBT.modify(stack, this::setID);

        stack.editMeta(itemMeta -> {

            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            itemMeta.displayName(
                    Component.text()
                            .append(
                                    Component.text("H ")
                                            .decorate(TextDecoration.BOLD, TextDecoration.OBFUSCATED)
                                            .color(NamedTextColor.WHITE)
                            )
                            .append(
                                    Component.text("HyperMiner Drill")
                                            .decorate(TextDecoration.BOLD, TextDecoration.ITALIC)
                                            .color(NamedTextColor.RED)
                            )
                            .append(
                                    Component.text(" H")
                                            .decorate(TextDecoration.BOLD, TextDecoration.OBFUSCATED)
                                            .color(NamedTextColor.WHITE)
                            ).build()
            );
        });

        updateLore(stack);

        return stack;
    }

    public void incrementBlocks(ItemStack stack) {
        NBT.modify(stack, nbt -> {
            if (nbt.hasTag("blocks", NBTType.NBTTagInt)) {
                nbt.setInteger("blocks", nbt.getInteger("blocks") + 1);
            } else {
                nbt.setInteger("blocks", 1);
            }
        });
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isSame(event.getItem())) return;

        var stack = event.getItem();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Utils.doIfPresent(event.getClickedBlock(), clicked -> {
                clicked.breakNaturally(Utils.getProperTool(stack, clicked.getBlockData()), true, true);
                incrementBlocks(stack);
                updateLore(stack);
            });
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR ) {
            var target = Utils.rayTrace(event.getPlayer(), 5);
            if (target != null) {
                event.getPlayer().teleportAsync(target);
            } else {
                event.getPlayer().sendMessage(
                        Component.text("Unable to teleport. Blocks are in the way...")
                                .decorate(TextDecoration.BOLD)
                                .color(NamedTextColor.RED)
                );
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!isSame(event.getItemDrop().getItemStack())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text("Cannot drop this Item!").color(NamedTextColor.RED));
    }

    @EventHandler
    public void onMove(InventoryClickEvent event) {
        var clicked = event.getClickedInventory();
        if (clicked == null) return;
        if (clicked.getType() == InventoryType.ENDER_CHEST) return;

        if (isSame(event.getCursor())) {
            if (event.getClickedInventory() != event.getWhoClicked().getInventory()) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(Component.text("Cannot move this Item to another inventory!").color(NamedTextColor.RED));
            }
        }

        if (!isSame(event.getCurrentItem())) return;
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) return;

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && event.getClickedInventory() == event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(Component.text("Cannot move this Item to another inventory!").color(NamedTextColor.RED));
        }
    }
}
