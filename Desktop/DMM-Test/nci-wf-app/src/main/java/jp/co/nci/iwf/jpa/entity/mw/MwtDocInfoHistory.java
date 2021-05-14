package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the MWT_DOC_INFO_HISTORY database table.
 *
 */
@Entity
@Table(name="MWT_DOC_INFO_HISTORY")
@NamedQuery(name="MwtDocInfoHistory.findAll", query="SELECT m FROM MwtDocInfoHistory m")
public class MwtDocInfoHistory extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_HISTORY_ID")
	private long docHistoryId;

	@Column(name="BINDER_ID")
	private Long binderId;

	@Column(name="BIZ_DOC_ID")
	private Long bizDocId;

	private String comments;

	@Column(name="CONTENTS_TYPE")
	private String contentsType;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DISP_COUNT")
	private Integer dispCount;

	@Column(name="DOC_BUSINESS_INFO_001")
	private String docBusinessInfo001;

	@Column(name="DOC_BUSINESS_INFO_002")
	private String docBusinessInfo002;

	@Column(name="DOC_BUSINESS_INFO_003")
	private String docBusinessInfo003;

	@Column(name="DOC_BUSINESS_INFO_004")
	private String docBusinessInfo004;

	@Column(name="DOC_BUSINESS_INFO_005")
	private String docBusinessInfo005;

	@Column(name="DOC_BUSINESS_INFO_006")
	private String docBusinessInfo006;

	@Column(name="DOC_BUSINESS_INFO_007")
	private String docBusinessInfo007;

	@Column(name="DOC_BUSINESS_INFO_008")
	private String docBusinessInfo008;

	@Column(name="DOC_BUSINESS_INFO_009")
	private String docBusinessInfo009;

	@Column(name="DOC_BUSINESS_INFO_010")
	private String docBusinessInfo010;

	@Column(name="DOC_BUSINESS_INFO_011")
	private String docBusinessInfo011;

	@Column(name="DOC_BUSINESS_INFO_012")
	private String docBusinessInfo012;

	@Column(name="DOC_BUSINESS_INFO_013")
	private String docBusinessInfo013;

	@Column(name="DOC_BUSINESS_INFO_014")
	private String docBusinessInfo014;

	@Column(name="DOC_BUSINESS_INFO_015")
	private String docBusinessInfo015;

	@Column(name="DOC_BUSINESS_INFO_016")
	private String docBusinessInfo016;

	@Column(name="DOC_BUSINESS_INFO_017")
	private String docBusinessInfo017;

	@Column(name="DOC_BUSINESS_INFO_018")
	private String docBusinessInfo018;

	@Column(name="DOC_BUSINESS_INFO_019")
	private String docBusinessInfo019;

	@Column(name="DOC_BUSINESS_INFO_020")
	private String docBusinessInfo020;

	@Column(name="DOC_BUSINESS_INFO_021")
	private String docBusinessInfo021;

	@Column(name="DOC_BUSINESS_INFO_022")
	private String docBusinessInfo022;

	@Column(name="DOC_BUSINESS_INFO_023")
	private String docBusinessInfo023;

	@Column(name="DOC_BUSINESS_INFO_024")
	private String docBusinessInfo024;

	@Column(name="DOC_BUSINESS_INFO_025")
	private String docBusinessInfo025;

	@Column(name="DOC_BUSINESS_INFO_026")
	private String docBusinessInfo026;

	@Column(name="DOC_BUSINESS_INFO_027")
	private String docBusinessInfo027;

	@Column(name="DOC_BUSINESS_INFO_028")
	private String docBusinessInfo028;

	@Column(name="DOC_BUSINESS_INFO_029")
	private String docBusinessInfo029;

	@Column(name="DOC_BUSINESS_INFO_030")
	private String docBusinessInfo030;

	@Column(name="DOC_BUSINESS_INFO_031")
	private String docBusinessInfo031;

	@Column(name="DOC_BUSINESS_INFO_032")
	private String docBusinessInfo032;

	@Column(name="DOC_BUSINESS_INFO_033")
	private String docBusinessInfo033;

	@Column(name="DOC_BUSINESS_INFO_034")
	private String docBusinessInfo034;

	@Column(name="DOC_BUSINESS_INFO_035")
	private String docBusinessInfo035;

	@Column(name="DOC_BUSINESS_INFO_036")
	private String docBusinessInfo036;

	@Column(name="DOC_BUSINESS_INFO_037")
	private String docBusinessInfo037;

	@Column(name="DOC_BUSINESS_INFO_038")
	private String docBusinessInfo038;

	@Column(name="DOC_BUSINESS_INFO_039")
	private String docBusinessInfo039;

	@Column(name="DOC_BUSINESS_INFO_040")
	private String docBusinessInfo040;

	@Column(name="DOC_BUSINESS_INFO_041")
	private String docBusinessInfo041;

	@Column(name="DOC_BUSINESS_INFO_042")
	private String docBusinessInfo042;

	@Column(name="DOC_BUSINESS_INFO_043")
	private String docBusinessInfo043;

	@Column(name="DOC_BUSINESS_INFO_044")
	private String docBusinessInfo044;

	@Column(name="DOC_BUSINESS_INFO_045")
	private String docBusinessInfo045;

	@Column(name="DOC_BUSINESS_INFO_046")
	private String docBusinessInfo046;

	@Column(name="DOC_BUSINESS_INFO_047")
	private String docBusinessInfo047;

	@Column(name="DOC_BUSINESS_INFO_048")
	private String docBusinessInfo048;

	@Column(name="DOC_BUSINESS_INFO_049")
	private String docBusinessInfo049;

	@Column(name="DOC_BUSINESS_INFO_050")
	private String docBusinessInfo050;

	@Column(name="DOC_BUSINESS_INFO_051")
	private String docBusinessInfo051;

	@Column(name="DOC_BUSINESS_INFO_052")
	private String docBusinessInfo052;

	@Column(name="DOC_BUSINESS_INFO_053")
	private String docBusinessInfo053;

	@Column(name="DOC_BUSINESS_INFO_054")
	private String docBusinessInfo054;

	@Column(name="DOC_BUSINESS_INFO_055")
	private String docBusinessInfo055;

	@Column(name="DOC_BUSINESS_INFO_056")
	private String docBusinessInfo056;

	@Column(name="DOC_BUSINESS_INFO_057")
	private String docBusinessInfo057;

	@Column(name="DOC_BUSINESS_INFO_058")
	private String docBusinessInfo058;

	@Column(name="DOC_BUSINESS_INFO_059")
	private String docBusinessInfo059;

	@Column(name="DOC_BUSINESS_INFO_060")
	private String docBusinessInfo060;

	@Column(name="DOC_BUSINESS_INFO_061")
	private String docBusinessInfo061;

	@Column(name="DOC_BUSINESS_INFO_062")
	private String docBusinessInfo062;

	@Column(name="DOC_BUSINESS_INFO_063")
	private String docBusinessInfo063;

	@Column(name="DOC_BUSINESS_INFO_064")
	private String docBusinessInfo064;

	@Column(name="DOC_BUSINESS_INFO_065")
	private String docBusinessInfo065;

	@Column(name="DOC_BUSINESS_INFO_066")
	private String docBusinessInfo066;

	@Column(name="DOC_BUSINESS_INFO_067")
	private String docBusinessInfo067;

	@Column(name="DOC_BUSINESS_INFO_068")
	private String docBusinessInfo068;

	@Column(name="DOC_BUSINESS_INFO_069")
	private String docBusinessInfo069;

	@Column(name="DOC_BUSINESS_INFO_070")
	private String docBusinessInfo070;

	@Column(name="DOC_BUSINESS_INFO_071")
	private String docBusinessInfo071;

	@Column(name="DOC_BUSINESS_INFO_072")
	private String docBusinessInfo072;

	@Column(name="DOC_BUSINESS_INFO_073")
	private String docBusinessInfo073;

	@Column(name="DOC_BUSINESS_INFO_074")
	private String docBusinessInfo074;

	@Column(name="DOC_BUSINESS_INFO_075")
	private String docBusinessInfo075;

	@Column(name="DOC_BUSINESS_INFO_076")
	private String docBusinessInfo076;

	@Column(name="DOC_BUSINESS_INFO_077")
	private String docBusinessInfo077;

	@Column(name="DOC_BUSINESS_INFO_078")
	private String docBusinessInfo078;

	@Column(name="DOC_BUSINESS_INFO_079")
	private String docBusinessInfo079;

	@Column(name="DOC_BUSINESS_INFO_080")
	private String docBusinessInfo080;

	@Column(name="DOC_BUSINESS_INFO_081")
	private String docBusinessInfo081;

	@Column(name="DOC_BUSINESS_INFO_082")
	private String docBusinessInfo082;

	@Column(name="DOC_BUSINESS_INFO_083")
	private String docBusinessInfo083;

	@Column(name="DOC_BUSINESS_INFO_084")
	private String docBusinessInfo084;

	@Column(name="DOC_BUSINESS_INFO_085")
	private String docBusinessInfo085;

	@Column(name="DOC_BUSINESS_INFO_086")
	private String docBusinessInfo086;

	@Column(name="DOC_BUSINESS_INFO_087")
	private String docBusinessInfo087;

	@Column(name="DOC_BUSINESS_INFO_088")
	private String docBusinessInfo088;

	@Column(name="DOC_BUSINESS_INFO_089")
	private String docBusinessInfo089;

	@Column(name="DOC_BUSINESS_INFO_090")
	private String docBusinessInfo090;

	@Column(name="DOC_BUSINESS_INFO_091")
	private String docBusinessInfo091;

	@Column(name="DOC_BUSINESS_INFO_092")
	private String docBusinessInfo092;

	@Column(name="DOC_BUSINESS_INFO_093")
	private String docBusinessInfo093;

	@Column(name="DOC_BUSINESS_INFO_094")
	private String docBusinessInfo094;

	@Column(name="DOC_BUSINESS_INFO_095")
	private String docBusinessInfo095;

	@Column(name="DOC_BUSINESS_INFO_096")
	private String docBusinessInfo096;

	@Column(name="DOC_BUSINESS_INFO_097")
	private String docBusinessInfo097;

	@Column(name="DOC_BUSINESS_INFO_098")
	private String docBusinessInfo098;

	@Column(name="DOC_BUSINESS_INFO_099")
	private String docBusinessInfo099;

	@Column(name="DOC_BUSINESS_INFO_100")
	private String docBusinessInfo100;

	@Column(name="DOC_BUSINESS_INFO_101")
	private String docBusinessInfo101;

	@Column(name="DOC_BUSINESS_INFO_102")
	private String docBusinessInfo102;

	@Column(name="DOC_BUSINESS_INFO_103")
	private String docBusinessInfo103;

	@Column(name="DOC_BUSINESS_INFO_104")
	private String docBusinessInfo104;

	@Column(name="DOC_BUSINESS_INFO_105")
	private String docBusinessInfo105;

	@Column(name="DOC_BUSINESS_INFO_106")
	private String docBusinessInfo106;

	@Column(name="DOC_BUSINESS_INFO_107")
	private String docBusinessInfo107;

	@Column(name="DOC_BUSINESS_INFO_108")
	private String docBusinessInfo108;

	@Column(name="DOC_BUSINESS_INFO_109")
	private String docBusinessInfo109;

	@Column(name="DOC_BUSINESS_INFO_110")
	private String docBusinessInfo110;

	@Column(name="DOC_BUSINESS_INFO_111")
	private String docBusinessInfo111;

	@Column(name="DOC_BUSINESS_INFO_112")
	private String docBusinessInfo112;

	@Column(name="DOC_BUSINESS_INFO_113")
	private String docBusinessInfo113;

	@Column(name="DOC_BUSINESS_INFO_114")
	private String docBusinessInfo114;

	@Column(name="DOC_BUSINESS_INFO_115")
	private String docBusinessInfo115;

	@Column(name="DOC_BUSINESS_INFO_116")
	private String docBusinessInfo116;

	@Column(name="DOC_BUSINESS_INFO_117")
	private String docBusinessInfo117;

	@Column(name="DOC_BUSINESS_INFO_118")
	private String docBusinessInfo118;

	@Column(name="DOC_BUSINESS_INFO_119")
	private String docBusinessInfo119;

	@Column(name="DOC_BUSINESS_INFO_120")
	private String docBusinessInfo120;

	@Column(name="DOC_BUSINESS_INFO_121")
	private String docBusinessInfo121;

	@Column(name="DOC_BUSINESS_INFO_122")
	private String docBusinessInfo122;

	@Column(name="DOC_BUSINESS_INFO_123")
	private String docBusinessInfo123;

	@Column(name="DOC_BUSINESS_INFO_124")
	private String docBusinessInfo124;

	@Column(name="DOC_BUSINESS_INFO_125")
	private String docBusinessInfo125;

	@Column(name="DOC_BUSINESS_INFO_126")
	private String docBusinessInfo126;

	@Column(name="DOC_BUSINESS_INFO_127")
	private String docBusinessInfo127;

	@Column(name="DOC_BUSINESS_INFO_128")
	private String docBusinessInfo128;

	@Column(name="DOC_BUSINESS_INFO_129")
	private String docBusinessInfo129;

	@Column(name="DOC_BUSINESS_INFO_130")
	private String docBusinessInfo130;

	@Column(name="DOC_BUSINESS_INFO_131")
	private String docBusinessInfo131;

	@Column(name="DOC_BUSINESS_INFO_132")
	private String docBusinessInfo132;

	@Column(name="DOC_BUSINESS_INFO_133")
	private String docBusinessInfo133;

	@Column(name="DOC_BUSINESS_INFO_134")
	private String docBusinessInfo134;

	@Column(name="DOC_BUSINESS_INFO_135")
	private String docBusinessInfo135;

	@Column(name="DOC_BUSINESS_INFO_136")
	private String docBusinessInfo136;

	@Column(name="DOC_BUSINESS_INFO_137")
	private String docBusinessInfo137;

	@Column(name="DOC_BUSINESS_INFO_138")
	private String docBusinessInfo138;

	@Column(name="DOC_BUSINESS_INFO_139")
	private String docBusinessInfo139;

	@Column(name="DOC_BUSINESS_INFO_140")
	private String docBusinessInfo140;

	@Column(name="DOC_BUSINESS_INFO_141")
	private String docBusinessInfo141;

	@Column(name="DOC_BUSINESS_INFO_142")
	private String docBusinessInfo142;

	@Column(name="DOC_BUSINESS_INFO_143")
	private String docBusinessInfo143;

	@Column(name="DOC_BUSINESS_INFO_144")
	private String docBusinessInfo144;

	@Column(name="DOC_BUSINESS_INFO_145")
	private String docBusinessInfo145;

	@Column(name="DOC_BUSINESS_INFO_146")
	private String docBusinessInfo146;

	@Column(name="DOC_BUSINESS_INFO_147")
	private String docBusinessInfo147;

	@Column(name="DOC_BUSINESS_INFO_148")
	private String docBusinessInfo148;

	@Column(name="DOC_BUSINESS_INFO_149")
	private String docBusinessInfo149;

	@Column(name="DOC_BUSINESS_INFO_150")
	private String docBusinessInfo150;

	@Column(name="DOC_BUSINESS_INFO_151")
	private String docBusinessInfo151;

	@Column(name="DOC_BUSINESS_INFO_152")
	private String docBusinessInfo152;

	@Column(name="DOC_BUSINESS_INFO_153")
	private String docBusinessInfo153;

	@Column(name="DOC_BUSINESS_INFO_154")
	private String docBusinessInfo154;

	@Column(name="DOC_BUSINESS_INFO_155")
	private String docBusinessInfo155;

	@Column(name="DOC_BUSINESS_INFO_156")
	private String docBusinessInfo156;

	@Column(name="DOC_BUSINESS_INFO_157")
	private String docBusinessInfo157;

	@Column(name="DOC_BUSINESS_INFO_158")
	private String docBusinessInfo158;

	@Column(name="DOC_BUSINESS_INFO_159")
	private String docBusinessInfo159;

	@Column(name="DOC_BUSINESS_INFO_160")
	private String docBusinessInfo160;

	@Column(name="DOC_BUSINESS_INFO_161")
	private String docBusinessInfo161;

	@Column(name="DOC_BUSINESS_INFO_162")
	private String docBusinessInfo162;

	@Column(name="DOC_BUSINESS_INFO_163")
	private String docBusinessInfo163;

	@Column(name="DOC_BUSINESS_INFO_164")
	private String docBusinessInfo164;

	@Column(name="DOC_BUSINESS_INFO_165")
	private String docBusinessInfo165;

	@Column(name="DOC_BUSINESS_INFO_166")
	private String docBusinessInfo166;

	@Column(name="DOC_BUSINESS_INFO_167")
	private String docBusinessInfo167;

	@Column(name="DOC_BUSINESS_INFO_168")
	private String docBusinessInfo168;

	@Column(name="DOC_BUSINESS_INFO_169")
	private String docBusinessInfo169;

	@Column(name="DOC_BUSINESS_INFO_170")
	private String docBusinessInfo170;

	@Column(name="DOC_BUSINESS_INFO_171")
	private String docBusinessInfo171;

	@Column(name="DOC_BUSINESS_INFO_172")
	private String docBusinessInfo172;

	@Column(name="DOC_BUSINESS_INFO_173")
	private String docBusinessInfo173;

	@Column(name="DOC_BUSINESS_INFO_174")
	private String docBusinessInfo174;

	@Column(name="DOC_BUSINESS_INFO_175")
	private String docBusinessInfo175;

	@Column(name="DOC_BUSINESS_INFO_176")
	private String docBusinessInfo176;

	@Column(name="DOC_BUSINESS_INFO_177")
	private String docBusinessInfo177;

	@Column(name="DOC_BUSINESS_INFO_178")
	private String docBusinessInfo178;

	@Column(name="DOC_BUSINESS_INFO_179")
	private String docBusinessInfo179;

	@Column(name="DOC_BUSINESS_INFO_180")
	private String docBusinessInfo180;

	@Column(name="DOC_BUSINESS_INFO_181")
	private String docBusinessInfo181;

	@Column(name="DOC_BUSINESS_INFO_182")
	private String docBusinessInfo182;

	@Column(name="DOC_BUSINESS_INFO_183")
	private String docBusinessInfo183;

	@Column(name="DOC_BUSINESS_INFO_184")
	private String docBusinessInfo184;

	@Column(name="DOC_BUSINESS_INFO_185")
	private String docBusinessInfo185;

	@Column(name="DOC_BUSINESS_INFO_186")
	private String docBusinessInfo186;

	@Column(name="DOC_BUSINESS_INFO_187")
	private String docBusinessInfo187;

	@Column(name="DOC_BUSINESS_INFO_188")
	private String docBusinessInfo188;

	@Column(name="DOC_BUSINESS_INFO_189")
	private String docBusinessInfo189;

	@Column(name="DOC_BUSINESS_INFO_190")
	private String docBusinessInfo190;

	@Column(name="DOC_BUSINESS_INFO_191")
	private String docBusinessInfo191;

	@Column(name="DOC_BUSINESS_INFO_192")
	private String docBusinessInfo192;

	@Column(name="DOC_BUSINESS_INFO_193")
	private String docBusinessInfo193;

	@Column(name="DOC_BUSINESS_INFO_194")
	private String docBusinessInfo194;

	@Column(name="DOC_BUSINESS_INFO_195")
	private String docBusinessInfo195;

	@Column(name="DOC_BUSINESS_INFO_196")
	private String docBusinessInfo196;

	@Column(name="DOC_BUSINESS_INFO_197")
	private String docBusinessInfo197;

	@Column(name="DOC_BUSINESS_INFO_198")
	private String docBusinessInfo198;

	@Column(name="DOC_BUSINESS_INFO_199")
	private String docBusinessInfo199;

	@Column(name="DOC_BUSINESS_INFO_200")
	private String docBusinessInfo200;

	@Column(name="DOC_BUSINESS_INFO_201")
	private String docBusinessInfo201;

	@Column(name="DOC_BUSINESS_INFO_202")
	private String docBusinessInfo202;

	@Column(name="DOC_BUSINESS_INFO_203")
	private String docBusinessInfo203;

	@Column(name="DOC_BUSINESS_INFO_204")
	private String docBusinessInfo204;

	@Column(name="DOC_BUSINESS_INFO_205")
	private String docBusinessInfo205;

	@Column(name="DOC_BUSINESS_INFO_206")
	private String docBusinessInfo206;

	@Column(name="DOC_BUSINESS_INFO_207")
	private String docBusinessInfo207;

	@Column(name="DOC_BUSINESS_INFO_208")
	private String docBusinessInfo208;

	@Column(name="DOC_BUSINESS_INFO_209")
	private String docBusinessInfo209;

	@Column(name="DOC_BUSINESS_INFO_210")
	private String docBusinessInfo210;

	@Column(name="DOC_BUSINESS_INFO_211")
	private String docBusinessInfo211;

	@Column(name="DOC_BUSINESS_INFO_212")
	private String docBusinessInfo212;

	@Column(name="DOC_BUSINESS_INFO_213")
	private String docBusinessInfo213;

	@Column(name="DOC_BUSINESS_INFO_214")
	private String docBusinessInfo214;

	@Column(name="DOC_BUSINESS_INFO_215")
	private String docBusinessInfo215;

	@Column(name="DOC_BUSINESS_INFO_216")
	private String docBusinessInfo216;

	@Column(name="DOC_BUSINESS_INFO_217")
	private String docBusinessInfo217;

	@Column(name="DOC_BUSINESS_INFO_218")
	private String docBusinessInfo218;

	@Column(name="DOC_BUSINESS_INFO_219")
	private String docBusinessInfo219;

	@Column(name="DOC_BUSINESS_INFO_220")
	private String docBusinessInfo220;

	@Column(name="DOC_BUSINESS_INFO_221")
	private String docBusinessInfo221;

	@Column(name="DOC_BUSINESS_INFO_222")
	private String docBusinessInfo222;

	@Column(name="DOC_BUSINESS_INFO_223")
	private String docBusinessInfo223;

	@Column(name="DOC_BUSINESS_INFO_224")
	private String docBusinessInfo224;

	@Column(name="DOC_BUSINESS_INFO_225")
	private String docBusinessInfo225;

	@Column(name="DOC_BUSINESS_INFO_226")
	private String docBusinessInfo226;

	@Column(name="DOC_BUSINESS_INFO_227")
	private String docBusinessInfo227;

	@Column(name="DOC_BUSINESS_INFO_228")
	private String docBusinessInfo228;

	@Column(name="DOC_BUSINESS_INFO_229")
	private String docBusinessInfo229;

	@Column(name="DOC_BUSINESS_INFO_230")
	private String docBusinessInfo230;

	@Column(name="DOC_BUSINESS_INFO_231")
	private String docBusinessInfo231;

	@Column(name="DOC_BUSINESS_INFO_232")
	private String docBusinessInfo232;

	@Column(name="DOC_BUSINESS_INFO_233")
	private String docBusinessInfo233;

	@Column(name="DOC_BUSINESS_INFO_234")
	private String docBusinessInfo234;

	@Column(name="DOC_BUSINESS_INFO_235")
	private String docBusinessInfo235;

	@Column(name="DOC_BUSINESS_INFO_236")
	private String docBusinessInfo236;

	@Column(name="DOC_BUSINESS_INFO_237")
	private String docBusinessInfo237;

	@Column(name="DOC_BUSINESS_INFO_238")
	private String docBusinessInfo238;

	@Column(name="DOC_BUSINESS_INFO_239")
	private String docBusinessInfo239;

	@Column(name="DOC_BUSINESS_INFO_240")
	private String docBusinessInfo240;

	@Column(name="DOC_BUSINESS_INFO_241")
	private String docBusinessInfo241;

	@Column(name="DOC_BUSINESS_INFO_242")
	private String docBusinessInfo242;

	@Column(name="DOC_BUSINESS_INFO_243")
	private String docBusinessInfo243;

	@Column(name="DOC_BUSINESS_INFO_244")
	private String docBusinessInfo244;

	@Column(name="DOC_BUSINESS_INFO_245")
	private String docBusinessInfo245;

	@Column(name="DOC_BUSINESS_INFO_246")
	private String docBusinessInfo246;

	@Column(name="DOC_BUSINESS_INFO_247")
	private String docBusinessInfo247;

	@Column(name="DOC_BUSINESS_INFO_248")
	private String docBusinessInfo248;

	@Column(name="DOC_BUSINESS_INFO_249")
	private String docBusinessInfo249;

	@Column(name="DOC_BUSINESS_INFO_250")
	private String docBusinessInfo250;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="DOC_NUM")
	private String docNum;

	@Column(name="HISTORY_SEQ_NO")
	private Integer historySeqNo;

	@Column(name="LOCK_CORPORATION_CODE")
	private String lockCorporationCode;

	@Column(name="LOCK_FLAG")
	private String lockFlag;

	@Column(name="LOCK_TIMESTAMP")
	private Timestamp lockTimestamp;

	@Column(name="LOCK_USER_CODE")
	private String lockUserCode;

	@Column(name="LOCK_USER_NAME")
	private String lockUserName;

	@Column(name="MAJOR_VERSION")
	private Integer majorVersion;

	@Column(name="META_TEMPLATE_ID")
	private Long metaTemplateId;

	@Column(name="MINOR_VERSION")
	private Integer minorVersion;

	@Column(name="OWNER_CORPORATION_CODE")
	private String ownerCorporationCode;

	@Column(name="OWNER_USER_CODE")
	private String ownerUserCode;

	@Column(name="OWNER_USER_NAME")
	private String ownerUserName;

	@Column(name="PROCESS_ID")
	private Long processId;

	@Column(name="PUBLISH_CORPORATION_CODE")
	private String publishCorporationCode;

	@Temporal(TemporalType.DATE)
	@Column(name="PUBLISH_END_DATE")
	private Date publishEndDate;

	@Column(name="PUBLISH_FLAG")
	private String publishFlag;

	@Temporal(TemporalType.DATE)
	@Column(name="PUBLISH_START_DATE")
	private Date publishStartDate;

	@Column(name="PUBLISH_TIMESTAMP")
	private Timestamp publishTimestamp;

	@Column(name="PUBLISH_USER_CODE")
	private String publishUserCode;

	@Column(name="PUBLISH_USER_NAME")
	private String publishUserName;

	@Column(name="RETENTION_TERM")
	private Integer retentionTerm;

	@Column(name="RETENTION_TERM_TYPE")
	private String retentionTermType;

	private String title;

	@Column(name="USER_NAME_CREATED")
	private String userNameCreated;

	@Column(name="USER_NAME_UPDATED")
	private String userNameUpdated;

	public MwtDocInfoHistory() {
	}

	public long getDocHistoryId() {
		return this.docHistoryId;
	}

	public void setDocHistoryId(long docHistoryId) {
		this.docHistoryId = docHistoryId;
	}

	public Long getBinderId() {
		return this.binderId;
	}

	public void setBinderId(Long binderId) {
		this.binderId = binderId;
	}

	public Long getBizDocId() {
		return this.bizDocId;
	}

	public void setBizDocId(Long bizDocId) {
		this.bizDocId = bizDocId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getContentsType() {
		return this.contentsType;
	}

	public void setContentsType(String contentsType) {
		this.contentsType = contentsType;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Integer getDispCount() {
		return this.dispCount;
	}

	public void setDispCount(Integer dispCount) {
		this.dispCount = dispCount;
	}

	public String getDocBusinessInfo001() {
		return this.docBusinessInfo001;
	}

	public void setDocBusinessInfo001(String docBusinessInfo001) {
		this.docBusinessInfo001 = docBusinessInfo001;
	}

	public String getDocBusinessInfo002() {
		return this.docBusinessInfo002;
	}

	public void setDocBusinessInfo002(String docBusinessInfo002) {
		this.docBusinessInfo002 = docBusinessInfo002;
	}

	public String getDocBusinessInfo003() {
		return this.docBusinessInfo003;
	}

	public void setDocBusinessInfo003(String docBusinessInfo003) {
		this.docBusinessInfo003 = docBusinessInfo003;
	}

	public String getDocBusinessInfo004() {
		return this.docBusinessInfo004;
	}

	public void setDocBusinessInfo004(String docBusinessInfo004) {
		this.docBusinessInfo004 = docBusinessInfo004;
	}

	public String getDocBusinessInfo005() {
		return this.docBusinessInfo005;
	}

	public void setDocBusinessInfo005(String docBusinessInfo005) {
		this.docBusinessInfo005 = docBusinessInfo005;
	}

	public String getDocBusinessInfo006() {
		return this.docBusinessInfo006;
	}

	public void setDocBusinessInfo006(String docBusinessInfo006) {
		this.docBusinessInfo006 = docBusinessInfo006;
	}

	public String getDocBusinessInfo007() {
		return this.docBusinessInfo007;
	}

	public void setDocBusinessInfo007(String docBusinessInfo007) {
		this.docBusinessInfo007 = docBusinessInfo007;
	}

	public String getDocBusinessInfo008() {
		return this.docBusinessInfo008;
	}

	public void setDocBusinessInfo008(String docBusinessInfo008) {
		this.docBusinessInfo008 = docBusinessInfo008;
	}

	public String getDocBusinessInfo009() {
		return this.docBusinessInfo009;
	}

	public void setDocBusinessInfo009(String docBusinessInfo009) {
		this.docBusinessInfo009 = docBusinessInfo009;
	}

	public String getDocBusinessInfo010() {
		return this.docBusinessInfo010;
	}

	public void setDocBusinessInfo010(String docBusinessInfo010) {
		this.docBusinessInfo010 = docBusinessInfo010;
	}

	public String getDocBusinessInfo011() {
		return this.docBusinessInfo011;
	}

	public void setDocBusinessInfo011(String docBusinessInfo011) {
		this.docBusinessInfo011 = docBusinessInfo011;
	}

	public String getDocBusinessInfo012() {
		return this.docBusinessInfo012;
	}

	public void setDocBusinessInfo012(String docBusinessInfo012) {
		this.docBusinessInfo012 = docBusinessInfo012;
	}

	public String getDocBusinessInfo013() {
		return this.docBusinessInfo013;
	}

	public void setDocBusinessInfo013(String docBusinessInfo013) {
		this.docBusinessInfo013 = docBusinessInfo013;
	}

	public String getDocBusinessInfo014() {
		return this.docBusinessInfo014;
	}

	public void setDocBusinessInfo014(String docBusinessInfo014) {
		this.docBusinessInfo014 = docBusinessInfo014;
	}

	public String getDocBusinessInfo015() {
		return this.docBusinessInfo015;
	}

	public void setDocBusinessInfo015(String docBusinessInfo015) {
		this.docBusinessInfo015 = docBusinessInfo015;
	}

	public String getDocBusinessInfo016() {
		return this.docBusinessInfo016;
	}

	public void setDocBusinessInfo016(String docBusinessInfo016) {
		this.docBusinessInfo016 = docBusinessInfo016;
	}

	public String getDocBusinessInfo017() {
		return this.docBusinessInfo017;
	}

	public void setDocBusinessInfo017(String docBusinessInfo017) {
		this.docBusinessInfo017 = docBusinessInfo017;
	}

	public String getDocBusinessInfo018() {
		return this.docBusinessInfo018;
	}

	public void setDocBusinessInfo018(String docBusinessInfo018) {
		this.docBusinessInfo018 = docBusinessInfo018;
	}

	public String getDocBusinessInfo019() {
		return this.docBusinessInfo019;
	}

	public void setDocBusinessInfo019(String docBusinessInfo019) {
		this.docBusinessInfo019 = docBusinessInfo019;
	}

	public String getDocBusinessInfo020() {
		return this.docBusinessInfo020;
	}

	public void setDocBusinessInfo020(String docBusinessInfo020) {
		this.docBusinessInfo020 = docBusinessInfo020;
	}

	public String getDocBusinessInfo021() {
		return this.docBusinessInfo021;
	}

	public void setDocBusinessInfo021(String docBusinessInfo021) {
		this.docBusinessInfo021 = docBusinessInfo021;
	}

	public String getDocBusinessInfo022() {
		return this.docBusinessInfo022;
	}

	public void setDocBusinessInfo022(String docBusinessInfo022) {
		this.docBusinessInfo022 = docBusinessInfo022;
	}

	public String getDocBusinessInfo023() {
		return this.docBusinessInfo023;
	}

	public void setDocBusinessInfo023(String docBusinessInfo023) {
		this.docBusinessInfo023 = docBusinessInfo023;
	}

	public String getDocBusinessInfo024() {
		return this.docBusinessInfo024;
	}

	public void setDocBusinessInfo024(String docBusinessInfo024) {
		this.docBusinessInfo024 = docBusinessInfo024;
	}

	public String getDocBusinessInfo025() {
		return this.docBusinessInfo025;
	}

	public void setDocBusinessInfo025(String docBusinessInfo025) {
		this.docBusinessInfo025 = docBusinessInfo025;
	}

	public String getDocBusinessInfo026() {
		return this.docBusinessInfo026;
	}

	public void setDocBusinessInfo026(String docBusinessInfo026) {
		this.docBusinessInfo026 = docBusinessInfo026;
	}

	public String getDocBusinessInfo027() {
		return this.docBusinessInfo027;
	}

	public void setDocBusinessInfo027(String docBusinessInfo027) {
		this.docBusinessInfo027 = docBusinessInfo027;
	}

	public String getDocBusinessInfo028() {
		return this.docBusinessInfo028;
	}

	public void setDocBusinessInfo028(String docBusinessInfo028) {
		this.docBusinessInfo028 = docBusinessInfo028;
	}

	public String getDocBusinessInfo029() {
		return this.docBusinessInfo029;
	}

	public void setDocBusinessInfo029(String docBusinessInfo029) {
		this.docBusinessInfo029 = docBusinessInfo029;
	}

	public String getDocBusinessInfo030() {
		return this.docBusinessInfo030;
	}

	public void setDocBusinessInfo030(String docBusinessInfo030) {
		this.docBusinessInfo030 = docBusinessInfo030;
	}

	public String getDocBusinessInfo031() {
		return this.docBusinessInfo031;
	}

	public void setDocBusinessInfo031(String docBusinessInfo031) {
		this.docBusinessInfo031 = docBusinessInfo031;
	}

	public String getDocBusinessInfo032() {
		return this.docBusinessInfo032;
	}

	public void setDocBusinessInfo032(String docBusinessInfo032) {
		this.docBusinessInfo032 = docBusinessInfo032;
	}

	public String getDocBusinessInfo033() {
		return this.docBusinessInfo033;
	}

	public void setDocBusinessInfo033(String docBusinessInfo033) {
		this.docBusinessInfo033 = docBusinessInfo033;
	}

	public String getDocBusinessInfo034() {
		return this.docBusinessInfo034;
	}

	public void setDocBusinessInfo034(String docBusinessInfo034) {
		this.docBusinessInfo034 = docBusinessInfo034;
	}

	public String getDocBusinessInfo035() {
		return this.docBusinessInfo035;
	}

	public void setDocBusinessInfo035(String docBusinessInfo035) {
		this.docBusinessInfo035 = docBusinessInfo035;
	}

	public String getDocBusinessInfo036() {
		return this.docBusinessInfo036;
	}

	public void setDocBusinessInfo036(String docBusinessInfo036) {
		this.docBusinessInfo036 = docBusinessInfo036;
	}

	public String getDocBusinessInfo037() {
		return this.docBusinessInfo037;
	}

	public void setDocBusinessInfo037(String docBusinessInfo037) {
		this.docBusinessInfo037 = docBusinessInfo037;
	}

	public String getDocBusinessInfo038() {
		return this.docBusinessInfo038;
	}

	public void setDocBusinessInfo038(String docBusinessInfo038) {
		this.docBusinessInfo038 = docBusinessInfo038;
	}

	public String getDocBusinessInfo039() {
		return this.docBusinessInfo039;
	}

	public void setDocBusinessInfo039(String docBusinessInfo039) {
		this.docBusinessInfo039 = docBusinessInfo039;
	}

	public String getDocBusinessInfo040() {
		return this.docBusinessInfo040;
	}

	public void setDocBusinessInfo040(String docBusinessInfo040) {
		this.docBusinessInfo040 = docBusinessInfo040;
	}

	public String getDocBusinessInfo041() {
		return this.docBusinessInfo041;
	}

	public void setDocBusinessInfo041(String docBusinessInfo041) {
		this.docBusinessInfo041 = docBusinessInfo041;
	}

	public String getDocBusinessInfo042() {
		return this.docBusinessInfo042;
	}

	public void setDocBusinessInfo042(String docBusinessInfo042) {
		this.docBusinessInfo042 = docBusinessInfo042;
	}

	public String getDocBusinessInfo043() {
		return this.docBusinessInfo043;
	}

	public void setDocBusinessInfo043(String docBusinessInfo043) {
		this.docBusinessInfo043 = docBusinessInfo043;
	}

	public String getDocBusinessInfo044() {
		return this.docBusinessInfo044;
	}

	public void setDocBusinessInfo044(String docBusinessInfo044) {
		this.docBusinessInfo044 = docBusinessInfo044;
	}

	public String getDocBusinessInfo045() {
		return this.docBusinessInfo045;
	}

	public void setDocBusinessInfo045(String docBusinessInfo045) {
		this.docBusinessInfo045 = docBusinessInfo045;
	}

	public String getDocBusinessInfo046() {
		return this.docBusinessInfo046;
	}

	public void setDocBusinessInfo046(String docBusinessInfo046) {
		this.docBusinessInfo046 = docBusinessInfo046;
	}

	public String getDocBusinessInfo047() {
		return this.docBusinessInfo047;
	}

	public void setDocBusinessInfo047(String docBusinessInfo047) {
		this.docBusinessInfo047 = docBusinessInfo047;
	}

	public String getDocBusinessInfo048() {
		return this.docBusinessInfo048;
	}

	public void setDocBusinessInfo048(String docBusinessInfo048) {
		this.docBusinessInfo048 = docBusinessInfo048;
	}

	public String getDocBusinessInfo049() {
		return this.docBusinessInfo049;
	}

	public void setDocBusinessInfo049(String docBusinessInfo049) {
		this.docBusinessInfo049 = docBusinessInfo049;
	}

	public String getDocBusinessInfo050() {
		return this.docBusinessInfo050;
	}

	public void setDocBusinessInfo050(String docBusinessInfo050) {
		this.docBusinessInfo050 = docBusinessInfo050;
	}

	public String getDocBusinessInfo051() {
		return this.docBusinessInfo051;
	}

	public void setDocBusinessInfo051(String docBusinessInfo051) {
		this.docBusinessInfo051 = docBusinessInfo051;
	}

	public String getDocBusinessInfo052() {
		return this.docBusinessInfo052;
	}

	public void setDocBusinessInfo052(String docBusinessInfo052) {
		this.docBusinessInfo052 = docBusinessInfo052;
	}

	public String getDocBusinessInfo053() {
		return this.docBusinessInfo053;
	}

	public void setDocBusinessInfo053(String docBusinessInfo053) {
		this.docBusinessInfo053 = docBusinessInfo053;
	}

	public String getDocBusinessInfo054() {
		return this.docBusinessInfo054;
	}

	public void setDocBusinessInfo054(String docBusinessInfo054) {
		this.docBusinessInfo054 = docBusinessInfo054;
	}

	public String getDocBusinessInfo055() {
		return this.docBusinessInfo055;
	}

	public void setDocBusinessInfo055(String docBusinessInfo055) {
		this.docBusinessInfo055 = docBusinessInfo055;
	}

	public String getDocBusinessInfo056() {
		return this.docBusinessInfo056;
	}

	public void setDocBusinessInfo056(String docBusinessInfo056) {
		this.docBusinessInfo056 = docBusinessInfo056;
	}

	public String getDocBusinessInfo057() {
		return this.docBusinessInfo057;
	}

	public void setDocBusinessInfo057(String docBusinessInfo057) {
		this.docBusinessInfo057 = docBusinessInfo057;
	}

	public String getDocBusinessInfo058() {
		return this.docBusinessInfo058;
	}

	public void setDocBusinessInfo058(String docBusinessInfo058) {
		this.docBusinessInfo058 = docBusinessInfo058;
	}

	public String getDocBusinessInfo059() {
		return this.docBusinessInfo059;
	}

	public void setDocBusinessInfo059(String docBusinessInfo059) {
		this.docBusinessInfo059 = docBusinessInfo059;
	}

	public String getDocBusinessInfo060() {
		return this.docBusinessInfo060;
	}

	public void setDocBusinessInfo060(String docBusinessInfo060) {
		this.docBusinessInfo060 = docBusinessInfo060;
	}

	public String getDocBusinessInfo061() {
		return this.docBusinessInfo061;
	}

	public void setDocBusinessInfo061(String docBusinessInfo061) {
		this.docBusinessInfo061 = docBusinessInfo061;
	}

	public String getDocBusinessInfo062() {
		return this.docBusinessInfo062;
	}

	public void setDocBusinessInfo062(String docBusinessInfo062) {
		this.docBusinessInfo062 = docBusinessInfo062;
	}

	public String getDocBusinessInfo063() {
		return this.docBusinessInfo063;
	}

	public void setDocBusinessInfo063(String docBusinessInfo063) {
		this.docBusinessInfo063 = docBusinessInfo063;
	}

	public String getDocBusinessInfo064() {
		return this.docBusinessInfo064;
	}

	public void setDocBusinessInfo064(String docBusinessInfo064) {
		this.docBusinessInfo064 = docBusinessInfo064;
	}

	public String getDocBusinessInfo065() {
		return this.docBusinessInfo065;
	}

	public void setDocBusinessInfo065(String docBusinessInfo065) {
		this.docBusinessInfo065 = docBusinessInfo065;
	}

	public String getDocBusinessInfo066() {
		return this.docBusinessInfo066;
	}

	public void setDocBusinessInfo066(String docBusinessInfo066) {
		this.docBusinessInfo066 = docBusinessInfo066;
	}

	public String getDocBusinessInfo067() {
		return this.docBusinessInfo067;
	}

	public void setDocBusinessInfo067(String docBusinessInfo067) {
		this.docBusinessInfo067 = docBusinessInfo067;
	}

	public String getDocBusinessInfo068() {
		return this.docBusinessInfo068;
	}

	public void setDocBusinessInfo068(String docBusinessInfo068) {
		this.docBusinessInfo068 = docBusinessInfo068;
	}

	public String getDocBusinessInfo069() {
		return this.docBusinessInfo069;
	}

	public void setDocBusinessInfo069(String docBusinessInfo069) {
		this.docBusinessInfo069 = docBusinessInfo069;
	}

	public String getDocBusinessInfo070() {
		return this.docBusinessInfo070;
	}

	public void setDocBusinessInfo070(String docBusinessInfo070) {
		this.docBusinessInfo070 = docBusinessInfo070;
	}

	public String getDocBusinessInfo071() {
		return this.docBusinessInfo071;
	}

	public void setDocBusinessInfo071(String docBusinessInfo071) {
		this.docBusinessInfo071 = docBusinessInfo071;
	}

	public String getDocBusinessInfo072() {
		return this.docBusinessInfo072;
	}

	public void setDocBusinessInfo072(String docBusinessInfo072) {
		this.docBusinessInfo072 = docBusinessInfo072;
	}

	public String getDocBusinessInfo073() {
		return this.docBusinessInfo073;
	}

	public void setDocBusinessInfo073(String docBusinessInfo073) {
		this.docBusinessInfo073 = docBusinessInfo073;
	}

	public String getDocBusinessInfo074() {
		return this.docBusinessInfo074;
	}

	public void setDocBusinessInfo074(String docBusinessInfo074) {
		this.docBusinessInfo074 = docBusinessInfo074;
	}

	public String getDocBusinessInfo075() {
		return this.docBusinessInfo075;
	}

	public void setDocBusinessInfo075(String docBusinessInfo075) {
		this.docBusinessInfo075 = docBusinessInfo075;
	}

	public String getDocBusinessInfo076() {
		return this.docBusinessInfo076;
	}

	public void setDocBusinessInfo076(String docBusinessInfo076) {
		this.docBusinessInfo076 = docBusinessInfo076;
	}

	public String getDocBusinessInfo077() {
		return this.docBusinessInfo077;
	}

	public void setDocBusinessInfo077(String docBusinessInfo077) {
		this.docBusinessInfo077 = docBusinessInfo077;
	}

	public String getDocBusinessInfo078() {
		return this.docBusinessInfo078;
	}

	public void setDocBusinessInfo078(String docBusinessInfo078) {
		this.docBusinessInfo078 = docBusinessInfo078;
	}

	public String getDocBusinessInfo079() {
		return this.docBusinessInfo079;
	}

	public void setDocBusinessInfo079(String docBusinessInfo079) {
		this.docBusinessInfo079 = docBusinessInfo079;
	}

	public String getDocBusinessInfo080() {
		return this.docBusinessInfo080;
	}

	public void setDocBusinessInfo080(String docBusinessInfo080) {
		this.docBusinessInfo080 = docBusinessInfo080;
	}

	public String getDocBusinessInfo081() {
		return this.docBusinessInfo081;
	}

	public void setDocBusinessInfo081(String docBusinessInfo081) {
		this.docBusinessInfo081 = docBusinessInfo081;
	}

	public String getDocBusinessInfo082() {
		return this.docBusinessInfo082;
	}

	public void setDocBusinessInfo082(String docBusinessInfo082) {
		this.docBusinessInfo082 = docBusinessInfo082;
	}

	public String getDocBusinessInfo083() {
		return this.docBusinessInfo083;
	}

	public void setDocBusinessInfo083(String docBusinessInfo083) {
		this.docBusinessInfo083 = docBusinessInfo083;
	}

	public String getDocBusinessInfo084() {
		return this.docBusinessInfo084;
	}

	public void setDocBusinessInfo084(String docBusinessInfo084) {
		this.docBusinessInfo084 = docBusinessInfo084;
	}

	public String getDocBusinessInfo085() {
		return this.docBusinessInfo085;
	}

	public void setDocBusinessInfo085(String docBusinessInfo085) {
		this.docBusinessInfo085 = docBusinessInfo085;
	}

	public String getDocBusinessInfo086() {
		return this.docBusinessInfo086;
	}

	public void setDocBusinessInfo086(String docBusinessInfo086) {
		this.docBusinessInfo086 = docBusinessInfo086;
	}

	public String getDocBusinessInfo087() {
		return this.docBusinessInfo087;
	}

	public void setDocBusinessInfo087(String docBusinessInfo087) {
		this.docBusinessInfo087 = docBusinessInfo087;
	}

	public String getDocBusinessInfo088() {
		return this.docBusinessInfo088;
	}

	public void setDocBusinessInfo088(String docBusinessInfo088) {
		this.docBusinessInfo088 = docBusinessInfo088;
	}

	public String getDocBusinessInfo089() {
		return this.docBusinessInfo089;
	}

	public void setDocBusinessInfo089(String docBusinessInfo089) {
		this.docBusinessInfo089 = docBusinessInfo089;
	}

	public String getDocBusinessInfo090() {
		return this.docBusinessInfo090;
	}

	public void setDocBusinessInfo090(String docBusinessInfo090) {
		this.docBusinessInfo090 = docBusinessInfo090;
	}

	public String getDocBusinessInfo091() {
		return this.docBusinessInfo091;
	}

	public void setDocBusinessInfo091(String docBusinessInfo091) {
		this.docBusinessInfo091 = docBusinessInfo091;
	}

	public String getDocBusinessInfo092() {
		return this.docBusinessInfo092;
	}

	public void setDocBusinessInfo092(String docBusinessInfo092) {
		this.docBusinessInfo092 = docBusinessInfo092;
	}

	public String getDocBusinessInfo093() {
		return this.docBusinessInfo093;
	}

	public void setDocBusinessInfo093(String docBusinessInfo093) {
		this.docBusinessInfo093 = docBusinessInfo093;
	}

	public String getDocBusinessInfo094() {
		return this.docBusinessInfo094;
	}

	public void setDocBusinessInfo094(String docBusinessInfo094) {
		this.docBusinessInfo094 = docBusinessInfo094;
	}

	public String getDocBusinessInfo095() {
		return this.docBusinessInfo095;
	}

	public void setDocBusinessInfo095(String docBusinessInfo095) {
		this.docBusinessInfo095 = docBusinessInfo095;
	}

	public String getDocBusinessInfo096() {
		return this.docBusinessInfo096;
	}

	public void setDocBusinessInfo096(String docBusinessInfo096) {
		this.docBusinessInfo096 = docBusinessInfo096;
	}

	public String getDocBusinessInfo097() {
		return this.docBusinessInfo097;
	}

	public void setDocBusinessInfo097(String docBusinessInfo097) {
		this.docBusinessInfo097 = docBusinessInfo097;
	}

	public String getDocBusinessInfo098() {
		return this.docBusinessInfo098;
	}

	public void setDocBusinessInfo098(String docBusinessInfo098) {
		this.docBusinessInfo098 = docBusinessInfo098;
	}

	public String getDocBusinessInfo099() {
		return this.docBusinessInfo099;
	}

	public void setDocBusinessInfo099(String docBusinessInfo099) {
		this.docBusinessInfo099 = docBusinessInfo099;
	}

	public String getDocBusinessInfo100() {
		return this.docBusinessInfo100;
	}

	public void setDocBusinessInfo100(String docBusinessInfo100) {
		this.docBusinessInfo100 = docBusinessInfo100;
	}

	public String getDocBusinessInfo101() {
		return this.docBusinessInfo101;
	}

	public void setDocBusinessInfo101(String docBusinessInfo101) {
		this.docBusinessInfo101 = docBusinessInfo101;
	}

	public String getDocBusinessInfo102() {
		return this.docBusinessInfo102;
	}

	public void setDocBusinessInfo102(String docBusinessInfo102) {
		this.docBusinessInfo102 = docBusinessInfo102;
	}

	public String getDocBusinessInfo103() {
		return this.docBusinessInfo103;
	}

	public void setDocBusinessInfo103(String docBusinessInfo103) {
		this.docBusinessInfo103 = docBusinessInfo103;
	}

	public String getDocBusinessInfo104() {
		return this.docBusinessInfo104;
	}

	public void setDocBusinessInfo104(String docBusinessInfo104) {
		this.docBusinessInfo104 = docBusinessInfo104;
	}

	public String getDocBusinessInfo105() {
		return this.docBusinessInfo105;
	}

	public void setDocBusinessInfo105(String docBusinessInfo105) {
		this.docBusinessInfo105 = docBusinessInfo105;
	}

	public String getDocBusinessInfo106() {
		return this.docBusinessInfo106;
	}

	public void setDocBusinessInfo106(String docBusinessInfo106) {
		this.docBusinessInfo106 = docBusinessInfo106;
	}

	public String getDocBusinessInfo107() {
		return this.docBusinessInfo107;
	}

	public void setDocBusinessInfo107(String docBusinessInfo107) {
		this.docBusinessInfo107 = docBusinessInfo107;
	}

	public String getDocBusinessInfo108() {
		return this.docBusinessInfo108;
	}

	public void setDocBusinessInfo108(String docBusinessInfo108) {
		this.docBusinessInfo108 = docBusinessInfo108;
	}

	public String getDocBusinessInfo109() {
		return this.docBusinessInfo109;
	}

	public void setDocBusinessInfo109(String docBusinessInfo109) {
		this.docBusinessInfo109 = docBusinessInfo109;
	}

	public String getDocBusinessInfo110() {
		return this.docBusinessInfo110;
	}

	public void setDocBusinessInfo110(String docBusinessInfo110) {
		this.docBusinessInfo110 = docBusinessInfo110;
	}

	public String getDocBusinessInfo111() {
		return this.docBusinessInfo111;
	}

	public void setDocBusinessInfo111(String docBusinessInfo111) {
		this.docBusinessInfo111 = docBusinessInfo111;
	}

	public String getDocBusinessInfo112() {
		return this.docBusinessInfo112;
	}

	public void setDocBusinessInfo112(String docBusinessInfo112) {
		this.docBusinessInfo112 = docBusinessInfo112;
	}

	public String getDocBusinessInfo113() {
		return this.docBusinessInfo113;
	}

	public void setDocBusinessInfo113(String docBusinessInfo113) {
		this.docBusinessInfo113 = docBusinessInfo113;
	}

	public String getDocBusinessInfo114() {
		return this.docBusinessInfo114;
	}

	public void setDocBusinessInfo114(String docBusinessInfo114) {
		this.docBusinessInfo114 = docBusinessInfo114;
	}

	public String getDocBusinessInfo115() {
		return this.docBusinessInfo115;
	}

	public void setDocBusinessInfo115(String docBusinessInfo115) {
		this.docBusinessInfo115 = docBusinessInfo115;
	}

	public String getDocBusinessInfo116() {
		return this.docBusinessInfo116;
	}

	public void setDocBusinessInfo116(String docBusinessInfo116) {
		this.docBusinessInfo116 = docBusinessInfo116;
	}

	public String getDocBusinessInfo117() {
		return this.docBusinessInfo117;
	}

	public void setDocBusinessInfo117(String docBusinessInfo117) {
		this.docBusinessInfo117 = docBusinessInfo117;
	}

	public String getDocBusinessInfo118() {
		return this.docBusinessInfo118;
	}

	public void setDocBusinessInfo118(String docBusinessInfo118) {
		this.docBusinessInfo118 = docBusinessInfo118;
	}

	public String getDocBusinessInfo119() {
		return this.docBusinessInfo119;
	}

	public void setDocBusinessInfo119(String docBusinessInfo119) {
		this.docBusinessInfo119 = docBusinessInfo119;
	}

	public String getDocBusinessInfo120() {
		return this.docBusinessInfo120;
	}

	public void setDocBusinessInfo120(String docBusinessInfo120) {
		this.docBusinessInfo120 = docBusinessInfo120;
	}

	public String getDocBusinessInfo121() {
		return this.docBusinessInfo121;
	}

	public void setDocBusinessInfo121(String docBusinessInfo121) {
		this.docBusinessInfo121 = docBusinessInfo121;
	}

	public String getDocBusinessInfo122() {
		return this.docBusinessInfo122;
	}

	public void setDocBusinessInfo122(String docBusinessInfo122) {
		this.docBusinessInfo122 = docBusinessInfo122;
	}

	public String getDocBusinessInfo123() {
		return this.docBusinessInfo123;
	}

	public void setDocBusinessInfo123(String docBusinessInfo123) {
		this.docBusinessInfo123 = docBusinessInfo123;
	}

	public String getDocBusinessInfo124() {
		return this.docBusinessInfo124;
	}

	public void setDocBusinessInfo124(String docBusinessInfo124) {
		this.docBusinessInfo124 = docBusinessInfo124;
	}

	public String getDocBusinessInfo125() {
		return this.docBusinessInfo125;
	}

	public void setDocBusinessInfo125(String docBusinessInfo125) {
		this.docBusinessInfo125 = docBusinessInfo125;
	}

	public String getDocBusinessInfo126() {
		return this.docBusinessInfo126;
	}

	public void setDocBusinessInfo126(String docBusinessInfo126) {
		this.docBusinessInfo126 = docBusinessInfo126;
	}

	public String getDocBusinessInfo127() {
		return this.docBusinessInfo127;
	}

	public void setDocBusinessInfo127(String docBusinessInfo127) {
		this.docBusinessInfo127 = docBusinessInfo127;
	}

	public String getDocBusinessInfo128() {
		return this.docBusinessInfo128;
	}

	public void setDocBusinessInfo128(String docBusinessInfo128) {
		this.docBusinessInfo128 = docBusinessInfo128;
	}

	public String getDocBusinessInfo129() {
		return this.docBusinessInfo129;
	}

	public void setDocBusinessInfo129(String docBusinessInfo129) {
		this.docBusinessInfo129 = docBusinessInfo129;
	}

	public String getDocBusinessInfo130() {
		return this.docBusinessInfo130;
	}

	public void setDocBusinessInfo130(String docBusinessInfo130) {
		this.docBusinessInfo130 = docBusinessInfo130;
	}

	public String getDocBusinessInfo131() {
		return this.docBusinessInfo131;
	}

	public void setDocBusinessInfo131(String docBusinessInfo131) {
		this.docBusinessInfo131 = docBusinessInfo131;
	}

	public String getDocBusinessInfo132() {
		return this.docBusinessInfo132;
	}

	public void setDocBusinessInfo132(String docBusinessInfo132) {
		this.docBusinessInfo132 = docBusinessInfo132;
	}

	public String getDocBusinessInfo133() {
		return this.docBusinessInfo133;
	}

	public void setDocBusinessInfo133(String docBusinessInfo133) {
		this.docBusinessInfo133 = docBusinessInfo133;
	}

	public String getDocBusinessInfo134() {
		return this.docBusinessInfo134;
	}

	public void setDocBusinessInfo134(String docBusinessInfo134) {
		this.docBusinessInfo134 = docBusinessInfo134;
	}

	public String getDocBusinessInfo135() {
		return this.docBusinessInfo135;
	}

	public void setDocBusinessInfo135(String docBusinessInfo135) {
		this.docBusinessInfo135 = docBusinessInfo135;
	}

	public String getDocBusinessInfo136() {
		return this.docBusinessInfo136;
	}

	public void setDocBusinessInfo136(String docBusinessInfo136) {
		this.docBusinessInfo136 = docBusinessInfo136;
	}

	public String getDocBusinessInfo137() {
		return this.docBusinessInfo137;
	}

	public void setDocBusinessInfo137(String docBusinessInfo137) {
		this.docBusinessInfo137 = docBusinessInfo137;
	}

	public String getDocBusinessInfo138() {
		return this.docBusinessInfo138;
	}

	public void setDocBusinessInfo138(String docBusinessInfo138) {
		this.docBusinessInfo138 = docBusinessInfo138;
	}

	public String getDocBusinessInfo139() {
		return this.docBusinessInfo139;
	}

	public void setDocBusinessInfo139(String docBusinessInfo139) {
		this.docBusinessInfo139 = docBusinessInfo139;
	}

	public String getDocBusinessInfo140() {
		return this.docBusinessInfo140;
	}

	public void setDocBusinessInfo140(String docBusinessInfo140) {
		this.docBusinessInfo140 = docBusinessInfo140;
	}

	public String getDocBusinessInfo141() {
		return this.docBusinessInfo141;
	}

	public void setDocBusinessInfo141(String docBusinessInfo141) {
		this.docBusinessInfo141 = docBusinessInfo141;
	}

	public String getDocBusinessInfo142() {
		return this.docBusinessInfo142;
	}

	public void setDocBusinessInfo142(String docBusinessInfo142) {
		this.docBusinessInfo142 = docBusinessInfo142;
	}

	public String getDocBusinessInfo143() {
		return this.docBusinessInfo143;
	}

	public void setDocBusinessInfo143(String docBusinessInfo143) {
		this.docBusinessInfo143 = docBusinessInfo143;
	}

	public String getDocBusinessInfo144() {
		return this.docBusinessInfo144;
	}

	public void setDocBusinessInfo144(String docBusinessInfo144) {
		this.docBusinessInfo144 = docBusinessInfo144;
	}

	public String getDocBusinessInfo145() {
		return this.docBusinessInfo145;
	}

	public void setDocBusinessInfo145(String docBusinessInfo145) {
		this.docBusinessInfo145 = docBusinessInfo145;
	}

	public String getDocBusinessInfo146() {
		return this.docBusinessInfo146;
	}

	public void setDocBusinessInfo146(String docBusinessInfo146) {
		this.docBusinessInfo146 = docBusinessInfo146;
	}

	public String getDocBusinessInfo147() {
		return this.docBusinessInfo147;
	}

	public void setDocBusinessInfo147(String docBusinessInfo147) {
		this.docBusinessInfo147 = docBusinessInfo147;
	}

	public String getDocBusinessInfo148() {
		return this.docBusinessInfo148;
	}

	public void setDocBusinessInfo148(String docBusinessInfo148) {
		this.docBusinessInfo148 = docBusinessInfo148;
	}

	public String getDocBusinessInfo149() {
		return this.docBusinessInfo149;
	}

	public void setDocBusinessInfo149(String docBusinessInfo149) {
		this.docBusinessInfo149 = docBusinessInfo149;
	}

	public String getDocBusinessInfo150() {
		return this.docBusinessInfo150;
	}

	public void setDocBusinessInfo150(String docBusinessInfo150) {
		this.docBusinessInfo150 = docBusinessInfo150;
	}

	public String getDocBusinessInfo151() {
		return this.docBusinessInfo151;
	}

	public void setDocBusinessInfo151(String docBusinessInfo151) {
		this.docBusinessInfo151 = docBusinessInfo151;
	}

	public String getDocBusinessInfo152() {
		return this.docBusinessInfo152;
	}

	public void setDocBusinessInfo152(String docBusinessInfo152) {
		this.docBusinessInfo152 = docBusinessInfo152;
	}

	public String getDocBusinessInfo153() {
		return this.docBusinessInfo153;
	}

	public void setDocBusinessInfo153(String docBusinessInfo153) {
		this.docBusinessInfo153 = docBusinessInfo153;
	}

	public String getDocBusinessInfo154() {
		return this.docBusinessInfo154;
	}

	public void setDocBusinessInfo154(String docBusinessInfo154) {
		this.docBusinessInfo154 = docBusinessInfo154;
	}

	public String getDocBusinessInfo155() {
		return this.docBusinessInfo155;
	}

	public void setDocBusinessInfo155(String docBusinessInfo155) {
		this.docBusinessInfo155 = docBusinessInfo155;
	}

	public String getDocBusinessInfo156() {
		return this.docBusinessInfo156;
	}

	public void setDocBusinessInfo156(String docBusinessInfo156) {
		this.docBusinessInfo156 = docBusinessInfo156;
	}

	public String getDocBusinessInfo157() {
		return this.docBusinessInfo157;
	}

	public void setDocBusinessInfo157(String docBusinessInfo157) {
		this.docBusinessInfo157 = docBusinessInfo157;
	}

	public String getDocBusinessInfo158() {
		return this.docBusinessInfo158;
	}

	public void setDocBusinessInfo158(String docBusinessInfo158) {
		this.docBusinessInfo158 = docBusinessInfo158;
	}

	public String getDocBusinessInfo159() {
		return this.docBusinessInfo159;
	}

	public void setDocBusinessInfo159(String docBusinessInfo159) {
		this.docBusinessInfo159 = docBusinessInfo159;
	}

	public String getDocBusinessInfo160() {
		return this.docBusinessInfo160;
	}

	public void setDocBusinessInfo160(String docBusinessInfo160) {
		this.docBusinessInfo160 = docBusinessInfo160;
	}

	public String getDocBusinessInfo161() {
		return this.docBusinessInfo161;
	}

	public void setDocBusinessInfo161(String docBusinessInfo161) {
		this.docBusinessInfo161 = docBusinessInfo161;
	}

	public String getDocBusinessInfo162() {
		return this.docBusinessInfo162;
	}

	public void setDocBusinessInfo162(String docBusinessInfo162) {
		this.docBusinessInfo162 = docBusinessInfo162;
	}

	public String getDocBusinessInfo163() {
		return this.docBusinessInfo163;
	}

	public void setDocBusinessInfo163(String docBusinessInfo163) {
		this.docBusinessInfo163 = docBusinessInfo163;
	}

	public String getDocBusinessInfo164() {
		return this.docBusinessInfo164;
	}

	public void setDocBusinessInfo164(String docBusinessInfo164) {
		this.docBusinessInfo164 = docBusinessInfo164;
	}

	public String getDocBusinessInfo165() {
		return this.docBusinessInfo165;
	}

	public void setDocBusinessInfo165(String docBusinessInfo165) {
		this.docBusinessInfo165 = docBusinessInfo165;
	}

	public String getDocBusinessInfo166() {
		return this.docBusinessInfo166;
	}

	public void setDocBusinessInfo166(String docBusinessInfo166) {
		this.docBusinessInfo166 = docBusinessInfo166;
	}

	public String getDocBusinessInfo167() {
		return this.docBusinessInfo167;
	}

	public void setDocBusinessInfo167(String docBusinessInfo167) {
		this.docBusinessInfo167 = docBusinessInfo167;
	}

	public String getDocBusinessInfo168() {
		return this.docBusinessInfo168;
	}

	public void setDocBusinessInfo168(String docBusinessInfo168) {
		this.docBusinessInfo168 = docBusinessInfo168;
	}

	public String getDocBusinessInfo169() {
		return this.docBusinessInfo169;
	}

	public void setDocBusinessInfo169(String docBusinessInfo169) {
		this.docBusinessInfo169 = docBusinessInfo169;
	}

	public String getDocBusinessInfo170() {
		return this.docBusinessInfo170;
	}

	public void setDocBusinessInfo170(String docBusinessInfo170) {
		this.docBusinessInfo170 = docBusinessInfo170;
	}

	public String getDocBusinessInfo171() {
		return this.docBusinessInfo171;
	}

	public void setDocBusinessInfo171(String docBusinessInfo171) {
		this.docBusinessInfo171 = docBusinessInfo171;
	}

	public String getDocBusinessInfo172() {
		return this.docBusinessInfo172;
	}

	public void setDocBusinessInfo172(String docBusinessInfo172) {
		this.docBusinessInfo172 = docBusinessInfo172;
	}

	public String getDocBusinessInfo173() {
		return this.docBusinessInfo173;
	}

	public void setDocBusinessInfo173(String docBusinessInfo173) {
		this.docBusinessInfo173 = docBusinessInfo173;
	}

	public String getDocBusinessInfo174() {
		return this.docBusinessInfo174;
	}

	public void setDocBusinessInfo174(String docBusinessInfo174) {
		this.docBusinessInfo174 = docBusinessInfo174;
	}

	public String getDocBusinessInfo175() {
		return this.docBusinessInfo175;
	}

	public void setDocBusinessInfo175(String docBusinessInfo175) {
		this.docBusinessInfo175 = docBusinessInfo175;
	}

	public String getDocBusinessInfo176() {
		return this.docBusinessInfo176;
	}

	public void setDocBusinessInfo176(String docBusinessInfo176) {
		this.docBusinessInfo176 = docBusinessInfo176;
	}

	public String getDocBusinessInfo177() {
		return this.docBusinessInfo177;
	}

	public void setDocBusinessInfo177(String docBusinessInfo177) {
		this.docBusinessInfo177 = docBusinessInfo177;
	}

	public String getDocBusinessInfo178() {
		return this.docBusinessInfo178;
	}

	public void setDocBusinessInfo178(String docBusinessInfo178) {
		this.docBusinessInfo178 = docBusinessInfo178;
	}

	public String getDocBusinessInfo179() {
		return this.docBusinessInfo179;
	}

	public void setDocBusinessInfo179(String docBusinessInfo179) {
		this.docBusinessInfo179 = docBusinessInfo179;
	}

	public String getDocBusinessInfo180() {
		return this.docBusinessInfo180;
	}

	public void setDocBusinessInfo180(String docBusinessInfo180) {
		this.docBusinessInfo180 = docBusinessInfo180;
	}

	public String getDocBusinessInfo181() {
		return this.docBusinessInfo181;
	}

	public void setDocBusinessInfo181(String docBusinessInfo181) {
		this.docBusinessInfo181 = docBusinessInfo181;
	}

	public String getDocBusinessInfo182() {
		return this.docBusinessInfo182;
	}

	public void setDocBusinessInfo182(String docBusinessInfo182) {
		this.docBusinessInfo182 = docBusinessInfo182;
	}

	public String getDocBusinessInfo183() {
		return this.docBusinessInfo183;
	}

	public void setDocBusinessInfo183(String docBusinessInfo183) {
		this.docBusinessInfo183 = docBusinessInfo183;
	}

	public String getDocBusinessInfo184() {
		return this.docBusinessInfo184;
	}

	public void setDocBusinessInfo184(String docBusinessInfo184) {
		this.docBusinessInfo184 = docBusinessInfo184;
	}

	public String getDocBusinessInfo185() {
		return this.docBusinessInfo185;
	}

	public void setDocBusinessInfo185(String docBusinessInfo185) {
		this.docBusinessInfo185 = docBusinessInfo185;
	}

	public String getDocBusinessInfo186() {
		return this.docBusinessInfo186;
	}

	public void setDocBusinessInfo186(String docBusinessInfo186) {
		this.docBusinessInfo186 = docBusinessInfo186;
	}

	public String getDocBusinessInfo187() {
		return this.docBusinessInfo187;
	}

	public void setDocBusinessInfo187(String docBusinessInfo187) {
		this.docBusinessInfo187 = docBusinessInfo187;
	}

	public String getDocBusinessInfo188() {
		return this.docBusinessInfo188;
	}

	public void setDocBusinessInfo188(String docBusinessInfo188) {
		this.docBusinessInfo188 = docBusinessInfo188;
	}

	public String getDocBusinessInfo189() {
		return this.docBusinessInfo189;
	}

	public void setDocBusinessInfo189(String docBusinessInfo189) {
		this.docBusinessInfo189 = docBusinessInfo189;
	}

	public String getDocBusinessInfo190() {
		return this.docBusinessInfo190;
	}

	public void setDocBusinessInfo190(String docBusinessInfo190) {
		this.docBusinessInfo190 = docBusinessInfo190;
	}

	public String getDocBusinessInfo191() {
		return this.docBusinessInfo191;
	}

	public void setDocBusinessInfo191(String docBusinessInfo191) {
		this.docBusinessInfo191 = docBusinessInfo191;
	}

	public String getDocBusinessInfo192() {
		return this.docBusinessInfo192;
	}

	public void setDocBusinessInfo192(String docBusinessInfo192) {
		this.docBusinessInfo192 = docBusinessInfo192;
	}

	public String getDocBusinessInfo193() {
		return this.docBusinessInfo193;
	}

	public void setDocBusinessInfo193(String docBusinessInfo193) {
		this.docBusinessInfo193 = docBusinessInfo193;
	}

	public String getDocBusinessInfo194() {
		return this.docBusinessInfo194;
	}

	public void setDocBusinessInfo194(String docBusinessInfo194) {
		this.docBusinessInfo194 = docBusinessInfo194;
	}

	public String getDocBusinessInfo195() {
		return this.docBusinessInfo195;
	}

	public void setDocBusinessInfo195(String docBusinessInfo195) {
		this.docBusinessInfo195 = docBusinessInfo195;
	}

	public String getDocBusinessInfo196() {
		return this.docBusinessInfo196;
	}

	public void setDocBusinessInfo196(String docBusinessInfo196) {
		this.docBusinessInfo196 = docBusinessInfo196;
	}

	public String getDocBusinessInfo197() {
		return this.docBusinessInfo197;
	}

	public void setDocBusinessInfo197(String docBusinessInfo197) {
		this.docBusinessInfo197 = docBusinessInfo197;
	}

	public String getDocBusinessInfo198() {
		return this.docBusinessInfo198;
	}

	public void setDocBusinessInfo198(String docBusinessInfo198) {
		this.docBusinessInfo198 = docBusinessInfo198;
	}

	public String getDocBusinessInfo199() {
		return this.docBusinessInfo199;
	}

	public void setDocBusinessInfo199(String docBusinessInfo199) {
		this.docBusinessInfo199 = docBusinessInfo199;
	}

	public String getDocBusinessInfo200() {
		return this.docBusinessInfo200;
	}

	public void setDocBusinessInfo200(String docBusinessInfo200) {
		this.docBusinessInfo200 = docBusinessInfo200;
	}

	public String getDocBusinessInfo201() {
		return this.docBusinessInfo201;
	}

	public void setDocBusinessInfo201(String docBusinessInfo201) {
		this.docBusinessInfo201 = docBusinessInfo201;
	}

	public String getDocBusinessInfo202() {
		return this.docBusinessInfo202;
	}

	public void setDocBusinessInfo202(String docBusinessInfo202) {
		this.docBusinessInfo202 = docBusinessInfo202;
	}

	public String getDocBusinessInfo203() {
		return this.docBusinessInfo203;
	}

	public void setDocBusinessInfo203(String docBusinessInfo203) {
		this.docBusinessInfo203 = docBusinessInfo203;
	}

	public String getDocBusinessInfo204() {
		return this.docBusinessInfo204;
	}

	public void setDocBusinessInfo204(String docBusinessInfo204) {
		this.docBusinessInfo204 = docBusinessInfo204;
	}

	public String getDocBusinessInfo205() {
		return this.docBusinessInfo205;
	}

	public void setDocBusinessInfo205(String docBusinessInfo205) {
		this.docBusinessInfo205 = docBusinessInfo205;
	}

	public String getDocBusinessInfo206() {
		return this.docBusinessInfo206;
	}

	public void setDocBusinessInfo206(String docBusinessInfo206) {
		this.docBusinessInfo206 = docBusinessInfo206;
	}

	public String getDocBusinessInfo207() {
		return this.docBusinessInfo207;
	}

	public void setDocBusinessInfo207(String docBusinessInfo207) {
		this.docBusinessInfo207 = docBusinessInfo207;
	}

	public String getDocBusinessInfo208() {
		return this.docBusinessInfo208;
	}

	public void setDocBusinessInfo208(String docBusinessInfo208) {
		this.docBusinessInfo208 = docBusinessInfo208;
	}

	public String getDocBusinessInfo209() {
		return this.docBusinessInfo209;
	}

	public void setDocBusinessInfo209(String docBusinessInfo209) {
		this.docBusinessInfo209 = docBusinessInfo209;
	}

	public String getDocBusinessInfo210() {
		return this.docBusinessInfo210;
	}

	public void setDocBusinessInfo210(String docBusinessInfo210) {
		this.docBusinessInfo210 = docBusinessInfo210;
	}

	public String getDocBusinessInfo211() {
		return this.docBusinessInfo211;
	}

	public void setDocBusinessInfo211(String docBusinessInfo211) {
		this.docBusinessInfo211 = docBusinessInfo211;
	}

	public String getDocBusinessInfo212() {
		return this.docBusinessInfo212;
	}

	public void setDocBusinessInfo212(String docBusinessInfo212) {
		this.docBusinessInfo212 = docBusinessInfo212;
	}

	public String getDocBusinessInfo213() {
		return this.docBusinessInfo213;
	}

	public void setDocBusinessInfo213(String docBusinessInfo213) {
		this.docBusinessInfo213 = docBusinessInfo213;
	}

	public String getDocBusinessInfo214() {
		return this.docBusinessInfo214;
	}

	public void setDocBusinessInfo214(String docBusinessInfo214) {
		this.docBusinessInfo214 = docBusinessInfo214;
	}

	public String getDocBusinessInfo215() {
		return this.docBusinessInfo215;
	}

	public void setDocBusinessInfo215(String docBusinessInfo215) {
		this.docBusinessInfo215 = docBusinessInfo215;
	}

	public String getDocBusinessInfo216() {
		return this.docBusinessInfo216;
	}

	public void setDocBusinessInfo216(String docBusinessInfo216) {
		this.docBusinessInfo216 = docBusinessInfo216;
	}

	public String getDocBusinessInfo217() {
		return this.docBusinessInfo217;
	}

	public void setDocBusinessInfo217(String docBusinessInfo217) {
		this.docBusinessInfo217 = docBusinessInfo217;
	}

	public String getDocBusinessInfo218() {
		return this.docBusinessInfo218;
	}

	public void setDocBusinessInfo218(String docBusinessInfo218) {
		this.docBusinessInfo218 = docBusinessInfo218;
	}

	public String getDocBusinessInfo219() {
		return this.docBusinessInfo219;
	}

	public void setDocBusinessInfo219(String docBusinessInfo219) {
		this.docBusinessInfo219 = docBusinessInfo219;
	}

	public String getDocBusinessInfo220() {
		return this.docBusinessInfo220;
	}

	public void setDocBusinessInfo220(String docBusinessInfo220) {
		this.docBusinessInfo220 = docBusinessInfo220;
	}

	public String getDocBusinessInfo221() {
		return this.docBusinessInfo221;
	}

	public void setDocBusinessInfo221(String docBusinessInfo221) {
		this.docBusinessInfo221 = docBusinessInfo221;
	}

	public String getDocBusinessInfo222() {
		return this.docBusinessInfo222;
	}

	public void setDocBusinessInfo222(String docBusinessInfo222) {
		this.docBusinessInfo222 = docBusinessInfo222;
	}

	public String getDocBusinessInfo223() {
		return this.docBusinessInfo223;
	}

	public void setDocBusinessInfo223(String docBusinessInfo223) {
		this.docBusinessInfo223 = docBusinessInfo223;
	}

	public String getDocBusinessInfo224() {
		return this.docBusinessInfo224;
	}

	public void setDocBusinessInfo224(String docBusinessInfo224) {
		this.docBusinessInfo224 = docBusinessInfo224;
	}

	public String getDocBusinessInfo225() {
		return this.docBusinessInfo225;
	}

	public void setDocBusinessInfo225(String docBusinessInfo225) {
		this.docBusinessInfo225 = docBusinessInfo225;
	}

	public String getDocBusinessInfo226() {
		return this.docBusinessInfo226;
	}

	public void setDocBusinessInfo226(String docBusinessInfo226) {
		this.docBusinessInfo226 = docBusinessInfo226;
	}

	public String getDocBusinessInfo227() {
		return this.docBusinessInfo227;
	}

	public void setDocBusinessInfo227(String docBusinessInfo227) {
		this.docBusinessInfo227 = docBusinessInfo227;
	}

	public String getDocBusinessInfo228() {
		return this.docBusinessInfo228;
	}

	public void setDocBusinessInfo228(String docBusinessInfo228) {
		this.docBusinessInfo228 = docBusinessInfo228;
	}

	public String getDocBusinessInfo229() {
		return this.docBusinessInfo229;
	}

	public void setDocBusinessInfo229(String docBusinessInfo229) {
		this.docBusinessInfo229 = docBusinessInfo229;
	}

	public String getDocBusinessInfo230() {
		return this.docBusinessInfo230;
	}

	public void setDocBusinessInfo230(String docBusinessInfo230) {
		this.docBusinessInfo230 = docBusinessInfo230;
	}

	public String getDocBusinessInfo231() {
		return this.docBusinessInfo231;
	}

	public void setDocBusinessInfo231(String docBusinessInfo231) {
		this.docBusinessInfo231 = docBusinessInfo231;
	}

	public String getDocBusinessInfo232() {
		return this.docBusinessInfo232;
	}

	public void setDocBusinessInfo232(String docBusinessInfo232) {
		this.docBusinessInfo232 = docBusinessInfo232;
	}

	public String getDocBusinessInfo233() {
		return this.docBusinessInfo233;
	}

	public void setDocBusinessInfo233(String docBusinessInfo233) {
		this.docBusinessInfo233 = docBusinessInfo233;
	}

	public String getDocBusinessInfo234() {
		return this.docBusinessInfo234;
	}

	public void setDocBusinessInfo234(String docBusinessInfo234) {
		this.docBusinessInfo234 = docBusinessInfo234;
	}

	public String getDocBusinessInfo235() {
		return this.docBusinessInfo235;
	}

	public void setDocBusinessInfo235(String docBusinessInfo235) {
		this.docBusinessInfo235 = docBusinessInfo235;
	}

	public String getDocBusinessInfo236() {
		return this.docBusinessInfo236;
	}

	public void setDocBusinessInfo236(String docBusinessInfo236) {
		this.docBusinessInfo236 = docBusinessInfo236;
	}

	public String getDocBusinessInfo237() {
		return this.docBusinessInfo237;
	}

	public void setDocBusinessInfo237(String docBusinessInfo237) {
		this.docBusinessInfo237 = docBusinessInfo237;
	}

	public String getDocBusinessInfo238() {
		return this.docBusinessInfo238;
	}

	public void setDocBusinessInfo238(String docBusinessInfo238) {
		this.docBusinessInfo238 = docBusinessInfo238;
	}

	public String getDocBusinessInfo239() {
		return this.docBusinessInfo239;
	}

	public void setDocBusinessInfo239(String docBusinessInfo239) {
		this.docBusinessInfo239 = docBusinessInfo239;
	}

	public String getDocBusinessInfo240() {
		return this.docBusinessInfo240;
	}

	public void setDocBusinessInfo240(String docBusinessInfo240) {
		this.docBusinessInfo240 = docBusinessInfo240;
	}

	public String getDocBusinessInfo241() {
		return this.docBusinessInfo241;
	}

	public void setDocBusinessInfo241(String docBusinessInfo241) {
		this.docBusinessInfo241 = docBusinessInfo241;
	}

	public String getDocBusinessInfo242() {
		return this.docBusinessInfo242;
	}

	public void setDocBusinessInfo242(String docBusinessInfo242) {
		this.docBusinessInfo242 = docBusinessInfo242;
	}

	public String getDocBusinessInfo243() {
		return this.docBusinessInfo243;
	}

	public void setDocBusinessInfo243(String docBusinessInfo243) {
		this.docBusinessInfo243 = docBusinessInfo243;
	}

	public String getDocBusinessInfo244() {
		return this.docBusinessInfo244;
	}

	public void setDocBusinessInfo244(String docBusinessInfo244) {
		this.docBusinessInfo244 = docBusinessInfo244;
	}

	public String getDocBusinessInfo245() {
		return this.docBusinessInfo245;
	}

	public void setDocBusinessInfo245(String docBusinessInfo245) {
		this.docBusinessInfo245 = docBusinessInfo245;
	}

	public String getDocBusinessInfo246() {
		return this.docBusinessInfo246;
	}

	public void setDocBusinessInfo246(String docBusinessInfo246) {
		this.docBusinessInfo246 = docBusinessInfo246;
	}

	public String getDocBusinessInfo247() {
		return this.docBusinessInfo247;
	}

	public void setDocBusinessInfo247(String docBusinessInfo247) {
		this.docBusinessInfo247 = docBusinessInfo247;
	}

	public String getDocBusinessInfo248() {
		return this.docBusinessInfo248;
	}

	public void setDocBusinessInfo248(String docBusinessInfo248) {
		this.docBusinessInfo248 = docBusinessInfo248;
	}

	public String getDocBusinessInfo249() {
		return this.docBusinessInfo249;
	}

	public void setDocBusinessInfo249(String docBusinessInfo249) {
		this.docBusinessInfo249 = docBusinessInfo249;
	}

	public String getDocBusinessInfo250() {
		return this.docBusinessInfo250;
	}

	public void setDocBusinessInfo250(String docBusinessInfo250) {
		this.docBusinessInfo250 = docBusinessInfo250;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getDocNum() {
		return this.docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public Integer getHistorySeqNo() {
		return this.historySeqNo;
	}

	public void setHistorySeqNo(Integer historySeqNo) {
		this.historySeqNo = historySeqNo;
	}

	public String getLockCorporationCode() {
		return this.lockCorporationCode;
	}

	public void setLockCorporationCode(String lockCorporationCode) {
		this.lockCorporationCode = lockCorporationCode;
	}

	public String getLockFlag() {
		return this.lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public Timestamp getLockTimestamp() {
		return this.lockTimestamp;
	}

	public void setLockTimestamp(Timestamp lockTimestamp) {
		this.lockTimestamp = lockTimestamp;
	}

	public String getLockUserCode() {
		return this.lockUserCode;
	}

	public void setLockUserCode(String lockUserCode) {
		this.lockUserCode = lockUserCode;
	}

	public String getLockUserName() {
		return this.lockUserName;
	}

	public void setLockUserName(String lockUserName) {
		this.lockUserName = lockUserName;
	}

	public Integer getMajorVersion() {
		return this.majorVersion;
	}

	public void setMajorVersion(Integer majorVersion) {
		this.majorVersion = majorVersion;
	}

	public Long getMetaTemplateId() {
		return this.metaTemplateId;
	}

	public void setMetaTemplateId(Long metaTemplateId) {
		this.metaTemplateId = metaTemplateId;
	}

	public Integer getMinorVersion() {
		return this.minorVersion;
	}

	public void setMinorVersion(Integer minorVersion) {
		this.minorVersion = minorVersion;
	}

	public String getOwnerCorporationCode() {
		return this.ownerCorporationCode;
	}

	public void setOwnerCorporationCode(String ownerCorporationCode) {
		this.ownerCorporationCode = ownerCorporationCode;
	}

	public String getOwnerUserCode() {
		return this.ownerUserCode;
	}

	public void setOwnerUserCode(String ownerUserCode) {
		this.ownerUserCode = ownerUserCode;
	}

	public String getOwnerUserName() {
		return this.ownerUserName;
	}

	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getPublishCorporationCode() {
		return this.publishCorporationCode;
	}

	public void setPublishCorporationCode(String publishCorporationCode) {
		this.publishCorporationCode = publishCorporationCode;
	}

	public Date getPublishEndDate() {
		return this.publishEndDate;
	}

	public void setPublishEndDate(Date publishEndDate) {
		this.publishEndDate = publishEndDate;
	}

	public String getPublishFlag() {
		return this.publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}

	public Date getPublishStartDate() {
		return this.publishStartDate;
	}

	public void setPublishStartDate(Date publishStartDate) {
		this.publishStartDate = publishStartDate;
	}

	public Timestamp getPublishTimestamp() {
		return this.publishTimestamp;
	}

	public void setPublishTimestamp(Timestamp publishTimestamp) {
		this.publishTimestamp = publishTimestamp;
	}

	public String getPublishUserCode() {
		return this.publishUserCode;
	}

	public void setPublishUserCode(String publishUserCode) {
		this.publishUserCode = publishUserCode;
	}

	public String getPublishUserName() {
		return this.publishUserName;
	}

	public void setPublishUserName(String publishUserName) {
		this.publishUserName = publishUserName;
	}

	public Integer getRetentionTerm() {
		return this.retentionTerm;
	}

	public void setRetentionTerm(Integer retentionTerm) {
		this.retentionTerm = retentionTerm;
	}

	public String getRetentionTermType() {
		return this.retentionTermType;
	}

	public void setRetentionTermType(String retentionTermType) {
		this.retentionTermType = retentionTermType;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserNameCreated() {
		return this.userNameCreated;
	}

	public void setUserNameCreated(String userNameCreated) {
		this.userNameCreated = userNameCreated;
	}

	public String getUserNameUpdated() {
		return this.userNameUpdated;
	}

	public void setUserNameUpdated(String userNameUpdated) {
		this.userNameUpdated = userNameUpdated;
	}

}