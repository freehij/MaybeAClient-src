package net.skidcode.gh.maybeaclient.events.impl;

import net.minecraft.src.Packet;
import net.skidcode.gh.maybeaclient.events.Event;

public class EventPacketReceive extends Event{
	protected static int id = ++Event.id; 
	
	public Packet packet;
	
	public EventPacketReceive(Packet packet) {
		this.packet = packet;
	}
	
	public int getID() {
		return id;
	}
}

