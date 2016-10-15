package nl.dirkkok.chemicalcraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/* Metadata:
 * 0: Empty
 * 1: Fresh water
 * 2: Pure water
 * 3: NaCl
 *
 */
public class TestTube extends BasicItem {
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
}
