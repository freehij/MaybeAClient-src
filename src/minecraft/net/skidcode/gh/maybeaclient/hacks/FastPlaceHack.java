package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class FastPlaceHack extends Hack{
	public static FastPlaceHack instance;
	
	public FastPlaceHack() {
		super("FastPlace", "Removes delay between block placement", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
	}

}
