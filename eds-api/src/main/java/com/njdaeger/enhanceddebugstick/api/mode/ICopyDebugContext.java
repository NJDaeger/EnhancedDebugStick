package com.njdaeger.enhanceddebugstick.api.mode;

import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.List;

public interface ICopyDebugContext extends IDebugContext {
    
    /**
     * Sets the current blockdata which is to be pasted on any applied blocks.
     *
     * @param blockData The clipboard blockdata, can be null
     */
    void setClipboard(BlockData blockData);
    
    /**
     * Sets the properties which are to be pasted on blocks. When properties are taken away from this list, they will
     * not be applied to the block being pasted on- regardless if the source block that was copied has said property.
     * @param clipboardProperties The list of properties that need to be pasted.
     */
    void setClipboardProperties(List<IProperty<?, ?>> clipboardProperties);
    
    /**
     * Gets the current blockdata in the clipboard.
     *
     * @return The current blockdata in the clipboard, or null if theres no blockdata copied
     */
    BlockData getClipboard();
    
    /**
     * Checks whether the clipboard has anything in it currently
     *
     * @return True if the clipboard has data, false otherwise.
     */
    boolean hasClipboard();
    
    /**
     * Gets a list of properties which the clipboard will paste to the block being pasted on. This will usually match
     * the source block, however, can differ if an external plugin changes what properties are copied.
     *
     * @return The list of properties the clipboard has.
     */
    List<IProperty<?, ?>> getClipboardProperties();
    
    /**
     * Applies the clipboard to the given block, pasting any similar properties which dont match on either block.
     *
     * @param block The block to apply the block data to.
     */
    void applyClipboardFor(Block block);
    
    /**
     * Sends the player information about the properties of a specific block via ActionBar. If this block has no
     * properties, or if the block is null, the message sent will be empty.
     *
     * @param block The block to get and send the properties of.
     */
    void sendPropertiesOf(Block block);
    
    /**
     * Sends the player information about the properties of a specific block in comparison to the current clipboard. If
     * the clipboard is empty, an empty actionbar will be sent towards the player. Properties in DARK_GRAY are
     * properties which the clipboard has which have the same value as the block being looked at. Properties in RED are
     * properties which the clipboard has which the block doesnt, or vice versa. Properties in GREEN are properties
     * which the clipboard and the block have, which do not have the same value.
     *
     * @param block The block to compare to the clipboard properties.
     */
    void sendMeshedPropertiesOf(Block block);
    
}
