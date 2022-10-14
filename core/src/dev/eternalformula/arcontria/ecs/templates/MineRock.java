package dev.eternalformula.arcontria.ecs.templates;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.TextureComponent;
import dev.eternalformula.arcontria.ecs.components.TypeComponent;
import dev.eternalformula.arcontria.ecs.components.physics.ColliderComponent;
import dev.eternalformula.arcontria.files.FileUtil;
import dev.eternalformula.arcontria.items.Material;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;
import dev.eternalformula.arcontria.scenes.GameScene;
import dev.eternalformula.arcontria.util.Assets;

public class MineRock {
	
	public static Entity createMineRock(World world, Vector2 pos, Material oreType) {
		
		Entity entity = GameScene.ENGINE.createEntity();
		
		TextureComponent texComp = GameScene.ENGINE.createComponent(TextureComponent.class);
		
		// Gets the atlas
		TextureAtlas atlas = Assets.get(FileUtil.MAP_SCENERY_ATLAS, TextureAtlas.class);
		TextureRegion rockTexReg = null;
		switch (oreType) {
		case VISMADA_ORE:
			rockTexReg = atlas.findRegion("stone-ore1");
			break;
		default:
			rockTexReg = atlas.findRegion("stone");
			break;
		}
		
		texComp.setTextureRegion(rockTexReg);
		
		PositionComponent posComp = GameScene.ENGINE.createComponent(PositionComponent.class);
		posComp.position = pos;
		
		TypeComponent typeComp = GameScene.ENGINE.createComponent(TypeComponent.class);
		typeComp.type = TypeComponent.MAP_OBJECT;
		
		ColliderComponent colliderComp = GameScene.ENGINE.createComponent(ColliderComponent.class);
		Body collBody = B2DUtil.createBody(world, posComp.getX(), posComp.getY() - 7 / 16f,
				1f, 0.125f, BodyType.StaticBody, PhysicsCategory.MAPOBJECT_COLLIDER, entity);
		
		colliderComp.b2dBody = collBody;
		
		entity.add(colliderComp);
		entity.add(texComp);
		entity.add(posComp);
		entity.add(typeComp);
		
		return entity;
	}

}
