package dev.eternalformula.arcontria.entity.hostile;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.level.GameLevel;

public abstract class HostileEntity extends LivingEntity {
	
	protected LivingEntity target;
	//protected Path pathToTarget;
	
	
	// idea: entitybuilder class?
	public HostileEntity(GameLevel level, LivingEntity target) {
		super(level);
		this.target = target;
	}
	
	public LivingEntity getTarget() {
		return target;
	}
	
	private void updatePath() {
		// do stuff
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		updatePath();
	}

}
