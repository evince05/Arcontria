package dev.eternalformula.arcontria.physics;

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
	
	private static final short GROUP_ATTACKBOX = -1;
	private static final short GROUP_COLLIDER = -2;
	private static final short GROUP_HITBOX = -4;
	
	
	
	public enum PhysicsCategory {
		ENTITY_ATTACKBOX(
				BIT_ENTITY_ATTACKBOX,
				BIT_PLAYER_HITBOX,
				(short) (GROUP_ATTACKBOX | GROUP_COLLIDER)),
		ENTITY_COLLIDER(
				BIT_ENTITY_COLLIDER,
				(short) (BIT_MAPOBJECT_COLLIDER | BIT_MAP_BOUNDS | BIT_PLAYER_COLLIDER),
				(short) (GROUP_ATTACKBOX | GROUP_HITBOX)),
		ENTITY_HITBOX(
				BIT_ENTITY_HITBOX,
				(short) (BIT_PLAYER_ATTACKBOX | BIT_PROJECTILE_COLLIDER),
				(short) (GROUP_COLLIDER | GROUP_HITBOX)),
		PLAYER_ATTACKBOX(
				BIT_PLAYER_ATTACKBOX,
				BIT_ENTITY_HITBOX,
				(short) (GROUP_ATTACKBOX | GROUP_COLLIDER)),
		PLAYER_COLLIDER(
				BIT_PLAYER_COLLIDER,
				(short) (BIT_MAPOBJECT_COLLIDER | BIT_MAP_BOUNDS | BIT_ENTITY_COLLIDER),
				(short) (GROUP_ATTACKBOX | GROUP_HITBOX)),
		PLAYER_HITBOX(
				BIT_PLAYER_HITBOX,
				(short) (BIT_ENTITY_ATTACKBOX | BIT_PROJECTILE_COLLIDER),
				(short) (GROUP_COLLIDER | GROUP_HITBOX)),
		PROJECTILE_COLLIDER(
				BIT_PROJECTILE_COLLIDER,
				(short) (BIT_PLAYER_HITBOX | BIT_ENTITY_HITBOX | BIT_MAPOBJECT_COLLIDER),
				(short) (GROUP_ATTACKBOX | GROUP_COLLIDER));
		
		private short cBits; // Bits which the object is (doesn't collide with)
		private short mBits; // Bits which the object is not (collides with)
		private short gIndex;
		
		private PhysicsCategory(short cBits, short mBits, short gIndex) {
			this.cBits = cBits;
			this.mBits = mBits;
			this.gIndex = gIndex;
		}
		
		public short getCBits() {
			return cBits;
		}
		
		public short getMBits() {
			return mBits;
		}
		
		public short getGIndex() {
			return gIndex;
		}
	}

}
