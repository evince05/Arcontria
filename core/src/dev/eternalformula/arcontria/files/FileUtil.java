package dev.eternalformula.arcontria.files;

import java.io.File;

public class FileUtil {
	
	public static final String SAVES_FOLDER_LOCATION = System.getenv("APPDATA") + File.separator +
			"Arcontria" + File.separator + "saves";
	
	public static final String MAP_SCENERY_ATLAS = "textures/maps/scenery/gen_map_scenery.atlas";

}
