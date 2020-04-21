package com.njdaeger.enhanceddebugstick.modes.classic;

import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotArea;
import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.Permissions;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

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

            //When the action is a left click on a block
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                if (!IProperty.hasProperties(block)) {
                    session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "This block has no properties");
                    if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                    return;
                }

                if (ConfigKey.CDM_PROPERTY) session.sendSound(Sound.UI_BUTTON_CLICK);
                context.applyNextPropertyFor(block);
                context.sendPropertiesOf(block);
                return;
            }

            //When the action is a right click on a block
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (plot(event.getPlayer(), block)) {
                    IProperty<?, ?> property = context.getCurrentProperty(block);

                    if (property == null) {
                        session.sendForcedBar(ChatColor.RED.toString() + ChatColor.BOLD + "This block has no properties");
                        if (ConfigKey.SOUND_ON_ERROR) session.sendSound(Sound.UI_TOAST_IN);
                        return;
                    }

                    if (ConfigKey.CDM_VALUE) session.sendSound(Sound.UI_BUTTON_CLICK);
                    context.applyNextValueFor(block);
                    context.sendPropertiesOf(block);
                } else session.sendForcedBar(RED.toString() + BOLD + "You are not an owner or a member of the location being changed.");
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

        if (ConfigKey.CDM_DISPLAY_ON_LOOK && session.isUsing(this) && session.hasPermission(this)) {
            ClassicDebugContext context = session.toDebugContext(this);
            RayTraceResult hit = event.getPlayer().rayTraceBlocks(ConfigKey.CDM_DISPLAY_DISTANCE);
            if (hit != null && hit.getHitBlock() != null) context.sendPropertiesOf(hit.getHitBlock());
            else context.sendPropertiesOf(null);
        }
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
