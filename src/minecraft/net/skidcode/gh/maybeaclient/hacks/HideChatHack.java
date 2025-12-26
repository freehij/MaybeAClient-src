package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingMode;

public class HideChatHack extends Hack{
	public static HideChatHack instance;
	public SettingMode mode = new SettingMode(this, "Mode", "HideExceptChatGui", "Always");
	public HideChatHack() {
		super("HideChat", "Hides chat", Keyboard.KEY_NONE, Category.UI);
		this.addSetting(this.mode);
		instance = this;
	}
}
