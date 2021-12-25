package dev.eternalformula.arcontria.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameScene { // extends GameState???
	
	private ExtendViewport viewport;
	private SpriteBatch batch;
	
	protected GameLevel level;
	
	public GameScene() {
		this.batch = new SpriteBatch();
		this.viewport = new ExtendViewport(20, 10);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// temp
		this.level = new TestLevel(this);
	}
	
	public void update(float delta) {
		level.update(delta);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void draw(float delta) {

		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		level.draw(batch, delta);
		batch.end();
	}
	
	public void dispose() {
		batch.dispose();
		level.dispose();
	}
	
	public ExtendViewport getViewport() {
		return viewport;
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public void resize(int width, int height) {
		level.resize(width, height);
	}
	
	public GameLevel getLevel() {
		return level;
	}

}
