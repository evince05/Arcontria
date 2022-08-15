package dev.eternalformula.arcontria.gfx.animations;

public abstract class ScreenAnimation {
	
	protected float alpha;
	protected float targetAlpha;
	protected float speed;
	protected boolean isFinished;
	
	ScreenAnimation(float alpha, float targetAlpha) {
		this.alpha = alpha;
		this.targetAlpha = targetAlpha;
		this.speed = 1f;
	}
	
	public abstract void update(float delta);
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public static class FadeOutAnimation extends ScreenAnimation {

		public FadeOutAnimation(float time) {
			super(0f, 1f);
			this.speed = 1f / time;
		}
		
		public FadeOutAnimation(float startingAlpha, float time) {
			super(startingAlpha, 1f);
			this.speed = 1f / time;
		}
		
		@Override
		public void update(float delta) {
			if (alpha + speed * delta < targetAlpha) {
				alpha += speed * delta;
			}
			else {
				alpha = targetAlpha;
				isFinished = true;
			}

		}
	}
	
	public static class FadeInAnimation extends ScreenAnimation {
		
		public FadeInAnimation(float time) {
			super(1f, 0f);
			this.speed = 1f / time;
		}
		
		public FadeInAnimation(float startingAlpha, float time) {
			super(startingAlpha, 0f);
			this.speed = 1f / time;
		}
		
		@Override
		public void update(float delta) {
			if (alpha - speed * delta > targetAlpha) {
				alpha -= speed * delta;
			}
			else {
				alpha = targetAlpha;
				isFinished = true;
			}

		}
	}
}
