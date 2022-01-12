package dev.eternalformula.arcontria.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

public class EGFXUtil {
	
	private ShapeRenderer shapeRenderer;
	
	public static final float DEFAULT_WIDTH = 640f;
	public static final float DEFAULT_HEIGHT = 360f;
	public static final float DEFAULT_UPP = 1/32f;
	public static float RENDER_SCALE = 2f;
	
	public EGFXUtil() {
		shapeRenderer = new ShapeRenderer();
	}
	
	/**
	 * This method draws lines in the center of both axes on the screen.
	 * <br>Note that this will not be called if an EGFXUtil object is not
	 * <br>loaded in memory.
	 */
	
	public void drawCenterLines() {
		Gdx.gl.glLineWidth(5f);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.line(Gdx.graphics.getWidth() / 2f, 0f, Gdx.graphics.getWidth() / 2f, 0);
		shapeRenderer.end();
	}
	
	public void setShapeRendererMatrix(Matrix4 matrix) {
		this.shapeRenderer.setProjectionMatrix(matrix);
	}

}
