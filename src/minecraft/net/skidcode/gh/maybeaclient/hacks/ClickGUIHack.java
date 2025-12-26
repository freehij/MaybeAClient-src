package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingMode;

public class ClickGUIHack extends Hack{
	public static ClickGUIHack instance; 
	public SettingMode guiScale = new SettingMode(this, "Gui Scale", "Game", "Small", "Normal", "Large");
	public ClickGUIHack() {
		super("ClickGUI", "Open ClickGUI", Keyboard.KEY_UP, Category.UI);
		instance = this;
		this.addSetting(this.guiScale);
	}
	
	public int getScale() {
		if(this.guiScale.currentMode.equalsIgnoreCase("Large")) return 3;
		if(this.guiScale.currentMode.equalsIgnoreCase("Normal")) return 2;
		if(this.guiScale.currentMode.equalsIgnoreCase("Small")) return 1;
		return mc.gameSettings.guiScale;
	}
	
	public void onEnable() {
		mc.displayGuiScreen(new ClickGUI(mc.currentScreen));
	}
	
	public void onDisable() {
		if(mc.currentScreen instanceof ClickGUI) {
			mc.displayGuiScreen(((ClickGUI)mc.currentScreen).parent);
		}
		
	}
}
