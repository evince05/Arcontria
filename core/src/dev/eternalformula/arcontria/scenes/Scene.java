package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	
	public abstract void draw(SpriteBatch batch, float delta);
	
	public abstract void update(float delta);
}
