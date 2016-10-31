package nl.dirkkok.chemicalcraft;

import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/* To keep things simple and fun, this mod incorporates a lot of scientific inaccuracies. I could make everything
 * correct, but it would make take away all of the fun and unnecessarily complicated for a game. Try to find them all,
 * and I'll give you a sticker.
 */
@Mod(modid = ChemicalCraft.MODID, name = ChemicalCraft.MODNAME, version = ChemicalCraft.VERSION)
@SuppressWarnings("unused")
public class ChemicalCraft {
	public static final String MODID = "chemicalcraft";
	public static final String MODNAME = "ChemicalCraft";
	public static final String VERSION = "0.1";
	public static Logger LOG;
	@SidedProxy(clientSide = "nl.dirkkok.chemicalcraft.ClientProxy",
				serverSide = "nl.dirkkok.chemicalcraft.ServerProxy")
	public static CommonProxy proxy;
	@Instance
	public static ChemicalCraft instance = new ChemicalCraft();
	
	// List of loaded mods that this mod supports. Used internally.
	public static final List<String> supportedModsLoaded = new ArrayList<String>();
	private boolean errorHasBeenLogged = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		LOG = e.getModLog();
		
		if (Loader.isModLoaded("railcraft")) {
			supportedModsLoaded.add("railcraft");
		}
		if (Loader.isModLoaded("IC2")) {
			supportedModsLoaded.add("ic2");
		}
		proxy.preInit(e);
		
		LOG.info("This is " + MODNAME + " version " + VERSION);
		if (e.getModState() == LoaderState.ModState.ERRORED) {
			LOG.error(MODNAME + " preinitialization FAILED");
			errorHasBeenLogged = true;
		}
		
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
		
		if (e.getModState() == LoaderState.ModState.ERRORED && !errorHasBeenLogged) {
			LOG.error(MODNAME + " initialization FAILED");
			errorHasBeenLogged = true;
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
		
		if (e.getModState() == LoaderState.ModState.ERRORED && !errorHasBeenLogged) {
			LOG.error(MODNAME + " postinitialization FAILED");
		} else {
			LOG.info(MODNAME + " initialization successful");
		}
	}
}
