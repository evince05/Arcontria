package dev.eternalformula.arcontria.scenes;

import java.io.File;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.entity.player.PlayerData;
import dev.eternalformula.arcontria.gfx.EGFXUtil;
import dev.eternalformula.arcontria.level.GameLevel;

/*
public class GameSession extends Scene {
	
	private String saveFolder;
	
	private Player player;
	private GameLevel level;
	
	private GameViewportHandler viewportHandler;
	private SpriteBatch batch;
	
	/**
	 * Loads a GameSession from the given save folder.
	 * @param saveFolder The location of the save folder.
	 
	
	public static GameSession load(String saveFolder) {
		// Creates an empty GameSession
		GameSession session = new GameSession();
		session.saveFolder = saveFolder;
		
		// Creates the viewport handler
		GameViewportHandler viewportHandler = new GameViewportHandler(0, 0);
		session.viewportHandler = viewportHandler;
		
		// Creates the GameLevel
		GameLevel level = GameLevel.load(session, saveFolder);
		session.level = level;
		
		// Creates the player
		PlayerData data = PlayerData.loadFromFile(saveFolder + File.separator + "playerdata.json");
		JsonNode playerDataNode = data.getPlayerDataNode();
		
		String playerName = playerDataNode.get("name").asText();
		UUID playerUUID = UUID.fromString(playerDataNode.get("uuid").asText());
		
		Player player = Player.create(level, playerName, playerUUID);
		Vector2 playerLoc = new Vector2(playerDataNode.get("locationX").floatValue(),
				playerDataNode.get("locationY").floatValue());
		
		player.setLocation(playerLoc);
		player.setPlayerData(data);
		session.player = player;
		
		//level.setupCamera();
		return session;
	}
	
	GameSession() {
		this.batch = new SpriteBatch();
	}
	
	public void save() {
		
	}

	@Override
	public void draw(float delta) {
		batch.setProjectionMatrix(viewportHandler.getViewport().getCamera().combined);
		
		level.draw(batch, delta);
		
	}

	@Override
	public void update(float delta) {
		viewportHandler.update(delta);
		
		level.update(delta);
		player.handleInput(delta);
		player.update(delta);

		Vector2 centerCameraPos = centerCamera(player);
		getGameCamera().position.set(centerCameraPos, 0f);
	}
	
	@Override
	public void resize(int width, int height) {
		viewportHandler.resize(width, height);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Gets the camera that is used to render the game.
	 
	
	public OrthographicCamera getGameCamera() {
		return (OrthographicCamera) viewportHandler.getViewport().getCamera();
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	/**
	 * Gets the proper camera position.
	 */
	
	/*
	public Vector2 centerCamera(Player player) {
		
		Vector2 cameraPos = new Vector2(getGameCamera().position.x,
				getGameCamera().position.y);
		
		Vector2 playerPos = new Vector2(player.getLocation().x,
				player.getLocation().y);
		
		float viewportWidth = viewportHandler.width;
		float viewportHeight = viewportHandler.height;
		
		//System.out.println(playerPos.x + 0.5f);
		
		// replace 100 with map.getWidth() or map.getHeight()
		if (playerPos.x >= viewportWidth / 2f - 0.5f && playerPos.x <= 100 - viewportWidth / 2f - 0.5f) {
			cameraPos.x = playerPos.x + 0.5f;
		}
		
		if (playerPos.y >= viewportHeight / 2f - 1f && playerPos.y <= 100 - viewportHeight / 2f - 1f) {
			cameraPos.y = playerPos.y + 1f;
		}
		return cameraPos;
	}*/

	/**
	 * Gets the viewport width
	 */
		
	/*
	public float getViewportWidth() {
		return viewportHandler.getViewport().getWorldWidth();
	}
	
	/**
	 * Gets the viewport height
	 */
	
	/*
	public float getViewportHeight() {
		return viewportHandler.getViewport().getWorldHeight();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public GameLevel getLevel() {
		return level;
	}
	
	private static class GameViewportHandler {
		
		private ScreenViewport viewport;
		
		private int width;
		private int height;
		
		GameViewportHandler(int width, int height) {
			
			this.width = width;
			this.height = height;
			
			this.viewport = new ScreenViewport();
			viewport.setUnitsPerPixel(EGFXUtil.DEFAULT_UPP);
			viewport.update(width, height);
		}
		
		public void update(float delta) {
			viewport.update(width, height);
		}
		
		public void resize(int width, int height) {
			this.width = width;
			this.height = height;
		}
		
		public ScreenViewport getViewport() {
			return viewport;
		}
		
		public float getWorldWidth() {
			return viewport.getWorldWidth();
		}
		
		public float getWorldHeight() {
			return viewport.getWorldHeight();
		}
	}
}*/
