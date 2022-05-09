package dev.eternalformula.arcontria.level.maps;

import java.util.ArrayList;
import java.util.List;

import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shibabandit.gdx_navmesh.coll.CollUtil;

import dev.eternalformula.arcontria.entity.MapEntityBuilder;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;
import dev.eternalformula.arcontria.util.EFConstants;

/**
 * An extension of the TmxMapLoader that allows Tiled's Template Objects to be loaded.
 * @author EternalFormula
 */

public class TemplateTmxMapLoader extends TmxMapLoader {

	FileHandle tmxFile;
	private GameLevel level;
	
	private Array<org.locationtech.jts.geom.Polygon> polygons;
	
	public TemplateTmxMapLoader(GameLevel level) {
		super();
		this.level = level;
		this.polygons = new Array<>();
	}
	
    @Override
    protected TiledMap loadTiledMap (FileHandle tmxFile, TmxMapLoader.Parameters parameter, ImageResolver imageResolver){
          this.tmxFile = tmxFile;
          return super.loadTiledMap(tmxFile,parameter,imageResolver);
    }
    
	@Override
	protected void loadObject (TiledMap map, MapObjects objects, XmlReader.Element element, float heightInPixels) {
		if (element.getName().equals("object")) {

			if(!element.hasAttribute("template")) {
				super.loadObject(map,objects,element,heightInPixels);
				return;
			}
			
			FileHandle template = getRelativeFileHandle(tmxFile,element.getAttribute("template"));
			Element el = xml.parse(template);
			for (Element obj : el.getChildrenByName("object")) {
				
				MapProperties objProps = getMapProperties(obj);
				if (objProps != null) {
					int id = obj.getInt("gid") - el.getChildByName("tileset").getInt("firstgid");
					FileHandle tilesetFile = getRelativeFileHandle(template, el.getChildByName("tileset").get("source"));
					
					Element tileset = xml.parse(tilesetFile);
					
					for (Element tileElement : tileset.getChildrenByName("tile")) {
						if (tileElement.getInt("id") == id) {
							String source = tileElement.getChild(0).get("source");
							FileHandle img = getRelativeFileHandle(tilesetFile, source);
							
							Texture tex = new Texture(Gdx.files.internal(img.path()));
							TextureRegion region = new TextureRegion(tex);
							float x = element.getFloat("x") / EFConstants.PPM;
							float y = (map.getProperties().get("height", int.class) * 16 
									- element.getFloat("y")) / EFConstants.PPM;
							
							float width = obj.getFloat("width") / EFConstants.PPM;
							float height = obj.getFloat("height") / EFConstants.PPM;
							
							if (objProps.containsKey("entityId")) {
								int entityId = objProps.get("entityId", int.class); // TODO: fix
								level.addEntity(MapEntityBuilder.createEntity(level, entityId, region, x, y,
										width, height, objProps));
							}
							else {
								// Creating polygon for navmesh
								List<PolygonPoint> points = new ArrayList<PolygonPoint>();
								float colliderX = objProps.get("colliderX", float.class);
								float colliderY = objProps.get("colliderY", float.class);
								float colliderW = objProps.get("colliderWidth", float.class);
								float colliderH = objProps.get("colliderHeight", float.class);
								
								points.add(new PolygonPoint(x + colliderX, y + colliderY));
								points.add(new PolygonPoint(x + colliderX + colliderW, y + colliderY));
								points.add(new PolygonPoint(x + colliderX + colliderW, y + colliderY + colliderH));
								points.add(new PolygonPoint(x + colliderX, y + colliderY + colliderH));
								
								// Add polygon to navmesh.
								polygons.add(CollUtil.toJtsPoly(new Polygon(points)));
								// Add TextureMapObject to objects
								TextureMapObject tmo = new TextureMapObject(region);
								tmo.setX(x * EFConstants.PPM);
								tmo.setY(y * EFConstants.PPM);
								objects.add(tmo);
								
								B2DUtil.createBody(level.getWorld(),
										x + colliderX + colliderW / 2f,
										y + colliderY + colliderH / 2f,
										colliderW, colliderH, BodyType.StaticBody,
										PhysicsCategory.MAPOBJECT_COLLIDER, null);
							}
							
						}
						continue;
					}
					return;
				}
				/*
				else {
					super.loadObject(map, objects, element, heightInPixels);
					
					// Load Object Properties and pass them to modified version of loadObject().
				}*/
				
				//return;
			}
		}
	}
	
	/**
	 * Gets the mapproperties of a given element.
	 * @param e The element to be used.
	 * @return The map properties of a given element, null<br>
	 *  if the element has no properties.
	 */
	
	private MapProperties getMapProperties(Element e) {
		
		if (e != null && e.getChildByName("properties") != null) {
			MapProperties props = new MapProperties();
			
			for (Element property : e.getChildByName("properties").getChildrenByName("property")) {
				String name = property.getAttribute("name");
				String value = property.getAttribute("value");
				String type = property.getAttribute("type");
				Object castValue = castProperty(name, value, type);
				props.put(name, castValue);
			}
			return props;
		}
		return null;
	}
	
	public Array<org.locationtech.jts.geom.Polygon> getNavmeshPolygons() {
		return polygons;
	}
}
