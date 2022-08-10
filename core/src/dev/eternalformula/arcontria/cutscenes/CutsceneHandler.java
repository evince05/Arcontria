package dev.eternalformula.arcontria.cutscenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.util.EFDebug;

public class CutsceneHandler {
	
	private Cutscene cutscene;
	
	private boolean isPlaying;
	
	public CutsceneHandler() {
		this.isPlaying = false;
	}
	
	public void setCutscene(Cutscene cutscene) {
		this.cutscene = cutscene;
	}
	
	public boolean isPlayingCutscene() {
		return isPlaying;
	}
	
	public void play() {
		if (cutscene == null) {
			EFDebug.error("CutsceneHandler has no cutscene to play!");
			return;
		}
		
		isPlaying = true;
	}
	
	public void update(float delta) {
		if (cutscene != null) {
			cutscene.update(delta);
			
			if (cutscene.isFinished()) {
				isPlaying = false;
				cutscene = null;
				EFDebug.info("Cutscene finished!");
			}
		}
		
	}
	
	public void draw(SpriteBatch batch, float delta) {
		if (cutscene != null) {
			cutscene.draw(batch, delta);
		}
	}
	
	public void drawUI(SpriteBatch uiBatch, float delta) {
		if (cutscene != null) {
			cutscene.drawUI(uiBatch, delta);
		}
	}

}
