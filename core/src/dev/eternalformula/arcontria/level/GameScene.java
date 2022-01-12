package dev.eternalformula.arcontria.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.gfx.EGFXUtil;

public class GameScene { // extends GameState???
	
	private ScreenViewport viewport;
	private SpriteBatch batch;
	
	protected GameLevel level;
	
	public GameScene() {
		this.batch = new SpriteBatch();
		this.viewport = new ScreenViewport();
		viewport.setUnitsPerPixel(EGFXUtil.DEFAULT_UPP);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// temp
		this.level = new TestLevel(this);
	}
	
	public void update(float delta) {
		level.update(delta);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			ArcontriaGame.GAME.resize(320, 180);
			Gdx.graphics.setWindowedMode(320, 180);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			ArcontriaGame.GAME.resize(640, 360);
			Gdx.graphics.setWindowedMode(640, 360);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			ArcontriaGame.GAME.resize(960, 540);
			Gdx.graphics.setWindowedMode(960, 540);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
			ArcontriaGame.GAME.resize(1280, 720);
			Gdx.graphics.setWindowedMode(1280, 720);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
			ArcontriaGame.GAME.resize(1600, 900);
			Gdx.graphics.setWindowedMode(1600, 900);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
			ArcontriaGame.GAME.resize(1920, 1080);
			Gdx.graphics.setWindowedMode(1920, 1080);
		}
	}
	
	public void draw(float delta) {

		batch.setProjectionMatrix(viewport.getCamera().combined);
		level.draw(batch, delta);
	}
	
	public void dispose() {
		batch.dispose();
		level.dispose();
	}
	
	public ScreenViewport getViewport() {
		return viewport;
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public void resize(int width, int height) {
		level.resize(width, height);
		
		// TODO: Add perfect scaling check
		float upp = (width / EGFXUtil.DEFAULT_WIDTH);
		viewport.setUnitsPerPixel(EGFXUtil.DEFAULT_UPP / upp);
	}
	
	public GameLevel getLevel() {
		return level;
	}

}
