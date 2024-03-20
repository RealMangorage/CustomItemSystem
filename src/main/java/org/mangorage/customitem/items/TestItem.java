package org.mangorage.customitem.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.mangorage.customitem.core.CustomItem;
import org.mangorage.customitem.core.Utils;
import org.mangorage.customitem.core.pdc.NBTPDC;
import org.mangorage.customitem.registry.Items;

import java.text.DecimalFormat;
import java.util.List;

public class TestItem extends CustomItem {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,###");

    public TestItem() {
        super(() -> Items.CUSTOM_ITEM);
    }

    public void updateLore(ItemStack stack) {
        var nbt = NBTPDC.getNBT(stack, CustomItem.NAMESPACE_ID);
        var blocks = 0;
        if (nbt.hasInteger("blocks")) {
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
        ItemStack stack = new ItemStack(Material.DIAMOND_AXE, 1);

        stack.editMeta(itemMeta -> {
            setID(NBTPDC.getNBT(itemMeta, CustomItem.NAMESPACE_ID));

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
                                    Component.text("HyperMiner Axe")
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
                                    Component.text("0")
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


        return stack;
    }

    public void incrementBlocks(ItemStack stack) {
        stack.editMeta(meta -> {
            var nbt = NBTPDC.getNBT(meta, CustomItem.NAMESPACE_ID);
            if (nbt.hasInteger("blocks")) {
                nbt.putInteger("blocks", nbt.getInteger("blocks") + 1);
            } else {
                nbt.putInteger("blocks", 1);
            }
        });
    }

    public void onInteract(PlayerInteractEvent event, ItemStack stack) {
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

    @Override
    public void onEvent(Event event, ItemStack itemStack) {
        if (event instanceof PlayerInteractEvent playerInteractEvent)
            onInteract(playerInteractEvent, itemStack);
    }
}
