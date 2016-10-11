package nl.dirkkok.chemicalcraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import nl.dirkkok.chemicalcraft.tileentity.ChemistryStandEntity;

public class GuiChemistryStandEntity extends GuiContainer {
	public GuiChemistryStandEntity(IInventory playerInv, ChemistryStandEntity te) {
		super(new ContainerChemistryStandEntity(playerInv, te));
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}
	
	@Override
	protected void drawGuiForegroundLayer(int mouseX, int mouseY) {}
	
	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super(mouseX, mouseY, partialTicks);
		
		mc.getTextureManager().bindTexture(ResourceLocation
	}
}
