package dev.eternalformula.arcontria.gfx.lighting;

import com.badlogic.gdx.graphics.Color;

import dev.eternalformula.arcontria.level.GameLevel;

public class DaylightHandler {
	
	private static final Color DARKEST_NIGHT_COLOR = new Color(58f / 255f, 35f / 255f, 82f / 255f, 1.0f); // 32, 21, 66
	public static final Color STRONGEST_SUNLIGHT_COLOR = new Color(214f / 255f, 138f / 255f, 45f / 255f, 1.0f);
	
	// A full day is 3000f in length. 2437.5f is roughly 7:30PM
	private static final float SUNSET_START_TIME = 2437.5f;
	
	// A full day is 3000f in length. 2562.5f is roughly 8:30PM
	private static final float SUNSET_END_TIME = 2562.5f;
	
	// A full day is 3000f in length. 812.5f is roughly 6:30AM
	private static final float SUNRISE_START_TIME = 812.5f;
	
	// A full day is 3000f in length. 937.5f is roughly 7:30AM
	private static final float SUNRISE_END_TIME = 937.5f;
	
	public static final float FULL_DAY_LENGTH = 3000f;
	
	// How much time is added to the counter every tick.
	private static final float TIME_INCREMENT_STEP = 0.069444f;
	
	// How much time should elapse over a color transition
	private float transitionTime;
	
	// The rate at which the "R" value should change when undergoing a color change
	private float transitionRateR;
	
	// The rate at which the "G" value should change when undergoing a color change
	private float transitionRateG;
	
	// The rate at which the "B" value should change when undergoing a color change
	private float transitionRateB;
	
	// True if the change rate variables should be recalculated, otherwise false
	private boolean shouldCalculateRates;
	
	// True if the color change rate is halfway done.
	private boolean isHalfwayDone;
	
	private GameLevel level;
	
	private Color currentColor;
	
	// measured in generic units.
	private float currentTime;
	
	// The time elapsed during a color transition.
	private float elapsedTransitionTime;
	
	/**
	 * Creates a new DaylightHandler with the time set to the default start time (9:00AM in-game).
	 * @param level The GameLevel which this DaylightHandler is managing.
	 */
	
	public DaylightHandler(GameLevel level) {
		this.level = level;
		
		// Default tint color, nothing is applied.
		this.currentColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		this.currentTime = 1125f;
		
		this.transitionTime = 0f;
		this.transitionRateR = 0f;
		this.transitionRateG = 0f;
		this.transitionRateB = 0f;
		this.elapsedTransitionTime = 0f;
		
		this.shouldCalculateRates = true;
		this.isHalfwayDone = false;
	}
	
	/**
	 * Creates a new DaylightHandler.
	 * @param level The GameLevel which this DaylightHandler is managing.
	 * @param worldTime The desired time in-game when this DaylightHandler is created.
	 */
	
	public DaylightHandler(GameLevel level, float worldTime) {
		this.level = level;
		
		this.currentColor = new Color(1.0f, 1.0f, 1.0f, 1.0f); // TODO: Calculate proper color based on time.
		this.currentTime = worldTime;
	}
	
	public void update() {
		// With a constant step of 0.069444f, the daylight cycle should finish in approx 12 mins.
		currentTime += TIME_INCREMENT_STEP;
		
		if (currentTime >= FULL_DAY_LENGTH) {
			// Begins new day.
			currentTime = 0f;
		}
		
		if (currentTime >= SUNSET_START_TIME + TIME_INCREMENT_STEP && 
				currentTime <= SUNSET_END_TIME + TIME_INCREMENT_STEP || 
				(currentTime >= SUNRISE_START_TIME + TIME_INCREMENT_STEP && 
				currentTime <= SUNRISE_END_TIME + TIME_INCREMENT_STEP)) {
			calculateCurrentColor();
		}
	}
	
	public void draw() {
		// Applies tints and colors to the MapRenderer's batch and the SpriteBatch.
		level.getMapRenderer().setColor(currentColor);
		level.getScene().getBatch().setColor(currentColor);
	}
	
