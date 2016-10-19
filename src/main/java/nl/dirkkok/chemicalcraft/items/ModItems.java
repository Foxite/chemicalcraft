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
	public static Item rawCork;
	public static Item corkBar;
	public static Item corkStopper;
	
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
		GameRegistry.register(rawCork = new BasicItem("raw_cork"));
		GameRegistry.register(corkBar = new BasicItem("cork_bar"));
		GameRegistry.register(corkStopper = new BasicItem("cork_stopper"));
		
		// OreDictionary
		OreDictionary.registerOre("foodSalt", tableSalt);
		OreDictionary.registerOre("dustSalt", tableSalt);
		OreDictionary.registerOre("dustSaltpeter", saltpeterDust);
		OreDictionary.registerOre("itemRawCork", rawCork);
		OreDictionary.registerOre("itemCork", corkBar);
		
		
	}
}
