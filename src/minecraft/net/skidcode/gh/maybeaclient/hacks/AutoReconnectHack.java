package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingInteger;

public class AutoReconnectHack extends Hack{
	public static AutoReconnectHack instance;
	public SettingInteger delaySeconds = new SettingInteger(this, "Delay(Seconds)", 5, 1, 10);
	public AutoReconnectHack() {
		super("AutoReconnect", "Automatically reconnects to server", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
		this.addSetting(this.delaySeconds);
	}

}
