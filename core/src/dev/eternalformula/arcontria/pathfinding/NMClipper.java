package dev.eternalformula.arcontria.pathfinding;

import com.badlogic.gdx.utils.Array;
import com.shibabandit.gdx_navmesh.coll.CollUtil;
import com.shibabandit.gdx_navmesh.util.JtsUtil;
import com.shibabandit.gdx_navmesh.util.Poly2TriPolygonFactory;

import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.poly2tri.geometry.polygon.Polygon;

/**
 * NavMesh preprocessor that clips geometry to world bounds using JTS and converts to poly2tri Polygons
 * read for triangulation. Adapts JTS Polygon Geometry to poly2tri Polygons for triangulation.
 */
public final class NMClipper {

    /** Default minimum area for a walkable surface. */
    private static final double DEFAULT_WALKABLE_MIN_AREA_THRESHOLD = 25d / EFConstants.PPM;

    /** Default simplification distance tolerance. */
    private static final double DEFAULT_DIST_TOLERANCE = 2d / EFConstants.PPM;

    /** Default buffer distance. */
    private static final double DEFAULT_BUFFER_DIST = 4d / EFConstants.PPM;

    private final GeometryFactory geomFactory;

    /** Minimum walkable area to be considered valid */
    private final double minWalkArea;

    /** Distance tolerance for simplification */
    private final double distTolerance;

    /** Distance to use for buffer operation. */
    private final double bufferDist;

    public NMClipper() {
        this(DEFAULT_WALKABLE_MIN_AREA_THRESHOLD, DEFAULT_DIST_TOLERANCE, DEFAULT_BUFFER_DIST);
    }

    /**
     * @param minWalkArea minimum walkable area to be considered valid
     * @param distTolerance distance tolerance for simplification
     * @param bufferDist distance to use for buffer operation
     */
    public NMClipper(double minWalkArea, double distTolerance, double bufferDist) {
        this.geomFactory = new GeometryFactory();
        this.minWalkArea = minWalkArea;
        this.distTolerance = distTolerance;
        this.bufferDist = bufferDist;
    }

    /**
     * Return a list of walkable {@link Polygon}. Unions obstacles together and subtracts their area from the world
     * bounds. Simplifies the resulting walkable polygons.
     *
     * @param bounds world bounds
     * @param jtsObs list of obstacle JTS Polygon Geometry
     * @return list of walkable Polygons
     */
    public Array<Polygon> clipToWalkables(Polygon bounds, Array<org.locationtech.jts.geom.Polygon> jtsObs) {
        final Array<Geometry> jtsGeoms = clipToJtsWalkables(bounds, jtsObs);
        final Array<Polygon> ptPolys = new Array<>(jtsGeoms.size);

        // Convert from JTS
        for(Geometry g : jtsGeoms) {
            ptPolys.add(Poly2TriPolygonFactory.fromJtsPoly(g));
        }

        return ptPolys;
    }


    /**
     * Return a list of walkable {@link Geometry}. Unions obstacles together and subtracts their area from the world
     * bounds. Simplifies the resulting walkable polygons.
     *
     * @param bounds world bounds
     * @param jtsObs list of obstacle JTS Polygon Geometry 
     * @return list of walkable Polygons
     */
    public Array<Geometry> clipToJtsWalkables(Polygon bounds, Array<org.locationtech.jts.geom.Polygon> jtsObs) {
        final Array<Geometry> simplePolys = new Array<>();

        // Guard: no obstacles
        if(jtsObs.size < 1) {
        	EFDebug.info("Returning line 89");
            return simplePolys; // TODO: Should return a walkable surface with the world...
        }

        // Convert bounds to JTS polygon
        final Geometry jtsBounds = CollUtil.toJtsPoly(bounds);

        // Union all obstacle polygons with buffer

        // TODO: Fix nasty array conversion
        final org.locationtech.jts.geom.Polygon[] jtsObsArray = new org.locationtech.jts.geom.Polygon[jtsObs.size];
        for(int i = 0; i < jtsObs.size; ++i) {
            jtsObsArray[i] = jtsObs.get(i);
        }

        final GeometryCollection geomColl = new GeometryCollection(jtsObsArray, geomFactory);
        final BufferOp tileBufferOp = new BufferOp(geomColl);
        tileBufferOp.setEndCapStyle(BufferParameters.CAP_SQUARE);
        final Geometry unionColl = tileBufferOp.getResultGeometry(bufferDist);

        final Geometry simpleUnionColl = DouglasPeuckerSimplifier.simplify(unionColl, distTolerance).buffer(0d);

        // Subtract obstacles from walkable bounds
        final Geometry boundsMinusObstacles = jtsBounds.difference(simpleUnionColl);
        EFDebug.info("bounds m o :" + boundsMinusObstacles.getArea());

        // Unroll any geometry collections
        Array<Geometry> jtsWalkables = JtsUtil.flatList(boundsMinusObstacles);

        // Simplify, convert from jts polys
        for(Geometry g : jtsWalkables) {
            if(g.getArea() >= minWalkArea) {

                // Simplify
                g = DouglasPeuckerSimplifier.simplify(g, distTolerance);

                // Simplified geometry may be empty
                if(!g.isEmpty()) {
                    simplePolys.add(g);
                }
            }
            else {
            	EFDebug.info("Nope, area is: " + g.getArea());
            }
        }

        EFDebug.info("Returning default");
        return simplePolys;
    }
}
