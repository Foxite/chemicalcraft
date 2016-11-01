package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.dirkkok.chemicalcraft.ChemicalCraft;
import nl.dirkkok.chemicalcraft.gui.ModGuiHandler;
import nl.dirkkok.chemicalcraft.tileentity.ElectrolysisStandEntity;

import javax.annotation.Nullable;

/* This class is only used if IndustrialCraft2 is loaded. */
public class ElectrolysisStand extends BasicBlock implements ITileEntityProvider {
	public ElectrolysisStand() {
		super("electrolysis_stand", Material.GLASS, 2.0f, 2.0f);
	}
	
	@Override @SuppressWarnings("Deprecated")
	public boolean isOpaqueCube(IBlockState s) { return false; }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ElectrolysisStandEntity();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		ElectrolysisStandEntity te = (ElectrolysisStandEntity) world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, te);
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
	                            ItemStack stack) {
		if (stack.hasDisplayName()) {
			((ElectrolysisStandEntity) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
	                                EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side,
	                                float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(ChemicalCraft.instance, ModGuiHandler.ELECTROLYSIS_STAND_ENTITY_GUI, worldIn, pos.getX(),
					pos.getY(), pos.getZ());
		}
		return true;
	}
}
