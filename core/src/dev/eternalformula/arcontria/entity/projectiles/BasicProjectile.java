package dev.eternalformula.arcontria.entity.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.ProjectileBox;
import dev.eternalformula.arcontria.util.EFConstants;

public class BasicProjectile extends Projectile {

	protected TextureRegion texture;
	protected ProjectileBox projBox;

	private Vector2 velocity;
	
	public BasicProjectile(GameLevel level) {
		super(level, 0f);
		this.velocity = new Vector2(0f, 0f);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		if (texture != null) {
			float w = texture.getRegionWidth() / EFConstants.PPM;
			float h = texture.getRegionHeight() / EFConstants.PPM;
			batch.draw(texture, location.x, location.y, w, h);
		}
	}

	@Override
	public void destroyBodies(World world) {
		if (projBox != null) {
			world.destroyBody(projBox.getBody());
		}
		
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (velocity.x != 0 || velocity.y != 0) {
			location.x += velocity.x * delta;
			location.y += velocity.y * delta;
			projBox.getBody().setTransform(location.x + width / 2f, location.y + height / 2f, 0);
		}
	}
	
	public void setTexture(TextureRegion texture) {
		this.texture = texture;
		this.width = texture.getRegionWidth() / EFConstants.PPM;
		this.height = texture.getRegionHeight() / EFConstants.PPM;
		
		this.projBox = new ProjectileBox(level, this);
	}
	
	public void applyVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
}
