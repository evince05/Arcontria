package dev.eternalformula.arcontria.objects.debug;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.pathfinding.Path;
import dev.eternalformula.arcontria.pathfinding.PathNode;

public class PathRenderer {
	
	 private ShapeRenderer renderer;	 
	 private List<Path> pathsToRender;
	 private boolean enabled;
	 
	 public PathRenderer() {
		 this.renderer = new ShapeRenderer();
		 this.pathsToRender = new ArrayList<Path>();
		 this.enabled = false;
		 renderer.setAutoShapeType(true);
	 }
	 
	 public void render() {
		 renderer.setProjectionMatrix(ArcontriaGame.GAME.getScene().getViewport().getCamera().combined);
		 renderer.begin(ShapeType.Line);
		 renderer.setColor(Color.GOLD);
		 for (Path path : pathsToRender) {
			 for (PathNode node : path.getNodes()) {
				 renderer.rect(node.gridX, node.gridY, 1, 1);
			 }
		 }
		 
		 renderer.end();
	 }
	 
	 public void addPath(Path p) {
		 pathsToRender.add(p);
	 }
	 
	 public void dispose() {
		 renderer.dispose();
	 }
	 
	 public void toggle() {
		 this.enabled = !enabled;
	 }
	 
	 public boolean isEnabled() {
		 return enabled;
	 }

}
