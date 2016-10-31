package nl.dirkkok.chemicalcraft.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;
import nl.dirkkok.chemicalcraft.ChemicalCraft;

public final class ModTileEntities {
	public static void init() {
		GameRegistry.registerTileEntity(ChemistryStandEntity.class, "chemistry_stand_entity");
		
		if (ChemicalCraft.supportedModsLoaded.contains("ic2")) {
			GameRegistry.registerTileEntity(ElectrolysisStandEntity.class, "electrolysis_stand_entity");
		}
	}
}
