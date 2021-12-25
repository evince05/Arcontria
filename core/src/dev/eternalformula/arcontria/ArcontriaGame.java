package dev.eternalformula.arcontria;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import dev.eternalformula.arcontria.level.GameScene;

public class ArcontriaGame extends ApplicationAdapter {
	
	private GameScene scene;
	
	public static ArcontriaGame GAME;
	
	@Override
	public void create () {
		scene = new GameScene();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		GAME = this;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		
		scene.update(Gdx.graphics.getDeltaTime());
		scene.draw(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		scene.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		scene.resize(width, height);
	}
	
	public GameScene getScene() {
		return scene;
	}
}
