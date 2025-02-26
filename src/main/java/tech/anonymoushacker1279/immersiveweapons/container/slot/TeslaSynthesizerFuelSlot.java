package tech.anonymoushacker1279.immersiveweapons.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.anonymoushacker1279.immersiveweapons.container.TeslaSynthesizerContainer;

public class TeslaSynthesizerFuelSlot extends Slot {

	private final TeslaSynthesizerContainer abstractTeslaSynthesizerContainer;

	/**
	 * Constructor for TeslaSynthesizerFuelSlot.
	 *
	 * @param teslaSynthesizerFuelSlot  the <code>AbstractTeslaSynthesizerContainer</code> instance
	 * @param teslaSynthesizerInventory the <code>IInventory</code> of the tile entity
	 * @param slotIndex                 the slot index
	 * @param xPos                      the X position of the slot
	 * @param yPos                      the Y position of the slot
	 */
	public TeslaSynthesizerFuelSlot(TeslaSynthesizerContainer teslaSynthesizerFuelSlot, Container teslaSynthesizerInventory, int slotIndex, int xPos, int yPos) {
		super(teslaSynthesizerInventory, slotIndex, xPos, yPos);
		abstractTeslaSynthesizerContainer = teslaSynthesizerFuelSlot;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot.
	 *
	 * @param stack the <code>ItemStack</code> to be checked
	 * @return boolean
	 */
	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return abstractTeslaSynthesizerContainer.isFuel(stack);
	}

	/**
	 * Get the max stack size.
	 *
	 * @param stack the <code>ItemStack</code> to be checked
	 * @return int
	 */
	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		return super.getMaxStackSize(stack);
	}
}