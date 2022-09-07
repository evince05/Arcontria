package dev.eternalformula.arcontria.ui.hud;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.util.Assets;

/**
 * The PlayerHUD acts as an overlay for all UI Widgets that the Player may use<br>
 * (eg. Hotbar, Healthbar, etc.).
 * @author EternalFormula
 */

public class PlayerHUD extends UIContainer {
	
	private Player player;
	
	private PlayerHotbar hotbar;
	private WorldClock worldClock;
	
	private EFButton settingsBtn;
	private EFButton questBtn;
	private EFButton inventoryBtn;
	
	public PlayerHUD(Player player) {
		super();
		this.player = player;
		this.children = new ArrayList<UIElement>();
		
		initElements();
	}
	
	/**
	 * Handles HUD element initialization.
	 */
	
	private void initElements() {
		
		TextureAtlas uiAtlas = Assets.get("ui/inventory/gameui.atlas", TextureAtlas.class);
		this.hotbar = new PlayerHotbar(player.getInventory());
		addChildren(hotbar);
		
		this.worldClock = new WorldClock();
		addChildren(worldClock);
		
		this.settingsBtn = new EFButton(this, 2, 160);
		TextureRegion settingsReg = uiAtlas.findRegion("settingsbtn");
		settingsBtn.setSkin(new TextureRegion(settingsReg, 0, 0, 18, 18));
		settingsBtn.setClickSkin(new TextureRegion(settingsReg, 18, 0, 18, 18));
		settingsBtn.setTooltip("Settings", false);
		addChildren(settingsBtn);
		
		this.questBtn = new EFButton(this, 2, 130);
		TextureRegion questReg = uiAtlas.findRegion("questbtn");
		questBtn.setSkin(new TextureRegion(questReg, 0, 0, 18, 18));
		questBtn.setClickSkin(new TextureRegion(questReg, 18, 0, 18, 18));
		questBtn.setTooltip("Quests (Q)", true);
		addChildren(questBtn);
		
		this.inventoryBtn = new EFButton(this, 2, 110);
		TextureRegion invReg = uiAtlas.findRegion("invbtn");
		inventoryBtn.setSkin(new TextureRegion(invReg, 0, 0, 18, 18));
		inventoryBtn.setClickSkin(new TextureRegion(invReg, 18, 0, 18, 18));
		inventoryBtn.setTooltip("Inventory (E)", true);
		addChildren(inventoryBtn);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
	}
	
	public WorldClock getClock() {
		return worldClock;
	}

}
