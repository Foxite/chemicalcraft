package nl.dirkkok.chemicalcraft.items;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class TestTubeColor implements IItemColor{
	/* Technically ammonia, acids, etc colorless but a crapton of chemicals are colorless and we can't use the alpha
	 * channel. I don't want a billion white chemicals that are indistinguishable in the game. IRL, sure, but not
	 * here. That's why I intentionally used colors of chemicals that were dyed, or contained in a colored bottle,
	 * for visual recognition.
	 */
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if (tintIndex == 1) {
			switch (stack.getItemDamage() & ~(1 << 30)) {
				case 0: return 0xFFFFFF; // Empty
				case 1: return 0x265BFF; // Fresh water
				case 2: return 0x44EBD6; // Pure water
				case 3: return 0xF2F2F2; // NaCl
				case 4: return 0xE1E114; // Nitric acid
				case 5: return 0xFFFFFF; // Sulfuric acid
				case 6: return 0xF2F295; // Mixture of nitric and sulfuric acid
				case 7: return 0x3D2B1F; // Toluene
				case 8: return 0xFFD300; // Nitrotoluene
				case 9: return 0xFFFFFF; // Sodium bicarbonate
				case 10: return 0xFFD300; // Cleaned nitrotoluene
				case 11: return 0xDACDB1; // Unfinished TNT
				case 12: return 0xDACDB1; // Raw TNT
				case 13: return 0x0B4FE0; // Copper(II) nitrate
				case 14: return 0xFFFFFF; // Sodium nitrate
				case 15: return 0x00CCFF; // Ammonia
				case 16: return 0x00308F; // Salty water
				case 17: return 0x00A9E6; // Mixture of ammonia and salty water
				case 18: return 0xBBBBBB; // CO2
				case 19: return 0x83B6C8; // Mixture of ammonia, salty water and CO2
				case 20: return 0xFFFFFF; // Elemental hydrogen
				case 21: return 0xFFFFFF; // Elemental nitrogen
				case 22: return 0xFFFFFF; // Elemental oxygen
				default: return 0xFFFFFF;
			}
		} else {
			return 0xFFFFFF; // White
		}
	}
}
