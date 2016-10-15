package nl.dirkkok.chemicalcraft.items;

import net.minecraft.item.Item;

public class BasicItem extends Item {
	public BasicItem(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(ModItems.chemTab);
		this.setRegistryName(name);
	}
	
}
