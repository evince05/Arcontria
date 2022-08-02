package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Strings;

public class EFTypingLabel extends UIElement {
	
	private static final float DEFAULT_TYPE_SPEED = 16f;
	private String text;
	private StringBuilder displayText;
	
	private int currentIndex;
	
	private float elapsedTime;
	private float typeSpeed;
	
	private boolean isFinished;
	
	private BitmapFont font;
	
	public EFTypingLabel(UIContainer container, String text) {
		super(container);
		this.text = text;
		
		this.displayText = new StringBuilder();
		this.typeSpeed = DEFAULT_TYPE_SPEED;
	}

	@Override
	public void onMouseHovered(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseDrag(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		this.elapsedTime += delta;
		if (elapsedTime >= 1f / typeSpeed && !isFinished) {
			elapsedTime = 0f;
			
			displayText.append(text.charAt(currentIndex));
			currentIndex++;
		
			if (currentIndex == text.length()) {
				isFinished = true;
			}
		}
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		
		if (font != null && !text.equals("")) {
			font.draw(uiBatch, displayText, location.x, location.y);
		}
	}
	
	/**
	 * Automatically skips the "typing" function, and displays the rest of the string at once.
	 */
	
	public void skipToEnd() {
		displayText = new StringBuilder(text);
	}
	
	/**
	 * Sets the type speed in characters per second.
	 * @param speed The type speed
	 */
	
	public void setSpeed(float speed) {
		this.typeSpeed = speed;
	}
	
	public void setFont(BitmapFont font) {
		this.font = font;
	}
}
