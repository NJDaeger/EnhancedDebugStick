package com.njdaeger.enhanceddebugstick.modes.classic;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.api.Permissions;
import com.njdaeger.enhanceddebugstick.event.PropertyChangeEvent;
import com.njdaeger.enhanceddebugstick.event.ValueChangeEvent;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

public class ClassicDebugMode extends DebugModeType<ClassicDebugMode, ClassicDebugContext> {

    /**
     * The Classic Debug Mode
     */
    public ClassicDebugMode() {
        super("Classic", ClassicDebugMode.class);
    }

    @Override
    public String getBasePermission() {
        return Permissions.CLASSIC_MODE;
    }

    @Override
    public ClassicDebugMode getModeType() {
        return DebugModeType.CLASSIC;
    }

    @Override
    public void pauseSession(DebugSession session) {
        paused.add(session.getSessionId());
    }

    @Override
    public void resumeSession(DebugSession session) {
        paused.remove(session.getSessionId());
    }

    @Override
    public ClassicDebugContext createContext(DebugSession session) {
        return new ClassicDebugContext(session);
    }

    @Override
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        //We check if this Debug Mode contains the players session, we also check if the player is holding the debug stick
        if (session.isUsing(this) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            //Check for the use permission
            if (!session.hasPermission(this)) {
                session.sendMessage(ChatColor.RED + "You do not have permission to use the Classic Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            ClassicDebugContext context = session.toDebugContext(this);
            if (block == null) return;
            //When the action is a left click on a block
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                
                IProperty<?, ?> currentProp = context.getCurrentProperty(block);
                IProperty<?, ?> newProp = context.getNextProperty(block);
                PropertyChangeEvent propEvent = new PropertyChangeEvent(event.getPlayer(), block, currentProp, newProp, context);
                Bukkit.getPluginManager().callEvent(propEvent);
                
                if (propEvent.isCancelled()) return;
                
                if (!IProperty.hasProperties(propEvent.getBlock())) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "This block has no properties");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (!propEvent.isPropertyApplicable()) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "You cannot apply that property to that block");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }
                
                if (ConfigKey.get().CDM_PROPERTY) session.sendSound(Sound.UI_BUTTON_CLICK);
                context.setCurrent(propEvent.getBlock(), propEvent.getNextProperty());
                context.sendPropertiesOf(propEvent.getBlock());
                return;
            }

            //When the action is a right click on a block
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
                
                IProperty<?, ?> property = context.getCurrentProperty(block);
    
                if (property == null) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "This block has no properties");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }
                
                ValueChangeEvent valEvent = new ValueChangeEvent(event.getPlayer(), block, property.getCurrentValue(block), property.getNextValue(block), property, context);
                Bukkit.getPluginManager().callEvent(valEvent);

                if (valEvent.isCancelled()) return;
    
                if (!valEvent.isValueApplicable()) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "You cannot apply that property to that block");
                    if (ConfigKey.get().SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }
                
                if (ConfigKey.get().CDM_VALUE) session.sendSound(Sound.UI_BUTTON_CLICK);
                context.setValue(valEvent.getBefore(), valEvent.getNextValue());
                context.sendPropertiesOf(valEvent.getBefore());
            }
        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        //
        //Check if the configuration allows data viewing
        //
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        if (ConfigKey.get().CDM_DISPLAY_ON_LOOK && session.isUsing(this) && session.hasPermission(this)) {
            ClassicDebugContext context = session.toDebugContext(this);
            RayTraceResult hit = event.getPlayer().rayTraceBlocks(ConfigKey.get().CDM_DISPLAY_DISTANCE);
            if (hit != null && hit.getHitBlock() != null) context.sendPropertiesOf(hit.getHitBlock());
            else context.sendPropertiesOf(null);
        }
    }
}
