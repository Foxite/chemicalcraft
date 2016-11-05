package nl.dirkkok.chemicalcraft.items;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.client.resources.I18n;

import java.util.List;

/* Metadata:
 * 0:  Empty
 * 1:  Fresh water
 * 2:  Pure water
 * 3:  NaCl
 * 4:  Nitric acid
 * 5:  Sulfuric acid
 * 6:  Mixture of nitric and sulfuric acid
 * 7:  Toluene
 * 8:  Nitrotoluene
 * 9:  Sodium bicarbonate
 * 10: Cleaned nitrotoluene
 * 11: Unfinished TNT
 * 12: Raw TNT
 * 13: Copper(II) nitrate
 * 14: Sodium nitrate
 * 15: Ammonia (stopped only)
 * 16: Salty water
 * 17: Mixture of ammonia and salty water
 * 18: CO2 (stopped only)
 * 19: Mixture of ammonia, salty water and CO2
 * 20: Elemental hydrogen (stopped only)
 * 21: Elemental nitrogen (stopped only)
 * 22: Elemental oxygen (stopped only)
 * 23: Benzene
 * 24: Ammonium chloride
 *
 * For stopped tubes, the first bit is 1. For unstopped tubes, the first bit is 0. Tubes with gas content do not have
 * unstopped contents registered.
 */
