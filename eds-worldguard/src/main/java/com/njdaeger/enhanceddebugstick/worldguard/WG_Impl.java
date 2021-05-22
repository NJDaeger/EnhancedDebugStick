package com.njdaeger.enhanceddebugstick.worldguard;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.event.CopyPropertyEvent;
import com.njdaeger.enhanceddebugstick.event.FreezeBlockEvent;
import com.njdaeger.enhanceddebugstick.event.PastePropertyEvent;
import com.njdaeger.enhanceddebugstick.event.ValueChangeEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.plaf.synth.Region;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class WG_Impl extends JavaPlugin implements Listener {
    
    public static StateFlag BLACKLIST;
    public static StateFlag OWNER_DEBUG;
    public static StateFlag MEMBER_DEBUG;
    
    private WG_Config config;
    
    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        /*
        
        debug-blacklist
        owner-debugging
        member-debugging
        
        debug-blacklist:
          - [age]               //prevents editing of all age properties
          - wheat               //prevents editing of all wheat blocks
          - pumpkin_stem[age]   //prevents editing of all pumpkin stem age, you can still edit the direction, though
        
         */
    
        StateFlag flag;
        
        try {
            flag = new StateFlag("debug-blacklist", true);
            registry.register(flag);
            BLACKLIST = flag;
        } catch (FlagConflictException e) {
            Flag<?> conflict = registry.get("debug-blacklist");
            if (conflict instanceof StateFlag) {
                BLACKLIST = (StateFlag)conflict;
            } else {
                Bukkit.getLogger().warning("Unable to register 'debug-blacklist' flag. Another flag with the same name is using it!");
            }
        }
    
        try {
            flag = new StateFlag("owner-debugging", true);
            registry.register(flag);
            OWNER_DEBUG = flag;
        } catch (FlagConflictException e) {
            Flag<?> conflict = registry.get("owner-debugging");
            if (conflict instanceof StateFlag) {
                OWNER_DEBUG = (StateFlag)conflict;
            } else {
                Bukkit.getLogger().warning("Unable to register 'owner-debugging' flag. Another flag with the same name is using it!");
            }
        }
    
        try {
            flag = new StateFlag("member-debugging", true);
            registry.register(flag);
            MEMBER_DEBUG = flag;
        } catch (FlagConflictException e) {
            Flag<?> conflict = registry.get("member-debugging");
            if (conflict instanceof StateFlag) {
                MEMBER_DEBUG = (StateFlag)conflict;
            } else {
                Bukkit.getLogger().warning("Unable to register 'member-debugging' flag. Another flag with the same name is using it!");
            }
        }
        
    }
    
    @Override
    public void onEnable() {
        config = new WG_Config(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    @EventHandler
    public void onFreezeEvent(FreezeBlockEvent event) {
        ApplicableRegionSet regionSet = getRegionSetAt(event.getFrozenBlock().getLocation());
        LocalPlayer player = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
        StateFlag.State ownerState = regionSet.queryState(player, OWNER_DEBUG);
        StateFlag.State memberState = regionSet.queryState(player, MEMBER_DEBUG);
        StateFlag.State blacklistState = regionSet.queryState(player, BLACKLIST);
        
        /*
        
        if owner of all regions at location
            if owner debugging is not allowed, deny freezing
            otherwise if blacklist is not set, allow them to freeze
            otherwise, check blacklist state and see if material is blacklisted.
        
        otherwise if member of all regions at a location
            if member debugging is not allowed, deny freezing
            otherwise if blacklist is not set, allow them to freeze
            otherwise, check blacklist state and see if material is blacklisted
        
         */
        
        System.out.println(regionSet.getRegions().stream().map(ProtectedRegion::getId).map(s -> s += " ").collect(Collectors.joining()));
        System.out.println(Arrays.toString(player.getGroups()));
//        if (regionSet.isOwnerOfAll(player)) {

            if (!regionSet.testState(player, OWNER_DEBUG)/*ownerState != null && !ownerState.equals(StateFlag.State.ALLOW)*/) {
                System.out.println(1);
                event.setCancelled(true);
                event.getDebugContext().getDebugSession().sendForcedBar(ChatColor.RED + "You can't freeze blocks here.");
                return;
            } else if (config.ownerBypass()) return; //if we are allowed to freeze blocks, we need to check the blacklist, so we just check if the owners bypass the blacklist and stop the event
//        }
//        if (regionSet.isMemberOfAll(player)) {
            System.out.println(2);
            if (!regionSet.testState(player, MEMBER_DEBUG)/*memberState != null && !memberState.equals(StateFlag.State.ALLOW)*/) {
                System.out.println(3);
                event.setCancelled(true);
                event.getDebugContext().getDebugSession().sendForcedBar(ChatColor.RED + "You can't freeze blocks here.");
                return;
            }
//       } else {
//           System.out.println(4);
//           event.setCancelled(true);
//           event.getDebugContext().getDebugSession().sendForcedBar(ChatColor.RED + "You can't freeze blocks here.");
//           return;
//        }
        if (blacklistState != null && blacklistState.equals(StateFlag.State.ALLOW) && config.getBlacklistedMaterials().contains(event.getFrozenBlock().getType())) {//blacklist is ONLY effective if it is set to ALLOW
            event.setCancelled(true);
            event.getDebugContext().getDebugSession().sendForcedBar(ChatColor.RED + "This material is blacklisted from being frozen.");
        }
    }
    
    @EventHandler
    public void onValueChangeEvent(ValueChangeEvent event) {
        //todo check if the block being changed is a specific material and check if it is a specific property being changed.
    }
    
    @EventHandler
    public void onPasteEvent(PastePropertyEvent event) {
        //todo: check if the region being pasted in has a blacklist restriction. If it does, set the pastable clipboard properties to the current clipboard minus the ones that are blacklisted.
        //also check if the block being pasted on is a blacklisted block to edit.
    }
    
    @EventHandler
    public void onCopyEvent(CopyPropertyEvent event) {
        //todo check if the block being copied is a blacklisted block to edit.
        //probs not needed here anymore. event.getDebugContext().setClipboardProperties(event.getCopiedProperties().stream().filter(e -> !config.getBlacklistedProperties().contains(e)).collect(Collectors.toList()));
    }
    
    private ApplicableRegionSet getRegionSetAt(Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.getApplicableRegions(BukkitAdapter.adapt(location));
    }

}
