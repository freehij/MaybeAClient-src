package net.skidcode.gh.maybeaclient.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import net.skidcode.gh.maybeaclient.Client;

public class RenderUtils {
	
	public static void drawOutlinedBlockBB(double x, double y, double z) {
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x + 1, y, z);
        tessellator.addVertex(x + 1, y, z + 1);
        tessellator.addVertex(x, y, z + 1);
        tessellator.addVertex(x, y, z);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.addVertex(x, y + 1, z);
        tessellator.addVertex(x + 1, y + 1, z);
        tessellator.addVertex(x + 1, y + 1, z + 1);
        tessellator.addVertex(x, y + 1, z + 1);
        tessellator.addVertex(x, y + 1, z);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x, y + 1, z);
        tessellator.addVertex(x + 1, y, z);
        tessellator.addVertex(x + 1, y + 1, z);
        tessellator.addVertex(x + 1, y, z + 1);
        tessellator.addVertex(x + 1, y + 1, z + 1);
        tessellator.addVertex(x, y, z + 1);
        tessellator.addVertex(x, y + 1, z + 1);
        tessellator.draw();
	}
	public static void drawOutlinedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.addVertex(minX, maxY, minZ);
        tessellator.addVertex(maxX, maxY, minZ);
        tessellator.addVertex(maxX, maxY, maxZ);
        tessellator.addVertex(minX, maxY, maxZ);
        tessellator.addVertex(minX, maxY, minZ);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(minX, maxY, minZ);
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, maxY, minZ);
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(maxX, maxY, maxZ);
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(minX, maxY, maxZ);
        tessellator.draw();
	}
	public static void drawOutlinedBB(AxisAlignedBB bb) {
		drawOutlinedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
	}
	public static void drawString(String s, double d, double d1, double d2)
    {
        double f = Math.sqrt(
        	(d - Client.mc.renderViewEntity.posX)*(d - Client.mc.renderViewEntity.posX) + 
        	(d1 - Client.mc.renderViewEntity.posY)*(d1 - Client.mc.renderViewEntity.posY) + 
        	(d2 - Client.mc.renderViewEntity.posZ)*(d2 - Client.mc.renderViewEntity.posZ)
        );
        FontRenderer fontrenderer = Client.mc.fontRenderer;
        float f1 = 1.6F;
        float f2 = 0.01666667F * f1;
        
			float scale = 1;
			if(f > 200) f = 200; 
			f2 *= scale * 0.1f * f;
			
			if(0.016666668F * f1 > f2) f2 = 0.016666668F * f1;
        
        
        GL11.glPushMatrix();
        GL11.glTranslated(d - RenderManager.renderPosX, d1 - RenderManager.renderPosY + 2.3D, d2 - RenderManager.renderPosZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f2, -f2, f2);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDepthMask(false);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        byte byte0 = 0;
        if(s.equals("deadmau5"))
        {
            byte0 = -10;
        }
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        tessellator.startDrawingQuads();
        int j = fontrenderer.getStringWidth(s) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-j - 1, -1 + byte0, 0.0D);
        tessellator.addVertex(-j - 1, 8 + byte0, 0.0D);
        tessellator.addVertex(j + 1, 8 + byte0, 0.0D);
        tessellator.addVertex(j + 1, -1 + byte0, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, 0x20ffffff);
        GL11.glDepthMask(true);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, -1);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glPopMatrix();
    }
	
}
