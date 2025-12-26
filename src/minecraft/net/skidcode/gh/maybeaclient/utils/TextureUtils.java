package net.skidcode.gh.maybeaclient.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextureUtils {
	
	public static int getAlpha(BufferedImage tex, int x, int y) {
		if(x >= tex.getWidth() || x < 0) return 0;
		if(y >= tex.getHeight() || y < 0) return 0;
		
		return (tex.getRGB(x, y) >> 24) & 0xff;
	}
	
	public static BufferedImage generateOutlinedTexture(BufferedImage tex) throws IOException {
		BufferedImage out = new BufferedImage(tex.getWidth(), tex.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for(int tx = 0; tx < tex.getWidth(); tx += 16) {
			for(int ty = 0; ty < tex.getHeight(); ty += 16) {
				int txmi = tx;
				int txma = tx+15;
				int tymi = ty;
				int tyma = ty+15;
				for(int y = tymi; y < tyma; ++y) {
					boolean hp = false;
					for(int x = txmi; x < txma; ++x) {
						int a = getAlpha(tex, x, y);
						
						if(hp) {
							if(y != tyma && getAlpha(tex, x, y+1) == 0) hp = false;
							if(y != tymi && getAlpha(tex, x, y-1) == 0) hp = false;
							
							if(x != txma && getAlpha(tex, x+1, y) == 0) hp = false;
							if(x != txmi && getAlpha(tex, x-1, y) == 0) hp = false;
							
						}
						
						if(hp && x == txma) hp = false;
						if(hp || a == 0) {
							out.setRGB(x, y, 0x00000000);
						}else {
							out.setRGB(x, y, 0xffffffff);
							hp = true;
						}
					}
				}
				
			}
		}
		return out;
	}

	public static byte[] getOutliningSides(BufferedImage tex) {
		byte[] arr = new byte[tex.getWidth()*tex.getHeight()];
		
		int m = (int) tex.getWidth() / 16;
		int n = m - 1;
		
		for(int tx = 0; tx < tex.getWidth(); ++tx) {
			for(int ty = 0; ty < tex.getHeight(); ++ty) {
				int a = getAlpha(tex, tx, ty);
				
				if(a != 0) {
					int xn = getAlpha(tex, tx-1, ty);
					int xp = getAlpha(tex, tx+1, ty);
					int yn = getAlpha(tex, tx, ty-1);
					int yp = getAlpha(tex, tx, ty+1);
					
					byte bm = 0;
					if(xn == 0 || tx % m == 0) bm |= 1; //needs left outline
					if(xp == 0 || tx % m == n) bm |= 2; //needs right outline
					if(yn == 0 || ty % m == 0) bm |= 4; //needs top outline
					if(yp == 0 || ty % m == n) bm |= 8; //needs top outline
					
					arr[tx*tex.getWidth() + ty] = bm;
				}
			}
		}
		
		
		return arr;
	}
}
