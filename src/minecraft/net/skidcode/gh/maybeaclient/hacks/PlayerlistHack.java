package net.skidcode.gh.maybeaclient.hacks;

import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet3Chat;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPacketReceive;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePre;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingInteger;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingMode;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingTextBox;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class PlayerlistHack extends Hack implements EventListener{
	public SettingTextBox startWith = new SettingTextBox(this, "StartsWithRegex", "&6\\(\\d*\\) &7", 75);
	public SettingTextBox nextMsgStart = new SettingTextBox(this, "NextMsgStartRegex", "&7", 75);
	public SettingTextBox separator = new SettingTextBox(this, "Separator", ", ", 75);
	public SettingInteger delayBeforeList = new SettingInteger(this, "DelayBeforeListS", 1, 0, 20);
	public SettingInteger delayBetweenMultipleMsgs = new SettingInteger(this, "MaxMsgDelayTicks", 2, 0, 20);
	public SettingMode alignment = new SettingMode(this, "Alignment", "Left", "Right");
	public SettingMode expand = new SettingMode(this, "Expand", "Bottom", "Top");
	public static PlayerlistHack instance;
	public PlayerlistHack() {
		super("Playerlist", "Shows player list", Keyboard.KEY_NONE, Category.UI);
		instance = this;
		
		this.addSetting(this.alignment);
		this.addSetting(this.expand);
		
		this.addSetting(this.startWith);
		this.addSetting(this.nextMsgStart);
		this.addSetting(this.separator);
		this.addSetting(this.delayBeforeList);
		this.addSetting(this.delayBetweenMultipleMsgs);
		EventRegistry.registerListener(EventPacketReceive.class, this);
		EventRegistry.registerListener(EventPlayerUpdatePre.class, this);
	}
	
	public static ArrayList<String> detectedPlayers = new ArrayList<String>();
	
	public static void resetDetected() {
		detectedPlayers.clear();
		listRequested = false;
		needsRequest = true;
		ticker = 0;
	}
	public void onEnable() {
		
	}
	
	public void onDisable() {
		resetDetected();
	}
	
	public void scanMatched() {
		matches = false;
		messages.set(0, messages.get(0).replaceAll(pp(this.startWith.value)+"(.*)$", "$1"));
		for(int i = 1; i < messages.size(); ++i) {
			messages.set(i, messages.get(i).replaceAll(pp(this.nextMsgStart.value)+"(.*)$", "$1"));
		}
		String msgFull = String.join("", messages);
		
		String[] nicks = msgFull.split(pp(this.separator.value));
		for(int i = 0; i < nicks.length; ++i) {
			detectedPlayers.add(nicks[i].trim());
		}
	}
	
	public static String pp(String s) {
		s = s.replace("&", ChatColor.SYM);
		return s;
	}
	
	public static boolean listRequested = false;
	public static boolean needsRequest = true;
	public static int ticker = 0;
	public static int lastMatchTicker = 0;
	
	public boolean matches = false;
	public ArrayList<String> messages;
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPlayerUpdatePre) {
			if(!mc.isMultiplayerWorld()) return; 
			int delay = this.delayBeforeList.getValue() * 20;
			if(ticker < delay) {
				++ticker;
				return;
			}
			
			if(matches) {
				if(lastMatchTicker < this.delayBetweenMultipleMsgs.getValue()) {
					++lastMatchTicker;
				}else {
					scanMatched();
				}
			}
			if(needsRequest) {
				Client.addMessage("Sending /list");
				resetDetected();
				listRequested = true;
				needsRequest = false;
				mc.thePlayer.sendChatMessage("/list"); //XXX maybe modifable?
			}
			
			
		}
		if(event instanceof EventPacketReceive) {
			try {
				if(((EventPacketReceive) event).packet instanceof Packet3Chat) {
					Packet3Chat pk = (Packet3Chat) ((EventPacketReceive) event).packet;
					if(!listRequested && !matches) {
						if(pk.message.startsWith(pp("&e"))) {
							String[] lst = pk.message.substring(2).split(" ");
							if(lst.length == 4) {
								if(lst[2].equalsIgnoreCase("the") && lst[3].equalsIgnoreCase("game.")) {
									String status = lst[1];
									String player = lst[0];
									
									if(status.equalsIgnoreCase("left")) detectedPlayers.remove(player);
									else if(status.equalsIgnoreCase("joined")) detectedPlayers.add(player);
								}
							}
						}

						return;
					}
					listRequested = false;
					if(!matches && pk.message.matches(pp(this.startWith.value)+".*$")) {
						matches = true;
						messages = new ArrayList<String>();
						lastMatchTicker = 0;
						messages.add(pk.message);
					}else {
						if(matches && pk.message.matches(pp(this.nextMsgStart.value+".*$"))) {
							lastMatchTicker = 0;
							messages.add(pk.message);
						}else if(matches){
							scanMatched();
						}
					}
					
				}
			}catch(PatternSyntaxException e) {
				e.printStackTrace();
				Client.addMessage("Disabling "+this.name+" due to invalid regex pattern. Stacktrace is in console");
				this.toggle();
			}
		}
	}
}
