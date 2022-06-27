package dev.eternalformula.arcontria.physics.boxes;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.level.maps.EFMapObject;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;
import dev.eternalformula.arcontria.util.EFConstants;

public class MapObjectHitbox extends Box {
	
	private EFMapObject mapObj;
	private float y;
	
	/**
	 * Creates a new MapObjectHitbox.
	 * @param level The GameLevel
	 * @param mapObj The MapObject
	 */
	
	public MapObjectHitbox(GameLevel level, EFMapObject mapObj) {
		this.mapObj = mapObj;
		
		//TODO: Create body
		TextureMapObject tmo = mapObj.getTextureMapObject();
		float x = tmo.getX() / EFConstants.PPM;
		this.y = tmo.getY() / EFConstants.PPM;
		float w = tmo.getTextureRegion().getRegionWidth() / EFConstants.PPM;
		float h = tmo.getTextureRegion().getRegionHeight() / EFConstants.PPM;
		
		B2DUtil.createBody(level.getWorld(), x + w / 2f, y + h / 2f, w, h,
				BodyType.StaticBody, PhysicsCategory.MAPOBJECT_HITBOX, this);
	}
	
	public EFMapObject getMapObject() {
		return mapObj;
	}
	
	/**
	 * Gets the y-location of the hitbox (in world units).
	 */
	
	public float getY() {
		return y;
	}

}
