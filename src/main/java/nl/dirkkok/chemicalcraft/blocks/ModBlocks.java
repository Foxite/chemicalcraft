package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public final class ModBlocks {
	// Block blocks
	public static Block chemStand;
	public static Block saltpeterOre;
	public static Block corkWood;

	// Item blocks
	public static ItemBlock chemStandItem;
	public static ItemBlock saltpeterOreItem;
	public static ItemBlock corkWoodItem;
	
	public static void createBlocks() {
		GameRegistry.register(chemStand = new ChemistryStand());
		GameRegistry.register(chemStandItem = (ItemBlock) new ItemBlock(chemStand).setRegistryName("chemistry_stand"));
		
		GameRegistry.register(saltpeterOre = new SaltpeterOre());
		GameRegistry.register(saltpeterOreItem = (ItemBlock) new ItemBlock(saltpeterOre).setRegistryName("saltpeter_ore"));
		
		GameRegistry.register(corkWood = new CorkWood());
		GameRegistry.register(corkWoodItem = (ItemBlock) new ItemBlock(corkWood).setRegistryName("cork_wood"));
		
		// OreDictionary
		OreDictionary.registerOre("oreSaltpeter", saltpeterOre);
	}
}
