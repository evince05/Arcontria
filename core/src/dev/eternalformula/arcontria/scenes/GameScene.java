package dev.eternalformula.arcontria.scenes;

import java.io.File;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneHandler;
import dev.eternalformula.arcontria.ecs.components.ItemEntityComponent;
import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.PathfindingComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.ShadowComponent;
import dev.eternalformula.arcontria.ecs.components.TextureComponent;
import dev.eternalformula.arcontria.ecs.components.quests.QuestComponent;
import dev.eternalformula.arcontria.ecs.systems.AnimationSystem;
import dev.eternalformula.arcontria.ecs.systems.CameraFocusSystem;
import dev.eternalformula.arcontria.ecs.systems.HealthSystem;
import dev.eternalformula.arcontria.ecs.systems.ItemEntitySystem;
import dev.eternalformula.arcontria.ecs.systems.PathfindingSystem;
import dev.eternalformula.arcontria.ecs.systems.PlayerControlSystem;
import dev.eternalformula.arcontria.ecs.systems.RenderingSystem;
import dev.eternalformula.arcontria.ecs.systems.physics.MovementSystem;
import dev.eternalformula.arcontria.ecs.systems.physics.PhysicsSystem;
import dev.eternalformula.arcontria.ecs.systems.quests.CompletedQuestSystem;
import dev.eternalformula.arcontria.ecs.systems.quests.IncompleteQuestSystem;
import dev.eternalformula.arcontria.ecs.systems.sfx.AmbientSoundSystem;
import dev.eternalformula.arcontria.ecs.templates.MineRock;
import dev.eternalformula.arcontria.ecs.templates.Player;
import dev.eternalformula.arcontria.files.FileUtil;
import dev.eternalformula.arcontria.gfx.animations.ScreenAnimation;
import dev.eternalformula.arcontria.items.Item;
import dev.eternalformula.arcontria.items.Material;
import dev.eternalformula.arcontria.items.Recipe;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.Strings;
import dev.eternalformula.arcontria.util.loaders.EFTiledMapLoader;

public class GameScene extends Scene {
	
	private GameSession session;
	
	private ScreenAnimation screenAnim;
	
	private CutsceneHandler csHandler;
	
	private float screenAlpha;
	
	public static final PooledEngine ENGINE = new PooledEngine();

	public GameScene(SceneManager manager) {
		super(manager);
		
		rayHandler.setAmbientLight(0.4f);
	}

	@Override
	protected void loadAssets() {
		Assets.setLoader(EFTiledMap.class, new EFTiledMapLoader(
				new InternalFileHandleResolver(), world, rayHandler));
		Assets.load("textures/items/itematlas.atlas", TextureAtlas.class);
		Assets.load("textures/maps/scenery/gen_map_scenery.atlas", TextureAtlas.class);
		Assets.load("ui/inventory/gameui.atlas", TextureAtlas.class);
		Assets.load("data/particles/smoke/smoke.particle", ParticleEffect.class);
		Assets.load("maps/data/mines/mine-level-1.tmx", EFTiledMap.class);
		Assets.load("data/cutscenes/saveintro-land/hospitalroom.tmx", EFTiledMap.class);
		Assets.load("textures/entities/projectiles/projectiles.atlas", TextureAtlas.class);
		Assets.load("textures/entities/player/player.atlas", TextureAtlas.class);
		
		//Assets.load("maps/data/dojo/dojo.tmx", EFTiledMap.class);
		Assets.updateInstance();
	}

	@Override
	public void load() {
		ENGINE.addSystem(new HealthSystem());
		ENGINE.addSystem(new RenderingSystem(manager.getGameBatch()));
		ENGINE.addSystem(new AnimationSystem());
		ENGINE.addSystem(new CameraFocusSystem(manager.getGameCamera()));
		ENGINE.addSystem(new PlayerControlSystem());
		ENGINE.addSystem(new MovementSystem());
		ENGINE.addSystem(new ItemEntitySystem());
		ENGINE.addSystem(new PathfindingSystem());
		ENGINE.addSystem(new AmbientSoundSystem());
		ENGINE.addSystem(new PhysicsSystem());
		ENGINE.addSystem(new IncompleteQuestSystem());
		ENGINE.addSystem(new CompletedQuestSystem());
		
		this.session = GameSession.load(this, FileUtil.SAVES_FOLDER_LOCATION + File.separator + "TestRun");
		
		this.csHandler = new CutsceneHandler();
		
		final Item testItem = new Item(Material.VISMADA_ORE, 25);
		
		Entity itemEntity = ENGINE.createEntity();
		ItemEntityComponent itemComp = ENGINE.createComponent(ItemEntityComponent.class);
		itemComp.setItem(testItem);
		TextureComponent texComp = ENGINE.createComponent(TextureComponent.class);
		PositionComponent posComp = ENGINE.createComponent(PositionComponent.class);
		posComp.position = new Vector2(14f, 22f);
		
		ShadowComponent shadComp = ENGINE.createComponent(ShadowComponent.class);
		shadComp.alpha = 0.10f;
		shadComp.radius = 0.30f;
		shadComp.pos = new Vector2(0f, -0.3f);
		
		PathfindingComponent pathComp = ENGINE.createComponent(PathfindingComponent.class);
		MotionComponent motionComp = ENGINE.createComponent(MotionComponent.class);
		itemEntity.add(pathComp);
		itemEntity.add(motionComp);
		
		Entity mineRockEnt = MineRock.createMineRock(world, new Vector2(16f, 20f), Material.VISMADA_ORE);
		ENGINE.addEntity(mineRockEnt);
		
		texComp.setTextureRegion(itemComp.getItem().getMaterial().getIcon());
		itemEntity.add(itemComp);
		itemEntity.add(posComp);
		itemEntity.add(texComp);
		itemEntity.add(shadComp);
		ENGINE.addEntity(itemEntity);
		
		Entity questEnt = ENGINE.createEntity();
		QuestComponent questComp = ENGINE.createComponent(QuestComponent.class);
		questComp.completedValue = 0;
		questComp.currentValue = 0;
		questComp.isQuestActive = true;
		questEnt.add(questComp);
		ENGINE.addEntity(questEnt);
		//csHandler.play();
		
		long s = System.currentTimeMillis();
		Array<Recipe> recipes = Recipe.getAllContaining("a");
		long e = System.currentTimeMillis();
		System.out.println("Parsed Recipes in " + (e - s) / 1000D + "s");
		recipes.forEach(recipe -> {
			System.out.println("Found recipe: " + Strings.inQuotations(recipe.getSearchName()));
		});
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
			ENGINE.update(delta);
			
			batch.end();
			rayHandler.render();
			batch.begin();
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
		session.onKeyTyped(key);
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
		session.onMouseDrag(x, y);
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
		
		session.dispose();
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
	
	public Player getPlayer() {
		if (session != null) {
			return session.getPlayer();
		}
		return null;
	}
}
