package com.njdaeger.enhanceddebugstick.modes.copy;

import com.njdaeger.enhanceddebugstick.ConfigKey;
import com.njdaeger.enhanceddebugstick.api.DebugContext;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import com.njdaeger.enhanceddebugstick.session.DebugSession;
import com.njdaeger.enhanceddebugstick.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class CopyDebugContext implements DebugContext {
    
    private final DebugSession session;
    private List<IProperty<?, ?>> clipboardProperties;
    private BlockData clipboard;

    CopyDebugContext(DebugSession session) {
        this.session = session;
        this.clipboardProperties = new ArrayList<>();
    }

    @Override
    public UUID getOwner() {
        return session.getSessionId();
    }

    @Override
    public DebugSession getDebugSession() {
        return session;
    }

    /**
     * Sets the current blockdata which is to be pasted on any applied blocks.
     *
     * @param blockData The clipboard blockdata, can be null
     */
    public void setClipboard(BlockData blockData) {
        this.clipboard = blockData;
        this.clipboardProperties = IProperty.getProperties(blockData);
    }
    
    /**
     * Sets the properties which are to be pasted on blocks. When properties are taken away from this list, they will
     * not be applied to the block being pasted on- regardless if the source block that was copied has said property.
     * @param clipboardProperties The list of properties that need to be pasted.
     */
    public void setClipboardProperties(List<IProperty<?, ?>> clipboardProperties) {
        this.clipboardProperties = Objects.requireNonNullElseGet(clipboardProperties, ArrayList::new);
    }
    
    /**
     * Gets the current blockdata in the clipboard.
     *
     * @return The current blockdata in the clipboard, or null if theres no blockdata copied
     */
    public BlockData getClipboard() {
        return clipboard;
    }
    
    /**
     * Checks whether the clipboard has anything in it currently
     *
     * @return True if the clipboard has data, false otherwise.
     */
    public boolean hasClipboard() {
        return getClipboard() != null;
    }

    /**
     * Gets a list of properties which the clipboard will paste to the block being pasted on. This will usually match
     * the source block, however, can differ if an external plugin changes what properties are copied.
     *
     * @return The list of properties the clipboard has.
     */
    public List<IProperty<?, ?>> getClipboardProperties() {
        return clipboardProperties;
    }

    /**
     * Applies the clipboard to the given block, pasting any similar properties which dont match on either block.
     *
     * @param block The block to apply the block data to.
     */
    public void applyClipboardFor(Block block) {

        List<IProperty<?, ?>> clipboardProperties = getClipboardProperties();
        for (IProperty<?, ?> property : clipboardProperties) {
            if (property.isApplicableTo(block)) {
                block.setBlockData(property.mergeBlockData(clipboard, block), false);
            }
        }
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
                builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD).append(property.getNiceName()).append(": ");
                builder.append(ChatColor.GRAY).append(ChatColor.BOLD).append(Util.format(property.getNiceCurrentValue(block))).append("    ");
            }
        }
        session.sendBar(builder.toString().trim());
    }

    /**
     * Sends the player information about the properties of a specific block in comparison to the current clipboard. If
     * the clipboard is empty, an empty actionbar will be sent towards the player. Properties in DARK_GRAY are
     * properties which the clipboard has which have the same value as the block being looked at. Properties in RED are
     * properties which the clipboard has which the block doesnt, or vice versa. Properties in GREEN are properties
     * which the clipboard and the block have, which do not have the same value.
     *
     * @param block The block to compare to the clipboard properties.
     */
    public void sendMeshedPropertiesOf(Block block) {
        StringBuilder builder = new StringBuilder();
        if (block != null && hasClipboard()) {

            List<IProperty<?, ?>> clipboardProperties = getClipboardProperties();

            for (IProperty<?, ?> property : IProperty.getProperties(block)) {
                if (clipboardProperties.contains(property)) {
                    //If the property value of both match, just set the property color to dark gray.
                    if (property.getCurrentValue(clipboard).equals(property.getCurrentValue(block))) {
                        if (!ConfigKey.get().COPY_DISPLAY_ALL) continue;
                        builder.append(ChatColor.DARK_GRAY).append(ChatColor.BOLD).append(property.getNiceName()).append(": ");
                    } else builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD).append(property.getNiceName()).append(": ");

                    builder.append(ChatColor.GRAY).append(ChatColor.BOLD).append(ChatColor.ITALIC).append(Util.format(property.getNiceCurrentValue(clipboard)));
                } else {
                    if (!ConfigKey.get().COPY_DISPLAY_ALL) continue;
                    builder.append(ChatColor.RED).append(ChatColor.BOLD).append(property.getNiceName()).append(": ");
                    builder.append(ChatColor.GRAY).append(ChatColor.BOLD).append(Util.format(property.getNiceCurrentValue(block)));
                }
                builder.append(ChatColor.RESET).append("    ");
            }
        }
        session.sendBar(builder.toString().trim());
    }
}
