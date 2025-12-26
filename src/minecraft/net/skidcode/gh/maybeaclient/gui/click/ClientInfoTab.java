package net.skidcode.gh.maybeaclient.gui.click;

import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.ArrayListHack;
import net.skidcode.gh.maybeaclient.hacks.ClientInfoHack;
import net.skidcode.gh.maybeaclient.utils.ChatColor;
import net.skidcode.gh.maybeaclient.utils.PlayerUtils;

public class ClientInfoTab extends Tab{
	
	public static ClientInfoTab instance;
	
	public ClientInfoTab() {
		super("Player info", 0, 12);
		this.xDefPos = this.xPos = 255;
		this.yDefPos = this.yPos = 10;
		instance = this;
	}
	
	public void renderIngame() {
		if(ClientInfoHack.instance.status) super.renderIngame();
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
		this.renderName(ClientInfoHack.instance.alignment.currentMode.equalsIgnoreCase("Right"));
	}
	
	boolean first = true;
	public void render() {
		int wid;
		int savdWidth = this.width;
		this.width = Client.mc.fontRenderer.getStringWidth(this.name) + 2;
		
		
		boolean renderCoords = ClientInfoHack.instance.coords.value;
		boolean renderFacing = ClientInfoHack.instance.facing.value;
		boolean renderFPS = ClientInfoHack.instance.fps.value;
		boolean renderUsername = ClientInfoHack.instance.username.value;
		boolean renderSpeed = ClientInfoHack.instance.walkingSpeed.value;
		
		String coordX = "", coordY = "", coordZ = "";

		String facing = String.format("Facing: %s", PlayerUtils.getDirection());
		String fps = String.format("FPS: %s", Client.mc.fps);
		String username = String.format("Username: %s", Client.mc.session.username);
		String walkingSpeed = String.format("Speed: %.4f BPS", PlayerUtils.getSpeed(ClientInfoHack.instance.useHorizontal.value));	
		
		this.height = 14;
		if(this.minimized) this.height = 12;
		else {
			if(renderCoords) {
				
				if(ClientInfoHack.instance.showNetherCoords.value) {
					boolean inNether = false;
					if(!Client.mc.isMultiplayerWorld()) {
						inNether = Client.mc.theWorld.worldProvider.isHellWorld;
					}else {
						if(ClientInfoHack.instance.isInNether.currentMode.equals("Detect")) {
							inNether = PlayerUtils.isInNether();
						}else if(ClientInfoHack.instance.isInNether.currentMode.equals("Nether")) {
							inNether = true;
						}
					}
					
					
					if(inNether) {
						coordX = String.format("X: %.2f %s%.2f", Client.mc.thePlayer.posX, ChatColor.LIGHTGREEN, Client.mc.thePlayer.posX*8);
						coordY = String.format("Y: %.2f %s%.2f", Client.mc.thePlayer.posY, ChatColor.LIGHTGREEN, Client.mc.thePlayer.posY*8);
						coordZ = String.format("Z: %.2f %s%.2f", Client.mc.thePlayer.posZ, ChatColor.LIGHTGREEN, Client.mc.thePlayer.posZ*8);
					}else {
						coordX = String.format("X: %.2f %s%.2f", Client.mc.thePlayer.posX, ChatColor.LIGHTRED, Client.mc.thePlayer.posX/8);
						coordY = String.format("Y: %.2f %s%.2f", Client.mc.thePlayer.posY, ChatColor.LIGHTRED, Client.mc.thePlayer.posY/8);
						coordZ = String.format("Z: %.2f %s%.2f", Client.mc.thePlayer.posZ, ChatColor.LIGHTRED, Client.mc.thePlayer.posZ/8);
					}
				}else {
					coordX = String.format("X: %.2f", Client.mc.thePlayer.posX);
					coordY = String.format("Y: %.2f", Client.mc.thePlayer.posY);
					coordZ = String.format("Z: %.2f", Client.mc.thePlayer.posZ);
				}
				
				wid = Client.mc.fontRenderer.getStringWidth(coordX) + 2;
				if(wid > this.width) this.width = wid;
				wid = Client.mc.fontRenderer.getStringWidth(coordY) + 2;
				if(wid > this.width) this.width = wid;
				wid = Client.mc.fontRenderer.getStringWidth(coordZ) + 2;
				if(wid > this.width) this.width = wid;
				this.height += 12*3;
			}
			if(renderFacing) {
				wid = Client.mc.fontRenderer.getStringWidth(facing) + 2;
				if(wid > this.width) this.width = wid;
				
				this.height += 12;
			}
			if(renderFPS) {
				wid = Client.mc.fontRenderer.getStringWidth(fps) + 2;
				if(wid > this.width) this.width = wid;
				
				this.height += 12;
			}
			if(renderUsername) {
				wid = Client.mc.fontRenderer.getStringWidth(username) + 2;
				if(wid > this.width) this.width = wid;
				
				this.height += 12;
			}
			
			if(renderSpeed) {
				wid = Client.mc.fontRenderer.getStringWidth(walkingSpeed) + 2;
				if(wid > this.width) this.width = wid;
				
				this.height += 12;
			}
		}
		
		
		
		boolean alignRight = ClientInfoHack.instance.alignment.currentMode.equalsIgnoreCase("Right");
		ScaledResolution scaledResolution = new ScaledResolution(Client.mc.gameSettings, Client.mc.displayWidth, Client.mc.displayHeight);
		
		if(ClientInfoHack.instance.staticPositon.currentMode.equalsIgnoreCase("Bottom Right")) {
			alignRight = true;
			this.xPos = scaledResolution.getScaledWidth() - this.width;
			this.yPos = scaledResolution.getScaledHeight() - this.height;
		}else if(ClientInfoHack.instance.staticPositon.currentMode.equalsIgnoreCase("Bottom Left")) {
			alignRight = false;
			this.xPos = 0;
			this.yPos = scaledResolution.getScaledHeight() - this.height;
		}else if(ClientInfoHack.instance.staticPositon.currentMode.equalsIgnoreCase("Top Right")) {
			alignRight = true;
			this.xPos = scaledResolution.getScaledWidth() - this.width;
			this.yPos = 0;
		}else if(ClientInfoHack.instance.staticPositon.currentMode.equalsIgnoreCase("Top Left")) {
			alignRight = false;
			this.xPos = 0;
			this.yPos = 0;
		}
		
		int rendX = this.xPos + 2; 
		
		if(!this.minimized) {
			if(first) {
				first = false;
			}else {
				boolean sav = false;
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
		this.renderFrame(this.xPos, this.yPos + 15, this.xPos + this.width, this.yPos + this.height);
		int i = 0;
		if(renderCoords) {
			if(alignRight) rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(coordX);
			Client.mc.fontRenderer.drawString(coordX, rendX, this.yPos + ++i*12 + 3 + 2, 0xffffff);
			if(alignRight) rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(coordY);
			Client.mc.fontRenderer.drawString(coordY, rendX, this.yPos + ++i*12 + 3 + 2, 0xffffff);
			if(alignRight) rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(coordZ);
			Client.mc.fontRenderer.drawString(coordZ, rendX, this.yPos + ++i*12 + 3 + 2, 0xffffff);
		}
		
		
		if(renderFacing) {
			if(alignRight) rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(facing);
			Client.mc.fontRenderer.drawString(facing, rendX, this.yPos + ++i*12 + 3 + 2, 0xffffff);
		}
		if(renderFPS) {
			if(alignRight) rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(fps);
			Client.mc.fontRenderer.drawString(fps, rendX, this.yPos + ++i*12 + 3 + 2, 0xffffff);
		}
		if(renderUsername) {
			if(alignRight) rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(username);
			Client.mc.fontRenderer.drawString(username, rendX, this.yPos + ++i*12 + 3 + 2, 0xffffff);
		}
		if(renderSpeed) {
			if(alignRight) rendX = (this.xPos + this.width) - Client.mc.fontRenderer.getStringWidth(walkingSpeed);
			Client.mc.fontRenderer.drawString(walkingSpeed, rendX, this.yPos + ++i*12 + 3 + 2, 0xffffff);
		}
	}
	
}
