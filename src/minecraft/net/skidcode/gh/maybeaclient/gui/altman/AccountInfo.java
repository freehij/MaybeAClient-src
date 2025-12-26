package net.skidcode.gh.maybeaclient.gui.altman;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountInfo {
	public String name;
	public HashMap<String, PasswordInfo> passwords = new HashMap<>();
	public ArrayList<PasswordInfo> pwds = new ArrayList<>();
	
	public void addPassword(String server, String pwd, String prompt) {
		if(passwords.containsKey(server)) return;
		PasswordInfo info = new PasswordInfo(pwd, server);
		info.loginPrompt = prompt;
		this.passwords.put(server, info);
		this.pwds.add(info);
	}
	
	//public void removePassword(PasswordInfo info) {
	//	
	//}
	
	public AccountInfo(String name) {
		this.name = name;
	}

	public void removePassword(PasswordInfo info) {
		this.pwds.remove(info);
		this.passwords.remove(info.serverIP);
	}

	public void addPassword(PasswordInfo passInfo) {
		this.passwords.put(passInfo.serverIP, passInfo);
		this.pwds.add(passInfo);
	}

	public void removePassword(int ind) {
		PasswordInfo info = this.pwds.remove(ind);
		this.passwords.remove(info.serverIP);
	}
}
