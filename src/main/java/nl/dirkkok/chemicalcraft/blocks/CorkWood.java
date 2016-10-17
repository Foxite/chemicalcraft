package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import nl.dirkkok.chemicalcraft.items.ModItems;

public class CorkWood extends BlockLog {
	public CorkWood() {
		super();
		this.setRegistryName("cork_wood");
		this.setUnlocalizedName("cork_wood");
		this.setHardness(1.5F);
		this.setCreativeTab(ModItems.chemTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		
		switch (meta) {
			case 0: iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
					break;
			case 4: iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
					break;
			case 8: iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
					break;
			default: iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
		}
		
		return iblockstate;
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		switch (state.getValue(LOG_AXIS)) {
			case X: return 4;
			case Y: return 0;
			case Z: return 8;
			default: return 12; // Also covers case NONE
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LOG_AXIS);
	}
}
