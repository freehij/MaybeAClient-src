package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class AutoScreenshotCopyHack extends Hack{
	public static AutoScreenshotCopyHack instance;
	public AutoScreenshotCopyHack() {
		super("AutoScreenshotCopy", "Automatically copy screenshot", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
	}

}
