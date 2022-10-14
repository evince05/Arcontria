package dev.eternalformula.arcontria.ecs.templates;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ecs.components.AmbientSoundComponent;
import dev.eternalformula.arcontria.ecs.components.AnimationComponent;
import dev.eternalformula.arcontria.ecs.components.CameraFocusComponent;
import dev.eternalformula.arcontria.ecs.components.HealthComponent;
import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.PlayerComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.StateComponent;
import dev.eternalformula.arcontria.ecs.components.StateComponent.State;
import dev.eternalformula.arcontria.ecs.components.TextureComponent;
import dev.eternalformula.arcontria.ecs.components.physics.ColliderComponent;
import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;
import dev.eternalformula.arcontria.scenes.GameScene;
import dev.eternalformula.arcontria.sfx.SoundManager.Sounds;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFUtil;

public class Player {
	
	private static final float BASE_SPEED = 1.8f;
	private Entity playerEntity;
	
	private String saveDirectory;
	
	public static float pickupItemRange = 3f;
	
	/**
	 * Creates a new player object
	 */
	private Player() {
	}
	
	public <T extends Component> T getComponent(Class<T> component) {
		return playerEntity.getComponent(component);
	}
	
	public static Player loadPlayer(World world, String saveDirectory) {
		
		try {
			
			File jsonFile = new File(saveDirectory + File.separator + "player.data");
			if (jsonFile != null && jsonFile.exists()) {
				
				Player player = new Player();
				player.saveDirectory = saveDirectory;
				
				// Creates base Entity and PlayerComponent
				Entity playerEntity = GameScene.ENGINE.createEntity();
				
				// Player exists
				ObjectMapper objMapper = new ObjectMapper();
				TypeReference<HashMap<String, String>> typeRef = new 
						TypeReference<HashMap<String, String>>() {};
				
				// Gets the map of components
				FileHandle fileHandle = new FileHandle(jsonFile);
				
				String decodedStr = Base64Coder.decodeString(fileHandle.readString());
				String decryptedStr = EFUtil.decryptString(decodedStr);
				
				System.out.println("Decrypted: " + decryptedStr);
				Map<String, String> components = objMapper.readValue
						(decryptedStr, typeRef);
				
				// Gets the player's necessary components
				PlayerComponent playerComp = objMapper.readValue(components.get("PlayerComponent"),
						PlayerComponent.class);
				HealthComponent healthComp = objMapper.readValue(components.get("HealthComponent"),
						HealthComponent.class);
				StateComponent stateComp = GameScene.ENGINE.createComponent(StateComponent.class);
				
				AnimationComponent animComp = loadAnimationComponent();
				TextureComponent texComp = GameScene.ENGINE.createComponent
						(TextureComponent.class);
				
				PositionComponent posComp = GameScene.ENGINE.createComponent
						(PositionComponent.class);
				posComp.setPosition(new Vector2(13f, 16f));
				MotionComponent motionComp = GameScene.ENGINE.createComponent
						(MotionComponent.class);
				CameraFocusComponent cfComp = GameScene.ENGINE.createComponent
						(CameraFocusComponent.class);
				
				AmbientSoundComponent asComp = GameScene.ENGINE.createComponent
						(AmbientSoundComponent.class);
				asComp.sound = Sounds.TORCH_CRACKLE;
				
				ColliderComponent collComp = GameScene.ENGINE.createComponent
						(ColliderComponent.class);
				
				collComp.b2dBody = B2DUtil.createBody(world, posComp.getX(), posComp.getY(),
						1f, 2f, BodyType.DynamicBody, PhysicsCategory.PLAYER_COLLIDER, playerEntity);
				
				playerEntity.add(collComp);
				playerEntity.add(asComp);
				playerEntity.add(playerComp);
				playerEntity.add(healthComp);
				playerEntity.add(stateComp);
				playerEntity.add(motionComp);
				playerEntity.add(texComp);
				playerEntity.add(posComp);
				playerEntity.add(animComp);
				playerEntity.add(motionComp);
				playerEntity.add(cfComp);
				
				System.out.println("Welcome back, " + playerComp.getName() + "!");
				System.out.println("You have $" + ((int) playerComp.getBalance()));
				motionComp.setDirection(4);
				stateComp.setState(State.IDLE);

				player.playerEntity = playerEntity;
				GameScene.ENGINE.addEntity(playerEntity);
				
				return player;
			}
			else {
				return Player.createNewPlayer(saveDirectory, "TestPlayer");
			}
		}
		catch (Exception e) {
			EFDebug.error("Error occured while loading player.data!");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	public void savePlayer(String saveDirectory) {
		try {
			File jsonFile = new File(saveDirectory + File.separator + "player.data");
			if (!jsonFile.exists()) {
				// Needs to create a new file
				jsonFile.getParentFile().mkdirs();
				jsonFile.createNewFile();	
			}
			
			// Creates file/print writers
			FileWriter fileWriter = new FileWriter(jsonFile);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			// Create the objectmapper instance
			ObjectMapper objMapper = new ObjectMapper();
			
			// Stringify each component.
			Map<String, String> components = new HashMap<String, String>();
			
			PlayerComponent playerComp = playerEntity.getComponent(PlayerComponent.class);
			String playerCompStr = objMapper.writeValueAsString(playerComp);
			components.put("PlayerComponent", playerCompStr);
			
			HealthComponent healthComp = playerEntity.getComponent(HealthComponent.class);
			String healthCompStr = objMapper.writeValueAsString(healthComp);
			components.put("HealthComponent", healthCompStr);
			
			String componentsStr = objMapper.writeValueAsString(components);
			
			String encryptStr = EFUtil.encryptString(componentsStr);
			String b64 = Base64Coder.encodeString(encryptStr);
			
			printWriter.print(b64);
			printWriter.close();
			fileWriter.close();
		}
		catch (Exception e) {
			EFDebug.error("Error occured while saving player.data!");
			e.printStackTrace();
			System.exit(1);
		}
		
		
	}
	
	public static Player createNewPlayer(String saveDirectory, String name) {
		Player player = new Player();
		player.saveDirectory = saveDirectory;
		player.playerEntity = createPlayerEntity(name, UUID.randomUUID());
		GameScene.ENGINE.addEntity(player.playerEntity);
		return player;
	}
	
	public static Entity createPlayerEntity(String name, UUID playerUUID) {
		
		// Creates the entity
		Entity entity = GameScene.ENGINE.createEntity();
		
		PlayerComponent playerComp = GameScene.ENGINE.createComponent(PlayerComponent.class);
		playerComp.setBalance(100f);
		
		HealthComponent healthComp = GameScene.ENGINE.createComponent(HealthComponent.class);
		StateComponent stateComp = GameScene.ENGINE.createComponent(StateComponent.class);
		stateComp.setState(State.IDLE);
		
		MotionComponent motionComp = GameScene.ENGINE.createComponent(MotionComponent.class);
		motionComp.setSpeed(Player.BASE_SPEED);
		motionComp.setDirection(4);
		
		PositionComponent posComp = GameScene.ENGINE.createComponent(PositionComponent.class);
		posComp.setPosition(new Vector2(13f, 16f));
		
		AnimationComponent animComp = loadAnimationComponent();
		
		TextureComponent texComp = GameScene.ENGINE.createComponent(TextureComponent.class);
		
		CameraFocusComponent camComp = GameScene.ENGINE.createComponent(CameraFocusComponent.class);
		AmbientSoundComponent asc = GameScene.ENGINE.createComponent(AmbientSoundComponent.class);
		asc.sound = Sounds.TORCH_CRACKLE;
		
		entity.add(asc);
		
		entity.add(playerComp);
		entity.add(healthComp);
		entity.add(motionComp);
		entity.add(posComp);
		entity.add(animComp);
		entity.add(texComp);
		entity.add(camComp);
		return entity;
	}
	
	private static AnimationComponent loadAnimationComponent() {
		TextureAtlas playerAtlas = Assets.get("textures/entities/player/player.atlas",
				TextureAtlas.class);
		
		// Creates empty AnimationComponent
		AnimationComponent comp = new AnimationComponent();
		
		JsonNode rootNode = JsonUtil.getRootNode("textures/entities/player/player.anim");
		Iterator<Entry<String, JsonNode>> nodes = rootNode.fields();
		while (nodes.hasNext()) {
			
			Map.Entry<String, JsonNode> node = nodes.next();
			
			// Gets all the values of the animation
			JsonNode animNode = node.getValue();
			int frameCount = animNode.get("frames").intValue();
			int frameW = animNode.get("frameW").intValue();
			int frameH = animNode.get("frameH").intValue();
			String animName = animNode.get("animName").textValue();
			float frameTime = animNode.get("frameTime").floatValue();
			
			// Gets the frames of the animation
			Array<TextureRegion> frames = new Array<TextureRegion>();
			TextureRegion animRegion = playerAtlas.findRegion(node.getKey());
			System.out.println(node.getKey());
			
			for (int i = 0; i < frameCount; i++) {
				frames.add(new TextureRegion(animRegion, i * frameW, 0, frameW, frameH)); 
			}
			
			// Creates the animation object and adds it to the component
			Animation<TextureRegion> anim = new Animation<TextureRegion>(frameTime, frames);
			comp.animations.put(animName, anim);
			
			if (animNode.has("hasIdle") && animNode.get("hasIdle").booleanValue()) {
				// This animation has an idle animation derived from it.
				// Static idle animations are composed of the first frame of the animation.
				// Idea: Add breathing animation (simple inhale/exhale) as idle animations.
				
				anim = new Animation<TextureRegion>(1f, frames.get(0));
				
				/*
				 * Gets the direction of the animation name.
				 * (eg. "moving-left" becomes "left")
				 */
				
				String nameDirection = animName.split("-")[1];
				String idleNameAnim = "idle-" + nameDirection;
				comp.animations.put(idleNameAnim, anim);
			}
		}
		
		return comp;
		
	}
	
	public void dispose() {
		savePlayer(saveDirectory);
	}
	
	public Entity getPlayerEntity() {
		return playerEntity;
	}

}
