package com.njdaeger.enhanceddebugstick.util.highlighter.impl;

import com.njdaeger.enhanceddebugstick.util.highlighter.IHighlightSession;
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

public class v1194HighlightSession implements IHighlightSession {
    
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
    private static Constructor<?> packetEntityLivingConstructor;
    private static Constructor<?> packetEntityMetadataConstructor;
    private static Constructor<?> packetEntityDestroyConstructor;
    
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
            entityType = Util.tryField(Util.getNMSClass("EntityTypes", "net.minecraft.world.entity."), "aG");
            if (entityType == null) throw new RuntimeException("Unable to find the Shulker field in EntityTypes class");
    
            invisible = Util.tryMethod(entityClass, new Class<?>[]{boolean.class}, "setInvisible", "j");
            if (invisible == null) throw new RuntimeException("Unable to find the setInvisible method in the Entity class");
    
            invulnerable = Util.tryMethod(entityClass, new Class<?>[]{boolean.class}, "setInvulnerable", "m");
            if (invulnerable == null) throw new RuntimeException("Unable to find the setInvulnerable method in the Entity class");
    
            noAi = Util.tryMethod(insentientClass, new Class<?>[]{boolean.class}, "setNoAI", "t");
            if (noAi == null) throw new RuntimeException("Unable to find the setNoAI method in the EntityInsentient class");
    
            location = Util.tryMethod(entityClass, new Class<?>[]{double.class, double.class, double.class, float.class, float.class}, "setLocation", "b");
            if (location == null) throw new RuntimeException("Unable to find the setLocation method in the Entity class");
    
            flag = Util.tryMethod(entityClass, new Class<?>[]{int.class, boolean.class}, "setFlag", "b");
            if (flag == null) throw new RuntimeException("Unable to find the setFlag method in the Entity class");
    
            id = Util.tryMethod(entityClass,  null, "af");
            if (id == null) throw new RuntimeException("Unable to find the getId method in the Entity class");
    
            dataWatcher = Util.tryMethod(entityClass,  null, "aj");
            if (dataWatcher == null) throw new RuntimeException("Unable to find the getDataWatcher method in the Entity class");
    
            entityLivingClass = Util.getNMSClass("EntityLiving", "net.minecraft.world.entity.");
            world = Util.getOBCClass("CraftWorld").getDeclaredMethod("getHandle");
            shulkerConstructor = shulkerClass.getConstructor(Util.getNMSClass("EntityTypes", "net.minecraft.world.entity."), Util.getNMSClass("World", "net.minecraft.world.level."));
    
            Class<?> spawnEntityPacketClass = Util.tryNMSClass("net.minecraft.network.protocol.game.", "PacketPlayOutSpawnEntityLiving", "PacketPlayOutSpawnEntity");
            if (spawnEntityPacketClass == null) throw new RuntimeException("Unable to find the PacketPlayOutSpawnEntity class");
    
            packetEntityLivingConstructor = spawnEntityPacketClass.getDeclaredConstructor(entityClass);
            packetEntityMetadataConstructor = Util.getNMSClass("PacketPlayOutEntityMetadata", "net.minecraft.network.protocol.game.").getDeclaredConstructor(int.class, List.class);
            packetEntityDestroyConstructor = Util.getNMSClass("PacketPlayOutEntityDestroy", "net.minecraft.network.protocol.game.").getDeclaredConstructor(int[].class);
            
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    private final Map<Block, Object> shulkerMap;
    private final Object player;
    
    public v1194HighlightSession(Player player) {
        this.shulkerMap = new HashMap<>();
        try {
            this.player = Util.getOBCClass("entity.CraftPlayer").getDeclaredMethod("getHandle").invoke(player);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create a highlight session for player " + player.getName(), e);
        }
        
    }
    
    @Override
    public void addBlock(Block block) {
        try {
            Object shulker;
            shulker = shulkerConstructor.newInstance(entityType.get(null), world.invoke(block.getWorld()));
        
            invisible.invoke(shulker, true);
            invulnerable.invoke(shulker, true);
            noAi.invoke(shulker, true);
            location.invoke(shulker, block.getX() + .5, block.getY(), block.getZ() + .5, 0, 0);
            flag.invoke(shulker, 6, true);
        
            shulkerMap.put(block, shulker);
        
            Object spawn = packetEntityLivingConstructor.newInstance(entityLivingClass.cast(shulker));
            Object effect;
            Object watcher = dataWatcher.invoke(shulker);
            Method defVals = watcher.getClass().getDeclaredMethod("c", null);
            effect = packetEntityMetadataConstructor.newInstance(id.invoke(shulker), defVals.invoke(watcher));
            
            sendPacket.invoke(connection.get(player), spawn);
            sendPacket.invoke(connection.get(player), effect);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void removeBlock(Block block) {
        Object shulker = shulkerMap.remove(block);
        if (shulker != null) {
            try {
                Object destroy;
                destroy = packetEntityDestroyConstructor.newInstance(new int[]{(int)id.invoke(shulker)});
                sendPacket.invoke(connection.get(player), destroy);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void removeAllBlocks() {
        List<Block> blocks = new ArrayList<>(shulkerMap.keySet());
        for (Block block : blocks) {
            removeBlock(block);
        }
    }
}
