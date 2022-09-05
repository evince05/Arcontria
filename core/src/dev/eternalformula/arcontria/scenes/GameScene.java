package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dev.eternalformula.arcontria.cutscenes.CutsceneHandler;
import dev.eternalformula.arcontria.gfx.animations.ScreenAnimation;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.loaders.EFTiledMapLoader;

public class GameScene extends Scene {
	
	private GameSession session;
	
	private ScreenAnimation screenAnim;
	
	private CutsceneHandler csHandler;
	
	private float screenAlpha;

	public GameScene(SceneManager manager) {
		super(manager);
		
		rayHandler.setAmbientLight(0.4f);
	}

	@Override
	protected void loadAssets() {
		Assets.setLoader(EFTiledMap.class, new EFTiledMapLoader(
				new InternalFileHandleResolver(), world, rayHandler));
		Assets.load("textures/maps/scenery/gen_map_scenery.atlas", TextureAtlas.class);
		Assets.load("ui/inventory/gameui.atlas", TextureAtlas.class);
		Assets.load("data/particles/smoke/smoke.particle", ParticleEffect.class);
		Assets.load("maps/data/mines/mine-level-1.tmx", EFTiledMap.class);
		//Assets.load("maps/data/dojo/dojo.tmx", EFTiledMap.class);
		Assets.updateInstance();
	}

	@Override
	public void load() {
		
		this.session = GameSession.load(this, null);
		
		this.csHandler = new CutsceneHandler();
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
		session.getGameCamera().update();
		
		world.step(1 / 60f, 6, 2);
		rayHandler.update();
		rayHandler.setCombinedMatrix(manager.getGameCamera());
		
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
		else {
			session.onMouseReleased(x, y, button);
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
		else {
			session.onMouseHovered(x, y);
		}
	}
	
	@Override
	public void onMouseWheelScrolled(int amount) {
		session.onMouseWheelScrolled(amount);
	}

	@Override
	public void dispose() {
		super.dispose();
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
	
	public GameLevel getLevel() {
		if (session.getLevel() != null) {
			return session.getLevel();
		}
		return null;
	}
}
