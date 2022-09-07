package dev.eternalformula.arcontria.physics;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.combat.DamageSource;
import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.entity.misc.MineRock;
import dev.eternalformula.arcontria.physics.boxes.Box;
import dev.eternalformula.arcontria.physics.boxes.BreakableObjHitbox;
import dev.eternalformula.arcontria.physics.boxes.EntityHitbox;
import dev.eternalformula.arcontria.physics.boxes.MapObjectHitbox;
import dev.eternalformula.arcontria.physics.boxes.PlayerAttackBox;
import dev.eternalformula.arcontria.physics.boxes.PlayerHitbox;
import dev.eternalformula.arcontria.physics.boxes.ProjectileBox;
import dev.eternalformula.arcontria.scenes.GameScene;
import dev.eternalformula.arcontria.util.EFDebug;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if (fa == null || fb == null) {
			return;
		}
		
		if (fa.getUserData() == null || fb.getUserData() == null) {
			EFDebug.debug("One of two userdata objects are null. Returning");
			return;
		}
		
		Box boxA = (Box) fa.getUserData();
		Box boxB = (Box) fb.getUserData();
		
		handleBoxContact(contact, boxA, boxB);
	}

	@Override
	public void endContact(Contact contact) {
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if (fa == null || fb == null) {
			return;
		}
		
		if (fa.getUserData() == null || fb.getUserData() == null) {
			EFDebug.debug("One of two userdata objects are null. Returning");
			return;
		}
		
		Box boxA = (Box) fa.getUserData();
		Box boxB = (Box) fb.getUserData();
		
		handleBoxEndContact(contact, boxA, boxB);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
	public void handleBoxContact(Contact contact, Box boxA, Box boxB) {
		if ((boxA instanceof PlayerAttackBox || boxB instanceof PlayerAttackBox) &&
				(boxA instanceof EntityHitbox || boxB instanceof EntityHitbox)) {
			
			// Gets the boxes.
			PlayerAttackBox pAttackBox = (PlayerAttackBox) (boxA instanceof PlayerAttackBox ? boxA : boxB);
			EntityHitbox eHitbox = (EntityHitbox) (boxA instanceof EntityHitbox ? boxA : boxB);
			
			float crit = ThreadLocalRandom.current().nextFloat();
			((LivingEntity) eHitbox.getEntity()).damage(new DamageSource(pAttackBox.getPlayer()), 4, (crit <= 0.2));
			return;
		}
		
		if ((boxA instanceof PlayerHitbox || boxB instanceof PlayerHitbox) &&
				(boxA instanceof MapObjectHitbox || boxB instanceof MapObjectHitbox)) {
			
			// Gets the boxes.
			PlayerHitbox pHitbox = (PlayerHitbox) (boxA instanceof PlayerHitbox ? boxA : boxB);
			MapObjectHitbox objHitbox = (MapObjectHitbox) (boxA instanceof MapObjectHitbox ? boxA : boxB);
			
			float playerY = pHitbox.getPlayer().getLocation().y;
			float objY = objHitbox.getY();
			
			if (playerY > objY) {
				objHitbox.getMapObject().beginFadeAnimation();
				//ArcontriaGame.GAME.getScene().getLevel().getPlayer().setBehindMapObject(true);
			}
			return;
		}
		
		if ((boxA instanceof ProjectileBox || boxB instanceof ProjectileBox) && 
				(boxA instanceof EntityHitbox || boxB instanceof EntityHitbox)) {
			
			ProjectileBox pBox = (ProjectileBox) (boxA instanceof ProjectileBox ? boxA : boxB);
			EntityHitbox eBox = (EntityHitbox) (boxA instanceof EntityHitbox ? boxA : boxB);
			
			((LivingEntity) eBox.getEntity()).damage(new DamageSource(pBox.getProjectile()), 100, false);
			return;
					
		}
		
		if ((boxA instanceof ProjectileBox || boxB instanceof ProjectileBox) &&
				(boxA instanceof BreakableObjHitbox || boxB instanceof BreakableObjHitbox)) {
			
			BreakableObjHitbox box = (BreakableObjHitbox)
					(boxA instanceof BreakableObjHitbox ? boxA : boxB);
			
			if (box.getEntity() instanceof MineRock) {
				((MineRock) box.getEntity()).destroy();
			}
			
			// Destroys the projectile
			ProjectileBox projBox = (ProjectileBox)
					(boxA instanceof ProjectileBox ? boxA : boxB);
			projBox.getProjectile().destroy();
			
			return;
		}
	}
	
	public void handleBoxEndContact(Contact contact, Box boxA, Box boxB) {
		if ((boxA instanceof PlayerHitbox || boxB instanceof PlayerHitbox) &&
				(boxA instanceof MapObjectHitbox || boxB instanceof MapObjectHitbox)) {
			
			// Gets the boxes.
			PlayerHitbox pHitbox = (PlayerHitbox) (boxA instanceof PlayerHitbox ? boxA : boxB);
			MapObjectHitbox objHitbox = (MapObjectHitbox) (boxA instanceof MapObjectHitbox ? boxA : boxB);
			
			float playerY = pHitbox.getPlayer().getLocation().y;
			float objY = objHitbox.getY();
			
			if (playerY > objY) {
				objHitbox.getMapObject().beginReappearAnimation();
			}
			return;
		}
		
		// Note: Must destroy bodies on endContact
		if ((boxA instanceof PlayerAttackBox || boxB instanceof PlayerAttackBox) && 
				(boxA instanceof BreakableObjHitbox || boxB instanceof BreakableObjHitbox)) {
			
			// Gets the hitbox
			BreakableObjHitbox box = (BreakableObjHitbox) 
					(boxA instanceof BreakableObjHitbox ? boxA : boxB);
			
			if (box.getEntity() instanceof MineRock) {
				((MineRock) box.getEntity()).destroy();
			}
			return;
			
		}
	}

}