class TestTube extends BasicItem {
	TestTube() {
		super("test_tube");
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.test_tube";
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		/* Gas contents and thus only in stopped tubes:
		 * Ammonia
		 * CO2
		 * Elemental hydrogen
		 * Elemental nitrogen
		 * Elemental oxygen
		 */
		
		// Unstopped tubes
		subItems.add(new ItemStack(itemIn, 1,  0)); // Empty
		subItems.add(new ItemStack(itemIn, 1,  1)); // Water (l)
		subItems.add(new ItemStack(itemIn, 1,  2)); // Pure water (l)
		subItems.add(new ItemStack(itemIn, 1,  3)); // NaCl (s)
		subItems.add(new ItemStack(itemIn, 1,  4)); // Nitric acid (l)
		subItems.add(new ItemStack(itemIn, 1,  5)); // Sulfuric acid (l)
		subItems.add(new ItemStack(itemIn, 1,  6)); // Mixture of nitric and sulfuric acid (l)
		subItems.add(new ItemStack(itemIn, 1,  7)); // Toluene (l)
		subItems.add(new ItemStack(itemIn, 1,  8)); // Nitrotoluene (l
		subItems.add(new ItemStack(itemIn, 1,  9)); // Sodium bicarbonate (s)
		subItems.add(new ItemStack(itemIn, 1, 10)); // Cleaned nitrotoluene (l)
		subItems.add(new ItemStack(itemIn, 1, 11)); // Unfinished TNT (s)
		subItems.add(new ItemStack(itemIn, 1, 12)); // Raw TNT (s)
		subItems.add(new ItemStack(itemIn, 1, 13)); // Copper(II) nitrate (s)
		subItems.add(new ItemStack(itemIn, 1, 14)); // Sodium nitrate (s)
		subItems.add(new ItemStack(itemIn, 1, 16)); // Salty water (l)
		subItems.add(new ItemStack(itemIn, 1, 17)); // Mixture of ammonia and salty water (l)
		subItems.add(new ItemStack(itemIn, 1, 19)); // Mixture of ammonia, salty water and CO2 (l)
		subItems.add(new ItemStack(itemIn, 1, 23)); // Benzene (l)
		subItems.add(new ItemStack(itemIn, 1, 24)); // Ammonium chloride (s)
		
		// Stopped tubes
		subItems.add(new ItemStack(itemIn, 1, setBit(0, 30, true))); // Empty (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(1, 30, true))); // Water (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(2, 30, true))); // Pure water (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(3, 30, true))); // NaCl (s)
		subItems.add(new ItemStack(itemIn, 1, setBit(4, 30, true))); // Nitric acid (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(5, 30, true))); // Sulfuric acid (l) Technically solid below 10C, but whatever
		subItems.add(new ItemStack(itemIn, 1, setBit(6, 30, true))); // Mixture of nitric and sulfuric acid (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(7, 30, true))); // Toluene (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(8, 30, true))); // Nitrotoluene (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(9, 30, true))); // Sodium bicarbonate (s)
		subItems.add(new ItemStack(itemIn, 1, setBit(10, 30, true))); // Cleaned nitrotoluene (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(11, 30, true))); // Unfinished TNT (s)
		subItems.add(new ItemStack(itemIn, 1, setBit(12, 30, true))); // Raw TNT (s)
		subItems.add(new ItemStack(itemIn, 1, setBit(13, 30, true))); // Copper(II) nitrate (s)
		subItems.add(new ItemStack(itemIn, 1, setBit(14, 30, true))); // Sodium nitrate (s)
		subItems.add(new ItemStack(itemIn, 1, setBit(15, 30, true))); // Ammonia (g)
		subItems.add(new ItemStack(itemIn, 1, setBit(16, 30, true))); // Salty water (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(17, 30, true))); // Mixture of ammonia and salty water (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(18, 30, true))); // CO2 (g)
		subItems.add(new ItemStack(itemIn, 1, setBit(19, 30, true))); // Mixture of ammonia, salty water and CO2 (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(20, 30, true))); // Elemental hydrogen (g)
		subItems.add(new ItemStack(itemIn, 1, setBit(22, 30, true))); // Elemental nitrogen (g)
		subItems.add(new ItemStack(itemIn, 1, setBit(22, 30, true))); // Elemental oxygen (g)
		subItems.add(new ItemStack(itemIn, 1, setBit(23, 30, true))); // Benzene (l)
		subItems.add(new ItemStack(itemIn, 1, setBit(24, 30, true))); // Ammonium chloried (s)
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String type;
		switch (setBit(stack.getItemDamage(), 30, false)) {
			case 0: type = I18n.format("chem.empty"); break;
			case 1: type = I18n.format("chem.contwater"); break;
			case 2: type = I18n.format("chem.h2o"); break;
			case 3: type = I18n.format("chem.nacl"); break;
			case 4: type = I18n.format("chem.hno3"); break;
			case 5: type = I18n.format("chem.h2so4"); break;
			case 6: type = I18n.format("chem.nitsulacid"); break;
			case 7: type = I18n.format("chem.toluene"); break;
			case 8: type = I18n.format("chem.nitrotoluene"); break;
			case 9: type = I18n.format("chem.nahco3"); break;
			case 10: type = I18n.format("chem.cnitrotoluene"); break;
			case 11: type = I18n.format("chem.unftnt"); break;
			case 12: type = I18n.format("chem.rawtnt"); break;
			case 13: type = I18n.format("chem.cuno32"); break;
			case 14: type = I18n.format("chem.nano3"); break;
			case 15: type = I18n.format("chem.nh3"); break;
			case 16: type = I18n.format("chem.h2o_nacl"); break;
			case 17: type = I18n.format("chem.nh3_h2o_nacl"); break;
			case 18: type = I18n.format("chem.co2"); break;
			case 19: type = I18n.format("chem.nh3_h2o_nacl_co2"); break;
			case 20: type = I18n.format("chem.h2"); break;
			case 21: type = I18n.format("chem.n2"); break;
			case 22: type = I18n.format("chem.o2"); break;
			case 23: type = I18n.format("chem.benzene"); break;
			case 24: type = I18n.format("chem.nh4cl"); break;
			default: type = I18n.format("chem.empty"); break;
		}
		if (getBit(stack.getItemDamage(), 30) == 1) type = type + " (" + I18n.format("chem.stopped") + ")";
		tooltip.add(type);
		
		// Warnings
		String warn;
		switch (setBit(stack.getItemDamage(), 30, false)) {
			case 4: warn = I18n.format("chem.hcorrosive");  break; // Nitric acid
			case 5: warn = I18n.format("chem.hcorrosive");  break; // Sulfuric acid
			case 6: warn = I18n.format("chem.hcorrosive");  break; // Nitric + sulfuric acid
			case 7: warn = I18n.format("chem.hflammable");  break; // Toluene
			case 13: warn = I18n.format("chem.explosive");  break; // Raw TNT
			case 15: warn = I18n.format("chem.alkaline");   break; // Ammonia
			case 20: warn = I18n.format("chem.eflammable"); break; // Elemental hydrogen
			case 22: warn = I18n.format("chem.oxidizer");   break; // Elemental oxygen
			case 23: warn = I18n.format("chem.hflammable"); break; // Benzene
			case 24: warn = I18n.format("chem.toxic");      break; // Benzene
			default: warn = ""; break;
		}
		if (!warn.equals("")) tooltip.add(warn);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		RayTraceResult raytraceresult = this.rayTrace(world, player, true);
		
		if (raytraceresult == null) return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		
		// Fill tube with water
		if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
			// Only allow unstopped empty tubes
			if (stack.getItemDamage() != 0) return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			
			BlockPos blockpos = raytraceresult.getBlockPos(); // Position of block the player is looking at.
			IBlockState blockState = world.getBlockState(blockpos);
			Material material = blockState.getMaterial();
			if (material == Material.WATER) {
				stack.setItemDamage(1);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}
	
	// Convenience method.
	private int getBit(int num, int index) {
		return (num >> index) & 1;
	}
	
	private int setBit(int num, int index, boolean value) {
		if (value) {
			return num | (1 << index);
		} else {
			return num & ~(1 << index);
		}
	}
}
