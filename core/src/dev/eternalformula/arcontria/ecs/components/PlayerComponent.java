package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
	
	private String name;
	private float balance;

	public PlayerComponent() {
		this.name = "Player";
		this.balance = 0f;
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public float getBalance() {
		return balance;
	}
	
	public void setBalance(float balance) {
		this.balance = balance;
	}
}
