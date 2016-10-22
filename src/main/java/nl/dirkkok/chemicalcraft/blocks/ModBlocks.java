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
	public static Block corkSapling;
	public static Block corkLeaf;

	// Item blocks
	public static ItemBlock chemStandItem;
	public static ItemBlock saltpeterOreItem;
	public static ItemBlock corkWoodItem;
	public static ItemBlock corkSaplingItem;
	public static ItemBlock corkLeafItem;
	
	public static void createBlocks() {
		GameRegistry.register(chemStand = new ChemistryStand());
		GameRegistry.register(chemStandItem = (ItemBlock) new ItemBlock(chemStand).setRegistryName("chemistry_stand"));
		
		GameRegistry.register(saltpeterOre = new SaltpeterOre());
		GameRegistry.register(saltpeterOreItem = (ItemBlock) new ItemBlock(saltpeterOre).setRegistryName("saltpeter_ore"));
		
		GameRegistry.register(corkWood = new CorkWood());
		GameRegistry.register(corkWoodItem = (ItemBlock) new ItemBlock(corkWood).setRegistryName("cork_wood"));
		
		GameRegistry.register(corkSapling = new CorkSapling());
		GameRegistry.register(corkSaplingItem = (ItemBlock) new ItemBlock(corkSapling).setRegistryName("cork_sapling"));
		
		GameRegistry.register(corkLeaf = new CorkLeaf());
		GameRegistry.register(corkLeafItem = (ItemBlock) new ItemBlock(corkLeaf).setRegistryName("cork_leaf"));
		
		
		// OreDictionary
		OreDictionary.registerOre("oreSaltpeter", saltpeterOre);
	}
}
