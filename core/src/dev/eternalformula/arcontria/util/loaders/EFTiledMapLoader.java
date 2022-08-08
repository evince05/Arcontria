package dev.eternalformula.arcontria.util.loaders;

import org.locationtech.jts.geom.Polygon;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.level.maps.EFMapObject;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.level.maps.TemplateTmxMapLoader;
import dev.eternalformula.arcontria.util.loaders.EFTiledMapLoader.MapParameters;

public class EFTiledMapLoader extends SynchronousAssetLoader<EFTiledMap,
	MapParameters> {

	public EFTiledMapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public EFTiledMap load(AssetManager assMan, String fileName, 
			FileHandle file, MapParameters params) {
		
		TemplateTmxMapLoader loader = new TemplateTmxMapLoader();
		TiledMap map = loader.load(fileName);
		Array<EFMapObject> mapObjs = loader.getMapObjects();
		Array<Polygon> polys = loader.getNavmeshPolygons();
		
		return new EFTiledMap(map, mapObjs, polys);
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
