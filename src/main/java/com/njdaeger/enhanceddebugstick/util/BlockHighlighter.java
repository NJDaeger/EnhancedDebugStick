package com.njdaeger.enhanceddebugstick.util;

import com.njdaeger.btu.Util;
import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BlockHighlighter {

    private static final BukkitTask highlightTask;
    private static final Map<UUID, Highlighter> tasks = new HashMap<>();

    private static Field connection;
    private static Method sendPacket;

    private static Method size;
    private static Method invisible;
    private static Method invulnerable;
    private static Method noAi;
    private static Method location;
    private static Method flag;
    private static Method id;
    private static Method dataWatcher;
    private static Method rotation;
    private static Method world;

    private static Constructor<?> magmaCubeConstructor;
    private static Constructor<?> packetEntityLivingConstructor;
    private static Constructor<?> packetEntityMetadataConstructor;
    private static Constructor<?> packetEntityDestroyConstructor;

    private static Class<?> entityLivingClass;

    static {
        try {

            connection = Util.getNMSClass("EntityPlayer").getDeclaredField("playerConnection");
            sendPacket = connection.getType().getDeclaredMethod("sendPacket", Util.getNMSClass("Packet"));

            Class<?> magmaCubeClass = Util.getNMSClass("EntityMagmaCube");
            Class<?> insentientClass = Util.getNMSClass("EntityInsentient");
            Class<?> entityClass = Util.getNMSClass("Entity");

            size = magmaCubeClass.getDeclaredMethod("setSize", int.class, boolean.class);
            invisible = entityClass.getDeclaredMethod("setInvisible", boolean.class);
            invulnerable = entityClass.getDeclaredMethod("setInvulnerable", boolean.class);
            noAi = insentientClass.getDeclaredMethod("setNoAI", boolean.class);
            location = entityClass.getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            flag = entityClass.getDeclaredMethod("setFlag", int.class, boolean.class);
            id = entityClass.getDeclaredMethod("getId");
            dataWatcher = entityClass.getDeclaredMethod("getDataWatcher");
            rotation = entityClass.getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);

            entityLivingClass = Util.getNMSClass("EntityLiving");
            world = Util.getOBCClass("CraftWorld").getDeclaredMethod("getHandle");
            magmaCubeConstructor = magmaCubeClass.getConstructor(Util.getNMSClass("World"));
            packetEntityLivingConstructor = Util.getNMSClass("PacketPlayOutSpawnEntityLiving").getDeclaredConstructor(Util.getNMSClass("EntityLiving"));
            packetEntityMetadataConstructor = Util.getNMSClass("PacketPlayOutEntityMetadata").getDeclaredConstructor(int.class, Util.getNMSClass("DataWatcher"), boolean.class);
            packetEntityDestroyConstructor = Util.getNMSClass("PacketPlayOutEntityDestroy").getDeclaredConstructor(int[].class);

        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        highlightTask = Bukkit.getScheduler().runTaskTimer(EnhancedDebugStick.getPlugin(EnhancedDebugStick.class), () -> tasks.forEach((id, light) -> light.lightBlocks()), 0, 1);

    }

    private BlockHighlighter() {}

    /**
     * This unlights all the blocks for a specific player and removes their Highlighter instance
     * @param player The player to unlight all blocks from and remove the Highlighter instance of
     */
    public static void removeTask(Player player) {
        unLightAllBlocks(player);
        tasks.remove(player.getUniqueId());
    }

    /**
     * This unlights all the blocks for a specific player without removing their Highlighter instance.
     * @param player The player to unlight all blocks from
     */
    public static void unLightAllBlocks(Player player) {
        if (tasks.containsKey(player.getUniqueId())) {
            Highlighter task = tasks.get(player.getUniqueId());
            task.removeAllBlocks();
        }
    }

    /**
     * Unlights a single block for a player
     * @param block The block to unlight
     * @param player The player to unlight the block for
     */
    public static void unLightBlock(Block block, Player player) {
        if (tasks.containsKey(player.getUniqueId())) {
            Highlighter task = tasks.get(player.getUniqueId());
            task.removeBlock(block);
        }
    }

    /**
     * Lights a single block for a player
     * @param block The block to light
     * @param player The player to light the block for
     */
    public static void lightBlock(Block block, Player player) {
        if (!tasks.containsKey(player.getUniqueId())) {
            Highlighter task = new Highlighter(player);
            tasks.put(player.getUniqueId(), task);
        }
        tasks.get(player.getUniqueId()).addBlock(block);
    }

    /**
     * Highlights the players current blocks
     */
    public static class Highlighter {

        private final Map<Block, Object> magmaCubeMap;
        private Object entityPlayer;

        private Highlighter(Player player) {
            this.magmaCubeMap = new HashMap<>();
            try {
                this.entityPlayer = Util.getOBCClass("entity.CraftPlayer").getDeclaredMethod("getHandle").invoke(player);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * Updates all the players currently lit blocks with new lit blocks
         */
        public void lightBlocks() {
            for (Object magmaCube : magmaCubeMap.values()) {
                try {
                    Object spawn = packetEntityLivingConstructor.newInstance(entityLivingClass.cast(magmaCube));
                    Object effect = packetEntityMetadataConstructor.newInstance(id.invoke(magmaCube), dataWatcher.invoke(magmaCube), true);
                    sendPacket.invoke(connection.get(entityPlayer), spawn);
                    sendPacket.invoke(connection.get(entityPlayer), effect);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Adds a block to this highlighter
         * @param block The block to add
         */
        public void addBlock(Block block) {
            try {

                Object magmaCube = magmaCubeConstructor.newInstance(world.invoke(block.getWorld()));

                size.invoke(magmaCube, 2, true);
                invisible.invoke(magmaCube, true);
                invulnerable.invoke(magmaCube, true);
                noAi.invoke(magmaCube, true);
                location.invoke(magmaCube, block.getX() + .5, block.getY(), block.getZ() + .5, 0, 0);
                flag.invoke(magmaCube, 6, true);
                rotation.invoke(magmaCube, block.getX() + .5, block.getY(), block.getZ() + .5,0,0);

                magmaCubeMap.put(block, magmaCube);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        /**
         * Removes a block from this highlighter
         * @param block The block to remove
         */
        public void removeBlock(Block block) {
            Object magmaCube = magmaCubeMap.remove(block);
            if (magmaCube != null) {
                try {
                    Object destroy = packetEntityDestroyConstructor.newInstance(new int[]{(int)id.invoke(magmaCube)});
                    sendPacket.invoke(connection.get(entityPlayer), destroy);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }

        /**
         * Removes all blocks from this highlighter
         */
        public void removeAllBlocks() {
            Collection<Block> blocks = Collections.unmodifiableSet(magmaCubeMap.keySet());
            for (Block block : blocks) {
                removeBlock(block);
            }
        }

        /**
         * Checks if this highlighter is empty
         * @return True if no blocks are set to be highlighted, false otherwise
         */
        public boolean isEmpty() {
            return magmaCubeMap.isEmpty();
        }

    }

}
