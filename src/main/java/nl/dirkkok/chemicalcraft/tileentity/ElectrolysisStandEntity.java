package nl.dirkkok.chemicalcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nl.dirkkok.chemicalcraft.items.ModItems;

import javax.annotation.Nullable;

/* This class is only used if IndustrialCraft2 is loaded. */
public class ElectrolysisStandEntity extends TileEntity implements ITickable, IInventory {
	
	/* The Electrolysis stand has four slots: input, two outputs, and a battery. It also has an EU power storage slot.
	 * [ input, firstOutput, secondOutput, battery ]
	 */
	private ItemStack[] inventory;
	private String customName;
	private int power; // EUs
	private int operationTime;
	
	public ElectrolysisStandEntity() {
		this.inventory = new ItemStack[this.getSizeInventory()];
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
		
		nbt.setInteger("operationTime", this.operationTime);
		
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
		
		this.operationTime = nbt.getInteger("operationTime");
		
	}
	
	@Override
	public void update() {
		// TODO check inventory[3] for batteries
		
		if (power == 0) {
			operationTime = 0;
		} else {
			power--;
		}
		
		// Operation checks
		// Operations take 200 ticks, or 10 seconds.
		if (canDoOperation() && operationTime < 200) { // Operation is in progress or is ready to start.
			operationTime++; // TODO when the stand is active, add bubble particles
		} else if (canDoOperation() && operationTime == 200) { // Operation is finished.
			operationTime = 0;
			doOperation();
		} else if (!canDoOperation() && operationTime != 0 && operationTime < 200) { // Items were removed prematurely.
			operationTime = 0;
		}
		
	}
	
	private boolean canDoOperation() {
		if (inventory[0] == null) return false;
		
		// Pure water -> Elemental hydrogen + Elemental oxygen
		if (getTubeMetadata(0) == 2) {
			if ((getTubeMetadata(1) == 20 || inventory[1] == null)
			 && (getTubeMetadata(2) == 22 || inventory[2] == null)) {
				return true;
			}
		}
		
		return false;
	}
	
	/* This removes the input items and adds to the output slots.
	 */
	private void doOperation() {
		// We don't have to check canDoOperation(), because all calls to this method will have done that beforehand.
		// This means that we can skip a lot of checks in this method.
		
		// Pure water -> Elemental hydrogen + Elemental oxygen
		if (getTubeMetadata(0) == 2) {
			decrStackSize(0, 1);
			
			if (getTubeMetadata(1) == 20) {
				decrStackSize(1, -1);
			} else {
				setInventorySlotContents(1, new ItemStack(ModItems.testTube, 1, 20));
			}
			if (getTubeMetadata(1) == 22) {
				decrStackSize(1, -1);
			} else {
				setInventorySlotContents(1, new ItemStack(ModItems.testTube, 1, 22));
			}
		}
	}
	
	/* Returns the metadata of the item in inventory[slot] if it's a test tube.
	 * If it's not a test tube, this returns -1.
	 */
	private int getTubeMetadata(int slot) {
		if (inventory[slot].getItem() == ModItems.testTube) {
			return inventory[slot].getItemDamage();
		}
		return -1;
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
				this.setInventorySlotContents(index, null);
				this.markDirty();
				return stack;
			} else {
				stack = stack.splitStack(count);
				
				if (stack.stackSize <= 0) {
					this.setInventorySlotContents(index, null);
				} else {
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
		return this.hasCustomName() ? this.customName : "container.electrolysis_stand";
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
		// Only allow test tubes (with any contents) in slots 0 and 1
		if (index == 0 || index == 1 || index == 2 || index == 4) {
			if (stack.getItem().getUnlocalizedName().equals("test_tube")) {
				return true;
			}
			// Only allow residue trays in slot 4
		} else if (index == 3) {
			if (stack.getItem().getUnlocalizedName().equals("residue_tray")) {
				return true;
			}
			// Only allow fuel items in slot 5
		} else if (index == 6) {
			if (GameRegistry.getFuelValue(stack) > 0) {
				return true;
			}
		}
		// Do not allow insertion into slots 2 and 3
		return false;
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < this.getSizeInventory(); i++)
			this.setInventorySlotContents(i, null);
	}
	
	public int getOperationTime() {
		return operationTime;
	}
}
