package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks {
	// Block blocks
	public static Block testBlock;
	public static Block chemStand;

	// Item blocks
	public static ItemBlock testBlockItem;
	public static ItemBlock chemStandItem;

	public static void createBlocks() {
		GameRegistry.register(testBlock = new BasicBlock("test_block"));
		GameRegistry.register(testBlockItem = (ItemBlock) new ItemBlock(testBlock).setRegistryName("test_block"));
		
		GameRegistry.register(chemStand = new ChemistryStand());
		GameRegistry.register(chemStandItem = (ItemBlock) new ItemBlock(chemStand).setRegistryName("chemistry_stand"));
		
	}
}
