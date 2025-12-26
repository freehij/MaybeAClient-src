package net.skidcode.gh.maybeaclient.gui.server;

public class ServerInfo {
	public String ip;
	public String name;
	public int port;
	
	public ServerInfo(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
}
