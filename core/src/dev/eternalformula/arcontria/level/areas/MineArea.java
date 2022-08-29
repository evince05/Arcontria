package dev.eternalformula.arcontria.level.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.misc.MineRock;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.objects.noise.NoiseGenerator;
import dev.eternalformula.arcontria.util.EFConstants;

public class MineArea extends MapArea {

	private int floorLevel;
	private boolean isDoorOpen;
	private Array<Entity> rocks;
	
	private float[][] baseNoiseGrid;
	private float[][] oreGrid;
	
	private Array<Polygon> spawnableAreas;
	
	public MineArea(GameLevel level, EFTiledMap map) {
		super(level, map);
		init();
	}
	
	private void init() {
		// import noisegrid from level
		rocks = new Array<Entity>();
		spawnableAreas = new Array<Polygon>();
		
		setupSpawnableAreas();
		generateRocksAndOres();
	}
	
	private void setupSpawnableAreas() {
		MapLayer layer = map.getMap().getLayers().get("SpawnAreas");
		
		layer.getObjects().getByType(PolygonMapObject.class).forEach(poly -> {
			Polygon p = poly.getPolygon();
			float[] vertices = p.getTransformedVertices();
			float[] worldVertices = new float[vertices.length];

			for (int i = 0; i < vertices.length; i++) {
				worldVertices[i] = vertices[i] / EFConstants.PPM;
			}
			Polygon newPoly = new Polygon(worldVertices);
			p.setVertices(worldVertices);
			spawnableAreas.add(newPoly);
		});
	}
	
	private void generateRocksAndOres() {
		
		baseNoiseGrid = NoiseGenerator.createNoiseGrid(MathUtils.random(1000000),
				map.getWidth(), map.getHeight());
		
		
		oreGrid = NoiseGenerator.createNoiseGrid(MathUtils.random(100000), 
				map.getWidth(), map.getHeight(), 4, 1 / 16f, 2, 1.75f, 0.5f);
		
		for (int y = baseNoiseGrid.length - 1; y >= 0; y--) {
			for (int x = 0; x < baseNoiseGrid[0].length; x++) {
				
				if (baseNoiseGrid[y][x] <= 0.55f && isSpawnable(x, y)) {
					
					int oreType = 0;
					if (baseNoiseGrid[y][x] <= 0.45f) {
						// Ore
						if (oreGrid[y][x] <= 0.3f) {
							oreType = MineRock.ORE_2;
						}
						else if (oreGrid[y][x] <= 0.4f) {
							oreType = MineRock.ORE_1;
						}
						
					}
					
					// Stone is present
					entities.add(new MineRock(new Vector2(x, y), oreType));
				}
			}
		}
	}
	
	private boolean isSpawnable(int x, int y) {
		for (Polygon p : spawnableAreas) {
			if (p.contains(x, y)) {
				return true;
			}
		}
		return false;
	}
	
	public void update(float delta) {
		super.update(delta);
		
		for (Entity e : entities) {
			e.update(delta);
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
		
		//NoiseGenerator.debugNoiseGrid(batch, baseNoiseGrid, Color.LIME, Color.SCARLET);
		//NoiseGenerator.debugOreGrid(batch, oreGrid);
		
		for (Entity e : entities) {
			e.draw(batch, delta);
		}
	}
	
	
	
}
