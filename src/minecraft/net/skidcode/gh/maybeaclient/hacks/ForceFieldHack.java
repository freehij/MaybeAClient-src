package net.skidcode.gh.maybeaclient.hacks;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimals;
import net.minecraft.src.EntityMobs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.Packet7;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventMPMovementUpdate;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBoolean;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingChooser;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingDouble;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingIgnoreList;
import net.skidcode.gh.maybeaclient.utils.ChatColor;

public class ForceFieldHack extends Hack implements EventListener{
	
	public SettingChooser chooser = new SettingChooser(
		this,
		"Entity Chooser",
		new String[] {"Players", "Animals", "Hostiles", "Neutrals"},
		new boolean[] {true, false, true, false}
	);
	
	public SettingIgnoreList ignoreList = new SettingIgnoreList(this, "Ignore");
	public SettingBoolean notifyInChat = new SettingBoolean(this, "NotifyInChat", false);
	public SettingBoolean middleClickFriend = new SettingBoolean(this, "MiddleClickFriend", false) {
		public void setValue(boolean d) {
			super.setValue(d);
			((ForceFieldHack) this.hack).notifyInChat.hidden = !this.getValue();
		}
	};
	
	public SettingDouble radius = new SettingDouble(this, "Radius", 6.0f, 0, 10);			
	
	public static ForceFieldHack instance;
	
	public ForceFieldHack() {
		super("KillAura", "Damages the entities around the player", Keyboard.KEY_NONE, Category.COMBAT);
		instance = this;
		
		this.addSetting(this.chooser);
		this.addSetting(this.radius);
		this.addSetting(this.ignoreList);
		this.addSetting(this.middleClickFriend);
		this.addSetting(this.notifyInChat);
		EventRegistry.registerListener(EventMPMovementUpdate.class, this);
	}
	
	@Override
	public String getNameForArrayList() {
		String s = "[";
		s += ChatColor.LIGHTCYAN;
		if(this.chooser.getValue("Players")) s += "P";
		if(this.chooser.getValue("Animals")) s += "A";
		if(this.chooser.getValue("Hostiles")) s += "H";
		if(this.chooser.getValue("Neutrals")) s += "N";
		s += ChatColor.WHITE;
		s += "]";
		
		return this.name + s;
	}
	
	public boolean canBeAttacked(Entity e) {
		return 
			(chooser.getValue("Players") && e instanceof EntityPlayer) ||
			(chooser.getValue("Animals") && e instanceof EntityAnimals && !(e instanceof EntitySquid || e instanceof EntityWolf)) ||
			(chooser.getValue("Hostiles") && e instanceof EntityMobs) ||
			(chooser.getValue("Neutrals") && (e instanceof EntitySquid || e instanceof EntityWolf));
	}
	
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventMPMovementUpdate) {
			double rad = this.radius.getValue();
			List entitiesNearby = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
				mc.thePlayer,
				AxisAlignedBB.getBoundingBox(
					mc.thePlayer.posX - rad, mc.thePlayer.posY - rad, mc.thePlayer.posZ - rad,
					mc.thePlayer.posX + rad, mc.thePlayer.posY + rad, mc.thePlayer.posZ + rad
				)
			);
			for(Object o : entitiesNearby) {
				Entity e = (Entity)o;
				if(this.canBeAttacked(e)) {
					if(e instanceof EntityPlayer && this.ignoreList.enabled && this.ignoreList.names.contains(((EntityPlayer)e).username.toLowerCase())) {
						continue;
					}
					mc.getSendQueue().addToSendQueue(new Packet7(0, e.entityId, 1));
				}
			}
		}
	}
}
