package dev.eternalformula.arcontria.gfx.particles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ParticleManager {
	
	public static ShapeRenderer rend;
	
	private List<Particle> activeParticles;
	private List<Particle> particlesToRemove;
	
	public ParticleManager() {
		this.activeParticles = new ArrayList<Particle>();
		this.particlesToRemove = new ArrayList<Particle>();
		rend = new ShapeRenderer();
	}
	
	public void spawnParticle(Particle particle) {
		activeParticles.add(particle);
	}
	
	public void update(float delta) {
		for (Particle p : activeParticles) {
			p.update(delta);
		}
		
		for (Particle pRemove : particlesToRemove) {
			activeParticles.remove(pRemove);
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		rend.setAutoShapeType(true);
		rend.begin();
		rend.setColor(Color.BLUE);
		for (Particle p : activeParticles) {
			p.draw(batch, delta);
		}
		rend.end();
	}

}
