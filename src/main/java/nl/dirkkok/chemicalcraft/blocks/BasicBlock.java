package nl.dirkkok.chemicalcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import nl.dirkkok.chemicalcraft.items.ModItems;

class BasicBlock extends Block {
	BasicBlock(String name, Material material, float hardness, float resistance) {
		super(material);
		this.setUnlocalizedName(name);
		this.setCreativeTab(ModItems.chemTab);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setRegistryName(name);
	}
}
