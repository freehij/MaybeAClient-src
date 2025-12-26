package net.skidcode.gh.maybeaclient.console;

import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class CommandHelp extends Command{

	public CommandHelp() {
		super("help", "Try to guess");
	}

	@Override
	public void onTyped(String[] args) {
		for(Command cmd : Client.commands.values()) {
			Client.addMessage(ChatColor.GOLD+Client.cmdPrefix+cmd.name+ChatColor.WHITE+": "+cmd.description);
		}
	}

}
