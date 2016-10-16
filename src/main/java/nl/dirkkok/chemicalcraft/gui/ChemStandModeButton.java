package nl.dirkkok.chemicalcraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import nl.dirkkok.chemicalcraft.tileentity.ChemistryStandEntity;

class ChemStandModeButton extends GuiButton {
	private ChemistryStandEntity te;
	private GuiChemistryStandEntity gui;
	
	ChemStandModeButton(int id, int posX, int posY, GuiChemistryStandEntity gui) {
		super(id, posX, posY, 20, 20, "");
		this.te = gui.te;
		this.gui = gui;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);
		
		mc.getTextureManager().bindTexture(new ResourceLocation("chemicalcraft:textures/gui/chemical_stand.png"));
		switch (this.te.getMode()) {
			case 0: this.drawTexturedModalRect(gui.i + 30, gui.j + 50, 176, 71,  20, 20); // HEAT
				break;
			case 1: this.drawTexturedModalRect(gui.i + 30, gui.j + 50, 176, 91,  20, 20); // REACT
				break;
			case 2: this.drawTexturedModalRect(gui.i + 30, gui.j + 50, 176, 111, 20, 20); // FILTER
				break;
		}
	}
}
