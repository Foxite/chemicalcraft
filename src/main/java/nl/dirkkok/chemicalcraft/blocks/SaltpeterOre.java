package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.dirkkok.chemicalcraft.items.ModItems;

import java.util.Random;

class SaltpeterOre extends BasicBlock {
	SaltpeterOre() {
		super("saltpeter_ore", Material.ROCK, 3.0F, 15);
	}
	
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		Random r = world instanceof World ? ((World)world).rand : new Random();
		return r.nextInt(2 * (fortune + 1));
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random r, int fortune) {
		return ModItems.saltpeterDust;
	}
	
	@Override
	public int quantityDropped(Random r) {
		return r.nextInt(3) + 3; // Random int between 3 and 5
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random r) {
		if (fortune > 0) return r.nextInt(3) * fortune + 3;
		else return quantityDropped(r);
	}
}
