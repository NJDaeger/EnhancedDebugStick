package com.njdaeger.enhanceddebugstick.modes.copy;

import com.njdaeger.btu.ActionBar;
import com.njdaeger.enhanceddebugstick.DebugSession;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.List;
import java.util.UUID;

import static com.njdaeger.enhanceddebugstick.Util.format;
import static org.bukkit.ChatColor.*;

public final class CopyDebugContext implements DebugContext {

    private final DebugSession session;
    private BlockData clipboard;

    CopyDebugContext(DebugSession session) {
        this.session = session;
    }

    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }

    @Override
    public DebugSession getDebugSession() {
        return session;
    }

    public void setClipboard(BlockData blockData) {
        this.clipboard = blockData;
    }

    public BlockData getClipboard() {
        return clipboard;
    }

    public boolean hasClipboard() {
        return getClipboard() != null;
    }

    //TODO double left click for clipboard clearing
    //TODO implement good sounds
    //TODO implement configuration keys
    //TODO

    public void applyClipboardFor(Block block) {

        List<IProperty<?, ?>> clipboardProperties = getClipboardProperties();

        for (IProperty<?, ?> property : clipboardProperties) {
            if (property.isApplicableTo(block)) {
                block.setBlockData(property.mergeBlockData(clipboard, block), false);
            }
        }
    }

    public List<IProperty<?, ?>> getClipboardProperties() {
        return IProperty.getProperties(clipboard);
    }

    /**
     * Sends the player information about the properties of a specific block via ActionBar. If this block has no
     * properties, or if the block is null, the message sent will be empty.
     *
     * @param block The block to get and send the properties of.
     */
    public void sendPropertiesOf(Block block) {
        StringBuilder builder = new StringBuilder();
        if (block != null) {
            for (IProperty<?, ?> property : IProperty.getProperties(block)) {
                builder.append(DARK_GREEN).append(BOLD).append(property.getNiceName()).append(": ");
                builder.append(GRAY).append(BOLD).append(format(property.getNiceCurrentValue(block))).append("    ");
            }
        }
        ActionBar.of(builder.toString().trim()).sendTo(Bukkit.getPlayer(session.getSessionId()));
    }

    public void sendMeshedPropertiesOf(Block block) {
        StringBuilder builder = new StringBuilder();
        if (block != null) {

            List<IProperty<? ,?>> clipboardProperties = getClipboardProperties();

            for (IProperty<?, ?> property : IProperty.getProperties(block)) {
                if (clipboardProperties.contains(property)) {
                    //If the property value of both match, just set the property color to dark gray.
                    if (property.getCurrentValue(clipboard).equals(property.getCurrentValue(block))) {
                        builder.append(DARK_GRAY).append(BOLD).append(property.getNiceName()).append(": ");
                    } else builder.append(DARK_GREEN).append(BOLD).append(property.getNiceName()).append(": ");

                    builder.append(GRAY).append(BOLD).append(ITALIC).append(format(property.getNiceCurrentValue(clipboard)));
                    builder.append(RESET).append("    ");
                } else {
                    builder.append(RED).append(BOLD).append(property.getNiceName()).append(": ");
                    builder.append(GRAY).append(BOLD).append(format(property.getNiceCurrentValue(block)));
                    builder.append(RESET).append("    ");
                }
            }
        }
        session.sendBar(builder.toString().trim());
    }

}
