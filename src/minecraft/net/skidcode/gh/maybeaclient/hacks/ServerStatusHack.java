package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventRenderIngameNoDebug;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class ServerStatusHack extends Hack implements EventListener{

	public ServerStatusHack() {
		super("ServerStatus", "Shows status of the server", Keyboard.KEY_NONE, Category.UI);
		EventRegistry.registerListener(EventRenderIngameNoDebug.class, this);
	}

	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventRenderIngameNoDebug) {
			EventRenderIngameNoDebug ev = (EventRenderIngameNoDebug) event;
			if(mc.getSendQueue() != null && mc.getSendQueue().netManager.timeSinceLastRead >= 20) {
				//ev.resolution
				double time = mc.getSendQueue().netManager.timeSinceLastRead / 20d;
				String s = ChatColor.WHITE+"Server is frozen for "+ChatColor.LIGHTCYAN+String.format("%.2f", time)+ChatColor.WHITE+" seconds";
				mc.fontRenderer.drawStringWithShadow(s, ev.resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(s) / 2, ev.resolution.getScaledHeight() / 2 - 12, 0xdeaddead);
			}
			
		}
	}

}
