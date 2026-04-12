package io.ruin.cache.runetek5.vartype.constants;

import io.ruin.api.utils.StringUtils;

public enum ScriptVarType {
	INT(0, 'i', BaseVarType.INTEGER, 0),
	BOOLEAN(1, '1', BaseVarType.INTEGER, 0),
	TYPE_2(2, '2', BaseVarType.INTEGER, -1),
	QUEST(3, ':', BaseVarType.INTEGER, -1),
	TYPE_4(4, ';', BaseVarType.INTEGER, -1),
	CURSOR(5, '@', BaseVarType.INTEGER, -1),
	SEQ(6, 'A', BaseVarType.INTEGER, -1),
	COLOUR(7, 'C', BaseVarType.INTEGER, -1),
	LOC_SHAPE(8, 'H', BaseVarType.INTEGER, -1, "locshape"),
	COMPONENT(9, 'I', BaseVarType.INTEGER, -1),
	IDKIT(10, 'K', BaseVarType.INTEGER, -1),
	MIDI(11, 'M', BaseVarType.INTEGER, -1),
	NPC_MODE(12, 'N', BaseVarType.INTEGER, -1),
	NAMEDOBJ(13, 'O', BaseVarType.INTEGER, -1),
	SYNTH(14, 'P', BaseVarType.INTEGER, -1),
	TYPE_15(15, 'Q', BaseVarType.INTEGER, -1),
	AREA(16, 'R', BaseVarType.INTEGER, -1),
	STAT(17, 'S', BaseVarType.INTEGER, -1),
	NPC_STAT(18, 'T', BaseVarType.INTEGER, -1),
	WRITEINV(19, 'V', BaseVarType.INTEGER, -1),
	MESH(20, '^', BaseVarType.INTEGER, -1),
	MAPAREA(21, '`', BaseVarType.INTEGER, -1, "wma"),
	COORDGRID(22, 'c', BaseVarType.INTEGER, -1, "coord"),
	GRAPHIC(23, 'd', BaseVarType.INTEGER, -1),
	CHATPHRASE(24, 'e', BaseVarType.INTEGER, -1),
	FONTMETRICS(25, 'f', BaseVarType.INTEGER, -1),
	ENUM(26, 'g', BaseVarType.INTEGER, -1),
	TYPE_27(27, 'h', BaseVarType.INTEGER, -1),
	JINGLE(28, 'j', BaseVarType.INTEGER, -1),
	CHATCAT(29, 'k', BaseVarType.INTEGER, -1),
	LOC(30, 'l', BaseVarType.INTEGER, -1),
	MODEL(31, 'm', BaseVarType.INTEGER, -1),
	NPC(32, 'n', BaseVarType.INTEGER, -1),
	OBJ(33, 'o', BaseVarType.INTEGER, -1, "namedobj"),
	PLAYER_UID(34, 'p', BaseVarType.INTEGER, -1),
	NEWVAR(35, '-', BaseVarType.INTEGER, -1),
	STRING(36, 's', BaseVarType.STRING, ""),
	SPOTANIM(37, 't', BaseVarType.INTEGER, -1),
	NPC_UID(38, 'u', BaseVarType.INTEGER, -1),
	INV(39, 'v', BaseVarType.INTEGER, -1),
	TEXTURE(40, 'x', BaseVarType.INTEGER, -1),
	CATEGORY(41, 'y', BaseVarType.INTEGER, -1),
	CHAR(42, 'z', BaseVarType.INTEGER, -1),
	LASER(43, '|', BaseVarType.INTEGER, -1),
	BAS(44, '€', BaseVarType.INTEGER, -1),
	TYPE_45(45, 'ƒ', BaseVarType.INTEGER, -1),
	COLLISION_GEOMETRY(46, '‡', BaseVarType.INTEGER, -1),
	PHYSICS_MODEL(47, '‰', BaseVarType.INTEGER, -1),
	PHYSICS_CONTROL_MODIFIER(48, 'Š', BaseVarType.INTEGER, -1),
	CLANHASH(49, 'Œ', BaseVarType.LONG, -1L),

	CUTSCENE(51, 'š', BaseVarType.INTEGER, -1),

