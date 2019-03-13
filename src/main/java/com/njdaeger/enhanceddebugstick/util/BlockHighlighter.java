package com.njdaeger.enhanceddebugstick.util;

import com.njdaeger.btu.Util;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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

    private static final Map<UUID, HighlightTask> tasks = new HashMap<>();

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

    }

    private BlockHighlighter() {}

    public static void removeTask(Player player) {
        if (tasks.containsKey(player.getUniqueId())) {
            HighlightTask task = tasks.get(player.getUniqueId());
            Bukkit.getScheduler().cancelTask(task.getId());
        }
    }

    public static void unLightAllBlocks(Player player) {
        if (tasks.containsKey(player.getUniqueId())) {
            HighlightTask task = tasks.get(player.getUniqueId());
            task.removeAllBlocks();
        }
    }

    public static void unLightBlock(Block block, Player player) {
        if (tasks.containsKey(player.getUniqueId())) {
            HighlightTask task = tasks.get(player.getUniqueId());
            task.removeBlock(block);
        }
    }

    public static void lightBlock(Plugin plugin, Block block, Player player) {
        if (!tasks.containsKey(player.getUniqueId())) {
            HighlightTask task = new HighlightTask(player);
            tasks.put(player.getUniqueId(), task);
            BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, task, 0, 2);
            task.taskId = bukkitTask.getTaskId();
        }
        tasks.get(player.getUniqueId()).addBlock(block);
    }

    public static class HighlightTask implements Runnable {

        private final Map<Block, Object> magmaCubeMap;
        private Object entityPlayer;
        private int taskId;

        private HighlightTask(Player player) {
            this.magmaCubeMap = new HashMap<>();
            try {
                this.entityPlayer = Util.getOBCClass("entity.CraftPlayer").getDeclaredMethod("getHandle").invoke(player);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
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

        public void removeBlock(Block block) {
            Object magmaCube = magmaCubeMap.remove(block);
            if (magmaCube != null) {
                try {
                    Object destroy = packetEntityDestroyConstructor.newInstance((int)id.invoke(magmaCube));
                    sendPacket.invoke(connection.get(entityPlayer), destroy);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }

        public void removeAllBlocks() {
            Collection<Block> blocks = Collections.unmodifiableSet(magmaCubeMap.keySet());
            for (Block block : blocks) {
                removeBlock(block);
            }
        }

        public boolean isEmpty() {
            return magmaCubeMap.isEmpty();
        }

        public int getId() {
            return taskId;
        }

    }

}
