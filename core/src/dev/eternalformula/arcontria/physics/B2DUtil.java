package dev.eternalformula.arcontria.physics;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.Strings;

/**
 * A class of static Box2D utility methods.
 * @author EternalFormula
 */

public class B2DUtil {
	
	public static Body createBodyForEntity(World world, Entity e, BodyType bodyType) {
		Body body;
		BodyDef def = new BodyDef();
		def.type = bodyType;
		def.position.set(e.getLocation());
		def.fixedRotation = true;
		body = world.createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(e.getWidth() / 2f, e.getHeight() / 2f);
		body.createFixture(shape, 1.0f);
		shape.dispose();
		return body;
	}
	
	/**
	 * Creates Box2D body for entity with bodyType set to static.
	 */
	
	public static Body createBodyForEntity(World world, Entity e) {
		return createBodyForEntity(world, e, BodyType.StaticBody);
	}
	
	public static ChainShape createPolyline(PolylineMapObject mapObj) {
		float[] vertices = mapObj.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		
		for (int i = 0; i < worldVertices.length; i++) {
			worldVertices[i] = new Vector2(vertices[i * 2] / EFConstants.PPM,
					vertices[i * 2 + 1] / EFConstants.PPM);
		}
		
		ChainShape cs = new ChainShape();
		cs.createChain(worldVertices);
		return cs;
	}
	
	public static void decomposePolygon(World world, PolygonMapObject obj) {
		
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(new Vector2(obj.getPolygon().getOriginX() / EFConstants.PPM,
				obj.getPolygon().getOriginY() / EFConstants.PPM));
		def.fixedRotation = true;
		System.out.println("Polygon Pos: " + Strings.vec2(def.position));
		body = world.createBody(def);
		
		int numFixtures = Math.round(obj.getPolygon().getVertices().length / 8f);
		float[][] pointArrays = new float[numFixtures][];
		
		int remainder = obj.getPolygon().getVertices().length % 8;
		for (int i = 0; i < pointArrays.length; i++) {
			pointArrays[i] = new float[(i == numFixtures - 1 ? remainder : 8)];
		}
		
		int q = 0;
		float[] verticesToAdd = obj.getPolygon().getVertices();
		for (int i = 0; i < pointArrays.length; i++) {
			for (int j = 0; j < pointArrays[i].length; j++) {
				float vertex = verticesToAdd[q] / EFConstants.PPM;
				System.out.println("V " + q + ": " + vertex);
				pointArrays[i][j] = vertex;
				q++;
			}
		}
		
		for (int i = 0; i < pointArrays.length; i++) {
			PolygonShape ps = new PolygonShape();
			System.out.println("Length: " + pointArrays[i].length);
			ps.set(pointArrays[i]);
			body.createFixture(ps, 1.0f);
			ps.dispose();
		}
	}
	
	public static void createObjectBody(World world, TextureMapObject mapObj) {
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(new Vector2(mapObj.getOriginX() / EFConstants.PPM,
				mapObj.getOriginY() / EFConstants.PPM));
		def.fixedRotation = true;
		body = world.createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(mapObj.getTextureRegion().getRegionWidth() / EFConstants.PPM / 2f,
				mapObj.getTextureRegion().getRegionHeight() / EFConstants.PPM / 2f);
		body.createFixture(shape, 1.0f);
		shape.dispose();
	}

}
