package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class Box {

	protected Body body;
	protected UUID id;
	
	public Body getBody() {
		return body;
	}
	
	public UUID getId() {
		return id;
	}
}
