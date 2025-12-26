package net.skidcode.gh.maybeaclient.gui.click;

import java.util.HashMap;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.ArrayListHack;
import net.skidcode.gh.maybeaclient.hacks.KeybindingsHack;
import net.skidcode.gh.maybeaclient.hacks.RadarHack;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class RadarTab extends Tab{
	
	public static RadarTab instance;
	
	public RadarTab() {
		super("Radar", 0, 12);
		instance = this;
	}
	public void renderIngame() {
		if(RadarHack.instance.status) super.renderIngame();
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
		this.renderName(RadarHack.instance.alignment.currentMode.equalsIgnoreCase("Right"));
	}
	
	boolean first = true;
	boolean prevMinimized = this.minimized;
	public void render() {
		
		int savdWidth = this.width;
		this.width = Client.mc.fontRenderer.getStringWidth(this.name) + 2;
		
		int savdHeight = this.height;
		
		EntityPlayer local = Client.mc.thePlayer;
		HashMap<Integer, String> players = new HashMap<>();
		
		int height = 14;
		int width = this.width;
		boolean showCoords = RadarHack.instance.showXYZ.value;
		
		for(Object o : Client.mc.theWorld.playerEntities) {
			EntityPlayer player = (EntityPlayer) o;
			if(player.entityId != local.entityId || Client.mc.currentScreen instanceof ClickGUI) {
				String d = String.format("%.2f", player.getDistance(local.posX, local.posY, local.posZ));
				String dist = player.username+" ["+ChatColor.LIGHTCYAN+d+ChatColor.WHITE+"]";
				if(showCoords) {
					dist += " XYZ: "+ String.format("%s%.2f %.2f %.2f", ChatColor.LIGHTCYAN, player.posX, player.posY, player.posZ);
				}
				players.put(player.entityId, dist);
				int w = Client.mc.fontRenderer.getStringWidth(dist) + 2;
				if(w > width) width = w;
				height += 12;
			}
		}
		
		this.height = height;
		this.width = width;
		
		boolean alignRight = RadarHack.instance.alignment.currentMode.equalsIgnoreCase("Right");
		boolean expandTop = RadarHack.instance.expand.currentMode.equalsIgnoreCase("Top");
		ScaledResolution scaledResolution = new ScaledResolution(Client.mc.gameSettings, Client.mc.displayWidth, Client.mc.displayHeight);
		
		if(RadarHack.instance.staticPositon.currentMode.equalsIgnoreCase("Bottom Right")) {
			 alignRight = true;
			 expandTop = true;
			 this.xPos = scaledResolution.getScaledWidth() - this.width;
			 this.yPos = scaledResolution.getScaledHeight() - this.height;
		}else if(RadarHack.instance.staticPositon.currentMode.equalsIgnoreCase("Bottom Left")) {
			 alignRight = false;
			 expandTop = true;
			 this.xPos = 0;
			 this.yPos = scaledResolution.getScaledHeight() - this.height;
		}else if(RadarHack.instance.staticPositon.currentMode.equalsIgnoreCase("Top Right")) {
			 alignRight = true;
			 expandTop = false;
			 this.xPos = scaledResolution.getScaledWidth() - this.width;
			 this.yPos = 0;
		}else if(RadarHack.instance.staticPositon.currentMode.equalsIgnoreCase("Top Left")) {
			 alignRight = false;
			 expandTop = false;
			 this.xPos = 0;
			 this.yPos = 0;	 
		}
		
		if(!this.minimized) {
			if(first) {
				first = false;
				if(alignRight && savdWidth != this.width){
					this.xPos -= (this.width - savdWidth);
					Client.saveClickGUI();
				}
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
		
		if(players.size() > 0) {
			this.renderFrame(this.xPos, this.yPos + 15, this.xPos + this.width, this.yPos + this.height);
			int h = 15;
			for(String s : players.values()) {
				if(alignRight) {
					Client.mc.fontRenderer.drawString(s, this.xPos + this.width - Client.mc.fontRenderer.getStringWidth(s), this.yPos + h + 2, 0xffffff);
				}else {
					Client.mc.fontRenderer.drawString(s, this.xPos + 2, this.yPos + h + 2, 0xffffff);
				}
				
				h += 12;
			}
		}
		
		this.renderName(alignRight);
		prevMinimized = this.minimized;
		//String d = String.format("%.2f", player.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
		//String s = ChatColor.LIGHTCYAN+"["+d+"] "+ChatColor.GOLD+player.username+ChatColor.LIGHTCYAN+" XYZ: "+ChatColor.GOLD+String.format("%.2f %.2f %.2f", player.posX, player.posY, player.posZ);
		
		
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagCompound comp = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		comp.setInteger("xPos", RadarHack.instance.alignment.currentMode.equalsIgnoreCase("Right") ? this.xPos + this.width : this.xPos);
		comp.setInteger("yPos", this.yPos);
		comp.setBoolean("Minimized", this.minimized);
		tag.setCompoundTag("Position", comp);
	}

}
