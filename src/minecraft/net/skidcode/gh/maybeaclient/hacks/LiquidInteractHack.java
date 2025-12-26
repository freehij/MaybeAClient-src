package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class LiquidInteractHack extends Hack{
	
	public static LiquidInteractHack instance;
	
	public LiquidInteractHack() {
		super("LiquidInteract", "Allows you to interact with liquids", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
	}
}
