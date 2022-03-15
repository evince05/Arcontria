package dev.eternalformula.arcontria.level.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import dev.eternalformula.arcontria.entity.MapEntityBuilder;
import dev.eternalformula.arcontria.entity.ambient.Lamppost;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.util.EFConstants;

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
				
				MapProperties objProps = getMapProperties(obj);
				if (true) {
					int id = obj.getInt("gid") - el.getChildByName("tileset").getInt("firstgid");
					FileHandle tilesetFile = getRelativeFileHandle(tmxFile, el.getChildByName("tileset").get("source"));
					
					Element tileset = xml.parse(tilesetFile);
					
					for (Element tileElement : tileset.getChildrenByName("tile")) {
						if (tileElement.getInt("id") == id) {
							String source = tileElement.getChild(0).get("source");
							FileHandle img = getRelativeFileHandle(tilesetFile, source);
							
							Texture tex = new Texture(Gdx.files.internal(img.path()));
							TextureRegion region = new TextureRegion(tex);
							float x = element.getFloat("x") / EFConstants.PPM;
							float y = (map.getProperties().get("height", int.class) * 16 
									- element.getFloat("y")) / EFConstants.PPM;
							
							float width = obj.getFloat("width") / EFConstants.PPM;
							float height = obj.getFloat("height") / EFConstants.PPM;
							
							int entityId = objProps.get("entityId", int.class); // TODO: fix
							level.addEntity(MapEntityBuilder.createEntity(level, entityId, region, x, y,
									width, height, objProps));
						}
						continue;
					}
					return;
				}
				/*
				else {
					super.loadObject(map, objects, element, heightInPixels);
					
					// Load Object Properties and pass them to modified version of loadObject().
				}*/
				
				//return;
			}
		}
	}
	
	/**
	 * Gets the mapproperties of a given element.
	 * @param e The element to be used.
	 * @return The map properties of a given element, null<br>
	 *  if the element has no properties.
	 */
	
	private MapProperties getMapProperties(Element e) {
		
		if (e != null && e.getChildByName("properties") != null) {
			MapProperties props = new MapProperties();
			
			for (Element property : e.getChildByName("properties").getChildrenByName("property")) {
				String name = property.getAttribute("name");
				String value = property.getAttribute("value");
				String type = property.getAttribute("type");
				Object castValue = castProperty(name, value, type);
				props.put(name, castValue);
			}
			return props;
		}
		return null;
	}
}
