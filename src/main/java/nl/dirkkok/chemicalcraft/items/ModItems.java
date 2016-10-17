package nl.dirkkok.chemicalcraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public final class ModItems {
	public static Item testTube;
	public static Item residueTray;
	public static Item tableSalt;
	public static Item saltpeterDust;
	
	public static final CreativeTabs chemTab = new CreativeTabs("chemicalCraft") {
		@Override
		public Item getTabIconItem() {
			return testTube;
		}
	};
	
	public static void createItems() {
		GameRegistry.register(testTube = new TestTube());
		GameRegistry.register(residueTray = new BasicItem("residue_tray"));
		GameRegistry.register(tableSalt = new BasicItem("table_salt"));
		GameRegistry.register(saltpeterDust = new BasicItem("saltpeter_dust"));
		
		// OreDictionary
		OreDictionary.registerOre("foodSalt", tableSalt);
		OreDictionary.registerOre("dustSalt", tableSalt);
		
		OreDictionary.registerOre("dustSaltpeter", saltpeterDust);
		
	}
}
