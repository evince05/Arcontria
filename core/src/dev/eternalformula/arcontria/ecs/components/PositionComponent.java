package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
	
	public static final ComponentMapper<PositionComponent> Map =
			ComponentMapper.getFor(PositionComponent.class);
	
	public Vector2 position;
	
	public PositionComponent() {
		this.position = new Vector2(0f, 0f);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public float getX() {
		return position.x;
	}
	
	public void setX(float x) {
		position.x = x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public void setY(float y) {
		position.y = y;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y) {
		this.position = new Vector2(x, y);
	}
	
	

}
