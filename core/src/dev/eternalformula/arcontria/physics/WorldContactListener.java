package dev.eternalformula.arcontria.physics;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import dev.eternalformula.arcontria.combat.DamageSource;
import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.physics.boxes.Box;
import dev.eternalformula.arcontria.physics.boxes.EntityHitbox;
import dev.eternalformula.arcontria.physics.boxes.PlayerAttackBox;
import dev.eternalformula.arcontria.util.EFDebug;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		EFDebug.debug("Contact Detected!");
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if (fa == null || fb == null) {
			return;
		}
		
		if (fa.getUserData() == null || fb.getUserData() == null) {
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
			
		}
	}

}
