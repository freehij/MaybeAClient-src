package net.skidcode.gh.maybeaclient.events.impl;

import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.events.Event;

public class EventRenderIngameNoDebug extends Event{
	protected static int id = ++Event.id; 
	
	public ScaledResolution resolution;
	
	public EventRenderIngameNoDebug(ScaledResolution resolution) {
		this.resolution = resolution;
	}
	
	public int getID() {
		return id;
	}
}
