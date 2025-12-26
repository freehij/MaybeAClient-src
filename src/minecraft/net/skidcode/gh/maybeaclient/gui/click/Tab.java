package net.skidcode.gh.maybeaclient.gui.click;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.Client;

public abstract class Tab {
	
	public String name;
	public int width = 0;
	public int height = 0;
	public int xPos = 0;
	public int yPos = 0;
	public int heightPrev = 12;
	public int selectedMouseX = 0;
	public int selectedMouseY = 0;
	public boolean dragging = false;
	public boolean waitsForInput = false;
	
	public boolean minimized = false;
	public boolean shown = true;
	
	public int xDefPos = 0;
	public int yDefPos = 0;
	
	
	public Tab(String name) {
		this.name = name;
	}
	
	public Tab(String name, int width, int height) {
		this(name);
		this.width = width;
		this.height = height;
	}
	
	public void onSelect(int click, int x, int y) {
		if(click == 0 && y <= this.yPos + 12) {
			this.selectedMouseX = x;
			this.selectedMouseY = y;
			this.dragging = true;
		}
	}
	
	public void minimize() {
		this.heightPrev = this.height;
		this.height = 12;
	}
	
	public void maximize() {
		this.height = this.heightPrev;
	}
	
	public void onDeselect(int click, int x, int y) {
		
		if(click == 1 && y <= this.yPos + 12) {
			this.minimized = !this.minimized;
			if(this.minimized) this.minimize();
			else this.maximize();
		}
		
		this.dragging = false;
		Client.saveClickGUI();
	}
	
	public void mouseMovedSelected(int click, int x, int y) {
		if(this.dragging) {
			this.xPos += x-this.selectedMouseX;
			this.yPos += y-this.selectedMouseY;
			if(this.xPos < 0) this.xPos = 0;
			if(this.yPos < 0) this.yPos = 0;
			this.selectedMouseX = x;
			this.selectedMouseY = y;
		}
	}
	public void renderFrameOutlines(int xStart, int yStart, int xEnd, int yEnd, float r, float g, float b, float a) {
		this.renderFrameOutlines(xStart, yStart, xEnd, yEnd, r, g, b, a, 2.5f);
	}
	public void renderFrameOutlines(int xStart, int yStart, int xEnd, int yEnd, float r, float g, float b, float a, float lineWidth) {
		Tessellator tess = Tessellator.instance;
		
		GL11.glColor4f(r, g, b, a);
		GL11.glLineWidth(lineWidth);
		
		tess.startDrawingQuads();
		tess.addVertex(xStart-1, yStart, 0);
		tess.addVertex(xStart-1, yEnd, 0);
		tess.addVertex(xStart, yEnd, 0);
		tess.addVertex(xStart, yStart, 0);
		
		tess.addVertex(xStart, yEnd+1, 0);
		tess.addVertex(xEnd, yEnd+1, 0);
		tess.addVertex(xEnd, yEnd, 0);
		tess.addVertex(xStart, yEnd, 0);
		
		tess.addVertex(xEnd, yStart, 0);
		tess.addVertex(xEnd, yEnd, 0);
		tess.addVertex(xEnd+1, yEnd, 0);
		tess.addVertex(xEnd+1, yStart, 0);
		
		tess.addVertex(xStart, yStart, 0);
		tess.addVertex(xEnd, yStart, 0);
		tess.addVertex(xEnd, yStart-1, 0);
		tess.addVertex(xStart, yStart-1, 0);
		tess.draw();
		
		//tess.startDrawingQuads();
		//
		//tess.draw();
		
		
		
		//tess.startDrawing(3);
		//tess.addVertex(xStart+1, yEnd, 0);
		//tess.addVertex(xEnd-1, yEnd, 0);
		//tess.draw();
		
		/*tess.startDrawing(3);
		tess.addVertex(xStart+1, yEnd, 0);
		tess.addVertex(xEnd-1, yEnd, 0);
		tess.draw();
		
		tess.startDrawing(3);
		tess.addVertex(xEnd, yEnd - 1, 0);
		tess.addVertex(xEnd, yStart + 1, 0);
		tess.draw();
		
		tess.startDrawing(3);
		tess.addVertex(xEnd - 1, yStart, 0);
		tess.addVertex(xStart + 1, yStart, 0);
		tess.draw();*/
		
		//tess.addVertex(xStart, yStart+1, 0);
		
	}
	
