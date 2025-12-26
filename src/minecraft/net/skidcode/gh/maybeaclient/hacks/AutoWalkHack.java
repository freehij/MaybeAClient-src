package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePost;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBoolean;

public class AutoWalkHack extends Hack{
	
	public static AutoWalkHack instance;
	
	public AutoWalkHack() {
		super("AutoWalk", "Makes the player walk automatically", Keyboard.KEY_F7, Category.MOVEMENT);
		instance = this;
	}
	
	

}