	ITEMCODE(53, '¡', BaseVarType.INTEGER, -1),
	TYPE_54(54, '¢', BaseVarType.INTEGER, -1),
	MAPSCENEICON(55, '£', BaseVarType.INTEGER, -1, "msi"),
	CLANFORUMQFC(56, '§', BaseVarType.LONG, -1L),
	VORBIS(57, '«', BaseVarType.INTEGER, -1),
	VERIFY_OBJECT(58, '®', BaseVarType.INTEGER, -1, "verifyobj"),
	MAPELEMENT(59, 'µ', BaseVarType.INTEGER, -1),
	CATEGORYTYPE(60, '¶', BaseVarType.INTEGER, -1),
	SOCIAL_NETWORK(61, 'Æ', BaseVarType.INTEGER, -1, "socialnetwork"),
	HITMARK(62, '×', BaseVarType.INTEGER, -1),
	PACKAGE(63, 'Þ', BaseVarType.INTEGER, -1),
	PARTICLE_EFFECTOR(64, 'á', BaseVarType.INTEGER, -1, "pef"),
	TYPE_65(65, 'æ', BaseVarType.INTEGER, -1),
	PARTICLE_EMITTER(66, 'é', BaseVarType.INTEGER, -1, "pem"),
	PLOGTYPE(67, 'í', BaseVarType.INTEGER, -1, "plog"),
	UNSIGNED_INT(68, 'î', BaseVarType.INTEGER, -1),
	SKYBOX(69, 'ó', BaseVarType.INTEGER, -1),
	SKYDECOR(70, 'ú', BaseVarType.INTEGER, -1),
	HASH64(71, 'û', BaseVarType.LONG, -1L),
	INPUTTYPE(72, 'Î', BaseVarType.INTEGER, -1),
	STRUCT(73, 'J', BaseVarType.INTEGER, -1),
	DBROW(74, 'Ð', BaseVarType.INTEGER, -1),
	TYPE_75(75, '¤', BaseVarType.INTEGER, -1),
	TYPE_76(76, '¥', BaseVarType.INTEGER, -1),
	TYPE_77(77, 'è', BaseVarType.INTEGER, -1),
	TYPE_78(78, '¹', BaseVarType.INTEGER, -1),
	TYPE_79(79, '°', BaseVarType.INTEGER, -1),
	REGION_VISIBILITY(80, 'ì', BaseVarType.INTEGER, -1, "region_visibility"),
	TYPE_81(81, 'ë', BaseVarType.INTEGER, -1),

