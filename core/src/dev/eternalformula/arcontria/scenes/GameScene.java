package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneHandler;
import dev.eternalformula.arcontria.gfx.animations.ScreenAnimation;

public class GameScene extends Scene {
	
	private GameSession session;
	
	private ScreenAnimation screenAnim;
	
	private CutsceneHandler csHandler;
	
	private float screenAlpha;

	public GameScene(SceneManager manager) {
		super(manager);
	}

	@Override
	protected void loadAssets() {
	}

	@Override
	public void load() {
		
		this.session = GameSession.load(this, null);
		
		this.csHandler = new CutsceneHandler();
		csHandler.setCutscene(Cutscene.load("data/cutscenes/saveintro-land/cutscene.json"));
		//csHandler.play();
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		// Handles fade effects
		super.draw(batch, delta);
		
		batch.begin();
		
		
		
		if (csHandler.isPlayingCutscene()) {
			csHandler.draw(batch, delta);
		}
		else {
			session.draw(batch, delta);
		}
		
		batch.end();
	}

	@Override
	public void drawUI(SpriteBatch batch, float delta) {
		// Handles fade effects
		super.drawUI(batch, delta);
		
		batch.begin();
		
		if (csHandler.isPlayingCutscene()) {
			csHandler.drawUI(batch, delta);
		}
		else {
			session.drawUI(batch, delta);
		}
		
		
		batch.end();
	}

	@Override
	public void update(float delta) {
		
		super.update(delta);
		
		if (screenAnim != null) {
			screenAnim.update(delta);
			screenAlpha = screenAnim.getAlpha();
		}
		
		if (csHandler.isPlayingCutscene()) {
			csHandler.update(delta);
		}
		else {
			session.update(delta);
		}
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void onKeyTyped(char key) {
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
		if (csHandler.isPlayingCutscene()) {
			csHandler.onMouseClicked(x, y, button);
		}
		else {
			session.onMouseClicked(x, y, button);
		}
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		if (csHandler.isPlayingCutscene()) {
			csHandler.onMouseReleased(x, y, button);
		}
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}
	
	@Override
	public void onMouseHovered(int x, int y) {
		if (csHandler.isPlayingCutscene()) {
			csHandler.onMouseHovered(x, y);
		}
	}

	@Override
	public void dispose() {
	}
	
	public ScreenAnimation getScreenAnimation() {
		return screenAnim;
	}
	
	public void setScreenAnimation(ScreenAnimation animation) {
		this.screenAnim = animation;
	}
	
	public float getScreenAlpha() {
		return screenAlpha;
	}
}
