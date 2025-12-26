package net.skidcode.gh.maybeaclient.events;

public interface EventListener<T extends Event> {
	public void handleEvent(T event);
}
