package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.ShadowComponent;
import dev.eternalformula.arcontria.ecs.components.TextureComponent;

public class RenderingSystem extends IteratingSystem {

	private SpriteBatch gameBatch;
	private ComponentMapper<PositionComponent> posMapper;
	private ComponentMapper<TextureComponent> texMapper;
	
	private Color shadowColor;
	
	public RenderingSystem(SpriteBatch gameBatch) {
		super(Family.all(PositionComponent.class, TextureComponent.class).get());
		this.gameBatch = gameBatch;
		
		this.posMapper = ComponentMapper.getFor(PositionComponent.class);
		this.texMapper = ComponentMapper.getFor(TextureComponent.class);
		
		this.shadowColor = new Color(0f, 0f, 0f, 0f);
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
		
		if (entity.getComponent(ShadowComponent.class) != null) {
			ShadowComponent shadComp = entity.getComponent(ShadowComponent.class);
			gameBatch.end();
			
			ShapeRenderer shapeRend = ArcontriaGame.GAME.getSceneManager().getShapeRenderer();
			shapeRend.setProjectionMatrix(ArcontriaGame.GAME.getSceneManager().getGameCamera().combined);
			shapeRend.setAutoShapeType(true);
			shapeRend.begin();
			shapeRend.set(ShapeType.Filled);
			shadowColor.a = shadComp.alpha;
			
			shapeRend.setColor(shadowColor);
			shapeRend.circle(pos.getX() + shadComp.pos.x, pos.getY() + shadComp.pos.y, shadComp.radius, 32);
			shapeRend.end();
			shapeRend.setColor(Color.WHITE);
			gameBatch.begin();
		}
		
		gameBatch.setProjectionMatrix(ArcontriaGame.GAME.getSceneManager().getGameCamera().combined);
		
		gameBatch.draw(tex.getTextureRegion(), pos.getX() - originX,
				pos.getY() - originY, width, height);
	}

}
