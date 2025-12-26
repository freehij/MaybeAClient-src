package net.skidcode.gh.maybeaclient.gui.click;

import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.ClientInfoHack;
import net.skidcode.gh.maybeaclient.hacks.ClientNameHack;

public class ClientNameTab extends Tab{
	public static ClientNameTab instance;
	public ClientNameTab() {
		super("ClientName");
		this.xDefPos = this.xPos = 0;
		this.yDefPos = this.yPos = 0;
		this.height = 12;
		instance = this;
	}
	public void renderName() {
		int xStart = this.xPos;
		int yStart = this.yPos;
		
		this.renderFrame(xStart, yStart, xStart + this.width, yStart + 12);
		
		Client.mc.fontRenderer.drawString(Client.clientName+" "+Client.clientVersion, xStart + 2, yStart + 2, 0xffffff);
	}
	public void render() {
		this.width = Client.mc.fontRenderer.getStringWidth(Client.clientName+" "+Client.clientVersion) + 2;
		this.renderName();
	}
	
	public void renderIngame() {
		if(ClientNameHack.instance.status) super.renderIngame();
	}
}
