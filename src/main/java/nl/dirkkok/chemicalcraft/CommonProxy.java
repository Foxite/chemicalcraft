package nl.dirkkok.chemicalcraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nl.dirkkok.chemicalcraft.blocks.ModBlocks;
import nl.dirkkok.chemicalcraft.gui.ModGuiHandler;
import nl.dirkkok.chemicalcraft.items.ModItems;
import nl.dirkkok.chemicalcraft.tileentity.ModTileEntities;
import nl.dirkkok.chemicalcraft.world.ModWorldGen;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		ModItems.createItems();
		ModBlocks.createBlocks();
		ModTileEntities.init();
	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(ChemicalCraft.instance, new ModGuiHandler());
		
		// Recipes
		GameRegistry.addRecipe(new ItemStack(ModItems.testTube, 3, 0), "# #", "# #", " # ", '#', Blocks.GLASS_PANE);
		GameRegistry.addRecipe(new ItemStack(ModBlocks.chemStandItem), "#U#", "###", '#', Items.IRON_INGOT,
				'U', ModItems.testTube);
		GameRegistry.addRecipe(new ItemStack(ModItems.residueTray, 3), "# #", "###", '#', Blocks.GLASS_PANE);
		
		// OreDictionary recipes
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.testTube, 1, 3), new ItemStack(ModItems.testTube, 1, 0),
				"dustSalt"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.testTube, 1, 3), new ItemStack(ModItems.testTube, 1, 0),
				"foodSalt"));
		
		// Generators
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);
		
	}

	public void postInit(FMLPostInitializationEvent e) {}
}
