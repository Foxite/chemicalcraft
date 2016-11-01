package nl.dirkkok.chemicalcraft.client.render.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import nl.dirkkok.chemicalcraft.ChemicalCraft;
import nl.dirkkok.chemicalcraft.blocks.CorkLeaf;
import nl.dirkkok.chemicalcraft.blocks.ModBlocks;

public final class BlockRenderRegister {
	private static String modid = ChemicalCraft.MODID;

	public static void registerBlockRenderer() {
		reg(ModBlocks.chemStand);
		reg(ModBlocks.saltpeterOre);
		reg(ModBlocks.corkWood);
		reg(ModBlocks.corkSapling);
		reg(ModBlocks.corkLeaf);
		reg(ModBlocks.elecStand);
	}

	private static void reg(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(Item.getItemFromBlock(block), 0,
						new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
	
	public static void preInit() {
		((CorkLeaf) ModBlocks.corkLeaf).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		ModelLoader.setCustomStateMapper(ModBlocks.corkLeaf, (new StateMap.Builder())
				.ignore(new IProperty[] {CorkLeaf.CHECK_DECAY, CorkLeaf.DECAYABLE}).build());
	}
}
