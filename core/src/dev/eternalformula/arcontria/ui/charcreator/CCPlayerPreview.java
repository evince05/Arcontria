package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;

public class CCPlayerPreview extends UIElement {
	
	private CharacterCreatorScene scene;
	private TextureAtlas playerBuilder;
	
	// IDs
	public int hairId;
	public int shirtId;
	public int pantsId;
	public int bootsId;
	
	// Tint Colors
	private Color skinColor;
	private Color hairColor; // and eyebrows
	private Color eyeColor;
	private Color shirtColor;
	private Color pantsColor;
	private Color bootsColor;

	public CCPlayerPreview(CharacterCreatorScene scene) {
		super();
		
		this.scene = scene;
		
		this.hairId = 0;
		this.shirtId = 0;
		this.pantsId = 0;
		this.bootsId = 0;
		
		this.skinColor = Color.WHITE;
		this.hairColor = Color.WHITE;
		this.eyeColor = Color.WHITE;
		this.shirtColor = Color.WHITE;
		this.pantsColor = Color.WHITE;
		this.bootsColor = Color.WHITE;
		
		TextureAtlas atlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		setSkin(atlas.findRegion("playerpreview"));
		
		this.playerBuilder = Assets.get("ui/charcreator/playerbuilder/"
				+ "playerbuilder.atlas",TextureAtlas.class);
	}
	@Override
	public void onMouseHovered(int x, int y) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		draw(uiBatch, delta, location.x, location.y);
		
	}
	
	public void draw(SpriteBatch uiBatch, float delta, float x, float y) {
		uiBatch.draw(skin, x, y);
		
		uiBatch.setShader(scene.getRGBShader());
		uiBatch.setColor(skinColor);
		uiBatch.draw(playerBuilder.findRegion("basehuman"), x + 3f, y + 3f);
		
		uiBatch.setColor(hairColor);
		uiBatch.draw(new TextureRegion(playerBuilder.findRegion("hair"), hairId * 16, 0, 16, 16), x + 3f, y + 19f);
		uiBatch.draw(playerBuilder.findRegion("eyebrows"), x + 8f, y + 27f);
		
		uiBatch.setColor(eyeColor);
		uiBatch.draw(playerBuilder.findRegion("eyes"), x + 9f, y + 25f);
		
		uiBatch.setColor(pantsColor);
		uiBatch.draw(playerBuilder.findRegion("pants"), x + 3f, y + 3f);
		
		uiBatch.setColor(shirtColor);
		uiBatch.draw(playerBuilder.findRegion("shirt"), x + 3f, y + 11f);
		
		uiBatch.setColor(bootsColor);
		uiBatch.draw(playerBuilder.findRegion("boots"), x + 3f, y + 3f);
		
		uiBatch.setColor(Color.WHITE);
		uiBatch.draw(playerBuilder.findRegion("eyewhites"), x + 8f, y + 25f);
		
		uiBatch.setShader(null);
	}
	
	public void setSkinColor(Color skinColor) {
		this.skinColor = skinColor;
	}
	
	public void setHairColor(Color hairColor) {
		this.hairColor = hairColor;
	}
	
	public void setEyeColor(Color eyeColor) {
		this.eyeColor = eyeColor;
	}
	
	public void setShirtColor(Color shirtColor) {
		this.shirtColor = shirtColor;
	}
	
	public void setPantsColor(Color pantsColor) {
		this.pantsColor = pantsColor;
	}
	
	public void setBootsColor(Color bootsColor) {
		this.bootsColor = bootsColor;
	}
}
