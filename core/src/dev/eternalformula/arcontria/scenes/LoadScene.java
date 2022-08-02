package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.gfx.EGFXUtil;
import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.Strings;

public class LoadScene extends Scene {
	
	private EFLoadSequence loadSequence;
	
	public LoadScene(SceneManager manager) {
		super(manager);
	}
	
	@Override
	public void loadAssets() {
	}
	
	@Override
	public void load() {
		this.loadSequence = new EFLoadSequence();
		manager.getGameCamera().position.set(manager.getViewportHandler().getWorldWidth() / 2f, 
				manager.getViewportHandler().getWorldHeight() / 2f, 0f);
		
		float a = manager.getViewportHandler().getUIViewport().getWorldWidth();
		float b = manager.getViewportHandler().getUIViewport().getWorldHeight();
		
		System.out.println(Strings.vec2(a, b));
		
		System.out.println(Strings.vec3(manager.getUICamera().position));
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		batch.begin();
		
		if (!loadSequence.isFinished()) {
			loadSequence.draw(batch, delta);
		}
		
		batch.end();
	}
	
	@Override
	public void drawUI(SpriteBatch batch, float delta) {
		if (!loadSequence.isFinished()) {
			loadSequence.drawUI(batch, delta);
		}
	}

	@Override
	public void update(float delta) {
		
		if (!loadSequence.isFinished()) {
			loadSequence.update(delta);
		}
		else {
			// Manager can move scenes now.
			manager.setCurrentScene(new CharacterCreatorScene(manager));
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
	}
	
	@Override
	public void onMouseReleased(int x, int y, int button) {
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * The EternalFormula Load Sequence.
	 * @author EternalFormula.
	 */
	
	static class EFLoadSequence {
		
		private Sound bubblesSfx;
		private Sound efRiffSfx;
		
		private TextureAtlas potionAtlas;
		
		private Animation<TextureRegion> potionAnim;
		
		private BitmapFont font;
		
		private boolean isFinished;
		private boolean isTitleFinished;
		
		private boolean hasRiffPlayed;
		
		private float animTime;
		
		private float textTimeElapsed;
		
		private float potionAlpha;
		private float textAlpha;
		
		private static final float TEXT_FADEIN_SPEED = 5/6f;
		private static final float TEXT_DISPLAY_TIME = 1.5f;
		private static final float FADEOUT_SPEED = 2/3f;
		
		public EFLoadSequence() {
			this.bubblesSfx = Gdx.audio.newSound(Gdx.files.internal("sfx/ambient/bubbles.wav"));
			this.efRiffSfx = Gdx.audio.newSound(Gdx.files.internal("loadgame/eternalformula.wav"));
			
			this.potionAlpha = 1f;
			this.textAlpha = 0f;
			
			this.potionAtlas = new TextureAtlas(Gdx.files.internal("loadgame/potionanim.atlas"));
			
			this.font = FontUtil.createFont("fonts/orange-kid.regular.ttf", 40);
			
			// Animation
			Array<TextureRegion> frames = new Array<TextureRegion>();
			TextureRegion region = potionAtlas.findRegion("loadanimation");
			for (int i = 0; i < 23; i++) {
				frames.add(new TextureRegion(region, i * 32, 0, 32, 96));
			}
			
			this.potionAnim = new Animation<TextureRegion>(0.096f, frames);
			bubblesSfx.play(0.6f);
		}
		
		public boolean isFinished() {
			return isFinished;
		}
		
		public void update(float delta) {
			
			animTime += delta;
			
			if (potionAnim.isAnimationFinished(animTime) && !isTitleFinished) {
				bubblesSfx.stop();

				// Start text fade-in
				if (textAlpha < 1f) {
					
					float fadeInAmt = TEXT_FADEIN_SPEED * delta;
					if (textAlpha + fadeInAmt >= 1f) {
						textAlpha = 1f;
						
						isTitleFinished = true;
					}
					else {
						textAlpha += fadeInAmt;
						
						if (textAlpha >= 0.2f) {
							if (!hasRiffPlayed) {
								efRiffSfx.play(0.6f);
								hasRiffPlayed = true;
							}
						}
					}
				}
			}
			if (isTitleFinished) {
				textTimeElapsed += delta;

				if (textTimeElapsed >= TEXT_DISPLAY_TIME) {

					// Handles fading out.
					float fadeOutAmt = FADEOUT_SPEED * delta;

					// Fadeout for text
					if (textAlpha - fadeOutAmt <= 0f) {
						textAlpha = 0f;
					}
					else {
						textAlpha -= fadeOutAmt;
					}

					// Fadeout for potion anim.
					if (potionAlpha - fadeOutAmt <= 0f) {
						potionAlpha = 0f;
					}
					else {
						potionAlpha -= fadeOutAmt;
					}

					if (potionAlpha == 0f && textAlpha == 0f) {
						// Once both are done, the LoadScene is finished.
						isFinished = true;
					}
				}
			}
		}
		
		public void draw(SpriteBatch batch, float delta) {
			
			TextureRegion reg = potionAnim.getKeyFrame(animTime);
			float width = reg.getRegionWidth() / EFConstants.PPM;
			float height = reg.getRegionHeight() / EFConstants.PPM;
			
			System.out.println("draw");
			// Alpha
			Color preColor = batch.getColor();
			Color drawColor = new Color(preColor.r, preColor.g, preColor.b, potionAlpha);
			batch.setColor(drawColor);
			
			// y = 2.625
			batch.draw(reg, 2f, 3.625f, width, height);
			batch.setColor(preColor);
		}
		
		public void drawUI(SpriteBatch batch, float delta) {
			
			batch.enableBlending();
			batch.begin();
			
			float x = Gdx.graphics.getWidth() / 2f / EGFXUtil.RENDER_SCALE;
			float y = (Gdx.graphics.getHeight() / 2f + font.getCapHeight() / 2f) / EGFXUtil.RENDER_SCALE;

			Color fontColor = new Color(1f, 1f, 1f, textAlpha);
			font.setColor(fontColor);
			font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			font.draw(batch, "EternalFormula", x - 75f, y);
			
			batch.end();
		}
		
		public void dispose() {
			bubblesSfx.dispose();
			efRiffSfx.dispose();
			potionAtlas.dispose();
			font.dispose();
		}
	}

}
