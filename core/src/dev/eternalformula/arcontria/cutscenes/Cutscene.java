package dev.eternalformula.arcontria.cutscenes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;
import dev.eternalformula.arcontria.cutscenes.commands.CutsceneCamMoveCmd;
import dev.eternalformula.arcontria.cutscenes.commands.CutsceneWaitCmd;
import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.level.maps.EFMapRenderer;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFMath;

/**
 * Cutscenes are used to view events in a real-time animation system.
 * @author EternalFormula
 */

public class Cutscene {
	
	private EFMapRenderer mapRend;
	private EFTiledMap map;
	
	private Map<String, CutsceneScript> scripts;
	private Map<String, CutsceneEntity> entities;
	
	private CutsceneScript currentScript;
	private CutsceneCommand currentCmd;
	private boolean isCurrentCmdFinished;
	
	// Waiting commands
	private boolean isWaiting;
	private float waitElapsedTime;
	private float waitTotalTime;
	
	public static Cutscene load(String file) {
		return new Cutscene(file);
	}
	
	/**
	 * Creates and loads a new cutscene.
	 * @param cutsceneFile The cutscene file (.csf file).
	 */
	
	private Cutscene(String cutsceneFile) {
		JsonNode rootNode = JsonUtil.getRootNode(cutsceneFile);

		JsonNode entities = rootNode.get("entities");
		entities.elements().forEachRemaining(e -> {
			System.out.println(e.toString());
		});
		
		Vector2 camLoc = EFMath.vec2FromString(rootNode.get("initialCamPos").textValue());
		ArcontriaGame.GAME.getSceneManager().getGameCamera().position.set(camLoc, 0f);
		
		map = Assets.get(rootNode.get("map").asText(), EFTiledMap.class);
		this.mapRend = new EFMapRenderer();

		// Waiting
		this.isWaiting = false;
		this.waitElapsedTime = 0f;
		this.waitTotalTime = 0f;
		
		// Parsing
		this.isCurrentCmdFinished = true;
		
		// Scripts
		this.scripts = new HashMap<String, CutsceneScript>();
		loadScripts(rootNode);
		
		// Entities
		this.entities = new HashMap<String, CutsceneEntity>();
		loadEntities(rootNode);
		
		// Loads the main script. This will always get run first.
		this.currentScript = scripts.get("main");
		System.out.println("Loaded!");
	}
	
	/**
	 * Loads the entities of the cutscene.
	 * @param rootNode The root node of the cutscene file.
	 */
	
	private void loadEntities(JsonNode rootNode) {
		ArrayNode entitiesNode = (ArrayNode) rootNode.get("entities");
		entitiesNode.forEach(entity -> {
			String name = entity.get("name").textValue();
			UUID uuid = UUID.fromString(entity.get("uuid").textValue());
			entities.put(entity.get("uuid").textValue(), new CutsceneEntity(name, uuid));
		});
	}
	
	/**
	 * Loads the scripts of the cutscene.
	 * @param rootNode The root node of the cutscene file.
	 */
	
	private void loadScripts(JsonNode rootNode) {
		Iterator<Entry<String, JsonNode>> scriptsItr = rootNode.get("scripts").fields();
		scriptsItr.forEachRemaining(script -> {
			JsonNode scrNode = script.getValue();
			String[] commands = new String[scrNode.size()];
			
			// Gets the array of commands in string[] format
			for (int i = 0; i < commands.length; i++) {
				commands[i] = scrNode.get(i).asText();
			}
			
			scripts.put(script.getKey(), new CutsceneScript(commands));
		});
	}
	
	/**
	 * Handles the core functionality of the cutscene.
	 * @param delta Delta time
	 */
	
	public void update(float delta) {
		
		if (isCurrentCmdFinished) {
			// If the current command is finished, the script can move to the next command.
			if (!currentScript.isFinished()) {
				String cmd = currentScript.getCurrentCommand();
				System.out.println("Current cmd: " + cmd);
				parseScriptCommand(cmd);
			}
		}
		else {
			// Updates the current command
			if (currentCmd != null) {
				currentCmd.update(delta);
				
				// Finishes the command once it is completed.
				if (currentCmd.isFinished()) {
					isCurrentCmdFinished = true;
					currentCmd = null;
					currentScript.moveToNextCommand();
				}
			}
			
		}
	}
	
	/**
	 * Draws the regular stuff of the cutscene (entities, maps, etc).
	 * @param batch The Game Batch
	 * @param delta Delta time
	 */
	
	public void draw(SpriteBatch batch, float delta) {
		mapRend.setTiledMap(map);
		mapRend.draw(batch, delta);
	}
	
	/**
	 * Draws the UI of the cutscene (Dialog Boxes, etc.)
	 * @param uiBatch The UIBatch
	 * @param delta Delta time
	 */
	
	public void drawUI(SpriteBatch uiBatch, float delta) {
		
	}
	
	private void parseScriptCommand(String cmd) {
		isCurrentCmdFinished = false;
		String[] args = cmd.split(" ");
		
		if (args[0].equalsIgnoreCase("entity")) {
			
			if (entities.containsKey(args[1])) {
				if (args[2].equalsIgnoreCase("setlocation")) {
					Vector2 pos = EFMath.vec2FromString(args[3]);
					entities.get(args[1]).setLocation(pos);
				}
			}
			else {
				EFDebug.warn("Entity not found in cutscene!");
			}
			//CutsceneEntity entity = 
		}
		else if (args[0].equalsIgnoreCase("camera")) {
			if (args[1].equalsIgnoreCase("moveto")) {
				Vector2 targetPos = EFMath.vec2FromString(args[2]);
				currentCmd = new CutsceneCamMoveCmd(this, targetPos);
			}
			
		}
		else if (args[0].equalsIgnoreCase("dialog")) {
			
		}
		else if (args[0].equalsIgnoreCase("wait")) {
			
			if (args[1] != null) {
				float time = Float.valueOf(args[1].substring(0, args[1].length() - 1));
				currentCmd = new CutsceneWaitCmd(this, time);
			}
			else {
				EFDebug.error("Could not parse command! Improper format: \"" + cmd + "\"");
			}
			
		}
		else if (args[0].equalsIgnoreCase("exit")) {
			
		}
	}
}
