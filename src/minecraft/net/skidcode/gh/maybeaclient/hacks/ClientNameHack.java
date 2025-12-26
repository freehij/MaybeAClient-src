package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventRenderIngameNoDebug;
import net.skidcode.gh.maybeaclient.gui.click.ArrayListTab;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.ClientNameTab;
import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class ClientNameHack extends Hack{

	public static ClientNameHack instance;

	public ClientNameHack() {
		super("ClientName", "Show client name", Keyboard.KEY_NONE, Category.UI);
		instance = this;
		this.status = true;
	}
}
