package net.skidcode.gh.maybeaclient.hacks;

import java.util.ArrayList;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventWorldRenderPreFog;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingBlockChooser;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingColor;
import net.skidcode.gh.maybeaclient.hacks.settings.SettingFloat;
import net.skidcode.gh.maybeaclient.utils.BlockPos;

public class BlockESPHack extends Hack implements EventListener{
	
	public static HashSet<BlockPos> blocksToRender = new HashSet<>();
	public static ArrayList<BlockPos> removed = new ArrayList<>();
	public static BlockESPHack instance;
	
	public SettingColor color = new SettingColor(this, "Color", 255, 0, 0);
	public SettingFloat width = new SettingFloat(this, "Line Width", 1f, 1f, 5, 0.1f);
	
	public SettingBlockChooser blocks = new SettingBlockChooser(this, "Blocks") {
		public void blockChanged(int id) {
			mc.entityRenderer.updateRenderer();
	        mc.theWorld.markBlocksDirty((int)mc.thePlayer.posX - 256, 0, (int)mc.thePlayer.posZ - 256, (int)mc.thePlayer.posX + 256, 127, (int)mc.thePlayer.posZ + 256);
		}
	};
	
	public BlockESPHack() {
		super("BlockESP", "Outlines blocks", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
		this.addSetting(this.blocks);
		this.addSetting(this.color);
		this.addSetting(this.width);
		EventRegistry.registerListener(EventWorldRenderPreFog.class, this);
	}

	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventWorldRenderPreFog) {
			Tessellator tess = Tessellator.instance;
			
			GL11.glPushMatrix();
			GL11.glBlendFunc(770, 771);
			GL11.glColor3f(this.color.red / 255f, this.color.green / 255f, this.color.blue / 255f);
			GL11.glLineWidth(this.width.value);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			
			for(BlockPos pos : BlockESPHack.blocksToRender) {
				int id = mc.theWorld.getBlockId(pos.x, pos.y, pos.z);
				if (blocks.blocks[id]) {
					double renderX = pos.x - RenderManager.renderPosX;
					double renderY = pos.y - RenderManager.renderPosY;
					double renderZ = pos.z - RenderManager.renderPosZ;
					
			        tess.startDrawing(3);
			        tess.addVertex(renderX, renderY, renderZ);
			        tess.addVertex(renderX+1, renderY, renderZ);
			        tess.addVertex(renderX+1, renderY, renderZ+1);
			        tess.addVertex(renderX, renderY, renderZ+1);
			        tess.addVertex(renderX, renderY, renderZ);
			        tess.draw();
			        tess.startDrawing(3);
			        tess.addVertex(renderX, renderY+1, renderZ);
			        tess.addVertex(renderX+1, renderY+1, renderZ);
			        tess.addVertex(renderX+1, renderY+1, renderZ+1);
			        tess.addVertex(renderX, renderY+1, renderZ+1);
			        tess.addVertex(renderX, renderY+1, renderZ);
			        tess.draw();
			        tess.startDrawing(1);
			        tess.addVertex(renderX, renderY, renderZ);
			        tess.addVertex(renderX, renderY+1, renderZ);
			        tess.addVertex(renderX+1, renderY, renderZ);
			        tess.addVertex(renderX+1, renderY+1, renderZ);
			        tess.addVertex(renderX+1, renderY, renderZ+1);
			        tess.addVertex(renderX+1, renderY+1, renderZ+1);
			        tess.addVertex(renderX, renderY, renderZ+1);
			        tess.addVertex(renderX, renderY+1, renderZ+1);
			        tess.draw();
				} else {
					removed.add(pos);
				}
			}
			
			int i = removed.size();
			while (--i >= 0) {
				BlockPos pos = removed.remove(i);
				blocksToRender.remove(pos);
			}
			
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}
	}
}
