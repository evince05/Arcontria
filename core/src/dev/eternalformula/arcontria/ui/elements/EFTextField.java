package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;

public class EFTextField extends UIElement {
	
	private StringBuilder sb;
	private BitmapFont font;
	
	private float inactivityTime;
	
	private float cursorTimer;
	private boolean drawCursor;
	
	private float maxTextWidth;
	
	/**
	 * Creates a new EFTextField.
	 * @param container The parent container.
	 */
	
	public EFTextField(UIContainer container) {
		super(container);

		this.sb = new StringBuilder();
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		this.drawCursor = false;
	}
	
	@Override
	public void setSkin(TextureRegion skin) {
		super.setSkin(skin);
		
		this.maxTextWidth = bounds.getWidth() - 10f;
	}

	@Override
	public void onMouseHovered(int x, int y) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}
	
	@Override
	public void onMouseWheelScrolled(int amount) {
	}

	@Override
	public void update(float delta) {
		
		if (container.isFocusedOnElement(this)) {
			inactivityTime += delta;
			
			if (inactivityTime >= 3f || sb.length() == 0) {
				cursorTimer += delta;
				
				if (cursorTimer >= 0.8f) {
					drawCursor = !drawCursor;
					cursorTimer = 0f;
				}
				
			}
		}
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		uiBatch.draw(skin, location.x, location.y);
		
		font.draw(uiBatch, sb, location.x + 6f, location.y + 22f);
		
		if (container.isFocusedOnElement(this)) {
			if (drawCursor) {
					
				ShapeRenderer shapeRend = ArcontriaGame.GAME.getSceneManager().getShapeRenderer();
				uiBatch.end();

				shapeRend.setColor(Color.WHITE);
				shapeRend.setAutoShapeType(true);
				shapeRend.setProjectionMatrix(uiBatch.getProjectionMatrix());
				shapeRend.begin();
				shapeRend.set(ShapeType.Filled);

				float textWidth = FontUtil.getWidth(font, sb.toString());
				float x = location.x + 7f + textWidth;
				float y = location.y + 6f;

				shapeRend.rect(x, y, 1, 12);
				shapeRend.end();

				uiBatch.begin();
			}
		}
	}
	
	@Override
	public void onKeyTyped(char key) {
		super.onKeyTyped(key);
		
		if (container.isFocusedOnElement(this)) {
			inactivityTime = 0f;
			
			if (key == '\b') {
				if (sb.length() > 0) {
					sb.setLength(sb.length() - 1);
				}
				return;
			}
			
			if (Character.isWhitespace(key) && key != ' ') {
				return;
			}
			
			if (FontUtil.getWidth(font, sb.toString() + key) <= maxTextWidth) {
				sb.append(key);
			}
		}
		else {
		}
		
	}
	
	public String getText() {
		return sb.toString();
	}
	

}
