package dev.eternalformula.arcontria.level.maps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.pathfinding.PathNode;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.Strings;

public class Map {
	
	private GameLevel level;
	private TiledMap map;
	
	private float mapWidth;
	private float mapHeight;
	
	private ShapeRenderer debugRenderer;
	
	/**
	 * A 2D Array of all path nodes (tiles).
	 */
	
	private PathNode[][] nodeGrid;
	
	public Map(GameLevel level, TiledMap map) {
		this.level = level;
		this.map = map;
		
		this.debugRenderer = new ShapeRenderer();
		
		this.mapWidth = Float.valueOf(map.getProperties().get("width", int.class));
		this.mapHeight = Float.valueOf(map.getProperties().get("height", int.class));
		Map.zSort(map);
		
		this.nodeGrid = new PathNode[(int) mapHeight][(int) mapWidth];
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				nodeGrid[row][col] = new PathNode(true, new Vector2(col, row), col, row);
			}
		}
		
		MapUtil.parseTiledObjectLayer(level.getWorld(), map.getLayers().get("Collisions").getObjects());
		
		// Create Lights
		createLights();
	}
	
	/**
	 * Sets tiles as "blocked" if they have an object on them.
	 */
	
	private void setBlockedTiles() {
		for (MapLayer layer : map.getLayers()) {
			if (layer.getObjects().getCount() > 0) {
				// Layer has objects.
				
				Array<TextureMapObject> rectObjects = layer.getObjects().getByType(TextureMapObject.class);
				//System.out.println("size: " + rectObjects.size);
				
				int c = 0;
				for (TextureMapObject tmo : rectObjects) {
					TextureRegion tex = tmo.getTextureRegion();
					
					float objX = tmo.getX() / EFConstants.PPM;
					float objY = tmo.getY() / EFConstants.PPM;
					float objW = tex.getRegionWidth() / EFConstants.PPM;
					float objH = tex.getRegionHeight() / EFConstants.PPM;
					System.out.println("Method Entry... [" + objX +", " + objY + ", " + objW + ", " + objH + "]");
					// Normal code executes if normal conditions are met.
					if (objX >= 0 && Math.floor(objX + objW) <= mapWidth && objY >= 0 && Math.floor(objY + objH) <= mapHeight) {
						Rectangle r = new Rectangle(objX, objY, objW, objH);
						
						PathNode minNode = getNode((int) r.x, (int) r.y);
						PathNode maxNode = getNode((int) Math.floor(r.getX() + r.getWidth()),
								(int) Math.floor(r.getY() + r.getHeight()));
						
						// TODO: Add checks for objects that go past the outside of the map.
						
						int width = (int) (maxNode.getPosition().x - minNode.getPosition().x);
						int height = (int) (maxNode.getPosition().y - minNode.getPosition().y);
						int x = (int) Math.floor(minNode.getPosition().x);
						int y = (int) Math.floor(minNode.getPosition().y);
						
						// Set all tiles in range to blocked.
						for (int w = x; w < x + width; w++) {
							for (int h = y; h < y + height; h++) {
								if (nodeGrid[h][w].isWalkable()) {
									c++;
									nodeGrid[h][w].setWalkable(false);
								}	
							}
						}
					}
					else {
						System.out.println("Found exception object!");
						
						// TODO: Optimize this code lmao
						if (objX < 0f) {
							System.out.println("left");
							// Gets the number of tiles the object covers on the map from the left side.
							int tilesTouchingX = (int) Math.ceil(Math.floor(objX) + objW);
							System.out.println(Strings.vec2((float) Math.floor(objX), objY));
							System.out.println("TTX: " + tilesTouchingX);
							for (int row = (int) objY; row < objY + objH; row++) {
								for (int col = 0; col < tilesTouchingX; col++) {
									if (row > 0 && row <= mapHeight) {
										if (nodeGrid[row][col].isWalkable()) {
											c++;
											nodeGrid[row][col].setWalkable(false);
										}
										
									}
								}
							}
						}
						else if (objX + objW > mapWidth) {
							// Gets the number of tiles the object covers on the map from the right side.
							System.out.println("right");
							int tilesTouchingX = (int) (objX + objW - mapWidth);
							System.out.println(Strings.vec2(objX, objY));
							System.out.println("TTX: " + tilesTouchingX);
							for (int row = (int) objY; row < objY + objH; row++) {
								for (int col = (int) mapWidth - 1; col >= mapWidth - tilesTouchingX; col--) {
									if (row > 0 && row <= mapHeight) {
										if (nodeGrid[row][col].isWalkable()) {
											c++;
											nodeGrid[row][col].setWalkable(false);
										}
										
									}
								}
							}
						}
							
						if (objY < 0f){
							System.out.println("heading up");
							int tilesTouchingY = (int) (objY + objH);
							System.out.println(Strings.vec2(objX, objY));
							System.out.println("TTY: " + tilesTouchingY);
							for (int row = 0; row < tilesTouchingY; row++) {
								for (int col = (int) objX; col < objX + objW; col++) {
									if (col > 0 && col <= mapWidth) {
										if (nodeGrid[row][col].isWalkable()) {
											c++;
											nodeGrid[row][col].setWalkable(false);
										}
									}
								}
							}
						}
						else if (objY + objH > mapHeight) {
							System.out.println("heading down");
							int tilesTouchingY = (int) (objY + objH - mapHeight);
							System.out.println(Strings.vec2(objX, objY));
							System.out.println("TTY: " + tilesTouchingY);
							for (int row = (int) mapHeight - 1; row >= mapHeight - tilesTouchingY; row--) {
								for (int col = (int) objX; col < objX + objW; col++) {
									if (nodeGrid[row][col].isWalkable()) {
										c++;
										nodeGrid[row][col].setWalkable(false);
									}
								}
							}
						}
					}
					
					// TODO: Add checks and cases for texturemapobjects that exceed the world bounds.
					/* Debug
					System.out.println("Min: " + Strings.vec2(minNode.getPosition()) + ", Max: " + Strings.vec2(maxNode.getPosition()));
					System.out.println("Width: " + ((maxNode.getPosition().x - minNode.getPosition().x) / 16f) + " tiles");
					System.out.println("Height: " + ((maxNode.getPosition().y - minNode.getPosition().y) / 16f) + " tiles");
					*/
				}
				System.out.println("[DEBUG] Blocked " + c + " tiles!");
			}
		}
	}
	
	private void createLights() {
		for (MapLayer layer : map.getLayers()) {
			if (layer.getObjects().getCount() > 0) {
				//System.out.println("Obj Count: " + layer.getObjects().getCount());
				for (int i = 0; i < layer.getObjects().getCount(); i++) {
					//System.out.println("Class: " + layer.getObjects().get(i).getClass());
				}
			}
			else {
				continue;
			}
		}
	}
	
	public void draw(float delta) {
		debugRenderer.setProjectionMatrix(level.getScene().getViewport().getCamera().combined);
		debugRenderer.setAutoShapeType(true);
		debugRenderer.setColor(Color.RED);
		debugRenderer.begin(ShapeType.Line);
		
		for (int row = 0; row < mapHeight; row++) {
			for(int col = 0; col < mapWidth; col++) {
				if (!nodeGrid[row][col].isWalkable()) {
					debugRenderer.rect(col, row, 1f, 1f);
				}
			}
		}
		debugRenderer.end();
	}
	
	public PathNode getNode(int x, int y) {
		return nodeGrid[y][x];
	}
	
	public GameLevel getLevel() {
		return level;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public float getWidth() {
		return mapWidth;
	}
	
	public float getHeight() {
		return mapHeight;
	}
	
	public static void zSort(TiledMap map) {
		
		for (MapLayer layer : map.getLayers()) {
			if (layer.getObjects().getCount() > 0) {
				
				Array<TextureMapObject> texObjs = layer.getObjects().getByType(TextureMapObject.class);
				Sort.instance().sort(texObjs, new MapComparator());
				
				List<MapObject> objectsToRemove = new ArrayList<>();
				for (int i = 0; i < texObjs.size; i++) {
					objectsToRemove.add(texObjs.get(i));
				}
				for (MapObject mapObj : objectsToRemove) {
					layer.getObjects().remove(mapObj);
				}
				
				for (int i = 0; i < texObjs.size; i++) {
					layer.getObjects().add(texObjs.get(i));
				}
			}
		}
	}
	
	public static class MapComparator implements Comparator<TextureMapObject> {

		@Override
		public int compare(TextureMapObject obj1, TextureMapObject obj2) {
			return Integer.compare(Math.round(obj2.getY()), Math.round(obj1.getY()));
		}
		
	}

}
