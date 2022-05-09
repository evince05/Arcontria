package dev.eternalformula.arcontria;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.utils.ScreenUtils;

import dev.eternalformula.arcontria.level.GameScene;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class ArcontriaGame extends ApplicationAdapter {
	
	public static final String TAG = ArcontriaGame.class.getName();
	
	private GameScene scene;
	
	public static ArcontriaGame GAME;
	
	public static MessageManager msgManager;
	
	private boolean pause;
	
	@Override
	public void create () {
		scene = new GameScene();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		Gdx.app.debug(TAG, "Entering Debug Mode!");
		
		msgManager = MessageManager.getInstance();
		
		GAME = this;
		pause = false;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			pause = !pause;
		}
		
		if (!pause) {
			scene.update(Gdx.graphics.getDeltaTime());
		}
		
		scene.draw(Gdx.graphics.getDeltaTime());
		
	}
	
	@Override
	public void dispose () {
		scene.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		scene.resize(width, height);
		EFDebug.info("Screen Dimensions: " + Strings.vec2(width, height));
	}
	
	public GameScene getScene() {
		return scene;
	}
}
