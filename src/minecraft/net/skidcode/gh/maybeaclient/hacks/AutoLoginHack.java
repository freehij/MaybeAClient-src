package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet3Chat;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPacketReceive;
import net.skidcode.gh.maybeaclient.gui.altman.AccountInfo;
import net.skidcode.gh.maybeaclient.gui.altman.GuiAccManager;
import net.skidcode.gh.maybeaclient.gui.altman.PasswordInfo;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class AutoLoginHack extends Hack implements EventListener{
	public AutoLoginHack() {
		super("AutoLogin", "Automatically enters password", Keyboard.KEY_NONE, Category.MISC);
		EventRegistry.registerListener(EventPacketReceive.class, this);
	}

	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPacketReceive) {
			if(((EventPacketReceive) event).packet instanceof Packet3Chat) {
				Packet3Chat pk = (Packet3Chat) ((EventPacketReceive) event).packet;
				AccountInfo accinfo = GuiAccManager.usernames.get(Client.mc.session.username);
				if(accinfo != null) {
					PasswordInfo serv = accinfo.passwords.get(mc.getSendQueue().ip+":"+mc.getSendQueue().port);
					if(serv == null && mc.getSendQueue().port == 25565) serv = accinfo.passwords.get(mc.getSendQueue().ip);
					
					if(serv != null) {
						boolean enterPassword = false;
						switch(serv.mode) {
							case EXACT:
								enterPassword = pk.message.equals(serv.loginPrompt.replace("&", ChatColor.SYM));
								break;
							case STARTSWITH:
								enterPassword = pk.message.startsWith(serv.loginPrompt.replace("&", ChatColor.SYM));
								break;
							default:
								break;
						}
						
						if(enterPassword) {
							Client.addMessage("Detected password request.");
							mc.thePlayer.sendChatMessage(serv.loginCommand.replace("%password%", serv.password));
						}
					}
				}
				
			}
		}
	}

}
