package net.skidcode.gh.maybeaclient.gui.click;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import net.skidcode.gh.maybeaclient.Client;
import net.skidcode.gh.maybeaclient.hacks.ClickGUIHack;
import net.skidcode.gh.maybeaclient.hacks.category.Category;
import net.skidcode.gh.maybeaclient.utils.InputHandler;

public class ClickGUI extends GuiScreen{
	
	public GuiScreen parent;
	public static boolean initialized = false;
	public static ArrayList<Tab> tabs = new ArrayList<>();
	public static float mouseX, mouseY, mouseClicked;
	
	
	public Tab selectedTab = null;
	public Tab hoveringOver = null;
	
	public ClickGUI(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void keyTyped(char var1, int var2) {
		if (var2 == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
			ClickGUIHack.instance.toggle();
		}

	}

	@Override
	public void handleMouseInput() {
		
	}
	public static InputHandler inputHandler = null;
	
	public static boolean setInputHandler(InputHandler hd) {
		if(inputHandler != null) inputHandler.onInputFocusStop();
		inputHandler = hd;
		return true;
	}
	
	public void handleKeyboardInput() {
		if(inputHandler != null) {
			char keyname = Keyboard.getEventCharacter();
			int keycode = Keyboard.getEventKey();
			inputHandler.onKeyPress(keycode);
		}else {
			super.handleKeyboardInput();
		}
    }
	public void handleInput() {
		
		while (Keyboard.next()) {
			this.handleKeyboardInput();
		}

	}
	
	public void mouseClicked(int x, int y, int click) {
		for(Tab tab : tabs) {
			if(!tab.shown) continue;
			if(tab.isPointInside(x, y)) {
				this.selectedTab = tab;
				tab.onSelect(click, x, y);
				tabs.remove(this.selectedTab);
				tabs.add(0, this.selectedTab);
				break;
			}
		}
	}
	private void mouseWheelMoved(int x, int y, int wheel) {
		for(Tab tab : tabs) {
			if(!tab.shown) continue;
			if(tab.isPointInside(x, y)) {
				this.selectedTab = tab;
				tab.wheelMoved(wheel, x, y);
				tabs.remove(this.selectedTab);
				tabs.add(0, this.selectedTab);
				break;
			}
		}
	}
	
	public void mouseMovedOrUp(int x, int y, int click) {
		if(this.selectedTab != null) {
			
			this.selectedTab.mouseMovedSelected(click, x, y);
			if(click != -1) {
				this.selectedTab.onDeselect(click, x, y);
				this.selectedTab = null;
			}
		}else {
			
			if(hoveringOver != null && hoveringOver.isPointInside(x, y)) {
				this.hoveringOver.mouseHovered(x, y, click);
			}else {
				if(this.hoveringOver != null) {
					this.hoveringOver.stopHovering();
				}
				
				for(Tab tab : tabs) {
					if(!tab.shown) continue;
					if(tab.isPointInside(x, y)) {
						this.hoveringOver = tab;
						this.hoveringOver.mouseHovered(x, y, click);
						break;
					}
				}
			}
			
			
		}
	}
	
	public static void registerTabs() {
		Tab t = new CategoryTab(Category.MOVEMENT, 160, 10, 90);
		t.minimized = true;
		tabs.add(t);
		t = new CategoryTab(Category.RENDER, t.xPos, 24, 90);
		t.minimized = true;
		tabs.add(t);
		t = new CategoryTab(Category.COMBAT, t.xPos, 24 + 14, 90);
		t.minimized = true;
		tabs.add(t);
		t = new CategoryTab(Category.MISC, t.xPos, 24 + 14 + 14, 90);
		t.minimized = true;
		tabs.add(t);
		t = new CategoryTab(Category.UI, t.xPos, 24 + 14 + 14 + 14, 90);
		t.minimized = true;
		tabs.add(t);
		initialized = true;
		
		tabs.add(new ArrayListTab());
		tabs.add(new ClientNameTab());
		tabs.add(new ClientInfoTab());
		tabs.add(new KeybindingsTab());
		tabs.add(new RadarTab());
		tabs.add(new PlayerViewTab());
		tabs.add(new InventoryTab());
		tabs.add(new ChestContentTab());
		tabs.add(new PlayerlistTab());
		tabs.add(new LastSeenSpotsTab());
	}
	
	
	public static int prevGUIScale = -1;
	public static int newGUIScale = -1;
	public void drawScreen(int var1, int var2, float var3) {
		prevGUIScale = mc.gameSettings.guiScale;
		newGUIScale = ClickGUIHack.instance.getScale();
		if(prevGUIScale != newGUIScale) {
			mc.gameSettings.guiScale = newGUIScale;
			ScaledResolution sc = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			this.setWorldAndResolution(mc, sc.getScaledWidth(), sc.getScaledHeight());
			mc.entityRenderer.setupScaledResolution();
		}
		
		while(Mouse.next()) {
			if(Mouse.getEventButton() != -1) break; 
		};
		
		for(int i = tabs.size()-1; i >= 0; --i ) { //inverse render them
			Tab t = tabs.get(i);
			if(!t.shown) continue;
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			t.render();
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		int x;
		int y;
		x = (int) ((Mouse.getEventX() * this.width) / this.mc.displayWidth);
		y = (int) (this.height - ((Mouse.getEventY() * this.height) / this.mc.displayHeight) - 1);
		int wheel = Mouse.getDWheel();
		
		if(wheel != 0) {
			this.mouseWheelMoved(x, y, wheel);
		}
		
		if (Mouse.getEventButtonState()) {
			this.mouseClicked(x, y, Mouse.getEventButton());
		} else {
			this.mouseMovedOrUp(x, y, Mouse.getEventButton());
		}
		super.drawScreen(var1, var2, var3);
		if(prevGUIScale != newGUIScale) {
			mc.gameSettings.guiScale = prevGUIScale;
			ScaledResolution sc = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			this.setWorldAndResolution(mc, sc.getScaledWidth(), sc.getScaledHeight());
			mc.entityRenderer.setupScaledResolution();
		}
		
	}
	@Override
	public void onGuiClosed() {
		if(prevGUIScale != newGUIScale) {
			mc.gameSettings.guiScale = prevGUIScale;
			ScaledResolution sc = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			this.setWorldAndResolution(mc, sc.getScaledWidth(), sc.getScaledHeight());
			mc.entityRenderer.setupScaledResolution();
		}
	}
}
