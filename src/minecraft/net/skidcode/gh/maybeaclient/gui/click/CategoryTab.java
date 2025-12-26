package net.skidcode.gh.maybeaclient.gui.click;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.Hack;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.Setting;

public class CategoryTab extends Tab {
	public Category category;
	public boolean resize = true;
	public CategoryTab(Category category) {
		super(category.name);
		this.category = category;
	}
	
	public CategoryTab(Category category, int x, int y, int width) {
		this(category);
		this.xPos = x;
		this.yPos = y;
		this.xDefPos = x;
		this.yDefPos = y;
		
		this.width = width;
	}
	
	int hoveringOver = -1;
	long hoverStart = -1;
	int hoverX, hoverY;
	boolean onHoverRender = false;
	@Override
	public void stopHovering() {
		this.hoveringOver = -1;
		this.hoverStart = -1;
		this.onHoverRender = false;
	}
	public void renderIngame() {
		
	}
	@Override
	public void mouseHovered(int x, int y, int click) {
		if(!this.minimized && y > (this.yPos + 12 + 2)) {
			
			int haxcnt = (this.category.hacks.size()-1);
			int sel = haxcnt - ((int)this.yPos+this.height - y)/12;
			
			if(hoveringOver == sel) {
				if(System.currentTimeMillis() - hoverStart > 4*1000 && !this.onHoverRender) {
					this.onHoverRender = true;
					this.hoverX = x;
					this.hoverY = y;
				}
			}else {
				hoveringOver = sel;
				hoverStart = System.currentTimeMillis();
				this.onHoverRender = false;
			}
		}
	}
	
	boolean canToggle = true;
	public void onSelect(int click, int x, int y) {
		
		if(!this.minimized && y > (this.yPos + 12 + 2) && this.canToggle) {
			
			int haxcnt = (this.category.hacks.size()-1);
			int sel = haxcnt - ((int)this.yPos+this.height - y)/12;
			Hack hacc = this.category.hacks.get(sel);
			if(click == 0) hacc.toggle();
			else if(click == 1) {
				hacc.expanded = !hacc.expanded;
				
				if(hacc.expanded) {
					hacc.tab = new SettingsTab(this, hacc, sel);
					ClickGUI.tabs.add(0, hacc.tab);
				}else {
					ClickGUI.tabs.remove(hacc.tab);
					hacc.tab = null;
				}
				
			}
			this.canToggle = false;
		}
		
		super.onSelect(click, x, y);
	}
	public void onDeselect(int click, int x, int y) {
		this.canToggle = true;
		
		super.onDeselect(click, x, y);
	}
	public void renderModules() {
		Tessellator tess = Tessellator.instance;
		int xStart = (int)this.xPos;
		int yStart = (int)this.yPos + 12 + 3;
		int xEnd = (int)this.xPos + this.width;
		int yEnd = (int)this.yPos + this.height;
		
		this.height = category.hacks.size()*12 + 14;
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		
		
		this.renderFrameBackGround(xStart, yStart, xEnd, yEnd);
		
		for(int i = 0; i < category.hacks.size(); ++i) {
			Hack h = this.category.hacks.get(i);
			if(h.status) {
				this.renderFrameBackGround(xStart, yStart + 12*i, xEnd, yStart + 12*i + 12, 0, 0xaa / 255f, 0xaa / 255f, 1f);
			}
		}
		
		this.renderFrameOutlines(xStart, yStart, xEnd, yEnd);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		
		for(int i = 0; i < category.hacks.size(); ++i) {
			Client.mc.fontRenderer.drawString(this.category.hacks.get(i).name, (int)xStart + 2, (int)yStart + 2 + 12*i , 0xffffff);
		}
	}
	
	public void render() {
		if(this.minimized) {
			this.renderMinimized();
			return;
		}
		
		if(this.resize) {
			for(int i = 0; i < category.hacks.size(); ++i) {
				int width = Client.mc.fontRenderer.getStringWidth(this.category.hacks.get(i).name) + 2;
				if(width > this.width) this.width = width;
			}
			this.resize = false;
		}
		
		super.render();
		this.renderModules();
		
		/*if(this.onHoverRender) {
			int xMin = this.hoverX;
			int yMin = this.hoverY;
			int xMax = this.hoverX + 6*24;
			
			int[] width_y = Client.mc.fontRenderer.getSplittedStringWidthAndHeight(this.category.hacks.get(this.hoveringOver).description, xMax - xMin, 12);
			
			xMax = this.hoverX + width_y[0] + 2;
			int yMax = this.hoverY + width_y[1] + 12;
			this.renderFrame(xMin, yMin, xMax, yMax);
			Client.mc.fontRenderer.drawSplittedString(this.category.hacks.get(this.hoveringOver).description, xMin + 2, yMin + 2, 0xffffff, xMax - xMin, 12);
		}*/
	}
}
