package dev.eternalformula.arcontria.pathfinding;

import java.util.ArrayList;
import java.util.List;

import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.shibabandit.gdx_navmesh.path.NavMeshClipper;
import com.shibabandit.gdx_navmesh.path.NavMeshPathNode;
import com.shibabandit.gdx_navmesh.path.NavMeshPathRequest;
import com.shibabandit.gdx_navmesh.path.NavMeshPortal;

import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.util.EFConstants;

public class Navmesh {

	private Array<Polygon> walkables;
	
	public Navmesh(EFTiledMap map, Array<org.locationtech.jts.geom.Polygon> mapObjColliders) {
		this.walkables = new Array<Polygon>();
		
		float width = (float) map.getProperty("width", int.class);// / EFConstants.PPM;
		float height = (float) map.getProperty("height", int.class);// / EFConstants.PPM;
		
		Polygon initialWalkable = new Polygon(new PolygonPoint[] {
				new PolygonPoint(0, 0),
				new PolygonPoint(width, 0),
				new PolygonPoint(width, height),
				new PolygonPoint(0, height)
		});
		
		NavMeshClipper nmc = new NavMeshClipper(10 / EFConstants.PPM, 1.2 / EFConstants.PPM, 1 / EFConstants.PPM);
		walkables = nmc.clipToWalkables(initialWalkable, mapObjColliders);
		
		for (Polygon polygon : walkables) {
			Poly2Tri.triangulate(polygon);
		}
	}
	
	public void draw(ShapeRenderer renderer) {
		 
		if (walkables != null) {
			for(org.poly2tri.geometry.polygon.Polygon walkable : walkables) {
				drawTriangleMesh(walkable, Color.FIREBRICK, renderer);
			}
		}
	}
	
	private void drawTriangleMesh(org.poly2tri.geometry.polygon.Polygon p, Color meshColor, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(meshColor);
        for(DelaunayTriangle dt : p.getTriangles()) {
            shapeRenderer.triangle(
                    dt.points[0].getXf(), dt.points[0].getYf(),
                    dt.points[1].getXf(), dt.points[1].getYf(),
                    dt.points[2].getXf(), dt.points[2].getYf());
        }

        // Draw holes (obstacles)
        final ArrayList<org.poly2tri.geometry.polygon.Polygon> holes = p.getHoles();
        if(holes != null) {
            shapeRenderer.setColor(Color.YELLOW);
            for (org.poly2tri.geometry.polygon.Polygon nextHole : holes) {
                final List<TriangulationPoint> points = nextHole.getPoints();
                for (int i = 0; i < points.size(); ++i) {
                    TriangulationPoint tpA = points.get(i);
                    TriangulationPoint tpB = points.get((i + 1) % points.size());
                    shapeRenderer.line(tpA.getXf(), tpA.getYf(), tpB.getXf(), tpB.getYf());
                }
            }
        }
    }
	
	public Array<Polygon> getWalkables() {
		return walkables;
	}
}
