package dev.eternalformula.arcontria.items;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial.MaterialType;

import dev.eternalformula.arcontria.util.Assets;

public enum Material {
	
	BATTLE_AXE("Battle Axe", "battle_axe", 0, MaterialType.WEAPON),
	BUTCHER_KNIFE("Butcher Knife", "butcher_knife", 1, MaterialType.WEAPON),
	DECONSTRUCTOR("Deconstructor", "deconstructor", 2, MaterialType.TOOL),
	ORE_1("Ore 1", "ore_1", 3, MaterialType.GENERIC),
	ORE_2("Ore 2", "ore_2", 4, MaterialType.GENERIC),
	POTION_BOTTLE("Potion Bottle", "potion_bottle", 5, MaterialType.GENERIC),
	POTION_OF_DAMAGE("Potion of Damage", "potion_of_damage", 6, MaterialType.POTION),
	POTION_OF_HEALING("Potion of Healing", "potion_of_healing", 7, MaterialType.POTION),
	POTION_OF_POISON("Potion of Poision", "potion_of_poison", 8, MaterialType.POTION),
	POTION_OF_SPEED("Potion of Speed", "potion_of_speed", 9, MaterialType.POTION),
	RUSTY_LONGSWORD("Rusty Longsword", "rusty_longsword", 10, MaterialType.WEAPON),
	RUSTY_SHORTSWORD("Rusty Shortsword", "rusty_shortsword", 11, MaterialType.WEAPON),
	RUSTY_SWORD("Rusty Sword", "rusty_sword", 12, MaterialType.WEAPON),
	STONE("Stone", "stone", 13, MaterialType.GENERIC);
	
	private static final int MAX_STACK_SIZE_GENERIC = 999;
	private static final int MAX_STACK_SIZE_TOOLS = 1;
	private static final int MAX_STACK_SIZE_POTIONS = 1;
	private static final int MAX_STACK_SIZE_WEAPONS = 1;
	
	private String name;
	private String textureName;
	private int id;
	private MaterialType type;
	
	Material(String displayName, String textureName, int id, MaterialType type) {
		this.name = displayName;
		this.textureName = textureName;
		this.id = id;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public TextureRegion getIcon() {
		return Assets.get("textures/items/itematlas.atlas", TextureAtlas.class)
				.findRegion(textureName);
	}
	
	public int getId() {
		return id;
	}
	
	public int getMaxStackSize() {
		return type.getMaxStackSize();
	}
	
	public enum MaterialType {
		GENERIC(MAX_STACK_SIZE_GENERIC),
		WEAPON(MAX_STACK_SIZE_WEAPONS),
		TOOL(MAX_STACK_SIZE_TOOLS),
		POTION(MAX_STACK_SIZE_POTIONS);
		
		private int maxStackSize;
		
		MaterialType(int maxStackSize) {
			this.maxStackSize = maxStackSize;
		}
		
		public int getMaxStackSize() {
			return maxStackSize;
		}
	};

}
