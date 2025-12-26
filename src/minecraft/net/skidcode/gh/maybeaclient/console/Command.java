package net.skidcode.gh.maybeaclient.console;

public abstract class Command {
	public String name;
	public String description;
	
	public Command(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public abstract void onTyped(String[] args);
}
