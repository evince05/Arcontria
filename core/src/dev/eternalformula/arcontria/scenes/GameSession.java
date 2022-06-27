package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.level.GameLevel;

public class GameSession extends Scene {
	
	private String saveFile;
	
	private Player player;
	private GameLevel level;
	
	/**
	 * Loads a GameSession from the given save folder.
	 * @param saveFolder The location of the save folder.
	 */
	
	public static GameSession load(String saveFolder) {
		Player player = new Player();
	}
	
	public void save();

	@Override
	public void draw(SpriteBatch batch, float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
	}

}
