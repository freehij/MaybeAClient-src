package net.skidcode.gh.maybeaclient.gui.click;

import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.Hack;
import net.skidcode.gh.maybeaclient.hacks.KeybindingsHack;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class KeybindingsTab extends Tab{
	
	public static KeybindingsTab instance;
	
	public KeybindingsTab() {
		super("Keybindings", 0, 14);
		this.yDefPos = this.yPos = 15;
		instance = this;
	}
	public void renderIngame() {
		if(KeybindingsHack.instance.status) super.renderIngame();
	}
	public void renderName(boolean alignRight) {
		if(alignRight) {
			int xStart = this.xPos;
			int yStart = this.yPos;
			
			this.renderFrame(xStart, yStart, xStart + this.width, yStart + 12);
			
			Client.mc.fontRenderer.drawString(this.name, xStart + this.width - Client.mc.fontRenderer.getStringWidth(this.name), yStart + 2, 0xffffff);
		}else {
			super.renderName();
		}
	}
	
	public void renderMinimized() {
		this.height = 12;
		this.width = Client.mc.fontRenderer.getStringWidth(this.name) + 2;
		this.renderName(KeybindingsHack.instance.alignment.currentMode.equalsIgnoreCase("Right"));
	}
	
	boolean first = true;
	public void render() {
		int height = 14;
		int savdHeight = this.height;
		int savdWidth = this.width;
		int width = Client.mc.fontRenderer.getStringWidth(this.name) + 2;
		boolean hasBinds = false;
		for(Hack h : Client.hacksByName.values()) {
        	if(h.keybinding.value != 0) {
        		height += 12;
        		int w = Client.mc.fontRenderer.getStringWidth("["+ChatColor.LIGHTCYAN+h.keybinding.valueToString()+ChatColor.WHITE+"] "+h.name) + 2;
        		if(w > width) width = w;
        		hasBinds = true;
        	}
		}
		boolean alignRight = KeybindingsHack.instance.alignment.currentMode.equalsIgnoreCase("Right");
		boolean expandTop = KeybindingsHack.instance.expand.currentMode.equalsIgnoreCase("Top");
		this.height = height;
		this.width = width;
		
		if(!this.minimized) {
			if(first) {
				first = false;
			}else {
				boolean sav = false;
				if(expandTop && savdHeight != this.height) {
					this.yPos -= (this.height - savdHeight);
					sav = true;
				}
				
				if(alignRight && savdWidth != this.width){
					this.xPos -= (this.width - savdWidth);
					sav = true;
				}
				
				if(sav) Client.saveClickGUI();
			}
		}
		
		if(this.minimized) {
			this.width = Client.mc.fontRenderer.getStringWidth(this.name) + 2;
			if(alignRight && savdWidth != this.width){
				this.xPos -= (this.width - savdWidth);
			}
			this.renderMinimized();
			return;
		}
		
		this.renderName(alignRight);
		if(!hasBinds) return;
		
		this.renderFrame(this.xPos, this.yPos + 12 + 3, this.xPos + this.width, this.yPos + this.height);
		
		int i = 1;
        for(Hack h : Client.hacksByName.values()) {
        	if(h.keybinding.value != 0) {
        		String s = "["+ChatColor.LIGHTCYAN+h.keybinding.valueToString()+ChatColor.WHITE+"] "+h.name;
        		
        		if(alignRight) {
        			Client.mc.fontRenderer.drawString(s, this.xPos + this.width - Client.mc.fontRenderer.getStringWidth(s), this.yPos + i*12 + 3 + 2, 0xffffff);
        		}else {
        			Client.mc.fontRenderer.drawString(s, this.xPos + 2, this.yPos + i*12 + 3 + 2, 0xffffff);
        		}
	        	++i;
        	}
        }
	}
	
}
