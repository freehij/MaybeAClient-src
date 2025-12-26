package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class FullBrightHack extends Hack{
	public static FullBrightHack INSTANCE;
	public FullBrightHack() {
		super("FullBright", "Makes the world brighter", Keyboard.KEY_C, Category.RENDER);
		INSTANCE = this;
	}
	public void toggle() {
		super.toggle();
		mc.entityRenderer.updateRenderer();
        mc.theWorld.markBlocksDirty((int)mc.thePlayer.posX - 256, 0, (int)mc.thePlayer.posZ - 256, (int)mc.thePlayer.posX + 256, 127, (int)mc.thePlayer.posZ + 256);
	}
}
