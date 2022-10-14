package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class MotionComponent implements Component {
	
	public static final ComponentMapper<MotionComponent> Map =
			ComponentMapper.getFor(MotionComponent.class);
	
	public static final float DEFAULT_ENTITY_SPEED = 1.5f;
	
	public Vector2 velocity;
	public Vector2 acceleration;
	
	public boolean isMoving;
	
	public MotionComponent() {
		this.speed = DEFAULT_ENTITY_SPEED;
		this.direction = 4;
		this.velocity = new Vector2(0f, 0f);
		this.acceleration = new Vector2(0f, 0f);
	}
	
	public float speed;
	public int direction;
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
