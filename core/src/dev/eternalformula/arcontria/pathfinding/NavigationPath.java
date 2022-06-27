package dev.eternalformula.arcontria.pathfinding;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.shibabandit.gdx_navmesh.path.NavMeshPathFinder;
import com.shibabandit.gdx_navmesh.path.NavMeshPathNode;
import com.shibabandit.gdx_navmesh.path.NavMeshPathRequest;
import com.shibabandit.gdx_navmesh.path.NavMeshPortal;
import com.shibabandit.gdx_navmesh.path.NavMeshStringPuller;
import com.shibabandit.gdx_navmesh.path.PortalMidpointDistHeuristic;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.level.maps.Map;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

/**
 * General class for Pathfinding using a Navigation Mesh.
 * @author EternalFormula
 */

public class NavigationPath implements Telegraph {
	
	// Monitor this.
	private static final float MAX_TRI_WALKABLE_DIST = 800f / EFConstants.PPM;
	private static final int PF_TELEGRAM_REQUEST = 1;
	private static final int PF_TELEGRAM_RESPONSE = 2;
	
	// This value is measured in frames.
	private static final float THRESHOLD_CHANGE_DURATION = 7.5f; // should roughly translate to 0.125s
	
	private Map map;
	private Navmesh navmesh;
	
	private Vector2 startPos;
	private Vector2 endPos;
	
	private Array<Vector2> points;
	private NavMeshPathFinder pathFinder;
	private NavMeshStringPuller stringPuller;
	
	// Threshold Angles
	
	/**
	 * Possible (Verbose) solution: Create 8 'angleThresh' variables, each representing a
	 * different 45 degree section of the 360 degree circle.
	 * 
	 * Upon crossing a threshold, decrease said threshold by a fixed number so that the entity
	 * must travel back at a further angle in order to switch the animation once more.
	 * 
	 * Pros: Should work (in theory?)
	 * Cons: Very verbose. I'm sure there's a much easier/concise way.
	 */ 
	
	private DefaultGraphPath<NavMeshPathNode> latestPath;
	private float lastAngleToTarget;
	
	private int framesSinceLastDirectionChange;
	
	private final float agentRadius;
	
	/**
	 * Creates a new navigation path.
	 * @param map The map in which this navigation path exists.
	 */
	
	public NavigationPath(Map map, Vector2 startPos, Vector2 endPos, float agentRadius) {
		this.map = map;
		this.navmesh = map.getNavmesh();
		this.startPos = startPos;
		
		this.agentRadius = agentRadius;
		
		this.pathFinder = new NavMeshPathFinder(new PortalMidpointDistHeuristic(),
				navmesh.getWalkables(), PF_TELEGRAM_REQUEST, PF_TELEGRAM_RESPONSE, MAX_TRI_WALKABLE_DIST);
		
		this.stringPuller = new NavMeshStringPuller();
		
		this.points = new Array<Vector2>();
	}
	
	/**
	 * Recalibrates the path between the specified coordinates.
	 * @param startPos The new starting position of the path.
	 * @param endPos The new end position of the path.
	 */
	
	public void recalibrate(Vector2 startPos, Vector2 endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
		
		// Recalibrate path.
		pathFinder.findPath(startPos, endPos, agentRadius, this);
	}
	
	/**
	 * Updates the path as it is being traversed.
	 * @param delta The amount of time that has elpased since the last frame.
	 */
	
	public void update() {
		pathFinder.run(1000000L);
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		switch (msg.message) {
		case PF_TELEGRAM_RESPONSE:
			final NavMeshPathRequest pfr = (NavMeshPathRequest) msg.extraInfo;
			latestPath = (DefaultGraphPath<NavMeshPathNode>) pfr.resultPath;
             
			if (latestPath.getCount() > 0) {
	             
				final NavMeshPortal[] portals = stringPuller.pathToPortals(latestPath);
				this.points = stringPuller.stringPull(startPos, endPos, portals, agentRadius);
			}
			else {
                points = null;
                EFDebug.warn("Path not found!");
            }

            // Release the request
            Pools.free(pfr);
            break;
		}
		return true;
	}
	
