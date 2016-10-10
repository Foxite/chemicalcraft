package nl.dirkkok.chemicalcraft.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModTileEntities {
	public static void init() {
		GameRegistry.registerTileEntity(ChemistryStandEntity.class, "chemistry_stand_entity");
	}
}
