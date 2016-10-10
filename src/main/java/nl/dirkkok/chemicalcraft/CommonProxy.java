package nl.dirkkok.chemicalcraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nl.dirkkok.chemicalcraft.blocks.ModBlocks;
import nl.dirkkok.chemicalcraft.gui.ModGuiHandler;
import nl.dirkkok.chemicalcraft.items.ModItems;
import nl.dirkkok.chemicalcraft.tileentity.ModTileEntities;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		ModItems.createItems();
		ModBlocks.createBlocks();
		ModTileEntities.init();
	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(ChemicalCraft.instance, new ModGuiHandler());
		
		GameRegistry.addRecipe(new ItemStack(ModItems.testTube, 3), "# #", "# #", " # ", '#', Blocks.GLASS_PANE);
		GameRegistry.addRecipe(new ItemStack(ModBlocks.chemStandItem), "#U#", "###", '#', Items.IRON_INGOT,
				'U', ModItems.testTube);
		GameRegistry.addRecipe(new ItemStack(ModItems.residueTray, 3), "# #", "###", '#', Blocks.GLASS_PANE);
	}

	public void postInit(FMLPostInitializationEvent e) {}
}
