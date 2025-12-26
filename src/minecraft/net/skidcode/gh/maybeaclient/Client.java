package net.skidcode.gh.maybeaclient;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagInt;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.console.*;
import net.skidcode.gh.maybeaclient.gui.altman.AccountInfo;
import net.skidcode.gh.maybeaclient.gui.altman.GuiAccManager;
import net.skidcode.gh.maybeaclient.gui.altman.PasswordInfo;
import net.skidcode.gh.maybeaclient.gui.altman.PasswordInfo.MatchMode;
import net.skidcode.gh.maybeaclient.gui.click.ClickGUI;
import net.skidcode.gh.maybeaclient.gui.click.Tab;
import net.skidcode.gh.maybeaclient.gui.server.GuiServerSelector;
import net.skidcode.gh.maybeaclient.gui.server.ServerInfo;
import net.skidcode.gh.maybeaclient.hacks.*;
import net.skidcode.gh.maybeaclient.hacks.settings.Setting;
import net.skidcode.gh.maybeaclient.utils.ChatColor;
import net.skidcode.gh.maybeaclient.utils.DrawCall;
import net.skidcode.gh.maybeaclient.utils.TextureUtils;

public class Client {
	public static HashMap<String, Hack> hacksByName = new HashMap<>();
	public static HashMap<String, Command> commands = new HashMap<>();
	
	public static HashMap<String, String> aliases = new HashMap<>();
	public static ArrayList<String> aliasesList = new ArrayList<>();
	
	public static String cmdPrefix = ".";
	public static Minecraft mc;
	public static final String clientName = "MaybeAClient";
	public static final String clientVersion = "1.9";
	
	public static final int saveVersion = 3; 
	/*
	 * Save format 2(1.3):
	 *  * SettingColor saves the data as CompoundTag instead of ByteArray
	 * Save format 3(1.5):
	 *  * ClickGUI tabs save priority into the tag.
	 */
	
	public static int convertingVersion = 0;
	
	public static void registerHack(Hack hack) {
		Client.hacksByName.put(hack.name.toLowerCase(), hack);
	}
	
	public static void registerCommand(Command cmd) {
		Client.commands.put(cmd.name.toLowerCase(), cmd);
	}
	
	public static void addMessage(String msg) {
		addMessageRaw(String.format("%s[%s]:%s %s", ChatColor.CYAN, Client.clientName, ChatColor.WHITE, msg));
	}
	
	public static void addMessageRaw(String msg) {
		mc.thePlayer.addChatMessage(msg);
	}
	public static void handleCommand(String command) {
		handleCommand(command, true);
	}
	public static void handleCommand(String command, boolean sendToChat) {
		String[] cmds = command.split(" ");
		int parcnt = cmds.length;
		if(sendToChat) mc.thePlayer.addChatMessageWithMoreOpacityBG(ChatColor.LIGHTGRAY+Client.cmdPrefix+command);
		Command cmd = Client.commands.get(cmds[0].toLowerCase());
		
		if(cmd == null) {
			
			String cmdalias = Client.aliases.get(cmds[0].toLowerCase());
			if(cmdalias != null) {
				String newcommand = cmdalias;
				for(int i = 1; i < cmds.length; ++i) {
					newcommand += " " + cmds[i];
				}
				
				Client.handleCommand(newcommand, false);
			}else {
				Client.addMessage("Invalid command: "+ChatColor.GOLD+cmds[0]+ChatColor.WHITE+". Use "+ChatColor.GOLD+Client.cmdPrefix+"help"+ChatColor.WHITE+" to view the available commands.");
			}
		
		}else {
			String[] args = Arrays.copyOfRange(cmds, 1, cmds.length);
			cmd.onTyped(args);
		}
		
	}
	
	public static String getAlias(String alias) {
		return Client.aliases.get(alias.toLowerCase());
	}
	public static void removeAlias(String alias) {
		alias = alias.toLowerCase();
		Client.aliases.remove(alias);
		if(Client.aliasesList.contains(alias)) {
			Client.aliasesList.remove(alias);
		}
		
	}
	public static void addAlias(String alias, String command) {
		alias = alias.toLowerCase();
		Client.aliases.put(alias, command);
		
		if(!Client.aliasesList.contains(alias)) {
			Client.aliasesList.add(alias);
		}
	}
	
