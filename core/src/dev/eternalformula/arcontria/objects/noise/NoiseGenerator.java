package dev.eternalformula.arcontria.objects.noise;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import dev.eternalformula.arcontria.ArcontriaGame;

/**
 * Noise generation utility class using {@link FractalNoise}.}<br>
 * Used to create FractalNoise grids with values in the range [0, 1].
 * @author EternalFormula
 */

public class NoiseGenerator {
	
	private static final float DEFAULT_FREQUENCY = 1 / 32f;
	private static final int DEFAULT_OCTAVE_COUNT = 5;
	private static final int DEFAULT_SCALE_FACTOR = 4;
	private static final float DEFAULT_LACUNARITY = 1.75f;
	private static final float DEFAULT_GAIN = 4/7f;
	private static final float DEFAULT_FOAM_SHARPNESS = 1f;
	
	/**
	 * Creates a noise grid using FractalNoise.
	 * @param seed The seed to use.
	 * @param width The width of the noise grid (in world units)
	 * @param height The height of the noise grid.
	 * @return A noise grid of size [width, height].
	 */
	
	public static float[][] createNoiseGrid(int seed, int width, int height) {
		return createNoiseGrid(seed, width, height, DEFAULT_SCALE_FACTOR, 
				DEFAULT_FREQUENCY, DEFAULT_OCTAVE_COUNT, DEFAULT_LACUNARITY, DEFAULT_GAIN,
				DEFAULT_FOAM_SHARPNESS);
	}
	
	/**
	 * Creates a noise grid using FractalNoise.
	 * @param seed The seed to use.
	 * @param width The width of the noise grid (in world units)
	 * @param height The height of the noise grid (in world units)
	 * @param scaleFactor The amount to scale the grid (higher values cause greater detail).
	 * @param frequency The frequency of the grid
	 * @param octaves The number of octaves
	 * @param lacunarity The lacunarity of the fractal
	 * @param gain The gain of the fractal
	 * @return A noise grid of size [width, height].
	 */
	
	public static float[][] createNoiseGrid(int seed, int width, int height, int scaleFactor,
			float frequency, int octaves, float lacunarity, float gain, float foamSharpness) {
		// Creates the noise object
		FractalNoise fracNoise = new FractalNoise(seed, frequency, octaves, lacunarity,
				gain, foamSharpness);
		
		// Creates the enlarged noise grid
		float[][] enlargedGrid = new float[height * scaleFactor][width * scaleFactor];
		
		for (int row = 0; row < height * scaleFactor; row++) {
			for (int col = 0; col < width * scaleFactor; col++) {
				
				// Get the value of the noise at the current pos
				enlargedGrid[row][col] = 
						fracNoise.singleSimplexFractalRidgedMulti(col, row) / 2f + 0.5f;
			}
		}
		
		// Creates the downscaled noise grid and returns
		return resizeNoiseGrid(enlargedGrid, scaleFactor);
	}
	
	public static float[][] createOreGrid(int seed, int width, int height, int scaleFactor,
			float frequency, int octaves, float lacunarity, float gain, float foamSharpness) {
		// Creates the noise object
		FractalNoise fracNoise = new FractalNoise(seed, frequency, octaves, lacunarity,
				gain, foamSharpness);
		
		// Creates the enlarged noise grid
		float[][] enlargedGrid = new float[height * scaleFactor][width * scaleFactor];
		
		for (int row = 0; row < height * scaleFactor; row++) {
			for (int col = 0; col < width * scaleFactor; col++) {
				
				// Get the value of the noise at the current pos
				enlargedGrid[row][col] = 
						fracNoise.singleFoamFractalBillow(col, row) / 2f + 0.5f;
			}
		}
		
		// Creates the downscaled noise grid and returns
		return resizeNoiseGrid(enlargedGrid, scaleFactor);
	}
	
	/**
	 * Downscales an enlarged noise grid to meet the original dimensions of [width, height]<br>
	 * while maintaining a cleaner and more detailed noise grid.
	 * @param noiseGrid The enlarged noise grid, of dimension [width * scaleFactor, height * scaleFactor].
	 * @param scale The scale to be used (use scaleFactor)
	 * @return The downscaled version of the enlarged noise grid.
	 */
	
