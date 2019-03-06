package com.njdaeger.enhanceddebugstick.modes.classic;

import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.Property;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.PropertyChangeEvent;
import com.njdaeger.enhanceddebugstick.api.Result;
import com.njdaeger.enhanceddebugstick.api.ValueChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.DEBUG_STICK;
import static com.njdaeger.enhanceddebugstick.api.DebugStickAPI.hasDebugStick;

public class ClassicDebugMode extends DebugModeType<ClassicDebugMode, ClassicDebugContext> {

    public ClassicDebugMode(String niceName, Class<ClassicDebugMode> type) {
        super(niceName, type);
    }

    @Override
    public boolean addSession(DebugSession session) {
        if (!hasSession(session.getSessionId())) {
            contextTrack.put(session.getSessionId(), new ClassicDebugContext(session));
            return true;
        }
        return false;
    }

    @Override
    public String getPermission() {
        return "enhanceddebugstick.classic";
    }

    @Override
    public DebugModeType getModeType() {
        return DebugModeType.CLASSIC;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (hasDebugStick(player) && DEBUG_STICK.equals(player.getInventory().getItemInMainHand()) && event.getHand() == EquipmentSlot.HAND) {

            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            if (!player.hasPermission(getPermission() + ".use")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the Debug Stick");
                return;
            }

            Block block = event.getClickedBlock();
            ClassicDebugContext context = getDebugContext(player.getUniqueId());

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                PropertyChangeEvent changeEvent;

                //
                //If this block does not have properties to be edited, we dont let the change happen.
                //
                if (!Property.hasProperties(event.getClickedBlock())) {
                    player.sendMessage(ChatColor.GRAY + "There are no properties for this block.");
                    changeEvent = new PropertyChangeEvent(context, block, null, null, Result.FAILURE);
                    Bukkit.getPluginManager().callEvent(changeEvent);
                    return;
                }

                //
                //From this point on, the only way the changing of the value can be stopped is by the PropertyChangeEvent
                //
                Property<?, ?> oldProperty = context.getCurrentProperty(block);
                Property<?, ?> newProperty = context.getNextProperty(block);
                changeEvent = new PropertyChangeEvent(context, block, oldProperty, newProperty, Result.SUCCESS);
                Bukkit.getPluginManager().callEvent(changeEvent);

                //
                //If the event hasnt been cancelled, we continue on with changing
                //
                if (!changeEvent.isCancelled()) {
                    if (config.soundOnNextProperty()) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    context.applyNextPropertyFor(block);
                    context.sendPropertiesOf(block);
                }
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                ValueChangeEvent changeEvent;
                Property<?, ?> property = context.getCurrentProperty(event.getClickedBlock());

                //
                //Block being right clicked has no properties to edit.
                //
                if (property == null) {
                    player.sendMessage(ChatColor.GRAY + "There are no properties for this block.");
                    changeEvent = new ValueChangeEvent(context, block, null, null, null, Result.FAILURE);
                    Bukkit.getPluginManager().callEvent(changeEvent);
                    return;
                }

                //
                //From this point on, the only way the changing of the value can be stopped is by the ValueChangeEvent.
                //
                Object from = property.getCurrentValue(block);
                Object to = property.getNextValue(block);
                changeEvent = new ValueChangeEvent(context, block, property, from, to, Result.SUCCESS);
                Bukkit.getPluginManager().callEvent(changeEvent);

                //
                //If the event hasnt been cancelled, we continue on with changing
                //
                if (!changeEvent.isCancelled()) {
                    if (config.soundOnNextValue()) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    context.applyNextValueFor(block);
                    context.sendPropertiesOf(block);
                }
            }
        }
    }

    @Override
    public void onMove(PlayerMoveEvent event) {

    }

    @Override
    public void onHeld(PlayerItemHeldEvent event) {

    }

}
