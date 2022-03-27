package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class PlayerHitbox extends Box {
	
	private Player player;
	
	public PlayerHitbox(GameLevel level, Player player) {
		this.player = player;
		this.body = B2DUtil.createBody(level.getWorld(), player.getLocation().x + 0.5f,
				player.getLocation().y + 1f, 1, 2, BodyType.DynamicBody, PhysicsCategory.PLAYER_HITBOX, this);
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
