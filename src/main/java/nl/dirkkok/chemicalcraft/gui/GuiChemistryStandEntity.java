package nl.dirkkok.chemicalcraft.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import nl.dirkkok.chemicalcraft.tileentity.ChemistryStandEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GuiChemistryStandEntity extends GuiContainer {
	private static final Logger log = LogManager.getLogger();
	final ChemistryStandEntity te;
	private final IInventory playerInv;
	private GuiButton modeButton;
	int i;
	int j;
	
	public GuiChemistryStandEntity(IInventory playerInv, ChemistryStandEntity te) {
		super(new ContainerChemistryStandEntity(playerInv, te));
		this.te = te;
		this.playerInv = playerInv;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	/* GUI structure:
	 * Progress bar is centered. The rest of the gui is build around it.
	 * progress bar: (76, 29) to (100, 46)
	 * first input slot: (54, 28) to (72, 46)
	 * second input slot: (31, 28) to (48, 45)
	 * first output slot: (104, 28) to (121, 45)
	 * second output slot: (127, 28) to (144, 45)
	 * residue slot: (104, 51) to (121, 68)
	 * fuel meter: (80, 53) to (93, 66)
	 * fuel slot: (54, 51) to (71, 68)
	 * mode button: (78, 4) to (97, 23)
	 *
	 * Coords on GUI texture:
	 * progress: (76, 29) to (100, 46)
	 * first input: (54, 28) to (72, 46)
	 * second input: (31, 28) to (48, 45)
	 * first output: (104, 28) to (121, 45)
	 * second output: (127, 28) to (144, 45)
	 * residue: (104, 51) to (121, 68)
	 * fuel meter: (80, 53) to (93, 66)
	 * fuel: (54, 51) to (71, 68)
	 * mode: (30, 50) to (49, 69) Heh, 69
	 *
	 * Textures:
	 * fuel: (176, 0) to (189, 13)
	 * progress: (176, 14) to (199, 30)
	 * button (inactive): (176, 31) to (195, 50)
	 * button (active): (176, 51) to (195, 70)
	 * heat: (176, 71) to (195, 90)
	 * react: (176, 91) to (195, 110)
	 * filter: (176, 111) to (195, 120)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(new ResourceLocation("chemicalcraft:textures/gui/chemical_stand.png"));
		i = (this.width - this.xSize) / 2;
		j = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(this.i, this.j, 0, 0, this.xSize, this.ySize); // GUI background
		
		/*if (mouseX >= 78 && mouseX <= 97 && mouseY >= 4 && mouseY <= 23) { // Mode button
			// Inside box
			this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 50, 176, 51, 20, 20);
		} else {
			// Outside box
			this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 50, 176, 31, 20, 20);
		}
		
		switch (this.te.getMode()) {
			case 0: this.drawTexturedModalRect(this.i + 30, this.j + 50, 176, 71,  20, 20);
				break;
			case 1: this.drawTexturedModalRect(this.i + 30, this.j + 50, 176, 91,  20, 20);
				break;
			case 2: this.drawTexturedModalRect(this.i + 30, this.j + 50, 176, 111, 20, 20);
				break;
		}*/
		
		if (te.getFuelTime() > 0) {
			log.error("FUELTIME");
			int scale = (int) Math.ceil((double) this.te.getFuelTime() / this.te.getMaxFuelTime() * 13);
			this.drawTexturedModalRect(i + 76, j + 29, 176, 189, 13, scale);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(new ResourceLocation("chemicalcraft:textures/gui/chemical_stand.png"));
		
		String s = this.te.getDisplayName().getUnformattedText();
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
		this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94,
				0x404040);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(this.modeButton = new ChemStandModeButton(0, this.guiLeft + 30, this.guiTop + 50, this));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if (button == this.modeButton) {
			switch (te.getMode()) {
				case 0: this.te.setMode(1);
						break;
				case 1: this.te.setMode(2);
						break;
				case 2: this.te.setMode(0);
						break;
			}
		}
	}
}
