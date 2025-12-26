package net.skidcode.gh.maybeaclient.hacks;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import lunatrius.schematica.GuiSchematicControl;
import lunatrius.schematica.GuiSchematicLoad;
import lunatrius.schematica.GuiSchematicSave;
import lunatrius.schematica.GuiSchematicaStats;
import lunatrius.schematica.SchematicWorld;
import lunatrius.schematica.Settings;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePost;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBoolean;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingFloat;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingInteger;

public class SchematicaHack extends Hack implements EventListener{
	
	public SettingBoolean enableAlpha = new SettingBoolean(this, "Enable Transparency", false) {
		@Override
		public void setValue(boolean b) {
			super.setValue(b);
			Settings.instance().needsUpdate = true;
		}
	};
	public SettingInteger alpha = new SettingInteger(this, "Transparency", 0, 0, 255) {
		@Override
		public void setValue(int i) {
			int prev = this.value;
			super.setValue(i);
			if(this.value != prev) Settings.instance().needsUpdate = true;
		}
	};
	
	public SettingBoolean highlight = new SettingBoolean(this, "Highlight", true) {
		@Override
		public void setValue(boolean b) {
			boolean prev = this.value;
			super.setValue(b);
			if(this.value != prev) Settings.instance().needsUpdate = true;
		}
	};
	
	//public SettingInteger renderRangeX = new SettingInteger(this, "Render Range X", 20, 5, 50);
	//public SettingInteger renderRangeY = new SettingInteger(this, "Render Range Y", 20, 5, 50);
	//public SettingInteger renderRangeZ = new SettingInteger(this, "Render Range Z", 20, 5, 50);
	//public SettingFloat blockDelta = new SettingFloat(this, "Highlight block delta", 0.005f, 0, 0.5f);
	
	public SettingBoolean openLoad = new SettingBoolean(this, "Load", false) {
		public void setValue(boolean d) {}
		public void onDeselect(Tab tab, int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
			mc.displayGuiScreen(new GuiSchematicLoad(mc.currentScreen));
		}
	};
	
	public SettingBoolean openSave = new SettingBoolean(this, "Save", false) {
		public void setValue(boolean d) {}
		public void onDeselect(Tab tab, int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
			mc.displayGuiScreen(new GuiSchematicSave(mc.currentScreen));
		}
	};
	
	public SettingBoolean openControl = new SettingBoolean(this, "Control", false) {
		public void setValue(boolean d) {}
		public void onDeselect(Tab tab, int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
			mc.displayGuiScreen(new GuiSchematicControl(mc.currentScreen));
		}
	};
	
	public SettingBoolean openStats = new SettingBoolean(this, "Stats", false) {
		public void setValue(boolean d) {}
		public void onDeselect(Tab tab, int xMin, int yMin, int xMax, int yMax, int mouseX, int mouseY, int mouseClick) {
			mc.displayGuiScreen(new GuiSchematicaStats(mc.currentScreen));
		}
	};
	
	public SettingBoolean autoPlacer;
	public SettingInteger placeDelay = new SettingInteger(this, "Delay", 5, 0, 10);
	public SettingInteger placeRadius = new SettingInteger(this, "Radius", 2, 1, 6);
	public final lunatrius.schematica.Render render = new lunatrius.schematica.Render(this);
	
	public static SchematicaHack instance;
	
