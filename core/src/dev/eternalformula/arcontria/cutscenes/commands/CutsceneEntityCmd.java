package dev.eternalformula.arcontria.cutscenes.commands;

import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneEntity;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;

public abstract class CutsceneEntityCmd extends CutsceneCommand {
	
	private CutsceneEntity entity;
	
	public CutsceneEntityCmd(Cutscene cutscene, CutsceneEntity entity) {
		super(cutscene);
		
		this.entity = entity;
	}
	
	public static class CutsceneMoveEntityCmd extends CutsceneEntityCmd {

		private Vector2 delta;
		
		/**
		 * Creates a CutsceneMoveEntityCmd.
		 * @param cutscene The cutscene
		 * @param entity The entity
		 * @param delta The delta x and y
		 */
		
		public CutsceneMoveEntityCmd(Cutscene cutscene, CutsceneEntity entity, Vector2 delta) {
			super(cutscene, entity);
			
			this.delta = delta;
		}

		@Override
		public void update(float delta) {
		}
	}

}
