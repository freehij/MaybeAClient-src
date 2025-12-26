package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class InstantHack extends Hack{
	
	public static InstantHack instance;
	
	public InstantHack() {
		super("Instant", "Instantmine", Keyboard.KEY_NONE, Category.MISC);
		
		instance = this;
	}

}
