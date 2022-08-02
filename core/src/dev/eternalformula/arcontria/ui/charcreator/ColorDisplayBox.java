package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;

public class ColorDisplayBox extends UIElement {
	
	private Color drawColor;
	private TextureRegion colorDisplayBox;
	
	public ColorDisplayBox(UIContainer container, int x, int y) {
		super(container);
		
		this.location = new Vector2(x, y);
		this.drawColor = new Color(0f, 0f, 0f, 1f);
		
		TextureAtlas atlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		
		setSkin(atlas.findRegion("colorpickerdisplay"));
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
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
		
		uiBatch.draw(skin, location.x, location.y);
		uiBatch.end();
		
		ShapeRenderer shapeRend = ArcontriaGame.GAME.getSceneManager().getShapeRenderer();
		shapeRend.setProjectionMatrix(uiBatch.getProjectionMatrix());
		shapeRend.setAutoShapeType(true);
		shapeRend.begin();
		
		shapeRend.set(ShapeType.Filled);
		shapeRend.setColor(drawColor);
		
		shapeRend.rect(location.x + 1f, location.y + 1f, 16f, 16f);
		
		shapeRend.end();
		uiBatch.begin();
	}
	
	public void setColor(Color color) {
		this.drawColor = color;
	}

}
