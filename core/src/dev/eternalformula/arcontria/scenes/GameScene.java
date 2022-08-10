package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneHandler;
import dev.eternalformula.arcontria.ui.elements.EFTypingLabel;
import dev.eternalformula.arcontria.util.Assets;

public class GameScene extends Scene {
	
	private GameSession session;
	
	private EFTypingLabel label;
	private BitmapFont font;
	
	private CutsceneHandler csHandler;

	public GameScene(SceneManager manager) {
		super(manager);
	}

	@Override
	protected void loadAssets() {
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
	}

	@Override
	public void load() {
		this.label = new EFTypingLabel(null, "Woah, take it easy! You hit your head pretty hard... Don't worry about that right now. Everything will be okay, trust me.");
		label.setColor(Color.LIME);
		label.setupWrapping(font, 240);
		label.setLocation(20, 60);
		
		this.session = GameSession.load(this, null);
		
		this.csHandler = new CutsceneHandler();
		csHandler.setCutscene(Cutscene.load("data/cutscenes/testcutscene.json"));
		csHandler.play();
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		batch.begin();
		
		session.draw(batch, delta);
		
		//if (csHandler.isPlayingCutscene()) {
			//csHandler.draw(batch, delta);
		//}
		
		batch.end();
	}

	@Override
	public void drawUI(SpriteBatch batch, float delta) {
		
		batch.begin();
		
		session.drawUI(batch, delta);
		
		if (csHandler.isPlayingCutscene()) {
			csHandler.drawUI(batch, delta);
		}
		label.draw(batch, delta);
		
		batch.end();
	}

	@Override
	public void update(float delta) {
		
		session.update(delta);
		
		if (csHandler.isPlayingCutscene()) {
			csHandler.update(delta);
		}
		label.update(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void onKeyTyped(char key) {
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {	
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}

	@Override
	public void dispose() {
	}
}
