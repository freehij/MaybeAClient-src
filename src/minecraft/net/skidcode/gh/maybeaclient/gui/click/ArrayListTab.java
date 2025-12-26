package net.skidcode.gh.maybeaclient.gui.click;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.Hack;
import net.skidcode.gh.maybeaclient.hacks.KeybindingsHack;
import net.skidcode.gh.maybeaclient.hacks.ArrayListHack;
import net.skidcode.gh.maybeaclient.hacks.ArrayListHack.SorterAZ;
import net.skidcode.gh.maybeaclient.hacks.ArrayListHack.SorterZA;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class ArrayListTab extends Tab{
	
	public static ArrayListTab instance;
	
	public ArrayListTab() {
		super("Enabled Modules");
		this.xDefPos = this.xPos = 10;
		this.yDefPos = this.yPos = 100;
		this.height = 12;
		
		instance = this;
	}
	public int yOffset = 0;
	
	public int oldEnabledCnt = 0;
	boolean first = true;
	public void renderIngame() {
		if(ArrayListHack.instance.status) super.renderIngame();
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
		this.renderName(ArrayListHack.instance.alignment.currentMode.equalsIgnoreCase("Right"));
	}
	
	public void render() {
		ArrayList<String> enabled = new ArrayList<>();
		int savdHeight = this.height;
		int savdWidth = this.width;
		int totalHeight = 0;
		int totalWidth = Client.mc.fontRenderer.getStringWidth(this.name) + 2;
		for(Hack h : Client.hacksByName.values()) {
			if(h.status) {
				totalHeight += 12;
				String name = h.getNameForArrayList();
				int size = Client.mc.fontRenderer.getStringWidth(name) + 2;
				if(totalWidth < size) {
					totalWidth = size;
				}
				enabled.add(name);
			}
		}
		this.width = totalWidth;
		this.height = totalHeight + 14;
		if(this.minimized) this.height = 12;
		
		boolean alignRight = ArrayListHack.instance.alignment.currentMode.equalsIgnoreCase("Right");
		boolean expandTop = ArrayListHack.instance.expand.currentMode.equalsIgnoreCase("Top");
		ScaledResolution scaledResolution = new ScaledResolution(Client.mc.gameSettings, Client.mc.displayWidth, Client.mc.displayHeight);
		if(ArrayListHack.instance.staticPositon.currentMode.equalsIgnoreCase("Bottom Right")) {
			 alignRight = true;
			 expandTop = true;
			 this.xPos = scaledResolution.getScaledWidth() - this.width;
			 this.yPos = scaledResolution.getScaledHeight() - this.height;
		}else if(ArrayListHack.instance.staticPositon.currentMode.equalsIgnoreCase("Bottom Left")) {
			 alignRight = false;
			 expandTop = true;
			 this.xPos = 0;
			 this.yPos = scaledResolution.getScaledHeight() - this.height;
		}else if(ArrayListHack.instance.staticPositon.currentMode.equalsIgnoreCase("Top Right")) {
			 alignRight = true;
			 expandTop = false;
			 this.xPos = scaledResolution.getScaledWidth() - this.width;
			 this.yPos = 0;
		}else if(ArrayListHack.instance.staticPositon.currentMode.equalsIgnoreCase("Top Left")) {
			 alignRight = false;
			 expandTop = false;
			 this.xPos = 0;
			 this.yPos = 0;
		}
		
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
			this.renderMinimized();
			return;
		}
		
		this.renderName(alignRight);
		
		this.renderFrame(this.xPos, this.yPos + 15, this.xPos + totalWidth, this.yPos + 15 + totalHeight + this.yOffset);
		//GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		//GL11.glStencilMask(0x00);
		if(ArrayListHack.instance.sortMode.currentMode.equalsIgnoreCase("Ascending")) enabled.sort(SorterAZ.inst);
		else if(ArrayListHack.instance.sortMode.currentMode.equalsIgnoreCase("Descending"))  enabled.sort(SorterZA.inst);
		//GL11.glTranslated(0.0f, yOffset, 0);
		for(int i = 0; i < enabled.size(); ++i) {
			String s = enabled.get(i);
			int rendX, rendY;
			if(alignRight) {
				rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(s);
			}else {
				rendX = this.xPos + 2;
			}
			
			rendY = this.yPos+i*12 + 15 + 2;
			
			Client.mc.fontRenderer.drawString(s, rendX, rendY, 0xffffff);
		}
		//GL11.glTranslated(0.0f, -yOffset, 0);
		
		//GL11.glStencilMask(0xFF);
		//GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
		//GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
	
	@Override
	public void wheelMoved(int wheel, int x, int y) {
		//int maxy = this.yPos + this.height + this.yOffset;
		//ScaledResolution sr = new ScaledResolution(Client.mc.gameSettings, Client.mc.displayWidth, Client.mc.displayHeight);
		//if(maxy > sr.getScaledHeight()) {
		//	 yOffset += Math.signum(wheel)*12;
		//	 if(yOffset > 0) yOffset = 0;
		//}else if(yOffset != 0 && wheel > 0){
		//	yOffset += Math.signum(wheel)*12;
		//	if(yOffset > 0) yOffset = 0;
		//}
	}
}
