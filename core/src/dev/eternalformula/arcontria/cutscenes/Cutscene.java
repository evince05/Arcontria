package dev.eternalformula.arcontria.cutscenes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;
import dev.eternalformula.arcontria.cutscenes.commands.CutsceneCamMoveCmd;
import dev.eternalformula.arcontria.cutscenes.commands.CutsceneDialogueCmd;
import dev.eternalformula.arcontria.cutscenes.commands.CutsceneFadeCmd;
import dev.eternalformula.arcontria.cutscenes.commands.CutsceneWaitCmd;
import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.level.maps.EFMapRenderer;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.ui.elements.EFDialogueBox;
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
	
	// Dialogue Box
	private EFDialogueBox dialogueBox;
	
	private boolean isFinished;
	
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
		
		this.dialogueBox = new EFDialogueBox("Hello!", 10, 10);
		dialogueBox.setFont(Assets.get("fonts/Habbo.fnt", BitmapFont.class), 136);

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
			Vector2 loc = EFMath.vec2FromString(entity.get("startingLoc").textValue());
			
			CutsceneEntity csEntity = new CutsceneEntity(name, uuid);
			csEntity.setLocation(loc);
			entities.put(entity.get("uuid").textValue(), csEntity);
			
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
		
		entities.forEach((name, entity) -> {
			entity.draw(batch, delta);
		});
	}
	
	/**
	 * Draws the UI of the cutscene (Dialog Boxes, etc.)
	 * @param uiBatch The UIBatch
	 * @param delta Delta time
	 */
	
	public void drawUI(SpriteBatch uiBatch, float delta) {
		if (currentCmd instanceof CutsceneFadeCmd) {
			((CutsceneFadeCmd) currentCmd).drawFade(uiBatch, delta);
		}
		else if (currentCmd instanceof CutsceneDialogueCmd) {
			((CutsceneDialogueCmd) currentCmd).draw(uiBatch, delta);
		}
	}
	
	public void onMouseClicked(int x, int y, int button) {
		if (currentCmd instanceof CutsceneDialogueCmd) {
			((CutsceneDialogueCmd) currentCmd).onMouseClicked(x, y, button);
		}
	}
	
	private void parseScriptCommand(String cmd) {
		isCurrentCmdFinished = false;
		String[] args = cmd.split(" ");
		
		if (args[0].equalsIgnoreCase("entity")) {
			
			if (entities.containsKey(args[1])) {
				if (args[2].equalsIgnoreCase("setlocation")) {
					
					Vector2 pos = EFMath.vec2FromString(args[3]);
					entities.get(args[1]).setLocation(pos);
					endSimpleCommand();
					return;
				}
				else if (args[2].equalsIgnoreCase("setanim")) {
					
					boolean looping = true;
					
					if (args.length >= 5 && args[4] != null) {
						looping = Boolean.valueOf(args[4]);
					}
					
					entities.get(args[1]).setAnimation(args[3], looping);
					endSimpleCommand();
					return;
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
				return;
			}
			else if (args[1].equalsIgnoreCase("setlocation")) {
				
				Vector2 camPos = EFMath.vec2FromString(args[2]);
				ArcontriaGame.GAME.getSceneManager().getGameCamera().position.set(camPos, 0f);
				endSimpleCommand();
				return;
			}
			
		}
		else if (args[0].equalsIgnoreCase("dialogue")) {
			if (args[1] != null) {
				String dialogue = cmd.substring(args[0].length() + 1);
				System.out.println("Dialogue: " + dialogue);
				currentCmd = new CutsceneDialogueCmd(this, dialogue);
			}
		}
		else if (args[0].equalsIgnoreCase("wait")) {
			
			if (args[1] != null) {
				float time = Float.valueOf(args[1].substring(0, args[1].length() - 1));
				currentCmd = new CutsceneWaitCmd(this, time);
				return;
			}
			else {
				EFDebug.error("Could not parse command! Improper format: \"" + cmd + "\"");
				return;
			}
			
		}
		else if (args[0].equalsIgnoreCase("fade")) {
			float time = Float.valueOf(args[2].substring(0, args[2].length() - 1));
			
			if (args[1].equalsIgnoreCase("in")) {
				currentCmd = new CutsceneFadeCmd(this, 0, time);
			}
			else if (args[1].equalsIgnoreCase("out")) {
				currentCmd = new CutsceneFadeCmd(this, 1, time);
			}
		}
		else if (args[0].equalsIgnoreCase("exit")) {
			isFinished = true;
		}
		else {
			System.out.println("No parsing occurred");
		}
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	/**
	 * Utility method for ending simple commands.
	 */
	
	private void endSimpleCommand() {
		isCurrentCmdFinished = true;
		currentScript.moveToNextCommand();
	}
}