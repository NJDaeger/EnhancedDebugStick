package com.njdaeger.enhanceddebugstick;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class DebugStick extends ItemStack {

    /**
     * Represents a debug stick ItemStack
     */
    public DebugStick() {
        
        Material mat = Material.matchMaterial(ConfigKey.get().STICK_MATERIAL);
        if (mat == null || !mat.isItem()) {
            EnhancedDebugStick.getInstance().getLogger().severe("Unable to bind '" + ConfigKey.get().STICK_MATERIAL + "' to the debug stick. Is it an item? Defaulting to minecraft:stick");
            mat = Material.STICK;
        }
        
        setAmount(1);
        setType(mat);
        ItemMeta meta = getItemMeta();
        if (meta == null) throw new IllegalStateException("ItemMeta was null. Please contact the developer.");
        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Enhanced Debug Stick");
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        setItemMeta(meta);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof ItemStack) {
            ItemStack stack = (ItemStack)obj;
            ItemMeta objMeta = stack.getItemMeta();
            ItemMeta meta = getItemMeta();
            return stack.getType() == getType() &&
                    objMeta != null &&
                    meta != null &&
                    objMeta.getDisplayName().equals(meta.getDisplayName()) &&
                    objMeta.hasEnchant(Enchantment.DURABILITY) &&
                    objMeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE) &&
                    objMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS) &&
                    objMeta.isUnbreakable();
        }
        return false;
    }
}
