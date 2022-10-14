package dev.eternalformula.arcontria.ecs.systems.quests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import dev.eternalformula.arcontria.ecs.components.quests.CompletedQuestComponent;
import dev.eternalformula.arcontria.ecs.components.quests.QuestComponent;
import dev.eternalformula.arcontria.ecs.components.quests.QuestComponent.CompletedQuestAction;
import dev.eternalformula.arcontria.util.EFDebug;

public class CompletedQuestSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(QuestComponent.class,
			CompletedQuestComponent.class).get();
	
	public CompletedQuestSystem() {
		super(FAMILY);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		QuestComponent questComp = entity.getComponent(QuestComponent.class);
		if (questComp.isQuestActive) {
			if (!questComp.hasActionOccurred) {
				questComp.hasActionOccurred = true;
				
				CompletedQuestAction cqa = questComp.action;
				if (cqa != null) {
					cqa.execute();
					questComp.isQuestActive = false;
				}
				else {
					EFDebug.warn("Quest reward missing for quest \"" + questComp.name + "\"");
				}
			}
		}
	}

}
