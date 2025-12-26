package net.skidcode.gh.maybeaclient.hacks;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPlayerUpdatePost;
import net.skidcode.gh.maybeaclient.events.impl.EventWorldRenderPreFog;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBlockChooser;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBoolean;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingInteger;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingMode;
import net.skidcode.gh.maybeaclient.utils.BlockPos;
import net.skidcode.gh.maybeaclient.utils.ChatColor;
import net.skidcode.gh.maybeaclient.utils.Direction;
import net.skidcode.gh.maybeaclient.utils.PlayerUtils;
import net.skidcode.gh.maybeaclient.utils.RenderUtils;

public class AutoTunnelHack extends Hack implements EventListener{
	
	public SettingMode direction = new SettingMode(this, "Direction", "Camera", "X+", "X-", "Z+", "Z-");
	public SettingMode breakMode;
	public SettingInteger maxMultiBlocks = new SettingInteger(this, "MaxMultiBlocks", 4, 1, 20);
	public SettingBoolean swing = new SettingBoolean(this, "Swing", false);
	public SettingBoolean autoWalk = new SettingBoolean(this, "DirectionAutowalk", false);
	public SettingBoolean backFill = new SettingBoolean(this, "BackFill", false);
	public SettingBoolean enableBlockFilter;
	public SettingBlockChooser filter = new SettingBlockChooser(this, "Backfill block filter");
	public SettingInteger placeDelay = new SettingInteger(this, "PlaceDelay", 1, 0, 20);
	public SettingInteger backfillReach = new SettingInteger(this, "BackfillReach", 1, 1, 4);
	public SettingBoolean clientSidePlace = new SettingBoolean(this, "ClientSidePlace", false);
	
	public static AutoTunnelHack instance;
	
	public int selectedBlockX = 0;
	public int selectedBlockY = 0;
	public int selectedBlockZ = 0;
	public int placeTimer = 0;
	public boolean isSelected = false;
	public static ArrayList<BlockPos> currentlyDestroying = new ArrayList<>();
	
	public AutoTunnelHack() {
		super("AutoTunnel", "Automatically destroy blocks in front of the player", Keyboard.KEY_NONE, Category.MISC);
		AutoTunnelHack.instance = this;
		
		this.breakMode = new SettingMode(this, "BreakMode", "Legal", "InstantSingle", "InstantMulti") {
			public void setValue(String value) {
				super.setValue(value);
				AutoTunnelHack.instance.maxMultiBlocks.hidden = !value.equalsIgnoreCase("InstantMulti");
			}
		};
		
		this.addSetting(this.direction);
		this.addSetting(this.autoWalk);
		this.addSetting(this.breakMode);
		this.addSetting(this.maxMultiBlocks);
		this.addSetting(this.swing);
		this.addSetting(this.backFill);
		this.enableBlockFilter = new SettingBoolean(this, "Enable place block filter", false) {
			public void setValue(boolean d) {
				super.setValue(d);
				((AutoTunnelHack)this.hack).filter.hidden = !this.value;
			}
		};
		//this.addSetting(this.radius);
		this.addSetting(this.enableBlockFilter);
		this.addSetting(this.filter);
		this.addSetting(this.placeDelay);
		this.addSetting(this.backfillReach);
		this.addSetting(this.clientSidePlace);
		
		EventRegistry.registerListener(EventPlayerUpdatePost.class, this);
		EventRegistry.registerListener(EventWorldRenderPreFog.class, this);
	}
	
	@Override
	public String getNameForArrayList() {
		String s = "[";
		s += ChatColor.LIGHTCYAN;
		s += this.getDirection().toString();
		s += ChatColor.WHITE;
		s += "]";
		
		return this.name + s;
	}
	
	@Override
	public void onDisable() {
		mc.playerController.field_1064_b = false;
		this.isSelected = false;
		this.placeTimer = 0;
	}
	
	public boolean trySettingXYZ(int x, int y, int z) {
		int id = mc.theWorld.getBlockId(x, y, z);
		
		if(id != 0) {
			float hardness = Block.blocksList[id].blockHardness;
			if(hardness >= 0) { //block is destructable
				//notch moment
				if(id != Block.waterMoving.blockID && id != Block.waterStill.blockID && id != Block.lavaMoving.blockID && id != Block.lavaStill.blockID) {
					this.isSelected = true;
					this.selectedBlockX = x;
					this.selectedBlockY = y;
					this.selectedBlockZ = z;
					
					if(this.breakMode.currentMode.equalsIgnoreCase("InstantMulti")) {
						this.isSelected = false;
						if(currentlyDestroying.size() >= this.maxMultiBlocks.value) {
							return true;
						}else {
							currentlyDestroying.add(new BlockPos(x, y, z));
							if(this.swing.value) mc.thePlayer.swingItem();
							PlayerUtils.destroyBlockInstant(x, y, z, this.getDirection().hitSide);
							return false;
						}
					}
				}else {
					this.isSelected = false;
				}
			}
			
		}else {
			this.isSelected = false;
		}
		
		return this.isSelected;
	}
	
	public Direction getDirection() {
		if(this.direction.currentMode.equalsIgnoreCase("X+")) return Direction.XPOS;
		if(this.direction.currentMode.equalsIgnoreCase("X-")) return Direction.XNEG;
		if(this.direction.currentMode.equalsIgnoreCase("Z+")) return Direction.ZPOS;
		if(this.direction.currentMode.equalsIgnoreCase("Z-")) return Direction.ZNEG;
		if(this.direction.currentMode.equalsIgnoreCase("Camera")) return PlayerUtils.getDirection();
		return Direction.NULL;
	}
	
