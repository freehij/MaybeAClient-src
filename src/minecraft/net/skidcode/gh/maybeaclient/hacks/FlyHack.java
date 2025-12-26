package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.GuiContainer;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePost;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.Setting;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingDouble;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class FlyHack extends Hack implements EventListener{
	
	public SettingDouble speedMultiplier = new SettingDouble(this, "Speed Multiplier", 0.2, 0.1, 2.0, 0.05);
	
	public static FlyHack instance;
	public FlyHack() {
		super("Fly", "FlyHack ^-^", Keyboard.KEY_R, Category.MOVEMENT);
		instance = this;
		this.addSetting(speedMultiplier);
		EventRegistry.registerListener(EventPlayerUpdatePost.class, this);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	@Override
	public String getNameForArrayList() {
		String s = "[";
		s += ChatColor.LIGHTCYAN;
		s += this.speedMultiplier.value;
		s += ChatColor.WHITE;
		s += "]";
		
		return this.name + s;
	}
	
	public static void handleFly(double speedMultiplier) {
		
		mc.thePlayer.onGround = false;
		mc.thePlayer.motionX = 0.0D;
		mc.thePlayer.motionY = 0.0D;
		mc.thePlayer.motionZ = 0.0D;
		if(!mc.inGameHasFocus) {
			if(!InventoryWalkHack.instance.status || !(mc.currentScreen instanceof GuiContainer)) return;
		}
		double d = mc.thePlayer.rotationPitch + 90F;
		double d1 = mc.thePlayer.rotationYaw + 90F;
		boolean flag = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode);
		boolean flag1 = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.keyCode);
		boolean flag2 = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.keyCode);
		boolean flag3 = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.keyCode);
		
		if((AutoTunnelHack.instance.status && AutoTunnelHack.instance.autoWalk.value)) {
			flag = true;
			d1 = AutoTunnelHack.instance.getDirection().yaw + 90;
		}
		
		if(AutoWalkHack.instance.status) flag = true;
		if(flag)
		{
			if(flag2)
			{
				d1 -= 45D;
			} else
			if(flag3)
			{
				d1 += 45D;
			}
		} else
		if(flag1)
		{
			d1 += 180D;
			if(flag2)
			{
				d1 += 45D;
			} else
			if(flag3)
			{
				d1 -= 45D;
			}
		} else
		if(flag2)
		{
			d1 -= 90D;
		} else
		if(flag3)
		{
			d1 += 90D;
		}
		if(flag || flag2 || flag1 || flag3)
		{
			mc.thePlayer.motionX = Math.cos(Math.toRadians(d1));
			mc.thePlayer.motionZ = Math.sin(Math.toRadians(d1));
		}
		if(Keyboard.isKeyDown(57))
		{
			mc.thePlayer.motionY++;
			
		} else
		if(Keyboard.isKeyDown(42))
		{
			mc.thePlayer.motionY--;
		}
		
		mc.thePlayer.motionX *= speedMultiplier;
		mc.thePlayer.motionY *= speedMultiplier;
		mc.thePlayer.motionZ *= speedMultiplier;
	}
	
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPlayerUpdatePost) {
			handleFly(this.speedMultiplier.value);
		}
	}
}
