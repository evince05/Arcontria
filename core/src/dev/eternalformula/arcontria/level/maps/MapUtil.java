package dev.eternalformula.arcontria.level.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.util.EFConstants;

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
				System.out.println("Continue");
				continue;
			}
			
			Body body;
			BodyDef bdef = new BodyDef();
			body = world.createBody(bdef);
			body.createFixture(shape, 1.0f);
			shape.dispose();
		}
	}
	
	private static PolygonShape createPolygon(PolygonMapObject object) {
		System.out.println("Creating polygon.");
		float[] vertices = object.getPolygon().getTransformedVertices();
		System.out.println("Found " + vertices.length + " vertices");
		
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] /= EFConstants.PPM;
		}
		
		PolygonShape ps = new PolygonShape();
		ps.set(vertices);
		return ps;
	}
	
	private static PolygonShape createRectangle(RectangleMapObject object) {
		PolygonShape shape = new PolygonShape();
		Rectangle rect = object.getRectangle();
		shape.setAsBox(rect.width / EFConstants.PPM / 2f, rect.height / EFConstants.PPM / 2f,
				new Vector2(rect.x / EFConstants.PPM + rect.width / EFConstants.PPM / 2f,
				rect.y / EFConstants.PPM + rect.height / EFConstants.PPM / 2f), 0);
		return shape;
		
		
	}

}
