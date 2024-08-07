package dev.eternalformula.arcontria.objects.noise;

/**
 * FractalNoise. Code from 
 * <a href="https://github.com/tommyettinger/make-some-noise/blob/master/src/main/java/make/some/noise/Noise.java">Source</a>
 */
public class FractalNoise {

	private int seed;
	private float frequency;
	private int octaves;
	private float lacunarity;
	private float gain;
	private float fractalBounding;
	private float foamSharpness;
	
	public static final float F2f = 0.3660254f;
	public static final float F3f = 0.33333334f;
	public static final float G2f = 0.21132487f;
    public static final float H2f = 0.42264974f;
    
    public enum NoiseConstants {
    	NOISE_CONST_A(0.321f),
    	NOISE_CONST_B(0.140f),
    	NOISE_CONST_C(0.470f),
    	NOISE_CONST_D(0.856f),
    	NOISE_CONST_E(0.138f),
    	NOISE_CONST_F(0.243f),
    	NOISE_CONST_G(0.368f),
    	NOISE_CONST_H(0.753f),
    	NOISE_CONST_I(0.472f),
    	NOISE_CONST_J(0.568f),
    	NOISE_CONST_K(0.187f),
    	NOISE_CONST_L(0.019f),
    	NOISE_CONST_M(0.034f),
    	NOISE_CONST_N(0.720f),
    	NOISE_CONST_O(0.964f),
    	NOISE_CONST_P(0.112f),
    	NOISE_CONST_Q(0.026f),
    	NOISE_CONST_R(0.285f),
    	NOISE_CONST_S(0.460f),
    	NOISE_CONST_T(0.103f),
    	NOISE_CONST_U(0.616f),
    	NOISE_CONST_V(0.860f),
    	NOISE_CONST_W(0.382f),
    	NOISE_CONST_X(0.575f),
    	NOISE_CONST_Y(0.559f),
    	NOISE_CONST_Z(0.723f);
    	
    	private final float num;
    	
    	NoiseConstants(float num) {
    		this.num = num;
    	}
    	
    	public static float get(char c) {
    		return NoiseConstants.values()[c - 97].num;
    	}
    	
    }
    public static final float NOISE_CONST_A = 0.321f;
    public static final float NOISE_CONST_B = 0.140f;
    public static final float NOISE_CONST_C = 0.470f;
    public static final float NOISE_CONST_D = 0.856f;
    public static final float NOISE_CONST_E = 0.138f;
    public static final float NOISE_CONST_F = 0.243f;
    public static final float NOISE_CONST_G = 0.368f;
    public static final float NOISE_CONST_H = 0.753f;
    public static final float NOISE_CONST_I = 0.472f;
    public static final float NOISE_CONST_J = 0.568f;
    public static final float NOISE_CONST_K = 0.187f;
    public static final float NOISE_CONST_L = 0.019f;
    public static final float NOISE_CONST_M = 0.034f;
    public static final float NOISE_CONST_N = 0.720f;
    public static final float NOISE_CONST_O = 0.964f;
    public static final float NOISE_CONST_P = 0.112f;
    public static final float NOISE_CONST_Q = 0.026f;
    public static final float NOISE_CONST_R = 0.285f;
    public static final float NOISE_CONST_S = 0.460f;
    public static final float NOISE_CONST_T = 0.103f;
    public static final float NOISE_CONST_U = 0.616f;
    public static final float NOISE_CONST_V = 0.860f;
    public static final float NOISE_CONST_W = 0.382f;
    public static final float NOISE_CONST_X = 0.575f;
    public static final float NOISE_CONST_Y = 0.559f;
    public static final float NOISE_CONST_Z = 0.723f;
	
