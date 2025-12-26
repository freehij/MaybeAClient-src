package net.skidcode.gh.maybeaclient.utils;

public class BlockPos {
	public final int x;
	public final int y;
	public final int z;
	public final int hash;
	
	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.hash = hash(x, hash(y, z));
	}
	
	private int hash(int a, int b) {
		return (a + b + 1) * (a + b) / 2 + b;
	}
	
	@Override
	public boolean equals(Object o) {
		BlockPos p = (BlockPos) o;
		return p.x == this.x && p.y == this.y && p.z == this.z;
	}
	
	public int hashCode() {
		return this.hash;
	}
}
