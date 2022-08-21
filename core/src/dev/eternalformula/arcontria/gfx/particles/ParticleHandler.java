package dev.eternalformula.arcontria.gfx.particles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.util.Assets;

public class ParticleHandler {
	
	private GameLevel level;
	private BitmapFont textParticleFont;
	
	private List<Particle> activeParticles;
	private List<Particle> particlesToRemove;
	
	public ParticleHandler(GameLevel level) {
		this.level = level;
		this.activeParticles = new ArrayList<Particle>();
		this.particlesToRemove = new ArrayList<Particle>();
		this.textParticleFont = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		textParticleFont.setUseIntegerPositions(false);
	}
	
	public void spawnParticle(Particle particle) {
		activeParticles.add(particle);
	}
	
	public void update(float delta) {
		for (Particle p : activeParticles) {
			p.update(this, delta);
		}
		
		for (Particle pRemove : particlesToRemove) {
			activeParticles.remove(pRemove);
		}
		
		particlesToRemove.clear();
	}
	
	public void draw(SpriteBatch batch, float delta) {
		for (Particle p : activeParticles) {
			p.draw(this, batch, delta);
		}
	}
	
	public void dispose() {
		textParticleFont.dispose();
	}
	
	public BitmapFont getTextParticleFont() {
		return textParticleFont;
	}
	
	public List<Particle> getActiveParticles() {
		return activeParticles;
	}
	
	public void removeParticle(Particle p) {
		particlesToRemove.add(p);
	}

}
