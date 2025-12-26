package net.skidcode.gh.maybeaclient.hacks;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.Setting;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingKeybind;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class Hack {
	public String name;
	public String description;
	public SettingKeybind keybinding;
	public boolean status = false;
	public boolean hasSettings = false;
	public boolean expanded = false;
	public HashMap<String, Setting> settings = new HashMap<>();
	public ArrayList<Setting> settingsArr = new ArrayList<>(); 
	public int hiddens = 0;
	public static Minecraft mc;
	public Category category;
	public Tab tab = null;
	
	
	public Hack(String name, String description, int keybind, Category category) {
		this.name = name;
		this.description = description;
		this.keybinding = new SettingKeybind(this, "Keybind", keybind);
		this.category = category;
		category.hacks.add(this);
		this.addSetting(this.keybinding);
	}
	
	public void addSetting(Setting setting) {
		this.settingsArr.add(setting);
		this.settings.put(setting.noWhitespacesName.toLowerCase(), setting);
		this.hasSettings = true;
	}
	
	public void bind(int key) {
		this.keybinding.setValue(key);
		Client.addMessage(
				"Module "+ChatColor.GOLD+this.name+ChatColor.WHITE+" is now binded to "+
				ChatColor.GOLD+this.keybinding.valueToString()+ChatColor.WHITE+
				"("+ChatColor.GOLD+this.keybinding.value+ChatColor.WHITE+")"
		);
		Client.saveModules();
	}
	
	public String getNameForArrayList() {
		return this.name;
	}
	
	public void toggle() {
		this.status = !this.status;
		
		if(this.status) {
			this.onEnable();
		}else {
			this.onDisable();
		}
		Client.saveModules();
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
}