	public void draw() {
		if (points != null && points.size > 0) {
			
			
			ShapeRenderer rend = map.getDebugRenderer();
			
			rend.begin(ShapeType.Filled);
			
			rend.setColor(Color.CHARTREUSE);
	        for(int i = 0; i < latestPath.getCount(); ++i) {
	            NavMeshPortal portal = latestPath.get(i).getPortal();
	            rend.rectLine(portal.getLeft(), portal.getRight(), 2 / 16f);
	        }
	        
	        rend.setColor(Color.PINK);
			for (int i = 0; i < points.size; i++) {
				Vector2 point = points.get(i);
				rend.rect(point.x, point.y, 4 / 16f, 4 / 16f);
			}
	        
			rend.end();
		}
		
		
	}

	/**
	 * Gets the current path.
	 */
	
	public GraphPath<NavMeshPathNode> getCurrentPath() {
		return latestPath;
	}
	
	public Array<Vector2> getPoints() {
		return points;
	}
	
	public Vector2 getEndPosition() {
		return endPos;
	}
	
	public float getLastAngleToTarget() {
		return lastAngleToTarget;
	}
	
	public void setLastAngleToTarget(float angle) {
		this.lastAngleToTarget = angle;
	}
	
	/**
	 * Determines if the specified angle has passed one of the
	 * threshold angles that cause an animation direction switch.
	 * @param currentAngle The angle to check.
	 * @return true if the angle has 
	 *
	
	public boolean hasJustPassedThreshold(float currentAngle) {
		return Math.abs(currentAngle / 45f - ((int) currentAngle / 45)) < 1 / 45f ||
				Math.abs(currentAngle / 45f - ((int) currentAngle / 45)) > 1 - 1 / 45f;
	}*/
	
	/**
	 * Gets the proper animation direction for the entity, while considering
	 * how recently the entity has switched directions (to avoid flickering).
	 * @param entity The entity to be animated.
	 * @param angle The angle of the entity to its target.
	 * 
	 * @return The animation direction which the entity should use. 
	 */
	
	public int getAnimationDirection(LivingEntity entity, float angle) {
		
		float threshUR = 45;
		float threshUL = 135;
		float threshDL = 225;
		float threshDR = 315;
		
		int simDirection = getSimulatedDirection(angle);
		
		if (simDirection != entity.getLastAnimationDirection() &&
				framesSinceLastDirectionChange > THRESHOLD_CHANGE_DURATION) {
			
			// Entity is crossing a threshold without recently having crossed it.
			int threshold = getThreshold(angle);
			
			float thresChangeDirection = getThreshChangeDirection(lastAngleToTarget, angle);
			float deltaThresh = 5 * thresChangeDirection;
			
			if (threshold == 1) {
				threshUR += deltaThresh;
			}
			else if (threshold == 3) {
				threshUL += deltaThresh;
			}
			else if (threshold == 5) {
				threshDL += deltaThresh;
			}
			else if (threshold == 7) {
				threshDR += deltaThresh;
			}
			
			framesSinceLastDirectionChange = 0;
			
			// Possible fix if this is broken. Move outside if clause.
			if (angle >= threshUR && angle <= threshUL) {
				return 1;
			}
			else if (angle > threshUL && angle < threshDL) {
				return 2;
			}
			else if (angle > threshDR || angle < threshUR) {
				return 3;
			}
			else if (angle >= threshDL && angle <= threshDR) {
				return 4;
			}
			
			return 4;
		}
		
		// If this block of code is executed, the direction has not changed.
		framesSinceLastDirectionChange++;
		
		/*
		EFDebug.debug("Returning previous animation direction: " +
				"[Current, Prev = " + Strings.vec2(angle, entity.getLastAnimationDirection()) + "], framesElapsed = "
				+ framesSinceLastDirectionChange);
		*/
		
		
		// Default return value is the entity's last animation.
		return entity.getLastAnimationDirection();
	}
	
	private int getSimulatedDirection(float angle) {
		if (angle >= 45 && angle <= 135) {
			return 1;
		}
		else if (angle > 135 && angle < 225) {
			return 2;
		}
		else if (angle > 315 || angle < 45) {
			return 3;
		}
		else if (angle >= 225 && angle <= 315) {
			return 4;
		}
		
		return 1;
	}
	
	private int getThreshold(float angle) {
		return (int) Math.round(angle / 45f);
	}
	
	private float getThreshChangeDirection(float oldAngle, float newAngle) {
		return (newAngle - oldAngle > 0) ? -1 : 1;
	}

}
