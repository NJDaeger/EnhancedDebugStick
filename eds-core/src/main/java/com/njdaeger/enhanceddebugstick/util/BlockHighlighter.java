package com.njdaeger.enhanceddebugstick.util;

import com.njdaeger.enhanceddebugstick.mcversion.Version;
import com.njdaeger.pdk.utils.Util;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class BlockHighlighter {

    private static final Map<UUID, Highlighter> tasks = new HashMap<>();

    private static Field connection;
    private static Field entityType;
    private static Method sendPacket;

    private static Method invisible;
    private static Method invulnerable;
    private static Method noAi;
    private static Method location;
    private static Method flag;
    private static Method id;
    private static Method dataWatcher;
    private static Method world;

    private static Constructor<?> shulkerConstructor;
    private static Constructor<?> shulkerConstructor113;
    private static Constructor<?> packetEntityLivingConstructor;
    private static Constructor<?> packetEntityMetadataConstructor;
    private static Constructor<?> packetEntityDestroyConstructor;
    private static Constructor<?> packetEntityDestroyConstructor117;

    private static Class<?> entityLivingClass;

    static {
        try {
            Class<?> entityPlayer = Util.getNMSClass("EntityPlayer", "net.minecraft.server.level.");
            connection = Util.tryField(entityPlayer, "playerConnection", "b", "connection");
            if (connection == null) throw new RuntimeException("Unable to find the player connection field in EntityPlayer");

            sendPacket = Util.tryMethod(connection.getType(), new Class<?>[]{Util.getNMSClass("Packet", "net.minecraft.network.protocol.")}, "sendPacket", "a", "send");
            if (sendPacket == null) throw new RuntimeException("Unable to find the send packet method in " + connection.getType().getName());
            

            Class<?> shulkerClass = Util.getNMSClass("EntityShulker", "net.minecraft.world.entity.monster.");
            Class<?> insentientClass = Util.getNMSClass("EntityInsentient", "net.minecraft.world.entity.");
            Class<?> entityClass = Util.getNMSClass("Entity", "net.minecraft.world.entity.");
            entityType = Util.tryField(Util.getNMSClass("EntityTypes", "net.minecraft.world.entity."), getShulkerEntityTypeArray());
            if (entityType == null) throw new RuntimeException("Unable to find the Shulker field in EntityTypes class");

            invisible = Util.tryMethod(entityClass, new Class<?>[]{boolean.class}, "setInvisible", "j");
            if (invisible == null) throw new RuntimeException("Unable to find the setInvisible method in the Entity class");
    
            invulnerable = Util.tryMethod(entityClass, new Class<?>[]{boolean.class}, "setInvulnerable", "m");
            if (invulnerable == null) throw new RuntimeException("Unable to find the setInvulnerable method in the Entity class");
            
            noAi = Util.tryMethod(insentientClass, new Class<?>[]{boolean.class}, "setNoAI", "s");
            if (noAi == null) throw new RuntimeException("Unable to find the setNoAI method in the EntityInsentient class");
    
            location = Util.tryMethod(entityClass, new Class<?>[]{double.class, double.class, double.class, float.class, float.class}, "setLocation", "b");
            if (location == null) throw new RuntimeException("Unable to find the setLocation method in the Entity class");
            
            flag = Util.tryMethod(entityClass, new Class<?>[]{int.class, boolean.class}, "setFlag", "b");
            if (flag == null) throw new RuntimeException("Unable to find the setFlag method in the Entity class");
            
            id = Util.tryMethod(entityClass, new Class<?>[]{}, "getId", "ae");
            if (Version.getCurrentVersion() == Version.v1_19_3) id = Util.tryMethod(entityClass,  null, "ah");
            if (id == null) throw new RuntimeException("Unable to find the getId method in the Entity class");

            dataWatcher = Util.tryMethod(entityClass, new Class<?>[]{}, "getDataWatcher", "ai");
            if (Version.getCurrentVersion() == Version.v1_19_3) dataWatcher = Util.tryMethod(entityClass,  null, "al");
            if (dataWatcher == null) throw new RuntimeException("Unable to find the getDataWatcher method in the Entity class");

            entityLivingClass = Util.getNMSClass("EntityLiving", "net.minecraft.world.entity.");
            world = Util.getOBCClass("CraftWorld").getDeclaredMethod("getHandle");
            try {
                shulkerConstructor = shulkerClass.getConstructor(Util.getNMSClass("EntityTypes", "net.minecraft.world.entity."), Util.getNMSClass("World", "net.minecraft.world.level."));
            } catch (Exception e) {
                shulkerConstructor113 = shulkerClass.getConstructor(Util.getNMSClass("World", "net.minecraft.world.level."));
            }
            
            Class<?> spawnEntityPacketClass = Util.tryNMSClass("net.minecraft.network.protocol.game.", "PacketPlayOutSpawnEntityLiving", "PacketPlayOutSpawnEntity");
            if (spawnEntityPacketClass == null) throw new RuntimeException("Unable to find the PacketPlayOutSpawnEntity class");
            
            Class<?> spawnEntityConstructorEntity;
            if (Version.getCurrentVersion() == Version.v1_19_3) spawnEntityConstructorEntity = entityClass;
            else spawnEntityConstructorEntity = Util.getNMSClass("EntityLiving", "net.minecraft.world.entity.");
            packetEntityLivingConstructor = spawnEntityPacketClass.getDeclaredConstructor(spawnEntityConstructorEntity);
            
            if (Version.getCurrentVersion() == Version.v1_19_3) packetEntityMetadataConstructor = Util.getNMSClass("PacketPlayOutEntityMetadata", "net.minecraft.network.protocol.game.").getDeclaredConstructor(int.class, List.class);
            else packetEntityMetadataConstructor = Util.getNMSClass("PacketPlayOutEntityMetadata", "net.minecraft.network.protocol.game.").getDeclaredConstructor(int.class, Util.getNMSClass("DataWatcher", "net.minecraft.network.syncher."), boolean.class);
            
            try {
                packetEntityDestroyConstructor = Util.getNMSClass("PacketPlayOutEntityDestroy", "net.minecraft.network.protocol.game.").getDeclaredConstructor(int[].class);
            } catch (Exception e) {
                packetEntityDestroyConstructor117 = Util.getNMSClass("PacketPlayOutEntityDestroy", "net.minecraft.network.protocol.game.").getDeclaredConstructor(int.class);
            }

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static String[] getShulkerEntityTypeArray() {
        if (Version.getCurrentVersion() == Version.v1_18) return new String[]{"ay", "SHULKER", "aB"};
        else if (Version.getCurrentVersion() == Version.v1_17) return new String[]{"ay"};
        else if (Version.getCurrentVersion() == Version.v1_19_3) return new String[]{"aC"};
        else return new String[]{"aB", "SHULKER", "ay"};
    }
    
    private BlockHighlighter() {}

    /**
     * This unlights all the blocks for a specific player and removes their AbstractHighlighter instance
     * @param player The player to unlight all blocks from and remove the AbstractHighlighter instance of
     */
    public static void removeTask(Player player) {
        unLightAllBlocks(player);
        tasks.remove(player.getUniqueId());
    }

    /**
     * This unlights all the blocks for a specific player without removing their AbstractHighlighter instance.
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

        private final Map<Block, Object> shulkerMap;
        private Object entityPlayer;

        private Highlighter(Player player) {
            this.shulkerMap = new HashMap<>();
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
            for (Object shulker : shulkerMap.values()) {
                try {
                    Object spawn = packetEntityLivingConstructor.newInstance(entityLivingClass.cast(shulker));
                    Object effect;
                    if (Version.getCurrentVersion() == Version.v1_19_3) {
                        Object watcher = dataWatcher.invoke(shulker);
                        Method defVals = watcher.getClass().getDeclaredMethod("c", null);
                        effect = packetEntityMetadataConstructor.newInstance(id.invoke(shulker), defVals.invoke(watcher));
                    }
                    else effect = packetEntityMetadataConstructor.newInstance(id.invoke(shulker), dataWatcher.invoke(shulker), true);
                    sendPacket.invoke(connection.get(entityPlayer), spawn);
                    sendPacket.invoke(connection.get(entityPlayer), effect);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * Adds a block to this highlighter
         * @param block The block to add
         */
        public void addBlock(Block block) {
            try {
                Object shulker;
                if (shulkerConstructor == null) shulker = shulkerConstructor113.newInstance(world.invoke(block.getWorld()));
                else shulker = shulkerConstructor.newInstance(entityType.get(null), world.invoke(block.getWorld()));

                invisible.invoke(shulker, true);
                invulnerable.invoke(shulker, true);
                noAi.invoke(shulker, true);
                location.invoke(shulker, block.getX() + .5, block.getY(), block.getZ() + .5, 0, 0);
                flag.invoke(shulker, 6, true);

                shulkerMap.put(block, shulker);

                Object spawn = packetEntityLivingConstructor.newInstance(entityLivingClass.cast(shulker));
                Object effect;
                if (Version.getCurrentVersion() == Version.v1_19_3) {
                    Object watcher = dataWatcher.invoke(shulker);
                    Method defVals = watcher.getClass().getDeclaredMethod("c", null);
                    effect = packetEntityMetadataConstructor.newInstance(id.invoke(shulker), defVals.invoke(watcher));
                }
                else effect = packetEntityMetadataConstructor.newInstance(id.invoke(shulker), dataWatcher.invoke(shulker), true);
                sendPacket.invoke(connection.get(entityPlayer), spawn);
                sendPacket.invoke(connection.get(entityPlayer), effect);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Removes a block from this highlighter
         * @param block The block to remove
         */
        public void removeBlock(Block block) {
            Object shulker = shulkerMap.remove(block);
            if (shulker != null) {
                try {
                    Object destroy;
                    if (packetEntityDestroyConstructor == null) destroy = packetEntityDestroyConstructor117.newInstance(id.invoke(shulker));
                    else destroy = packetEntityDestroyConstructor.newInstance(new int[]{(int)id.invoke(shulker)});
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
            List<Block> blocks = new ArrayList<>(shulkerMap.keySet());
            for (Block block : blocks) {
                removeBlock(block);
            }
        }

        /**
         * Checks if this highlighter is empty
         * @return True if no blocks are set to be highlighted, false otherwise
         */
        public boolean isEmpty() {
            return shulkerMap.isEmpty();
        }

    }

}
