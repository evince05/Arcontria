package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.ui.elements.EFTypingLabel;
import dev.eternalformula.arcontria.util.Assets;

public class GameScene extends Scene {
	
	private EFTypingLabel label;
	private BitmapFont font;

	public GameScene(SceneManager manager) {
		super(manager);
	}

	@Override
	protected void loadAssets() {
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
	}

	@Override
	public void load() {
		this.label = new EFTypingLabel(null, "Hello Andria. You are a doofus.");
		label.setFont(font);
		label.setLocation(20, 30);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
	}

	@Override
	public void drawUI(SpriteBatch batch, float delta) {
		
		batch.begin();
		label.draw(batch, delta);
		
		batch.end();
	}

	@Override
	public void update(float delta) {
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
