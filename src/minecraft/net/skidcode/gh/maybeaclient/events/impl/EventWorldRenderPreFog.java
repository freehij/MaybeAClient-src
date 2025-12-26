package net.skidcode.gh.maybeaclient.events.impl;

import net.skidcode.gh.maybeaclient.events.Event;

public class EventWorldRenderPreFog extends Event{
	
	protected static int id = ++Event.id; 
	public float param;
	public EventWorldRenderPreFog(float var1) {
		this.param = var1;
	}

	public int getID() {
		return id;
	}
}

