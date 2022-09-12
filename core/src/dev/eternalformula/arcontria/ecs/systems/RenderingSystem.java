package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.TextureComponent;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.Strings;

public class RenderingSystem extends IteratingSystem {

	private SpriteBatch gameBatch;
	private ComponentMapper<PositionComponent> posMapper;
	private ComponentMapper<TextureComponent> texMapper;
	
	public RenderingSystem(SpriteBatch gameBatch) {
		super(Family.all(PositionComponent.class, TextureComponent.class).get());
		this.gameBatch = gameBatch;
		
		this.posMapper = ComponentMapper.getFor(PositionComponent.class);
		this.texMapper = ComponentMapper.getFor(TextureComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TextureComponent tex = texMapper.get(entity);
		PositionComponent pos = posMapper.get(entity);
		
		if (tex.getTextureRegion() == null || tex.isHidden) {
			return;
		}
		
		float width = tex.getWidth();
		float height = tex.getHeight();
		
		float originX = width / 2f;
		float originY = height / 2f;
		
		gameBatch.setProjectionMatrix(ArcontriaGame.GAME.getSceneManager().getGameCamera().combined);
		
		gameBatch.draw(tex.getTextureRegion(), pos.getX() - originX,
				pos.getY() - originY, width, height);
	}

}
