package nl.dirkkok.chemicalcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import nl.dirkkok.chemicalcraft.tileentity.ChemistryStandEntity;

public class ContainerChemistryStandEntity extends Container {
	private ChemistryStandEntity te;
	
	/* Initializes the container for the GUI and adds slots to it.
	 *
	 * GUI structure:
	 * Progress bar is centered. The rest of the gui is build around it. Elements are spaced by 5 pixels.
	 * progress bar: (76, 29) to (100, 46)
	 * first input slot: (31, 28) to (48, 45)
	 * second input slot: (54, 28) to (72, 45)
	 * first output slot: (104, 28) to (121, 45)
	 * second output slot: (127, 28) to (144, 45)
	 * residue slot: (104, 51) to (121, 68)
	 * fuel meter: (80, 53) to (93, 66)
	 * fuel slot: (54, 51) to (71, 68)
	 * mode button: (78, 4) to (97, 23)
	 *
	 * Slot IDs:
	 * Tile Entity:
	 * Player inventory:
	 *
	 */
	public ContainerChemistryStandEntity(IInventory inv, ChemistryStandEntity te) {
		this.te = te;
		
		// Add slots for the tile entity, slots 0-5
		this.addSlotToContainer(new Slot(te, 0, 32,  29)); // First input
		this.addSlotToContainer(new Slot(te, 1, 55,  29)); // Second input
		this.addSlotToContainer(new Slot(te, 2, 105, 29)); // First output
		this.addSlotToContainer(new Slot(te, 3, 128, 29)); // Second output
		this.addSlotToContainer(new Slot(te, 4, 105, 52)); // Residue
		this.addSlotToContainer(new Slot(te, 5, 55,  52)); // Fuel
		
		// Add slots for the player inventory (copied from ContainerFurnace)
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++)	{
				this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for (int i = 0; i < 9; i++)	{
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.te.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
		ItemStack previous = null;
		Slot slot = this.inventorySlots.get(fromSlot);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack current = slot.getStack();
			previous = current.copy();
			
			if (fromSlot < 6) {
				// From TE Inventory to Player Inventory
				if (!this.mergeItemStack(current, 6, 41, true))
					return null;
			} else {
				// From Player Inventory to TE Inventory
				if (!this.mergeItemStack(current, 0, 6, false))
					return null;
			}
			
			if (current.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
			
			if (current.stackSize == previous.stackSize)
				return null;
			slot.onPickupFromSlot(playerIn, current);
		}
		return previous;
	}
}
