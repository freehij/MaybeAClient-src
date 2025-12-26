package net.skidcode.gh.maybeaclient.gui.server;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.gui.altman.AccountInfo;
import net.skidcode.gh.maybeaclient.gui.altman.GuiAccManager;
import net.skidcode.gh.maybeaclient.gui.altman.PasswordInfo;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class GuiServerSlot extends GuiSlot{
	public GuiServerSelector parent;
	public GuiServerSlot(GuiServerSelector gui) {
		super(Client.mc, gui.width, gui.height, 32, gui.height - 55 + 4, 24);
		this.parent = gui;
	}

	@Override
	protected int getSize() {
		return GuiServerSelector.servers.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2) {
		this.parent.selectServer(var1);
	}

	@Override
	protected boolean isSelected(int var1) {
		return this.parent.selectedServer == var1;
	}

	@Override
	protected int getContentHeight() {
		return this.getSize() * 24;
	}

	@Override
	protected void drawBackground() {
		this.parent.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int slotID, int x, int y, int var4, Tessellator tess) {
		ServerInfo info = GuiServerSelector.servers.get(slotID);
		Client.mc.fontRenderer.drawString(info.name, x, y, 0xffffff);
		Client.mc.fontRenderer.drawString(info.ip+":"+info.port, x, y+10, 0x505050);
		AccountInfo accinfo = GuiAccManager.usernames.get(Client.mc.session.username);
		if(accinfo != null) {
			PasswordInfo serv = accinfo.passwords.get(info.ip+":"+info.port);
			if(serv == null && info.port == 25565) serv = accinfo.passwords.get(info.ip);
			
			if(serv != null) {
				Client.mc.fontRenderer.drawString(ChatColor.LIGHTGREEN+"AL", this.width / 2 + 110 - 16 , y + 6, 0xffffff);
			}
		}
		//Client.mc.fontRenderer.drawString(ChatColor.LIGHTRED+"AL", this.width / 2 + 110 - 16 , y + 6, 0xffffff);
	}

}
