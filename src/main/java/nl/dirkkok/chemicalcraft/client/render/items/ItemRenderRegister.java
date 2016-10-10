package nl.dirkkok.chemicalcraft.client.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import nl.dirkkok.chemicalcraft.ChemicalCraft;
import nl.dirkkok.chemicalcraft.items.ModItems;

public final class ItemRenderRegister {
	public static void registerItemRenderer() {
		reg(ModItems.testTube);
		reg(ModItems.residueTray);
	}

	private static void reg(Item item) {
		String modid = ChemicalCraft.MODID;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5),
						 "inventory"));
	}
}
