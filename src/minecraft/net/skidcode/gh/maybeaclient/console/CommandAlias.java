package net.skidcode.gh.maybeaclient.console;

import java.util.Map;

import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class CommandAlias extends Command{

	public CommandAlias() {
		super("alias", "Manage command aliases");
	}

	@Override
	public void onTyped(String[] args) {
		
		int argc = args.length;
		if(argc < 1 || args[0].equalsIgnoreCase("help")) {
			args = new String[] {"help"};
			
		}
		
		switch(args[0]) {
			case "new":
			case "add":
				if(argc < 3) {
					Client.addMessage("Not enough arguments!");
				}else {
					String alias = args[1];
					String command = Client.getAlias(alias);
					if(command != null) {
						Client.addMessage("Alias "+ChatColor.GOLD+alias+ChatColor.WHITE+" already has a command attached to it: "+ChatColor.GOLD+command);
					}else {
						
						command = args[2];
						for(int i = 3; i < args.length; ++i) {
							command += " "+ args[i];
						}
						
						Client.addAlias(alias, command);
						Client.saveAliases();
						Client.addMessage("Alias "+ChatColor.GOLD+alias+ChatColor.WHITE+" was attached to "+ChatColor.GOLD+command);
					}
				}
				break;
			case "list":
				int pages = (int) Math.ceil((float)Client.aliasesList.size() / 10);
				int page = 0;
				int page4humans = (page+1);
				Client.addMessage("Aliases list page "+ChatColor.GOLD+page4humans+ChatColor.WHITE+"/"+ChatColor.GOLD+pages+ChatColor.WHITE+":");
				for(int i = page*10; i < page*10+10; ++i) {
					if(Client.aliasesList.size() <= i) break;
					String alias = Client.aliasesList.get(i);
					String cmd = Client.aliases.get(alias);
					Client.addMessage(ChatColor.GOLD+alias+ChatColor.WHITE+": "+ChatColor.LIGHTCYAN+cmd);
				}
				break;
			case "delete":
			case "remove":
				if(argc < 2) {
					Client.addMessage("Not enough arguments!");
				}
				
				String alias = args[1];
				String command = Client.getAlias(alias);
				if(command != null) {
					Client.removeAlias(alias);
					Client.saveAliases();
					Client.addMessage("Alias "+ChatColor.GOLD+alias+ChatColor.WHITE+" was removed");
				}else {
					Client.addMessage("Alias "+ChatColor.GOLD+alias+ChatColor.WHITE+" doesn't exist");
				}
				break;
			default:
				Client.addMessage(".alias list - List available aliases");
				Client.addMessage(".alias new <alias> <command...> - Create a new alias");
				Client.addMessage(".alias delete <alias> - Delete an alias");
				break;
		}
	}
	
}
