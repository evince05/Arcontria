package dev.eternalformula.arcontria.level.maps;

import java.util.ArrayList;
import java.util.List;

import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;

public class MapUtil {
	
	public static void parseTiledObjectLayer(World world, MapObjects objects) {
		for (MapObject mapObj : objects) {
			Shape shape;
			if (mapObj instanceof PolygonMapObject) {
				
				PolygonMapObject polygon = (PolygonMapObject) mapObj;
				if (polygon.getPolygon().getVertices().length > 8) {
					B2DUtil.decomposePolygon(world, polygon);
					return;
				}
				shape = createPolygon((PolygonMapObject) mapObj);
			}
			else if (mapObj instanceof RectangleMapObject) {
				shape = createRectangle((RectangleMapObject) mapObj);
			}
			else {
				continue;
			}
			
			Body body;
			BodyDef bdef = new BodyDef();
			body = world.createBody(bdef);
			
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.density = 1.0f;
			
			// It is assumed that each of these are mapbounds
			fdef.filter.categoryBits = PhysicsConstants.BIT_MAP_BOUNDS;
			
			body.createFixture(fdef);
			shape.dispose();
		}
	}
	
	private static PolygonShape createPolygon(PolygonMapObject object) {
		float[] vertices = object.getPolygon().getTransformedVertices();
		
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] /= EFConstants.PPM;
		}
		
		PolygonShape ps = new PolygonShape();
		ps.set(vertices);
		return ps;
	}
	
	private static PolygonShape createRectangle(RectangleMapObject object) {
		EFDebug.debug("Actually creating shape (MapUtil.java:72)");
		PolygonShape shape = new PolygonShape();
		Rectangle rect = object.getRectangle();
		shape.setAsBox(rect.width / EFConstants.PPM / 2f, rect.height / EFConstants.PPM / 2f,
				new Vector2(rect.x / EFConstants.PPM + rect.width / EFConstants.PPM / 2f,
				rect.y / EFConstants.PPM + rect.height / EFConstants.PPM / 2f), 0);
		return shape;
		
		
	}
	
	/**
	 * Parses and creates a polygon array from a MapObject layer.
	 * @param layer The layer to be parsed (must be a MapObject layer)
	 * @return An array containing all rectangle bounds of the MapObject layer.
	 */
	
	public static Array<Polygon> createPolygonArrayFromMap(TiledMap map) {
		Array<Polygon> polygons = new Array<Polygon>();
		
		for (MapLayer layer : map.getLayers()) {
			
			for (MapObject mapObj : layer.getObjects()) {
				if (mapObj instanceof RectangleMapObject) {
					RectangleMapObject rectObj = (RectangleMapObject) mapObj;
					Rectangle rect = rectObj.getRectangle();
					
					List<PolygonPoint> points = new ArrayList<PolygonPoint>();
					float x = rect.x / EFConstants.PPM; 
					float y = rect.y / EFConstants.PPM;
					float width = rect.width / EFConstants.PPM;
					float height = rect.height / EFConstants.PPM;
					
					points.add(new PolygonPoint(x, y));
					points.add(new PolygonPoint(x + width, y));
					points.add(new PolygonPoint(x, y + height));
					points.add(new PolygonPoint(x + width, y + height));
					
					Polygon poly = new Polygon(points);
					polygons.add(poly);
				}
			}
		}
		return polygons;
	}
}
