package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.audio.Sound;

import dev.eternalformula.arcontria.sfx.SoundManager.Sounds;

public class AmbientSoundComponent implements Component {

	public static ComponentMapper<AmbientSoundComponent> Map =
			ComponentMapper.getFor(AmbientSoundComponent.class);
	
	public Sounds sound;
	public long soundId;
	
	public float volume;
	
	public AmbientSoundComponent() {
		this.sound = null;
	}
}
