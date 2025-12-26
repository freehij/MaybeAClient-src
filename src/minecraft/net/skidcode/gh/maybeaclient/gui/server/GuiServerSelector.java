package net.skidcode.gh.maybeaclient.gui.server;

import java.util.ArrayList;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.ImageBufferDownload;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.gui.altman.GuiAccManager;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class GuiServerSelector extends GuiScreen{
	public GuiScreen prevMenu;
	public GuiServerSlot slot;
	
	public GuiButton joinButton, editButton, deleteButton;
	public GuiButton directConnect, addButton, accountManager, cancelButton;
	public int selectedServer = -1;
	
	public static ArrayList<ServerInfo> servers = new ArrayList<ServerInfo>();
	
	public GuiServerSelector(GuiScreen menu) {
		this.prevMenu = menu;
	}
	public void initGui() {
		super.initGui();
		this.controlList.clear();
		this.joinButton = new GuiSmallButton(1, this.width / 2 - 50 - 100 - 5, this.height - 47, 100, 20, "Join");
		this.directConnect = new GuiSmallButton(2, this.width / 2 - 50, this.height - 47, 100, 20, "Direct Connect");
		this.addButton = new GuiSmallButton(3, this.width / 2 - 50 + 100 + 5, this.height - 47, 100, 20, "Add");
		
		this.editButton = new GuiSmallButton(4, this.width / 2 - 50 - 100 - 5, this.height - 47 + 20 + 2, 100, 20, "Edit");
		this.deleteButton = new GuiSmallButton(5, this.width / 2 - 50, this.height - 47 + 20 + 2, 100, 20, "Delete");
		this.cancelButton = new GuiSmallButton(7, this.width / 2 - 50 + 100 + 5, this.height - 47 + 20 + 2, 100, 20, "Cancel");
		
		this.accountManager = new GuiSmallButton(6, this.width - 75, 6, 75, 20, "Accounts");
		
		if(this.selectedServer == -1) this.selectServer(-1);
		
		this.controlList.add(this.joinButton);
        this.controlList.add(this.directConnect);
        this.controlList.add(this.addButton);
        this.controlList.add(this.editButton);
        this.controlList.add(this.deleteButton);
        this.controlList.add(this.accountManager);
        this.controlList.add(this.cancelButton);
        
		this.slot = new GuiServerSlot(this);
		this.slot.registerScrollButtons(this.controlList, 8, 9);
	}
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			ServerInfo selected;
			switch(button.id) {
				case 1:
					selected = servers.get(this.selectedServer);
					this.mc.displayGuiScreen(new GuiConnecting(this.mc, selected.ip, selected.port));
					break;
				case 2:
					mc.displayGuiScreen(new GuiMultiplayer(this));
					break;
				case 3:
					mc.displayGuiScreen(new GuiEditServer(this));
					break;
				case 4:
					selected = servers.get(this.selectedServer);
					mc.displayGuiScreen(new GuiEditServer(this, selected));
					break;
				case 5:
					servers.remove(this.selectedServer);
					this.selectServer(-1);
					Client.writeCurrentServers();
					break;
				case 6:
					mc.displayGuiScreen(new GuiAccManager(this, this.selectedServer));
					break;
				case 7:
					mc.displayGuiScreen(this.prevMenu);
					break;
			}
		}
	}
	public void selectServer(int i) {
		if(i == -1) {
			this.joinButton.enabled = false;
			this.editButton.enabled = false;
			this.deleteButton.enabled = false;
		}else {
			this.joinButton.enabled = true;
			this.editButton.enabled = true;
			this.deleteButton.enabled = true;
		}
		this.selectedServer = i;
	}
	public void loadSkin(String username) {
		String url = "http://s3.amazonaws.com/MinecraftSkins/" + username + ".png";
		mc.renderEngine.obtainImageData(url, new ImageBufferDownload());
		int i = mc.renderEngine.getTextureForDownloadableImage(url, "/mob/char.png");
		if(i >= 0) {
			mc.renderEngine.bindTexture(i);
		}
	}
	public void drawScreen(int mX, int mY, float rendTicks) {
		this.slot.drawScreen(mX, mY, rendTicks);
		this.drawCenteredString(this.fontRenderer, "Servers", this.width / 2, 12, 16777215);
		
		this.drawString(this.fontRenderer, "Username: "+ChatColor.LIGHTGREEN+mc.session.username, 26, 12, 16777215);
		
		this.loadSkin(mc.session.username);
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		tes.setColorOpaque_I(16777215);
		tes.addVertexWithUV((double)2, 7+16, 0.0D, 8d/64d, 16d/32d);
        tes.addVertexWithUV(18, 7+16, 0.0D, 16d/64d, 16d/32d);
        tes.addVertexWithUV(18, 6, 0.0D, 16d/64d, 8d/32d);
        tes.addVertexWithUV(2, 6, 0.0D, 8d/64d, 8d/32d);
        tes.draw();
		
		super.drawScreen(mX, mY, rendTicks);
	}
}
