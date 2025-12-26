package net.skidcode.gh.maybeaclient.gui.altman;

import net.minecraft.src.GuiSlot;
import net.minecraft.src.ImageBufferDownload;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.Client;

public class GuiAltSlot extends GuiSlot{
	public GuiAccManager parent;
	
	public GuiAltSlot(GuiAccManager gui) {
		super(Client.mc, gui.width, gui.height, 32, gui.height - 55 + 4, 20);
		this.parent = gui;
	}

	@Override
	protected int getSize() {
		return GuiAccManager.accounts.size();
	}

	@Override
	protected void elementClicked(int i, boolean var2) {
		this.parent.selectAccount(i);
	}

	@Override
	protected boolean isSelected(int i) {
		return this.parent.currentlySelected == i;
	}

	@Override
	protected int getContentHeight() {
		return this.getSize() * 20;
	}

	@Override
	protected void drawBackground() {
		this.parent.drawDefaultBackground();
	}
	public void loadSkin(String username) {
		String url = "http://s3.amazonaws.com/MinecraftSkins/" + username + ".png";
		Client.mc.renderEngine.obtainImageData(url, new ImageBufferDownload());
		int i = Client.mc.renderEngine.getTextureForDownloadableImage(url, "/mob/char.png");
		if(i >= 0) {
			Client.mc.renderEngine.bindTexture(i);
		}
	}
	@Override
	protected void drawSlot(int slotID, int x, int y, int var4, Tessellator var5) {
		AccountInfo acc = GuiAccManager.accounts.get(slotID);
		this.loadSkin(acc.name);
		Tessellator tes = Tessellator.instance;
		
		tes.startDrawingQuads();
		tes.setColorOpaque_I(16777215);
		tes.addVertexWithUV((double)x+1, y+15, 0.0D, 8d/64d, 16d/32d);
        tes.addVertexWithUV(x+15, y+15, 0.0D, 16d/64d, 16d/32d);
        tes.addVertexWithUV(x+15, y+1, 0.0D, 16d/64d, 8d/32d);
        tes.addVertexWithUV(x+1, y+1, 0.0D, 8d/64d, 8d/32d);
        tes.draw();
        
		Client.mc.fontRenderer.drawString(acc.name, x + 17, y + 5, 0xffffff);
	} 

}
