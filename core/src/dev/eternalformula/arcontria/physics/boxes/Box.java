package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Box {

	protected Body body;
	protected UUID id;
	
	public void setLinearVelocity(Vector2 velocity) {
		body.setLinearVelocity(velocity);
	}
	
	public void setLinearVelocity(float vX, float vY) {
		body.setLinearVelocity(vX, vY);
	}
	
	public Body getBody() {
		return body;
	}
	
	public UUID getId() {
		return id;
	}
}
