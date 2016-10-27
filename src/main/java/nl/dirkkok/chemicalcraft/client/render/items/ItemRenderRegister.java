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
		reg(ModItems.testTube, 3, "test_tube_table_salt");
		reg(ModItems.testTube, 4, "test_tube_nitric_acid");
		reg(ModItems.testTube, 5, "test_tube_sulfuric_acid");
		reg(ModItems.testTube, 6, "test_tube_nitric+sulfuric_acid");
		reg(ModItems.testTube, 7, "test_tube_toluene");
		reg(ModItems.testTube, 8, "test_tube_nitrotoluene");
		reg(ModItems.testTube, 9, "test_tube_sodium_bicarbonate");
		reg(ModItems.testTube, 10, "test_tube_cleaned_nitrotoluene");
		reg(ModItems.testTube, 11, "test_tube_unfinished_tnt");
		reg(ModItems.testTube, 12, "test_tube_raw_tnt");
		reg(ModItems.testTube, 13, "test_tube_copper2_nitrate");
		reg(ModItems.testTube, 14, "test_tube_sodium_nitrate");
		reg(ModItems.testTube, 16, "test_tube_salty_water");
		reg(ModItems.testTube, 17, "test_tube_ammonia+salty_water");
		reg(ModItems.testTube, 19, "test_tube_ammonia+salty_water+co2");
		reg(ModItems.testTube, (1 << 30), "test_tube_stopped");
		reg(ModItems.testTube, 1 + (1 << 30), "test_tube_water_stopped");
		reg(ModItems.testTube, 2 + (1 << 30), "test_tube_purewater_stopped");
		reg(ModItems.testTube, 3 + (1 << 30), "test_tube_table_salt_stopped");
		reg(ModItems.testTube, 4 + (1 << 30), "test_tube_nitric_acid_stopped");
		reg(ModItems.testTube, 5 + (1 << 30), "test_tube_sulfuric_acid_stopped");
		reg(ModItems.testTube, 6 + (1 << 30), "test_tube_nitric+sulfuric_acid_stopped");
		reg(ModItems.testTube, 7 + (1 << 30), "test_tube_toluene_stopped");
		reg(ModItems.testTube, 8 + (1 << 30), "test_tube_nitrotoluene_stopped");
		reg(ModItems.testTube, 9 + (1 << 30), "test_tube_sodium_bicarbonate_stopped");
		reg(ModItems.testTube, 10 + (1 << 30), "test_tube_cleaned_nitrotoluene_stopped");
		reg(ModItems.testTube, 11 + (1 << 30), "test_tube_unfinished_tnt_stopped");
		reg(ModItems.testTube, 12 + (1 << 30), "test_tube_raw_tnt_stopped");
		reg(ModItems.testTube, 13 + (1 << 30), "test_tube_copper2_nitrate_stopped");
		reg(ModItems.testTube, 14 + (1 << 30), "test_tube_sodium_nitrate_stopped");
		reg(ModItems.testTube, 15 + (1 << 30), "test_tube_ammonia_stopped");
		reg(ModItems.testTube, 16 + (1 << 30), "test_tube_salty_water_stopped");
		reg(ModItems.testTube, 17 + (1 << 30), "test_tube_ammonia+salty_water_stopped");
		reg(ModItems.testTube, 18 + (1 << 30), "test_tube_co2_stopped");
		reg(ModItems.testTube, 19 + (1 << 30), "test_tube_ammonia+salty_water+co2_stopped");
		reg(ModItems.testTube, 20 + (1 << 30), "test_tube_hydrogen_stopped");
		reg(ModItems.testTube, 21 + (1 << 30), "test_tube_nitrogen_stopped");
		reg(ModItems.testTube, 22 + (1 << 30), "test_tube_oxygen_stopped");
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