	/**
     * A constructor that takes only a parameter for the Noise's seed, which should produce different results for
     * any different seeds. An example call to this would be {@code new Noise(1337)}, which makes noise with the
     * seed 1337, a default frequency of 1.0f/32.0f, 1 octave of Simplex noise (since this doesn't specify octave count,
     * it always uses 1 even for the SIMPLEX_FRACTAL noiseType this uses, but you can call
     * {@link #setFractalOctaves(int)} later to benefit from the fractal noiseType), and normal lacunarity and gain
     * (when unspecified, they are 2f and 0.5f).
     * @param seed the int seed for the noise, which should significantly affect the produced noise
     */
	/**
     * A constructor that takes a few parameters to specify the Noise from the start. An example call to this
     * would be {@code new Noise(1337, 0.02f, Noise.SIMPLEX)}, which makes noise with the seed 1337, a lower
     * frequency, 1 octave of Simplex noise (since this doesn't specify octave count, it always uses 1 even for
     * noiseTypes like SIMPLEX_FRACTAL, but using a fractal noiseType can make sense if you call
     * {@link #setFractalOctaves(int)} later), and normal lacunarity and gain (when unspecified, they are 2f and 0.5f).
     * @param seed the int seed for the noise, which should significantly affect the produced noise
     * @param frequency the multiplier for all dimensions, which is usually fairly small (1.0f/32.0f is the default)
     * @param noiseType the noiseType, which should be a constant from this class (see {@link #setNoiseType(int)})
     */
    public FractalNoise(int seed, float frequency, int octaves)
    {
        this(seed, frequency, octaves, 2f, 0.5f, 1f);
    }
    
    public FractalNoise(int seed, float frequency, int octaves, float lacunarity,
    		float gain, float foamSharpness)
    {
        this.seed = seed;
        this.frequency = Math.max(0.0001f, frequency);
        this.octaves = octaves;
        this.lacunarity = lacunarity;
        this.gain = gain;
        this.foamSharpness = foamSharpness;
        calculateFractalBounding();
    }
    
    private void calculateFractalBounding() {
        float amp = gain;
        float ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        fractalBounding = 1 / ampFractal;
    }
    
