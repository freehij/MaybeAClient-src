package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.RenderManager;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventRenderIngameNoDebug;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.PlayerViewTab;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBoolean;

public class PlayerViewHack extends Hack{
	
	public static PlayerViewHack instance;
	public SettingBoolean dontRotate = new SettingBoolean(this, "Disable Roatation", true);
	
	public PlayerViewHack() {
		super("PlayerView", "Enables player view ingame", Keyboard.KEY_NONE, Category.UI);
		instance = this;
		this.addSetting(this.dontRotate);
	}
}
