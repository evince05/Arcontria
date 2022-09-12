package dev.eternalformula.arcontria.util;

import java.text.DecimalFormat;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;

import dev.eternalformula.arcontria.objects.noise.FractalNoise.NoiseConstants;

public class EFUtil {

	public static Color getColorFromRGB(float r, float g, float b) {
		return new Color(r / 255f, g / 255f, b / 255f, 1.0f);	
	}
	
	public static Color getColorFromRGBA(float r, float g, float b, float a) {
		return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
	}
	
	public static String encryptString(String str) {
		double startTime = System.currentTimeMillis();
		String keycode = "34p61e99r";
		
		// consts[0] is not a value that should be used
		String[] consts = keycode.split("[0-9]{2}");
		
		String[] rands = keycode.split("[^0-9]");
		
		float constA = NoiseConstants.get(consts[1].charAt(0));
		float constB = NoiseConstants.get(consts[2].charAt(0));
		float constC = NoiseConstants.get(consts[3].charAt(0));

		float randA = Float.valueOf(rands[0]);
		float randB = Float.valueOf(rands[1]);
		float randC = Float.valueOf(rands[2]);
		
		float prevEnc = 0f;
		StringBuilder sb = new StringBuilder();
		DecimalFormat formatter = new DecimalFormat("####.##");
		
		int i = 0;
		for (char c : str.toCharArray()) {
			int charAscii = EFMath.toAscii(c);

			float enc = charAscii * (constB * constC / constA) + randA / (randC - 2f * randB);
			
			if (i != 0) {
				enc -= prevEnc;
			}
			String encS = formatter.format(enc) + EFMath.getRandomLetter(1);
			sb.append(encS);
			
			prevEnc = enc;
			
			i++;
		}
		
		double time = (System.currentTimeMillis() - startTime) / 1000D;
		System.out.println("Encryption took " + time + "s");
		
		return sb.toString();
	}
	
	public static String decryptString(String str) {
		double startTime = System.currentTimeMillis();
		
		String keycode = "34p61e99r";
		
		String[] cs = str.split("[a-zA-Z]");
		float[] fcs = new float[cs.length];
		
		for (int f = 0; f < fcs.length; f++) {
			fcs[f] = Float.valueOf(cs[f]);
		}
		cs = null;
		
		String[] consts = keycode.split("[0-9]{2}");
		
		String[] rands = keycode.split("[^0-9]");
		
		float constA = NoiseConstants.get(consts[1].charAt(0));
		float constB = NoiseConstants.get(consts[2].charAt(0));
		float constC = NoiseConstants.get(consts[3].charAt(0));
		
		float randA = Float.valueOf(rands[0]);
		float randB = Float.valueOf(rands[1]);
		float randC = Float.valueOf(rands[2]);
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < fcs.length; i++) {
			float ascii = 0f;
			if (i != 0) {
				ascii = ((fcs[i] + fcs[i - 1]) - (randA / (randC - 2f * randB))) /
						(constB * constC / constA);
			}
			else {
				ascii = (fcs[i] - (randA / (randC - 2f * randB))) /
						(constB * constC / constA);
			}
			
			int r = Math.round(ascii);
			sb.append(EFMath.toChar(r));
		}
		
		double time = (System.currentTimeMillis() - startTime) / 1000D;
		System.out.println("Decryption took " + time + "s");
		
		return sb.toString();
	}
}
