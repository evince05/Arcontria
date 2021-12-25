package dev.eternalformula.arcontria.gfx.particles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Particle {

	protected Vector2 position;

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public abstract void update(float delta);
	public abstract void draw(SpriteBatch batch, float delta);
}
