package dev.eternalformula.arcontria.level.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import dev.eternalformula.arcontria.entity.ambient.Lamppost;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.Strings;

/**
 * An extension of the TmxMapLoader that allows Tiled's Template Objects to be loaded.
 * @author EternalFormula
 */

public class TemplateTmxMapLoader extends TmxMapLoader {

	FileHandle tmxFile;
	private GameLevel level;
	
	public TemplateTmxMapLoader(GameLevel level) {
		super();
		this.level = level;
	}
	
    @Override
    protected TiledMap loadTiledMap (FileHandle tmxFile, TmxMapLoader.Parameters parameter, ImageResolver imageResolver){
          this.tmxFile = tmxFile;
          return super.loadTiledMap(tmxFile,parameter,imageResolver);
    }
    
	@Override
	protected void loadObject (TiledMap map, MapObjects objects, XmlReader.Element element, float heightInPixels) {
		if (element.getName().equals("object")) {

			if(!element.hasAttribute("template")) {
				super.loadObject(map,objects,element,heightInPixels);
				return;
			}
			
			FileHandle template = getRelativeFileHandle(tmxFile,element.getAttribute("template"));
			Element el = xml.parse(template);
			for(Element obj : el.getChildrenByName("object")) {
				int id = obj.getInt("gid") - el.getChildByName("tileset").getInt("firstgid");
				FileHandle tilesetFile = getRelativeFileHandle(tmxFile, el.getChildByName("tileset").get("source"));
				
				Element tileset = xml.parse(tilesetFile);
				
				for (Element tileElement : tileset.getChildrenByName("tile")) {
					if (tileElement.getInt("id") == id) {
						String source = tileElement.getChild(0).get("source");
						FileHandle img = getRelativeFileHandle(tilesetFile, source);
						
						Texture tex = new Texture(Gdx.files.internal(img.path()));
						TextureRegion region = new TextureRegion(tex);
						float x = element.getFloat("x");
						float y = map.getProperties().get("height", int.class) * 16 - element.getFloat("y"); // tex.getWidth() is because tiled renders in top left.
						
						int type = obj.getChildByName("properties").getChild(0).getIntAttribute("value");
						
						level.addEntity(new Lamppost(level, region, x, y, type));
					}
					continue;
				}
				return;
			}
		}
	}
}
