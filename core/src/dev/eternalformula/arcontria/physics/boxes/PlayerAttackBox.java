package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class PlayerAttackBox extends Box {
	
	private Player player;
	
	public PlayerAttackBox(GameLevel level, Player player) {
		this.player = player;
		this.body = B2DUtil.createBody(level.getWorld(), player.getLocation().x + 0.5f,
				player.getLocation().y, 0, 0, PhysicsCategory.PLAYER_ATTACKBOX);
		this.id = UUID.randomUUID();
		body.setUserData(this);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public UUID getId() {
		return id;
	}

}
