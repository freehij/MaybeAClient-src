package net.skidcode.gh.maybeaclient.events.impl;

import net.skidcode.gh.maybeaclient.events.Event;

public class EventPlayerUpdatePost extends Event{
	
	protected static int id = ++Event.id; 
	
	public int getID() {
		return id;
	}
}
