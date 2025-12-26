package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.MathHelper;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePost;
import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class StrafeHack extends Hack implements EventListener{

	public static StrafeHack instance;

	public StrafeHack() {
		super("Strafe", "Allows to instantly switch directions", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
		EventRegistry.registerListener(EventPlayerUpdatePost.class, this);
	}
	
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPlayerUpdatePost && !FlyHack.instance.status) {
			
			
			
			float yaw = mc.thePlayer.rotationYaw;
			mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
			if(!mc.inGameHasFocus) {
				if(!InventoryWalkHack.instance.status || !(mc.currentScreen instanceof GuiContainer)) return;
			}
			boolean fw = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode);
			boolean bw = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.keyCode);
			boolean lw = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.keyCode);
			boolean rw = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.keyCode);
				
			if((AutoTunnelHack.instance.status && AutoTunnelHack.instance.autoWalk.value)) {
				fw = true;
				yaw = AutoTunnelHack.instance.getDirection().yaw;
			}
				
			float d1 = yaw + 90;
				
			if(AutoWalkHack.instance.status) fw = true;
				
			if(fw){
				if(lw) d1 -= 45;
				else if(rw) d1 += 45;
			} else if(bw){
				d1 += 180D;
				if(lw)
				{
					d1 += 45D;
				} else
				if(rw)
				{
					d1 -= 45D;
				}
			} 
			else if(lw) d1 -= 90D;
			else if(rw) d1 += 90D;
			
			
			if(fw || bw || lw || rw)
			{
				mc.thePlayer.motionX = Math.cos(Math.toRadians(d1))*0.2;
				mc.thePlayer.motionZ = Math.sin(Math.toRadians(d1))*0.2;
			}
		}
	}
	
	

}
