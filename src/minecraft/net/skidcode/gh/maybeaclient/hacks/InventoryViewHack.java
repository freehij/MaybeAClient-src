package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.RenderManager;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventRenderIngameNoDebug;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.InventoryTab;
import net.skidcode.gh.maybeaclient.gui.click.PlayerViewTab;
import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class InventoryViewHack extends Hack{
	public static InventoryViewHack instance;
	public InventoryViewHack() {
		super("InventoryView", "Shows Inventory tab", Keyboard.KEY_NONE, Category.UI);
		instance = this;
	}
}
