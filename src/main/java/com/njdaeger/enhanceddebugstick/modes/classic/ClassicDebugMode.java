package com.njdaeger.enhanceddebugstick.modes.classic;

import com.njdaeger.enhanceddebugstick.Configuration;
import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

public class ClassicDebugMode extends DebugModeType<ClassicDebugMode, ClassicDebugContext> {

    private final Configuration config;

    /**
     * The Classic Debug Mode
     */
    public ClassicDebugMode() {
        super("Classic", ClassicDebugMode.class);
        this.config = plugin.getDebugConfig();
    }

    @Override
    public String getBasePermission() {
        return "enhanceddebugstick.classic";
    }

    @Override
    public ClassicDebugMode getModeType() {
        return DebugModeType.CLASSIC;
    }

    @Override
    public ClassicDebugContext createContext(DebugSession session) {
        return new ClassicDebugContext(session);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DebugSession session = plugin.getDebugSession(player.getUniqueId());

        //We check if this Debug Mode contains the players session, we also check if the player is holding the debug stick
        if (hasSession(player.getUniqueId()) && session.isHoldingDebugStick() && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            //Check for the use permission
            if (!player.hasPermission(getBasePermission() + ".use")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the Classic Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            ClassicDebugContext context = session.toDebugContext(this);

            //This really should never be ran, in the case that it is though, provide instructions.
            if (context == null) {
                player.sendMessage(ChatColor.RED + "Cannot bind DebugSession to ClassicDebugContext.");
                Bukkit.getLogger().warning("Cannot bind DebugSession to ClassicDebugContext. Please create a ticket and send your latest.log to https://github.com/NJDaeger/Bug-Reports! Sorry for this issue.");
                return;
            }

            //When the action is a left click on a block
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                if (!IProperty.hasProperties(event.getClickedBlock())) {
                    player.sendMessage(ChatColor.GRAY + "There are no properties for this block.");
                    return;
                }

                if (config.soundOnNextProperty()) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                context.applyNextPropertyFor(block);
                context.sendPropertiesOf(block);
                return;
            }

            //When the action is a right click on a block
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                IProperty<?, ?> property = context.getCurrentProperty(event.getClickedBlock());

                if (property == null) {
                    player.sendMessage(ChatColor.GRAY + "There are no properties for this block.");
                    return;
                }

                if (config.soundOnNextValue()) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                context.applyNextValueFor(block);
                context.sendPropertiesOf(block);
            }
        }
    }

    @Override
    public void onMove(PlayerMoveEvent event) {

        //
        //Check if the configuration allows data viewing
        //
        if (config.displayDataOnLook()) {
            Player player = event.getPlayer();
            ClassicDebugContext context = getDebugContext(player.getUniqueId());
            if (context.getDebugSession().isHoldingDebugStick() && player.hasPermission(getBasePermission() + ".use")) {
                RayTraceResult hit = player.rayTraceBlocks(config.displayDataDistance());
                if (hit != null && hit.getHitBlock() != null) {
                    context.sendPropertiesOf(hit.getHitBlock());
                } else context.sendPropertiesOf(null);
            }
        }
    }
}
