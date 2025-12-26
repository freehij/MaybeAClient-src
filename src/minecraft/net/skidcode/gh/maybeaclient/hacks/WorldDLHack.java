package net.skidcode.gh.maybeaclient.hacks;

import java.io.File;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkProviderClient;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.WorldClient;
import net.skidcode.gh.maybeaclient.events.Event;
import net.skidcode.gh.maybeaclient.events.EventListener;
import net.skidcode.gh.maybeaclient.events.EventRegistry;
import net.skidcode.gh.maybeaclient.events.impl.EventPacketReceive;
import net.skidcode.gh.maybeaclient.events.impl.EventPacketSend;
import net.skidcode.gh.maybeaclient.hacks.category.Category;

public class WorldDLHack extends Hack implements EventListener{
	public static WorldDLHack instance;
	public WorldDLHack() {
		super("WorldDL", "Creates a world download", Keyboard.KEY_NONE, Category.MISC);
		instance = this;
		EventRegistry.registerListener(EventPacketSend.class, this);
		EventRegistry.registerListener(EventPacketReceive.class, this);
		
	}
	
	public void onEnable() {
		if(mc.isMultiplayerWorld()) {
			this.startDownload();
		}else {
			this.status = false;
		}
	}
	
	public void onDisable() {
		if(mc.isMultiplayerWorld()) {
			this.stopDownload();
		}
	}
	
	public void stopDownload() {
		WorldClient worldclient = (WorldClient)mc.theWorld;
		worldclient.saveWorld(true, null);
		worldclient.worldInfo.setWorldName(mc.getSendQueue().netManager.remoteSocketAddress.toString()+" "+System.currentTimeMillis());
		worldclient.downloadThisWorld = false;
		worldclient.downloadChunkLoader = null;
		worldclient.downloadSaveHandler = null;
		mc.ingameGUI.addChatMessage("\247c[WorldDL] \247cDownload stopped.");
	}

	public void startDownload()
	{
		String s = mc.gameSettings.lastServer;
		if(s.isEmpty()) s = "Downloaded World";
		WorldClient worldclient = (WorldClient)mc.theWorld;
		worldclient.worldInfo.setWorldName(s+" "+System.currentTimeMillis());
		worldclient.downloadSaveHandler = (SaveHandler)mc.getSaveLoader().getSaveLoader(s, false);
		worldclient.downloadChunkLoader = worldclient.downloadSaveHandler.getChunkLoader(worldclient.worldProvider);
		worldclient.worldInfo.setSizeOnDisk(getFileSizeRecursive(worldclient.downloadSaveHandler.getSaveDirectory()));
		Chunk.wc = worldclient;
		((ChunkProviderClient)worldclient.chunkProvider).importOldTileEntities();
		worldclient.downloadThisWorld = true;
		mc.ingameGUI.addChatMessage("\247c[WorldDL] \247cDownloading everything you can see...");
		mc.ingameGUI.addChatMessage("\247c[WorldDL] \2476You can increase that area by travelling around.");
	}
	
	private long getFileSizeRecursive(File file)
	{
		long l = 0L;
		File afile[] = file.listFiles();
		File afile1[] = afile;
		int i = afile1.length;
		for(int j = 0; j < i; j++)
		{
			File file1 = afile1[j];
			if(file1.isDirectory())
			{
				l += getFileSizeRecursive(file1);
				continue;
			}
			if(file1.isFile())
			{
				l += file1.length();
			}
		}

		return l;
	}

	@Override
	public void handleEvent(Event event) {
		if(event instanceof EventPacketSend) {
			if(((EventPacketSend)event).packet instanceof Packet255KickDisconnect && this.status) {
				this.stopDownload();
			}
		}
		
		if(event instanceof EventPacketReceive) {
			if(((EventPacketReceive)event).packet instanceof Packet255KickDisconnect && this.status) {
				this.stopDownload();
			}
		}
	}
}
