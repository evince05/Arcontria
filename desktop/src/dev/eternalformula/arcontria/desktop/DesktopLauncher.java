package dev.eternalformula.arcontria.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.gfx.EGFXUtil;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) EGFXUtil.DEFAULT_WIDTH;
		config.height = (int) EGFXUtil.DEFAULT_HEIGHT;
		config.resizable = false;
		config.title = "Game";
		new LwjglApplication(new ArcontriaGame(), config);
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
}
