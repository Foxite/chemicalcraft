package nl.dirkkok.chemicalcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import nl.dirkkok.chemicalcraft.tileentity.ChemistryStandEntity;

public class ContainerChemistryStandEntity extends Container {
	private ChemistryStandEntity te;
	private int operationTime; // In ticks
	private int fuelTime; // In ticks; value does not matter if unused and will only be reset when mode changes to HEAT
	private int maxFuelTime; // Used to determine % of fuel left; remains active even if mode is not HEAT
	
	/* Initializes the container for the GUI and adds slots to it.
	 *
	 * GUI structure:
	 * Progress bar is centered. The rest of the gui is build around it. Elements are spaced by 5 pixels.
	 * progress bar: x = 8 + 16 * 4 = 72, y = 17 + 16 = 33; (72, 33)
	 * second input slot: x = 72 - 21 = 51 (51, 33)
	 * first input slot: x = 51 - 21 = 30 (30, 33)
	 * first output slot: x = 72 + 21 = 93 (92, 33)
	 * second output slot: x = 93 + 21 = 114 (114, 33)
	 * residue slot: below first output slot, y = 33 + 21 = 54 (92, 54)
	 * fuel meter: below progress bar, y = 54 (72, 54)
	 * fuel slot: left of fuel meter and below second input (51, 54)
	 * mode button: above progress bar, y = 33 - 21 = 12 (72, 12)
	 *
	 * Slot IDs:      Slots  IDs
	 * Tile entity:   0-5
	 * Player craft:  0-4
	 * Player armor:  5-8
	 * Player inv:    9-35
	 * Player hotbar: 36-44
	 * Offhand slot:  45
	 */
	public ContainerChemistryStandEntity(IInventory inv, ChemistryStandEntity te) {
		this.te = te;
		
		// Add slots for the tile entity, slots 0-5
		this.addSlotToContainer(new Slot(te, 0, 30,  33)); // First input
		this.addSlotToContainer(new Slot(te, 1, 51,  33)); // Second input
		this.addSlotToContainer(new Slot(te, 2, 92,  33)); // First output
		this.addSlotToContainer(new Slot(te, 3, 114, 33)); // Second output
		this.addSlotToContainer(new Slot(te, 4, 92,  54)); // Residue
		this.addSlotToContainer(new Slot(te, 5, 51,  54)); // Fuel
		
		// Add slots for player inventory
		// Main inventory, slots 9 to 35, IDs 6-32
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(inv, x + y * 9 + 6, 8 + x * 18, 84 + y * 18));
			}
		}
		
		// Hotbar, slots 0-8, IDs 33-41
		for (int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(inv, x + 33, 8 + x * 18, 142));
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
