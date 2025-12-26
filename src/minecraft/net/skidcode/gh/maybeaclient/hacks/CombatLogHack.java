package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePost;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingInteger;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingMode;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class CombatLogHack extends Hack implements EventListener{
	
	public SettingMode mode;
	public SettingInteger health;
	
	public static CombatLogHack instance;
	public static boolean shouldQuit = false;
	
	public CombatLogHack() {
		super("CombatLog", "Leave the game if health is too low/you were damaged", Keyboard.KEY_NONE, Category.COMBAT);
		instance = this;
		this.health = new SettingInteger(this, "Health", 10, 0, 20);
		this.mode = new SettingMode(this, "Mode", "Health", "OnHit") {
			@Override
			public void setValue(String mode) {
				super.setValue(mode);
				((CombatLogHack)this.hack).health.hidden = !mode.equalsIgnoreCase("Health");
			}
		};
		
		EventRegistry.registerListener(EventPlayerUpdatePost.class, this);
		
		this.addSetting(this.mode);
		this.addSetting(this.health);
	}
	
	public String getNameForArrayList() {
		String s = "[";
		s += ChatColor.LIGHTCYAN;
		s += this.mode.currentMode;
		s += ChatColor.WHITE;
		s += "]";
		
		return this.name + s;
	}
	
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPlayerUpdatePost) {
			if(this.mode.currentMode.equalsIgnoreCase("Health") && mc.thePlayer.health <= this.health.value) {
				if(mc.isMultiplayerWorld()) {
	    			mc.theWorld.sendQuittingDisconnectingPacket();
	    			if(this.mode.currentMode.equalsIgnoreCase("Health")) this.status = false;
	    		}else {
	    			CombatLogHack.shouldQuit = true;
	    		}
			}
		}
	}

}
