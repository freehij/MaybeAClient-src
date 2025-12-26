package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingFloat;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class SpeedMineHack extends Hack{

	public SettingFloat sendDestroyAfter = new SettingFloat(this, "Destroy After", 0.7f, 0, 1);
	public static SpeedMineHack instance;
	
	public SpeedMineHack() {
		super("SpeedMine", "Allows to mine faster", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
		
		this.addSetting(this.sendDestroyAfter);
	}
	
	@Override
	public String getNameForArrayList() {
		String s = "[";
		s += ChatColor.LIGHTCYAN;
		s += this.sendDestroyAfter.value;
		s += ChatColor.WHITE;
		s += "]";
		
		return this.name + s;
	}
	
}
