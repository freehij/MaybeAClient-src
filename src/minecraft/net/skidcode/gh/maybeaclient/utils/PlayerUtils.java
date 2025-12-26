package net.skidcode.gh.maybeaclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.PlayerControllerMP;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.AutoToolHack;

public class PlayerUtils {
	
	public static Minecraft mc;
	
	public static Direction getDirection() {
		EntityPlayer player = mc.thePlayer;
		int dir = MathHelper.floor_float((player.rotationYaw * 4.0f) / 360.0f + 0.5f) & 3;
		return Direction.values()[dir];
	}
	
	public static boolean destroyBlock(int x, int y, int z, int side) {
		mc.playerController.clickBlock(x, y, z, side); //force set xyz block
		mc.playerController.sendBlockRemoving(x, y, z, side); //send removing
		mc.playerController.field_1064_b = mc.playerController.isBeingUsed();
		return mc.playerController.isBeingUsed();
	}
	
	public static float wrapAngle180(float angle){
		angle = angle % 360;
		
		if(angle >= 180) angle -= 360;
		if(angle < -180) angle += 360;
		return angle;
	}
	public static boolean placeBlockUnsafe(int x, int y, int z, int side) {
		mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y, z, side);
		return mc.playerController.isBeingUsed();
	}
	
	public static boolean placeBlock(int x, int y, int z, int side) {
		if(mc.isMultiplayerWorld()) {
			mc.getSendQueue().addToSendQueue(new net.minecraft.src.Packet15Place(x, y, z, side, mc.thePlayer.getCurrentEquippedItem()));
			return true;
		}
		
		return placeBlockUnsafe(x, y, z, side);
	}
	public static void destroyBlockInstant(int x, int y, int z, int side) {
		
		if(AutoToolHack.instance.status) {
	       	mc.thePlayer.inventory.currentItem = AutoToolHack.getBestSlot(Block.blocksList[mc.theWorld.getBlockId(x, y, z)]);
        }
		
		if(mc.isMultiplayerWorld()) {
			PlayerControllerMP mp = (PlayerControllerMP) mc.playerController;
			mp.func_730_e();
			mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, x, y, z, side));
			mc.getSendQueue().addToSendQueue(new Packet14BlockDig(2, x, y, z, side));
		}else {
			mc.playerController.sendBlockRemoved(x, y, z, side);
		}
	}

	public static double getSpeed(boolean horizontal) {
		double diffX = (mc.thePlayer.prevPosX - mc.thePlayer.posX)*mc.timer.ticksPerSecond;
		double diffZ = (mc.thePlayer.prevPosZ - mc.thePlayer.posZ)*mc.timer.ticksPerSecond;
		if(horizontal) {
			double diffY = (mc.thePlayer.prevPosY - mc.thePlayer.posY)*mc.timer.ticksPerSecond;
			return Math.sqrt(diffX*diffX + diffY*diffY + diffZ*diffZ);
		}
		
		return Math.sqrt(diffX*diffX + diffZ*diffZ);
	}

	public static boolean isInNether() {
		int x = MathHelper.floor_double(mc.thePlayer.posX);
		int z = MathHelper.floor_double(mc.thePlayer.posZ);
		
		return mc.theWorld.getBlockId(x, 127, z) == Block.bedrock.blockID;
	}

	public static void useItem() {
		ItemStack var10 = mc.thePlayer.inventory.getCurrentItem();
        if (var10 != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, var10)) {
        	mc.entityRenderer.itemRenderer.func_9450_c();
        }
	}
}