	public SchematicaHack() {
		super("Schematica", "Port of Schematica", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
		this.addSetting(this.autoPlacer = new SettingBoolean(this, "AutoBlockPlace", false) {
			public void setValue(boolean d) {
				super.setValue(d);
				SchematicaHack.instance.placeDelay.hidden = SchematicaHack.instance.placeRadius.hidden = !d;
			}
		});
		this.addSetting(this.placeDelay);
		this.addSetting(this.placeRadius);
		
		this.addSetting(this.enableAlpha);
		this.addSetting(this.alpha);
		this.addSetting(this.highlight);
		
		//this.addSetting(this.renderRangeX);
		//this.addSetting(this.renderRangeY);
		//this.addSetting(this.renderRangeZ);
		
		this.addSetting(this.openLoad);
		this.addSetting(this.openStats);
		this.openStats.hidden = true;
		this.addSetting(this.openSave);
		this.addSetting(this.openControl);
		
        Settings.schematicDirectory.mkdirs();
        Settings.textureDirectory.mkdirs();
		//this.addSetting(this.blockDelta);
        
        EventRegistry.registerListener(EventPlayerUpdatePost.class, this);
	}
	public int ticks = 0;
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPlayerUpdatePost) {
			if(!this.autoPlacer.getValue()) return;
			
			Settings inst = Settings.instance();
			SchematicWorld schem = inst.schematic;
			if(schem != null) {
				if(this.ticks++ < this.placeDelay.getValue()) {
					return;
				}
				this.ticks = 0;
				int minX = 0, maxX = 0, minY = 0, maxY = 0, minZ = 0, maxZ = 0;
				maxX = inst.schematic.width();
				maxY = inst.schematic.height();
				maxZ = inst.schematic.length();

				if (inst.renderingLayer >= 0) {
					minY = inst.renderingLayer;
					maxY = inst.renderingLayer + 1;
				}
				
				int radius = this.placeRadius.getValue();
				int ppX = (int) mc.thePlayer.posX;
				int ppY = (int) mc.thePlayer.posY;
				int ppZ = (int) mc.thePlayer.posZ;
				
				HashMap<Integer, Entry<ItemStack, Integer>> hm = new HashMap<Integer, Entry<ItemStack, Integer>>();
				for(int i = 0; i < 9; ++i) {
					ItemStack s = mc.thePlayer.inventory.mainInventory[i];
					if(s != null) {
						int idm = s.itemID << 8 | s.getItemDamage();
						hm.put(idm, new AbstractMap.SimpleEntry<ItemStack, Integer>(s, i));
					}
				}

				int minXr = (int) (ppX - radius);
				int maxXr = (int) (ppX + radius);
				int minZr = (int) (ppZ - radius);
				int maxZr = (int) (ppZ + radius);
				int minYr = (int) (ppY - radius);
				int maxYr = (int) (ppY + radius);
				for(int x = minX; x < maxX; ++x) {
					for(int z = minZ; z < maxZ; ++z) {
						for(int y = minY; y < maxY; ++y) {
							if(y > 127) continue;
							int rX = 0, rY = 0, rZ = 0;
							try {
								rX = x + inst.offset.x;
								rY = y + inst.offset.y;
								rZ = z + inst.offset.z;
								if(rX <= maxXr && rX >= minXr && rY <= maxYr && rY >= minYr && rZ <= maxZr && rZ >= minZr) {
									int id = schem.blocks[x][y][z];
									int worldID = mc.theWorld.getBlockId(rX, rY, rZ);
									int meta = schem.metadata[x][y][z];
									Entry<ItemStack, Integer> ent = hm.get(id << 8 | meta);
									if((id != 0 && (worldID == 0/*XXX modern versions: Replaceable || Block.blocksList[worldID].blockMaterial.getIsGroundCover()*/)) && id != worldID && ent != null) {
										ItemStack is = ent.getKey();
										int slot = ent.getValue();
										int saved = mc.thePlayer.inventory.currentItem;
										mc.thePlayer.inventory.currentItem = slot;
										this.placeBlock(rX, rY, rZ);
										mc.thePlayer.inventory.currentItem = saved;
									}
								}
							}catch(java.lang.ArrayIndexOutOfBoundsException e) {e.printStackTrace();}
						}
					}
				}
				
				
			}
		}
	}
	
	public boolean canPlaceBlock(int x, int y, int z) {
		int id = mc.theWorld.getBlockId(x, y, z);
		return id == 0 || id == 10 || id == 11 || id == 8 || id == 9;
	}
	
	public void placeBlock(int x, int y, int z) {
		if (!canPlaceBlock(x - 1, y, z)) {
			mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x - 1, y, z, 5);
		} else if (!canPlaceBlock(x + 1, y, z)) {
			mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x + 1, y, z, 4);
		} else if (!canPlaceBlock(x, y, z - 1)) {
			mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y, z - 1, 3);
		} else if (!canPlaceBlock(x, y, z + 1)) {
			mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y, z + 1, 2);
		} else if (!canPlaceBlock(x, y - 1, z)) {
			mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y - 1, z, 1);
		} else if (!canPlaceBlock(x, y + 1, z)) {
			mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y - 1, z, 0);
		}
	}
	
	@Override
	public void onEnable() {
		this.ticks = 0;
	}
}
