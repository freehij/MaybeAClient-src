package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePost;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBoolean;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingFloat;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class CameraLockHack extends Hack implements EventListener{
	public static CameraLockHack instance;
	public SettingBoolean lockYaw = new SettingBoolean(this, "LockYaw", true);
	public SettingBoolean lockPitch = new SettingBoolean(this, "LockPitch", false);
	
	public SettingFloat yaw = new SettingFloat(this, "Yaw", 0, 0, 359, 5);
	public SettingFloat pitch = new SettingFloat(this, "Pitch", 0, -90, 90, 5);
	
	public CameraLockHack() {
		super("CameraLock", "Allows players to lock yaw and pitch of the camera", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
		this.addSetting(lockYaw);
		this.addSetting(lockPitch);
		this.addSetting(yaw);
		this.addSetting(pitch);
		
		EventRegistry.registerListener(EventPlayerUpdatePost.class, this);
	}

	@Override
	public String getNameForArrayList() {
		String s = "[";
		if(this.lockYaw.value) {
			s += ChatColor.LIGHTCYAN;
			s += this.yaw.value;
			s += ChatColor.WHITE;
			if(this.lockPitch.value) s += ";";
		}
		if(this.lockPitch.value) {
			s += ChatColor.LIGHTCYAN;
			s += this.pitch.value;
			s += ChatColor.WHITE;
		}
		s += "]";
		if(s.equals("[]")) return this.name;
		return this.name + s;
	}
	
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPlayerUpdatePost) {
			if(this.lockYaw.value) mc.thePlayer.rotationYaw = mc.thePlayer.prevRotationYaw = this.yaw.value % 360;
			if(this.lockPitch.value) mc.thePlayer.rotationPitch = mc.thePlayer.prevRotationPitch = this.pitch.value % 91;
		}
	}
	
}