	public int getPossiblePlaceSide(int xx, int yy, int zz) {
		
		ItemStack item = mc.thePlayer.getCurrentEquippedItem();
		if(item == null || !(item.getItem() instanceof ItemBlock)) return 6; 
		
		for(int i = 0; i < 6; ++i) {
			int x = xx;
			int y = yy;
			int z = zz;
			
			if(i == 0) ++y;
			if(i == 1) --y;
			if(i == 2) ++z;
			if(i == 3) --z;
			if(i == 4) ++x;
			if(i == 5) --x;
			int placeon = mc.theWorld.getBlockId(x, y, z);
			if(placeon == 0) continue;
			
			Block b = Block.blocksList[placeon];
			if(b.blockMaterial.getIsSolid()) {
				return i;
			}
		}
		
		return 6;
	}
	public int findItemToPlace() {
		for(int i = 0; i < 9; ++i) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			
			if(stack != null && stack.getItem() instanceof ItemBlock) {
				if(stack.stackSize == 0) {
					mc.thePlayer.inventory.mainInventory[i] = null;
					continue;
				}
				ItemBlock bl = (ItemBlock) stack.getItem();
				if(!this.enableBlockFilter.value) {
					if(Block.blocksList[bl.blockID].blockMaterial.getIsSolid()) return i;
					continue;
				}else if(this.filter.blocks[bl.blockID]) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public boolean isLiquid(int id) {
		return id == Block.waterMoving.blockID || id == Block.waterStill.blockID || id == Block.lavaMoving.blockID || id == Block.lavaStill.blockID;
	}
	
	public boolean isBlockWalkable(int id) {
		Block b = Block.blocksList[id];
		if(b == null || this.isLiquid(id)) return false;
		return b.blockMaterial.getIsSolid() && b.renderAsNormalBlock();
	}
	
	public void tryPlacingXYZ(int x, int y, int z) {
		if(this.placeTimer > 0) return;
		int it = this.findItemToPlace();
		int prev = -1;
		if(it != -1) {
			prev = mc.thePlayer.inventory.currentItem;
			mc.thePlayer.inventory.currentItem = it;
			if(mc.isMultiplayerWorld()) {
				((PlayerControllerMP)mc.playerController).func_730_e();
			}
		}
		
		if(this.isBlockWalkable(mc.theWorld.getBlockId(x, y, z))) {
			return;
		}
		
		int face = this.getPossiblePlaceSide(x, y, z);
		if(face != 6) {
			if(face == 0) ++y;
			if(face == 1) --y;
			if(face == 2) ++z;
			if(face == 3) --z;
			if(face == 4) ++x;
			if(face == 5) --x;
			if(this.clientSidePlace.getValue()) PlayerUtils.placeBlockUnsafe(x, y, z, face);
			else PlayerUtils.placeBlock(x, y, z, face);
			placeTimer = this.placeDelay.getValue();
		}
		
		if(it != -1) {
			mc.thePlayer.inventory.currentItem = prev;
			if(mc.isMultiplayerWorld()) {
				((PlayerControllerMP)mc.playerController).func_730_e();
			}
		}
	}
	
	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventWorldRenderPreFog) {
			
			double rendX = RenderManager.renderPosX;
			double rendY = RenderManager.renderPosY;
			double rendZ = RenderManager.renderPosZ;
			GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			if(this.isSelected) {
				RenderUtils.drawOutlinedBlockBB(this.selectedBlockX - rendX, this.selectedBlockY - rendY, this.selectedBlockZ - rendZ);
			}
			
			for(BlockPos pos : currentlyDestroying){
				RenderUtils.drawOutlinedBlockBB(pos.x - rendX, pos.y - rendY, pos.z - rendZ);
			}
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
		}else if(event instanceof EventPlayerUpdatePost) {
			currentlyDestroying.clear();
			Direction dir = this.getDirection();
			int x = MathHelper.floor_double(mc.thePlayer.posX);
			int y = MathHelper.floor_double(mc.thePlayer.posY);
			int z = MathHelper.floor_double(mc.thePlayer.posZ);
			boolean success = false;
			if(this.placeTimer > 0) --this.placeTimer;
			if(this.backFill.getValue()) {
				int xb = x;
				int yb = y;
				int zb = z;
				for(int i = 0; i < this.backfillReach.getValue(); ++i) {
					xb -= dir.offX;
					yb -= dir.offY;
					zb -= dir.offZ;
					this.tryPlacingXYZ(xb, yb, zb);
					this.tryPlacingXYZ(xb, yb-1, zb);
				}
			}
			
			for(int i = 0; i < 4 && !success; ++i) {
				x += dir.offX;
				y += dir.offY;
				z += dir.offZ;
				success = this.trySettingXYZ(x, y, z);
				if(!success) success = this.trySettingXYZ(x, y - 1, z);
			}
			
			if(this.isSelected) {
				if(this.swing.value) mc.thePlayer.swingItem();
				if(this.breakMode.currentMode.equalsIgnoreCase("InstantSingle")) {
		            PlayerUtils.destroyBlockInstant(this.selectedBlockX, this.selectedBlockY, this.selectedBlockZ, dir.hitSide);
				}else {
					PlayerUtils.destroyBlock(this.selectedBlockX, this.selectedBlockY, this.selectedBlockZ, dir.hitSide);
				}
			}
		}
	}

}
