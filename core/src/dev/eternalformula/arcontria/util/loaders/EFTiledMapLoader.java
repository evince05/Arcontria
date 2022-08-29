package dev.eternalformula.arcontria.util.loaders;

import org.locationtech.jts.geom.Polygon;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import box2dLight.RayHandler;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.maps.EFMapObject;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.level.maps.TemplateTmxMapLoader;
import dev.eternalformula.arcontria.util.loaders.EFTiledMapLoader.MapParameters;

public class EFTiledMapLoader extends SynchronousAssetLoader<EFTiledMap,
	MapParameters> {
	
	private World world;
	private RayHandler rayHandler;

	public EFTiledMapLoader(FileHandleResolver resolver, World world, RayHandler rayHandler) {
		super(resolver);
	
		// Physics
		this.world = world;
		this.rayHandler = rayHandler;
	}

	@Override
	public EFTiledMap load(AssetManager assMan, String fileName, 
			FileHandle file, MapParameters params) {
		
		TemplateTmxMapLoader loader = new TemplateTmxMapLoader(world, rayHandler);
		TiledMap map = loader.load(fileName);
		Array<EFMapObject> mapObjs = loader.getMapObjects();
		Array<Polygon> polys = loader.getNavmeshPolygons();
		Array<Entity> mapEntities = loader.getMapEntities();
		
		return new EFTiledMap(map, mapObjs, polys, mapEntities);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, 
			FileHandle file, MapParameters params) {
		return null;
	}
	
	public static class MapParameters extends AssetLoaderParameters<EFTiledMap> {
	}

}
