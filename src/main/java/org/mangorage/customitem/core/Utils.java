package org.mangorage.customitem.core;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.function.Consumer;

public class Utils {

    private static final ItemStack PICKAXE = new ItemStack(Material.NETHERITE_PICKAXE, 1);
    private static final ItemStack AXE = new ItemStack(Material.NETHERITE_AXE, 1);
    private static final ItemStack SHOVEL = new ItemStack(Material.NETHERITE_SHOVEL, 1);

    private static final List<ItemStack> ITEMS = List.of(PICKAXE, AXE, SHOVEL);

    public static ItemStack getProperTool(ItemStack stack, BlockData blockData) {
        if (blockData.isPreferredTool(stack)) return stack;
        for (ItemStack item : ITEMS) {
            if (blockData.isPreferredTool(item)) {
                ItemStack newItem = item.asOne();
                newItem.setItemMeta(stack.getItemMeta());
                return newItem;
            }
        }

        return stack;
    }


    public static <T> void doIfPresent(T object, Consumer<T> consumer) {
        if (object != null)
            consumer.accept(object);
    }

    public static Location rayTrace(Player player, double maxDistance) {
        Location playerLocation = player.getEyeLocation();
        Vector direction = playerLocation.getDirection().normalize();
        Location targetLocation = playerLocation.clone();

        double distanceTraveled = 0;

        while (distanceTraveled < maxDistance) {
            targetLocation.add(direction);
            distanceTraveled += 0.1;

            Block block = targetLocation.getBlock();
            if (!block.isEmpty() && !block.isLiquid() && !block.isPassable()) {
                // If the block is not empty, not liquid, and not passable (solid), we return the location
                return targetLocation;
            } else if (distanceTraveled >= maxDistance) {
                // If the maximum distance is reached and no obstacles encountered, return the location
                return targetLocation;
            }
        }

        // If no suitable location is found within the maximum distance, return null
        return null;
    }
}
