package dev.eternalformula.arcontria.entity.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.items.Item;
import dev.eternalformula.arcontria.util.EFConstants;

public class ItemEntity extends Entity {
	
	private Item item;
	
	public ItemEntity(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		TextureRegion region = item.getMaterial().getIcon();
		float w = region.getRegionWidth() / EFConstants.PPM;
		float h = region.getRegionHeight() / EFConstants.PPM;
		
		batch.draw(region, location.x, location.y, w, h);
		
	}

	@Override
	public void destroyBodies(World world) {
	}

}