	TYPE_83(83, 'þ', BaseVarType.INTEGER, -1),
	TYPE_84(84, 'ý', BaseVarType.INTEGER, -1),
	TYPE_85(85, 'ÿ', BaseVarType.INTEGER, -1),
	TYPE_86(86, 'õ', BaseVarType.INTEGER, -1),
	TYPE_87(87, 'ô', BaseVarType.INTEGER, -1),
	TYPE_88(88, 'ö', BaseVarType.INTEGER, -1),
	GWC_PLATFORM(89, 'ò', BaseVarType.INTEGER, -1, "gwc_platform"),
	TYPE_90(90, 'Ü', BaseVarType.INTEGER, -1),
	TYPE_91(91, 'ù', BaseVarType.INTEGER, -1),
	TYPE_92(92, 'ï', BaseVarType.INTEGER, -1),
	TYPE_93(93, '¯', BaseVarType.INTEGER, -1),
	BUG_TEMPLATE(94, 'ê', BaseVarType.INTEGER, -1, "bugtemplate"),
	BILLING_AUTH_FLAG(95, 'ð', BaseVarType.INTEGER, -1, "billingauthflag"),
	ACCOUNT_FEATURE_FLAG(96, 'å', BaseVarType.INTEGER, -1, "accountfeatureflag"),
	INTERFACE(97, 'a', BaseVarType.INTEGER, -1),
	TOPLEVELINTERFACE(98, 'F', BaseVarType.INTEGER, -1),
	OVERLAYINTERFACE(99, 'L', BaseVarType.INTEGER, -1),
	CLIENTINTERFACE(100, '©', BaseVarType.INTEGER, -1),
	MOVESPEED(101, 'Ý', BaseVarType.INTEGER, -1),
	MATERIAL(102, '¬', BaseVarType.INTEGER, -1),
	SEQGROUP(103, 'ø', BaseVarType.INTEGER, -1),
	TEMP_HISCORE(104, 'ä', BaseVarType.INTEGER, -1, "temphiscore"),
	TEMP_HISCORE_LENGTH_TYPE(105, 'ã', BaseVarType.INTEGER, -1, "temphiscorelengthtype"),
	TEMP_HISCORE_DISPLAY_TYPE(106, 'â', BaseVarType.INTEGER, -1, "temphiscoretype"),
	TEMP_HISCORE_CONTRIBUTE_RESULT(107, 'à', BaseVarType.INTEGER, -1, "temphiscorecontributeresult"),
	AUDIOGROUP(108, 'À', BaseVarType.INTEGER, -1),
	AUDIOMIXBUSS(109, 'Ò', BaseVarType.INTEGER, -1, "audiobuss"),
	LONG(110, 'Ï', BaseVarType.LONG, 0L),
	CRM_CHANNEL(111, 'Ì', BaseVarType.INTEGER, -1),
	HTTP_IMAGE(112, 'É', BaseVarType.INTEGER, -1),
	POP_UP_DISPLAY_BEHAVIOUR(113, 'Ê', BaseVarType.INTEGER, -1, "popupdisplaybehaviour"),
	POLL(114, '÷', BaseVarType.INTEGER, -1),
	TYPE_115(115, '¼', BaseVarType.LONG, -1L),
	TYPE_116(116, '½', BaseVarType.LONG, -1L),
	POINTLIGHT(117, '•', BaseVarType.INTEGER, -1),
	PLAYER_GROUP(118, 'Â', BaseVarType.LONG, -1L),
	PLAYER_GROUP_STATUS(119, 'Ã', BaseVarType.INTEGER, -1),
	PLAYER_GROUP_INVITE_RESULT(120, 'Å', BaseVarType.INTEGER, -1),
	PLAYER_GROUP_MODIFY_RESULT(121, 'Ë', BaseVarType.INTEGER, -1),
	PLAYER_GROUP_JOIN_OR_CREATE_RESULT(122, 'Í', BaseVarType.INTEGER, -1),
	PLAYER_GROUP_AFFINITY_MODIFY_RESULT(123, 'Õ', BaseVarType.INTEGER, -1),
	PLAYER_GROUP_DELTA_TYPE(124, '²', BaseVarType.INTEGER, -1),
	CLIENT_TYPE(125, 'ª', BaseVarType.INTEGER, -1),
	TELEMETRY_INTERVAL(126, '\u0000', BaseVarType.INTEGER, 0),
	WORLDAREA(127, '\u0000', BaseVarType.INTEGER, -1, "worldarea"),
	TYPE_128(128, '\u0000', BaseVarType.INTEGER, -1),
	TYPE_200(200, 'X', BaseVarType.INTEGER, -1),
	TYPE_201(201, 'W', BaseVarType.INTEGER, -1),
	TYPE_202(202, 'b', BaseVarType.INTEGER, -1),
	TYPE_203(203, 'B', BaseVarType.INTEGER, -1),
	TYPE_204(204, '4', BaseVarType.INTEGER, -1),
	TYPE_205(205, 'w', BaseVarType.INTEGER, -1),
	TYPE_206(206, 'q', BaseVarType.INTEGER, -1),
	TYPE_207(207, '0', BaseVarType.INTEGER, -1),
	TYPE_208(208, '6', BaseVarType.INTEGER, -1),
	TYPE_209(BaseVarType.INTEGER, Integer.valueOf(-1), '#'),
	TYPE_210(BaseVarType.INTEGER, Integer.valueOf(-1), '('),
	TYPE_211(BaseVarType.INTEGER, Integer.valueOf(-1), '%'),
	TYPE_212(BaseVarType.INTEGER, Integer.valueOf(-1), '&'),
	TYPE_213(BaseVarType.INTEGER, Integer.valueOf(-1), ')'),
	TYPE_214(BaseVarType.INTEGER, Integer.valueOf(-1), '3'),
	TYPE_215(BaseVarType.INTEGER, Integer.valueOf(-1), '5'),
	TYPE_216(BaseVarType.INTEGER, Integer.valueOf(-1), '7'),
	TYPE_217(BaseVarType.INTEGER, Integer.valueOf(-1), '8'),
	TYPE_218(BaseVarType.INTEGER, Integer.valueOf(-1), '9'),
	TYPE_219(BaseVarType.INTEGER, Integer.valueOf(-1), 'D'),
	TYPE_220(BaseVarType.INTEGER, Integer.valueOf(-1), 'G'),
	TYPE_221(BaseVarType.INTEGER, Integer.valueOf(-1), 'U'),
	TYPE_222(BaseVarType.INTEGER, Integer.valueOf(-1), 'Á'),
	TYPE_223(BaseVarType.INTEGER, Integer.valueOf(-1), 'Z'),
	TYPE_224(BaseVarType.INTEGER, Integer.valueOf(-1), '~'),
	TYPE_225(BaseVarType.INTEGER, Integer.valueOf(-1), '±'),
	TYPE_226(BaseVarType.INTEGER, Integer.valueOf(-1), '»'),
	TYPE_227(BaseVarType.INTEGER, Integer.valueOf(-1), '¿'),
	TYPE_228(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ç'),
	TYPE_229(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ø'),
	TYPE_230(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ñ'),
	TYPE_231(BaseVarType.INTEGER, Integer.valueOf(-1), 'ñ'),
	TYPE_232(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ù'),
	TYPE_233(BaseVarType.INTEGER, Integer.valueOf(-1), 'ß'),
	TYPE_234(BaseVarType.INTEGER, Integer.valueOf(-1), 'E'),
	INT_ARRAY(BaseVarType.INTEGER, Integer.valueOf(-1), 'Y'),
	TYPE_235(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ä'),
	TYPE_236(BaseVarType.INTEGER, Integer.valueOf(-1), 'ü'),
	TYPE_237(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ú'),
	TYPE_238(BaseVarType.INTEGER, Integer.valueOf(-1), 'Û'),
	TYPE_239(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ó'),
	TYPE_240(BaseVarType.INTEGER, Integer.valueOf(-1), 'È'),
	TYPE_241(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ô'),
	TYPE_242(BaseVarType.INTEGER, Integer.valueOf(-1), '¾'),
	TYPE_243(BaseVarType.INTEGER, Integer.valueOf(-1), 'Ö'),
	TYPE_244(BaseVarType.INTEGER, Integer.valueOf(-1), '³'),
	TYPE_245(BaseVarType.INTEGER, Integer.valueOf(-1), '·'),
	TYPE_246(BaseVarType.INTEGER, Integer.valueOf(-1), '\u0000'),
	TYPE_247(BaseVarType.INTEGER, Integer.valueOf(-1), '\u0000'),
	TYPE_248(BaseVarType.INTEGER, Integer.valueOf(-1), '\u0000'),
	TYPE_249(BaseVarType.INTEGER, Integer.valueOf(-1), 'º'),
	TYPE_250(BaseVarType.INTEGER, Integer.valueOf(-1), '\u0000'),
	TYPE_251(BaseVarType.INTEGER, Integer.valueOf(-1), '\u0000'),
	TYPE_252(BaseVarType.INTEGER, Integer.valueOf(-1), '\u0000'),
	TYPE_253(null, Integer.valueOf(-1), '!'),
	TYPE_254(null, Integer.valueOf(-1), '$'),
	TYPE_255(null, Integer.valueOf(-1), '?'),
	TYPE_256(null, Integer.valueOf(-1), 'ç'),
	TYPE_257(null, Integer.valueOf(-1), '*');

	public static ScriptVarType[] legacy_lut;

	public final char code;
	public final int id;
	public final BaseVarType base_type;
	public final Object default_value;

	ScriptVarType(BaseVarType base_type, Object default_value, char type_code) {
		this(-1, type_code, base_type, default_value);
	}

	ScriptVarType(int id, char code, BaseVarType base_type, Object default_value) {
		this(id, code, base_type, default_value, null);
	}

	ScriptVarType(int id, char code, BaseVarType base_type, Object default_value, String name) {
		this.id = id;
		this.code = code;
		this.base_type = base_type;
		this.default_value = default_value;
		if (name != null && name.length() > 0) {

		}
		register_legacy(this);
	}

	private static void register_legacy(ScriptVarType type) {
		if (legacy_lut == null) {
			legacy_lut = new ScriptVarType[256];
		}
		legacy_lut[StringUtils.charToCp1252(type.code) & 0xFF] = type;
	}

	public int getId() {
		return id;
	}

	public BaseVarType getBaseType() {
		return base_type;
	}

	public Object getDefaultValue() {
		return default_value;
	}

	public static ScriptVarType lookupByLegacy(char ch) {
		if (ch == 'O') {
			return OBJ;
		}
		return legacy_lut[StringUtils.charToCp1252(ch) & 0xFF];
	}
}
