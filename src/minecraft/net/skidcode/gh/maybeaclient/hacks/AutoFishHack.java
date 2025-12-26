package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class AutoFishHack extends Hack{
	public static AutoFishHack instance;
	public AutoFishHack() {
		super("AutoFish", "Automatically fishes", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
	}

}
