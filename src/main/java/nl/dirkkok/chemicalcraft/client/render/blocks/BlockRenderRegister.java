package nl.dirkkok.chemicalcraft.client.render.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import nl.dirkkok.chemicalcraft.ChemicalCraft;
import nl.dirkkok.chemicalcraft.blocks.ModBlocks;

public final class BlockRenderRegister {
	private static String modid = ChemicalCraft.MODID;

	public static void registerBlockRenderer() {
		reg(ModBlocks.chemStand);
		reg(ModBlocks.saltpeterOre);
		reg(ModBlocks.corkWood);
	}

	private static void reg(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(Item.getItemFromBlock(block), 0,
						new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
