package net.skidcode.gh.maybeaclient.gui.altman;

public class PasswordInfo {
	public String password;
	public String loginPrompt = "&cPlease log in using";
	public String loginCommand = "/login %password%";
	public String serverIP;
	public MatchMode mode = MatchMode.STARTSWITH;
	public boolean ignoreColor = false;
	
	public PasswordInfo(String password, String serverIP) {
		this.password = password;
		this.serverIP = serverIP;
	}
	
	public enum MatchMode{ //do NOT change enum places
		EXACT("Exact"),
		STARTSWITH("StartsWith");
		public String name;
		MatchMode(String name){
			this.name = name;
		}
	}
}
