package dev.eternalformula.arcontria.level.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.util.EFConstants;

public class EFMapRenderer {
	
	private OrthogonalTiledMapRendererBleeding mapRenderer;
	private EFTiledMap tiledMap;
	
	public EFMapRenderer() {
		this.mapRenderer = new OrthogonalTiledMapRendererBleeding(null, 1 / EFConstants.PPM);
	}
	
	public void setTiledMap(EFTiledMap tiledMap) {
		this.tiledMap = tiledMap;
		mapRenderer.setMap(tiledMap.getMap());
		
	}
	
	public void draw(SpriteBatch gameBatch, float delta) {
		
		if (mapRenderer.getMap() != null) {
			mapRenderer.setView(ArcontriaGame.GAME.getSceneManager().getGameCamera());
			mapRenderer.render();
			tiledMap.drawMapObjects(gameBatch, delta);
		}
		
	}

}
