package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class StateComponent implements Component {
	
	public static final ComponentMapper<StateComponent> Map =
			ComponentMapper.getFor(StateComponent.class);
	
	public enum State {
		DEFAULT(0, "default"),
		IDLE(1, "idle"),
		MOVING(2, "moving"),
		ATTACKING_MELEE(3, "attackingMelee"),
		ATTACKING_RANGED(4, "attackingRanged"),
		MINING(5, "mining"),
		OTHER(6, "");
		
		public final int id;
		public String animPrefix;
		
		State(int id, String animPrefix) {
			this.id = id;
			this.animPrefix = animPrefix;	
		}
	}
	
	private State currentState;
	
	/**
	 * The time that the entity has spent in the current state.
	 */
	
	public float stateTime;
	
	public boolean isLooping = false;
	
	public StateComponent() {
		this.currentState = State.IDLE;
	}
	
	public void setState(State state) {
		this.currentState = state;
		this.stateTime = 0f;
	}
	
	public void setState(State state, boolean looping) {
		this.currentState = state;
		this.stateTime = 0f;
		this.isLooping = looping;
	}
	
	public State getState() {
		return currentState;
	}
	
	public float getStateTime() {
		return stateTime;
	}
	
}
