package nl.dirkkok.chemicalcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ChemistryStandEntity extends TileEntity implements ITickable, IInventory {
	private static final Logger log = LogManager.getLogger();
	private boolean isActive;
	
	private enum Mode {
		HEAT(0), REACT(1), FILTER(2);
		
		private int value;
		Mode(int value) {
			this.value = value;
		}
		int getValue() { return value; }
	}
	
	/* The Chemistry Stand has 6 slots. 2 input slots, 2 output slots, a residue slot, and a fuel slot.
	 * [ firstInput, secondInput, firstOutput, secondOutput, residue, fuel ]
	 */
	private ItemStack[] inventory;
	private Mode mode;
	private String customName;
	private int operationTime; // In ticks
	private int fuelTime; // In ticks; value does not matter if unused and will only be reset when mode changes to HEAT
	private int maxFuelTime; // Used to determine % of fuel left; remains active even if mode is not HEAT
	
	
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
		
		nbt.setInteger("operationTime", this.operationTime);
		nbt.setInteger("fuelTime", this.fuelTime);
		nbt.setInteger("maxFuelTime", this.maxFuelTime);
		
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
			case 0: mode = Mode.HEAT;
					break;
			case 1: mode = Mode.REACT;
					break;
			case 2: mode = Mode.FILTER;
					break;
			default: mode = Mode.REACT;
					 log.error("Invalid chemistry stand mode!" +
							   "(mode = " + nbt.getInteger("mode") + ") falling back to react.");
		}
		
		this.operationTime = nbt.getInteger("operationTime");
		this.fuelTime = nbt.getInteger("fuelTime");
		this.maxFuelTime = nbt.getInteger("maxFuelTime");
		
	}
	
	@Override
	public void update() {
		// Fuel checks
		if (fuelTime > 0) {
			fuelTime--;
			if (mode == Mode.HEAT && canDoOperation()) {
				operationTime++;
			}
		} else if (maxFuelTime != 0) { // Only gets checked if fuelTime == 0
			maxFuelTime = 0;
			if (mode == Mode.HEAT) { // Out of fuel for process
				operationTime = 0; // Cancel process
			}
		}
		
		// Operation checks
		if (canDoOperation() && operationTime < 200) { // Operations take 200 ticks, or 10 seconds.
			operationTime++; // TODO when the stand is active, add bubble particles
		} else { // Operation is finished.
			operationTime = 0;
			doOperation(); // Redo operation
		}
		
		// Reload fuel
		if (fuelTime == 0 && inventory[5] != null && maxFuelTime == 0) {
			if (getFuelTimeOfItem(inventory[5].getItem()) > 0) {
				fuelTime = getFuelTimeOfItem(inventory[5].getItem());
				maxFuelTime = getFuelTimeOfItem(inventory[5].getItem());
				this.decrStackSize(5, 1);
			}
		}
	}
	
	private boolean canDoOperation() {
		// TODO add more recipes
		// REACT recipes
		
		// HEAT recipes
		
		// FILTER recipes
		
		
		return false;
	}
	
	private void doOperation() {
		if (canDoOperation()) {
			isActive = true;
			// TODO
		} else {
			isActive = false;
		}
	}
	
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
		return this.hasCustomName() ? this.customName : "container.chemistry_stand";
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
	
	public boolean isUsableByPlayer(EntityPlayer p) { // I insist on using usable.
		return isUseableByPlayer(p);
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
		return 6;
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
		log.error("isItemValidForSlot()");
		// Only allow test tubes (with any contents) in slots 0 and 1
		if (index == 0 || index == 1 || index == 2 || index == 4) {
			if (stack.getItem().getUnlocalizedName().equals("test_tube")) {
				log.error("true");
				return true;
			}
			// Only allow residue trays in slot 4
		} else if (index == 3) {
			if (stack.getItem().getUnlocalizedName().equals("residue_tray")) {
				log.error("true");
				return true;
			}
			// Only allow fuel items in slot 5
		} else if (index == 6) {
			if (GameRegistry.getFuelValue(stack) > 0) {
				log.error("true");
				return true;
			}
		}
		// Do not allow insertion into slots 2 and 3
		log.error("false");
		return false;
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
	
	public void setMode(int mode) {
		switch (mode) {
			case 0: this.mode = Mode.REACT;
					break;
			case 1: this.mode = Mode.HEAT;
					break;
			case 2: this.mode = Mode.FILTER;
					break;
			default: throw new RuntimeException("Invalid chemistry stand mode! (mode=" + mode + ")");
		}
		
		log.error("Mode is now " + mode);
	}
	
	public int getFuelTime() {
		return fuelTime;
	}
	
	public int getMaxFuelTime() {
		return maxFuelTime;
	}
	
	public int getFuelTimeOfItem(Item item) {
		if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
		if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
		if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
		if (item == Items.STICK) return 100;
		if (item == Items.COAL) return 1600;
		if (item == Items.LAVA_BUCKET) return 20000;
		if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
		if (item == Items.BLAZE_ROD) return 2400;
		return GameRegistry.getFuelValue(inventory[5]);
	}
	
	public int getOperationTime() {
		return operationTime;
	}
	
	public boolean isActive() { return isActive; };
}
