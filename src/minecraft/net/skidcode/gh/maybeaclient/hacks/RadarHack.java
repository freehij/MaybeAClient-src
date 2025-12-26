package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventRenderIngameNoDebug;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.InventoryTab;
import net.skidcode.gh.maybeaclient.gui.click.PlayerViewTab;
import net.skidcode.gh.maybeaclient.gui.click.RadarTab;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBoolean;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingMode;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class RadarHack extends Hack{
	
	public SettingBoolean showXYZ = new SettingBoolean(this, "Show XYZ", false);
	public SettingMode alignment = new SettingMode(this, "Alignment", "Left", "Right");
	public SettingMode expand = new SettingMode(this, "Expand", "Bottom", "Top");
	public SettingMode staticPositon;
	public static RadarHack instance;
	public RadarHack() {
		super("Radar", "Shows players nearby", Keyboard.KEY_TAB, Category.UI);
		instance = this;
		this.addSetting(this.showXYZ);
		this.addSetting(this.staticPositon = new SettingMode(this, "Static Position", "Top Right", "Bottom Right", "Top Left", "Bottom Left", "Disabled") {
			public void setValue(String value) {
				super.setValue(value);
				if(this.currentMode.equalsIgnoreCase("Disabled")) {
					RadarHack.instance.alignment.show();
					RadarHack.instance.expand.show();
				}else {
					RadarHack.instance.alignment.hide();
					RadarHack.instance.expand.hide();
				}
			}
		});
		this.addSetting(this.alignment);
		this.addSetting(this.expand);
	}
}
