package com.njdaeger.enhanceddebugstick.modes.classic;

import com.njdaeger.btu.ActionBar;
import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.util.BlockHighlighter;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.njdaeger.enhanceddebugstick.api.IProperty.getProperties;
import static com.njdaeger.enhanceddebugstick.api.IProperty.hasProperties;
import static org.bukkit.ChatColor.*;

public final class ClassicDebugContext implements DebugContext {

    private Block current;
    private final Map<Material, Integer> currentProperty;
    private final EnhancedDebugStick plugin;
    private final UUID uuid;
    private final DebugSession session;

    ClassicDebugContext(DebugSession session) {
        this.plugin = EnhancedDebugStick.getPlugin(EnhancedDebugStick.class);
        this.currentProperty = new HashMap<>();
        this.uuid = session.getSessionId();
        this.session = session;

        for (Material mat : IProperty.getMaterialProperties().keySet()) {
            currentProperty.put(mat, 0);
        }
    }

    @Override
    public UUID getOwner() {
        return uuid;
    }

    @Override
    public DebugSession getDebugSession() {
        return session;
    }

    /**
     * Gets the current property selected for the specific block.
     *
     * @param block The block to get the current property of.
     * @return The property selected for the block, or null if the block has no properties.
     */
    public IProperty<?, ?> getCurrentProperty(Block block) {
        return getCurrentProperty(block.getType());
    }

    /**
     * Gets the current property selected for the specific material.
     *
     * @param material The material to get the current property of
     * @return The property selected for the material, or null if the material has no properties.
     */
    public IProperty<?, ?> getCurrentProperty(Material material) {
        if (!hasProperties(material)) return null;
        else return getProperties(material).get(currentProperty.get(material));
    }

    /**
     * Gets the next property to be selected for the specific block. (This will not change the current selection of the
     * block, it will only find the next property which will be selected by the {@link #applyNextPropertyFor(Block)}
     * method)
     *
     * @param block The block to get the next property of
     * @return The next property to be selected for the block, or null if the block has no properties.
     */
    public IProperty<?, ?> getNextProperty(Block block) {
        return getNextProperty(block.getType());
    }

    /**
     * Gets the next property to be selected for the specific material. (This will not change the current selection of
     * the material, it will only find the next property which will be selected by the {@link
     * #applyNextPropertyFor(Block)} method)
     *
     * @param material The material to get the next property of
     * @return The next property to be selected for the material, or null if the material has no properties.
     */
    public IProperty<?, ?> getNextProperty(Material material) {
        if (!hasProperties(material)) return null;
        int current = currentProperty.get(material);
        if (current == getProperties(material).size() - 1) current = 0;
        else current++;
        return getProperties(material).get(current);
    }

    /**
     * Sets the current property to be selected for the specific block
     *
     * @param block The block to change the current property of.
     * @param property The property to change the block's selection to.
     * @throws RuntimeException if the property given is not applicable to the specified block.
     */
    public void setCurrent(Block block, IProperty<?, ?> property) {
        setCurrent(block.getType(), property);
    }

    /**
     * Sets the current property to be selected for the specific material
     *
     * @param material The material to change the current property of.
     * @param property The property to change the material's selection to.
     * @throws RuntimeException if the property given is not applicable to the specified material.
     */
    public void setCurrent(Material material, IProperty<?, ?> property) {
        if (!property.isApplicableTo(material))
            throw new RuntimeException("Property \"" + property.getNiceName() + "\" is not applicable for material " + material.name());
        currentProperty.replace(material, getProperties(material).indexOf(property));
    }

    /**
     * Shifts the selected property to the next property for the specific block. Does nothing if the block has no
     * properties.
     *
     * @param block The block to shift to the next property for.
     */
    public void applyNextPropertyFor(Block block) {
        if (!hasProperties(block)) return;
        int current = currentProperty.get(block.getType());
        if (current == getProperties(block).size() - 1) current = 0;
        else current++;
        currentProperty.replace(block.getType(), current);
    }

    /**
     * Shifts the selected value (of the block's current selected property) to the next value, then sets the blockdata
     * in the world. Does nothing if the block has no properties.
     *
     * @param block The block to shift to the next property value for.
     */
    @SuppressWarnings("all")
    public void applyNextValueFor(Block block) {
        if (!hasProperties(block)) return;

        BlockData newData = getCurrentProperty(block).nextBlockData(block);//This wont be null.

        if (plugin.getDebugConfig().coreprotectLogging()) {
            CoreProtectAPI api = plugin.getCoreProtectAPI();
            if (api != null) {
                api.logRemoval(Bukkit.getPlayer(uuid).getName(), block.getLocation(), block.getType(), block.getBlockData());
                api.logPlacement(Bukkit.getPlayer(uuid).getName(), block.getLocation(), block.getType(), newData);
            }
        }
        block.setType(block.getType(), false);
        block.setBlockData(newData, false);
    }

    /**
     * Sends the player information about the properties of a specific block via ActionBar. If this block has no
     * properties, or if the block is null, the message sent will be empty.
     *
     * @param block The block to get and send the properties of.
     */
    public void sendPropertiesOf(Block block) {
        StringBuilder builder = new StringBuilder();
        if (block != null) {
            for (IProperty<?, ?> property : getProperties(block)) {
                if (getCurrentProperty(block) == property) {
                    builder.append(DARK_GREEN).append(BOLD).append(UNDERLINE).append(property.getNiceName()).append(": ");
                    builder.append(GRAY).append(BOLD).append(UNDERLINE).append(format(property.getNiceCurrentValue(block))).append(RESET).append("    ");
                } else {
                    builder.append(DARK_GREEN).append(property.getNiceName()).append(": ");
                    builder.append(GRAY).append(format(property.getNiceCurrentValue(block))).append("    ");
                }
            }
        }
        ActionBar.of(builder.toString().trim()).sendTo(Bukkit.getPlayer(uuid));
    }

    /**
     * Puts a selection block around the current selection if it is a full block
     * @param block
     */
    public void changeSelection(Block block) {
        if (block != null && block.isPassable()) block = null;
        if (block == null) {
            if (current != null) {
                BlockHighlighter.unLightBlock(current, Bukkit.getPlayer(uuid));
                current = null;
            }
            return;
        }
        drawParticles(block);
        if (current == null || !block.getLocation().equals(current.getLocation())) {
            if (current != null) BlockHighlighter.unLightBlock(current, Bukkit.getPlayer(uuid));
            BlockHighlighter.lightBlock(block, Bukkit.getPlayer(uuid));
            this.current = block;
        }
    }

    private void drawParticles(Block block) {
        BoundingBox box = block.getBoundingBox();
        box.expand(1);
        World world = block.getWorld();
        final double ACCURACY = .01;
        System.out.println(box.getMinY());

        for (double x = box.getMinX(); x < box.getMaxX(); x+=ACCURACY) {
            for (double y = box.getMinY(); y < box.getMaxY(); y+=ACCURACY) {
                for (double z = box.getMinZ(); z < box.getMaxZ(); z+=ACCURACY) {
                    world.spawnParticle(Particle.REDSTONE, x, y, z, 1, new Particle.DustOptions(Color.WHITE, 0f));
                }
            }
        }

    }

    private static String format(String string) {
        String[] split = string.split("_");
        StringBuilder result = new StringBuilder();
        for (String str : split) {
            result.append(Character.toUpperCase(str.charAt(0))).append(str.substring(1));
            result.append(" ");
        }
        return result.toString().trim();
    }

}
