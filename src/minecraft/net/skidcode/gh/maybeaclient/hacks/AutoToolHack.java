package net.skidcode.gh.maybeaclient.hacks;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class AutoToolHack extends Hack{

	public static AutoToolHack instance;

	public AutoToolHack() {
		super("AutoTool", "Automatically selects tools from hotbar", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
	}
	
	public static int getBestSlot(Block block) {
		int prev = mc.thePlayer.inventory.currentItem;
		int bestSlot = prev;
		float bestStrength = block.blockStrength(mc.thePlayer);
    	for(int i = 0; i < 9; ++i) {
    		mc.thePlayer.inventory.currentItem = i;
    		float strength = block.blockStrength(mc.thePlayer);
    		if(strength > bestStrength) {
    			bestStrength = strength;
    			bestSlot = i;
    		}
    	}
    	mc.thePlayer.inventory.currentItem = prev;
    	return bestSlot;
	}
	
}
