package nl.dirkkok.chemicalcraft.client.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import nl.dirkkok.chemicalcraft.ChemicalCraft;
import nl.dirkkok.chemicalcraft.items.ModItems;

public final class ItemRenderRegister {
	public static void registerItemRenderer() {
		reg(ModItems.residueTray);
		reg(ModItems.tableSalt);
		reg(ModItems.saltpeterDust);
		reg(ModItems.rawCork);
		reg(ModItems.corkBar);
		reg(ModItems.corkStopper);
		reg(ModItems.testTube, 0, "test_tube");
		reg(ModItems.testTube, 1, "test_tube_water");
		reg(ModItems.testTube, 2, "test_tube_purewater");
		reg(ModItems.testTube, 3, "test_tube_nacl");
		reg(ModItems.testTube, (1 << 30), "test_tube_stopped");
		reg(ModItems.testTube, 1 + (1 << 30), "test_tube_water_stopped");
		reg(ModItems.testTube, 2 + (1 << 30), "test_tube_purewater_stopped");
		reg(ModItems.testTube, 3 + (1 << 30), "test_tube_nacl_stopped");
	}
	
	private static void reg(Item item) {
		String modid = ChemicalCraft.MODID;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
				new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
	
	private static void reg(Item item, int meta, String file) {
		String modid = ChemicalCraft.MODID;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta,
				new ModelResourceLocation(modid + ":" + file, "inventory"));
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(modid + ":" + file, "inventory"));
	}
}
