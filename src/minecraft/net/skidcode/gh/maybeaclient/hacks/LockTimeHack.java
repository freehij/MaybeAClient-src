package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingLong;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class LockTimeHack extends Hack{
	public SettingLong lockedTime = new SettingLong(this, "Time", 0, 0, 24000);
	
	public static LockTimeHack INSTANCE;
	
	public LockTimeHack() {
		super("LockTime", "Lock time on specific value.", Keyboard.KEY_NONE, Category.MISC);
		INSTANCE = this;
		this.addSetting(this.lockedTime);
	}
	
	@Override
	public String getNameForArrayList() {
		String s = "[";
		s += ChatColor.LIGHTCYAN;
		s += this.lockedTime.value;
		s += ChatColor.WHITE;
		s += "]";
		
		return this.name + s;
	}
}
