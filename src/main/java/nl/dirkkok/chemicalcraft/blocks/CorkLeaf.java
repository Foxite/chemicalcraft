package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CorkLeaf extends BasicBlock implements net.minecraftforge.common.IShearable {
	public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
	public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
	private boolean leavesFancy;
	private int[] surroundings;
	
	public CorkLeaf() {
		super("cork_leaf", Material.LEAVES, 0.2F, 1F);
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.TRUE).withProperty(DECAYABLE, Boolean.TRUE));
	}
	
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		int k = pos.getX();
		int l = pos.getY();
		int i1 = pos.getZ();
		
		if (worldIn.isAreaLoaded(new BlockPos(k - 2, l - 2, i1 - 2), new BlockPos(k + 2, l + 2, i1 + 2)))
		{
			for (int j1 = -1; j1 <= 1; ++j1)
			{
				for (int k1 = -1; k1 <= 1; ++k1)
				{
					for (int l1 = -1; l1 <= 1; ++l1)
					{
						BlockPos blockpos = pos.add(j1, k1, l1);
						IBlockState iblockstate = worldIn.getBlockState(blockpos);
						
						if (iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos))
						{
							iblockstate.getBlock().beginLeavesDecay(iblockstate, worldIn, blockpos);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (state.getValue(CHECK_DECAY) && state.getValue(DECAYABLE))
			{
				int k = pos.getX();
				int l = pos.getY();
				int i1 = pos.getZ();
				
				if (this.surroundings == null)
				{
					this.surroundings = new int[32768];
				}
				
				if (worldIn.isAreaLoaded(new BlockPos(k - 5, l - 5, i1 - 5), new BlockPos(k + 5, l + 5, i1 + 5)))
				{
					BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
					
					for (int i2 = -4; i2 <= 4; ++i2)
					{
						for (int j2 = -4; j2 <= 4; ++j2)
						{
							for (int k2 = -4; k2 <= 4; ++k2)
							{
								IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2));
								Block block = iblockstate.getBlock();
								
								if (!block.canSustainLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2)))
								{
									if (block.isLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2)))
									{
										this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -2;
									}
									else
									{
										this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -1;
									}
								}
								else
								{
									this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = 0;
								}
							}
						}
					}
					
					for (int i3 = 1; i3 <= 4; ++i3)
					{
						for (int j3 = -4; j3 <= 4; ++j3)
						{
							for (int k3 = -4; k3 <= 4; ++k3)
							{
								for (int l3 = -4; l3 <= 4; ++l3)
								{
									if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16] == i3 - 1)
									{
										if (this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
										}
										
										if (this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
										}
										
										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] = i3;
										}
										
										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] = i3;
										}
										
										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] = i3;
										}
										
										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] = i3;
										}
									}
								}
							}
						}
					}
				}
				
				int l2 = this.surroundings[16912];
				
				if (l2 >= 0)
				{
					worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, Boolean.FALSE), 4);
				}
				else
				{
					this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
					worldIn.setBlockToAir(pos);
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (worldIn.isRainingAt(pos.up()) && !worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP)
				&& rand.nextInt(15) == 1)
		{
			double d0 = (double)((float)pos.getX() + rand.nextFloat());
			double d1 = (double)pos.getY() - 0.05D;
			double d2 = (double)((float)pos.getZ() + rand.nextFloat());
			worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return random.nextInt(20) == 0 ? 1 : 0;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ModBlocks.corkSaplingItem;
	}
	
	/*
	public boolean isOpaqueCube(IBlockState state)
	{
		return !this.leavesFancy;
	}*/
	
	@SideOnly(Side.CLIENT)
	public void setGraphicsLevel(boolean fancy)
	{
		this.leavesFancy = fancy;
	}
	
	@SideOnly(Side.CLIENT) @Override
	public BlockRenderLayer getBlockLayer()
	{
		return this.leavesFancy ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
	}
	
	@Override
	public boolean isVisuallyOpaque()
	{
		return false;
	}
	
	@Override public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos){ return true; }
	@Override public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos){ return true; }
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
	{
		return Collections.singletonList(new ItemStack(this, 1));
	}
	
	@Override
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos)
	{
		if (!state.getValue(CHECK_DECAY))
		{
			world.setBlockState(pos, state.withProperty(CHECK_DECAY, true), 4);
		}
	}
	
	@Override
	public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		int chance = 20;
		
		if (fortune > 0)
		{
			chance -= 2 << fortune;
			if (chance < 10) chance = 10;
		}
		
		if (rand.nextInt(chance) == 0)
			ret.add(new ItemStack(ModBlocks.corkSaplingItem));
		
		return ret;
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE});
	}
	
	@Override
	public int getMetaFromState(IBlockState s) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int m) {
		return this.getDefaultState();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(CHECK_DECAY, state.getValue(CHECK_DECAY))
				.withProperty(DECAYABLE, state.getValue(DECAYABLE));
	}
	
	/*
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return !(!this.leavesFancy && blockAccess.getBlockState(pos.offset(side)).getBlock() == this) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}*/
	
}
