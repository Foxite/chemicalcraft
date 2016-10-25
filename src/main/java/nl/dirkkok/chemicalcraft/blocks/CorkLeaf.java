package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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

public class CorkLeaf extends BlockLeaves implements net.minecraftforge.common.IShearable {
	public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
	public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
	int[] surroundings;
	
	public CorkLeaf() {
		//super("cork_leaf", Material.LEAVES, 0.2F, 1F);
		this.setRegistryName("cork_leaf");
		this.setUnlocalizedName("cork_leaf");
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.TRUE)
				.withProperty(DECAYABLE, Boolean.TRUE));
	}
	
	@Override
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
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return !Minecraft.isFancyGraphicsEnabled();
	}
	
	@SideOnly(Side.CLIENT) @Override
	public BlockRenderLayer getBlockLayer()
	{
		return Minecraft.isFancyGraphicsEnabled() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
	}
	
	
	@Override
	public boolean isVisuallyOpaque()
	{
		return !Minecraft.isFancyGraphicsEnabled();
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
	
	@Override public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos){ return true; }
	
	@Override public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos){ return true; }
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
	{
		return Collections.singletonList(new ItemStack(this, 1));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return !(!Minecraft.isFancyGraphicsEnabled() && blockAccess.getBlockState(pos.offset(side)).getBlock() == this) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	@Override
	public BlockPlanks.EnumType getWoodType(int i) {
		return BlockPlanks.EnumType.OAK;
	}
	
}
