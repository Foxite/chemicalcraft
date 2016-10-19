package nl.dirkkok.chemicalcraft.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import nl.dirkkok.chemicalcraft.blocks.ModBlocks;

import java.util.Random;

public class ModWorldGen implements IWorldGenerator {
	private WorldGenerator genSaltpeterOre;
	private WorldGenerator genCorkTrees;
	
	public ModWorldGen() {
		this.genSaltpeterOre = new WorldGenMinable(ModBlocks.saltpeterOre.getDefaultState(), 10);
		this.genCorkTrees = new WorldGenTrees(true, 5, ModBlocks.corkWood.getDefaultState(), Blocks.LEAVES.getDefaultState(), false);
	}
	
	@Override
	public void generate(Random r, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider
			chunkProvider) {
		if (world.provider.getDimension() == 0) { // Overworld
			this.runGenerator(this.genSaltpeterOre, world, r, chunkX, chunkZ, 10, 0, 64);
			
			// Cork trees
			switch (Biome.getIdForBiome(world.getBiomeForCoordsBody(new BlockPos(chunkX, 64, chunkZ)))) {
				case 4: // Forest
				case 18: // Forest Hills
				case 132: // Forest M, Flower Forest
					this.runGenerator(this.genCorkTrees, world, r, chunkX, chunkZ, 10, 0, 64); // 10% chance
					break;
				case 21: // Jungle
				case 22: // Jungle Hills
				case 23: // Jungle Edge
				case 149: // Jungle M
				case 151: // Jungle Edge M
					this.runGenerator(this.genCorkTrees, world, r, chunkX, chunkZ, 20, 0, 64); // 20% chance
					break;
				default: break;
			}
		} else if (world.provider.getDimension() == -1) { // Nether
			
		} else if (world.provider.getDimension() == 1) { // End
			
		}
	}
	
	private void runGenerator(WorldGenerator gen, World world, Random r, int chunkX, int chunkZ, int chance, int
			minHeight, int maxHeight) {
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal height arguments for runGenerator. minHeight = " + minHeight +
					", maxHeight = " + maxHeight);
		
		int dHeight = maxHeight - minHeight + 1;
		for (int i = 0; i < chance; i++) {
			int x = chunkX * 16 + r.nextInt(16);
			int y = minHeight + r.nextInt(dHeight);
			int z = chunkZ * 16 + r.nextInt(16);
			gen.generate(world, r, new BlockPos(x, y, z));
		}
	}
	
	public WorldGenerator getCorkTreeGenerator() { return genCorkTrees; }
}
