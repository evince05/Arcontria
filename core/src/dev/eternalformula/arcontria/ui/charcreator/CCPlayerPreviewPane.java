package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.util.Assets;

/**
 * Not to be confused with {@link CCPlayerPreview}, this class<br> 
 * displays the Player Preview pane, which contains the CCPlayerPreview. <br>
 * <br>
 * It acts similarly to the {@link CCStylePicker}, except it does<br>
 * not have the buttons to select a new style.
 * 
 * @author EternalFormula
 */

public class CCPlayerPreviewPane extends UIContainer {
	
	private CharacterCreatorScene scene;
	private BitmapFont font;
	
	public CCPlayerPreviewPane(CharacterCreatorScene scene, UIContainer parent, int x, int y) {
		super(parent);
		
		this.scene = scene;
		
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		this.location = new Vector2(x, y);
		
		// Textures
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		setSkin(ccAtlas.findRegion("playerpreviewpane"));
		
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		scene.getPlayerPreview().setLocation(location.x + 19, location.y + 27);
		scene.getPlayerPreview().draw(uiBatch, delta);
		
		String text = "Preview";
		float width = FontUtil.getWidth(font, "Preview");
		
		font.draw(uiBatch, text, location.x + 30 - width / 2f, location.y + 28);
	}
	
	

}
