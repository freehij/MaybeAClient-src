package net.skidcode.gh.maybeaclient.hacks.settings;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.FontAllowedCharacters;
import net.minecraft.src.NBTTagCompound;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.hacks.Hack;
import net.skidcode.gh.maybeaclient.utils.InputHandler;

public class SettingTextBox extends Setting implements InputHandler{

	public String initial, value;
	public int maxTextboxWidth = -1;
	public boolean isEditing = false;
	public SettingTextBox(Hack hack, String name, String value) {
		super(hack, name);
		this.initial = value;
		this.setValue(value);
	}
	
	public SettingTextBox(Hack hack, String name, String value, int maxTBWidth) {
		super(hack, name);
		this.initial = value;
		this.setValue(value);
		this.maxTextboxWidth = maxTBWidth;
	}

	@Override
	public String valueToString() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public void reset() {
		this.setValue(this.initial);
	}

	@Override
	public boolean validateValue(String value) {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound output) {
		output.setString(this.name, this.value);
	}

	@Override
	public void readFromNBT(NBTTagCompound input) {
		if(input.hasKey(this.name)) this.setValue(input.getString(this.name));
	}
	
	public long time = 0;
	public String add = "";
	@Override
	public void renderText(int x, int y) {
		long time = System.currentTimeMillis();
		if(time - this.time > 500) {
			this.time = time;
			add = add.equals("_") ? "" : "_";
		}
		if(!this.isEditing) add = "";
		String s = this.name + ": ";
		int maxy = (y + this.splittedHeight);
		int miny = y;
		Client.mc.fontRenderer.drawString(s, x + 2, y + (maxy - miny) / 2 - 5, 0xffffff);
		Client.mc.fontRenderer.drawSplittedString(this.value+add, x + 2 + Client.mc.fontRenderer.getStringWidth(s), y + 2, this.isEditing ? 0xafafaf : 0xffffff, this.maxSplitWidth, 12);
	}
	@Override
	public void onPressedInside(int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
		int tbxmin = xMin + Client.mc.fontRenderer.getStringWidth(this.name + ": ");
		int tbymin = yMin;
		int tbxmax = xMax;
		int tbymax = yMax;
		
		if(mouseX >= tbxmin && mouseX <= tbxmax && mouseY >= tbymin && mouseY <= tbymax) {
			this.isEditing = ClickGUI.setInputHandler(this);
		}
	}
	
	@Override
	public void renderElement(Tab tab, int xStart, int yStart, int xEnd, int yEnd) {
		int tbxs = xStart + Client.mc.fontRenderer.getStringWidth(this.name + ": ");
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glStencilOp(GL11.GL_ZERO, GL11.GL_ZERO, GL11.GL_REPLACE);  
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glStencilFunc(GL11.GL_ALWAYS, Client.STENCIL_REF_ELDRAW, 0xFF);
		GL11.glStencilMask(0xFF);
		tab.renderFrameOutlines(tbxs, yStart, xEnd, yEnd);
		GL11.glColorMask(false, false, false, false);
		tab.renderFrameBackGround(tbxs, yStart, xEnd, yEnd, 0, 0xaa / 255f, 0xaa / 255f, 1f);
		GL11.glColorMask(true, true, true, true);
		GL11.glStencilOp(GL11.GL_ZERO, GL11.GL_ZERO, GL11.GL_ZERO);
		GL11.glStencilFunc(GL11.GL_NOTEQUAL, Client.STENCIL_REF_ELDRAW, 0xFF);
		tab.renderFrameBackGround(xStart, yStart, xEnd, yEnd, 0, 0xaa / 255f, 0xaa / 255f, 1f);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
	@Override
	public int getSettingWidth() {
		if(this.maxTextboxWidth >= 0) return Client.mc.fontRenderer.getStringWidth(this.name + ":" + "_") + this.maxTextboxWidth;
		return Client.mc.fontRenderer.getStringWidth(this.name + ":" + this.value + "_") + 10;
	}

	@Override
	public void onKeyPress(int keycode) {
		boolean b = Keyboard.getEventKeyState();
		if(!b) return;
		if(keycode == Keyboard.KEY_ESCAPE) {
			ClickGUI.setInputHandler(null);
			return;
		}
		if(keycode == Keyboard.KEY_BACK) {
			if(this.value.length() > 0) this.setValue(this.value.substring(0, this.value.length()-1));
			return;
		}
		char c = Keyboard.getEventCharacter();
		
		if(FontAllowedCharacters.allowedCharacters.indexOf(c) >= 0) this.setValue(this.value + c);
	}

	@Override
	public void onInputFocusStop() {
		this.isEditing = false;
		Client.saveModules();
	}
	
	public int maxSplitWidth = this.maxTextboxWidth;
	public int splittedHeight = 12;
	@Override
	public int getSettingHeight(Tab tab) {
		//return 24;
		if(this.maxTextboxWidth < 0) return super.getSettingHeight(tab);
		int a = tab.width - Client.mc.fontRenderer.getStringWidth(this.name + ":" + "_");
		int b = this.maxTextboxWidth;
		if(b > a) a = b;
		this.maxSplitWidth = a;
		int[] sz = Client.mc.fontRenderer.getSplittedStringWidthAndHeight(this.value+"_", a, 12);
		this.splittedHeight = sz[1];
		return sz[1];
	}
	
}
