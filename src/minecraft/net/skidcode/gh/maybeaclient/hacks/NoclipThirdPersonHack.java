package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class NoclipThirdPersonHack extends Hack{

	public static NoclipThirdPersonHack instance;

	public NoclipThirdPersonHack() {
		super("NoClipThirdPerson", "Allows seeing through blocks using third person", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}

}
