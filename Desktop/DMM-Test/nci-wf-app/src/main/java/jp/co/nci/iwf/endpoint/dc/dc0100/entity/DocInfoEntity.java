package jp.co.nci.iwf.endpoint.dc.dc0100.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class DocInfoEntity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	public Long id;

	@Column(name="DOC_ID")
	public Long docId;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="CONTENTS_TYPE")
	public String contentsType;
	@Column(name="MAJOR_VERSION")
	public Integer majorVersion;
	@Column(name="MINOR_VERSION")
	public Integer minorVersion;
	@Column(name="TITLE")
	public String title;
	@Column(name="COMMENTS")
	public String comments;
	@Column(name="PUBLISH_TIMESTAMP")
	public Timestamp publishTimestamp;
	@Column(name="PUBLISH_CORPORATION_CODE")
	public String publishCorporationCode;
	@Column(name="PUBLISH_CORPORATION_NAME")
	public String publishCorporationName;
	@Column(name="PUBLISH_USER_CODE")
	public String publishUserCode;
	@Column(name="PUBLISH_USER_NAME")
	public String publishUserName;
	@Column(name="OWNER_CORPORATION_CODE")
	public String ownerCorporationCode;
	@Column(name="OWNER_CORPORATION_NAME")
	public String ownerCorporationName;
	@Column(name="OWNER_USER_CODE")
	public String ownerUserCode;
	@Column(name="OWNER_USER_NAME")
	public String ownerUserName;
	@Column(name="PUBLISH_FLAG")
	public String publishFlag;
	@Temporal(TemporalType.DATE)
	@Column(name="PUBLISH_START_DATE")
	public Date publishStartDate;
	@Temporal(TemporalType.DATE)
	@Column(name="PUBLISH_END_DATE")
	public Date publishEndDate;
	@Column(name="RETENTION_TERM_TYPE")
	public String retentionTermType;
	@Column(name="RETENTION_TERM")
	public Integer retentionTerm;
	@Column(name="DISP_COUNT")
	public Integer dispCount;
	@Column(name="LOCK_FLAG")
	public String lockFlag;
	@Column(name="LOCK_TIMESTAMP")
	public Timestamp lockTimestamp;
	@Column(name="LOCK_CORPORATION_CODE")
	public String lockCorporationCode;
	@Column(name="LOCK_CORPORATION_NAME")
	public String lockCorporationName;
	@Column(name="LOCK_USER_CODE")
	public String lockUserCode;
	@Column(name="LOCK_USER_NAME")
	public String lockUserName;
	@Column(name="USER_NAME_CREATED")
	public String userNameCreated;
	@Column(name="USER_NAME_UPDATED")
	public String userNameUpdated;
	@Column(name="TIMESTAMP_CREATED")
	public Timestamp timestampCreated;
	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;
	@Column(name="VERSION")
	public Long version;
	@Column(name="BIZ_DOC_ID")
	public Long bizDocId;
	@Column(name="BINDER_ID")
	public Long binderId;
	@Column(name="AUTH_REFER")
	public String authRefer;
	@Column(name="AUTH_DOWNLOAD")
	public String authDownload;
	@Column(name="AUTH_EDIT")
	public String authEdit;
	@Column(name="AUTH_DELETE")
	public String authDelete;
	@Column(name="AUTH_COPY")
	public String authCopy;
	@Column(name="AUTH_MOVE")
	public String authMove;
	@Column(name="AUTH_PRINT")
	public String authPrint;
	@Column(name="DOC_FOLDER_RELATION_ID")
	public Long docFolderRelationId;
	@Column(name="DOC_FOLDER_ID")
	public Long docFolderId;
	@Column(name="FOLDER_PATH")
	public String folderPath;
	@Column(name="PUBLISH_FLAG_NAME")
	public String publishFlagName;
	@Column(name="RETENTION_TERM_TYPE_NAME")
	public String retentionTermTypeName;
	@Column(name="CONTENTS_TYPE_NAME")
	public String contentsTypeName;
	@Column(name="OWN_LOCK_FLAG")
	public String ownLockFlag;
	@Column(name="WF_APPLYING")
	public String wfApplying;
	@Column(name="PROCESS_ID")
	public Long processId;
	// 以下、文書管理項目
	@Column(name="DOC_BUSINESS_INFO_001")
	public String docBusinessInfo001;
	@Column(name="DOC_BUSINESS_INFO_002")
	public String docBusinessInfo002;
	@Column(name="DOC_BUSINESS_INFO_003")
	public String docBusinessInfo003;
	@Column(name="DOC_BUSINESS_INFO_004")
	public String docBusinessInfo004;
	@Column(name="DOC_BUSINESS_INFO_005")
	public String docBusinessInfo005;
	@Column(name="DOC_BUSINESS_INFO_006")
	public String docBusinessInfo006;
	@Column(name="DOC_BUSINESS_INFO_007")
	public String docBusinessInfo007;
	@Column(name="DOC_BUSINESS_INFO_008")
	public String docBusinessInfo008;
	@Column(name="DOC_BUSINESS_INFO_009")
	public String docBusinessInfo009;
	@Column(name="DOC_BUSINESS_INFO_010")
	public String docBusinessInfo010;
	@Column(name="DOC_BUSINESS_INFO_011")
	public String docBusinessInfo011;
	@Column(name="DOC_BUSINESS_INFO_012")
	public String docBusinessInfo012;
	@Column(name="DOC_BUSINESS_INFO_013")
	public String docBusinessInfo013;
	@Column(name="DOC_BUSINESS_INFO_014")
	public String docBusinessInfo014;
	@Column(name="DOC_BUSINESS_INFO_015")
	public String docBusinessInfo015;
	@Column(name="DOC_BUSINESS_INFO_016")
	public String docBusinessInfo016;
	@Column(name="DOC_BUSINESS_INFO_017")
	public String docBusinessInfo017;
	@Column(name="DOC_BUSINESS_INFO_018")
	public String docBusinessInfo018;
	@Column(name="DOC_BUSINESS_INFO_019")
	public String docBusinessInfo019;
	@Column(name="DOC_BUSINESS_INFO_020")
	public String docBusinessInfo020;
	@Column(name="DOC_BUSINESS_INFO_021")
	public String docBusinessInfo021;
	@Column(name="DOC_BUSINESS_INFO_022")
	public String docBusinessInfo022;
	@Column(name="DOC_BUSINESS_INFO_023")
	public String docBusinessInfo023;
	@Column(name="DOC_BUSINESS_INFO_024")
	public String docBusinessInfo024;
	@Column(name="DOC_BUSINESS_INFO_025")
	public String docBusinessInfo025;
	@Column(name="DOC_BUSINESS_INFO_026")
	public String docBusinessInfo026;
	@Column(name="DOC_BUSINESS_INFO_027")
	public String docBusinessInfo027;
	@Column(name="DOC_BUSINESS_INFO_028")
	public String docBusinessInfo028;
	@Column(name="DOC_BUSINESS_INFO_029")
	public String docBusinessInfo029;
	@Column(name="DOC_BUSINESS_INFO_030")
	public String docBusinessInfo030;
	@Column(name="DOC_BUSINESS_INFO_031")
	public String docBusinessInfo031;
	@Column(name="DOC_BUSINESS_INFO_032")
	public String docBusinessInfo032;
	@Column(name="DOC_BUSINESS_INFO_033")
	public String docBusinessInfo033;
	@Column(name="DOC_BUSINESS_INFO_034")
	public String docBusinessInfo034;
	@Column(name="DOC_BUSINESS_INFO_035")
	public String docBusinessInfo035;
	@Column(name="DOC_BUSINESS_INFO_036")
	public String docBusinessInfo036;
	@Column(name="DOC_BUSINESS_INFO_037")
	public String docBusinessInfo037;
	@Column(name="DOC_BUSINESS_INFO_038")
	public String docBusinessInfo038;
	@Column(name="DOC_BUSINESS_INFO_039")
	public String docBusinessInfo039;
	@Column(name="DOC_BUSINESS_INFO_040")
	public String docBusinessInfo040;
	@Column(name="DOC_BUSINESS_INFO_041")
	public String docBusinessInfo041;
	@Column(name="DOC_BUSINESS_INFO_042")
	public String docBusinessInfo042;
	@Column(name="DOC_BUSINESS_INFO_043")
	public String docBusinessInfo043;
	@Column(name="DOC_BUSINESS_INFO_044")
	public String docBusinessInfo044;
	@Column(name="DOC_BUSINESS_INFO_045")
	public String docBusinessInfo045;
	@Column(name="DOC_BUSINESS_INFO_046")
	public String docBusinessInfo046;
	@Column(name="DOC_BUSINESS_INFO_047")
	public String docBusinessInfo047;
	@Column(name="DOC_BUSINESS_INFO_048")
	public String docBusinessInfo048;
	@Column(name="DOC_BUSINESS_INFO_049")
	public String docBusinessInfo049;
	@Column(name="DOC_BUSINESS_INFO_050")
	public String docBusinessInfo050;
	@Column(name="DOC_BUSINESS_INFO_051")
	public String docBusinessInfo051;
	@Column(name="DOC_BUSINESS_INFO_052")
	public String docBusinessInfo052;
	@Column(name="DOC_BUSINESS_INFO_053")
	public String docBusinessInfo053;
	@Column(name="DOC_BUSINESS_INFO_054")
	public String docBusinessInfo054;
	@Column(name="DOC_BUSINESS_INFO_055")
	public String docBusinessInfo055;
	@Column(name="DOC_BUSINESS_INFO_056")
	public String docBusinessInfo056;
	@Column(name="DOC_BUSINESS_INFO_057")
	public String docBusinessInfo057;
	@Column(name="DOC_BUSINESS_INFO_058")
	public String docBusinessInfo058;
	@Column(name="DOC_BUSINESS_INFO_059")
	public String docBusinessInfo059;
	@Column(name="DOC_BUSINESS_INFO_060")
	public String docBusinessInfo060;
	@Column(name="DOC_BUSINESS_INFO_061")
	public String docBusinessInfo061;
	@Column(name="DOC_BUSINESS_INFO_062")
	public String docBusinessInfo062;
	@Column(name="DOC_BUSINESS_INFO_063")
	public String docBusinessInfo063;
	@Column(name="DOC_BUSINESS_INFO_064")
	public String docBusinessInfo064;
	@Column(name="DOC_BUSINESS_INFO_065")
	public String docBusinessInfo065;
	@Column(name="DOC_BUSINESS_INFO_066")
	public String docBusinessInfo066;
	@Column(name="DOC_BUSINESS_INFO_067")
	public String docBusinessInfo067;
	@Column(name="DOC_BUSINESS_INFO_068")
	public String docBusinessInfo068;
	@Column(name="DOC_BUSINESS_INFO_069")
	public String docBusinessInfo069;
	@Column(name="DOC_BUSINESS_INFO_070")
	public String docBusinessInfo070;
	@Column(name="DOC_BUSINESS_INFO_071")
	public String docBusinessInfo071;
	@Column(name="DOC_BUSINESS_INFO_072")
	public String docBusinessInfo072;
	@Column(name="DOC_BUSINESS_INFO_073")
	public String docBusinessInfo073;
	@Column(name="DOC_BUSINESS_INFO_074")
	public String docBusinessInfo074;
	@Column(name="DOC_BUSINESS_INFO_075")
	public String docBusinessInfo075;
	@Column(name="DOC_BUSINESS_INFO_076")
	public String docBusinessInfo076;
	@Column(name="DOC_BUSINESS_INFO_077")
	public String docBusinessInfo077;
	@Column(name="DOC_BUSINESS_INFO_078")
	public String docBusinessInfo078;
	@Column(name="DOC_BUSINESS_INFO_079")
	public String docBusinessInfo079;
	@Column(name="DOC_BUSINESS_INFO_080")
	public String docBusinessInfo080;
	@Column(name="DOC_BUSINESS_INFO_081")
	public String docBusinessInfo081;
	@Column(name="DOC_BUSINESS_INFO_082")
	public String docBusinessInfo082;
	@Column(name="DOC_BUSINESS_INFO_083")
	public String docBusinessInfo083;
	@Column(name="DOC_BUSINESS_INFO_084")
	public String docBusinessInfo084;
	@Column(name="DOC_BUSINESS_INFO_085")
	public String docBusinessInfo085;
	@Column(name="DOC_BUSINESS_INFO_086")
	public String docBusinessInfo086;
	@Column(name="DOC_BUSINESS_INFO_087")
	public String docBusinessInfo087;
	@Column(name="DOC_BUSINESS_INFO_088")
	public String docBusinessInfo088;
	@Column(name="DOC_BUSINESS_INFO_089")
	public String docBusinessInfo089;
	@Column(name="DOC_BUSINESS_INFO_090")
	public String docBusinessInfo090;
	@Column(name="DOC_BUSINESS_INFO_091")
	public String docBusinessInfo091;
	@Column(name="DOC_BUSINESS_INFO_092")
	public String docBusinessInfo092;
	@Column(name="DOC_BUSINESS_INFO_093")
	public String docBusinessInfo093;
	@Column(name="DOC_BUSINESS_INFO_094")
	public String docBusinessInfo094;
	@Column(name="DOC_BUSINESS_INFO_095")
	public String docBusinessInfo095;
	@Column(name="DOC_BUSINESS_INFO_096")
	public String docBusinessInfo096;
	@Column(name="DOC_BUSINESS_INFO_097")
	public String docBusinessInfo097;
	@Column(name="DOC_BUSINESS_INFO_098")
	public String docBusinessInfo098;
	@Column(name="DOC_BUSINESS_INFO_099")
	public String docBusinessInfo099;
	@Column(name="DOC_BUSINESS_INFO_100")
	public String docBusinessInfo100;
	@Column(name="DOC_BUSINESS_INFO_101")
	public String docBusinessInfo101;
	@Column(name="DOC_BUSINESS_INFO_102")
	public String docBusinessInfo102;
	@Column(name="DOC_BUSINESS_INFO_103")
	public String docBusinessInfo103;
	@Column(name="DOC_BUSINESS_INFO_104")
	public String docBusinessInfo104;
	@Column(name="DOC_BUSINESS_INFO_105")
	public String docBusinessInfo105;
	@Column(name="DOC_BUSINESS_INFO_106")
	public String docBusinessInfo106;
	@Column(name="DOC_BUSINESS_INFO_107")
	public String docBusinessInfo107;
	@Column(name="DOC_BUSINESS_INFO_108")
	public String docBusinessInfo108;
	@Column(name="DOC_BUSINESS_INFO_109")
	public String docBusinessInfo109;
	@Column(name="DOC_BUSINESS_INFO_110")
	public String docBusinessInfo110;
	@Column(name="DOC_BUSINESS_INFO_111")
	public String docBusinessInfo111;
	@Column(name="DOC_BUSINESS_INFO_112")
	public String docBusinessInfo112;
	@Column(name="DOC_BUSINESS_INFO_113")
	public String docBusinessInfo113;
	@Column(name="DOC_BUSINESS_INFO_114")
	public String docBusinessInfo114;
	@Column(name="DOC_BUSINESS_INFO_115")
	public String docBusinessInfo115;
	@Column(name="DOC_BUSINESS_INFO_116")
	public String docBusinessInfo116;
	@Column(name="DOC_BUSINESS_INFO_117")
	public String docBusinessInfo117;
	@Column(name="DOC_BUSINESS_INFO_118")
	public String docBusinessInfo118;
	@Column(name="DOC_BUSINESS_INFO_119")
	public String docBusinessInfo119;
	@Column(name="DOC_BUSINESS_INFO_120")
	public String docBusinessInfo120;
	@Column(name="DOC_BUSINESS_INFO_121")
	public String docBusinessInfo121;
	@Column(name="DOC_BUSINESS_INFO_122")
	public String docBusinessInfo122;
	@Column(name="DOC_BUSINESS_INFO_123")
	public String docBusinessInfo123;
	@Column(name="DOC_BUSINESS_INFO_124")
	public String docBusinessInfo124;
	@Column(name="DOC_BUSINESS_INFO_125")
	public String docBusinessInfo125;
	@Column(name="DOC_BUSINESS_INFO_126")
	public String docBusinessInfo126;
	@Column(name="DOC_BUSINESS_INFO_127")
	public String docBusinessInfo127;
	@Column(name="DOC_BUSINESS_INFO_128")
	public String docBusinessInfo128;
	@Column(name="DOC_BUSINESS_INFO_129")
	public String docBusinessInfo129;
	@Column(name="DOC_BUSINESS_INFO_130")
	public String docBusinessInfo130;
	@Column(name="DOC_BUSINESS_INFO_131")
	public String docBusinessInfo131;
	@Column(name="DOC_BUSINESS_INFO_132")
	public String docBusinessInfo132;
	@Column(name="DOC_BUSINESS_INFO_133")
	public String docBusinessInfo133;
	@Column(name="DOC_BUSINESS_INFO_134")
	public String docBusinessInfo134;
	@Column(name="DOC_BUSINESS_INFO_135")
	public String docBusinessInfo135;
	@Column(name="DOC_BUSINESS_INFO_136")
	public String docBusinessInfo136;
	@Column(name="DOC_BUSINESS_INFO_137")
	public String docBusinessInfo137;
	@Column(name="DOC_BUSINESS_INFO_138")
	public String docBusinessInfo138;
	@Column(name="DOC_BUSINESS_INFO_139")
	public String docBusinessInfo139;
	@Column(name="DOC_BUSINESS_INFO_140")
	public String docBusinessInfo140;
	@Column(name="DOC_BUSINESS_INFO_141")
	public String docBusinessInfo141;
	@Column(name="DOC_BUSINESS_INFO_142")
	public String docBusinessInfo142;
	@Column(name="DOC_BUSINESS_INFO_143")
	public String docBusinessInfo143;
	@Column(name="DOC_BUSINESS_INFO_144")
	public String docBusinessInfo144;
	@Column(name="DOC_BUSINESS_INFO_145")
	public String docBusinessInfo145;
	@Column(name="DOC_BUSINESS_INFO_146")
	public String docBusinessInfo146;
	@Column(name="DOC_BUSINESS_INFO_147")
	public String docBusinessInfo147;
	@Column(name="DOC_BUSINESS_INFO_148")
	public String docBusinessInfo148;
	@Column(name="DOC_BUSINESS_INFO_149")
	public String docBusinessInfo149;
	@Column(name="DOC_BUSINESS_INFO_150")
	public String docBusinessInfo150;
	@Column(name="DOC_BUSINESS_INFO_151")
	public String docBusinessInfo151;
	@Column(name="DOC_BUSINESS_INFO_152")
	public String docBusinessInfo152;
	@Column(name="DOC_BUSINESS_INFO_153")
	public String docBusinessInfo153;
	@Column(name="DOC_BUSINESS_INFO_154")
	public String docBusinessInfo154;
	@Column(name="DOC_BUSINESS_INFO_155")
	public String docBusinessInfo155;
	@Column(name="DOC_BUSINESS_INFO_156")
	public String docBusinessInfo156;
	@Column(name="DOC_BUSINESS_INFO_157")
	public String docBusinessInfo157;
	@Column(name="DOC_BUSINESS_INFO_158")
	public String docBusinessInfo158;
	@Column(name="DOC_BUSINESS_INFO_159")
	public String docBusinessInfo159;
	@Column(name="DOC_BUSINESS_INFO_160")
	public String docBusinessInfo160;
	@Column(name="DOC_BUSINESS_INFO_161")
	public String docBusinessInfo161;
	@Column(name="DOC_BUSINESS_INFO_162")
	public String docBusinessInfo162;
	@Column(name="DOC_BUSINESS_INFO_163")
	public String docBusinessInfo163;
	@Column(name="DOC_BUSINESS_INFO_164")
	public String docBusinessInfo164;
	@Column(name="DOC_BUSINESS_INFO_165")
	public String docBusinessInfo165;
	@Column(name="DOC_BUSINESS_INFO_166")
	public String docBusinessInfo166;
	@Column(name="DOC_BUSINESS_INFO_167")
	public String docBusinessInfo167;
	@Column(name="DOC_BUSINESS_INFO_168")
	public String docBusinessInfo168;
	@Column(name="DOC_BUSINESS_INFO_169")
	public String docBusinessInfo169;
	@Column(name="DOC_BUSINESS_INFO_170")
	public String docBusinessInfo170;
	@Column(name="DOC_BUSINESS_INFO_171")
	public String docBusinessInfo171;
	@Column(name="DOC_BUSINESS_INFO_172")
	public String docBusinessInfo172;
	@Column(name="DOC_BUSINESS_INFO_173")
	public String docBusinessInfo173;
	@Column(name="DOC_BUSINESS_INFO_174")
	public String docBusinessInfo174;
	@Column(name="DOC_BUSINESS_INFO_175")
	public String docBusinessInfo175;
	@Column(name="DOC_BUSINESS_INFO_176")
	public String docBusinessInfo176;
	@Column(name="DOC_BUSINESS_INFO_177")
	public String docBusinessInfo177;
	@Column(name="DOC_BUSINESS_INFO_178")
	public String docBusinessInfo178;
	@Column(name="DOC_BUSINESS_INFO_179")
	public String docBusinessInfo179;
	@Column(name="DOC_BUSINESS_INFO_180")
	public String docBusinessInfo180;
	@Column(name="DOC_BUSINESS_INFO_181")
	public String docBusinessInfo181;
	@Column(name="DOC_BUSINESS_INFO_182")
	public String docBusinessInfo182;
	@Column(name="DOC_BUSINESS_INFO_183")
	public String docBusinessInfo183;
	@Column(name="DOC_BUSINESS_INFO_184")
	public String docBusinessInfo184;
	@Column(name="DOC_BUSINESS_INFO_185")
	public String docBusinessInfo185;
	@Column(name="DOC_BUSINESS_INFO_186")
	public String docBusinessInfo186;
	@Column(name="DOC_BUSINESS_INFO_187")
	public String docBusinessInfo187;
	@Column(name="DOC_BUSINESS_INFO_188")
	public String docBusinessInfo188;
	@Column(name="DOC_BUSINESS_INFO_189")
	public String docBusinessInfo189;
	@Column(name="DOC_BUSINESS_INFO_190")
	public String docBusinessInfo190;
	@Column(name="DOC_BUSINESS_INFO_191")
	public String docBusinessInfo191;
	@Column(name="DOC_BUSINESS_INFO_192")
	public String docBusinessInfo192;
	@Column(name="DOC_BUSINESS_INFO_193")
	public String docBusinessInfo193;
	@Column(name="DOC_BUSINESS_INFO_194")
	public String docBusinessInfo194;
	@Column(name="DOC_BUSINESS_INFO_195")
	public String docBusinessInfo195;
	@Column(name="DOC_BUSINESS_INFO_196")
	public String docBusinessInfo196;
	@Column(name="DOC_BUSINESS_INFO_197")
	public String docBusinessInfo197;
	@Column(name="DOC_BUSINESS_INFO_198")
	public String docBusinessInfo198;
	@Column(name="DOC_BUSINESS_INFO_199")
	public String docBusinessInfo199;
	@Column(name="DOC_BUSINESS_INFO_200")
	public String docBusinessInfo200;
	@Column(name="DOC_BUSINESS_INFO_201")
	public String docBusinessInfo201;
	@Column(name="DOC_BUSINESS_INFO_202")
	public String docBusinessInfo202;
	@Column(name="DOC_BUSINESS_INFO_203")
	public String docBusinessInfo203;
	@Column(name="DOC_BUSINESS_INFO_204")
	public String docBusinessInfo204;
	@Column(name="DOC_BUSINESS_INFO_205")
	public String docBusinessInfo205;
	@Column(name="DOC_BUSINESS_INFO_206")
	public String docBusinessInfo206;
	@Column(name="DOC_BUSINESS_INFO_207")
	public String docBusinessInfo207;
	@Column(name="DOC_BUSINESS_INFO_208")
	public String docBusinessInfo208;
	@Column(name="DOC_BUSINESS_INFO_209")
	public String docBusinessInfo209;
	@Column(name="DOC_BUSINESS_INFO_210")
	public String docBusinessInfo210;
	@Column(name="DOC_BUSINESS_INFO_211")
	public String docBusinessInfo211;
	@Column(name="DOC_BUSINESS_INFO_212")
	public String docBusinessInfo212;
	@Column(name="DOC_BUSINESS_INFO_213")
	public String docBusinessInfo213;
	@Column(name="DOC_BUSINESS_INFO_214")
	public String docBusinessInfo214;
	@Column(name="DOC_BUSINESS_INFO_215")
	public String docBusinessInfo215;
	@Column(name="DOC_BUSINESS_INFO_216")
	public String docBusinessInfo216;
	@Column(name="DOC_BUSINESS_INFO_217")
	public String docBusinessInfo217;
	@Column(name="DOC_BUSINESS_INFO_218")
	public String docBusinessInfo218;
	@Column(name="DOC_BUSINESS_INFO_219")
	public String docBusinessInfo219;
	@Column(name="DOC_BUSINESS_INFO_220")
	public String docBusinessInfo220;
	@Column(name="DOC_BUSINESS_INFO_221")
	public String docBusinessInfo221;
	@Column(name="DOC_BUSINESS_INFO_222")
	public String docBusinessInfo222;
	@Column(name="DOC_BUSINESS_INFO_223")
	public String docBusinessInfo223;
	@Column(name="DOC_BUSINESS_INFO_224")
	public String docBusinessInfo224;
	@Column(name="DOC_BUSINESS_INFO_225")
	public String docBusinessInfo225;
	@Column(name="DOC_BUSINESS_INFO_226")
	public String docBusinessInfo226;
	@Column(name="DOC_BUSINESS_INFO_227")
	public String docBusinessInfo227;
	@Column(name="DOC_BUSINESS_INFO_228")
	public String docBusinessInfo228;
	@Column(name="DOC_BUSINESS_INFO_229")
	public String docBusinessInfo229;
	@Column(name="DOC_BUSINESS_INFO_230")
	public String docBusinessInfo230;
	@Column(name="DOC_BUSINESS_INFO_231")
	public String docBusinessInfo231;
	@Column(name="DOC_BUSINESS_INFO_232")
	public String docBusinessInfo232;
	@Column(name="DOC_BUSINESS_INFO_233")
	public String docBusinessInfo233;
	@Column(name="DOC_BUSINESS_INFO_234")
	public String docBusinessInfo234;
	@Column(name="DOC_BUSINESS_INFO_235")
	public String docBusinessInfo235;
	@Column(name="DOC_BUSINESS_INFO_236")
	public String docBusinessInfo236;
	@Column(name="DOC_BUSINESS_INFO_237")
	public String docBusinessInfo237;
	@Column(name="DOC_BUSINESS_INFO_238")
	public String docBusinessInfo238;
	@Column(name="DOC_BUSINESS_INFO_239")
	public String docBusinessInfo239;
	@Column(name="DOC_BUSINESS_INFO_240")
	public String docBusinessInfo240;
	@Column(name="DOC_BUSINESS_INFO_241")
	public String docBusinessInfo241;
	@Column(name="DOC_BUSINESS_INFO_242")
	public String docBusinessInfo242;
	@Column(name="DOC_BUSINESS_INFO_243")
	public String docBusinessInfo243;
	@Column(name="DOC_BUSINESS_INFO_244")
	public String docBusinessInfo244;
	@Column(name="DOC_BUSINESS_INFO_245")
	public String docBusinessInfo245;
	@Column(name="DOC_BUSINESS_INFO_246")
	public String docBusinessInfo246;
	@Column(name="DOC_BUSINESS_INFO_247")
	public String docBusinessInfo247;
	@Column(name="DOC_BUSINESS_INFO_248")
	public String docBusinessInfo248;
	@Column(name="DOC_BUSINESS_INFO_249")
	public String docBusinessInfo249;
	@Column(name="DOC_BUSINESS_INFO_250")
	public String docBusinessInfo250;

}