	public static void overrideHackInfo(Hack hack, NBTTagCompound input) {
		hack.status = input.getBoolean("Status");
		if(input.hasKey("Keybind")) { //XXX older versions
			hack.keybinding.setValue(input.getInteger("Keybind"));
		}
		NBTTagCompound settings = input.getCompoundTag("Settings");
		if(hack.equals(EntityESPHack.instance)) {
			if(settings.hasKey("Mode")) {
				EntityESPHack.instance.animalsMode.setValue(settings.getString("Mode"));
				EntityESPHack.instance.hostileMode.setValue(settings.getString("Mode"));
				EntityESPHack.instance.playersMode.setValue(settings.getString("Mode"));
			}
		}
		
		for(Map.Entry<String, Setting> entry : hack.settings.entrySet()) {
			entry.getValue().readFromNBT(settings);
		}
	}
	
	public static void writeHackInfo(Hack hack, NBTTagCompound output) {
		NBTTagCompound hacc = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		hacc.setBoolean("Status", hack.status);
		
		NBTTagCompound settings = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		for(Map.Entry<String, Setting> entry : hack.settings.entrySet()) {
			entry.getValue().writeToNBT(settings);
		}
		hacc.setCompoundTag("Settings", settings);
		
		output.setCompoundTag(hack.name.toLowerCase(), hacc);
	}
	
	public static void writeModuleSettings(File f) throws IOException {
		NBTTagCompound comp = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		
		for(Map.Entry<String, Hack> entry : Client.hacksByName.entrySet()) {
			Hack hack = entry.getValue();
			writeHackInfo(hack, comp);
		}
		
		
		NBTTagCompound write = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		write.setCompoundTag("Modules", comp);
		write.setInteger("FormatVersion", Client.saveVersion);
		write.writeTagContents(new DataOutputStream(new FileOutputStream(f)));
	}
	
	public static void saveModules() {
		try {
			File f = new File(Minecraft.getMinecraftDir()+"/MaybeAClient/");
			f.mkdirs();
			File ms = new File(f, "module-settings");
			if(!ms.exists()) ms.createNewFile();
			writeModuleSettings(ms);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to save module settings!");
		}
	}
	
	public static void saveClickGUI() {
		try {
			File f = new File(Minecraft.getMinecraftDir()+"/MaybeAClient/");
			f.mkdirs();
			File ms = new File(f, "clickgui-settings");
			if(!ms.exists()) ms.createNewFile();
			writeClickGUISettings(ms);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to save clickgui!");
		}
	}
	
	public static void saveAliases() {
		try {
			File f = new File(Minecraft.getMinecraftDir()+"/MaybeAClient/");
			f.mkdirs();
			File ms = new File(f, "aliases");
			if(!ms.exists()) ms.createNewFile();
			writeCommandAliases(ms);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to save aliases!");
		}
	}
	
	public static void readModuleSettings(File f) throws IOException {
		NBTTagCompound modules = null;
		NBTTagInt version = null;
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		while(true) {
			NBTBase base = NBTBase.readTag(dis);
			
			if(base instanceof NBTTagInt) {
				version = (NBTTagInt) base;
			}else if(base instanceof NBTTagCompound) {//TODO rework
				modules = (NBTTagCompound) base;
			}else {
				if(modules == null) {
					System.out.println("No module information was found!");
				}
				if(version == null) {
					System.out.println("No version was found!");
				}
				break;
			}
		}
		if(modules == null) return;
		
		if(version.intValue != Client.saveVersion) {
			System.out.println(String.format("Module settings save version does not match client(%d != %d). Converting...", version.intValue, Client.saveVersion));
			Client.convertingVersion = version.intValue;
		}
		
		for(Map.Entry<String, Hack> entry : Client.hacksByName.entrySet()) {
			Hack hack = entry.getValue();
			if(!modules.hasKey(hack.name.toLowerCase())) {
				if(hack.name.equalsIgnoreCase(ClientInfoHack.instance.name) && modules.hasKey("coords")) {
					NBTTagCompound hacc = modules.getCompoundTag("coords");
					overrideHackInfo(hack, hacc);
				}else if(hack.name.equalsIgnoreCase(NoClientSideDestroyHack.instance.name) && modules.hasKey("noclientsidedestroy")) {
					NBTTagCompound hacc = modules.getCompoundTag("noclientsidedestroy");
					overrideHackInfo(hack, hacc);
				}else {
					System.out.println("Information about "+hack.name+" was not found");
				}
			}else {
				NBTTagCompound hacc = modules.getCompoundTag(hack.name.toLowerCase());
				overrideHackInfo(hack, hacc);
			}
			writeHackInfo(hack, modules);
		}
		
		NBTTagCompound write = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		write.setCompoundTag("Modules", modules);
		write.setInteger("FormatVersion", Client.saveVersion);
		write.writeTagContents(new DataOutputStream(new FileOutputStream(f)));
		Client.convertingVersion = 0;
	}
	
	
	
