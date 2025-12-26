package net.skidcode.gh.maybeaclient.hacks.settings;

import java.util.HashMap;

import net.minecraft.src.NBTTagCompound;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.hacks.Hack;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class SettingChooser extends Setting{
	
	public HashMap<String, Boolean> value = new HashMap<>();
	public String[] choices;
	public boolean[] initial;
	public boolean minimized = false;
	
	public SettingChooser(Hack hack, String name, String[] choices, boolean[] initial) {
		super(hack, name);
		if(choices.length != initial.length) throw new RuntimeException("Lengths of choices and initial are different!");
		
		this.initial = initial;
		this.choices = choices;
		this.setValue(initial);
	}
	
	public void setValue(boolean[] values) {
		for(int i = 0; i < this.choices.length; ++i) {
			this.setValue(this.choices[i], values[i]);
		}
	}
	
	public boolean getValue(String key) {
		return this.value.get(key.toLowerCase());
	}
	
	public void setValue(String name, boolean value) {
		this.value.put(name.toLowerCase(), value);
	}
	@Override
	public String valueToString() {
		String s = "";
		for(int i = 0; i < this.choices.length; ++i) {
			String m = this.choices[i];
			
			s += this.getValue(m) ? ChatColor.LIGHTGREEN : ChatColor.LIGHTRED;
			s += m;
			s += ChatColor.WHITE;
			s += ";";
		}
		return s.substring(0, s.length()-1);
	}
	public String valueToStringConsole() {
		String s = ChatColor.WHITE+"";
		for(int i = 0; i < this.choices.length; ++i) {
			String m = this.choices[i];
			
			s += this.getValue(m) ? ChatColor.LIGHTGREEN : ChatColor.LIGHTRED;
			s += m;
			s += ChatColor.WHITE;
			s += ";";
		}
		
		return s.substring(0, s.length()-1);
	}
	@Override
	public void reset() {
		this.setValue(this.initial);
	}

	@Override
	public boolean validateValue(String value) {
		
		String[] splitted = value.split(";");
		
		for(String s : splitted) {
			if(!this.value.containsKey(s.toLowerCase())) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound output) {
		NBTTagCompound val = new NBTTagCompound();
		for(int i = 0; i < this.choices.length; ++i) {
			val.setBoolean(this.choices[i], this.value.get(this.choices[i].toLowerCase()));
		}
		val.setBoolean("Minimized", this.minimized);
		output.setCompoundTag(this.name, val);
	}

	@Override
	public void readFromNBT(NBTTagCompound input) {
		NBTTagCompound val = input.getCompoundTag(this.name);
		
		if(!val.tagMap.isEmpty()) {
			this.minimized = val.getBoolean("Minimized");
			for(int i = 0; i < this.choices.length; ++i) {
				this.setValue(this.choices[i], val.getBoolean(this.choices[i]));
			}
		}
	}
	@Override
	public int getSettingWidth() {
		int wid = super.getSettingWidth();
		
		for(int i = 0; i < this.choices.length; ++i) {
			int wid2 = Client.mc.fontRenderer.getStringWidth(this.choices[i]) + 4;
			if(wid2 > wid) wid = wid2;
		}
		
		return wid;
	}
	
	@Override
	public void renderElement(Tab tab, int xStart, int yStart, int xEnd, int yEnd) {
		if(this.minimized) return;
		tab.renderFrameBackGround(xStart, yStart, xEnd, yStart + 10, 0, 0xaa / 255f, 0xaa / 255f, 1f);
		
		yStart += 11;
		
		for(int i = 0; i < this.choices.length; ++i) {
			if(this.getValue(this.choices[i])) {
				tab.renderFrameBackGround(xStart, yStart + 1, xEnd, yStart + 12 - 1, 0, 0xaa / 255f, 0xaa / 255f, 1f);
			}
			yStart += 12;
		}
		
	}
	public int lastPressed = -1;
	@Override
	public void onPressedInside(int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
		if(this.lastPressed != -1) return;
		int diff = mouseY - yMin;
		int md = diff / 12;
		if(md > 0) {
			md -= 1;
			this.setValue(this.choices[md], !this.getValue(this.choices[md]));
		}else if(md == 0){
			this.minimized = !this.minimized;
		}else {
			return;
		}
		
		this.lastPressed = md;
	}
	
	@Override
	public void onDeselect(Tab tab, int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
		this.lastPressed = -1;
	}
	public void renderText(int x, int y) {
		Client.mc.fontRenderer.drawString(this.name, x + 2, y + 2, 0xffffff);
		if(this.minimized) return;
		
		int rx = x + 2 + 4;
		int ry = y + 12;
		
		for(int i = 0; i < this.choices.length; ++i){
			Client.mc.fontRenderer.drawString(this.choices[i], rx + 2, ry + i*12 + 2, 0xffffff);
		}
		
	}
	
	public int getSettingHeight() {
		if(this.minimized) return 12;
		return 12*this.choices.length + 12;
	}
}
