package dev.eternalformula.arcontria.entity.hostile;

import dev.eternalformula.arcontria.entity.LivingEntity;

public abstract class HostileEntity extends LivingEntity {
	
	protected LivingEntity target;
	//protected Path pathToTarget;
	
	
	// idea: entitybuilder class?
	public HostileEntity(LivingEntity target) {
		super();
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
