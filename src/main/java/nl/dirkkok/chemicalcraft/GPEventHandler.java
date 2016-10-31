package nl.dirkkok.chemicalcraft;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import nl.dirkkok.chemicalcraft.client.render.blocks.BlockRenderRegister;

/**
 * General Purpose Event Handler.
 */
public class GPEventHandler {
	//@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		ChemicalCraft.LOG.error("HEY!");
		BlockRenderRegister.preInit();
	}
}