	private static float[][] resizeNoiseGrid(float[][] noiseGrid, int scale) {
		int gridW = noiseGrid[0].length; // num cols
		int gridH = noiseGrid.length; // num rows
		
		int numSquaresW = gridW / scale;
		int numSquaresH = gridH / scale;
		
		float[][] downscaledGrid = new float[numSquaresH][numSquaresW];
		
		// Iterates through the "rows and cols" of the squares
		for (int sRow = 0; sRow < numSquaresH; sRow++) {
			for (int sCol = 0; sCol < numSquaresW; sCol++) {
				
				// Gets square coords
				int sX = scale * sCol;
				int sY = scale * sRow;
				
				float squareValsSum = 0f;
				int count = 0;
				
				// Adds the whole inner square to the squareValsSum
				for (int row = 0; row < scale; row++) {
					for (int col = 0; col < scale; col++) {
						squareValsSum += noiseGrid[sY + row][sX + col];
						count++;
					}
				}
				
				// Takes the avg the squareVals array and puts it into the downsized array
				float downscaledValue = squareValsSum / count;
				int dsX = sX / scale;
				int dsY = sY / scale;
				downscaledGrid[dsY][dsX] = downscaledValue;
			}
		}
		return downscaledGrid;
	}
	
	public static void debugNoiseGrid(SpriteBatch batch, float[][] noiseGrid,
			Color spawnColor, Color noSpawnColor) {
		
		noSpawnColor.a = 0.3f;
		
		batch.end();
		
		ShapeRenderer shapeRend = ArcontriaGame.GAME.getSceneManager().getShapeRenderer();
		shapeRend.setAutoShapeType(true);
		shapeRend.setProjectionMatrix(batch.getProjectionMatrix());
		
		// Alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Draws the rect
		shapeRend.begin();
		shapeRend.set(ShapeType.Filled);
		
		for (int row = noiseGrid.length - 1; row >= 0; row--) {
			for (int col = 0; col < noiseGrid[0].length; col++) {
				
				if (noiseGrid[row][col] <= 0.55f) {
					// Spawnable
					float alpha = noiseGrid[row][col] / 0.55f + 0.2f;
					if (alpha > 1f) {
						alpha = 1f;
					}
					
					spawnColor.a = alpha;
				}
				shapeRend.setColor(noiseGrid[row][col] <= 0.55f ? spawnColor : noSpawnColor);
				shapeRend.rect(col, row, 1f, 1f);
			}
		}
		
		shapeRend.end();
		
		// Stop alpha
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		batch.begin();
	}
	
	public static void debugOreGrid(SpriteBatch batch, float[][] noiseGrid) {
		
		Color color10 = Color.PINK;
		Color color20 = Color.MAGENTA;
		Color color30 = Color.ORANGE;
		Color color40 = Color.CYAN;
		
		batch.end();
		
		ShapeRenderer shapeRend = ArcontriaGame.GAME.getSceneManager().getShapeRenderer();
		shapeRend.setAutoShapeType(true);
		shapeRend.setProjectionMatrix(batch.getProjectionMatrix());
		
		// Alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Draws the rect
		shapeRend.begin();
		shapeRend.set(ShapeType.Filled);
		
		for (int row = noiseGrid.length - 1; row >= 0; row--) {
			for (int col = 0; col < noiseGrid[0].length; col++) {
				
				Color rockColor = Color.LIME;
				if (noiseGrid[row][col] <= 0.4f) {
					// Ore
					
					float oreVal = noiseGrid[row][col];
					if (oreVal < 0.25f) {
						rockColor = color10;
					}
					else if (oreVal < 0.275f) {
						rockColor = color20;
					}
					else if (oreVal < 0.325f) {
						rockColor = color30;
					}
					else if (oreVal < 0.4f) {
						rockColor = color40;
					}
					
					shapeRend.setColor(rockColor);
					shapeRend.rect(col, noiseGrid.length - 1 - row, 1f, 1f);
				}
			}
		}
		
		shapeRend.end();
		
		// Stop alpha
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		batch.begin();
	}

}
