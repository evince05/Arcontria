package dev.eternalformula.arcontria.ecs.systems.quests;

import java.io.File;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.ecs.components.quests.CompletedQuestComponent;
import dev.eternalformula.arcontria.ecs.components.quests.QuestComponent;
import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.scenes.GameScene;
import dev.eternalformula.arcontria.util.EFDebug;

public class IncompleteQuestSystem extends IteratingSystem {
	
	private static boolean QUESTS_LOADED = false;
	
	private static final Family FAMILY = Family.all(QuestComponent.class)
			.exclude(CompletedQuestComponent.class).get();
	
	public IncompleteQuestSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		QuestComponent questComp = entity.getComponent(QuestComponent.class);
		if (questComp.isQuestActive) {
			if (questComp.currentValue.equals(questComp.completedValue)) {
				// Quest is complete
				System.out.println("Quest complete!");
				entity.add(GameScene.ENGINE.createComponent(CompletedQuestComponent.class));
			}
		}
		
	}
	
	public static void loadQuests(Engine engine, String saveFolder) {
		if (!IncompleteQuestSystem.QUESTS_LOADED) {
			
			long s = System.currentTimeMillis();
			
			String path = saveFolder + File.separator + "player.data";
			
			int questsLoaded = 0;
			
			// Gets the necessary nodes
			JsonNode questInfoNode = JsonUtil.getRootNode("data/quests/quests.data").get("quests");
			//JsonNode pdQuestNode = JsonUtil.getRootNodeFromLocalFile(path).get("quests");
			
			for (JsonNode quest : questInfoNode) {
				Entity questEntity = engine.createEntity();
				
				// Creates the QuestComponent.
				QuestComponent questComp = engine.createComponent(QuestComponent.class);
				questComp.name = quest.get("name").asText();
				questComp.displayName = quest.get("displayName").asText();
				questComp.promptText = quest.get("displayText").asText();
				questComp.completedValue = quest.get("completedValue");
				
				// Sets the QuestComponent's currentValue to what is stored in the player.data file.
				
				/*
				for (JsonNode pdQuest : pdQuestNode) {
					if (pdQuest.get("name").asText().equals(questComp.name)) {
						questComp.currentValue = pdQuest.get("currentValue");
						break;
					}
					else {
						continue;
					}
				}*/
				
				// TODO: Add CompletedQuestAction
				
				// Adds the QuestEntity to the engine.
				questEntity.add(questComp);
				engine.addEntity(questEntity);
				questsLoaded++;
			}
			
			IncompleteQuestSystem.QUESTS_LOADED = true;
			
			long e = System.currentTimeMillis();
			double t = (e - s) / 1000D;
			EFDebug.info("Loaded " + questsLoaded + " quests! (" + t + "s)");
		}
		else {
			EFDebug.info("Ignored call to load quests. Already loaded!");
		}
	}

}
