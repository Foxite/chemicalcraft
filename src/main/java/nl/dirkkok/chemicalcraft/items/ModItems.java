package nl.dirkkok.chemicalcraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
	public static Item testTube;
	public static Item residueTray;
	
	public static final CreativeTabs chemTab = new CreativeTabs("chemicalCraft") {
		@Override
		public Item getTabIconItem() {
			return testTube;
		}
	};

	public static void createItems() {
		GameRegistry.register(testTube = new TestTube());
		GameRegistry.register(residueTray = new BasicItem("residue_tray"));
	}
}
