package dev.eternalformula.arcontria.ecs.systems.sfx;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;

import dev.eternalformula.arcontria.ecs.components.AmbientSoundComponent;
import dev.eternalformula.arcontria.sfx.SoundManager;

public class AmbientSoundSystem extends IteratingSystem {
	
	public static Family FAMILY = Family.all(AmbientSoundComponent.class).get();
	
	public AmbientSoundSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		AmbientSoundComponent asc = AmbientSoundComponent.Map.get(entity);
		
		SoundManager soundMgr = SoundManager.getInstance();
		
		if (!soundMgr.isPlayingSound(asc.soundId)) {
			long soundId = SoundManager.getInstance().playSound(asc.sound);
			asc.soundId = soundId;
			Sound s = SoundManager.getInstance().getSound(asc.soundId);
		}
		
	}

	

}
