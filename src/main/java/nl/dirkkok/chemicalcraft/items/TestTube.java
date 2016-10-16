package nl.dirkkok.chemicalcraft.items;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
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
 */
public class TestTube extends BasicItem {
	private static final Logger log = LogManager.getLogger(); // TODO for debugging only, remove
	
	public TestTube() {
		super("test_tube");
		this.setHasSubtypes(true);
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		String type;
		switch (stack.getItemDamage()) {
			case 0: type = ""; break;
			case 1: type = "_water"; break;
			case 2: type = "_purewater"; break;
			case 3: type = "_nacl"; break;
			default: type = ""; break;
		}
		return "item.test_tube" + type;
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
		subItems.add(new ItemStack(itemIn, 1, 2));
		subItems.add(new ItemStack(itemIn, 1, 3));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String type;
		switch (stack.getItemDamage()) {
			case 0: type = "Empty"; break;
			case 1: type = "Fresh water with contaminants"; break;
			case 2: type = "Purified water"; break;
			case 3: type = "NaCl"; break;
			default: type = "Empty"; break;
		}
		tooltip.add(type);
	}
	
	// Code "borrowed" from net.minecraft.item.ItemBucket.
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (stack.getItemDamage() != 0) return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		
		RayTraceResult raytraceresult = this.rayTrace(world, player, true);
		
		if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
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
}
