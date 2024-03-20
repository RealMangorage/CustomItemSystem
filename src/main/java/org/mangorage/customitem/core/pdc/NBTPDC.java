package org.mangorage.customitem.core.pdc;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class NBTPDC {
    public static NBTPDC getNBT(ItemStack stack, String ID) {
        return new NBTPDC(stack.getItemMeta().getPersistentDataContainer(), ID);
    }

    public static NBTPDC getNBT(ItemMeta meta, String ID) {
        return new NBTPDC(meta.getPersistentDataContainer(), ID);
    }

    private final PersistentDataContainer PDC;
    private final String ID;

    public NBTPDC(PersistentDataContainer PDC, String ID) {
        this.PDC = PDC;
        this.ID = ID;
    }

    private NamespacedKey getKey(String key) {
        return new NamespacedKey(ID, key);
    }
    public void putString(String key, String value) {
        PDC.set(getKey(key), PersistentDataType.STRING, value);
    }

    public String getString(String key) {
        return PDC.get(getKey(key), PersistentDataType.STRING);
    }

    public void putInteger(String key, Integer integer) {
        PDC.set(getKey(key), PersistentDataType.INTEGER, integer);
    }

    public Integer getInteger(String key) {
        return PDC.get(getKey(key), PersistentDataType.INTEGER);
    }

    public NBTPDC getCompound(String key) {
        return new NBTPDC(PDC.get(getKey(key), PersistentDataType.TAG_CONTAINER), ID);
    }

    public void putCompound(String key, NBTPDC nbtpdc) {
        PDC.set(getKey(key), PersistentDataType.TAG_CONTAINER, nbtpdc.PDC);
    }

    public boolean hasString(String key) {
        return PDC.has(getKey(key), PersistentDataType.STRING);
    }

    public boolean hasInteger(String key) {
        return PDC.has(getKey(key), PersistentDataType.INTEGER);
    }

    public boolean hasCompoundTag(String key) {
        return PDC.has(getKey(key), PersistentDataType.TAG_CONTAINER);
    }

    public NBTPDC createNewPDC() {
        return new NBTPDC(PDC.getAdapterContext().newPersistentDataContainer(), ID);
    }
}
