package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.Packet53BlockChange;
import net.minecraft.src.RenderManager;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPacketReceive;
import net.skidcode.gh.maybeaclient.events.impl.EventWorldRenderPreFog;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingColor;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingInteger;
import net.skidcode.gh.maybeaclient.utils.RenderUtils;
import net.skidcode.gh.maybeaclient.utils.TwoIntHashSet;

public class NewChunksHack extends Hack implements EventListener{
	public TwoIntHashSet chunks = new TwoIntHashSet();
	
	public SettingInteger yStart = new SettingInteger(this, "YStart", 0, 0, 127);
	public SettingInteger height = new SettingInteger(this, "Height", 0, 0, 127);
	public SettingColor color = new SettingColor(this, "Color", 255, 0, 0);
	
	
	public NewChunksHack() {
		super("NewChunks", "Shows newly loaded chunks", Keyboard.KEY_NONE, Category.RENDER);
		
		this.addSetting(this.yStart);
		this.addSetting(this.height);
		this.addSetting(this.color);
		
		EventRegistry.registerListener(EventPacketReceive.class, this);
		EventRegistry.registerListener(EventWorldRenderPreFog.class, this);
	}

	@Override
	public void handleEvent(Event e) {
		if(e instanceof EventPacketReceive) {
			if(((EventPacketReceive) e).packet instanceof Packet53BlockChange) {
				Packet53BlockChange pk = (Packet53BlockChange) ((EventPacketReceive) e).packet;
				if(pk.type == 9 || pk.type == 11) {
					chunks.add(pk.xPosition >> 4, pk.zPosition >> 4);
				}
			}
		}else if(e instanceof EventWorldRenderPreFog) {
			GL11.glPushMatrix();
			GL11.glLineWidth(1);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glColor4ub((byte)this.color.red, (byte)this.color.green, (byte)this.color.blue, (byte)255);
			for(int i = 0; i < this.chunks.slots.length; ++i) {
				TwoIntHashSet.Element slot = this.chunks.slots[i];
				TwoIntHashSet.Element el, prev;
				for(el = slot; el != null; el = prev)
		        {
					prev = el.prev;
					int xw = el.xPos << 4;
					int zw = el.zPos << 4;
					
					double dist = mc.thePlayer.getDistance(xw, mc.thePlayer.posY, zw);
					if(dist > 512) {
						this.chunks.count--;
		                if(slot == el)
		                {
		                	this.chunks.slots[i] = prev;
		                } else
		                {
		                    slot.prev = prev;
		                }
					}else {
						double relX = xw - RenderManager.renderPosX;
						double relY = this.yStart.getValue() - RenderManager.renderPosY;
						double relZ = zw - RenderManager.renderPosZ;
						
						RenderUtils.drawOutlinedBB(relX, relY, relZ, relX + 16, relY + this.height.getValue(), relZ + 16);
					}
		        }
			}
			
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}
	}

	
	
}
