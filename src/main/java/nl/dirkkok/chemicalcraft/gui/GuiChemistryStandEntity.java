package nl.dirkkok.chemicalcraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import nl.dirkkok.chemicalcraft.tileentity.ChemistryStandEntity;

public class GuiChemistryStandEntity extends GuiContainer {
	public static final ResourceLocation INVENTORY_BACKGROUND = new ResourceLocation
			("chemicalcraft:textures/gui/chemical_stand.png");
	private final ChemistryStandEntity te;
	private final IInventory playerInv;
	
	public GuiChemistryStandEntity(IInventory playerInv, ChemistryStandEntity te) {
		super(new ContainerChemistryStandEntity(playerInv, te));
		this.te = te;
		this.playerInv = playerInv;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	/* GUI structure:
	 * Progress bar is centered. The rest of the gui is build around it. Elements are spaced by 5 pixels.
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
	 * mode: (78, 4) to (97, 23)
	 *
	 * Textures:
	 * fuel: (176, 0) to (189, 13)
	 * progress: (176, 14) to (199, 30)
	 * button (inactive): (176, 31) to (195, 50)
	 * button (active): (176, 51) to (195, 70)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(new ResourceLocation("chemicalcraft:textures/gui/chemical_stand.png"));
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.te.getDisplayName().getUnformattedText();
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
		this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2,
				0x404040);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
