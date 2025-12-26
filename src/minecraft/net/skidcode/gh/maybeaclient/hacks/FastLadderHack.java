package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingFloat;

public class FastLadderHack extends Hack{
	public static FastLadderHack instance;
	
	public SettingFloat upwardSpeed = new SettingFloat(this, "Upward speed", 1, 0.2f, 2, 0.05f);
	public SettingFloat downwardSpeed = new SettingFloat(this, "Downward speed", 1, 0.15f, 2, 0.05f);
	
	public FastLadderHack() {
		super("FastLadder", "Allows to quiclky climb ladder", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
		
		this.addSetting(this.upwardSpeed);
		this.addSetting(this.downwardSpeed);
	}

}
