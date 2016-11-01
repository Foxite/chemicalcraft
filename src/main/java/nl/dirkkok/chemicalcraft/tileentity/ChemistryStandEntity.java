package nl.dirkkok.chemicalcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nl.dirkkok.chemicalcraft.ChemicalCraft;
import nl.dirkkok.chemicalcraft.items.ModItems;

import javax.annotation.Nullable;

public class ChemistryStandEntity extends TileEntity implements ITickable, IInventory {
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
	private boolean recipeWillCauseExplosion;
	
	
	public ChemistryStandEntity() {
		this.inventory = new ItemStack[this.getSizeInventory()];
		this.mode = Mode.HEAT;
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
					 ChemicalCraft.LOG.error("Invalid chemistry stand mode!" +
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
		} else if (maxFuelTime != 0) { // Only gets checked if fuelTime == 0
			maxFuelTime = 0;
		}
		
		// Reload fuel
		if (fuelTime == 0 && inventory[5] != null) {
			if (getFuelTimeOfItem(inventory[5].getItem()) > 0) {
				fuelTime = getFuelTimeOfItem(inventory[5].getItem());
				maxFuelTime = getFuelTimeOfItem(inventory[5].getItem());
				this.decrStackSize(5, 1);
			}
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
		recipeWillCauseExplosion = false;
		if (inventory[0] == null) return false;
		
		// HEAT recipes
		if (mode == Mode.HEAT) {
			if (inventory[1] != null) return false;
			if (fuelTime == 0) return false;
			
			// Water -> H2O + NaCl (TODO residue)
			if (getTubeMetadata(0) == 1) {
				if ((inventory[2] == null || getTubeMetadata(2) == 2)
				 && (inventory[3] == null || inventory[3].getItem() == ModItems.tableSalt)) {
					return true;
				}
			}
			
			// Unfinished TNT
			// It might seem counterintuitive to heat TNT to make it explosive, but wikipedia says it works.
			if (getTubeMetadata(0) == 11) {
				if (inventory[3] == null || getTubeMetadata(2) == 12) {
					return true;
				}
			}
			
			// Raw TNT -> Boom
			if (inventory[0].getItem() == ModItems.testTube && inventory[0].getItemDamage() == 12) {
				recipeWillCauseExplosion = true;
				return true;
			}
			
			// Gunpowder -> Boom
			if (inventory[0].getItem() == Items.GUNPOWDER) {
				recipeWillCauseExplosion = true;
				return true;
			}
			
		}
		
		// REACT recipes
		if (mode == Mode.REACT) {
			if (inventory[1] == null) return false;
			
			// Nitric Acid + Sulfuric Acid mix
			if (getTubeMetadata(0) == 4 && getTubeMetadata(1) == 5) {
				if (inventory[2] == null || getTubeMetadata(2) == 6) {
					return true;
				}
			}
			
			// Nitric+sulfur acid + Toluene -> Nitrotoluene
			if (getTubeMetadata(0) == 6 && getTubeMetadata(1) == 7) {
				if (inventory[2] == null || getTubeMetadata(2) == 8) {
					return true;
				}
			}
			
			// Nitrotoluene + Sodium Bicarbonate -> Cleaned Nitrotoluene
			if (getTubeMetadata(0) == 8 && getTubeMetadata(1) == 9) {
				if (inventory[2] == null || getTubeMetadata(2) == 10) {
					return true;
				}
			}
			
			// Cleaned NT + Nitric+sulfur acid -> Unfinished TNT
			if (getTubeMetadata(0) == 10 && getTubeMetadata(1) == 6) {
				if (inventory[2] == null || getTubeMetadata(2) == 11) {
					return true;
				}
			}
			
			// Pure water + Salt -> Salty water
			if (getTubeMetadata(0) == 2 && getTubeMetadata(1) == 3) {
				if (inventory[2] == null || getTubeMetadata(2) == 16) {
					return true;
				}
			}
			
			// Salty water + ammonia mix
			if (getTubeMetadata(0) == 16 && getTubeMetadata(1) == 15) {
				if (inventory[2] == null || getTubeMetadata(2) == 17) {
					return true;
				}
			}
			
			// Saltywater+ammonia + CO2 -> Sodium bicarbonate + ammonium chloride
			if (getTubeMetadata(0) == 16 && getTubeMetadata(1) == 15) {
				if ((getTubeMetadata(2) == 17 || inventory[2] == null)
				 && (inventory[2] == null || getTubeMetadata(3) == 24)) {
					return true;
				}
			}
		}
		
		// FILTER recipes
		if (mode == Mode.FILTER) {
			if (inventory[1] != null) return false;
			
			// Crossmod recipes
			if (ChemicalCraft.supportedModsLoaded.contains("railcraft")) {
				// (Railcraft) Creosote Oil -> Toluene (TODO residue)
				if (inventory[0].getItem() == Items.BUCKET && (FluidRegistry.getFluidStack("creosote", 1000))
						.isFluidEqual(inventory[0])) {
					if (inventory[3] == null || getTubeMetadata(2) == 7) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/* This removes the input items and adds to the output slots.
	 */
	private void doOperation() {
		// We don't have to check canDoOperation(), because all calls to this method will have done that beforehand.
		// This means that we can skip a lot of checks in this method.
		
		// HEAT recipes
		if (mode == Mode.HEAT) {
			// Water -> H2O + NaCl (TODO residue)
			if (getTubeMetadata(0) == 1) {
				// Remove items from input
				decrStackSize(0, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 2));
				} else {
					decrStackSize(2, -1); // Increase stack size by 1. I have checked everything, it will work.
				}
				if (inventory[3] == null) {
					setInventorySlotContents(3, new ItemStack(ModItems.testTube, 1, 3));
				} else {
					decrStackSize(3, -1);
				}
				
			}
			
			// Unfinished TNT -> Raw TNT
			else if (getTubeMetadata(0) == 11) {
				decrStackSize(0, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 12));
				} else {
					decrStackSize(2, -1);
				}
			}
			
			else if (inventory[0].getItem() == ModItems.testTube && inventory[0].getItemDamage() == 12) {
				if (!this.worldObj.isRemote) {
					worldObj.createExplosion(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 3.0F, true);
				}
			}
			
			else if (inventory[0].getItem() == Items.GUNPOWDER) {
				if (!this.worldObj.isRemote) {
					worldObj.createExplosion(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 3.0F, true);
				}
			}
		}
		
		// REACT recipes
		else if (mode == Mode.REACT) {
			// Nitric Acid + Sulfuric Acid mix
			if (getTubeMetadata(0) == 4 && getTubeMetadata(1) == 5) {
				decrStackSize(0, 1);
				decrStackSize(1, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 6));
				} else {
					decrStackSize(2, -1);
				}
			}
			
			// Nitric+sulfur acid + Toluene -> Nitrotoluene
			else if (getTubeMetadata(0) == 6 && getTubeMetadata(1) == 7) {
				decrStackSize(0, 1);
				decrStackSize(1, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 8));
				} else {
					decrStackSize(2, -1);
				}
			}
			
			// Nitrotoluene + Sodium Bicarbonate -> Cleaned Nitrotoluene
			else if (getTubeMetadata(0) == 8 && getTubeMetadata(1) == 9) {
				decrStackSize(0, 1);
				decrStackSize(1, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 10));
				} else {
					decrStackSize(2, -1);
				}
			}
			
			// Cleaned NT + Nitric+sulfur acid -> Unfinished TNT
			else if (getTubeMetadata(0) == 10 && getTubeMetadata(1) == 6) {
				decrStackSize(0, 1);
				decrStackSize(1, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 11));
				} else {
					decrStackSize(2, -1);
				}
			}
			
			// Pure water + Salt -> Salty water
			else if (getTubeMetadata(0) == 2 && getTubeMetadata(1) == 3) {
				decrStackSize(0, 1);
				decrStackSize(1, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 16));
				} else {
					decrStackSize(2, -1);
				}
			}
			
			// Salty water + ammonia mix
			else if (getTubeMetadata(0) == 16 && getTubeMetadata(1) == 15) {
				decrStackSize(0, 1);
				decrStackSize(1, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 17));
				} else {
					decrStackSize(2, -1);
				}
			}
			
			// Saltywater+ammonia + CO2 -> Sodium bicarbonate + ammonium chloride
			else if (getTubeMetadata(0) == 17 && getTubeMetadata(1) == 18) {
				decrStackSize(0, 1);
				decrStackSize(1, 1);
				
				if (inventory[2] == null) {
					setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 24));
				} else {
					decrStackSize(2, -1);
				}
			}
		}
		
		// FILTER recipes
		else if (mode == Mode.FILTER) {
			// Crossmod recipes
			if (ChemicalCraft.supportedModsLoaded.contains("railcraft")) {
				// (Railcraft) Creosote Oil -> Toluene (TODO residue)
				if (inventory[0].getItem() == Items.BUCKET && (FluidRegistry.getFluidStack("creosote", 1000))
						.isFluidEqual(inventory[0])) {
					// Remove items from input
					decrStackSize(0, 1);
					
					if (inventory[2] == null) {
						setInventorySlotContents(2, new ItemStack(ModItems.testTube, 1, 7));
					} else if (getTubeMetadata(2) == 7) {
						decrStackSize(2, -1); // Increase stack size by 1. I have checked everything, it will work.
					}
				}
			}
		}
	}
	
	/* Returns the metadata of the item in inventory[slot] if it's a test tube.
	 * If it's not a test tube, this returns -1.
	 */
	private int getTubeMetadata(int slot) {
		if (inventory[slot].getItem() == ModItems.testTube) {
			return inventory[slot].getItemDamage() & ~(1 << 30);
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
	
	public int getMode() {
		return mode.getValue();
	}
	
	public void setMode(int mode) {
		switch (mode) {
			case 0: this.mode = Mode.HEAT;
					break;
			case 1: this.mode = Mode.REACT;
					break;
			case 2: this.mode = Mode.FILTER;
					break;
			default: throw new IllegalArgumentException("Invalid chemistry stand mode! (mode=" + mode + ")");
		}
	}
	
	public int getFuelTime() {
		return fuelTime;
	}
	
	public int getMaxFuelTime() {
		return maxFuelTime;
	}
	
	private int getFuelTimeOfItem(Item item) {
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
	
	public boolean willRecipeCauseExplosion() { return recipeWillCauseExplosion; }
	
}
