package nl.dirkkok.chemicalcraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ChemicalCraft.MODID, name = ChemicalCraft.MODNAME, version = ChemicalCraft.VERSION)
@SuppressWarnings("unused")
public class ChemicalCraft {
	public static final String MODID = "chemicalcraft";
	public static final String MODNAME = "ChemicalCraft";
	public static final String VERSION = "0.1";
	public static final Logger LOG = LogManager.getLogger();

	@SidedProxy(clientSide = "nl.dirkkok.chemicalcraft.ClientProxy",
				serverSide = "nl.dirkkok.chemicalcraft.ServerProxy")
	public static CommonProxy proxy;

	@Instance
	public static ChemicalCraft instance = new ChemicalCraft();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
		LOG.info("This is " + MODNAME + " version " + VERSION);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) { proxy.init(e); }

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
		LOG.info(MODNAME + " initialization successful");
	}
}
