package nl.dirkkok.chemicalcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import nl.dirkkok.chemicalcraft.tileentity.ChemistryStandEntity;

public class ModGuiHandler implements IGuiHandler {
	public static final int CHEMISTRY_STAND_ENTITY_GUI = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == CHEMISTRY_STAND_ENTITY_GUI)
			return new ContainerChemistryStandEntity(player.inventory,
													(ChemistryStandEntity) world.getTileEntity(new BlockPos(x, y, z)));
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == CHEMISTRY_STAND_ENTITY_GUI)
			return new ContainerChemistryStandEntity(player.inventory,
					   (ChemistryStandEntity) world.getTileEntity(new BlockPos(x, y, z)));
		
		return null;
	}
}