	public boolean isPointInside(float x, float y) {
		return x >= this.xPos && x <= (this.xPos + this.width) && y >= this.yPos && y <= (this.yPos + this.height);
	}
	public void renderFrameOutlines(int xStart, int yStart, int xEnd, int yEnd, float width) {
		this.renderFrameOutlines(xStart, yStart, xEnd, yEnd, 0, 0, 0, 0.9f, width);
	}
	
	public void renderFrameOutlines(int xStart, int yStart, int xEnd, int yEnd) {
		this.renderFrameOutlines(xStart, yStart, xEnd, yEnd, 0, 0, 0, 0.9f);
	}
	
	public void renderFrameBackGround(int xStart, int yStart, int xEnd, int yEnd) {
		this.renderFrameBackGround(xStart, yStart, xEnd, yEnd, 0, 0, 0, 0.5f);
	}
	public void renderFrameBackGround(int xStart, int yStart, int xEnd, int yEnd, float r, float g, float b, float a) {
		Tessellator tess = Tessellator.instance;
		GL11.glColor4f(r, g, b, a);
		tess.startDrawingQuads();
		tess.addVertex(xStart, yStart, 0);
		tess.addVertex(xStart, yEnd, 0);
		tess.addVertex(xEnd, yEnd, 0);
		tess.addVertex(xEnd, yStart, 0);
		tess.addVertex(xStart, yStart, 0);
		
		/*tess.addVertex(xStart, yStart+1, 0);
		tess.addVertex(xStart, yEnd-1, 0);
		tess.addVertex(xStart+1, yEnd, 0);
		tess.addVertex(xEnd-1, yEnd, 0);
		tess.draw();
		
		tess.startDrawingQuads();
		tess.addVertex(xEnd, yEnd-1, 0);
		tess.addVertex(xEnd, yStart+1, 0);
		tess.addVertex(xEnd-1, yStart, 0);
		tess.addVertex(xStart+1, yStart, 0);
		tess.draw();
		
		tess.startDrawingQuads();
		tess.addVertex(xEnd, yEnd-1, 0);
		tess.addVertex(xStart+1, yStart, 0);
		tess.addVertex(xStart, yStart+1, 0);
		tess.addVertex(xEnd-1, yEnd, 0);*/
		tess.draw();
		
	}
	
	public void renderFrame(int xStart, int yStart, int xEnd, int yEnd) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		
		this.renderFrameBackGround(xStart, yStart, xEnd, yEnd);
		this.renderFrameOutlines(xStart, yStart, xEnd, yEnd);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	
	
	public void renderName() {
		int xStart = this.xPos;
		int yStart = this.yPos;
		
		this.renderFrame(xStart, yStart, xStart + this.width, yStart + 12);
		
		Client.mc.fontRenderer.drawString(this.name, xStart + 2, yStart + 2, 0xffffff);
	}
	
	public void renderMinimized() {
		this.height = 12;
		this.renderName();
	}
	
	public void renderIngame() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.render();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void render() {
		this.renderName();
	}
	public void readFromNBT(NBTTagCompound tag) {
		NBTTagCompound comp = tag.getCompoundTag("Position");
		this.xPos = comp.getInteger("xPos");
		this.yPos = comp.getInteger("yPos");
		this.minimized = comp.getBoolean("Minimized");
		if(this.minimized) this.minimize();
		else this.maximize();
	}
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagCompound comp = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		comp.setInteger("xPos", this.xPos);
		comp.setInteger("yPos", this.yPos);
		comp.setBoolean("Minimized", this.minimized);
		tag.setCompoundTag("Position", comp);
		tag.setInteger("Priority", ClickGUI.tabs.indexOf(this));
	}

	public void mouseHovered(int x, int y, int click) {
		
	}

	public void stopHovering() {
	}

	public void wheelMoved(int wheel, int x, int y) {
	}
}
