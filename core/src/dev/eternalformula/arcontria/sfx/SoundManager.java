package dev.eternalformula.arcontria.sfx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;

public class SoundManager {
	
	private Map<Long, Sound> liveSounds;
	
	private static SoundManager instance;
	
	public SoundManager() {
		this.liveSounds = new HashMap<Long, Sound>();
		instance = this;
	}
	
	public void initSounds() {
		
		int count = 0;
		for (int i = 0; i < Sounds.values().length; i++) {
			Assets.load(Sounds.values()[i].getPath(), Sound.class);
			count++;
		}
		
		Assets.updateInstance();
		EFDebug.info("Loaded " + count + " sounds through SoundManager!");
	}
	
	/**
	 * Plays the specified sound with the sound's default volume.
	 * @param sound The sound to play.
	 */
	
	public long playSound(Sounds sound) {
		return playSound(sound, sound.getDefaultVolume());
	}
	
	public long playSound(Sounds sound, float volume) {
		Sound s = Assets.get(sound.getPath(), Sound.class);
		long soundId = s.play(volume);
		
		liveSounds.put(soundId, s);
		
		return soundId;
	}
	
	public boolean isPlayingSound(long soundId) {
		return liveSounds.containsKey(soundId);
	}
	
	public Sound getSound(long soundId) {
		return liveSounds.get(soundId);
	}
	
	public enum Sounds {
		TORCH_CRACKLE("sfx/ambient/torch.wav", 1f);
		
		private String sfxPath;
		private float defaultVolume;
		
		Sounds(String sfxPath, float defaultVolume) {
			this.sfxPath = sfxPath;
			this.defaultVolume = defaultVolume;
		}
		
		public String getPath() {
			return sfxPath;
		}
		
		public float getDefaultVolume() {
			return defaultVolume;
		}
	}
	
	public static final SoundManager getInstance() {
		return instance;
	}

}
