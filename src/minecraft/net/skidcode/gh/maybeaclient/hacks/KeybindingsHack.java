package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventRenderIngameNoDebug;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.KeybindingsTab;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingMode;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class KeybindingsHack extends Hack{
	
	public SettingMode alignment = new SettingMode(this, "Alignment", "Left", "Right");
	public SettingMode expand = new SettingMode(this, "Expand", "Bottom", "Top");
	public static KeybindingsHack instance;
	public KeybindingsHack() {
		super("Keybindings", "Show binds in hud", Keyboard.KEY_NONE, Category.UI);
		
		this.addSetting(this.alignment);
		this.addSetting(this.expand);
		instance = this;
	}
}
