package net.skidcode.gh.maybeaclient.hacks.settings;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.hacks.Hack;

public class SettingBlockChooser extends Setting{
	public boolean blocks[];
	public int ids[];
	public static final int colsMax = 13;
	public int width, height;
	
	public boolean minimized = false;
	
	public SettingBlockChooser(Hack hack, String name, int... ids) {
		super(hack, name);
		blocks = new boolean[Block.blocksList.length];
		for(int id : ids) blocks[id] = true;
		this.ids = ids;
		
		int id = 1;
		int drawn = 0;
		int hei = 0;
		int wid = 0;
		while(id < 256) {
			Block b = Block.blocksList[id];
			if(b != null) {
				wid = drawn*18;
				++drawn;
			}
			if(wid > this.width) this.width = wid;
			
			if(drawn >= colsMax) {
				drawn = 0;
				wid = 0;
				hei += 18;
			}
			++id;
		}
		this.width += 18;
		this.height = hei+18;
	}
	
	public void blockChanged(int id) {
		
	}
	
	@Override
	public String valueToString() {
		String s = "";
		for(int i = 0; i < blocks.length; ++i) {
			if(blocks[i]) s += i+", ";
		}
		
		return s.substring(0, s.length()-2);
	}

	@Override
	public void reset() {
		for(int i = 0; i < blocks.length; ++i) blocks[i] = false;
		for(int id : this.ids) blocks[id] = true;
	}

	@Override
	public boolean validateValue(String value) {
		try{
			Integer.parseInt(value);
			return true;
		}catch(NumberFormatException e) {
		}
		return false;
	}
	
	@Override
	public void renderElement(Tab tab, int xStart, int yStart, int xEnd, int yEnd) {
		if(this.minimized) return;
		tab.renderFrameBackGround(xStart, yStart, xEnd, yStart + 10, 0, 0xaa / 255f, 0xaa / 255f, 1f);
		yStart += 12;
		GL11.glPushMatrix();
		int id = 1;
		int drawn = 0;
		int yOff = 0;
		while(id < 256) {
			Block b = Block.blocksList[id];
			if(b != null) {
				GL11.glDisable(2896 /*GL_LIGHTING*/);
				
				if(this.blocks[id]) {
					int xb = xStart + drawn*18;
					int yb = yStart + yOff;
					tab.renderFrameBackGround(xb, yb, xb+16, yb+16, 0, 0xaa / 255f, 0xaa / 255f, 1f);
				}
				++drawn;
			}
			if(drawn >= colsMax) {
				drawn = 0;
				yOff += 18;
			}
			++id;
		}
		GL11.glPopMatrix();
	}
	public boolean minPressd = false;
	public int xoff = -1;
	public int yoff = -1;
	@Override
	public void onDeselect(Tab tab, int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
		xoff = yoff = -1;
		this.minPressd = false;
	}
	@Override
	public void onPressedInside(int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
		
		if(mouseY > yMin && mouseY < (yMin+12) && mouseX > xMin && mouseY < xMax) {
			if(!this.minPressd) {
				this.minimized = !this.minimized;
				this.minPressd = true;
			}
			return;
		}
		
		xMin += 2;
		yMin += 2 + 12;
		
		int col = (mouseX-xMin) / 18;
		int row = (mouseY-yMin) / 18;
		int offX = (mouseX-xMin) % 18;
		
		int id = 1;
		int drawn = 0;
		int yOff = 0;
		if(xoff == -1 && yoff == -1 && offX < 14) {
			while(id < 256) {
				Block b = Block.blocksList[id];
				if(b != null) {
					if(yOff == row && col == drawn) {
						this.blocks[id] = !this.blocks[id];
						this.blockChanged(id);
						xoff = drawn;
						yoff = row;
						break;
					}
					++drawn;
				}
				if(drawn >= colsMax) {
					drawn = 0;
					++yOff;
				}
				++id;
			}
		}
	}
	@Override
	public void renderText(int x, int y) {
		Client.mc.fontRenderer.drawString(this.name, x + 2, y + 2, 0xffffff);
		if(this.minimized) return;
		y += 12;
		GL11.glPushMatrix();
		int id = 1;
		int drawn = 0;
		int yOff = 0;
		while(id < 256) {
			Block b = Block.blocksList[id];
			if(b != null) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GuiIngame.itemRenderer.renderItemIntoGUI(Client.mc.fontRenderer, Client.mc.renderEngine, new ItemStack(b), x + drawn*18, y + yOff);
				++drawn;
			}
			if(drawn >= colsMax) {
				drawn = 0;
				yOff += 18;
			}
			++id;
		}
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound output) {
		byte[] bs = new byte[this.blocks.length];
		for(int i = 0; i < bs.length; ++i) bs[i] = (byte) (this.blocks[i] ? 1 : 0);
		NBTTagCompound tg = new NBTTagCompound();
		tg.setByteArray("Blocks", bs);
		tg.setBoolean("Minimized", this.minimized);
		output.setCompoundTag(this.name, tg);
	}

	@Override
	public void readFromNBT(NBTTagCompound input) {
		NBTTagCompound tg = input.getCompoundTag(this.name);
		byte[] bts = tg.getByteArray("Blocks");
		for(int i = 0; i < bts.length; ++i) {
			this.blocks[i] = bts[i] != 0;
		}
		
		this.minimized = tg.getBoolean("Minimized");
	}
	
	public int getSettingWidth() {
		return this.width;
	}
	
	public int getSettingHeight() {
		if(this.minimized) return 12;
		return this.height + 12;
	}
}
