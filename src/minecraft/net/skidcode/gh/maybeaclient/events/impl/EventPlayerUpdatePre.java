package net.skidcode.gh.maybeaclient.events.impl;

import net.skidcode.gh.maybeaclient.events.Event;

public class EventPlayerUpdatePre extends Event{
	protected static int id = ++Event.id; 
	
	public int getID() {
		return id;
	}
}
