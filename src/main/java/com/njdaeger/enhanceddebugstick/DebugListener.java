package com.njdaeger.enhanceddebugstick;

import com.njdaeger.enhanceddebugstick.api.PropertyChangeEvent;
import com.njdaeger.enhanceddebugstick.api.Result;
import com.njdaeger.enhanceddebugstick.api.ValueChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;
import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.hasDebugStick;

public final class DebugListener implements Listener {

    private final EnhancedDebugStick plugin;
    private final Configuration config;

    DebugListener(EnhancedDebugStick plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.config = plugin.getDebugConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.getDebugSession(event.getPlayer().getUniqueId()) == null) plugin.addDebugSession(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (hasDebugStick(player) && DEBUG_STICK.equals(player.getInventory().getItemInMainHand()) && event.getHand() == EquipmentSlot.HAND) {

            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            if (!player.hasPermission("enhanceddebugstick.use")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the Debug Stick");
                return;
            }

            Block block = event.getClickedBlock();
            DebugSession session = plugin.getDebugSession(player.getUniqueId());

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                PropertyChangeEvent changeEvent;

                //
                //If this block does not have properties to be edited, we dont let the change happen.
                //
                if (!Property.hasProperties(event.getClickedBlock())) {
                    player.sendMessage(ChatColor.GRAY + "There are no properties for this block.");
                    changeEvent = new PropertyChangeEvent(session, block, null, null, Result.FAILURE);
                    Bukkit.getPluginManager().callEvent(changeEvent);
                    return;
                }

                //
                //From this point on, the only way the changing of the value can be stopped is by the PropertyChangeEvent
                //
                Property<?, ?> oldProperty = session.getCurrentProperty(block);
                Property<?, ?> newProperty = session.getNextProperty(block);
                changeEvent = new PropertyChangeEvent(session, block, oldProperty, newProperty, Result.SUCCESS);
                Bukkit.getPluginManager().callEvent(changeEvent);

                //
                //If the event hasnt been cancelled, we continue on with changing
                //
                if (!changeEvent.isCancelled()) {
                    if (config.soundOnNextProperty()) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    session.applyNextPropertyFor(block);
                    session.sendPropertiesOf(block);
                }
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                ValueChangeEvent changeEvent;
                Property<?, ?> property = session.getCurrentProperty(event.getClickedBlock());

                //
                //Block being right clicked has no properties to edit.
                //
                if (property == null) {
                    player.sendMessage(ChatColor.GRAY + "There are no properties for this block.");
                    changeEvent = new ValueChangeEvent(session, block, null, null, null, Result.FAILURE);
                    Bukkit.getPluginManager().callEvent(changeEvent);
                    return;
                }

                //
                //From this point on, the only way the changing of the value can be stopped is by the ValueChangeEvent.
                //
                Object from = property.getCurrentValue(block);
                Object to = property.getNextValue(block);
                changeEvent = new ValueChangeEvent(session, block, property, from, to, Result.SUCCESS);
                Bukkit.getPluginManager().callEvent(changeEvent);

                //
                //If the event hasnt been cancelled, we continue on with changing
                //
                if (!changeEvent.isCancelled()) {
                    if (config.soundOnNextValue()) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    session.applyNextValueFor(block);
                    session.sendPropertiesOf(block);
                }
            }
        }
    }

    @EventHandler
    public void onLook(PlayerMoveEvent event) {

        //
        //Check if the configuration allows data viewing
        //
        if (config.displayDataOnLook()) {
            Player player = event.getPlayer();
            DebugSession session = plugin.getDebugSession(player.getUniqueId());
            if (hasDebugStick(player) && DEBUG_STICK.equals(player.getInventory().getItemInMainHand()) && player.hasPermission("greenfieldcore.debugstick.use")) {
                RayTraceResult hit = player.rayTraceBlocks(config.displayDataDistance());
                if (hit != null && hit.getHitBlock() != null) {
                    session.sendPropertiesOf(hit.getHitBlock());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryChange(PlayerItemHeldEvent event) {

        if (config.soundOnSelect()) {
            Player player = event.getPlayer();
            ItemStack oldSlot = player.getInventory().getItem(event.getPreviousSlot());
            ItemStack newSlot = player.getInventory().getItem(event.getNewSlot());

            if (DEBUG_STICK.equals(oldSlot)) {
                player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
            }
            if (DEBUG_STICK.equals(newSlot)) {
                player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 1, 1);
            }
        }
    }

}
