package dev.eternalformula.arcontria;

import java.util.Scanner;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.utils.ScreenUtils;

import dev.eternalformula.arcontria.objects.misc.Changelog;
import dev.eternalformula.arcontria.scenes.GameScene;
import dev.eternalformula.arcontria.scenes.Scene;
import dev.eternalformula.arcontria.scenes.SceneManager;
import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Settings;
import dev.eternalformula.arcontria.util.Strings;

public class ArcontriaGame extends ApplicationAdapter {
	
	public static final String TAG = ArcontriaGame.class.getName();
	
	private SceneManager sceneManager;
	
	public static ArcontriaGame GAME;
	
	public static MessageManager msgManager;
	public Settings settings;
	
	private boolean pause;
	
	@Override
	public void create () {
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.debug(TAG, "Entering Debug Mode!");
		
		Assets.setupManager();
		Assets.load();
		
		// Settings
		//this.settings = new Settings();
		while (!Assets.assMan.update()) {
			EFDebug.info("[LOAD] Loading assets (" + (Assets.assMan.getProgress() * 100) + "%)!");
		}
		
		this.sceneManager = new SceneManager();
		
		msgManager = MessageManager.getInstance();
		
		GAME = this;
		
		System.out.println("Arcontria Version [" + Changelog.GAME_VERSION + "] Loaded!");
		System.out.println("\tWelcome back, EternalFormula");
		System.out.println("\nQuickNav - Enter a number to navigate to the following areas");
		System.out.println("\t0 - Changelog");
		System.out.println("\t1 - Character Creator");
		System.out.println("\t2 - Game Level");
		
		/*
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		
		
		Scene scene = null;
		if (choice == 1) {
			scene = new CharacterCreatorScene(sceneManager);
		}
		else if (choice == 2) {
			scene = new GameScene(sceneManager);
		}
		
		if (choice != 0) {
			sceneManager.setCurrentScene(scene);
		}
		else {
			Changelog.load().print();
			
		}*/
		
		sceneManager.setCurrentScene(new GameScene(sceneManager));
		
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			pause = !pause;
		}
		
		if (!pause) {
			sceneManager.update(Gdx.graphics.getDeltaTime());
		}
		sceneManager.draw(Gdx.graphics.getDeltaTime());
		sceneManager.drawUI(Gdx.graphics.getDeltaTime());
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			Gdx.graphics.setWindowedMode(640, 360);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			Gdx.graphics.setWindowedMode(960, 540);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
			Gdx.graphics.setWindowedMode(1280, 720);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
			Gdx.graphics.setWindowedMode(1600, 900);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
			Gdx.graphics.setWindowedMode(1920, 1080);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
	}
	
	@Override
	public void dispose() {
		sceneManager.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		sceneManager.resize(width, height);
		
		EFDebug.info("Screen Dimensions: " + Strings.vec2(width, height));
	}
	
	public Scene getScene() {
		return sceneManager.getCurrentScene();
	}
	
	public SceneManager getSceneManager() {
		return sceneManager;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public float getWindowWidth() {
		return Gdx.graphics.getWidth();
	}
	
	public float getWindowHeight() {
		return Gdx.graphics.getHeight();
	}
}
