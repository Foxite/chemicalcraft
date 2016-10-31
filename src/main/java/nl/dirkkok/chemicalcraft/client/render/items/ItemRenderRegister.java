package nl.dirkkok.chemicalcraft.client.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import nl.dirkkok.chemicalcraft.ChemicalCraft;
import nl.dirkkok.chemicalcraft.items.ModItems;
import nl.dirkkok.chemicalcraft.items.TestTubeColor;

public final class ItemRenderRegister {
	public static void registerItemRenderer() {
		reg(ModItems.residueTray);
		reg(ModItems.tableSalt);
		reg(ModItems.saltpeterDust);
		reg(ModItems.rawCork);
		reg(ModItems.corkBar);
		reg(ModItems.corkStopper);
		regTestTubeType(0); // Empty
		regTestTubeType(1); // Fresh water
		regTestTubeType(2); // Pure water
		regTestTubeType(3); // NaCl
		regTestTubeType(4); // Nitric acid
		regTestTubeType(5); // Sulfuric acid
		regTestTubeType(6); // Mixture of nitric and sulfuric acid
		regTestTubeType(7); // Toluene
		regTestTubeType(8); // Nitrotoluene
		regTestTubeType(9); // Sodium bicarbonate
		regTestTubeType(10); // Cleaned nitrotoluene
		regTestTubeType(11); // Unfinished TNT
		regTestTubeType(12); // Raw TNT
		regTestTubeType(13); // Copper(II) nitrate
		regTestTubeType(14); // Sodium nitrate
		regTestTubeType(15, true); // Ammonia (stopped only)
		regTestTubeType(16); // Salty water
		regTestTubeType(17); // Mixture of ammonia and salty water
		regTestTubeType(18, true); // CO2 (stopped only)
		regTestTubeType(19); // Mixture of ammonia, salty water and CO2
		regTestTubeType(20, true); // Elemental hydrogen (stopped only)
		regTestTubeType(21, true); // Elemental nitrogen (stopped only)
		regTestTubeType(22, true); // Elemental oxygen (stopped only)
		regTestTubeType(23); // Benzene
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new TestTubeColor(), ModItems.testTube);
	}
	
	private static void regTestTubeType(int type) {
		regTestTubeType(type, false);
	}
	
	
	private static void regTestTubeType(int type, boolean gas) {
		if (!gas) {
			reg(ModItems.testTube, type, "test_tube");
		}
		reg(ModItems.testTube, type + (1 << 30), "test_tube_stopped");
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
