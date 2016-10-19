package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;
import nl.dirkkok.chemicalcraft.items.ModItems;

import java.util.Random;

public class CorkSapling extends BlockBush implements IGrowable {
	public CorkSapling() {
		super();
		this.setRegistryName("cork_sapling");
		this.setUnlocalizedName("cork_sapling");
		this.setCreativeTab(ModItems.chemTab);
	}
	
	@Override
	public void grow(World world, Random r, BlockPos pos, IBlockState state) {
		WorldGenerator treeGen = new WorldGenTrees(true, 5, ModBlocks.corkWood.getDefaultState(), Blocks.LEAVES.getDefaultState(), false);
		treeGen.generate(world, r, pos);
	}
	
	@Override
	public boolean canGrow(World w, BlockPos p, IBlockState s, boolean b) { return true; }
	
	@Override
	public boolean canUseBonemeal(World w, Random r, BlockPos p, IBlockState s) { return true; }
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}
}
