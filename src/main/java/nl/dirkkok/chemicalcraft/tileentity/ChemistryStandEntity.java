package nl.dirkkok.chemicalcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ChemistryStandEntity extends TileEntity implements ITickable, IInventory {
	private static final Logger log = LogManager.getLogger();
	
	private enum Mode {
		REACT(0), HEAT(1), FILTER(2);
		
		private int value;
		Mode(int value) {
			this.value = value;
		}
		int getValue() { return value; }
	}
	
	/* The Chemistry Stand has 5 slots. 2 input slots, an output slot, a residue slot, and a tube storage slot.
	 * [ firstInput, secondInput, output, residue, tubeStorage ]
	 */
	private ItemStack[] inventory;
	private Mode mode;
	private String customName;
	
	public ChemistryStandEntity() {
		this.inventory = new ItemStack[this.getSizeInventory()];
		this.mode = Mode.REACT;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			ItemStack stack = this.getStackInSlot(i);
			if (stack != null) {
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("slot", (byte) i);
				stack.writeToNBT(stackTag);
				list.appendTag(stackTag);
			}
		}
		nbt.setTag("items", list);
		
		if (this.hasCustomName()) {
			nbt.setString("customName", this.getCustomName());
		}
		
		nbt.setInteger("mode", mode.getValue());
		
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		NBTTagList list = nbt.getTagList("items", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound stackTag = list.getCompoundTagAt(i);
			int slot = stackTag.getByte("slot") & 255;
			this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
		}
		
		if (nbt.hasKey("customName", 8)) {
			this.setCustomName(nbt.getString("customName"));
		}
		
		switch (nbt.getInteger("mode")) {
			case 0: mode = Mode.REACT;
					break;
			case 1: mode = Mode.HEAT;
					break;
			case 2: mode = Mode.FILTER;
					break;
			default: mode = Mode.REACT;
					 log.error("Invalid chemistry stand mode!" +
							   "(mode = " + nbt.getInteger("mode") + ") falling back to react.");
		}
		
	}
	
	@Override
	public void update() {}
	
	private String getCustomName() {
		return customName;
	}
	
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	
	@Override @Nullable
	public ItemStack getStackInSlot(int index) {
		if (index < 0 || index >= this.getSizeInventory())
			return null;
		return this.inventory[index];
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = this.getStackInSlot(index);
		
		if (stack != null) {
			if (stack.stackSize <= count) {
				//stack = this.getStackInSlot(index);
				this.setInventorySlotContents(index, null);
				this.markDirty();
				return stack;
			} else {
				stack = stack.splitStack(count);
				
				if (stack.stackSize <= 0) {
					this.setInventorySlotContents(index, null);
				} else {
					//Just to show that changes happened
					this.setInventorySlotContents(index, this.getStackInSlot(index));
				}
				
				this.markDirty();
				return stack;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.getStackInSlot(index);
		this.setInventorySlotContents(index, null);
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory())
			return;
		
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		
		if (stack != null && stack.stackSize == 0)
			stack = null;
		
		this.inventory[index] = stack;
		this.markDirty();
	}
	
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.tutorial_tile_entity";
	}
	
	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.equals("");
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this
				.getName());
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}
	
	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public int getSizeInventory() {
		return 5;
	}
	
	@Override
	public int getField(int id) {
		return 0;
	}
	
	@Override
	public void setField(int id, int value) {
	}
	
	@Override
	public int getFieldCount() {
		return 0;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
		/*
		// Only allow test tubes (with any contents) in slots 0,1,2,4, and residue trays in slot 3.
		if (index == 0 || index == 1 || index == 2 || index == 4) {
			if (stack.getItem().getUnlocalizedName().equals("test_tube")) {
				return true;
			}
		} else if (index == 3 ) {
			if (stack.getItem().getUnlocalizedName().equals("residue_tray")) {
				return true;
			}
		}
		return false;*/
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < this.getSizeInventory(); i++)
			this.setInventorySlotContents(i, null);
	}
	
	public int getMode() {
		switch (mode) {
			case REACT:  return 0;
			case HEAT:   return 1;
			case FILTER: return 2;
			default: return 0; // There is no way this can happen, but Java insists that I add this line.
		}
	}
	
	public int setMode(int mode) {
		switch (mode) {
			case 0: this.mode = Mode.REACT;
			case 1: this.mode = Mode.HEAT;
			case 2: this.mode = Mode.FILTER;
			default: throw new RuntimeException("Invalid chemistry stand mode! (mode=" + mode + ")");
		}
	}
	
}
