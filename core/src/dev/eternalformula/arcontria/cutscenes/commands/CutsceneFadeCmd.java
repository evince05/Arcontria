package dev.eternalformula.arcontria.cutscenes.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;
import dev.eternalformula.arcontria.scenes.SceneManager;
import dev.eternalformula.arcontria.util.EFDebug;

public class CutsceneFadeCmd extends CutsceneCommand {

	public static final int FADE_IN = 0;
	public static final int FADE_OUT = 1;
	
	private int direction;
	
	private float alpha;
	private float targetAlpha;
	
	private float speed;
	
	/**
	 * Creates a new CutsceneFadeCmd.
	 * @param cutscene The cutscene
	 * @param direction 0 = fadeIn, 1 = fadeOut
	 * @param totalTime The total time that should elapse during the animation.
	 */
	
	public CutsceneFadeCmd(Cutscene cutscene, int direction, float totalTime) {
		super(cutscene);
		
		this.direction = direction;
		
		// v = d/t (alpha per second)
		this.speed = 1f / totalTime;
		
		if (direction == FADE_IN) {
			this.targetAlpha = 0f;
			this.alpha = 1f;
		}
		else if (direction == FADE_OUT) {
			this.targetAlpha = 1f;
			this.alpha = 0f;
		}
		else {
			EFDebug.error("Could not create CutsceneFadeCmd! Invalid direction param.");
		}
	}

	/**
	 * Handles fade effects.
	 */
	
	@Override
	public void update(float delta) {
		if (direction == FADE_IN) {
			if (alpha - speed * delta > targetAlpha) {
				alpha -= speed * delta;
			}
			else {
				alpha = targetAlpha;
				isFinished = true;
			}
		}
		else if (direction == FADE_OUT) {
			if (alpha + speed * delta < targetAlpha) {
				alpha += speed * delta;
			}
			else {
				alpha = targetAlpha;
				isFinished = true;
			}
		}
	}
	
	/**
	 * Draws the "fade" effect. Use this in DrawUI to avoid having to do it twice.
	 * @param batch The batch to be used
	 * @param delta Delta time
	 */
	
	public void drawFade(SpriteBatch batch, float delta) {
		batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		
		SceneManager mgr = ArcontriaGame.GAME.getSceneManager();
		Vector2 dimensions = mgr.getViewportHandler().getUIDimensions();
		
		ShapeRenderer shapeRend = mgr.getShapeRenderer();
		shapeRend.setAutoShapeType(true);
		shapeRend.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRend.begin();
		shapeRend.set(ShapeType.Filled);
		
		
		shapeRend.setColor(new Color(0f, 0f, 0f, alpha));
		shapeRend.rect(0, 0, dimensions.x, dimensions.y);
		
		shapeRend.end();
		
		batch.begin();	
	}
	
	public float getAlpha() {
		return alpha;
	}
}
