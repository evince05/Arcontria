package dev.eternalformula.arcontria.entity.misc;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.gfx.particles.DamageTextParticle;
import dev.eternalformula.arcontria.level.GameLevel;

/**
 * The Dummy is a non-hostile entity found in the Dojo. It allows the player to test
 * different weapons against it, showing them the damage output as a result.
 * 
 * @author EternalFormula
 */

public class Dummy extends LivingEntity {

	public Dummy(GameLevel level) {
		super(level);
	}
	
	@Override
	public void damage(float amount) {
		// TODO: Play animation that sends dummy tilting back in direction opposite to hit
		
		// No damage will occur to the dummy. Instead, a damage text particle is spawned.
		level.getParticleHandler().spawnParticle(new DamageTextParticle(location, amount));
	}
	

}
