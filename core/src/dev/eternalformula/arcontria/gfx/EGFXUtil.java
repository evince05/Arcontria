package dev.eternalformula.arcontria.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import dev.eternalformula.arcontria.ArcontriaGame;

public class EGFXUtil {
	
	public static final float DEFAULT_WIDTH = 640f;
	public static final float DEFAULT_HEIGHT = 360f;
	public static final float DEFAULT_UPP = 1/32f;
	public static float RENDER_SCALE = 2f;
	
	/**
	 * Draws a fade rectangle over the screen.<br>
	 * Note that this handles pausing and resuming the batch.
	 * 
	 * @param batch The current batch being rendered.
	 * @param alpha The alpha of the fade rectangle
	 */
	
	public static void drawFadeRect(SpriteBatch batch, float alpha) {
		batch.end();
		
		// Get the shape renderer
		ShapeRenderer shapeRend = ArcontriaGame.GAME.getSceneManager().getShapeRenderer();
		shapeRend.setAutoShapeType(true);
		
		// Alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Draws the rect
		shapeRend.begin();
		shapeRend.set(ShapeType.Filled);
		shapeRend.setColor(new Color(0f, 0f, 0f, alpha));
		shapeRend.rect(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRend.end();
		
		// Stop alpha
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		batch.begin();
	}
	
	/**
	 * Draws a black rectangle with an alpha component.
	 * @param batch The batch to use
	 * @param rect The rectangle to draw
	 * @param alpha The alpha value of the rect.
	 */
	
	public static void drawAlphaRect(SpriteBatch batch, Rectangle rect, float alpha) {
		drawColorRect(batch, new Color(0f, 0f, 0f, alpha), rect);
	}
	
	/**
	 * Draws a colored, filled rectangle.
	 * @param batch The batch to use
	 * @param color The color to use
	 * @param rect The rectangle to draw.
	 */
	
	public static void drawColorRect(SpriteBatch batch, Color color, Rectangle rect) {
		batch.end();
		
		// Get the shape renderer
		ShapeRenderer shapeRend = ArcontriaGame.GAME.getSceneManager().getShapeRenderer();
		shapeRend.setAutoShapeType(true);
		shapeRend.setProjectionMatrix(batch.getProjectionMatrix());
		
		// Alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Draws the rect
		shapeRend.begin();
		shapeRend.set(ShapeType.Filled);
		shapeRend.setColor(color);
		shapeRend.rect(rect.x, rect.y, rect.width, rect.height);
		shapeRend.end();
		
		// Stop alpha
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		batch.begin();
	}
	
	public static float getSceneAlpha() {
		return ArcontriaGame.GAME.getScene().getBatchAlpha();
	}
	
	public static void setSceneAlpha(float alpha) {
		ArcontriaGame.GAME.getScene().setBatchAlpha(alpha);
	}
}