	private void calculateCurrentColor() {
		
		boolean isSunRising = currentTime >= SUNRISE_START_TIME && currentTime < SUNSET_START_TIME;
				
		if (shouldCalculateRates) {
			// Change rate variables need to be recalculated.
			
			if (!isSunRising) {
				this.transitionTime = SUNSET_END_TIME - SUNSET_START_TIME;
				
				this.transitionRateR = (STRONGEST_SUNLIGHT_COLOR.r * 255f - 255f) / transitionTime * 2f;
				this.transitionRateG = (STRONGEST_SUNLIGHT_COLOR.g * 255f - 255f) / transitionTime * 2f;
				this.transitionRateB = (STRONGEST_SUNLIGHT_COLOR.b * 255f - 255f) / transitionTime * 2f;
			}
			else {
				this.transitionTime = SUNRISE_END_TIME - SUNRISE_START_TIME;
				
				this.transitionRateR = (STRONGEST_SUNLIGHT_COLOR.r - DARKEST_NIGHT_COLOR.r) * 255f / transitionTime * 2f;
				this.transitionRateG = (STRONGEST_SUNLIGHT_COLOR.g - DARKEST_NIGHT_COLOR.g) * 255f / transitionTime * 2f;
				this.transitionRateB = (STRONGEST_SUNLIGHT_COLOR.b - DARKEST_NIGHT_COLOR.b) * 255f / transitionTime * 2f;
			}

			shouldCalculateRates = false;	
		}
			
		if ((currentTime >= SUNRISE_START_TIME + transitionTime / 2f && currentTime < SUNRISE_END_TIME 
				|| currentTime >= SUNSET_START_TIME + transitionTime / 2f && currentTime < SUNSET_END_TIME) && !isHalfwayDone) {
			isHalfwayDone = true;

			if (isSunRising) {
				this.transitionRateR = (1.0f - DARKEST_NIGHT_COLOR.r) * 255f / transitionTime * 2f;
				this.transitionRateG = (1.0f - DARKEST_NIGHT_COLOR.g) * 255f / transitionTime * 2f;
				this.transitionRateB = (1.0f - DARKEST_NIGHT_COLOR.b) * 255f / transitionTime * 2f;
			}
			else {
				this.transitionRateR = (DARKEST_NIGHT_COLOR.r * 255f - currentColor.r * 255f) / transitionTime * 2f;
				this.transitionRateG = (DARKEST_NIGHT_COLOR.g * 255f - currentColor.g * 255f) / transitionTime * 2f;
				this.transitionRateB = (DARKEST_NIGHT_COLOR.b * 255f - currentColor.b * 255f) / transitionTime * 2f;
			}
		}
		
		elapsedTransitionTime += 0.069444f;
		if (elapsedTransitionTime >= 1f) {
			elapsedTransitionTime -= 1f;
			currentColor.r += transitionRateR / 255f;
			currentColor.g += transitionRateG / 255f;
			currentColor.b += transitionRateB / 255f;
		}
		
		boolean isFinished = false;
		
		if (isSunRising) {

			if (currentTime >= SUNRISE_END_TIME) {
				isFinished = true;
				// updates the color so that any small difference is accounted for.
				currentColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
		else {
			if (currentTime >= SUNSET_END_TIME) {
				isFinished = true;
				// updates the color so that any small difference is accounted for.
				currentColor = DARKEST_NIGHT_COLOR;
			}
		}
		if (isFinished) {
			System.out.println("Color transition finished at " + getFormattedWorldTime());
			transitionTime = 0f;
			transitionRateR = 0f;
			transitionRateG = 0f;
			transitionRateB = 0f;
			shouldCalculateRates = true;
			isHalfwayDone = false;
		}
	}
	
	/**
	 * Returns a formatted string from the current world time.
	 * <br> eg. If the time is 0f, this returns 12:00AM.
	 * @return A formatted string representation from the current world time/
	 */
	
	public String getFormattedWorldTime() {
		// 1f = 28.8 seconds (in game)
		float elapsedTimeInSeconds = currentTime * 28.8f;
		float elapsedTimeInHours = elapsedTimeInSeconds / 3600f;
		
		int numHours = (int) Math.floor(elapsedTimeInHours);
		
		if (currentTime >= 1625f) {
			/*
			 * removes 12 hours from the format if the time is past 1pm. This causes the format to no 
			 * longer be in military time and it instead uses regular time.
			 */
		}
		float numMinutes = (elapsedTimeInHours - numHours) * 60;

		// Formats the time in a string formatted in regular time [hours:minutes{AM/PM}]
		StringBuilder formattedTime = new StringBuilder();
		if (numHours > 12) {
			/*
			 * removes 12 hours from the format if the time is past 1pm. This causes the format to no 
			 * longer be in military time and it instead uses regular time.
			 */
			formattedTime.append(numHours - 12 + ":");
		}
		else if (numHours < 1) {
			formattedTime.append(12 + ":");
		}
		else {
			formattedTime.append(numHours + ":");
		}
		
		if (numMinutes < 10f) {
			formattedTime.append('0');
		}
		formattedTime.append((int) Math.floor(numMinutes));
		
		formattedTime.append(currentTime >= 1500f ? "PM" : "AM");
		
		return formattedTime.toString();
	}
	
	public float getWorldTime() {
		return currentTime;
	}
	
	public void setWorldTime(float worldTime) {
		this.currentTime = worldTime;
	}
}
