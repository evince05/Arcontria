package dev.eternalformula.arcontria.cutscenes.commands;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;
import dev.eternalformula.arcontria.util.EFMath;
import dev.eternalformula.arcontria.util.Strings;

/**
 * Cutscene Command for Camera Movement.
 * @author EternalFormula
 */

public class CutsceneCamMoveCmd extends CutsceneCommand {

	private Vector2 targetPos;
	
	private float speed;
	
	public CutsceneCamMoveCmd(Cutscene cutscene, Vector2 targetPos) {
		super(cutscene);
		
		this.targetPos = targetPos;
		this.speed = 1f;
	}
	
	@Override
	public void update(float delta) {
		
		OrthographicCamera cam = ArcontriaGame.GAME.getSceneManager().getGameCamera();
		Vector3 currentPos = cam.position;;

		float slope = (targetPos.y - currentPos.y) / (targetPos.x - currentPos.x);
		
		float deltaX = Math.signum(targetPos.x - currentPos.x) * speed * delta;
		float deltaY = Math.signum(targetPos.y - currentPos.y) * slope * speed * delta;
		
		if (Math.abs(targetPos.x - currentPos.x) <= deltaX) {
			currentPos.x = targetPos.x;
		}
		else {
			currentPos.x += deltaX;
		}
		
		if (Math.abs(targetPos.y - currentPos.y) <= deltaY) {
			currentPos.y = targetPos.y;
		}
		else {
			currentPos.y += deltaY;
		}
		
		if (targetPos.equals(new Vector2(currentPos.x, currentPos.y))) {
			isFinished = true;
		}
	}

}
