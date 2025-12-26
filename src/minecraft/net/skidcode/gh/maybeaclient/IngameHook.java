package net.skidcode.gh.maybeaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.gui.click.ArrayListTab;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.hacks.ClickGUIHack;

public class IngameHook {
	public static Minecraft mc;
	public static void handleIngame(ScaledResolution res) {
		if(!(mc.currentScreen instanceof ClickGUI)) {
			int prev = mc.gameSettings.guiScale;
			int newScale = ClickGUIHack.instance.getScale();
			if(prev != newScale) {
				mc.gameSettings.guiScale = newScale;
				mc.entityRenderer.setupScaledResolution();
			}
			
			for(int i = ClickGUI.tabs.size()-1; i >= 0; --i ) {
				ClickGUI.tabs.get(i).renderIngame();
			}
			if(prev != newScale) {
				mc.gameSettings.guiScale = prev;
				mc.entityRenderer.setupScaledResolution();
			}
		}
	}
}
