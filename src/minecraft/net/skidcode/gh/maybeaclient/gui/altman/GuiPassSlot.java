package net.skidcode.gh.maybeaclient.gui.altman;

import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.Client;

public class GuiPassSlot extends GuiSlot{
	public GuiPwdManager parent;
	
	public GuiPassSlot(GuiPwdManager gui) {
		super(Client.mc, gui.width, gui.height, 32, gui.height - 55 + 4, 24);
		this.parent = gui;
	}

	@Override
	protected int getSize() {
		return this.parent.info.passwords.size();
	}

	@Override
	protected void elementClicked(int i, boolean var2) {
		this.parent.select(i);
	}

	@Override
	protected boolean isSelected(int i) {
		return this.parent.currentlySelected == i;
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
	protected void drawSlot(int slotID, int x, int y, int var4, Tessellator var5) {
		PasswordInfo pwd = this.parent.info.pwds.get(slotID);
		Client.mc.fontRenderer.drawString(pwd.serverIP, x, y, 0xffffff);
		String s = "";
		for(int i = 0; i < pwd.password.length(); ++i) s += "*";
		Client.mc.fontRenderer.drawString(s, x, y+12, 0x505050);
	} 

}
