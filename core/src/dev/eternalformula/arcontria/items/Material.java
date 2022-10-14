package dev.eternalformula.arcontria.items;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial.MaterialType;

import dev.eternalformula.arcontria.util.Assets;

public enum Material {
	
	ACRONO_WOOD("data/items/acrono_wood.itd"),
	CHEST("data/items/chest.itd"),
	LOCARUM_BAR("data/items/locarum_bar.itd"),
	LOCARUM_ORE("data/items/locarum_ore.itd"),
	RUDIAM_BAR("data/items/rudiam_bar.itd"),
	RUDIAM_ORE("data/items/rudiam_ore.itd"),
	VISMADA_BAR("data/items/vismada_bar.itd"),
	VISMADA_CLOBO("data/items/vismada_clobo.itd"),
	VISMADA_CLOCA("data/items/vismada_cloca.itd"),
	VISMADA_CLOSHI("data/items/vismada_closhi.itd"),
	VISMADA_ORE("data/items/vismada_ore.itd"),
	VISMADA_LONGSWORD("data/items/vismada_longsword.itd");
	
	int id;
	
	String name;
	String displayName;
	List<String> lore;
	
	int maxStackSize;
	int durability;
	
	boolean hasDurability;
	boolean isConsumable;
	boolean isEquipable;
	
	Material(String itemDataFile){
		MaterialLoader.loadMaterial(this, itemDataFile);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public TextureRegion getIcon() {
		TextureAtlas uiAtlas = Assets.get("textures/items/itematlas.atlas", TextureAtlas.class);
		TextureRegion icon = uiAtlas.findRegion(name);
		
		return icon != null ? icon : uiAtlas.findRegion("item_null"); 
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public boolean isConsumable() {
		return isConsumable;
	}
	
	public boolean isEquipable() {
		return isEquipable;
	}

}
