package dev.eternalformula.arcontria.ui.hud;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.gfx.EGFXUtil;
import dev.eternalformula.arcontria.gfx.lighting.DaylightHandler;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;

public class WorldClock extends UIElement {

	private DaylightHandler daylightHandler;
	
	private BitmapFont font;
	
	private TextureRegion sunriseIcon; // icon 1
	private TextureRegion sunsetIcon; // icon 3
	private TextureRegion daytimeIcon; // icon 2
	private TextureRegion nighttimeIcon; // icon 4
	
	private int currentIconIndex;
	
	private TextureRegion currentTimeIcon;
	
	private static final float ICON_FADE_SPEED = 5/3f;
	private boolean isFading;
	
	private float fadeDirection;
	private float fadeAmtElapsed;
	private float currentAlpha;
	
	/**
	 * Creates the World Clock.
	 * @param daylightHandler The DaylightHandler being used.
	 */
	
	WorldClock() {
		
		// Sets the location (top right corner)
		this.location = new Vector2(320f - 50f, 180f - 58f);
		
		TextureAtlas atlas = Assets.get("ui/inventory/gameui.atlas",
				TextureAtlas.class);
		
		TextureRegion skin = atlas.findRegion("clocktimedisplay");
		setSkin(skin);
		
		// Sets the regions
		sunriseIcon = atlas.findRegion("sunrise");
		sunsetIcon = atlas.findRegion("sunset");
		daytimeIcon = atlas.findRegion("daytime");
		nighttimeIcon = atlas.findRegion("nighttime");
		
		// Gets the font
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		// Sets the icon
		this.currentIconIndex = 2;
		this.currentTimeIcon = daytimeIcon;
		
	}
	
	@Override
	public void onMouseHovered(int x, int y) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}

	@Override
	public void onMouseWheelScrolled(int amount) {
	}

	@Override
	public void update(float delta) {
		
		float time = daylightHandler.getWorldTime();
		
		if (currentIconIndex != getCurrentIconIndex(time)) {
			beginFadeIfNeeded(time);
		}
		
		
		if (isFading) {
			calculateCurrentAlpha(delta);
		}
		
		calculateTimeIcon(daylightHandler.getWorldTime());
	}

	/**
	 * Calculates the current alpha in the fade animation.
	 * @param delta The delta time
	 */
	
	private void calculateCurrentAlpha(float delta) {
		float deltaAmt = ICON_FADE_SPEED * delta;
		
		if (fadeDirection == -1) {
			if (currentAlpha - deltaAmt >= 0f) {
				currentAlpha -= deltaAmt;
				fadeAmtElapsed += deltaAmt;
			}
			else {
				fadeAmtElapsed += currentAlpha;
				currentAlpha = 0f;
			}
		}
		else if (fadeDirection == 1) {
			if (currentAlpha + deltaAmt <= 1f) {
				currentAlpha += deltaAmt;
				fadeAmtElapsed += deltaAmt;
			}
			else {
				fadeAmtElapsed += 1f - currentAlpha;
				currentAlpha = 1f;
			}
		}
		
		
		if (currentAlpha == 1f) {
			fadeDirection = -1;
		}
		
		if (fadeAmtElapsed >= 2f) {
			fadeAmtElapsed = 0f;
			isFading = false;
		}
	}
	
	public int getCurrentIconIndex(float worldTime) {
		if (worldTime >= DaylightHandler.SUNRISE_START_TIME &&
				worldTime < DaylightHandler.SUNRISE_END_TIME) {
			// Sunrise
			return 1;
		}
		else if (worldTime >= DaylightHandler.SUNSET_START_TIME &&
				worldTime < DaylightHandler.SUNSET_END_TIME) {
			// Sunset
			return 3;
		}
		else if (worldTime >= DaylightHandler.SUNSET_END_TIME ||
				worldTime < DaylightHandler.SUNRISE_START_TIME) {
			// Nighttime
			return 4;
		}
		else {
			// Daytime
			return 2;
		}
	}
	
	/**
	 * Determines if a fade effect needs to start.
	 * @param time The world time.
	 */
	
	private void beginFadeIfNeeded(float time) {
		if ((time >= DaylightHandler.SUNRISE_START_TIME && time < 
				DaylightHandler.SUNRISE_END_TIME) && currentIconIndex != 1) {
			if (!isFading) {
				isFading = true;
				fadeDirection = 1;
				currentIconIndex = 1;
			}
		}
		else if ((time >= DaylightHandler.SUNRISE_END_TIME && time <
				DaylightHandler.SUNSET_START_TIME) && currentIconIndex != 2) {
			if (!isFading) {
				isFading = true;
				fadeDirection = 1;
				currentIconIndex = 2;
			}
		}
		else if ((time >= DaylightHandler.SUNSET_START_TIME && time < 
				DaylightHandler.SUNSET_END_TIME) && currentIconIndex != 3) {
			if (!isFading) {
				isFading = true;
				fadeDirection = 1;
				currentIconIndex = 3;
			}
		}
		else if ((time >= DaylightHandler.SUNSET_END_TIME && time <
				DaylightHandler.SUNRISE_START_TIME) && currentIconIndex != 4) {
			if (!isFading) {
				isFading = true;
				fadeDirection = 1;
				currentIconIndex = 4;
			}
		}
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		float w = skin.getRegionWidth();
		float h = skin.getRegionHeight();
		
		uiBatch.draw(skin, location.x, location.y, w, h);
		uiBatch.draw(currentTimeIcon, location.x + 14f, location.y + 22f);
		
		Rectangle alphaRect = new Rectangle(location.x + 14f, location.y + 22f, 32f, 32f);
		if (isFading) {
			EGFXUtil.drawAlphaRect(uiBatch, alphaRect, currentAlpha);
		}
		
		
		font.draw(uiBatch, daylightHandler.getFormattedWorldTime(),
				location.x + 6f, location.y + 20f);
		
	}
	
	private void calculateTimeIcon(float worldTime) {
		if (!isFading) {
			if (worldTime > DaylightHandler.SUNSET_START_TIME &&
					worldTime < DaylightHandler.SUNSET_END_TIME) {
				// Sunset
				currentTimeIcon = sunsetIcon;
			}
			else if (worldTime >= DaylightHandler.SUNSET_END_TIME ||
					worldTime < DaylightHandler.SUNRISE_START_TIME) {
				// Nighttime
				currentTimeIcon = nighttimeIcon;
			}
			else if (worldTime >= DaylightHandler.SUNRISE_START_TIME &&
					worldTime < DaylightHandler.SUNRISE_END_TIME) {
				// Sunrise
				currentTimeIcon = sunriseIcon;
			}
			else {
				// Daytime
				currentTimeIcon = daytimeIcon;
			}
		}
		else {
			if (fadeAmtElapsed >= 1f) {
				switch (currentIconIndex) {
				case 1:
					currentTimeIcon = sunriseIcon;
					break;
				case 2:
					currentTimeIcon = daytimeIcon;
					break;
				case 3:
					currentTimeIcon = sunsetIcon;
					break;
				case 4:
					currentTimeIcon = nighttimeIcon;
					break;
				default:
					currentTimeIcon = daytimeIcon;
					break;
				}
			}
		}
		
	}
	
	public void setDaylightHandler(DaylightHandler daylightHandler) {
		this.daylightHandler = daylightHandler;
	}
	
	

}
