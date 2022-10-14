package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class ShadowComponent implements Component {
	
	public float radius;
	
	public float alpha;
	
	/**
	 * This position is added to the entity's position on render.
	 */
	public Vector2 pos;
	
	public static final ComponentMapper<ShadowComponent> Map = 
			ComponentMapper.getFor(ShadowComponent.class);
	
	public ShadowComponent() {
		pos = new Vector2(0f, 0f);
	}
	

}
