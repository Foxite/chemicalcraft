package nl.dirkkok.chemicalcraft;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
