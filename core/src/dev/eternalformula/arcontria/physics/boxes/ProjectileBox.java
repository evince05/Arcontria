package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import dev.eternalformula.arcontria.entity.projectiles.Projectile;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class ProjectileBox extends Box {
	
	private Projectile projectile;
	
	public ProjectileBox(GameLevel level, Projectile projectile) {
		this.projectile = projectile;
		this.id = UUID.randomUUID();
		
		this.body = B2DUtil.createBody(level.getWorld(), projectile.getLocation().x + 0.5f,
				projectile.getLocation().y, projectile.getWidth(), projectile.getHeight(),
				BodyType.DynamicBody, PhysicsCategory.PROJECTILE_COLLIDER, this);
		body.setUserData(this);
	}
	
	public Projectile getProjectile() {
		return projectile;
	}

}