	public static void readClickGUISettings(File f) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		NBTBase base = NBTBase.readTag(dis);
		NBTTagCompound cc = (NBTTagCompound) base;
		NBTTagCompound clickgui = cc.getCompoundTag("ClickGUI");
		int version = cc.getInteger("FormatVersion");
		if(version != Client.saveVersion) {
			System.out.println(String.format("Gui settings save version does not match client(%d != %d). Converting...", version, Client.saveVersion));
			Client.convertingVersion = version;
		}
		int lowest = Integer.MIN_VALUE;
		HashMap<Integer, Tab> priority = new HashMap<>();
		for(Tab tab : ClickGUI.tabs) {
			NBTTagCompound tg = clickgui.getCompoundTag(tab.name.toLowerCase());
			if(!clickgui.hasKey(tab.name.toLowerCase())) {
				System.out.println("Information about "+tab.name+" not found.");
				priority.put(lowest++, tab);
			}else {
				tab.readFromNBT(tg);
				if(tg.hasKey("Priority")) {
					int tabprio = tg.getInteger("Priority");
					if(priority.containsKey(tabprio)) {
						System.out.println("Priortity "+tabprio+" was set to another tab already!");
						priority.put(lowest++, tab);
					}else {
						priority.put(tabprio, tab);
					}
				}else {
					priority.put(lowest++, tab);
				}
			}
		}
		
