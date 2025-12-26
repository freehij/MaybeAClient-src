package net.skidcode.gh.maybeaclient.console;

import net.skidcode.gh.maybeaclient.Client;

public class CommandSlot9 extends Command{

	public CommandSlot9() {
		super("slot9", "An old way to make tool durability in top left slot of the inventory infinite.");
	}

	@Override
	public void onTyped(String[] args) {
		Client.mc.thePlayer.inventory.currentItem = 9;
		Client.addMessage("Changed hotbar slot to top left slot of the inventory.");
	}

}
