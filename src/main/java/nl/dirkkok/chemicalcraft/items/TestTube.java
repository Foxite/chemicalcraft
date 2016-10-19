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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/* Metadata:
 * 0: Empty
 * 1: Fresh water
 * 2: Pure water
 * 3: NaCl
 *
 * For stopped tubes, the first bit is 1. For unstopped tubes, the first bit is 0. Tubes with gas content do not have
 * unstopped contents registered.
 */
class TestTube extends BasicItem {
	TestTube() {
		super("test_tube");
		this.setHasSubtypes(true);
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		String type;
		switch (setBit(stack.getItemDamage(), 30, false)) {
			case 0: type = ""; break;
			case 1: type = "_water"; break;
			case 2: type = "_purewater"; break;
			case 3: type = "_nacl"; break;
			default: type = ""; break;
		}
		
		if (getBit(stack.getItemDamage(), 30) == 1) type = type + "_stopped";
		
		return "item.test_tube" + type;
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		// Unstopped tubes (with supporting contents)
		subItems.add(new ItemStack(itemIn, 1, 0)); // Empty
		subItems.add(new ItemStack(itemIn, 1, 1)); // Water
		subItems.add(new ItemStack(itemIn, 1, 2)); // Pure water
		subItems.add(new ItemStack(itemIn, 1, 3)); // NaCl
		
		// Stopped tubes
		subItems.add(new ItemStack(itemIn, 1, setBit(0, 30, true))); // Empty
		subItems.add(new ItemStack(itemIn, 1, setBit(1, 30, true))); // Water
		subItems.add(new ItemStack(itemIn, 1, setBit(2, 30, true))); // Pure water
		subItems.add(new ItemStack(itemIn, 1, setBit(3, 30, true))); // NaCl
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String type;
		switch (setBit(stack.getItemDamage(), 30, false)) {
			case 0: type = "Empty"; break;
			case 1: type = "Fresh water with contaminants"; break;
			case 2: type = "Purified water"; break;
			case 3: type = "NaCl"; break;
			default: type = "Empty"; break;
		}
		if (getBit(stack.getItemDamage(), 30) == 1) type = type + " (stopped)";
		tooltip.add(type);
	}
	
	// Code "borrowed" from net.minecraft.item.ItemBucket.
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		RayTraceResult raytraceresult = this.rayTrace(world, player, true);
		
		// IDEA says that this statement is always false. That's bullshit, try removing this line and rightclicking into
		// open space.
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
	int getBit(int num, int index) {
		return (num >> index) & 1;
	}
	
	int setBit(int num, int index, boolean value) {
		if (value) {
			return num | (1 << index);
		} else {
			return num & ~(1 << index);
		}
	}
}
