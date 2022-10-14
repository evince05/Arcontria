package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component {
	
	public static final int PLAYER = 0;
	public static final int MAP_OBJECT = 1;
	public static final int NPC = 2;
	public static final int ENEMY = 3;
	public static final int ITEM_ENTITY = 4;
	public static final int OTHER = 5;
	
	public int type = OTHER;
}
