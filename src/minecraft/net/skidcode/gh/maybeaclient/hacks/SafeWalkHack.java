package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class SafeWalkHack extends Hack {
	
	public static SafeWalkHack instance;
	
	public SafeWalkHack() {
		super("SafeWalk", "Players will not fall from blocks", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
	}

}
