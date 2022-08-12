package dev.eternalformula.arcontria.level.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.util.EFConstants;

public class EFMapRenderer {
	
	private OrthogonalTiledMapRendererBleeding mapRenderer;
	private EFTiledMap tiledMap;
	
	private float screenAlpha;
	private boolean shouldUseAlpha;
	
	public EFMapRenderer() {
		this.mapRenderer = new OrthogonalTiledMapRendererBleeding(null, 1 / EFConstants.PPM);
		this.screenAlpha = 1f;
	}
	
	public void setTiledMap(EFTiledMap tiledMap) {
		this.tiledMap = tiledMap;
		mapRenderer.setMap(tiledMap.getMap());
		
	}
	
	public void draw(SpriteBatch gameBatch, float delta) {
		if (mapRenderer.getMap() != null) {
			
			if (shouldUseAlpha) {
				// Uses screenAlpha for every field for a smooth transition.
				mapRenderer.getBatch().setColor(screenAlpha, screenAlpha, screenAlpha, screenAlpha);
			}
			else {
				mapRenderer.getBatch().setColor(Color.WHITE);
			}
			
			mapRenderer.setView(ArcontriaGame.GAME.getSceneManager().getGameCamera());
			mapRenderer.render();
			tiledMap.drawMapObjects(gameBatch, delta);
		}	
	}
	
	public void setScreenAlpha(float screenAlpha) {
		this.screenAlpha = screenAlpha;
	}
	
	public void toggleScreenAlpha(boolean shouldUseAlpha) {
		this.shouldUseAlpha = shouldUseAlpha;
	}
}
