package dev.eternalformula.arcontria.scenes.charcreator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dev.eternalformula.arcontria.scenes.Scene;
import dev.eternalformula.arcontria.scenes.SceneManager;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CCBootsTab;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CCEyesTab;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CCHairTab;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CCInfoTab;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CCPantsTab;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CCShirtTab;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CCSkinTab;
import dev.eternalformula.arcontria.scenes.charcreator.tabs.CharacterCreatorTab;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.charcreator.CCColorPicker;
import dev.eternalformula.arcontria.util.Assets;

public class CharacterCreatorScene extends Scene {

	private int currentTab;
	
	private UIContainer ccContainer;
	
	private BitmapFont font;
	private Texture bgPane;
	
	public CharacterCreatorScene(SceneManager manager) {
		super(manager);
	}

	@Override
	public void load() {
		
		TextureAtlas atlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		
		// UIContainer
		this.ccContainer = new UIContainer();
		ccContainer.setLocation(30, 16);
	    ccContainer.setSkin(atlas.findRegion("charcreatorpane"));
	    
		CCInfoTab infoTab = new CCInfoTab(this);
		CCEyesTab eyesTab = new CCEyesTab(this);
		CCSkinTab skinTab = new CCSkinTab(this);
		CCHairTab hairTab = new CCHairTab(this);
		CCShirtTab shirtTab = new CCShirtTab(this);
		CCPantsTab pantsTab = new CCPantsTab(this);
		CCBootsTab bootsTab = new CCBootsTab(this);
		
		ccContainer.addChildren(infoTab, eyesTab, skinTab, hairTab, shirtTab, pantsTab, bootsTab);
		
		// Current Tab
		currentTab = infoTab.getTabId();
		infoTab.setCurrentTab(true);
		
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {		
	}

	@Override
	public void drawUI(SpriteBatch batch, float delta) {
		
		batch.begin();
		
		ccContainer.draw(batch, delta);

		batch.end();
		
	}

	@Override
	public void update(float delta) {
		ccContainer.update(delta);
	}

	@Override
	public void resize(int width, int height) {
	}
	
	@Override
	public void onKeyTyped(char key) {
		ccContainer.onKeyTyped(key);
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		ccContainer.onMouseClicked(x, y, button);
	}
	
	@Override
	public void onMouseReleased(int x, int y, int button) {
		ccContainer.onMouseReleased(x, y, button);
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
		ccContainer.onMouseDrag(x, y);
	}

	@Override
	public void dispose() {
	}
	
	public int getCurrentTab() {
		return currentTab;
	}
	
	public void setCurrentTab(int tabId) {
		this.currentTab = tabId;
		
		ccContainer.getChildren().forEach(e -> {
			if (e instanceof CharacterCreatorTab) {
				CharacterCreatorTab ccTab = (CharacterCreatorTab) e;
				if (ccTab.getTabId() != currentTab) {
					ccTab.setCurrentTab(false);
				}
			}
		});
	}

}