    public float singleFoam(int seed, float x, float y) {
        final float p0 = x;
        final float p1 = x * -0.5f + y * 0.8660254037844386f;
        final float p2 = x * -0.5f + y * -0.8660254037844387f;

        float xin = p2;
        float yin = p0;
        final float a = valueNoise(seed, xin, yin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p1;
        yin = p2;
        final float b = valueNoise(seed, xin + a, yin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        final float c = valueNoise(seed, xin + b, yin);
        final float result = (a + b + c) * F3f;
        final float sharp = foamSharpness * 2.2f;
        final float diff = 0.5f - result;
        final int sign = Float.floatToIntBits(diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }
	
	/**
     * Produces noise from 0 to 1, instead of the normal -1 to 1.
     * @param seed
     * @param x
     * @param y
     * @return noise from 0 to 1.
     */
    protected float valueNoise (int seed, float x, float y) {
        int xFloor = x >= 0 ? (int) x : (int) x - 1;
        x -= xFloor;
        x *= x * (3 - 2 * x);
        int yFloor = y >= 0 ? (int) y : (int) y - 1;
        y -= yFloor;
        y *= y * (3 - 2 * y);
        xFloor *= 0xD1B55;
        yFloor *= 0xABC99;
        return ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, seed) + x * hashPart1024(xFloor + 0xD1B55, yFloor, seed))
            + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xABC99, seed) + x * hashPart1024(xFloor + 0xD1B55, yFloor + 0xABC99, seed)))
            * 0x1p-10f + 0.5f;
    }
    
    private static int hashPart1024(final int x, final int y, int s) {
        s += x ^ y;
        return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >> 22;
    }
	
	public float singleFoamFractalBillow(float x, float y) {
		x *= frequency;
		y *= frequency;
		
        int seed = this.seed;
        float sum = Math.abs(singleFoam(seed, x, y)) * 2 - 1;
        float amp = 1, t;

        for (int i = 1; i < octaves; i++) {
            t = x;
            x = y * lacunarity;
            y = t * lacunarity;

            amp *= gain;
            sum += (Math.abs(singleFoam(++seed, x, y)) * 2 - 1) * amp;
        }

        return sum * fractalBounding;
	}

	public float singleSimplexFractalFBM(float x, float y) {
        int seed = this.seed;
        x *= frequency;
        y *= frequency;
        float sum = singleSimplex(seed, x, y);
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += singleSimplex(seed + i, x, y) * amp;
        }

        return sum * fractalBounding;
    }
	
	public float singleSimplexFractalRidgedMulti(float x, float y) {
        int seed = this.seed;
        x *= frequency;
        y *= frequency;
        
        float sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleSimplex(seed + i, x, y));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }
	
	public float singleSimplex(int seed, float x, float y) {
        float t = (x + y) * F2f;
        int i = fastFloor(x + t);
        int j = fastFloor(y + t);

        t = (i + j) * G2f;
        float X0 = i - t;
        float Y0 = j - t;

        float x0 = x - X0;
        float y0 = y - Y0;

        int i1, j1;
        if (x0 > y0) {
            i1 = 1;
            j1 = 0;
        } else {
            i1 = 0;
            j1 = 1;
        }

        float x1 = x0 - i1 + G2f;
        float y1 = y0 - j1 + G2f;
        float x2 = x0 - 1 + H2f;
        float y2 = y0 - 1 + H2f;

        float n = 0f;

        t = 0.5f - x0 * x0 - y0 * y0;
        if (t >= 0) {
            t *= t;
            n += t * t * gradCoord2D(seed, i, j, x0, y0);
        }

        t = 0.5f - x1 * x1 - y1 * y1;
        if (t > 0) {
            t *= t;
            n += t * t * gradCoord2D(seed, i + i1, j + j1, x1, y1);
        }

        t = 0.5f - x2 * x2 - y2 * y2;
        if (t > 0)  {
            t *= t;
            n += t * t * gradCoord2D(seed, i + 1, j + 1, x2, y2);
        }
        return n * 99.20689070704672f; // this is 99.83685446303647 / 1.00635 ; the first number was found by kdotjpg
    }
	
	private float gradCoord2D(int seed, int x, int y, float xd, float yd) {
        final int hash = hash256(x, y, seed) << 1;
        return xd * GRAD_2D[hash] + yd * GRAD_2D[hash + 1];
    }
	
	public static int hash256(int x, int y, int s) {
        s ^= x * 0x1827F5 ^ y * 0x123C21;
        return (s ^ (s << 19 | s >>> 13) ^ (s << 5 | s >>> 27) ^ 0xD1B54A35) * 0x125493 >>> 24;
    }
	
	protected static int fastFloor(float f) {
        return (f >= 0 ? (int) f : (int) f - 1);
    }
	
	protected static final float[] GRAD_2D = {
            +0.6499429579167653f, +0.7599829941876370f,
            -0.1551483029088119f, +0.9878911904175052f,
            -0.8516180517334043f, +0.5241628506120981f,
            -0.9518580082090311f, -0.3065392833036837f,
            -0.3856887670108717f, -0.9226289476282616f,
            +0.4505066120763985f, -0.8927730912586049f,
            +0.9712959670388622f, -0.2378742197339624f,
            +0.8120673355833279f, +0.5835637432865366f,
            +0.0842989251943661f, +0.9964405106232257f,
            -0.7024883500032670f, +0.7116952424385647f,
            -0.9974536374007479f, -0.0713178886116052f,
            -0.5940875849508908f, -0.8044003613917750f,
            +0.2252075529515288f, -0.9743108118529653f,
            +0.8868317111719171f, -0.4620925405802277f,
            +0.9275724981153959f, +0.3736432265409930f,
            +0.3189067150428103f, +0.9477861083074618f,
            -0.5130301507665112f, +0.8583705868705491f,
            -0.9857873824221494f, +0.1679977281313266f,
            -0.7683809836504446f, -0.6399927061806058f,
            -0.0130202362193748f, -0.9999152331316848f,
            +0.7514561619680513f, -0.6597830223946701f,
            +0.9898275175279653f, +0.1422725748147741f,
            +0.5352066871710182f, +0.8447211386057674f,
            -0.2941198828144364f, +0.9557685360657266f,
            -0.9175289804081126f, +0.3976689202229027f,
            -0.8985631161871687f, -0.4388443075032474f,
            -0.2505005588110731f, -0.9681164547900940f,
            +0.5729409678802212f, -0.8195966369650838f,
            +0.9952584535626074f, -0.0972656702653466f,
            +0.7207814785200723f, +0.6931623620930514f,
            -0.0583247612407003f, +0.9982976621360060f,
            -0.7965970142012075f, +0.6045107087270838f,
            -0.9771604781144960f, -0.2125027058911242f,
            -0.4736001288089817f, -0.8807399831914728f,
            +0.3615343409387538f, -0.9323587937709286f,
            +0.9435535266854258f, -0.3312200813348966f,
            +0.8649775992346886f, +0.5018104750024599f,
            +0.1808186720712497f, +0.9835164502083277f,
            -0.6299339540895539f, +0.7766487066139361f,
            -0.9996609468975833f, +0.0260382650694516f,
            -0.6695112313914258f, -0.7428019325774111f,
            +0.1293727267195084f, -0.9915960354807594f,
            +0.8376810167470904f, -0.5461597881403947f,
            +0.9595170289111490f, +0.2816506190824391f,
            +0.4095816551369482f, +0.9122734610714476f,
            -0.4271076040148479f, +0.9042008043530463f,
            -0.9647728141412515f, +0.2630844295924223f,
            -0.8269869890664444f, -0.5622210596507540f,
            -0.1102159255238020f, -0.9939076666174438f,
            +0.6837188597775012f, -0.7297455178242300f,
            +0.9989724417383330f, +0.0453217458550843f,
            +0.6148313475439905f, +0.7886586169422362f,
            -0.1997618324529528f, +0.9798444827088829f,
            -0.8744989400706802f, +0.4850274258382270f,
            -0.9369870231562731f, -0.3493641630687752f,
            -0.3434772946489506f, -0.9391609809082988f,
            +0.4905057254335028f, -0.8714379687143274f,
            +0.9810787787756657f, -0.1936089611460388f,
            +0.7847847614201463f, +0.6197684069414349f,
            +0.0390518795551629f, +0.9992371844077906f,
            -0.7340217731995672f, +0.6791259356474049f,
            -0.9931964444524306f, -0.1164509455824639f,
            -0.5570202966000876f, -0.8304988796955420f,
            +0.2691336060685578f, -0.9631028512493016f,
            +0.9068632806061000f, -0.4214249521425399f,
            +0.9096851999779008f, +0.4152984913783901f,
            +0.2756236986873733f, +0.9612656119522284f,
            -0.5514058359842319f, +0.8342371389734039f,
            -0.9923883787916933f, +0.1231474954645637f,
            -0.7385858406439617f, -0.6741594440488484f,
            +0.0323110469045428f, -0.9994778618098213f,
            +0.7805865154410089f, -0.6250477517051506f,
            +0.9823623706068018f, +0.1869870926448790f,
            +0.4963724943556111f, +0.8681096398768929f,
            -0.3371347561867868f, +0.9414564016304079f,
            -0.9346092156607797f, +0.3556762769737983f,
            -0.8777506000588920f, -0.4791178185960681f,
            -0.2063664269701996f, -0.9784747813917093f,
            +0.6094977881394418f, -0.7927877687333024f,
            +0.9986440175043460f, -0.0520588734297966f,
            +0.6886255051458764f, +0.7251171723677399f,
            -0.1035094220814735f, +0.9946284731196666f,
            -0.8231759450656516f, +0.5677863713275190f,
            -0.9665253951623188f, -0.2565709658288005f,
            -0.4331968034012919f, -0.9012993562201753f,
            +0.4034189716368784f, -0.9150153732716426f,
            +0.9575954428121146f, -0.2881162402667889f,
            +0.8413458575409575f, +0.5404971304259356f,
            +0.1360581877502697f, +0.9907008476558967f,
            -0.6644857355505560f, +0.7473009482463117f,
            -0.9998138366647180f, -0.0192948701414780f,
            -0.6351581891853917f, -0.7723820781910558f,
            +0.1741806522163015f, -0.9847137149413040f,
            +0.8615731658120597f, -0.5076334109892543f,
            +0.9457661714829020f, +0.3248481935898273f,
            +0.3678149601703667f, +0.9298990026206456f,
            -0.4676486851245607f, +0.8839144230643990f,
            -0.9757048995218635f, +0.2190889067228882f,
            -0.8006563717736747f, -0.5991238388999518f,
            -0.0650570415691071f, -0.9978815467490495f,
            +0.7160896397121960f, -0.6980083293893113f,
            +0.9958918787052943f, +0.0905503502413954f,
            +0.5784561871098056f, +0.8157134543418942f,
            -0.2439648281544816f, +0.9697840804135497f,
            -0.8955826311865743f, +0.4448952131872543f,
            -0.9201904205900768f, -0.3914710587696841f,
            -0.3005599364234082f, -0.9537629289384008f,
            +0.5294967923694863f, -0.8483119396014800f,
            +0.9888453593035162f, -0.1489458135829932f,
            +0.7558893631265085f, +0.6546993743025888f,
            -0.0062754222469803f, +0.9999803093439501f,
            -0.7640466961212760f, +0.6451609459244744f,
            -0.9868981170802014f, -0.1613446822909051f,
            -0.5188082666339063f, -0.8548906260290385f,
            +0.3125065582647844f, -0.9499156020623616f,
            +0.9250311403279032f, -0.3798912863223621f,
            +0.8899283927548960f, +0.4561002694240463f,
            +0.2317742435145519f, +0.9727696027545563f,
            -0.5886483179573486f, +0.8083892365475831f,
            -0.9969499014064180f, +0.0780441803450664f,
            -0.7072728176724660f, -0.7069407057042696f,
            +0.0775759270620736f, -0.9969864470194466f,
            +0.8081126726681943f, -0.5890279350532263f,
            +0.9728783545459001f, +0.2313173302112532f,
            +0.4565181982253288f, +0.8897140746830408f,
            -0.3794567783511009f, +0.9252094645881026f,
            -0.9497687200714887f, +0.3129526775309106f,
            -0.8551342041690687f, -0.5184066867432686f,
            -0.1618081880753845f, -0.9868222283024238f,
            +0.6448020194233159f, -0.7643496292585048f,
            +0.9999772516247822f, -0.0067450895432855f,
            +0.6550543261176665f, +0.7555817823601425f,
            -0.1484813589986064f, +0.9889152066936411f,
            -0.8480631534437840f, +0.5298951667745091f,
            -0.9539039899003245f, -0.3001119425351840f,
            -0.3919032080850608f, -0.9200064540494471f,
            +0.4444745293405786f, -0.8957914895596358f,
            +0.9696693887216105f, -0.2444202867526717f,
            +0.8159850520735595f, +0.5780730012658526f,
            +0.0910180879994953f, +0.9958492394217692f,
            -0.6976719213969089f, +0.7164173993520435f,
            -0.9979119924958648f, -0.0645883521459785f,
            -0.5994998228898376f, -0.8003748886334786f,
            +0.2186306161766729f, -0.9758076929755208f,
            +0.8836946816279001f, -0.4680637880274058f,
            +0.9300716543684309f, +0.3673781672069940f,
            +0.3252923626016029f, +0.9456134933645286f,
            -0.5072286936943775f, +0.8618114946396893f,
            -0.9846317976415725f, +0.1746431306210620f,
            -0.7726803123417516f, -0.6347953488483143f,
            -0.0197644578133314f, -0.9998046640256011f,
            +0.7469887719961158f, -0.6648366525032559f,
            +0.9907646418168752f, +0.1355928631067248f,
            +0.5408922318074902f, +0.8410919055432124f,
            -0.2876664477065717f, +0.9577306588304888f,
            -0.9148257956391065f, +0.4038486890325085f,
            -0.9015027194859215f, -0.4327734358292892f,
            -0.2570248925062563f, -0.9664047830139022f,
            +0.5673996816983953f, -0.8234425306046317f,
            +0.9945797473944409f, -0.1039765650173647f,
            +0.7254405241129018f, +0.6882848581617921f,
            -0.0515898273251730f, +0.9986683582233687f,
            -0.7925014140531963f, +0.6098700752813540f,
            -0.9785715990807187f, -0.2059068368767903f,
            -0.4795300252265173f, -0.8775254725113429f,
            +0.3552372730694574f, -0.9347761656258549f,
            +0.9412979532686209f, -0.3375768996425928f,
            +0.8683426789873530f, +0.4959647082697184f,
            +0.1874484652642005f, +0.9822744386728669f,
            -0.6246810590458048f, +0.7808800000444446f,
            -0.9994625758058275f, +0.0327804753409776f,
            -0.6745062666468870f, -0.7382691218343610f,
            +0.1226813796500722f, -0.9924461089082646f,
            +0.8339780641890598f, -0.5517975973592748f,
            +0.9613949601033843f, +0.2751721837101493f,
            +0.4157257040026583f, +0.9094900433932711f,
            -0.4209989726203348f, +0.9070611142875780f,
            -0.9629763390922247f, +0.2695859238694348f,
            -0.8307604078465821f, -0.5566301687427484f,
            -0.1169174144996730f, -0.9931416405461567f,
            +0.6787811074228051f, -0.7343406622310046f,
            +0.9992554159724470f, +0.0385825562881973f,
            +0.6201369341201711f, +0.7844935837468874f,
            -0.1931481494214682f, +0.9811696042861612f,
            -0.8712074932224428f, +0.4909149659086258f,
            -0.9393222007870077f, -0.3430361542296271f,
            -0.3498042060103595f, -0.9368228314134226f,
            +0.4846166400948296f, -0.8747266499559725f,
            +0.9797505510481769f, -0.2002220210685972f,
            +0.7889473022428521f, +0.6144608647291752f,
            +0.0457909354721791f, +0.9989510449609544f,
            -0.7294243101497431f, +0.6840615292227530f,
            -0.9939593229024027f, -0.1097490975607407f,
            -0.5626094146025390f, -0.8267228354174018f,
            +0.2626312687452330f, -0.9648962724963078f,
            +0.9040001019019392f, -0.4275322394408211f,
            +0.9124657316291773f, +0.4091531358824348f,
            +0.2821012513235693f, +0.9593846381935018f,
            -0.5457662881946498f, +0.8379374431723614f,
            -0.9915351626845509f, +0.1298384425357957f,
            -0.7431163048326799f, -0.6691622803863227f,
            +0.0255687442062853f, -0.9996730662170076f,
            +0.7763527553119807f, -0.6302986588273021f,
            +0.9836012681423212f, +0.1803567168386515f,
            +0.5022166799422209f, +0.8647418148718223f,
            -0.3307768791887710f, +0.9437089891455613f,
            -0.9321888864830543f, +0.3619722087639923f,
            -0.8809623252471085f, -0.4731864130500873f,
            -0.2129616324856343f, -0.9770605626515961f,
            +0.6041364985661350f, -0.7968808512571063f,
            +0.9982701582127194f, -0.0587936324949578f,
            +0.6935008202914851f, +0.7204558364362367f,
            -0.0967982092968079f, +0.9953040272584711f,
            -0.8193274492343137f, +0.5733258505694586f,
            -0.9682340024187017f, -0.2500458289199430f,
            -0.4392662937408502f, -0.8983569018954422f,
            +0.3972379338845546f, -0.9177156552457467f,
            +0.9556302892322005f, -0.2945687530984589f,
            +0.8449724198323217f, +0.5348098818484104f,
            +0.1427374585755972f, +0.9897605861618151f,
            -0.6594300077680133f, +0.7517659641504648f,
            -0.9999212381512442f, -0.0125505973595986f,
            -0.6403535266476091f, -0.7680803088935230f,
            +0.1675347077076747f, -0.9858661784001437f,
            +0.8581295336101056f, -0.5134332513054668f,
            +0.9479357869928937f, +0.3184615263075951f,
            +0.3740788450165170f, +0.9273969040875156f,
            -0.4616759649446430f, +0.8870486477034012f,
            -0.9742049295269273f, +0.2256651397213017f,
            -0.8046793020829978f, -0.5937097108850584f,
            -0.0717863620135296f, -0.9974200309943962f,
            +0.7113652211526822f, -0.7028225395748172f,
            +0.9964799940037152f, +0.0838309104707540f,
            +0.5839450884626246f, +0.8117931594072332f,
            -0.2374179978909748f, +0.9714075840127259f,
            -0.8925614000865144f, +0.4509258775847768f,
            -0.9228099950981292f, -0.3852553866553855f,
            -0.3069863155319683f, -0.9517139286971200f,
            +0.5237628071845146f, -0.8518641451605984f,
            +0.9878182118285335f, -0.1556122758007173f,
            +0.7602881737752754f, +0.6495859395164404f,
            +0.0004696772366984f, +0.9999998897016406f,
            -0.7596776469502666f, +0.6502998329417794f,
            -0.9879639510809196f, -0.1546842957917130f,
            -0.5245627784110601f, -0.8513717704420726f,
            +0.3060921834538644f, -0.9520018777441807f,
            +0.9224476966294768f, -0.3861220622846781f,
            +0.8929845854878761f, +0.4500872471877493f,
            +0.2383303891026603f, +0.9711841358002995f,
            -0.5831822693781987f, +0.8123413326200348f,
            -0.9964008074312266f, +0.0847669213219385f,
            -0.7120251067268070f, -0.7021540054650968f,
            +0.0708493994771745f, -0.9974870237721009f,
            +0.8041212432524677f, -0.5944653279629567f,
            +0.9744164792492415f, +0.2247499165016809f,
            +0.4625090142797330f, +0.8866145790082576f,
    };
}
