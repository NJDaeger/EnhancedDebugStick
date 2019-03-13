package com.njdaeger.enhanceddebugstick.modes.classic;

import com.njdaeger.btu.ActionBar;
import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import com.njdaeger.enhanceddebugstick.Property;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.util.BlockHighlighter;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.njdaeger.enhanceddebugstick.Property.getProperties;
import static com.njdaeger.enhanceddebugstick.Property.hasProperties;
import static org.bukkit.ChatColor.*;

public final class ClassicDebugContext implements DebugContext {

    private Block current;
    private final Map<Material, Integer> currentProperty;
    private final EnhancedDebugStick plugin;
    private final UUID uuid;

    ClassicDebugContext(DebugSession session) {
        this.plugin = EnhancedDebugStick.getPlugin(EnhancedDebugStick.class);
        this.currentProperty = new HashMap<>();
        this.uuid = session.getSessionId();

        for (Material mat : Property.getMaterialProperties().keySet()) {
            currentProperty.put(mat, 0);
        }
    }

    @Override
    public UUID getOwner() {
        return uuid;
    }

    /**
     * Gets the current property selected for the specific block.
     *
     * @param block The block to get the current property of.
     * @return The property selected for the block, or null if the block has no properties.
     */
    public Property<?, ?> getCurrentProperty(Block block) {
        return getCurrentProperty(block.getType());
    }

    /**
     * Gets the current property selected for the specific material.
     *
     * @param material The material to get the current property of
     * @return The property selected for the material, or null if the material has no properties.
     */
    public Property<?, ?> getCurrentProperty(Material material) {
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
    public Property<?, ?> getNextProperty(Block block) {
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
    public Property<?, ?> getNextProperty(Material material) {
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
    public void setCurrent(Block block, Property<?, ?> property) {
        setCurrent(block.getType(), property);
    }

    /**
     * Sets the current property to be selected for the specific material
     *
     * @param material The material to change the current property of.
     * @param property The property to change the material's selection to.
     * @throws RuntimeException if the property given is not applicable to the specified material.
     */
    public void setCurrent(Material material, Property<?, ?> property) {
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
            for (Property<?, ?> property : getProperties(block)) {
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

    public void changeSelection(Block block) {
        if (block == null) {
            if (current != null) {
                BlockHighlighter.unLightBlock(current, Bukkit.getPlayer(uuid));
                current = null;
            }
        }
        if (block != current) {
            BlockHighlighter.unLightBlock(current, Bukkit.getPlayer(uuid));
            BlockHighlighter.lightBlock(plugin, block, Bukkit.getPlayer(uuid));
            this.current = block;
        }
    }

/*
    public void changeSelection(Block block, Player player) {

        Field connection = null;
        Object entityPlayer = null;
        Method sendPacket = null;

        try {
            //Class<?> entityPlayer = Util.getNMSClass("EntityPlayer");
            entityPlayer = player.getClass().getDeclaredMethod("getHandle").invoke(player);
            connection = entityPlayer.getClass().getDeclaredField("playerConnection");
            sendPacket = connection.getType().getDeclaredMethod("sendPacket", Util.getNMSClass("Packet"));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (connection == null || entityPlayer == null || sendPacket == null) return;

        //PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        if (selection != null) {
            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(selection.getEntityId());
            try {
                sendPacket.invoke(connection.get(entityPlayer), destroy);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            this.selection = null;
            return;
        }
        if (block != null && block != current) {

            try {
                Class<?> magmaCubeClass = Util.getNMSClass("EntityMagmaCube");
                Class<?> insentientClass = Util.getNMSClass("EntityInsentient");
                Class<?> entityClass = Util.getNMSClass("Entity");
                Method size = magmaCubeClass.getDeclaredMethod("setSize", int.class, boolean.class);
                Method invisible = entityClass.getDeclaredMethod("setInvisible", boolean.class);
                Method invulnerable = entityClass.getDeclaredMethod("setInvulnerable", boolean.class);
                Method noAi = insentientClass.getDeclaredMethod("setNoAI", boolean.class);
                Method location = entityClass.getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
                Method flag = entityClass.getDeclaredMethod("setFlag", int.class, boolean.class);
                Method bukkitEntity = entityClass.getDeclaredMethod("getBukkitEntity");
                Method id = entityClass.getDeclaredMethod("getId");
                Method watcher = entityClass.getDeclaredMethod("getDataWatcher");
                Method rotation = entityClass.getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
                Constructor<?> magmaCubeConstructor = magmaCubeClass.getConstructor(Util.getNMSClass("World"));
                Object magmaCube = magmaCubeConstructor.newInstance(block.getWorld().getClass().getDeclaredMethod("getHandle").invoke(block.getWorld()));

                size.invoke(magmaCube, 2, true);
                invisible.invoke(magmaCube, true);
                invulnerable.invoke(magmaCube, true);
                noAi.invoke(magmaCube, true);
                location.invoke(magmaCube, block.getX() + .5, block.getY(), block.getZ() + .5, 0, 0);
                flag.invoke(magmaCube, 6, true);
                rotation.invoke(magmaCube, block.getX() + .5, block.getY(), block.getZ() + .5,0,0);
                this.selection = (MagmaCube) bukkitEntity.invoke(magmaCube);
                this.current = block;
                PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving((EntityLiving) magmaCube);
                PacketPlayOutEntityMetadata eff = new PacketPlayOutEntityMetadata((int)id.invoke(magmaCube), (DataWatcher) watcher.invoke(magmaCube), true);
                sendPacket.invoke(connection.get(entityPlayer), spawn);
                sendPacket.invoke(connection.get(entityPlayer), eff);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }


            EntityMagmaCube magmaCube = new EntityMagmaCube(((CraftWorld)block.getWorld()).getHandle());
            magmaCube.setSize(1, true);
            magmaCube.setInvisible(true);
            magmaCube.setInvulnerable(true);
            magmaCube.setNoAI(true);
            magmaCube.setLocation(block.getX() + .5, block.getY(), block.getZ() + .5, 0, 0);
            magmaCube.setHeadRotation(0);
            magmaCube.setFlag(6, true);
            magmaCube.setPositionRotation();
            this.selection = (MagmaCube) magmaCube.getBukkitEntity();
            this.current = block;*/
            /*PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(magmaCube);
            PacketPlayOutEntityMetadata eff = new PacketPlayOutEntityMetadata(magmaCube.getId(), magmaCube.getDataWatcher(), true);
            connection.sendPacket(spawn);
            connection.sendPacket(eff);*/

            /*EntityShulker shulker = new EntityShulker(((CraftWorld)block.getWorld()).getHandle());
            shulker.setLocation(block.getX() + .5, block.getY(), block.getZ() + .5, 0, 0);
            shulker.setInvisible(true);
            shulker.setInvulnerable(true);
            shulker.setHeadRotation(0);
            shulker.setPositionRotation(block.getX() + .5, block.getY(), block.getZ() + .5, 0, 0);
            shulker.setNoAI(true);
            shulker.setFlag(6, true);
            this.selection = (Shulker) shulker.getBukkitEntity();
            this.current = block;
            PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(shulker);
            PacketPlayOutEntityMetadata eff = new PacketPlayOutEntityMetadata(shulker.getId(), shulker.getDataWatcher(), true);
            connection.sendPacket(spawn);
            connection.sendPacket(eff);
        }
    }*/

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
