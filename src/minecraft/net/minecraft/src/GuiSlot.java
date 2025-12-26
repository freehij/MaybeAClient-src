package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiSlot {
    private final Minecraft mc;
    protected final int width;
    private final int height;
    private final int top;
    private final int bottom;
    private final int right;
    private final int left;
    protected final int posZ;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    private float initialClickY = -2.0F;
    private float scrollMultiplier;
    private float amountScrolled;
    private int selectedElement = -1;
    private long lastClicked = 0L;
    private boolean field_25123_p = true;

    public GuiSlot(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
        this.mc = var1;
        this.width = var2;
        this.height = var3;
        this.top = var4;
        this.bottom = var5;
        this.posZ = var6;
        this.left = 0;
        this.right = var2;
    }

    protected abstract int getSize();

    protected abstract void elementClicked(int var1, boolean var2);

    protected abstract boolean isSelected(int var1);

    protected abstract int getContentHeight();

    protected abstract void drawBackground();

    protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

    public void registerScrollButtons(List var1, int scrollUp, int scrollDown) {
        this.scrollUpButtonID = scrollUp;
        this.scrollDownButtonID = scrollDown;
    }

    private void bindAmountScrolled() {
        int var1 = this.getContentHeight() - (this.bottom - this.top - 4);
        if (var1 < 0) {
            var1 /= 2;
        }

        if (this.amountScrolled < 0.0F) {
            this.amountScrolled = 0.0F;
        }

        if (this.amountScrolled > (float)var1) {
            this.amountScrolled = (float)var1;
        }

    }

    public void actionPerformed(GuiButton var1) {
        if (var1.enabled) {
            if (var1.id == this.scrollUpButtonID) {
                this.amountScrolled -= (float)(this.posZ * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            } else if (var1.id == this.scrollDownButtonID) {
                this.amountScrolled += (float)(this.posZ * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }

        }
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawBackground();
        int var4 = this.getSize();
        int var5 = this.width / 2 + 124;
        int var6 = var5 + 6;
        int var9;
        int var11;
        int var18;
        if (Mouse.isButtonDown(0)) {
            if (this.initialClickY == -1.0F) {
                if (var2 >= this.top && var2 <= this.bottom) {
                    int var7 = this.width / 2 - 110;
                    int var8 = this.width / 2 + 110;
                    var9 = (var2 - this.top + (int)this.amountScrolled - 2) / this.posZ;
                    if (var1 >= var7 && var1 <= var8 && var9 >= 0 && var9 < var4) {
                        boolean var10 = var9 == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L;
                        this.elementClicked(var9, var10);
                        this.selectedElement = var9;
                        this.lastClicked = System.currentTimeMillis();
                    }

                    if (var1 >= var5 && var1 <= var6 && this.getContentHeight() > 0) {
                        this.scrollMultiplier = -1.0F;
                        var18 = this.getContentHeight() - (this.bottom - this.top - 4);
                        if (var18 < 1) {
                            var18 = 1;
                        }

                        var11 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                        if (var11 < 32) {
                            var11 = 32;
                        }

                        if (var11 > this.bottom - this.top - 8) {
                            var11 = this.bottom - this.top - 8;
                        }

                        this.scrollMultiplier /= (float)(this.bottom - this.top - var11) / (float)var18;
                    } else {
                        this.scrollMultiplier = 1.0F;
                    }

                    this.initialClickY = (float)var2;
                } else {
                    this.initialClickY = -2.0F;
                }
            } else if (this.initialClickY >= 0.0F) {
                this.amountScrolled -= ((float)var2 - this.initialClickY) * this.scrollMultiplier;
                this.initialClickY = (float)var2;
            }
        } else {
            this.initialClickY = -1.0F;
        }

        this.bindAmountScrolled();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(2912 /*GL_FOG*/);
        Tessellator var15 = Tessellator.instance;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var16 = 32.0F;
        var15.startDrawingQuads();
        var15.setColorOpaque_I(2105376);
        var15.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, (double)((float)this.left / var16), (double)((float)(this.bottom + (int)this.amountScrolled) / var16));
        var15.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, (double)((float)this.right / var16), (double)((float)(this.bottom + (int)this.amountScrolled) / var16));
        var15.addVertexWithUV((double)this.right, (double)this.top, 0.0D, (double)((float)this.right / var16), (double)((float)(this.top + (int)this.amountScrolled) / var16));
        var15.addVertexWithUV((double)this.left, (double)this.top, 0.0D, (double)((float)this.left / var16), (double)((float)(this.top + (int)this.amountScrolled) / var16));
        var15.draw();

        int var12;
        for(var9 = 0; var9 < var4; ++var9) {
            var18 = this.width / 2 - 92 - 16;
            var11 = this.top + 4 + var9 * this.posZ - (int)this.amountScrolled;
            var12 = this.posZ - 4;
            if (this.field_25123_p && this.isSelected(var9)) {
                int var13 = this.width / 2 - 110;
                int var14 = this.width / 2 + 110;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                var15.startDrawingQuads();
                var15.setColorOpaque_I(8421504);
                var15.addVertexWithUV((double)var13, (double)(var11 + var12 + 2), 0.0D, 0.0D, 1.0D);
                var15.addVertexWithUV((double)var14, (double)(var11 + var12 + 2), 0.0D, 1.0D, 1.0D);
                var15.addVertexWithUV((double)var14, (double)(var11 - 2), 0.0D, 1.0D, 0.0D);
                var15.addVertexWithUV((double)var13, (double)(var11 - 2), 0.0D, 0.0D, 0.0D);
                var15.setColorOpaque_I(0);
                var15.addVertexWithUV((double)(var13 + 1), (double)(var11 + var12 + 1), 0.0D, 0.0D, 1.0D);
                var15.addVertexWithUV((double)(var14 - 1), (double)(var11 + var12 + 1), 0.0D, 1.0D, 1.0D);
                var15.addVertexWithUV((double)(var14 - 1), (double)(var11 - 1), 0.0D, 1.0D, 0.0D);
                var15.addVertexWithUV((double)(var13 + 1), (double)(var11 - 1), 0.0D, 0.0D, 0.0D);
                var15.draw();
                GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            }

            this.drawSlot(var9, var18, var11, var12, var15);
        }

        byte var17 = 4;
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glShadeModel(7425 /*GL_SMOOTH*/);
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        var15.startDrawingQuads();
        var15.setColorRGBA_I(0, 0);
        var15.addVertexWithUV((double)this.left, (double)(this.top + var17), 0.0D, 0.0D, 1.0D);
        var15.addVertexWithUV((double)this.right, (double)(this.top + var17), 0.0D, 1.0D, 1.0D);
        var15.setColorRGBA_I(0, 255);
        var15.addVertexWithUV((double)this.right, (double)this.top, 0.0D, 1.0D, 0.0D);
        var15.addVertexWithUV((double)this.left, (double)this.top, 0.0D, 0.0D, 0.0D);
        var15.draw();
        var15.startDrawingQuads();
        var15.setColorRGBA_I(0, 255);
        var15.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, 0.0D, 1.0D);
        var15.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, 1.0D, 1.0D);
        var15.setColorRGBA_I(0, 0);
        var15.addVertexWithUV((double)this.right, (double)(this.bottom - var17), 0.0D, 1.0D, 0.0D);
        var15.addVertexWithUV((double)this.left, (double)(this.bottom - var17), 0.0D, 0.0D, 0.0D);
        var15.draw();
        var18 = this.getContentHeight() - (this.bottom - this.top - 4);
        if (var18 > 0) {
            var11 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            if (var11 < 32) {
                var11 = 32;
            }

            if (var11 > this.bottom - this.top - 8) {
                var11 = this.bottom - this.top - 8;
            }

            var12 = (int)this.amountScrolled * (this.bottom - this.top - var11) / var18 + this.top;
            if (var12 < this.top) {
                var12 = this.top;
            }

            var15.startDrawingQuads();
            var15.setColorRGBA_I(0, 255);
            var15.addVertexWithUV((double)var5, (double)this.bottom, 0.0D, 0.0D, 1.0D);
            var15.addVertexWithUV((double)var6, (double)this.bottom, 0.0D, 1.0D, 1.0D);
            var15.addVertexWithUV((double)var6, (double)this.top, 0.0D, 1.0D, 0.0D);
            var15.addVertexWithUV((double)var5, (double)this.top, 0.0D, 0.0D, 0.0D);
            var15.draw();
            var15.startDrawingQuads();
            var15.setColorRGBA_I(8421504, 255);
            var15.addVertexWithUV((double)var5, (double)(var12 + var11), 0.0D, 0.0D, 1.0D);
            var15.addVertexWithUV((double)var6, (double)(var12 + var11), 0.0D, 1.0D, 1.0D);
            var15.addVertexWithUV((double)var6, (double)var12, 0.0D, 1.0D, 0.0D);
            var15.addVertexWithUV((double)var5, (double)var12, 0.0D, 0.0D, 0.0D);
            var15.draw();
            var15.startDrawingQuads();
            var15.setColorRGBA_I(12632256, 255);
            var15.addVertexWithUV((double)var5, (double)(var12 + var11 - 1), 0.0D, 0.0D, 1.0D);
            var15.addVertexWithUV((double)(var6 - 1), (double)(var12 + var11 - 1), 0.0D, 1.0D, 1.0D);
            var15.addVertexWithUV((double)(var6 - 1), (double)var12, 0.0D, 1.0D, 0.0D);
            var15.addVertexWithUV((double)var5, (double)var12, 0.0D, 0.0D, 0.0D);
            var15.draw();
        }

        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glShadeModel(7424 /*GL_FLAT*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
    }

    private void overlayBackground(int var1, int var2, int var3, int var4) {
        Tessellator var5 = Tessellator.instance;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var6 = 32.0F;
        var5.startDrawingQuads();
        var5.setColorRGBA_I(4210752, var4);
        var5.addVertexWithUV(0.0D, (double)var2, 0.0D, 0.0D, (double)((float)var2 / var6));
        var5.addVertexWithUV((double)this.width, (double)var2, 0.0D, (double)((float)this.width / var6), (double)((float)var2 / var6));
        var5.setColorRGBA_I(4210752, var3);
        var5.addVertexWithUV((double)this.width, (double)var1, 0.0D, (double)((float)this.width / var6), (double)((float)var1 / var6));
        var5.addVertexWithUV(0.0D, (double)var1, 0.0D, 0.0D, (double)((float)var1 / var6));
        var5.draw();
    }
}
