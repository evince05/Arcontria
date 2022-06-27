package dev.eternalformula.arcontria.level.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.TextureMapObject;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.util.EFConstants;

/*
 * EFMapObjects are enhanced MapObjects.
 */

public class EFMapObject {
	
	private static final float MIN_ALPHA = 0.55f;
	private static final float TRANSITION_TIME = 0.8f;
	private float alpha;
	private TextureMapObject texObj;
	
	private boolean isFading;
	private boolean isReappearing;
	
	private float y;
	
	/**
	 * Creates a new EFMapObject
	 * @param obj The TextureMapObject from which to create the EFMapObject.
	 */
	
	public EFMapObject(TextureMapObject obj) {
		this.texObj = obj;
		alpha = 1.0f;
		
		this.y = obj.getY() / EFConstants.PPM;
	}
	
	public void update(float delta) {
		if (isFading) {
			// Fade animation.
			if (alpha > MIN_ALPHA) {
				float diff = delta * ((1.0f - MIN_ALPHA) / TRANSITION_TIME);
				if (alpha - diff >= MIN_ALPHA) {
					alpha -= diff;
				}
				else {
					alpha = MIN_ALPHA;
				}
			}
			else {
				// Animation has finished.
				System.out.println("Animation finished (fade)");
				isFading = false;
			}
		}
		else if (isReappearing) {
			// Reappear animation.
			if (alpha < 1f) {
				float extra = delta * ((1.0f - MIN_ALPHA) / TRANSITION_TIME);
				if (alpha + extra <= 1f) {
					alpha += extra;
				}
				else {
					alpha = 1f;
				}

			}
			else {
				// Animation has finished.
				System.out.println("Animation finished (reapp)");
				isReappearing = false;
				ArcontriaGame.GAME.getScene().getLevel().getPlayer().setBehindMapObject(false);
			}
		}
	}
	
	public float getY() {
		return y;
	}
	
	public void draw(SpriteBatch batch, float delta) {
		// Get alpha color
		Color drawColor = batch.getColor();
		batch.setColor(drawColor.r, drawColor.g, drawColor.b, alpha);
		
		batch.draw(texObj.getTextureRegion(),
				(float) (texObj.getX() / EFConstants.PPM), 
				(float) (texObj.getY() / EFConstants.PPM), 
				texObj.getOriginX(), texObj.getOriginY(), 
				texObj.getTextureRegion().getRegionWidth() / EFConstants.PPM, 
				texObj.getTextureRegion().getRegionHeight() / EFConstants.PPM, 
				texObj.getScaleX(), texObj.getScaleY(), texObj.getRotation());
		
		// Resume previous color
		batch.setColor(drawColor.r, drawColor.g, drawColor.b, 1.0f);
	}
	
	public TextureMapObject getTextureMapObject() {
		return texObj;
	}
	
	public void beginFadeAnimation() {
		isFading = true;
	}
	
	public void beginReappearAnimation() {
		isReappearing = true;
	}
	
	
	
}
