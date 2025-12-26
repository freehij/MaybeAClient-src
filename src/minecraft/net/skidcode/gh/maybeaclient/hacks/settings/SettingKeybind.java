package net.skidcode.gh.maybeaclient.hacks.settings;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.NBTTagCompound;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.hacks.Hack;
import net.skidcode.gh.maybeaclient.utils.ChatColor;
import net.skidcode.gh.maybeaclient.utils.InputHandler;

public class SettingKeybind extends Setting implements InputHandler{
	
	public int value, initialValue;
	boolean activated = false;
	
	public SettingKeybind(Hack hack, String name, int initialValue) {
		super(hack, name);
		this.setValue(initialValue);
		this.initialValue = initialValue;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int d) {
		this.value = d;
	}
	
	@Override
	public String valueToString() {
		return Keyboard.getKeyName(this.value);
	}

	public boolean validateValue(String value) {
		try {
			Integer.parseInt(value); //TODO better value parsing
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void reset() {
		this.setValue(this.initialValue);
	}
	
	public int getSettingWidth() {
		return Client.mc.fontRenderer.getStringWidth(this.name + ": "+ (listening ? ChatColor.LIGHTGRAY+"Listening..." : this.valueToString())) + 10;
	}
	
	@Override
	public void onDeselect(Tab tab, int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
		this.listening = ClickGUI.setInputHandler(this);
	}
	
	@Override
	public void onKeyPress(int keycode) {
		if(keycode == Keyboard.KEY_ESCAPE || Keyboard.KEY_NONE == keycode) {
			this.value = 0;
		}else {
			this.value = keycode;
		}
		
		ClickGUI.setInputHandler(null);
		Client.saveModules();
	}
	
	@Override
	public void onInputFocusStop() {
		this.listening = false;
	}
	
	public void renderElement(Tab tab, int xStart, int yStart, int xEnd, int yEnd) {
		tab.renderFrameBackGround(xStart, yStart, xEnd, yEnd, 0, 0xaa / 255f, 0xaa / 255f, 1f);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound output) {
		output.setInteger(this.name, this.value);
	}
	
	boolean listening = false;
	public void renderText(int x, int y) {
		Client.mc.fontRenderer.drawString(this.name + ": "+ (listening ? ChatColor.LIGHTGRAY+"Listening..." : this.valueToString()), x + 2, y + 2, 0xffffff);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound input) {
		if(input.hasKey(this.name)) this.setValue(input.getInteger(this.name));
	}

	
}
