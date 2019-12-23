package com.njdaeger.enhanceddebugstick.modes.freeze;

import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotArea;
import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.Permissions;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import static org.bukkit.ChatColor.*;

public class FreezeDebugMode extends DebugModeType<FreezeDebugMode, FreezeDebugContext> {

    public FreezeDebugMode() {
        super("Freeze", FreezeDebugMode.class);
    }

    @Override
    public String getBasePermission() {
        return Permissions.COPY_MODE;
    }

    @Override
    public FreezeDebugMode getModeType() {
        return DebugModeType.FREEZE;
    }

    @Override
    public void pauseSession(DebugSession session) {
        session.toDebugContext(this).unlightFrozen();
        paused.add(session.getSessionId());
    }

    @Override
    public void resumeSession(DebugSession session) {
        session.toDebugContext(this).lightFrozen();
        paused.remove(session.getSessionId());
    }

    @Override
    public FreezeDebugContext createContext(DebugSession session) {
        return new FreezeDebugContext(session);
    }

    @Override
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        DebugSession session = plugin.getDebugSession(event.getPlayer().getUniqueId());

        if (session.isUsing(this) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            if (!session.hasPermission(this)) {
                session.sendMessage(RED + "You do not have permission to use the Freeze Debug Mode");
                return;
            }

            Block block = event.getClickedBlock();
            FreezeDebugContext context = session.toDebugContext(this);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                if (!context.hasFrozenBlocks()) {
                    session.sendForcedBar(RED.toString() + BOLD + "You do not have any frozen blocks");
                    if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }
                if (ConfigKey.FDM_UNFREEZE_ALL) session.sendSound(Sound.ITEM_TRIDENT_RETURN);
                context.unfreezeAllBlocks();
                session.sendMessage(GRAY + "Unfroze all frozen blocks.");
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (context.isSelected(block)) {
                    if (plot(event.getPlayer(), block)) {
                        if (ConfigKey.FDM_UNFREEZE) session.sendSound(Sound.ITEM_AXE_STRIP);
                        context.unfreezeBlock(block);
                    } else session.sendForcedBar(RED.toString() + BOLD + "You are not an owner or a member of the location being unlocked.");
                }
                else {
                    if (plot(event.getPlayer(), block)) {
                        if (ConfigKey.FDM_FREEZE) session.sendSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP);
                        context.freezeBlock(block);
                    } else session.sendForcedBar(RED.toString() + BOLD + "You are not an owner or a member of the location being locked.");
                }
            }

        }
    }

    @Override
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

    }

    private boolean plot(Player player, Block block) {
        if (ConfigKey.PLOT_INTEGRATION) {
            Location location = new Location(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
            PlotArea area = plugin.getPlotAPI().getPlotSquared().getApplicablePlotArea(location);
            if (area == null) return false;
            Plot plot = area.getOwnedPlot(location);
            if (plot == null || plot.isDenied(player.getUniqueId())) return false;
            else return plot.isOwner(player.getUniqueId()) || plot.isAdded(player.getUniqueId());
        }
        return true;
    }

}
