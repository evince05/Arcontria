package dev.eternalformula.arcontria.ui.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Strings;

public class EFTypingLabel extends UIElement {
	
	private static final float DEFAULT_TYPE_SPEED = 22f;
	private String text;
	private StringBuilder displayText;
	
	private int currentIndex;
	
	private float elapsedTime;
	private float typeSpeed;
	
	private boolean isFinished;
	
	private BitmapFont font;
	private Color textColor;
	
	private List<String> lines;
	private int currentRow;
	private int lineWidth;
	
	private float lineYOffset;
	
	public EFTypingLabel(UIContainer container, String text) {
		super(container);
		this.text = text;
		
		this.displayText = new StringBuilder();
		this.typeSpeed = DEFAULT_TYPE_SPEED;
		
		this.lines = new ArrayList<String>();
		
		// default values
		this.lineYOffset = 12f;
		this.textColor = Color.WHITE;
		this.isInteractive = false;
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
			
			appendNextChar();
		}
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
			
			if (font != null && !text.equals("")) {
			
			font.setColor(textColor);
			
			// Draws each line with appropriate spacing.
			for (int line = 0; line < lines.size(); line++) {
				if (lines.get(line) != null && !lines.get(line).equals("")) {
					font.draw(uiBatch, lines.get(line), location.x, location.y - line * lineYOffset);
				}
				
			}
			font.setColor(Color.WHITE);
		}
	}
	
	/**
	 * Automatically skips the "typing" function, and displays the rest of the string at once.
	 */
	
	public void skipToEnd() {
		while (currentIndex != text.length()) {
			appendNextChar();
		}
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
	
	public void setupWrapping(BitmapFont font, int lineWidth) {
		this.font = font;
		this.lineWidth = lineWidth;
	}
	
	private void setLine(int index, String s) {
		if (lines.size() <= index) {
			lines.add(s);
		}
		else {
			lines.set(index, s);
		}
	}
	
	private void appendNextChar() {
		if (text.charAt(currentIndex) == ' ') {
			
			String nextWord = "";
			if (text.indexOf(" ", currentIndex + 1) != -1) {
				// There are multiple words to come
				
				// (currentIndex + 1) as the start bound skips over the current space
				nextWord = text.substring(currentIndex + 1, text.indexOf(" ", currentIndex + 1));
			}
			else {
				// This is the last word of the label
				nextWord = text.substring(currentIndex + 1, text.length());
			}
			
			// The space between displayText and nextWord must be included in the calculation.
			if (FontUtil.getWidth(font, displayText + " " + nextWord) >= lineWidth) {
				// Starts a new line
				
				setLine(currentRow, displayText.toString());
				
				// Substrings an extra character so the space isn't included at the start of the next line.
				text = text.substring(displayText.length() + 1);
				
				displayText = new StringBuilder();
				currentRow++;
				currentIndex = 0;
				return;
			}
			else {
				// The next word can fit on the line
				displayText.append(text.charAt(currentIndex));
				
				setLine(currentRow, displayText.toString());
				//displayLines[currentRow] = displayText.toString();
			}
		}
		else {
			// Appends the next character.
			displayText.append(text.charAt(currentIndex));
			setLine(currentRow, displayText.toString());
		}
		currentIndex++;
	
		if (currentIndex == text.length()) {
			isFinished = true;
		}
	}
	
	/**
	 * Sets the distance between the top of each line (in px).
	 * @param lineYOffset The line y offset (magnitude... use positive number).
	 */
	
	public void setLineYOffset(float lineYOffset) {
		this.lineYOffset = lineYOffset;
	}
	
	public void setColor(Color textColor) {
		this.textColor = textColor;
	}
	
	/**
	 * Determines whether the typing-label has finished typing.
	 */
	
	public boolean isFinished() {
		return isFinished;
	}
	
	/**
	 * Resets the typing label with a new text. Used mainly for dialogue chaining.
	 * @param text The new text of the typing label.
	 */
	
	public void resetWithNewText(String text) {
		// Clears all existing text in the TypingLabel.
		lines.clear();
		displayText = new StringBuilder();
		
		// Reset appendNewChar() fields.
		this.currentIndex = 0;
		this.currentRow = 0;
		this.isFinished = false;
		this.text = text;
	}
}