		ClickGUI.tabs.clear();
		ArrayList<Map.Entry<Integer, Tab>> arr = new ArrayList<>(priority.entrySet());
		arr.sort(Map.Entry.comparingByKey());
		for(Entry<Integer, Tab> entry : arr) {
			ClickGUI.tabs.add(entry.getValue());
		}
	}
	
	public static void writeClickGUISettings(File f) throws IOException {
		NBTTagCompound clickgui = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		
		for(Tab tab : ClickGUI.tabs) {
			NBTTagCompound tb = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
			clickgui.setCompoundTag(tab.name.toLowerCase(), tb);
			tab.writeToNBT(tb);
		}
		
		NBTTagCompound write1 = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		NBTTagCompound write2 = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		write1.setCompoundTag("ClickGUI", clickgui);
		write1.setInteger("FormatVersion", Client.saveVersion);
		write2.setCompoundTag("", write1);
		write2.writeTagContents(new DataOutputStream(new FileOutputStream(f)));
	}
	
	public static void readCommandAliases(File f) throws IOException {
		
		NBTTagCompound aliases = null;
		NBTTagInt version = null;
		DataInputStream in = new DataInputStream(new FileInputStream(f));
		while(true) {
			NBTBase base = NBTBase.readTag(in);
			
			if(base instanceof NBTTagInt) {
				version = (NBTTagInt) base;
			}else if(base instanceof NBTTagCompound) {
				aliases = (NBTTagCompound) base;
			}else {
				if(aliases == null) {
					System.out.println("No aliases information was found!");
				}
				if(version == null) {
					System.out.println("No version was found!");
				}
				break;
			}
		}
		
		for(Map.Entry<String, NBTBase> entry : aliases.tagMap.entrySet()) {
			String alias = entry.getKey();
			NBTBase bs = entry.getValue();
			if(bs instanceof NBTTagString) {
				NBTTagString cmd = (NBTTagString) bs;
				Client.addAlias(alias, cmd.stringValue);
			}else {
				System.out.println("Alias "+alias+" has wrong nbttag type("+bs.getType()+")!");
			}
		}
		
		NBTTagCompound write = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		write.setCompoundTag("Aliases", aliases);
		write.setInteger("FormatVersion", Client.saveVersion);
		write.writeTagContents(new DataOutputStream(new FileOutputStream(f)));
	}
	
	public static void writeCommandAliases(File f) throws IOException {
		NBTTagCompound comp = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		
		for(Map.Entry<String, String> entry : Client.aliases.entrySet()) {
			comp.setString(entry.getKey().toLowerCase(), entry.getValue());
		}
		
		
		NBTTagCompound write = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		write.setCompoundTag("Aliases", comp);
		write.setInteger("FormatVersion", Client.saveVersion);
		write.writeTagContents(new DataOutputStream(new FileOutputStream(f)));
	}
	
	public static void readCurrentServers(File f) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		NBTBase base = NBTBase.readTag(dis);
		NBTTagCompound cc = (NBTTagCompound) base;
		NBTTagList servers = cc.getTagList("Servers");
		for(int i = 0; i < servers.tagCount(); ++i) {
			NBTBase tag = servers.tagAt(i);
			if(tag instanceof NBTTagCompound) {
				NBTTagCompound server = (NBTTagCompound) tag;
				String name = server.getString("Name");
				String ip = server.getString("IP");
				int port = server.getInteger("Port");
				GuiServerSelector.servers.add(new ServerInfo(name, ip, port));
			}else {
				System.out.println("Server entry is not compound! "+tag);
			}
		}
	}
	public static void writeCurrentServers(){
		try {
			File fpr = new File(Minecraft.getMinecraftDir()+"/MaybeAClient_USER/");
			File servers = new File(fpr, "servers");
			writeCurrentServers(servers);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeCurrentServers(File f) throws IOException {
		NBTTagList comp = (NBTTagList) NBTBase.createTagOfType(NBTBase.LIST);
		for(ServerInfo server : GuiServerSelector.servers) {
			NBTTagCompound info = (NBTTagCompound)NBTBase.createTagOfType(NBTBase.COMPOUND);
			info.setString("Name", server.name);
			info.setString("IP", server.ip);
			info.setInteger("Port", server.port);
			comp.setTag(info);
		}
		NBTTagCompound write = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		NBTTagCompound write2 = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		write.setTag("Servers", comp);
		write.setInteger("FormatVersion", Client.saveVersion);
		write2.setCompoundTag("", write);
		write2.writeTagContents(new DataOutputStream(new FileOutputStream(f)));
	}
	
	public static void postClientLoad() throws IOException {
		System.out.println("Loaded "+Client.hacksByName.size()+" modules, "+Client.commands.size()+" commands.");
		
		ClickGUI.registerTabs();
		
		File f = new File(Minecraft.getMinecraftDir()+"/MaybeAClient/");
		File fpr = new File(Minecraft.getMinecraftDir()+"/MaybeAClient_USER/");
		f.mkdirs();
		fpr.mkdirs();
		System.out.println("Getting module settings");
		File ms = new File(f, "module-settings");
		if(ms.exists()) {
			readModuleSettings(ms);
		}else {
			ms.createNewFile();
			writeModuleSettings(ms);
		}
		
		System.out.println("Getting command aliases");
		File aliases = new File(f, "aliases");
		if(aliases.exists()) {
			readCommandAliases(aliases);
		}else {
			aliases.createNewFile();
			writeCommandAliases(aliases);
		}
		
		System.out.println("Getting clickgui settings");
		File clickgui = new File(f, "clickgui-settings");
		if(clickgui.exists()) {
			readClickGUISettings(clickgui);
			writeClickGUISettings(clickgui);
		}else {
			clickgui.createNewFile();
			writeClickGUISettings(clickgui);
		}
		System.out.println("Getting servers");
		File servers = new File(fpr, "servers");
		if(servers.exists()) {
			readCurrentServers(servers);
			writeCurrentServers(servers);
		}else {
			servers.createNewFile();
			writeCurrentServers(servers);
		}
		
		System.out.println("Getting accounts");
		File accounts = new File(fpr, "accounts");
		if(accounts.exists()) {
			readCurrentAccounts(accounts);
			writeCurrentAccounts(accounts);
		}else {
			accounts.createNewFile();
			writeCurrentAccounts(accounts);
		}
		
		
		ClickGUIHack.instance.status = false;
		generateOutlinedTextures();
	}
	
	public static void writeCurrentAccounts(){
		try {
			File fpr = new File(Minecraft.getMinecraftDir()+"/MaybeAClient_USER/");
			File servers = new File(fpr, "accounts");
			writeCurrentAccounts(servers);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readCurrentAccounts(File f) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		NBTBase base = NBTBase.readTag(dis);
		NBTTagCompound cc = (NBTTagCompound) base;
		NBTTagList accounts = cc.getTagList("Accounts");
		for(int i = 0; i < accounts.tagCount(); ++i) {
			NBTBase tag = accounts.tagAt(i);
			if(tag instanceof NBTTagCompound) {
				NBTTagCompound account = (NBTTagCompound) tag;
				String username = account.getString("Username");
				AccountInfo info = new AccountInfo(username);
				
				NBTTagList passwords = account.getTagList("Passwords");
				for(int j = 0; j < passwords.tagCount(); ++j) {
					NBTBase pwdtag = passwords.tagAt(j);
					if(pwdtag instanceof NBTTagCompound) {
						NBTTagCompound pwdtag2 = (NBTTagCompound) pwdtag;
						String password = pwdtag2.getString("Password");
						String prompt = pwdtag2.getString("Prompt");
						String loginCmd = pwdtag2.getString("LoginCommand");
						String ipAddress = pwdtag2.getString("IP");
						MatchMode[] vals = MatchMode.values();
						int ii = pwdtag2.getInteger("Mode");
						if(ii >= vals.length) {
							System.out.println("Invalid mode! ("+ii+"/"+vals.length+")");
							ii = 0;
						}
						PasswordInfo passInfo = new PasswordInfo(password, ipAddress);
						passInfo.loginPrompt = prompt;
						passInfo.loginCommand = loginCmd;
						passInfo.mode = vals[ii];
						
						info.addPassword(passInfo);
					}
				}
				GuiAccManager.addAccount(info);
			}else {
				System.out.println("Account entry is not compound! "+tag);
			}
		}
	}
	
	public static void writeCurrentAccounts(File f) throws IOException {
		NBTTagList comp = (NBTTagList) NBTBase.createTagOfType(NBTBase.LIST);
		for(AccountInfo acc : GuiAccManager.accounts) {
			NBTTagCompound info = (NBTTagCompound)NBTBase.createTagOfType(NBTBase.COMPOUND);
			info.setString("Username", acc.name);
			
			NBTTagList passwds = (NBTTagList) NBTBase.createTagOfType(NBTBase.LIST);
			for(PasswordInfo pass : acc.pwds) {
				NBTTagCompound ps = (NBTTagCompound)NBTBase.createTagOfType(NBTBase.COMPOUND);
				ps.setString("Password", pass.password);
				ps.setString("Prompt", pass.loginPrompt);
				ps.setString("LoginCommand", pass.loginCommand);
				ps.setString("IP", pass.serverIP);
				ps.setInteger("Mode", pass.mode.ordinal());
				passwds.setTag(ps);
			}
			
			info.setTag("Passwords", passwds);
			comp.setTag(info);
		}
		NBTTagCompound write = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		NBTTagCompound write2 = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		write.setTag("Accounts", comp);
		write.setInteger("FormatVersion", Client.saveVersion);
		write2.setCompoundTag("", write);
		write2.writeTagContents(new DataOutputStream(new FileOutputStream(f)));
	}
	
	public static BufferedImage outlinedItemsTexture;
	public static BufferedImage outlinedTerrainTexture;
	
	public static byte[] itemsTextureSides;
	public static byte[] terrainTextureSides;
	
	
	public static void generateOutlinedTextures() throws IOException {
		try {
			outlinedItemsTexture = TextureUtils.generateOutlinedTexture(ImageIO.read(Minecraft.class.getResource("/gui/items.png")));
			outlinedTerrainTexture = TextureUtils.generateOutlinedTexture(ImageIO.read(Minecraft.class.getResource("/terrain.png")));
			itemsTextureSides = TextureUtils.getOutliningSides(ImageIO.read(Minecraft.class.getResource("/gui/items.png")));
			terrainTextureSides = TextureUtils.getOutliningSides(ImageIO.read(Minecraft.class.getResource("/terrain.png")));
		}catch(java.lang.IllegalArgumentException e) {
			e.printStackTrace();
		}
		
	}
	
	public static final int STENCIL_REF_ELDRAW = 123;
	
	/**
	* 1.9
	* - Added HideChat
	* - Added Schematica Stats
	* - Added AutoBlockPlace into Schematica
	* - Added PlayerList
	* - Added LastSeenSpots
	* - Fixed Schematica GUIs opening incorrectly when ClickGUI scale is not same as game
	* - Fixed text in Schematica control GUI 
	*/
	static {
		//1.0
		registerHack(new FlyHack());
		registerHack(new JesusHack());
		registerHack(new NoFallHack());
		registerHack(new XRayHack());
		registerHack(new FullBrightHack());
		registerHack(new ForceFieldHack());
		registerHack(new EntityESPHack());
		registerHack(new FreecamHack());
		registerHack(new RadarHack());
		registerHack(new LockTimeHack());
		registerHack(new LiquidInteractHack());
		registerHack(new InstantHack());
		registerHack(new NoClipHack());
		registerHack(new ClientInfoHack());
		registerHack(new KeybindingsHack());
		registerHack(new ClockspeedHack());
		registerHack(new AntiKnockbackHack());
		registerHack(new ServerStatusHack());
		//1.1
		registerHack(new AutoWalkHack());
		registerHack(new SafeWalkHack());
		registerHack(new StrafeHack());
		registerHack(new CameraLockHack());
		registerHack(new SpeedMineHack());
		registerHack(new AutoTunnelHack());
		registerHack(new ArrayListHack());
		//1.2
		registerHack(new StepHack());
		registerHack(new ClickGUIHack());
		registerHack(new ClientNameHack());
		registerHack(new Packet19SenderHack());
		registerHack(new TracersHack());
		//1.3
		registerHack(new BlockESPHack());
		registerHack(new NameTagsHack());
		registerHack(new NoClientSideDestroyHack());
		//1.4
		registerHack(new SchematicaHack());
		//1.5
		registerHack(new FastPlaceHack());
		registerHack(new ReachHack());
		registerHack(new FastLadderHack());
		registerHack(new NoPushHack());
		registerHack(new WorldDLHack());
		registerHack(new FOVHack());
		registerHack(new ScaffoldHack());
		registerHack(new InventoryViewHack());
		registerHack(new PlayerViewHack());
		registerHack(new CombatLogHack());
		//1.6
		registerHack(new AutoEatHack());
		registerHack(new AutoMouseClickHack());
		registerHack(new InventoryWalkHack());
		registerHack(new XCarryHack());
		registerHack(new AutoToolHack());
		//1.7
		registerHack(new AutoReconnectHack());
		registerHack(new AutoLoginHack());
		registerHack(new ItemNameTagsHack());
		registerHack(new AntiSlowdownHack());
		//1.7.1
		registerHack(new NoRenderHack());
		//1.8
		registerHack(new AutoScreenshotCopyHack());
		registerHack(new NoclipThirdPersonHack());
		registerHack(new FastPortalHack());
		registerHack(new AutoFishHack());
		registerHack(new ChestCheckerHack());
		registerHack(new NewChunksHack());
		//1.9
		registerHack(new HideChatHack());
		registerHack(new PlayerlistHack());
		registerHack(new LastSeenSpotsHack());
		//x.x
		//TODO registerHack(new DogOwnerHack());
		
		//1.0
		registerCommand(new CommandModule());
		registerCommand(new CommandHelp());
		registerCommand(new CommandSlot9());
		//1.1
		registerCommand(new CommandAlias());
		//1.2
		registerCommand(new CommandTab());
		
		
		try {
			postClientLoad();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static ArrayList<DrawCall> drawCallsTerrain[] = new ArrayList[256];
	public static ArrayList<DrawCall> drawCallsItems[] = new ArrayList[256];
	public static int itemsTexSize = 16;
	public static int terrainTexSize = 16;
	public static boolean notJarMod = false;
	public static void initTextures() {
		for(int i = 0; i < 256; ++i) {
			drawCallsTerrain[i] = new ArrayList<DrawCall>();
			drawCallsItems[i] = new ArrayList<DrawCall>();
		}
		try {
			itemsTextureSides = TextureUtils.getOutliningSides(ImageIO.read(mc.texturePackList.selectedTexturePack.func_6481_a("/gui/items.png")));
			terrainTextureSides = TextureUtils.getOutliningSides(ImageIO.read(mc.texturePackList.selectedTexturePack.func_6481_a("/terrain.png")));
			itemsTexSize = (int) Math.sqrt(itemsTextureSides.length) / 16;
			terrainTexSize = (int) Math.sqrt(terrainTextureSides.length) / 16;
			
			bakeSides(itemsTextureSides);
			bakeSides(terrainTextureSides);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void bakeSides(byte[] sides) {
    	int tsize = 16;
    	ArrayList<DrawCall> calls[] = null;
    	if(sides == terrainTextureSides) {
    		tsize = terrainTexSize;
    		calls = drawCallsTerrain;
    	}else {
    		tsize = itemsTexSize;
    		calls = drawCallsItems;
    	}
    	
    	for(int gx = 0; gx < 16; ++gx) {
			for(int gy = 0; gy < 16; ++gy) {
				ArrayList<DrawCall> dcalls = calls[gx + gy*16];
				for(int x = gx*tsize; x < gx*tsize+tsize; ++x) {
					boolean begin = false;
					int beginX = 0;
					int beginY = 0;
					
					for(int y = gy*tsize; y < gy*tsize+tsize; ++y) {
						byte b = sides[x*(tsize*16) + y];
	
						if((b & 1) > 0) {
							if(!begin) {
								begin = true;
								beginX = x % tsize;
								beginY = y % tsize;
							}
						}else {
							if(begin) {
								dcalls.add(new DrawCall(1, beginX, beginY, x%tsize, y%tsize));
								begin = false;
							}
						}
					}
					
					if(begin) {
						dcalls.add(new DrawCall(1, beginX, beginY, x%tsize, tsize));
						begin = false;
					}
					
					for(int y = gy*tsize; y < gy*tsize+tsize; ++y) {
						byte b = sides[x*(tsize*16) + y];
	
						if((b & 2) > 0) {
							if(!begin) {
								begin = true;
								beginX = x % tsize;
								beginY = y % tsize;
							}
						}else {
							if(begin) {
								dcalls.add(new DrawCall(2, beginX, beginY, x%tsize, y%tsize));
								begin = false;
							}
						}
					}
					
					if(begin) {
						dcalls.add(new DrawCall(2, beginX, beginY, x%tsize, tsize));
						begin = false;
					}
				}
				
				for(int y = gy*tsize; y < gy*tsize+tsize; ++y) {
					boolean begin = false;
					int beginX = 0;
					int beginY = 0;
					for(int x = gx*tsize; x < gx*tsize+tsize; ++x) {
						byte b = sides[x*(tsize*16) + y];
						if((b & 4) > 0) {
							if(!begin) {
								begin = true;
								beginX = x % tsize;
								beginY = y % tsize;
							}
						}else {
							if(begin) {
								dcalls.add(new DrawCall(4, beginX, beginY, x%tsize, y%tsize));
								begin = false;
							}
						}
					}
					if(begin) {
						dcalls.add(new DrawCall(4, beginX, beginY, tsize, y%tsize));
						begin = false;
					}
					
					for(int x = gx*tsize; x < gx*tsize+tsize; ++x) {
						byte b = sides[x*(tsize*16) + y];
						if((b & 8) > 0) {
							if(!begin) {
								begin = true;
								beginX = x % tsize;
								beginY = y % tsize;
							}
						}else {
							if(begin) {
								dcalls.add(new DrawCall(8, beginX, beginY, x%tsize, y%tsize));
								begin = false;
							}
						}
					}
					
					if(begin) {
						dcalls.add(new DrawCall(8, beginX, beginY, tsize, y%tsize));
						begin = false;
					}
				}
			}
		}
    }
	
	public static void onKeyPress(int keycode, boolean pressed) {
		if(keycode == 0){
			System.out.println("Tried activating 0 keycode(NONE) somehow??");
			return;
		}
		
		if(pressed) {
			for(Hack h : Client.hacksByName.values()) {
				if(h.keybinding.value == keycode){
					h.toggle();
				}
			}
		}
	}
}
