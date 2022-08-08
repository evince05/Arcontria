package dev.eternalformula.arcontria.cutscenes.commands;

import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneEntity;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;

public abstract class CutsceneEntityCmd extends CutsceneCommand {
	
	protected CutsceneEntity entity;
	
	public CutsceneEntityCmd(Cutscene cutscene, CutsceneEntity entity) {
		super(cutscene);
		
		this.entity = entity;
	}
	
	public static class CutsceneMoveEntityCmd extends CutsceneEntityCmd {

		private Vector2 moveAmts;
		private Vector2 amtMoved;
		
		private boolean doneMovingX;
		private boolean doneMovingY;
		
		/**
		 * Creates a CutsceneMoveEntityCmd.
		 * @param cutscene The cutscene
		 * @param entity The entity
		 * @param moveAmts The amt to move on the x and y axes.
		 */
		
		public CutsceneMoveEntityCmd(Cutscene cutscene, CutsceneEntity entity, Vector2 moveAmts) {
			super(cutscene, entity);
			
			this.moveAmts = moveAmts;
		}

		@Override
		public void update(float delta) {
			
			if (moveAmts.x > moveAmts.y) {
				if (!doneMovingX) {
					if (amtMoved.x < Math.abs(moveAmts.x)) {
						
						// Move on x axis first
						float velX = entity.getSpeed() * delta;
						entity.move(Math.signum(moveAmts.x * delta), 0f);
						amtMoved.x += velX;
					}
					else {
						doneMovingX = true;
					}
				}
			}
			else {
				if (!doneMovingY) {
					if (amtMoved.y < Math.abs(moveAmts.y)) {
						// Move on y axis first
						float velY = entity.getSpeed() * delta;
						entity.move(0f, Math.signum(moveAmts.y) * velY);
						amtMoved.y += velY;
					}
				}
				
			}
			
			if (doneMovingX && doneMovingY) {
				isFinished = true;
			}
		}
	}

}
