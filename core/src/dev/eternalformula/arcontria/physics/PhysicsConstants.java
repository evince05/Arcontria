package dev.eternalformula.arcontria.physics;

import com.badlogic.gdx.graphics.Color;

public class PhysicsConstants {
	
	public static final short BIT_PLAYER_HITBOX = 1;
	public static final short BIT_PLAYER_COLLIDER = 2;
	public static final short BIT_PLAYER_ATTACKBOX = 4;
	public static final short BIT_ENTITY_HITBOX = 16;
	public static final short BIT_ENTITY_COLLIDER = 32;
	public static final short BIT_ENTITY_ATTACKBOX = 64;
	public static final short BIT_MAPOBJECT_COLLIDER = 128;
	public static final short BIT_MAP_BOUNDS = 256;
	public static final short BIT_PROJECTILE_COLLIDER = 512;
	
	private static final short GROUP_ATTACKBOX_COLLIDE = 1;
	private static final short GROUP_COLLIDER_COLLIDE = 2;
	private static final short GROUP_HITBOX_COLLIDE = 4;
	private static final short GROUP_ATTACKBOX_NONCOLLIDE = -1;
	private static final short GROUP_COLLIDER_NONCOLLIDE = -2;
	private static final short GROUP_HITBOX_NONCOLLIDE = -4;
	
	public static final Color DEFAULT_B2DR_STATIC_COLOR = Color.GREEN;
	public static final Color DEFAULT_B2DR_DYNAMIC_COLOR = Color.PINK;
	
	
	
	public enum PhysicsCategory {
		ENTITY_ATTACKBOX(
				BIT_ENTITY_ATTACKBOX,
				BIT_PLAYER_HITBOX,
				//(short) (GROUP_ATTACKBOX_NONCOLLIDE | GROUP_COLLIDER_NONCOLLIDE),
				new Color(0f, 0f, 0f, 0f)),
		ENTITY_COLLIDER(
				BIT_ENTITY_COLLIDER,
				(short) (BIT_MAPOBJECT_COLLIDER | BIT_MAP_BOUNDS | BIT_PLAYER_COLLIDER),
				//(short) (GROUP_ATTACKBOX_NONCOLLIDE | GROUP_HITBOX_NONCOLLIDE),
				new Color(0f, 0f, 0f, 0f)),
		ENTITY_HITBOX(
				BIT_ENTITY_HITBOX,
				(short) (BIT_PLAYER_ATTACKBOX | BIT_PROJECTILE_COLLIDER),
				true,
				//(short) (GROUP_COLLIDER_NONCOLLIDE | GROUP_HITBOX_NONCOLLIDE),
				new Color(0f, 0f, 0f, 0f)),
		PLAYER_ATTACKBOX(
				BIT_PLAYER_ATTACKBOX,
				BIT_ENTITY_HITBOX,
				//(short) (GROUP_ATTACKBOX_NONCOLLIDE | GROUP_COLLIDER_NONCOLLIDE),
				new Color(0f, 0f, 0f, 0f)),
		PLAYER_COLLIDER(
				BIT_PLAYER_COLLIDER,
				(short) (BIT_MAPOBJECT_COLLIDER | BIT_MAP_BOUNDS | BIT_ENTITY_COLLIDER),
				//(short) (GROUP_ATTACKBOX_NONCOLLIDE | GROUP_HITBOX_NONCOLLIDE),
				new Color(0f, 0f, 0f, 0f)),
		PLAYER_HITBOX(
				BIT_PLAYER_HITBOX,
				(short) (BIT_ENTITY_ATTACKBOX | BIT_PROJECTILE_COLLIDER),
				true,
				//(short) (GROUP_COLLIDER_NONCOLLIDE | GROUP_HITBOX_NONCOLLIDE),
				new Color(0f, 0f, 0f, 0f)),
		PROJECTILE_COLLIDER(
				BIT_PROJECTILE_COLLIDER,
				(short) (BIT_PLAYER_HITBOX | BIT_ENTITY_HITBOX | BIT_MAPOBJECT_COLLIDER),
				//(short) (GROUP_ATTACKBOX_NONCOLLIDE | GROUP_COLLIDER_NONCOLLIDE),
				new Color(0f, 0f, 0f, 0f));
		
		private short cBits; // Bits which the object is (doesn't collide with)
		private short mBits; // Bits which the object is not (collides with)
		private boolean isSensor;
		private Color debugColor;
		
		private PhysicsCategory(short cBits, short mBits, Color debugColor) {
			this.cBits = cBits;
			this.mBits = mBits;
			this.debugColor = debugColor;
		}
		
		private PhysicsCategory(short cBits, short mBits, boolean isSensor, Color debugColor) {
			this.cBits = cBits;
			this.mBits = mBits;
			this.isSensor = isSensor;
			this.debugColor = debugColor;
		}
		
		public short getCBits() {
			return cBits;
		}
		
		public short getMBits() {
			return mBits;
		}
		
		public boolean isSensor() {
			return isSensor;
		}
		
		public Color getDebugColor() {
			return debugColor;
		}
	}

}
