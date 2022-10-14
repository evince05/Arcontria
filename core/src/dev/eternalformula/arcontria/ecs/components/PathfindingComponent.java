package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;

import dev.eternalformula.arcontria.pathfinding.paths.Path;

public class PathfindingComponent implements Component {
	
	private Path path;
	
	public Path getPath() {
		return path;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}
	
	public boolean hasPath() {
		return path != null;
	}

}
