package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ecs.components.AnimationComponent;
import dev.eternalformula.arcontria.ecs.templates.Player;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.projectiles.ProspectorPickaxe;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.util.EFDebug;

public class GameSession {
	
	private GameScene scene;
	
	private String saveFolder;
	
	private Player player;
	private GameLevel level;
	
	/**
	 * Loads a GameSession from the given save folder.
	 * @param saveFolder The location of the save folder.
	 */
	
	public static GameSession load(GameScene scene, String saveFolder) {
		
		
		// Creates an empty GameSession
		GameSession session = new GameSession(scene);
		session.saveFolder = saveFolder;
		session.level = GameLevel.load(session, saveFolder);
		session.player = Player.loadPlayer(saveFolder);
		/*
		
		// Creates the GameLevel
		 *
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
		 * */
		return session;
	}
	
	GameSession(GameScene scene) {
		this.scene = scene;
	}
	
	public void save() {
		
	}

	public void update(float delta) {
		level.update(delta);
		/*
		if (pickaxe.isFinished()) {
			direction++;
			if (direction > 4) {
				direction = 1;
			}
			pickaxe.reset(new Vector2(1, 2), direction);
		}*/

		/*
		level.update(delta);
		player.handleInput(delta);
		player.update(delta);

		Vector2 centerCameraPos = centerCamera(player);
		getGameCamera().position.set(centerCameraPos, 0f);
		*/
	}
	
	public void resize(int width, int height) {
		level.resize(width, height);
	}
	
	/**
	 * Gets the proper camera position.
	 */
	
	public Vector2 centerCamera(Entity entity) {
		
		Vector2 cameraPos = new Vector2(getGameCamera().position.x,
				getGameCamera().position.y);
		
		Vector2 ePos = new Vector2(entity.getLocation().x,
				entity.getLocation().y);
		
		float viewportWidth = scene.manager.getViewportHandler().getWorldWidth();
		float viewportHeight = scene.manager.getViewportHandler().getWorldHeight();
		
		if (ePos.x >= viewportWidth / 2f - 0.5f && ePos.x <= 
				level.getMapArea().getMapWidth() - viewportWidth / 2f - 0.5f) {
			
			cameraPos.x = ePos.x + 0.5f;
		}
		
		if (ePos.y >= viewportHeight / 2f - 1f && ePos.y <=
				level.getMapArea().getMapHeight() - viewportHeight / 2f - 1f) {
			cameraPos.y = ePos.y + 1f;
		}
		return cameraPos;
	}


	public void dispose() {
		player.dispose();
		
	}

	protected void loadAssets() {
		// TODO Auto-generated method stub
		
	}
	
	public void load() {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(SpriteBatch batch, float delta) {
		level.draw(batch, delta);
		
	}

	public void drawUI(SpriteBatch batch, float delta) {
		level.drawUI(batch, delta);
		
	}

	public void onKeyTyped(char key) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseClicked(int x, int y, int button) {
		level.onMouseClicked(x, y, button);
	}
	
	public void onMouseHovered(int x, int y) {
		level.onMouseHovered(x, y);
	}

	public void onMouseReleased(int x, int y, int button) {
		level.onMouseReleased(x, y, button);
		
	}

	public void onMouseDrag(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	public void onMouseWheelScrolled(int amount) {
		level.onMouseWheelScrolled(amount);
	}
	
	/**
	 * Gets the camera that is used to render the game.
	 */
	
	public OrthographicCamera getGameCamera() {
		return scene.manager.getGameCamera();
	}
	
	public GameLevel getLevel() {
		return level;
	}
	
	/**
	 * Sets the level of the game.
	 * <br>Note that this should be used only to switch levels.
	 * @param level The level to be switched to.
	 */
	
	public void setLevel(GameLevel level) {
	
		/*this.level = level;
		level.addEntity(player);
		setupCamera(player);
		focusCameraOnEntity(player);
		*/
	}
	
	public GameScene getGameScene() {
		return scene;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	private void setupCamera(Entity focusEntity) {
		// Calculates and sets the proper location of the camera.
		Vector2 entPos = focusEntity.getLocation();
		Vector2 cameraPos = new Vector2();

		float viewportWidth = scene.manager.getViewportHandler().getWorldWidth();
		float viewportHeight = scene.manager.getViewportHandler().getWorldHeight();

		float mWidth = level.getMapArea().getMapWidth();
		float mHeight = level.getMapArea().getMapHeight();

		if (mWidth <= viewportWidth) {
			EFDebug.debug("Watch TestLevel.java:setupCamera... line cameraPosX = map.getWidth() / 2f");
			cameraPos.x = mWidth / 2f;
		}
		else {
			float centerX = viewportWidth / 2f - 0.5f;
			if (entPos.x < centerX) {
				cameraPos.x = viewportWidth / 2f;
			}
			else if (entPos.x > mWidth - centerX) {
				cameraPos.x = mWidth - viewportWidth / 2f + 0.5f;
			}
			else {
				cameraPos.x = entPos.x;
			}
		}

		if (mHeight <= viewportHeight) {
			cameraPos.y = mHeight / 2f + 0.5f;
		}
		else {
			float centerY = viewportHeight / 2f - 1f;

			if (entPos.y < centerY) {
				cameraPos.y = viewportHeight / 2f;
			}
			else if (entPos.y > mHeight - centerY) {
				cameraPos.y = mHeight - viewportHeight / 2f + 1f;
			}
			else {
				cameraPos.y = entPos.y;
			}
		}
		scene.manager.getGameCamera().position.set(cameraPos, 0f);
	}
	
	private void focusCameraOnEntity(Entity focusEntity) {
		centerCamera(focusEntity);
	}
	
	public String getSaveFolder() {
		return saveFolder;
	}
	
}
