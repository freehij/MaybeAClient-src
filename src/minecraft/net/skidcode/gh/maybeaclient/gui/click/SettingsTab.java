package net.skidcode.gh.maybeaclient.gui.click;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.Hack;
import net.skidcode.gh.maybeaclient.hacks.settings.Setting;

public class SettingsTab extends Tab{
	
	public Hack hack;
	public Tab parent;
	public int hackID;
	
	public Setting selected = null;
	public int selectedMinX = 0, selectedMinY = 0, selectedMaxX = 0, selectedMaxY = 0;
	public SettingsTab(Tab parent, Hack hack, int hackID) {
		super("");
		this.parent = parent;
		this.hack = hack;
		this.hackID = hackID;
	}
	
	@Override
	public boolean isPointInside(float x, float y) {
		if(this.parent.minimized) return false;
		return super.isPointInside(x, y);
	}
	public void onSelect(int click, int x, int y) {
		if(this.parent.minimized) return;
		int sx = (int)this.xPos;
		int sy = (int)this.yPos;
		for(Setting s : this.hack.settingsArr) {
			if(s.hidden) continue;
			int h = s.getSettingHeight(this);
			if(x >= sx && x <= (sx + this.width)) {
				if(y >= sy && y <= (sy + h)) {
					s.onPressedInside(sx + 1, sy + 1, sx + this.width - 1, sy + h - 1, x, y, click);
					this.selected = s;
					this.selectedMinX = sx + 1;
					this.selectedMinY = sy + 1;
					this.selectedMaxX = sx + this.width - 1;
					this.selectedMaxY = sy + h - 1;
					
					break;
				}
			}
			sy += h;
		}
	}
	public void renderIngame() {
		
	}
	public void mouseMovedSelected(int click, int x, int y) {
		if(this.parent.minimized) return;
		if(this.selected != null) {
			this.selected.onMouseMoved(this.selectedMinX, this.selectedMinY, this.selectedMinX + this.width, this.selectedMaxY, x, y, click);
		}
	}
	
	@Override
	public void onDeselect(int click, int x, int y) {
		if(this.parent.minimized) return;
		super.onDeselect(click, x, y);
		if(this.selected != null) {
			this.selected.onDeselect(this, this.selectedMinX, this.selectedMinY, this.selectedMinX + this.width, this.selectedMaxY, x, y, click);
			this.selected = null;
		}
		Client.saveModules();
	}
	public void render() {
		if(this.parent.minimized) return;
		this.xPos = this.parent.xPos + this.parent.width + 3;
		this.yPos = this.parent.yPos + this.hackID*12 + 14;
		
		int settingsHeight = 0;
		int settingsWidth = 60;
		
		for(Setting set : this.hack.settingsArr) {
			if(set.hidden) continue;
			int w = set.getSettingWidth();
			if(w > settingsWidth) settingsWidth = w;
		}
		this.width = settingsWidth;
		
		for(Setting set : this.hack.settingsArr) {
			if(set.hidden) continue;
			settingsHeight += set.getSettingHeight(this);
		}
		
		ScaledResolution scaledResolution = new ScaledResolution(Client.mc.gameSettings, Client.mc.displayWidth, Client.mc.displayHeight);
		if(this.yPos + settingsHeight > scaledResolution.getScaledHeight()) {
			this.yPos -= settingsHeight - 12;
		}
		this.height = settingsHeight;
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		this.renderFrameBackGround((int)this.xPos, (int)this.yPos, (int)this.xPos + settingsWidth, (int)this.yPos + settingsHeight);
		int height = (int)this.yPos;
		for(Setting set : this.hack.settingsArr) {
			if(set.hidden) continue;
			int sHeight = set.getSettingHeight(this);
			set.renderElement(this, (int)this.xPos + 1, height + 1, (int)this.xPos + settingsWidth - 1, height + sHeight - 1);
			height += sHeight;
		}
		

		
		this.renderFrameOutlines((int)this.xPos, (int)this.yPos, (int)this.xPos + settingsWidth, (int)this.yPos + settingsHeight);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		
		height = (int)this.yPos;
		for(Setting set : this.hack.settingsArr) {
			if(set.hidden) continue;
			int sHeight = set.getSettingHeight(this);
			set.renderText((int)this.xPos + 1, height + 1);
			height += sHeight;
		}
		
		
	}
}
