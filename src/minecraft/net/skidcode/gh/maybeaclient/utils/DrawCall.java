package net.skidcode.gh.maybeaclient.utils;

public class DrawCall {
	public byte xStart, xEnd;
	public byte yStart, yEnd;
	public DrawCall(int side, int x, int y, int xx, int yy) {
		
		this.xStart = (byte) x;
		this.xEnd = (byte) xx;
		this.yEnd = (byte) yy;
		this.yStart = (byte) y;
		
		if(side == 2) {
			this.xStart += 1;
			this.xEnd += 1;
		}
		if(side == 8) {
			this.yStart += 1;
			this.yEnd += 1;
		}
	}
}
