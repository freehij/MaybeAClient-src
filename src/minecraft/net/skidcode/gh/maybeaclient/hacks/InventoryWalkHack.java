package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class InventoryWalkHack extends Hack{
	public static InventoryWalkHack instance;
	public InventoryWalkHack() {
		super("InventoryWalk", "Allows you to walk while being in container gui", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
	}

}
