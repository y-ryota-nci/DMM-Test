package jp.co.nci.iwf.endpoint.api.request;

import java.sql.Date;
import java.sql.Timestamp;

public class SetAdditionalInfoRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセスID. */
	private Long processId;
	/** プロセス処理区分. */
	private String processRefUnitType;
	/** 識別Key01. */
	private String identificationKey01;
	/** 識別Key02. */
	private String identificationKey02;
	/** 識別Key03. */
	private String identificationKey03;
	/** 識別Key04. */
	private String identificationKey04;
	/** 識別Key05. */
	private String identificationKey05;
	/** 識別Key06. */
	private String identificationKey06;
	/** 識別Key07. */
	private String identificationKey07;
	/** 識別Key08. */
	private String identificationKey08;
	/** 識別Key09. */
	private String identificationKey09;
	/** 識別Key10. */
	private String identificationKey10;
	/** 業務管理情報01. */
	private String businessInfo001;
	/** 業務管理情報02. */
	private String businessInfo002;
	/** 業務管理情報03. */
	private String businessInfo003;
	/** 業務管理情報04. */
	private String businessInfo004;
	/** 業務管理情報05. */
	private String businessInfo005;
	/** 業務管理情報06. */
	private String businessInfo006;
	/** 業務管理情報07. */
	private String businessInfo007;
	/** 業務管理情報08. */
	private String businessInfo008;
	/** 業務管理情報09. */
	private String businessInfo009;
	/** 業務管理情報10. */
	private String businessInfo010;
	/** 業務管理項目011. */
	private String businessInfo011;
	/** 業務管理項目012. */
	private String businessInfo012;
	/** 業務管理項目013. */
	private String businessInfo013;
	/** 業務管理項目014. */
	private String businessInfo014;
	/** 業務管理項目015. */
	private String businessInfo015;
	/** 業務管理項目016. */
	private String businessInfo016;
	/** 業務管理項目017. */
	private String businessInfo017;
	/** 業務管理項目018. */
	private String businessInfo018;
	/** 業務管理項目019. */
	private String businessInfo019;
	/** 業務管理項目020. */
	private String businessInfo020;
	/** 業務管理項目021. */
	private String businessInfo021;
	/** 業務管理項目022. */
	private String businessInfo022;
	/** 業務管理項目023. */
	private String businessInfo023;
	/** 業務管理項目024. */
	private String businessInfo024;
	/** 業務管理項目025. */
	private String businessInfo025;
	/** 業務管理項目026. */
	private String businessInfo026;
	/** 業務管理項目027. */
	private String businessInfo027;
	/** 業務管理項目028. */
	private String businessInfo028;
	/** 業務管理項目029. */
	private String businessInfo029;
	/** 業務管理項目030. */
	private String businessInfo030;
	/** 業務管理項目031. */
	private String businessInfo031;
	/** 業務管理項目032. */
	private String businessInfo032;
	/** 業務管理項目033. */
	private String businessInfo033;
	/** 業務管理項目034. */
	private String businessInfo034;
	/** 業務管理項目035. */
	private String businessInfo035;
	/** 業務管理項目036. */
	private String businessInfo036;
	/** 業務管理項目037. */
	private String businessInfo037;
	/** 業務管理項目038. */
	private String businessInfo038;
	/** 業務管理項目039. */
	private String businessInfo039;
	/** 業務管理項目040. */
	private String businessInfo040;
	/** 業務管理項目041. */
	private String businessInfo041;
	/** 業務管理項目042. */
	private String businessInfo042;
	/** 業務管理項目043. */
	private String businessInfo043;
	/** 業務管理項目044. */
	private String businessInfo044;
	/** 業務管理項目045. */
	private String businessInfo045;
	/** 業務管理項目046. */
	private String businessInfo046;
	/** 業務管理項目047. */
	private String businessInfo047;
	/** 業務管理項目048. */
	private String businessInfo048;
	/** 業務管理項目049. */
	private String businessInfo049;
	/** 業務管理項目050. */
	private String businessInfo050;
	/** 業務管理項目051. */
	private String businessInfo051;
	/** 業務管理項目052. */
	private String businessInfo052;
	/** 業務管理項目053. */
	private String businessInfo053;
	/** 業務管理項目054. */
	private String businessInfo054;
	/** 業務管理項目055. */
	private String businessInfo055;
	/** 業務管理項目056. */
	private String businessInfo056;
	/** 業務管理項目057. */
	private String businessInfo057;
	/** 業務管理項目058. */
	private String businessInfo058;
	/** 業務管理項目059. */
	private String businessInfo059;
	/** 業務管理項目060. */
	private String businessInfo060;
	/** 業務管理項目061. */
	private String businessInfo061;
	/** 業務管理項目062. */
	private String businessInfo062;
	/** 業務管理項目063. */
	private String businessInfo063;
	/** 業務管理項目064. */
	private String businessInfo064;
	/** 業務管理項目065. */
	private String businessInfo065;
	/** 業務管理項目066. */
	private String businessInfo066;
	/** 業務管理項目067. */
	private String businessInfo067;
	/** 業務管理項目068. */
	private String businessInfo068;
	/** 業務管理項目069. */
	private String businessInfo069;
	/** 業務管理項目070. */
	private String businessInfo070;
	/** 業務管理項目071. */
	private String businessInfo071;
	/** 業務管理項目072. */
	private String businessInfo072;
	/** 業務管理項目073. */
	private String businessInfo073;
	/** 業務管理項目074. */
	private String businessInfo074;
	/** 業務管理項目075. */
	private String businessInfo075;
	/** 業務管理項目076. */
	private String businessInfo076;
	/** 業務管理項目077. */
	private String businessInfo077;
	/** 業務管理項目078. */
	private String businessInfo078;
	/** 業務管理項目079. */
	private String businessInfo079;
	/** 業務管理項目080. */
	private String businessInfo080;
	/** 業務管理項目081. */
	private String businessInfo081;
	/** 業務管理項目082. */
	private String businessInfo082;
	/** 業務管理項目083. */
	private String businessInfo083;
	/** 業務管理項目084. */
	private String businessInfo084;
	/** 業務管理項目085. */
	private String businessInfo085;
	/** 業務管理項目086. */
	private String businessInfo086;
	/** 業務管理項目087. */
	private String businessInfo087;
	/** 業務管理項目088. */
	private String businessInfo088;
	/** 業務管理項目089. */
	private String businessInfo089;
	/** 業務管理項目090. */
	private String businessInfo090;
	/** 業務管理項目091. */
	private String businessInfo091;
	/** 業務管理項目092. */
	private String businessInfo092;
	/** 業務管理項目093. */
	private String businessInfo093;
	/** 業務管理項目094. */
	private String businessInfo094;
	/** 業務管理項目095. */
	private String businessInfo095;
	/** 業務管理項目096. */
	private String businessInfo096;
	/** 業務管理項目097. */
	private String businessInfo097;
	/** 業務管理項目098. */
	private String businessInfo098;
	/** 業務管理項目099. */
	private String businessInfo099;
	/** 業務管理項目100. */
	private String businessInfo100;
	/** 業務管理項目101. */
	private String businessInfo101;
	/** 業務管理項目102. */
	private String businessInfo102;
	/** 業務管理項目103. */
	private String businessInfo103;
	/** 業務管理項目104. */
	private String businessInfo104;
	/** 業務管理項目105. */
	private String businessInfo105;
	/** 業務管理項目106. */
	private String businessInfo106;
	/** 業務管理項目107. */
	private String businessInfo107;
	/** 業務管理項目108. */
	private String businessInfo108;
	/** 業務管理項目109. */
	private String businessInfo109;
	/** 業務管理項目110. */
	private String businessInfo110;
	/** 業務管理項目111. */
	private String businessInfo111;
	/** 業務管理項目112. */
	private String businessInfo112;
	/** 業務管理項目113. */
	private String businessInfo113;
	/** 業務管理項目114. */
	private String businessInfo114;
	/** 業務管理項目115. */
	private String businessInfo115;
	/** 業務管理項目116. */
	private String businessInfo116;
	/** 業務管理項目117. */
	private String businessInfo117;
	/** 業務管理項目118. */
	private String businessInfo118;
	/** 業務管理項目119. */
	private String businessInfo119;
	/** 業務管理項目120. */
	private String businessInfo120;
	/** 業務管理項目121. */
	private String businessInfo121;
	/** 業務管理項目122. */
	private String businessInfo122;
	/** 業務管理項目123. */
	private String businessInfo123;
	/** 業務管理項目124. */
	private String businessInfo124;
	/** 業務管理項目125. */
	private String businessInfo125;
	/** 業務管理項目126. */
	private String businessInfo126;
	/** 業務管理項目127. */
	private String businessInfo127;
	/** 業務管理項目128. */
	private String businessInfo128;
	/** 業務管理項目129. */
	private String businessInfo129;
	/** 業務管理項目130. */
	private String businessInfo130;
	/** 業務管理項目131. */
	private String businessInfo131;
	/** 業務管理項目132. */
	private String businessInfo132;
	/** 業務管理項目133. */
	private String businessInfo133;
	/** 業務管理項目134. */
	private String businessInfo134;
	/** 業務管理項目135. */
	private String businessInfo135;
	/** 業務管理項目136. */
	private String businessInfo136;
	/** 業務管理項目137. */
	private String businessInfo137;
	/** 業務管理項目138. */
	private String businessInfo138;
	/** 業務管理項目139. */
	private String businessInfo139;
	/** 業務管理項目140. */
	private String businessInfo140;
	/** 業務管理項目141. */
	private String businessInfo141;
	/** 業務管理項目142. */
	private String businessInfo142;
	/** 業務管理項目143. */
	private String businessInfo143;
	/** 業務管理項目144. */
	private String businessInfo144;
	/** 業務管理項目145. */
	private String businessInfo145;
	/** 業務管理項目146. */
	private String businessInfo146;
	/** 業務管理項目147. */
	private String businessInfo147;
	/** 業務管理項目148. */
	private String businessInfo148;
	/** 業務管理項目149. */
	private String businessInfo149;
	/** 業務管理項目150. */
	private String businessInfo150;
	/** 業務管理項目151. */
	private String businessInfo151;
	/** 業務管理項目152. */
	private String businessInfo152;
	/** 業務管理項目153. */
	private String businessInfo153;
	/** 業務管理項目154. */
	private String businessInfo154;
	/** 業務管理項目155. */
	private String businessInfo155;
	/** 業務管理項目156. */
	private String businessInfo156;
	/** 業務管理項目157. */
	private String businessInfo157;
	/** 業務管理項目158. */
	private String businessInfo158;
	/** 業務管理項目159. */
	private String businessInfo159;
	/** 業務管理項目160. */
	private String businessInfo160;
	/** 業務管理項目161. */
	private String businessInfo161;
	/** 業務管理項目162. */
	private String businessInfo162;
	/** 業務管理項目163. */
	private String businessInfo163;
	/** 業務管理項目164. */
	private String businessInfo164;
	/** 業務管理項目165. */
	private String businessInfo165;
	/** 業務管理項目166. */
	private String businessInfo166;
	/** 業務管理項目167. */
	private String businessInfo167;
	/** 業務管理項目168. */
	private String businessInfo168;
	/** 業務管理項目169. */
	private String businessInfo169;
	/** 業務管理項目170. */
	private String businessInfo170;
	/** 業務管理項目171. */
	private String businessInfo171;
	/** 業務管理項目172. */
	private String businessInfo172;
	/** 業務管理項目173. */
	private String businessInfo173;
	/** 業務管理項目174. */
	private String businessInfo174;
	/** 業務管理項目175. */
	private String businessInfo175;
	/** 業務管理項目176. */
	private String businessInfo176;
	/** 業務管理項目177. */
	private String businessInfo177;
	/** 業務管理項目178. */
	private String businessInfo178;
	/** 業務管理項目179. */
	private String businessInfo179;
	/** 業務管理項目180. */
	private String businessInfo180;
	/** 業務管理項目181. */
	private String businessInfo181;
	/** 業務管理項目182. */
	private String businessInfo182;
	/** 業務管理項目183. */
	private String businessInfo183;
	/** 業務管理項目184. */
	private String businessInfo184;
	/** 業務管理項目185. */
	private String businessInfo185;
	/** 業務管理項目186. */
	private String businessInfo186;
	/** 業務管理項目187. */
	private String businessInfo187;
	/** 業務管理項目188. */
	private String businessInfo188;
	/** 業務管理項目189. */
	private String businessInfo189;
	/** 業務管理項目190. */
	private String businessInfo190;
	/** 業務管理項目191. */
	private String businessInfo191;
	/** 業務管理項目192. */
	private String businessInfo192;
	/** 業務管理項目193. */
	private String businessInfo193;
	/** 業務管理項目194. */
	private String businessInfo194;
	/** 業務管理項目195. */
	private String businessInfo195;
	/** 業務管理項目196. */
	private String businessInfo196;
	/** 業務管理項目197. */
	private String businessInfo197;
	/** 業務管理項目198. */
	private String businessInfo198;
	/** 業務管理項目199. */
	private String businessInfo199;
	/** 業務管理項目200. */
	private String businessInfo200;
	/** 業務管理項目201. */
	private String businessInfo201;
	/** 業務管理項目202. */
	private String businessInfo202;
	/** 業務管理項目203. */
	private String businessInfo203;
	/** 業務管理項目204. */
	private String businessInfo204;
	/** 業務管理項目205. */
	private String businessInfo205;
	/** 業務管理項目206. */
	private String businessInfo206;
	/** 業務管理項目207. */
	private String businessInfo207;
	/** 業務管理項目208. */
	private String businessInfo208;
	/** 業務管理項目209. */
	private String businessInfo209;
	/** 業務管理項目210. */
	private String businessInfo210;
	/** 業務管理項目211. */
	private String businessInfo211;
	/** 業務管理項目212. */
	private String businessInfo212;
	/** 業務管理項目213. */
	private String businessInfo213;
	/** 業務管理項目214. */
	private String businessInfo214;
	/** 業務管理項目215. */
	private String businessInfo215;
	/** 業務管理項目216. */
	private String businessInfo216;
	/** 業務管理項目217. */
	private String businessInfo217;
	/** 業務管理項目218. */
	private String businessInfo218;
	/** 業務管理項目219. */
	private String businessInfo219;
	/** 業務管理項目220. */
	private String businessInfo220;
	/** 業務管理項目221. */
	private String businessInfo221;
	/** 業務管理項目222. */
	private String businessInfo222;
	/** 業務管理項目223. */
	private String businessInfo223;
	/** 業務管理項目224. */
	private String businessInfo224;
	/** 業務管理項目225. */
	private String businessInfo225;
	/** 業務管理項目226. */
	private String businessInfo226;
	/** 業務管理項目227. */
	private String businessInfo227;
	/** 業務管理項目228. */
	private String businessInfo228;
	/** 業務管理項目229. */
	private String businessInfo229;
	/** 業務管理項目230. */
	private String businessInfo230;
	/** 業務管理項目231. */
	private String businessInfo231;
	/** 業務管理項目232. */
	private String businessInfo232;
	/** 業務管理項目233. */
	private String businessInfo233;
	/** 業務管理項目234. */
	private String businessInfo234;
	/** 業務管理項目235. */
	private String businessInfo235;
	/** 業務管理項目236. */
	private String businessInfo236;
	/** 業務管理項目237. */
	private String businessInfo237;
	/** 業務管理項目238. */
	private String businessInfo238;
	/** 業務管理項目239. */
	private String businessInfo239;
	/** 業務管理項目240. */
	private String businessInfo240;
	/** 業務管理項目241. */
	private String businessInfo241;
	/** 業務管理項目242. */
	private String businessInfo242;
	/** 業務管理項目243. */
	private String businessInfo243;
	/** 業務管理項目244. */
	private String businessInfo244;
	/** 業務管理項目245. */
	private String businessInfo245;
	/** 業務管理項目246. */
	private String businessInfo246;
	/** 業務管理項目247. */
	private String businessInfo247;
	/** 業務管理項目248. */
	private String businessInfo248;
	/** 業務管理項目249. */
	private String businessInfo249;
	/** 業務管理項目250. */
	private String businessInfo250;
	/** ワークリスト表示開始年月日. */
	private Date worklistDisplatyStartDate;
	/** 期限年月日. */
	private Timestamp limitDateProcess;
	/** 更新日時(プロセスインスタンス). */
	private Timestamp timestampUpdatedProcess;
	/** 起案担当者会社コード. */
	private String corporationCodeProxyStart;
	/** 起案組織コード. */
	private String organizationCodeStart;
	/** 起案役職コード. */
	private String postCodeStart;
	/** 起案担当者ユーザコード. */
	private String userCodeProxyStart;

	/**
	 * 会社コードを取得する.
	 * @return 会社コード
	 */
	public final String getCorporationCode() {
		return corporationCode;
	}
	/**
	 * 会社コードを設定する.
	 * @param pCorporationCode 会社コード
	 */
	public final void setCorporationCode(final String pCorporationCode) {
		this.corporationCode = pCorporationCode;
	}
	/**
	 * プロセスIDを取得する.
	 * @return プロセスID
	 */
	public final Long getProcessId() {
		return processId;
	}
	/**
	 * プロセスIDを設定する.
	 * @param val 設定するプロセスID
	 */
	public final void setProcessId(final Long val) {
		this.processId = val;
	}
	/**
	 * 識別Key01を取得する.
	 * @return 識別Key01
	 */
	public final String getIdentificationKey01() {
		return identificationKey01;
	}
	/**
	 * 識別Key01を設定する.
	 * @param pIdentificationKey01 識別Key01
	 */
	public final void setIdentificationKey01(final String pIdentificationKey01) {
		this.identificationKey01 = pIdentificationKey01;
	}
	/**
	 * 識別Key02を取得する.
	 * @return 識別Key02
	 */
	public final String getIdentificationKey02() {
		return identificationKey02;
	}
	/**
	 * 識別Key02を設定する.
	 * @param pIdentificationKey02 識別Key02
	 */
	public final void setIdentificationKey02(final String pIdentificationKey02) {
		this.identificationKey02 = pIdentificationKey02;
	}
	/**
	 * 識別Key03を取得する.
	 * @return 識別Key03
	 */
	public final String getIdentificationKey03() {
		return identificationKey03;
	}
	/**
	 * 識別Key03を設定する.
	 * @param pIdentificationKey03 識別Key03
	 */
	public final void setIdentificationKey03(final String pIdentificationKey03) {
		this.identificationKey03 = pIdentificationKey03;
	}
	/**
	 * 識別Key04を取得する.
	 * @return 識別Key04
	 */
	public final String getIdentificationKey04() {
		return identificationKey04;
	}
	/**
	 * 識別Key04を設定する.
	 * @param pIdentificationKey04 識別Key04
	 */
	public final void setIdentificationKey04(final String pIdentificationKey04) {
		this.identificationKey04 = pIdentificationKey04;
	}
	/**
	 * 識別Key05を取得する.
	 * @return 識別Key05
	 */
	public final String getIdentificationKey05() {
		return identificationKey05;
	}
	/**
	 * 識別Key05を設定する.
	 * @param pIdentificationKey05 識別Key05
	 */
	public final void setIdentificationKey05(final String pIdentificationKey05) {
		this.identificationKey05 = pIdentificationKey05;
	}
	/**
	 * 識別Key06を取得する.
	 * @return 識別Key06
	 */
	public final String getIdentificationKey06() {
		return identificationKey06;
	}
	/**
	 * 識別Key06を設定する.
	 * @param pIdentificationKey06 識別Key06
	 */
	public final void setIdentificationKey06(final String pIdentificationKey06) {
		this.identificationKey06 = pIdentificationKey06;
	}
	/**
	 * 識別Key07を取得する.
	 * @return 識別Key07
	 */
	public final String getIdentificationKey07() {
		return identificationKey07;
	}
	/**
	 * 識別Key07を設定する.
	 * @param pIdentificationKey07 識別Key07
	 */
	public final void setIdentificationKey07(final String pIdentificationKey07) {
		this.identificationKey07 = pIdentificationKey07;
	}
	/**
	 * 識別Key08を取得する.
	 * @return 識別Key08
	 */
	public final String getIdentificationKey08() {
		return identificationKey08;
	}
	/**
	 * 識別Key08を設定する.
	 * @param pIdentificationKey08 識別Key08
	 */
	public final void setIdentificationKey08(final String pIdentificationKey08) {
		this.identificationKey08 = pIdentificationKey08;
	}
	/**
	 * 識別Key09を取得する.
	 * @return 識別Key09
	 */
	public final String getIdentificationKey09() {
		return identificationKey09;
	}
	/**
	 * 識別Key09を設定する.
	 * @param pIdentificationKey09 識別Key09
	 */
	public final void setIdentificationKey09(final String pIdentificationKey09) {
		this.identificationKey09 = pIdentificationKey09;
	}
	/**
	 * 識別Key10を取得する.
	 * @return 識別Key10
	 */
	public final String getIdentificationKey10() {
		return identificationKey10;
	}
	/**
	 * 識別Key10を設定する.
	 * @param pIdentificationKey10 識別Key10
	 */
	public final void setIdentificationKey10(final String pIdentificationKey10) {
		this.identificationKey10 = pIdentificationKey10;
	}
	/**
	 * 業務管理情報01を設定する.
	 * @param val 業務管理情報01
	 */
	public final void setBusinessInfo001(final String val) {
		this.businessInfo001 = val;
	}
	/**
	 * 業務管理情報01を取得する.
	 * @return 業務管理情報01
	 */
	public final String getBusinessInfo001() {
		return businessInfo001;
	}
	/**
	 * 業務管理情報02を設定する.
	 * @param val 業務管理情報02
	 */
	public final void setBusinessInfo002(final String val) {
		this.businessInfo002 = val;
	}
	/**
	 * 業務管理情報02を取得する.
	 * @return 業務管理情報02
	 */
	public final String getBusinessInfo002() {
		return businessInfo002;
	}
	/**
	 * 業務管理情報03を設定する.
	 * @param val 業務管理情報03
	 */
	public final void setBusinessInfo003(final String val) {
		this.businessInfo003 = val;
	}
	/**
	 * 業務管理情報03を取得する.
	 * @return 業務管理情報03
	 */
	public final String getBusinessInfo003() {
		return businessInfo003;
	}
	/**
	 * 業務管理情報04を設定する.
	 * @param val 業務管理情報04
	 */
	public final void setBusinessInfo004(final String val) {
		this.businessInfo004 = val;
	}
	/**
	 * 業務管理情報04を取得する.
	 * @return 業務管理情報04
	 */
	public final String getBusinessInfo004() {
		return businessInfo004;
	}
	/**
	 * 業務管理情報05を設定する.
	 * @param val 業務管理情報05
	 */
	public final void setBusinessInfo005(final String val) {
		this.businessInfo005 = val;
	}
	/**
	 * 業務管理情報05を取得する.
	 * @return 業務管理情報05
	 */
	public final String getBusinessInfo005() {
		return businessInfo005;
	}
	/**
	 * 業務管理情報06を設定する.
	 * @param val 業務管理情報06
	 */
	public final void setBusinessInfo006(final String val) {
		this.businessInfo006 = val;
	}
	/**
	 * 業務管理情報06を取得する.
	 * @return 業務管理情報06
	 */
	public final String getBusinessInfo006() {
		return businessInfo006;
	}
	/**
	 * 業務管理情報07を設定する.
	 * @param val 業務管理情報07
	 */
	public final void setBusinessInfo007(final String val) {
		this.businessInfo007 = val;
	}
	/**
	 * 業務管理情報07を取得する.
	 * @return 業務管理情報07
	 */
	public final String getBusinessInfo007() {
		return businessInfo007;
	}
	/**
	 * 業務管理情報08を設定する.
	 * @param val 業務管理情報08
	 */
	public final void setBusinessInfo008(final String val) {
		this.businessInfo008 = val;
	}
	/**
	 * 業務管理情報08を取得する.
	 * @return 業務管理情報08
	 */
	public final String getBusinessInfo008() {
		return businessInfo008;
	}
	/**
	 * 業務管理情報09を設定する.
	 * @param val 業務管理情報09
	 */
	public final void setBusinessInfo009(final String val) {
		this.businessInfo009 = val;
	}
	/**
	 * 業務管理情報09を取得する.
	 * @return 業務管理情報09
	 */
	public final String getBusinessInfo009() {
		return businessInfo009;
	}
	/**
	 * 業務管理情報10を設定する.
	 * @param val 業務管理情報10
	 */
	public final void setBusinessInfo010(final String val) {
		this.businessInfo010 = val;
	}
	/**
	 * 業務管理情報10を取得する.
	 * @return 業務管理情報10
	 */
	public final String getBusinessInfo010() {
		return businessInfo010;
	}

	/**
	 * 業務管理項目011を設定する.
	 * @param val 業務管理項目011
	 */
	public final void setBusinessInfo011(final String val) {
		this.businessInfo011 = val;
	}
	/**
	 * 業務管理項目011を取得する.
	 * @return 業務管理項目011
	 */
	public final String getBusinessInfo011() {
		return this.businessInfo011;
	}

	/**
	 * 業務管理項目012を設定する.
	 * @param val 業務管理項目012
	 */
	public final void setBusinessInfo012(final String val) {
		this.businessInfo012 = val;
	}
	/**
	 * 業務管理項目012を取得する.
	 * @return 業務管理項目012
	 */
	public final String getBusinessInfo012() {
		return this.businessInfo012;
	}

	/**
	 * 業務管理項目013を設定する.
	 * @param val 業務管理項目013
	 */
	public final void setBusinessInfo013(final String val) {
		this.businessInfo013 = val;
	}
	/**
	 * 業務管理項目013を取得する.
	 * @return 業務管理項目013
	 */
	public final String getBusinessInfo013() {
		return this.businessInfo013;
	}

	/**
	 * 業務管理項目014を設定する.
	 * @param val 業務管理項目014
	 */
	public final void setBusinessInfo014(final String val) {
		this.businessInfo014 = val;
	}
	/**
	 * 業務管理項目014を取得する.
	 * @return 業務管理項目014
	 */
	public final String getBusinessInfo014() {
		return this.businessInfo014;
	}

	/**
	 * 業務管理項目015を設定する.
	 * @param val 業務管理項目015
	 */
	public final void setBusinessInfo015(final String val) {
		this.businessInfo015 = val;
	}
	/**
	 * 業務管理項目015を取得する.
	 * @return 業務管理項目015
	 */
	public final String getBusinessInfo015() {
		return this.businessInfo015;
	}

	/**
	 * 業務管理項目016を設定する.
	 * @param val 業務管理項目016
	 */
	public final void setBusinessInfo016(final String val) {
		this.businessInfo016 = val;
	}
	/**
	 * 業務管理項目016を取得する.
	 * @return 業務管理項目016
	 */
	public final String getBusinessInfo016() {
		return this.businessInfo016;
	}

	/**
	 * 業務管理項目017を設定する.
	 * @param val 業務管理項目017
	 */
	public final void setBusinessInfo017(final String val) {
		this.businessInfo017 = val;
	}
	/**
	 * 業務管理項目017を取得する.
	 * @return 業務管理項目017
	 */
	public final String getBusinessInfo017() {
		return this.businessInfo017;
	}

	/**
	 * 業務管理項目018を設定する.
	 * @param val 業務管理項目018
	 */
	public final void setBusinessInfo018(final String val) {
		this.businessInfo018 = val;
	}
	/**
	 * 業務管理項目018を取得する.
	 * @return 業務管理項目018
	 */
	public final String getBusinessInfo018() {
		return this.businessInfo018;
	}

	/**
	 * 業務管理項目019を設定する.
	 * @param val 業務管理項目019
	 */
	public final void setBusinessInfo019(final String val) {
		this.businessInfo019 = val;
	}
	/**
	 * 業務管理項目019を取得する.
	 * @return 業務管理項目019
	 */
	public final String getBusinessInfo019() {
		return this.businessInfo019;
	}

	/**
	 * 業務管理項目020を設定する.
	 * @param val 業務管理項目020
	 */
	public final void setBusinessInfo020(final String val) {
		this.businessInfo020 = val;
	}
	/**
	 * 業務管理項目020を取得する.
	 * @return 業務管理項目020
	 */
	public final String getBusinessInfo020() {
		return this.businessInfo020;
	}

	/**
	 * 業務管理項目021を設定する.
	 * @param val 業務管理項目021
	 */
	public final void setBusinessInfo021(final String val) {
		this.businessInfo021 = val;
	}
	/**
	 * 業務管理項目021を取得する.
	 * @return 業務管理項目021
	 */
	public final String getBusinessInfo021() {
		return this.businessInfo021;
	}

	/**
	 * 業務管理項目022を設定する.
	 * @param val 業務管理項目022
	 */
	public final void setBusinessInfo022(final String val) {
		this.businessInfo022 = val;
	}
	/**
	 * 業務管理項目022を取得する.
	 * @return 業務管理項目022
	 */
	public final String getBusinessInfo022() {
		return this.businessInfo022;
	}

	/**
	 * 業務管理項目023を設定する.
	 * @param val 業務管理項目023
	 */
	public final void setBusinessInfo023(final String val) {
		this.businessInfo023 = val;
	}
	/**
	 * 業務管理項目023を取得する.
	 * @return 業務管理項目023
	 */
	public final String getBusinessInfo023() {
		return this.businessInfo023;
	}

	/**
	 * 業務管理項目024を設定する.
	 * @param val 業務管理項目024
	 */
	public final void setBusinessInfo024(final String val) {
		this.businessInfo024 = val;
	}
	/**
	 * 業務管理項目024を取得する.
	 * @return 業務管理項目024
	 */
	public final String getBusinessInfo024() {
		return this.businessInfo024;
	}

	/**
	 * 業務管理項目025を設定する.
	 * @param val 業務管理項目025
	 */
	public final void setBusinessInfo025(final String val) {
		this.businessInfo025 = val;
	}
	/**
	 * 業務管理項目025を取得する.
	 * @return 業務管理項目025
	 */
	public final String getBusinessInfo025() {
		return this.businessInfo025;
	}

	/**
	 * 業務管理項目026を設定する.
	 * @param val 業務管理項目026
	 */
	public final void setBusinessInfo026(final String val) {
		this.businessInfo026 = val;
	}
	/**
	 * 業務管理項目026を取得する.
	 * @return 業務管理項目026
	 */
	public final String getBusinessInfo026() {
		return this.businessInfo026;
	}

	/**
	 * 業務管理項目027を設定する.
	 * @param val 業務管理項目027
	 */
	public final void setBusinessInfo027(final String val) {
		this.businessInfo027 = val;
	}
	/**
	 * 業務管理項目027を取得する.
	 * @return 業務管理項目027
	 */
	public final String getBusinessInfo027() {
		return this.businessInfo027;
	}

	/**
	 * 業務管理項目028を設定する.
	 * @param val 業務管理項目028
	 */
	public final void setBusinessInfo028(final String val) {
		this.businessInfo028 = val;
	}
	/**
	 * 業務管理項目028を取得する.
	 * @return 業務管理項目028
	 */
	public final String getBusinessInfo028() {
		return this.businessInfo028;
	}

	/**
	 * 業務管理項目029を設定する.
	 * @param val 業務管理項目029
	 */
	public final void setBusinessInfo029(final String val) {
		this.businessInfo029 = val;
	}
	/**
	 * 業務管理項目029を取得する.
	 * @return 業務管理項目029
	 */
	public final String getBusinessInfo029() {
		return this.businessInfo029;
	}

	/**
	 * 業務管理項目030を設定する.
	 * @param val 業務管理項目030
	 */
	public final void setBusinessInfo030(final String val) {
		this.businessInfo030 = val;
	}
	/**
	 * 業務管理項目030を取得する.
	 * @return 業務管理項目030
	 */
	public final String getBusinessInfo030() {
		return this.businessInfo030;
	}

	/**
	 * 業務管理項目031を設定する.
	 * @param val 業務管理項目031
	 */
	public final void setBusinessInfo031(final String val) {
		this.businessInfo031 = val;
	}
	/**
	 * 業務管理項目031を取得する.
	 * @return 業務管理項目031
	 */
	public final String getBusinessInfo031() {
		return this.businessInfo031;
	}

	/**
	 * 業務管理項目032を設定する.
	 * @param val 業務管理項目032
	 */
	public final void setBusinessInfo032(final String val) {
		this.businessInfo032 = val;
	}
	/**
	 * 業務管理項目032を取得する.
	 * @return 業務管理項目032
	 */
	public final String getBusinessInfo032() {
		return this.businessInfo032;
	}

	/**
	 * 業務管理項目033を設定する.
	 * @param val 業務管理項目033
	 */
	public final void setBusinessInfo033(final String val) {
		this.businessInfo033 = val;
	}
	/**
	 * 業務管理項目033を取得する.
	 * @return 業務管理項目033
	 */
	public final String getBusinessInfo033() {
		return this.businessInfo033;
	}

	/**
	 * 業務管理項目034を設定する.
	 * @param val 業務管理項目034
	 */
	public final void setBusinessInfo034(final String val) {
		this.businessInfo034 = val;
	}
	/**
	 * 業務管理項目034を取得する.
	 * @return 業務管理項目034
	 */
	public final String getBusinessInfo034() {
		return this.businessInfo034;
	}

	/**
	 * 業務管理項目035を設定する.
	 * @param val 業務管理項目035
	 */
	public final void setBusinessInfo035(final String val) {
		this.businessInfo035 = val;
	}
	/**
	 * 業務管理項目035を取得する.
	 * @return 業務管理項目035
	 */
	public final String getBusinessInfo035() {
		return this.businessInfo035;
	}

	/**
	 * 業務管理項目036を設定する.
	 * @param val 業務管理項目036
	 */
	public final void setBusinessInfo036(final String val) {
		this.businessInfo036 = val;
	}
	/**
	 * 業務管理項目036を取得する.
	 * @return 業務管理項目036
	 */
	public final String getBusinessInfo036() {
		return this.businessInfo036;
	}

	/**
	 * 業務管理項目037を設定する.
	 * @param val 業務管理項目037
	 */
	public final void setBusinessInfo037(final String val) {
		this.businessInfo037 = val;
	}
	/**
	 * 業務管理項目037を取得する.
	 * @return 業務管理項目037
	 */
	public final String getBusinessInfo037() {
		return this.businessInfo037;
	}

	/**
	 * 業務管理項目038を設定する.
	 * @param val 業務管理項目038
	 */
	public final void setBusinessInfo038(final String val) {
		this.businessInfo038 = val;
	}
	/**
	 * 業務管理項目038を取得する.
	 * @return 業務管理項目038
	 */
	public final String getBusinessInfo038() {
		return this.businessInfo038;
	}

	/**
	 * 業務管理項目039を設定する.
	 * @param val 業務管理項目039
	 */
	public final void setBusinessInfo039(final String val) {
		this.businessInfo039 = val;
	}
	/**
	 * 業務管理項目039を取得する.
	 * @return 業務管理項目039
	 */
	public final String getBusinessInfo039() {
		return this.businessInfo039;
	}

	/**
	 * 業務管理項目040を設定する.
	 * @param val 業務管理項目040
	 */
	public final void setBusinessInfo040(final String val) {
		this.businessInfo040 = val;
	}
	/**
	 * 業務管理項目040を取得する.
	 * @return 業務管理項目040
	 */
	public final String getBusinessInfo040() {
		return this.businessInfo040;
	}

	/**
	 * 業務管理項目041を設定する.
	 * @param val 業務管理項目041
	 */
	public final void setBusinessInfo041(final String val) {
		this.businessInfo041 = val;
	}
	/**
	 * 業務管理項目041を取得する.
	 * @return 業務管理項目041
	 */
	public final String getBusinessInfo041() {
		return this.businessInfo041;
	}

	/**
	 * 業務管理項目042を設定する.
	 * @param val 業務管理項目042
	 */
	public final void setBusinessInfo042(final String val) {
		this.businessInfo042 = val;
	}
	/**
	 * 業務管理項目042を取得する.
	 * @return 業務管理項目042
	 */
	public final String getBusinessInfo042() {
		return this.businessInfo042;
	}

	/**
	 * 業務管理項目043を設定する.
	 * @param val 業務管理項目043
	 */
	public final void setBusinessInfo043(final String val) {
		this.businessInfo043 = val;
	}
	/**
	 * 業務管理項目043を取得する.
	 * @return 業務管理項目043
	 */
	public final String getBusinessInfo043() {
		return this.businessInfo043;
	}

	/**
	 * 業務管理項目044を設定する.
	 * @param val 業務管理項目044
	 */
	public final void setBusinessInfo044(final String val) {
		this.businessInfo044 = val;
	}
	/**
	 * 業務管理項目044を取得する.
	 * @return 業務管理項目044
	 */
	public final String getBusinessInfo044() {
		return this.businessInfo044;
	}

	/**
	 * 業務管理項目045を設定する.
	 * @param val 業務管理項目045
	 */
	public final void setBusinessInfo045(final String val) {
		this.businessInfo045 = val;
	}
	/**
	 * 業務管理項目045を取得する.
	 * @return 業務管理項目045
	 */
	public final String getBusinessInfo045() {
		return this.businessInfo045;
	}

	/**
	 * 業務管理項目046を設定する.
	 * @param val 業務管理項目046
	 */
	public final void setBusinessInfo046(final String val) {
		this.businessInfo046 = val;
	}
	/**
	 * 業務管理項目046を取得する.
	 * @return 業務管理項目046
	 */
	public final String getBusinessInfo046() {
		return this.businessInfo046;
	}

	/**
	 * 業務管理項目047を設定する.
	 * @param val 業務管理項目047
	 */
	public final void setBusinessInfo047(final String val) {
		this.businessInfo047 = val;
	}
	/**
	 * 業務管理項目047を取得する.
	 * @return 業務管理項目047
	 */
	public final String getBusinessInfo047() {
		return this.businessInfo047;
	}

	/**
	 * 業務管理項目048を設定する.
	 * @param val 業務管理項目048
	 */
	public final void setBusinessInfo048(final String val) {
		this.businessInfo048 = val;
	}
	/**
	 * 業務管理項目048を取得する.
	 * @return 業務管理項目048
	 */
	public final String getBusinessInfo048() {
		return this.businessInfo048;
	}

	/**
	 * 業務管理項目049を設定する.
	 * @param val 業務管理項目049
	 */
	public final void setBusinessInfo049(final String val) {
		this.businessInfo049 = val;
	}
	/**
	 * 業務管理項目049を取得する.
	 * @return 業務管理項目049
	 */
	public final String getBusinessInfo049() {
		return this.businessInfo049;
	}

	/**
	 * 業務管理項目050を設定する.
	 * @param val 業務管理項目050
	 */
	public final void setBusinessInfo050(final String val) {
		this.businessInfo050 = val;
	}
	/**
	 * 業務管理項目050を取得する.
	 * @return 業務管理項目050
	 */
	public final String getBusinessInfo050() {
		return this.businessInfo050;
	}

	/**
	 * 業務管理項目051を設定する.
	 * @param val 業務管理項目051
	 */
	public final void setBusinessInfo051(final String val) {
		this.businessInfo051 = val;
	}
	/**
	 * 業務管理項目051を取得する.
	 * @return 業務管理項目051
	 */
	public final String getBusinessInfo051() {
		return this.businessInfo051;
	}

	/**
	 * 業務管理項目052を設定する.
	 * @param val 業務管理項目052
	 */
	public final void setBusinessInfo052(final String val) {
		this.businessInfo052 = val;
	}
	/**
	 * 業務管理項目052を取得する.
	 * @return 業務管理項目052
	 */
	public final String getBusinessInfo052() {
		return this.businessInfo052;
	}

	/**
	 * 業務管理項目053を設定する.
	 * @param val 業務管理項目053
	 */
	public final void setBusinessInfo053(final String val) {
		this.businessInfo053 = val;
	}
	/**
	 * 業務管理項目053を取得する.
	 * @return 業務管理項目053
	 */
	public final String getBusinessInfo053() {
		return this.businessInfo053;
	}

	/**
	 * 業務管理項目054を設定する.
	 * @param val 業務管理項目054
	 */
	public final void setBusinessInfo054(final String val) {
		this.businessInfo054 = val;
	}
	/**
	 * 業務管理項目054を取得する.
	 * @return 業務管理項目054
	 */
	public final String getBusinessInfo054() {
		return this.businessInfo054;
	}

	/**
	 * 業務管理項目055を設定する.
	 * @param val 業務管理項目055
	 */
	public final void setBusinessInfo055(final String val) {
		this.businessInfo055 = val;
	}
	/**
	 * 業務管理項目055を取得する.
	 * @return 業務管理項目055
	 */
	public final String getBusinessInfo055() {
		return this.businessInfo055;
	}

	/**
	 * 業務管理項目056を設定する.
	 * @param val 業務管理項目056
	 */
	public final void setBusinessInfo056(final String val) {
		this.businessInfo056 = val;
	}
	/**
	 * 業務管理項目056を取得する.
	 * @return 業務管理項目056
	 */
	public final String getBusinessInfo056() {
		return this.businessInfo056;
	}

	/**
	 * 業務管理項目057を設定する.
	 * @param val 業務管理項目057
	 */
	public final void setBusinessInfo057(final String val) {
		this.businessInfo057 = val;
	}
	/**
	 * 業務管理項目057を取得する.
	 * @return 業務管理項目057
	 */
	public final String getBusinessInfo057() {
		return this.businessInfo057;
	}

	/**
	 * 業務管理項目058を設定する.
	 * @param val 業務管理項目058
	 */
	public final void setBusinessInfo058(final String val) {
		this.businessInfo058 = val;
	}
	/**
	 * 業務管理項目058を取得する.
	 * @return 業務管理項目058
	 */
	public final String getBusinessInfo058() {
		return this.businessInfo058;
	}

	/**
	 * 業務管理項目059を設定する.
	 * @param val 業務管理項目059
	 */
	public final void setBusinessInfo059(final String val) {
		this.businessInfo059 = val;
	}
	/**
	 * 業務管理項目059を取得する.
	 * @return 業務管理項目059
	 */
	public final String getBusinessInfo059() {
		return this.businessInfo059;
	}

	/**
	 * 業務管理項目060を設定する.
	 * @param val 業務管理項目060
	 */
	public final void setBusinessInfo060(final String val) {
		this.businessInfo060 = val;
	}
	/**
	 * 業務管理項目060を取得する.
	 * @return 業務管理項目060
	 */
	public final String getBusinessInfo060() {
		return this.businessInfo060;
	}

	/**
	 * 業務管理項目061を設定する.
	 * @param val 業務管理項目061
	 */
	public final void setBusinessInfo061(final String val) {
		this.businessInfo061 = val;
	}
	/**
	 * 業務管理項目061を取得する.
	 * @return 業務管理項目061
	 */
	public final String getBusinessInfo061() {
		return this.businessInfo061;
	}

	/**
	 * 業務管理項目062を設定する.
	 * @param val 業務管理項目062
	 */
	public final void setBusinessInfo062(final String val) {
		this.businessInfo062 = val;
	}
	/**
	 * 業務管理項目062を取得する.
	 * @return 業務管理項目062
	 */
	public final String getBusinessInfo062() {
		return this.businessInfo062;
	}

	/**
	 * 業務管理項目063を設定する.
	 * @param val 業務管理項目063
	 */
	public final void setBusinessInfo063(final String val) {
		this.businessInfo063 = val;
	}
	/**
	 * 業務管理項目063を取得する.
	 * @return 業務管理項目063
	 */
	public final String getBusinessInfo063() {
		return this.businessInfo063;
	}

	/**
	 * 業務管理項目064を設定する.
	 * @param val 業務管理項目064
	 */
	public final void setBusinessInfo064(final String val) {
		this.businessInfo064 = val;
	}
	/**
	 * 業務管理項目064を取得する.
	 * @return 業務管理項目064
	 */
	public final String getBusinessInfo064() {
		return this.businessInfo064;
	}

	/**
	 * 業務管理項目065を設定する.
	 * @param val 業務管理項目065
	 */
	public final void setBusinessInfo065(final String val) {
		this.businessInfo065 = val;
	}
	/**
	 * 業務管理項目065を取得する.
	 * @return 業務管理項目065
	 */
	public final String getBusinessInfo065() {
		return this.businessInfo065;
	}

	/**
	 * 業務管理項目066を設定する.
	 * @param val 業務管理項目066
	 */
	public final void setBusinessInfo066(final String val) {
		this.businessInfo066 = val;
	}
	/**
	 * 業務管理項目066を取得する.
	 * @return 業務管理項目066
	 */
	public final String getBusinessInfo066() {
		return this.businessInfo066;
	}

	/**
	 * 業務管理項目067を設定する.
	 * @param val 業務管理項目067
	 */
	public final void setBusinessInfo067(final String val) {
		this.businessInfo067 = val;
	}
	/**
	 * 業務管理項目067を取得する.
	 * @return 業務管理項目067
	 */
	public final String getBusinessInfo067() {
		return this.businessInfo067;
	}

	/**
	 * 業務管理項目068を設定する.
	 * @param val 業務管理項目068
	 */
	public final void setBusinessInfo068(final String val) {
		this.businessInfo068 = val;
	}
	/**
	 * 業務管理項目068を取得する.
	 * @return 業務管理項目068
	 */
	public final String getBusinessInfo068() {
		return this.businessInfo068;
	}

	/**
	 * 業務管理項目069を設定する.
	 * @param val 業務管理項目069
	 */
	public final void setBusinessInfo069(final String val) {
		this.businessInfo069 = val;
	}
	/**
	 * 業務管理項目069を取得する.
	 * @return 業務管理項目069
	 */
	public final String getBusinessInfo069() {
		return this.businessInfo069;
	}

	/**
	 * 業務管理項目070を設定する.
	 * @param val 業務管理項目070
	 */
	public final void setBusinessInfo070(final String val) {
		this.businessInfo070 = val;
	}
	/**
	 * 業務管理項目070を取得する.
	 * @return 業務管理項目070
	 */
	public final String getBusinessInfo070() {
		return this.businessInfo070;
	}

	/**
	 * 業務管理項目071を設定する.
	 * @param val 業務管理項目071
	 */
	public final void setBusinessInfo071(final String val) {
		this.businessInfo071 = val;
	}
	/**
	 * 業務管理項目071を取得する.
	 * @return 業務管理項目071
	 */
	public final String getBusinessInfo071() {
		return this.businessInfo071;
	}

	/**
	 * 業務管理項目072を設定する.
	 * @param val 業務管理項目072
	 */
	public final void setBusinessInfo072(final String val) {
		this.businessInfo072 = val;
	}
	/**
	 * 業務管理項目072を取得する.
	 * @return 業務管理項目072
	 */
	public final String getBusinessInfo072() {
		return this.businessInfo072;
	}

	/**
	 * 業務管理項目073を設定する.
	 * @param val 業務管理項目073
	 */
	public final void setBusinessInfo073(final String val) {
		this.businessInfo073 = val;
	}
	/**
	 * 業務管理項目073を取得する.
	 * @return 業務管理項目073
	 */
	public final String getBusinessInfo073() {
		return this.businessInfo073;
	}

	/**
	 * 業務管理項目074を設定する.
	 * @param val 業務管理項目074
	 */
	public final void setBusinessInfo074(final String val) {
		this.businessInfo074 = val;
	}
	/**
	 * 業務管理項目074を取得する.
	 * @return 業務管理項目074
	 */
	public final String getBusinessInfo074() {
		return this.businessInfo074;
	}

	/**
	 * 業務管理項目075を設定する.
	 * @param val 業務管理項目075
	 */
	public final void setBusinessInfo075(final String val) {
		this.businessInfo075 = val;
	}
	/**
	 * 業務管理項目075を取得する.
	 * @return 業務管理項目075
	 */
	public final String getBusinessInfo075() {
		return this.businessInfo075;
	}

	/**
	 * 業務管理項目076を設定する.
	 * @param val 業務管理項目076
	 */
	public final void setBusinessInfo076(final String val) {
		this.businessInfo076 = val;
	}
	/**
	 * 業務管理項目076を取得する.
	 * @return 業務管理項目076
	 */
	public final String getBusinessInfo076() {
		return this.businessInfo076;
	}

	/**
	 * 業務管理項目077を設定する.
	 * @param val 業務管理項目077
	 */
	public final void setBusinessInfo077(final String val) {
		this.businessInfo077 = val;
	}
	/**
	 * 業務管理項目077を取得する.
	 * @return 業務管理項目077
	 */
	public final String getBusinessInfo077() {
		return this.businessInfo077;
	}

	/**
	 * 業務管理項目078を設定する.
	 * @param val 業務管理項目078
	 */
	public final void setBusinessInfo078(final String val) {
		this.businessInfo078 = val;
	}
	/**
	 * 業務管理項目078を取得する.
	 * @return 業務管理項目078
	 */
	public final String getBusinessInfo078() {
		return this.businessInfo078;
	}

	/**
	 * 業務管理項目079を設定する.
	 * @param val 業務管理項目079
	 */
	public final void setBusinessInfo079(final String val) {
		this.businessInfo079 = val;
	}
	/**
	 * 業務管理項目079を取得する.
	 * @return 業務管理項目079
	 */
	public final String getBusinessInfo079() {
		return this.businessInfo079;
	}

	/**
	 * 業務管理項目080を設定する.
	 * @param val 業務管理項目080
	 */
	public final void setBusinessInfo080(final String val) {
		this.businessInfo080 = val;
	}
	/**
	 * 業務管理項目080を取得する.
	 * @return 業務管理項目080
	 */
	public final String getBusinessInfo080() {
		return this.businessInfo080;
	}

	/**
	 * 業務管理項目081を設定する.
	 * @param val 業務管理項目081
	 */
	public final void setBusinessInfo081(final String val) {
		this.businessInfo081 = val;
	}
	/**
	 * 業務管理項目081を取得する.
	 * @return 業務管理項目081
	 */
	public final String getBusinessInfo081() {
		return this.businessInfo081;
	}

	/**
	 * 業務管理項目082を設定する.
	 * @param val 業務管理項目082
	 */
	public final void setBusinessInfo082(final String val) {
		this.businessInfo082 = val;
	}
	/**
	 * 業務管理項目082を取得する.
	 * @return 業務管理項目082
	 */
	public final String getBusinessInfo082() {
		return this.businessInfo082;
	}

	/**
	 * 業務管理項目083を設定する.
	 * @param val 業務管理項目083
	 */
	public final void setBusinessInfo083(final String val) {
		this.businessInfo083 = val;
	}
	/**
	 * 業務管理項目083を取得する.
	 * @return 業務管理項目083
	 */
	public final String getBusinessInfo083() {
		return this.businessInfo083;
	}

	/**
	 * 業務管理項目084を設定する.
	 * @param val 業務管理項目084
	 */
	public final void setBusinessInfo084(final String val) {
		this.businessInfo084 = val;
	}
	/**
	 * 業務管理項目084を取得する.
	 * @return 業務管理項目084
	 */
	public final String getBusinessInfo084() {
		return this.businessInfo084;
	}

	/**
	 * 業務管理項目085を設定する.
	 * @param val 業務管理項目085
	 */
	public final void setBusinessInfo085(final String val) {
		this.businessInfo085 = val;
	}
	/**
	 * 業務管理項目085を取得する.
	 * @return 業務管理項目085
	 */
	public final String getBusinessInfo085() {
		return this.businessInfo085;
	}

	/**
	 * 業務管理項目086を設定する.
	 * @param val 業務管理項目086
	 */
	public final void setBusinessInfo086(final String val) {
		this.businessInfo086 = val;
	}
	/**
	 * 業務管理項目086を取得する.
	 * @return 業務管理項目086
	 */
	public final String getBusinessInfo086() {
		return this.businessInfo086;
	}

	/**
	 * 業務管理項目087を設定する.
	 * @param val 業務管理項目087
	 */
	public final void setBusinessInfo087(final String val) {
		this.businessInfo087 = val;
	}
	/**
	 * 業務管理項目087を取得する.
	 * @return 業務管理項目087
	 */
	public final String getBusinessInfo087() {
		return this.businessInfo087;
	}

	/**
	 * 業務管理項目088を設定する.
	 * @param val 業務管理項目088
	 */
	public final void setBusinessInfo088(final String val) {
		this.businessInfo088 = val;
	}
	/**
	 * 業務管理項目088を取得する.
	 * @return 業務管理項目088
	 */
	public final String getBusinessInfo088() {
		return this.businessInfo088;
	}

	/**
	 * 業務管理項目089を設定する.
	 * @param val 業務管理項目089
	 */
	public final void setBusinessInfo089(final String val) {
		this.businessInfo089 = val;
	}
	/**
	 * 業務管理項目089を取得する.
	 * @return 業務管理項目089
	 */
	public final String getBusinessInfo089() {
		return this.businessInfo089;
	}

	/**
	 * 業務管理項目090を設定する.
	 * @param val 業務管理項目090
	 */
	public final void setBusinessInfo090(final String val) {
		this.businessInfo090 = val;
	}
	/**
	 * 業務管理項目090を取得する.
	 * @return 業務管理項目090
	 */
	public final String getBusinessInfo090() {
		return this.businessInfo090;
	}

	/**
	 * 業務管理項目091を設定する.
	 * @param val 業務管理項目091
	 */
	public final void setBusinessInfo091(final String val) {
		this.businessInfo091 = val;
	}
	/**
	 * 業務管理項目091を取得する.
	 * @return 業務管理項目091
	 */
	public final String getBusinessInfo091() {
		return this.businessInfo091;
	}

	/**
	 * 業務管理項目092を設定する.
	 * @param val 業務管理項目092
	 */
	public final void setBusinessInfo092(final String val) {
		this.businessInfo092 = val;
	}
	/**
	 * 業務管理項目092を取得する.
	 * @return 業務管理項目092
	 */
	public final String getBusinessInfo092() {
		return this.businessInfo092;
	}

	/**
	 * 業務管理項目093を設定する.
	 * @param val 業務管理項目093
	 */
	public final void setBusinessInfo093(final String val) {
		this.businessInfo093 = val;
	}
	/**
	 * 業務管理項目093を取得する.
	 * @return 業務管理項目093
	 */
	public final String getBusinessInfo093() {
		return this.businessInfo093;
	}

	/**
	 * 業務管理項目094を設定する.
	 * @param val 業務管理項目094
	 */
	public final void setBusinessInfo094(final String val) {
		this.businessInfo094 = val;
	}
	/**
	 * 業務管理項目094を取得する.
	 * @return 業務管理項目094
	 */
	public final String getBusinessInfo094() {
		return this.businessInfo094;
	}

	/**
	 * 業務管理項目095を設定する.
	 * @param val 業務管理項目095
	 */
	public final void setBusinessInfo095(final String val) {
		this.businessInfo095 = val;
	}
	/**
	 * 業務管理項目095を取得する.
	 * @return 業務管理項目095
	 */
	public final String getBusinessInfo095() {
		return this.businessInfo095;
	}

	/**
	 * 業務管理項目096を設定する.
	 * @param val 業務管理項目096
	 */
	public final void setBusinessInfo096(final String val) {
		this.businessInfo096 = val;
	}
	/**
	 * 業務管理項目096を取得する.
	 * @return 業務管理項目096
	 */
	public final String getBusinessInfo096() {
		return this.businessInfo096;
	}

	/**
	 * 業務管理項目097を設定する.
	 * @param val 業務管理項目097
	 */
	public final void setBusinessInfo097(final String val) {
		this.businessInfo097 = val;
	}
	/**
	 * 業務管理項目097を取得する.
	 * @return 業務管理項目097
	 */
	public final String getBusinessInfo097() {
		return this.businessInfo097;
	}

	/**
	 * 業務管理項目098を設定する.
	 * @param val 業務管理項目098
	 */
	public final void setBusinessInfo098(final String val) {
		this.businessInfo098 = val;
	}
	/**
	 * 業務管理項目098を取得する.
	 * @return 業務管理項目098
	 */
	public final String getBusinessInfo098() {
		return this.businessInfo098;
	}

	/**
	 * 業務管理項目099を設定する.
	 * @param val 業務管理項目099
	 */
	public final void setBusinessInfo099(final String val) {
		this.businessInfo099 = val;
	}
	/**
	 * 業務管理項目099を取得する.
	 * @return 業務管理項目099
	 */
	public final String getBusinessInfo099() {
		return this.businessInfo099;
	}

	/**
	 * 業務管理項目100を設定する.
	 * @param val 業務管理項目100
	 */
	public final void setBusinessInfo100(final String val) {
		this.businessInfo100 = val;
	}
	/**
	 * 業務管理項目100を取得する.
	 * @return 業務管理項目100
	 */
	public final String getBusinessInfo100() {
		return this.businessInfo100;
	}

	/**
	 * 業務管理項目101を設定する.
	 * @param val 業務管理項目101
	 */
	public final void setBusinessInfo101(final String val) {
		this.businessInfo101 = val;
	}
	/**
	 * 業務管理項目101を取得する.
	 * @return 業務管理項目101
	 */
	public final String getBusinessInfo101() {
		return this.businessInfo101;
	}

	/**
	 * 業務管理項目102を設定する.
	 * @param val 業務管理項目102
	 */
	public final void setBusinessInfo102(final String val) {
		this.businessInfo102 = val;
	}
	/**
	 * 業務管理項目102を取得する.
	 * @return 業務管理項目102
	 */
	public final String getBusinessInfo102() {
		return this.businessInfo102;
	}

	/**
	 * 業務管理項目103を設定する.
	 * @param val 業務管理項目103
	 */
	public final void setBusinessInfo103(final String val) {
		this.businessInfo103 = val;
	}
	/**
	 * 業務管理項目103を取得する.
	 * @return 業務管理項目103
	 */
	public final String getBusinessInfo103() {
		return this.businessInfo103;
	}

	/**
	 * 業務管理項目104を設定する.
	 * @param val 業務管理項目104
	 */
	public final void setBusinessInfo104(final String val) {
		this.businessInfo104 = val;
	}
	/**
	 * 業務管理項目104を取得する.
	 * @return 業務管理項目104
	 */
	public final String getBusinessInfo104() {
		return this.businessInfo104;
	}

	/**
	 * 業務管理項目105を設定する.
	 * @param val 業務管理項目105
	 */
	public final void setBusinessInfo105(final String val) {
		this.businessInfo105 = val;
	}
	/**
	 * 業務管理項目105を取得する.
	 * @return 業務管理項目105
	 */
	public final String getBusinessInfo105() {
		return this.businessInfo105;
	}

	/**
	 * 業務管理項目106を設定する.
	 * @param val 業務管理項目106
	 */
	public final void setBusinessInfo106(final String val) {
		this.businessInfo106 = val;
	}
	/**
	 * 業務管理項目106を取得する.
	 * @return 業務管理項目106
	 */
	public final String getBusinessInfo106() {
		return this.businessInfo106;
	}

	/**
	 * 業務管理項目107を設定する.
	 * @param val 業務管理項目107
	 */
	public final void setBusinessInfo107(final String val) {
		this.businessInfo107 = val;
	}
	/**
	 * 業務管理項目107を取得する.
	 * @return 業務管理項目107
	 */
	public final String getBusinessInfo107() {
		return this.businessInfo107;
	}

	/**
	 * 業務管理項目108を設定する.
	 * @param val 業務管理項目108
	 */
	public final void setBusinessInfo108(final String val) {
		this.businessInfo108 = val;
	}
	/**
	 * 業務管理項目108を取得する.
	 * @return 業務管理項目108
	 */
	public final String getBusinessInfo108() {
		return this.businessInfo108;
	}

	/**
	 * 業務管理項目109を設定する.
	 * @param val 業務管理項目109
	 */
	public final void setBusinessInfo109(final String val) {
		this.businessInfo109 = val;
	}
	/**
	 * 業務管理項目109を取得する.
	 * @return 業務管理項目109
	 */
	public final String getBusinessInfo109() {
		return this.businessInfo109;
	}

	/**
	 * 業務管理項目110を設定する.
	 * @param val 業務管理項目110
	 */
	public final void setBusinessInfo110(final String val) {
		this.businessInfo110 = val;
	}
	/**
	 * 業務管理項目110を取得する.
	 * @return 業務管理項目110
	 */
	public final String getBusinessInfo110() {
		return this.businessInfo110;
	}

	/**
	 * 業務管理項目111を設定する.
	 * @param val 業務管理項目111
	 */
	public final void setBusinessInfo111(final String val) {
		this.businessInfo111 = val;
	}
	/**
	 * 業務管理項目111を取得する.
	 * @return 業務管理項目111
	 */
	public final String getBusinessInfo111() {
		return this.businessInfo111;
	}

	/**
	 * 業務管理項目112を設定する.
	 * @param val 業務管理項目112
	 */
	public final void setBusinessInfo112(final String val) {
		this.businessInfo112 = val;
	}
	/**
	 * 業務管理項目112を取得する.
	 * @return 業務管理項目112
	 */
	public final String getBusinessInfo112() {
		return this.businessInfo112;
	}

	/**
	 * 業務管理項目113を設定する.
	 * @param val 業務管理項目113
	 */
	public final void setBusinessInfo113(final String val) {
		this.businessInfo113 = val;
	}
	/**
	 * 業務管理項目113を取得する.
	 * @return 業務管理項目113
	 */
	public final String getBusinessInfo113() {
		return this.businessInfo113;
	}

	/**
	 * 業務管理項目114を設定する.
	 * @param val 業務管理項目114
	 */
	public final void setBusinessInfo114(final String val) {
		this.businessInfo114 = val;
	}
	/**
	 * 業務管理項目114を取得する.
	 * @return 業務管理項目114
	 */
	public final String getBusinessInfo114() {
		return this.businessInfo114;
	}

	/**
	 * 業務管理項目115を設定する.
	 * @param val 業務管理項目115
	 */
	public final void setBusinessInfo115(final String val) {
		this.businessInfo115 = val;
	}
	/**
	 * 業務管理項目115を取得する.
	 * @return 業務管理項目115
	 */
	public final String getBusinessInfo115() {
		return this.businessInfo115;
	}

	/**
	 * 業務管理項目116を設定する.
	 * @param val 業務管理項目116
	 */
	public final void setBusinessInfo116(final String val) {
		this.businessInfo116 = val;
	}
	/**
	 * 業務管理項目116を取得する.
	 * @return 業務管理項目116
	 */
	public final String getBusinessInfo116() {
		return this.businessInfo116;
	}

	/**
	 * 業務管理項目117を設定する.
	 * @param val 業務管理項目117
	 */
	public final void setBusinessInfo117(final String val) {
		this.businessInfo117 = val;
	}
	/**
	 * 業務管理項目117を取得する.
	 * @return 業務管理項目117
	 */
	public final String getBusinessInfo117() {
		return this.businessInfo117;
	}

	/**
	 * 業務管理項目118を設定する.
	 * @param val 業務管理項目118
	 */
	public final void setBusinessInfo118(final String val) {
		this.businessInfo118 = val;
	}
	/**
	 * 業務管理項目118を取得する.
	 * @return 業務管理項目118
	 */
	public final String getBusinessInfo118() {
		return this.businessInfo118;
	}

	/**
	 * 業務管理項目119を設定する.
	 * @param val 業務管理項目119
	 */
	public final void setBusinessInfo119(final String val) {
		this.businessInfo119 = val;
	}
	/**
	 * 業務管理項目119を取得する.
	 * @return 業務管理項目119
	 */
	public final String getBusinessInfo119() {
		return this.businessInfo119;
	}

	/**
	 * 業務管理項目120を設定する.
	 * @param val 業務管理項目120
	 */
	public final void setBusinessInfo120(final String val) {
		this.businessInfo120 = val;
	}
	/**
	 * 業務管理項目120を取得する.
	 * @return 業務管理項目120
	 */
	public final String getBusinessInfo120() {
		return this.businessInfo120;
	}

	/**
	 * 業務管理項目121を設定する.
	 * @param val 業務管理項目121
	 */
	public final void setBusinessInfo121(final String val) {
		this.businessInfo121 = val;
	}
	/**
	 * 業務管理項目121を取得する.
	 * @return 業務管理項目121
	 */
	public final String getBusinessInfo121() {
		return this.businessInfo121;
	}

	/**
	 * 業務管理項目122を設定する.
	 * @param val 業務管理項目122
	 */
	public final void setBusinessInfo122(final String val) {
		this.businessInfo122 = val;
	}
	/**
	 * 業務管理項目122を取得する.
	 * @return 業務管理項目122
	 */
	public final String getBusinessInfo122() {
		return this.businessInfo122;
	}

	/**
	 * 業務管理項目123を設定する.
	 * @param val 業務管理項目123
	 */
	public final void setBusinessInfo123(final String val) {
		this.businessInfo123 = val;
	}
	/**
	 * 業務管理項目123を取得する.
	 * @return 業務管理項目123
	 */
	public final String getBusinessInfo123() {
		return this.businessInfo123;
	}

	/**
	 * 業務管理項目124を設定する.
	 * @param val 業務管理項目124
	 */
	public final void setBusinessInfo124(final String val) {
		this.businessInfo124 = val;
	}
	/**
	 * 業務管理項目124を取得する.
	 * @return 業務管理項目124
	 */
	public final String getBusinessInfo124() {
		return this.businessInfo124;
	}

	/**
	 * 業務管理項目125を設定する.
	 * @param val 業務管理項目125
	 */
	public final void setBusinessInfo125(final String val) {
		this.businessInfo125 = val;
	}
	/**
	 * 業務管理項目125を取得する.
	 * @return 業務管理項目125
	 */
	public final String getBusinessInfo125() {
		return this.businessInfo125;
	}

	/**
	 * 業務管理項目126を設定する.
	 * @param val 業務管理項目126
	 */
	public final void setBusinessInfo126(final String val) {
		this.businessInfo126 = val;
	}
	/**
	 * 業務管理項目126を取得する.
	 * @return 業務管理項目126
	 */
	public final String getBusinessInfo126() {
		return this.businessInfo126;
	}

	/**
	 * 業務管理項目127を設定する.
	 * @param val 業務管理項目127
	 */
	public final void setBusinessInfo127(final String val) {
		this.businessInfo127 = val;
	}
	/**
	 * 業務管理項目127を取得する.
	 * @return 業務管理項目127
	 */
	public final String getBusinessInfo127() {
		return this.businessInfo127;
	}

	/**
	 * 業務管理項目128を設定する.
	 * @param val 業務管理項目128
	 */
	public final void setBusinessInfo128(final String val) {
		this.businessInfo128 = val;
	}
	/**
	 * 業務管理項目128を取得する.
	 * @return 業務管理項目128
	 */
	public final String getBusinessInfo128() {
		return this.businessInfo128;
	}

	/**
	 * 業務管理項目129を設定する.
	 * @param val 業務管理項目129
	 */
	public final void setBusinessInfo129(final String val) {
		this.businessInfo129 = val;
	}
	/**
	 * 業務管理項目129を取得する.
	 * @return 業務管理項目129
	 */
	public final String getBusinessInfo129() {
		return this.businessInfo129;
	}

	/**
	 * 業務管理項目130を設定する.
	 * @param val 業務管理項目130
	 */
	public final void setBusinessInfo130(final String val) {
		this.businessInfo130 = val;
	}
	/**
	 * 業務管理項目130を取得する.
	 * @return 業務管理項目130
	 */
	public final String getBusinessInfo130() {
		return this.businessInfo130;
	}

	/**
	 * 業務管理項目131を設定する.
	 * @param val 業務管理項目131
	 */
	public final void setBusinessInfo131(final String val) {
		this.businessInfo131 = val;
	}
	/**
	 * 業務管理項目131を取得する.
	 * @return 業務管理項目131
	 */
	public final String getBusinessInfo131() {
		return this.businessInfo131;
	}

	/**
	 * 業務管理項目132を設定する.
	 * @param val 業務管理項目132
	 */
	public final void setBusinessInfo132(final String val) {
		this.businessInfo132 = val;
	}
	/**
	 * 業務管理項目132を取得する.
	 * @return 業務管理項目132
	 */
	public final String getBusinessInfo132() {
		return this.businessInfo132;
	}

	/**
	 * 業務管理項目133を設定する.
	 * @param val 業務管理項目133
	 */
	public final void setBusinessInfo133(final String val) {
		this.businessInfo133 = val;
	}
	/**
	 * 業務管理項目133を取得する.
	 * @return 業務管理項目133
	 */
	public final String getBusinessInfo133() {
		return this.businessInfo133;
	}

	/**
	 * 業務管理項目134を設定する.
	 * @param val 業務管理項目134
	 */
	public final void setBusinessInfo134(final String val) {
		this.businessInfo134 = val;
	}
	/**
	 * 業務管理項目134を取得する.
	 * @return 業務管理項目134
	 */
	public final String getBusinessInfo134() {
		return this.businessInfo134;
	}

	/**
	 * 業務管理項目135を設定する.
	 * @param val 業務管理項目135
	 */
	public final void setBusinessInfo135(final String val) {
		this.businessInfo135 = val;
	}
	/**
	 * 業務管理項目135を取得する.
	 * @return 業務管理項目135
	 */
	public final String getBusinessInfo135() {
		return this.businessInfo135;
	}

	/**
	 * 業務管理項目136を設定する.
	 * @param val 業務管理項目136
	 */
	public final void setBusinessInfo136(final String val) {
		this.businessInfo136 = val;
	}
	/**
	 * 業務管理項目136を取得する.
	 * @return 業務管理項目136
	 */
	public final String getBusinessInfo136() {
		return this.businessInfo136;
	}

	/**
	 * 業務管理項目137を設定する.
	 * @param val 業務管理項目137
	 */
	public final void setBusinessInfo137(final String val) {
		this.businessInfo137 = val;
	}
	/**
	 * 業務管理項目137を取得する.
	 * @return 業務管理項目137
	 */
	public final String getBusinessInfo137() {
		return this.businessInfo137;
	}

	/**
	 * 業務管理項目138を設定する.
	 * @param val 業務管理項目138
	 */
	public final void setBusinessInfo138(final String val) {
		this.businessInfo138 = val;
	}
	/**
	 * 業務管理項目138を取得する.
	 * @return 業務管理項目138
	 */
	public final String getBusinessInfo138() {
		return this.businessInfo138;
	}

	/**
	 * 業務管理項目139を設定する.
	 * @param val 業務管理項目139
	 */
	public final void setBusinessInfo139(final String val) {
		this.businessInfo139 = val;
	}
	/**
	 * 業務管理項目139を取得する.
	 * @return 業務管理項目139
	 */
	public final String getBusinessInfo139() {
		return this.businessInfo139;
	}

	/**
	 * 業務管理項目140を設定する.
	 * @param val 業務管理項目140
	 */
	public final void setBusinessInfo140(final String val) {
		this.businessInfo140 = val;
	}
	/**
	 * 業務管理項目140を取得する.
	 * @return 業務管理項目140
	 */
	public final String getBusinessInfo140() {
		return this.businessInfo140;
	}

	/**
	 * 業務管理項目141を設定する.
	 * @param val 業務管理項目141
	 */
	public final void setBusinessInfo141(final String val) {
		this.businessInfo141 = val;
	}
	/**
	 * 業務管理項目141を取得する.
	 * @return 業務管理項目141
	 */
	public final String getBusinessInfo141() {
		return this.businessInfo141;
	}

	/**
	 * 業務管理項目142を設定する.
	 * @param val 業務管理項目142
	 */
	public final void setBusinessInfo142(final String val) {
		this.businessInfo142 = val;
	}
	/**
	 * 業務管理項目142を取得する.
	 * @return 業務管理項目142
	 */
	public final String getBusinessInfo142() {
		return this.businessInfo142;
	}

	/**
	 * 業務管理項目143を設定する.
	 * @param val 業務管理項目143
	 */
	public final void setBusinessInfo143(final String val) {
		this.businessInfo143 = val;
	}
	/**
	 * 業務管理項目143を取得する.
	 * @return 業務管理項目143
	 */
	public final String getBusinessInfo143() {
		return this.businessInfo143;
	}

	/**
	 * 業務管理項目144を設定する.
	 * @param val 業務管理項目144
	 */
	public final void setBusinessInfo144(final String val) {
		this.businessInfo144 = val;
	}
	/**
	 * 業務管理項目144を取得する.
	 * @return 業務管理項目144
	 */
	public final String getBusinessInfo144() {
		return this.businessInfo144;
	}

	/**
	 * 業務管理項目145を設定する.
	 * @param val 業務管理項目145
	 */
	public final void setBusinessInfo145(final String val) {
		this.businessInfo145 = val;
	}
	/**
	 * 業務管理項目145を取得する.
	 * @return 業務管理項目145
	 */
	public final String getBusinessInfo145() {
		return this.businessInfo145;
	}

	/**
	 * 業務管理項目146を設定する.
	 * @param val 業務管理項目146
	 */
	public final void setBusinessInfo146(final String val) {
		this.businessInfo146 = val;
	}
	/**
	 * 業務管理項目146を取得する.
	 * @return 業務管理項目146
	 */
	public final String getBusinessInfo146() {
		return this.businessInfo146;
	}

	/**
	 * 業務管理項目147を設定する.
	 * @param val 業務管理項目147
	 */
	public final void setBusinessInfo147(final String val) {
		this.businessInfo147 = val;
	}
	/**
	 * 業務管理項目147を取得する.
	 * @return 業務管理項目147
	 */
	public final String getBusinessInfo147() {
		return this.businessInfo147;
	}

	/**
	 * 業務管理項目148を設定する.
	 * @param val 業務管理項目148
	 */
	public final void setBusinessInfo148(final String val) {
		this.businessInfo148 = val;
	}
	/**
	 * 業務管理項目148を取得する.
	 * @return 業務管理項目148
	 */
	public final String getBusinessInfo148() {
		return this.businessInfo148;
	}

	/**
	 * 業務管理項目149を設定する.
	 * @param val 業務管理項目149
	 */
	public final void setBusinessInfo149(final String val) {
		this.businessInfo149 = val;
	}
	/**
	 * 業務管理項目149を取得する.
	 * @return 業務管理項目149
	 */
	public final String getBusinessInfo149() {
		return this.businessInfo149;
	}

	/**
	 * 業務管理項目150を設定する.
	 * @param val 業務管理項目150
	 */
	public final void setBusinessInfo150(final String val) {
		this.businessInfo150 = val;
	}
	/**
	 * 業務管理項目150を取得する.
	 * @return 業務管理項目150
	 */
	public final String getBusinessInfo150() {
		return this.businessInfo150;
	}

	/**
	 * 業務管理項目151を設定する.
	 * @param val 業務管理項目151
	 */
	public final void setBusinessInfo151(final String val) {
		this.businessInfo151 = val;
	}
	/**
	 * 業務管理項目151を取得する.
	 * @return 業務管理項目151
	 */
	public final String getBusinessInfo151() {
		return this.businessInfo151;
	}

	/**
	 * 業務管理項目152を設定する.
	 * @param val 業務管理項目152
	 */
	public final void setBusinessInfo152(final String val) {
		this.businessInfo152 = val;
	}
	/**
	 * 業務管理項目152を取得する.
	 * @return 業務管理項目152
	 */
	public final String getBusinessInfo152() {
		return this.businessInfo152;
	}

	/**
	 * 業務管理項目153を設定する.
	 * @param val 業務管理項目153
	 */
	public final void setBusinessInfo153(final String val) {
		this.businessInfo153 = val;
	}
	/**
	 * 業務管理項目153を取得する.
	 * @return 業務管理項目153
	 */
	public final String getBusinessInfo153() {
		return this.businessInfo153;
	}

	/**
	 * 業務管理項目154を設定する.
	 * @param val 業務管理項目154
	 */
	public final void setBusinessInfo154(final String val) {
		this.businessInfo154 = val;
	}
	/**
	 * 業務管理項目154を取得する.
	 * @return 業務管理項目154
	 */
	public final String getBusinessInfo154() {
		return this.businessInfo154;
	}

	/**
	 * 業務管理項目155を設定する.
	 * @param val 業務管理項目155
	 */
	public final void setBusinessInfo155(final String val) {
		this.businessInfo155 = val;
	}
	/**
	 * 業務管理項目155を取得する.
	 * @return 業務管理項目155
	 */
	public final String getBusinessInfo155() {
		return this.businessInfo155;
	}

	/**
	 * 業務管理項目156を設定する.
	 * @param val 業務管理項目156
	 */
	public final void setBusinessInfo156(final String val) {
		this.businessInfo156 = val;
	}
	/**
	 * 業務管理項目156を取得する.
	 * @return 業務管理項目156
	 */
	public final String getBusinessInfo156() {
		return this.businessInfo156;
	}

	/**
	 * 業務管理項目157を設定する.
	 * @param val 業務管理項目157
	 */
	public final void setBusinessInfo157(final String val) {
		this.businessInfo157 = val;
	}
	/**
	 * 業務管理項目157を取得する.
	 * @return 業務管理項目157
	 */
	public final String getBusinessInfo157() {
		return this.businessInfo157;
	}

	/**
	 * 業務管理項目158を設定する.
	 * @param val 業務管理項目158
	 */
	public final void setBusinessInfo158(final String val) {
		this.businessInfo158 = val;
	}
	/**
	 * 業務管理項目158を取得する.
	 * @return 業務管理項目158
	 */
	public final String getBusinessInfo158() {
		return this.businessInfo158;
	}

	/**
	 * 業務管理項目159を設定する.
	 * @param val 業務管理項目159
	 */
	public final void setBusinessInfo159(final String val) {
		this.businessInfo159 = val;
	}
	/**
	 * 業務管理項目159を取得する.
	 * @return 業務管理項目159
	 */
	public final String getBusinessInfo159() {
		return this.businessInfo159;
	}

	/**
	 * 業務管理項目160を設定する.
	 * @param val 業務管理項目160
	 */
	public final void setBusinessInfo160(final String val) {
		this.businessInfo160 = val;
	}
	/**
	 * 業務管理項目160を取得する.
	 * @return 業務管理項目160
	 */
	public final String getBusinessInfo160() {
		return this.businessInfo160;
	}

	/**
	 * 業務管理項目161を設定する.
	 * @param val 業務管理項目161
	 */
	public final void setBusinessInfo161(final String val) {
		this.businessInfo161 = val;
	}
	/**
	 * 業務管理項目161を取得する.
	 * @return 業務管理項目161
	 */
	public final String getBusinessInfo161() {
		return this.businessInfo161;
	}

	/**
	 * 業務管理項目162を設定する.
	 * @param val 業務管理項目162
	 */
	public final void setBusinessInfo162(final String val) {
		this.businessInfo162 = val;
	}
	/**
	 * 業務管理項目162を取得する.
	 * @return 業務管理項目162
	 */
	public final String getBusinessInfo162() {
		return this.businessInfo162;
	}

	/**
	 * 業務管理項目163を設定する.
	 * @param val 業務管理項目163
	 */
	public final void setBusinessInfo163(final String val) {
		this.businessInfo163 = val;
	}
	/**
	 * 業務管理項目163を取得する.
	 * @return 業務管理項目163
	 */
	public final String getBusinessInfo163() {
		return this.businessInfo163;
	}

	/**
	 * 業務管理項目164を設定する.
	 * @param val 業務管理項目164
	 */
	public final void setBusinessInfo164(final String val) {
		this.businessInfo164 = val;
	}
	/**
	 * 業務管理項目164を取得する.
	 * @return 業務管理項目164
	 */
	public final String getBusinessInfo164() {
		return this.businessInfo164;
	}

	/**
	 * 業務管理項目165を設定する.
	 * @param val 業務管理項目165
	 */
	public final void setBusinessInfo165(final String val) {
		this.businessInfo165 = val;
	}
	/**
	 * 業務管理項目165を取得する.
	 * @return 業務管理項目165
	 */
	public final String getBusinessInfo165() {
		return this.businessInfo165;
	}

	/**
	 * 業務管理項目166を設定する.
	 * @param val 業務管理項目166
	 */
	public final void setBusinessInfo166(final String val) {
		this.businessInfo166 = val;
	}
	/**
	 * 業務管理項目166を取得する.
	 * @return 業務管理項目166
	 */
	public final String getBusinessInfo166() {
		return this.businessInfo166;
	}

	/**
	 * 業務管理項目167を設定する.
	 * @param val 業務管理項目167
	 */
	public final void setBusinessInfo167(final String val) {
		this.businessInfo167 = val;
	}
	/**
	 * 業務管理項目167を取得する.
	 * @return 業務管理項目167
	 */
	public final String getBusinessInfo167() {
		return this.businessInfo167;
	}

	/**
	 * 業務管理項目168を設定する.
	 * @param val 業務管理項目168
	 */
	public final void setBusinessInfo168(final String val) {
		this.businessInfo168 = val;
	}
	/**
	 * 業務管理項目168を取得する.
	 * @return 業務管理項目168
	 */
	public final String getBusinessInfo168() {
		return this.businessInfo168;
	}

	/**
	 * 業務管理項目169を設定する.
	 * @param val 業務管理項目169
	 */
	public final void setBusinessInfo169(final String val) {
		this.businessInfo169 = val;
	}
	/**
	 * 業務管理項目169を取得する.
	 * @return 業務管理項目169
	 */
	public final String getBusinessInfo169() {
		return this.businessInfo169;
	}

	/**
	 * 業務管理項目170を設定する.
	 * @param val 業務管理項目170
	 */
	public final void setBusinessInfo170(final String val) {
		this.businessInfo170 = val;
	}
	/**
	 * 業務管理項目170を取得する.
	 * @return 業務管理項目170
	 */
	public final String getBusinessInfo170() {
		return this.businessInfo170;
	}

	/**
	 * 業務管理項目171を設定する.
	 * @param val 業務管理項目171
	 */
	public final void setBusinessInfo171(final String val) {
		this.businessInfo171 = val;
	}
	/**
	 * 業務管理項目171を取得する.
	 * @return 業務管理項目171
	 */
	public final String getBusinessInfo171() {
		return this.businessInfo171;
	}

	/**
	 * 業務管理項目172を設定する.
	 * @param val 業務管理項目172
	 */
	public final void setBusinessInfo172(final String val) {
		this.businessInfo172 = val;
	}
	/**
	 * 業務管理項目172を取得する.
	 * @return 業務管理項目172
	 */
	public final String getBusinessInfo172() {
		return this.businessInfo172;
	}

	/**
	 * 業務管理項目173を設定する.
	 * @param val 業務管理項目173
	 */
	public final void setBusinessInfo173(final String val) {
		this.businessInfo173 = val;
	}
	/**
	 * 業務管理項目173を取得する.
	 * @return 業務管理項目173
	 */
	public final String getBusinessInfo173() {
		return this.businessInfo173;
	}

	/**
	 * 業務管理項目174を設定する.
	 * @param val 業務管理項目174
	 */
	public final void setBusinessInfo174(final String val) {
		this.businessInfo174 = val;
	}
	/**
	 * 業務管理項目174を取得する.
	 * @return 業務管理項目174
	 */
	public final String getBusinessInfo174() {
		return this.businessInfo174;
	}

	/**
	 * 業務管理項目175を設定する.
	 * @param val 業務管理項目175
	 */
	public final void setBusinessInfo175(final String val) {
		this.businessInfo175 = val;
	}
	/**
	 * 業務管理項目175を取得する.
	 * @return 業務管理項目175
	 */
	public final String getBusinessInfo175() {
		return this.businessInfo175;
	}

	/**
	 * 業務管理項目176を設定する.
	 * @param val 業務管理項目176
	 */
	public final void setBusinessInfo176(final String val) {
		this.businessInfo176 = val;
	}
	/**
	 * 業務管理項目176を取得する.
	 * @return 業務管理項目176
	 */
	public final String getBusinessInfo176() {
		return this.businessInfo176;
	}

	/**
	 * 業務管理項目177を設定する.
	 * @param val 業務管理項目177
	 */
	public final void setBusinessInfo177(final String val) {
		this.businessInfo177 = val;
	}
	/**
	 * 業務管理項目177を取得する.
	 * @return 業務管理項目177
	 */
	public final String getBusinessInfo177() {
		return this.businessInfo177;
	}

	/**
	 * 業務管理項目178を設定する.
	 * @param val 業務管理項目178
	 */
	public final void setBusinessInfo178(final String val) {
		this.businessInfo178 = val;
	}
	/**
	 * 業務管理項目178を取得する.
	 * @return 業務管理項目178
	 */
	public final String getBusinessInfo178() {
		return this.businessInfo178;
	}

	/**
	 * 業務管理項目179を設定する.
	 * @param val 業務管理項目179
	 */
	public final void setBusinessInfo179(final String val) {
		this.businessInfo179 = val;
	}
	/**
	 * 業務管理項目179を取得する.
	 * @return 業務管理項目179
	 */
	public final String getBusinessInfo179() {
		return this.businessInfo179;
	}

	/**
	 * 業務管理項目180を設定する.
	 * @param val 業務管理項目180
	 */
	public final void setBusinessInfo180(final String val) {
		this.businessInfo180 = val;
	}
	/**
	 * 業務管理項目180を取得する.
	 * @return 業務管理項目180
	 */
	public final String getBusinessInfo180() {
		return this.businessInfo180;
	}

	/**
	 * 業務管理項目181を設定する.
	 * @param val 業務管理項目181
	 */
	public final void setBusinessInfo181(final String val) {
		this.businessInfo181 = val;
	}
	/**
	 * 業務管理項目181を取得する.
	 * @return 業務管理項目181
	 */
	public final String getBusinessInfo181() {
		return this.businessInfo181;
	}

	/**
	 * 業務管理項目182を設定する.
	 * @param val 業務管理項目182
	 */
	public final void setBusinessInfo182(final String val) {
		this.businessInfo182 = val;
	}
	/**
	 * 業務管理項目182を取得する.
	 * @return 業務管理項目182
	 */
	public final String getBusinessInfo182() {
		return this.businessInfo182;
	}

	/**
	 * 業務管理項目183を設定する.
	 * @param val 業務管理項目183
	 */
	public final void setBusinessInfo183(final String val) {
		this.businessInfo183 = val;
	}
	/**
	 * 業務管理項目183を取得する.
	 * @return 業務管理項目183
	 */
	public final String getBusinessInfo183() {
		return this.businessInfo183;
	}

	/**
	 * 業務管理項目184を設定する.
	 * @param val 業務管理項目184
	 */
	public final void setBusinessInfo184(final String val) {
		this.businessInfo184 = val;
	}
	/**
	 * 業務管理項目184を取得する.
	 * @return 業務管理項目184
	 */
	public final String getBusinessInfo184() {
		return this.businessInfo184;
	}

	/**
	 * 業務管理項目185を設定する.
	 * @param val 業務管理項目185
	 */
	public final void setBusinessInfo185(final String val) {
		this.businessInfo185 = val;
	}
	/**
	 * 業務管理項目185を取得する.
	 * @return 業務管理項目185
	 */
	public final String getBusinessInfo185() {
		return this.businessInfo185;
	}

	/**
	 * 業務管理項目186を設定する.
	 * @param val 業務管理項目186
	 */
	public final void setBusinessInfo186(final String val) {
		this.businessInfo186 = val;
	}
	/**
	 * 業務管理項目186を取得する.
	 * @return 業務管理項目186
	 */
	public final String getBusinessInfo186() {
		return this.businessInfo186;
	}

	/**
	 * 業務管理項目187を設定する.
	 * @param val 業務管理項目187
	 */
	public final void setBusinessInfo187(final String val) {
		this.businessInfo187 = val;
	}
	/**
	 * 業務管理項目187を取得する.
	 * @return 業務管理項目187
	 */
	public final String getBusinessInfo187() {
		return this.businessInfo187;
	}

	/**
	 * 業務管理項目188を設定する.
	 * @param val 業務管理項目188
	 */
	public final void setBusinessInfo188(final String val) {
		this.businessInfo188 = val;
	}
	/**
	 * 業務管理項目188を取得する.
	 * @return 業務管理項目188
	 */
	public final String getBusinessInfo188() {
		return this.businessInfo188;
	}

	/**
	 * 業務管理項目189を設定する.
	 * @param val 業務管理項目189
	 */
	public final void setBusinessInfo189(final String val) {
		this.businessInfo189 = val;
	}
	/**
	 * 業務管理項目189を取得する.
	 * @return 業務管理項目189
	 */
	public final String getBusinessInfo189() {
		return this.businessInfo189;
	}

	/**
	 * 業務管理項目190を設定する.
	 * @param val 業務管理項目190
	 */
	public final void setBusinessInfo190(final String val) {
		this.businessInfo190 = val;
	}
	/**
	 * 業務管理項目190を取得する.
	 * @return 業務管理項目190
	 */
	public final String getBusinessInfo190() {
		return this.businessInfo190;
	}

	/**
	 * 業務管理項目191を設定する.
	 * @param val 業務管理項目191
	 */
	public final void setBusinessInfo191(final String val) {
		this.businessInfo191 = val;
	}
	/**
	 * 業務管理項目191を取得する.
	 * @return 業務管理項目191
	 */
	public final String getBusinessInfo191() {
		return this.businessInfo191;
	}

	/**
	 * 業務管理項目192を設定する.
	 * @param val 業務管理項目192
	 */
	public final void setBusinessInfo192(final String val) {
		this.businessInfo192 = val;
	}
	/**
	 * 業務管理項目192を取得する.
	 * @return 業務管理項目192
	 */
	public final String getBusinessInfo192() {
		return this.businessInfo192;
	}

	/**
	 * 業務管理項目193を設定する.
	 * @param val 業務管理項目193
	 */
	public final void setBusinessInfo193(final String val) {
		this.businessInfo193 = val;
	}
	/**
	 * 業務管理項目193を取得する.
	 * @return 業務管理項目193
	 */
	public final String getBusinessInfo193() {
		return this.businessInfo193;
	}

	/**
	 * 業務管理項目194を設定する.
	 * @param val 業務管理項目194
	 */
	public final void setBusinessInfo194(final String val) {
		this.businessInfo194 = val;
	}
	/**
	 * 業務管理項目194を取得する.
	 * @return 業務管理項目194
	 */
	public final String getBusinessInfo194() {
		return this.businessInfo194;
	}

	/**
	 * 業務管理項目195を設定する.
	 * @param val 業務管理項目195
	 */
	public final void setBusinessInfo195(final String val) {
		this.businessInfo195 = val;
	}
	/**
	 * 業務管理項目195を取得する.
	 * @return 業務管理項目195
	 */
	public final String getBusinessInfo195() {
		return this.businessInfo195;
	}

	/**
	 * 業務管理項目196を設定する.
	 * @param val 業務管理項目196
	 */
	public final void setBusinessInfo196(final String val) {
		this.businessInfo196 = val;
	}
	/**
	 * 業務管理項目196を取得する.
	 * @return 業務管理項目196
	 */
	public final String getBusinessInfo196() {
		return this.businessInfo196;
	}

	/**
	 * 業務管理項目197を設定する.
	 * @param val 業務管理項目197
	 */
	public final void setBusinessInfo197(final String val) {
		this.businessInfo197 = val;
	}
	/**
	 * 業務管理項目197を取得する.
	 * @return 業務管理項目197
	 */
	public final String getBusinessInfo197() {
		return this.businessInfo197;
	}

	/**
	 * 業務管理項目198を設定する.
	 * @param val 業務管理項目198
	 */
	public final void setBusinessInfo198(final String val) {
		this.businessInfo198 = val;
	}
	/**
	 * 業務管理項目198を取得する.
	 * @return 業務管理項目198
	 */
	public final String getBusinessInfo198() {
		return this.businessInfo198;
	}

	/**
	 * 業務管理項目199を設定する.
	 * @param val 業務管理項目199
	 */
	public final void setBusinessInfo199(final String val) {
		this.businessInfo199 = val;
	}
	/**
	 * 業務管理項目199を取得する.
	 * @return 業務管理項目199
	 */
	public final String getBusinessInfo199() {
		return this.businessInfo199;
	}

	/**
	 * 業務管理項目200を設定する.
	 * @param val 業務管理項目200
	 */
	public final void setBusinessInfo200(final String val) {
		this.businessInfo200 = val;
	}
	/**
	 * 業務管理項目200を取得する.
	 * @return 業務管理項目200
	 */
	public final String getBusinessInfo200() {
		return this.businessInfo200;
	}

	/**
	 * 業務管理項目201を設定する.
	 * @param val 業務管理項目201
	 */
	public final void setBusinessInfo201(final String val) {
		this.businessInfo201 = val;
	}
	/**
	 * 業務管理項目201を取得する.
	 * @return 業務管理項目201
	 */
	public final String getBusinessInfo201() {
		return this.businessInfo201;
	}

	/**
	 * 業務管理項目202を設定する.
	 * @param val 業務管理項目202
	 */
	public final void setBusinessInfo202(final String val) {
		this.businessInfo202 = val;
	}
	/**
	 * 業務管理項目202を取得する.
	 * @return 業務管理項目202
	 */
	public final String getBusinessInfo202() {
		return this.businessInfo202;
	}

	/**
	 * 業務管理項目203を設定する.
	 * @param val 業務管理項目203
	 */
	public final void setBusinessInfo203(final String val) {
		this.businessInfo203 = val;
	}
	/**
	 * 業務管理項目203を取得する.
	 * @return 業務管理項目203
	 */
	public final String getBusinessInfo203() {
		return this.businessInfo203;
	}

	/**
	 * 業務管理項目204を設定する.
	 * @param val 業務管理項目204
	 */
	public final void setBusinessInfo204(final String val) {
		this.businessInfo204 = val;
	}
	/**
	 * 業務管理項目204を取得する.
	 * @return 業務管理項目204
	 */
	public final String getBusinessInfo204() {
		return this.businessInfo204;
	}

	/**
	 * 業務管理項目205を設定する.
	 * @param val 業務管理項目205
	 */
	public final void setBusinessInfo205(final String val) {
		this.businessInfo205 = val;
	}
	/**
	 * 業務管理項目205を取得する.
	 * @return 業務管理項目205
	 */
	public final String getBusinessInfo205() {
		return this.businessInfo205;
	}

	/**
	 * 業務管理項目206を設定する.
	 * @param val 業務管理項目206
	 */
	public final void setBusinessInfo206(final String val) {
		this.businessInfo206 = val;
	}
	/**
	 * 業務管理項目206を取得する.
	 * @return 業務管理項目206
	 */
	public final String getBusinessInfo206() {
		return this.businessInfo206;
	}

	/**
	 * 業務管理項目207を設定する.
	 * @param val 業務管理項目207
	 */
	public final void setBusinessInfo207(final String val) {
		this.businessInfo207 = val;
	}
	/**
	 * 業務管理項目207を取得する.
	 * @return 業務管理項目207
	 */
	public final String getBusinessInfo207() {
		return this.businessInfo207;
	}

	/**
	 * 業務管理項目208を設定する.
	 * @param val 業務管理項目208
	 */
	public final void setBusinessInfo208(final String val) {
		this.businessInfo208 = val;
	}
	/**
	 * 業務管理項目208を取得する.
	 * @return 業務管理項目208
	 */
	public final String getBusinessInfo208() {
		return this.businessInfo208;
	}

	/**
	 * 業務管理項目209を設定する.
	 * @param val 業務管理項目209
	 */
	public final void setBusinessInfo209(final String val) {
		this.businessInfo209 = val;
	}
	/**
	 * 業務管理項目209を取得する.
	 * @return 業務管理項目209
	 */
	public final String getBusinessInfo209() {
		return this.businessInfo209;
	}

	/**
	 * 業務管理項目210を設定する.
	 * @param val 業務管理項目210
	 */
	public final void setBusinessInfo210(final String val) {
		this.businessInfo210 = val;
	}
	/**
	 * 業務管理項目210を取得する.
	 * @return 業務管理項目210
	 */
	public final String getBusinessInfo210() {
		return this.businessInfo210;
	}

	/**
	 * 業務管理項目211を設定する.
	 * @param val 業務管理項目211
	 */
	public final void setBusinessInfo211(final String val) {
		this.businessInfo211 = val;
	}
	/**
	 * 業務管理項目211を取得する.
	 * @return 業務管理項目211
	 */
	public final String getBusinessInfo211() {
		return this.businessInfo211;
	}

	/**
	 * 業務管理項目212を設定する.
	 * @param val 業務管理項目212
	 */
	public final void setBusinessInfo212(final String val) {
		this.businessInfo212 = val;
	}
	/**
	 * 業務管理項目212を取得する.
	 * @return 業務管理項目212
	 */
	public final String getBusinessInfo212() {
		return this.businessInfo212;
	}

	/**
	 * 業務管理項目213を設定する.
	 * @param val 業務管理項目213
	 */
	public final void setBusinessInfo213(final String val) {
		this.businessInfo213 = val;
	}
	/**
	 * 業務管理項目213を取得する.
	 * @return 業務管理項目213
	 */
	public final String getBusinessInfo213() {
		return this.businessInfo213;
	}

	/**
	 * 業務管理項目214を設定する.
	 * @param val 業務管理項目214
	 */
	public final void setBusinessInfo214(final String val) {
		this.businessInfo214 = val;
	}
	/**
	 * 業務管理項目214を取得する.
	 * @return 業務管理項目214
	 */
	public final String getBusinessInfo214() {
		return this.businessInfo214;
	}

	/**
	 * 業務管理項目215を設定する.
	 * @param val 業務管理項目215
	 */
	public final void setBusinessInfo215(final String val) {
		this.businessInfo215 = val;
	}
	/**
	 * 業務管理項目215を取得する.
	 * @return 業務管理項目215
	 */
	public final String getBusinessInfo215() {
		return this.businessInfo215;
	}

	/**
	 * 業務管理項目216を設定する.
	 * @param val 業務管理項目216
	 */
	public final void setBusinessInfo216(final String val) {
		this.businessInfo216 = val;
	}
	/**
	 * 業務管理項目216を取得する.
	 * @return 業務管理項目216
	 */
	public final String getBusinessInfo216() {
		return this.businessInfo216;
	}

	/**
	 * 業務管理項目217を設定する.
	 * @param val 業務管理項目217
	 */
	public final void setBusinessInfo217(final String val) {
		this.businessInfo217 = val;
	}
	/**
	 * 業務管理項目217を取得する.
	 * @return 業務管理項目217
	 */
	public final String getBusinessInfo217() {
		return this.businessInfo217;
	}

	/**
	 * 業務管理項目218を設定する.
	 * @param val 業務管理項目218
	 */
	public final void setBusinessInfo218(final String val) {
		this.businessInfo218 = val;
	}
	/**
	 * 業務管理項目218を取得する.
	 * @return 業務管理項目218
	 */
	public final String getBusinessInfo218() {
		return this.businessInfo218;
	}

	/**
	 * 業務管理項目219を設定する.
	 * @param val 業務管理項目219
	 */
	public final void setBusinessInfo219(final String val) {
		this.businessInfo219 = val;
	}
	/**
	 * 業務管理項目219を取得する.
	 * @return 業務管理項目219
	 */
	public final String getBusinessInfo219() {
		return this.businessInfo219;
	}

	/**
	 * 業務管理項目220を設定する.
	 * @param val 業務管理項目220
	 */
	public final void setBusinessInfo220(final String val) {
		this.businessInfo220 = val;
	}
	/**
	 * 業務管理項目220を取得する.
	 * @return 業務管理項目220
	 */
	public final String getBusinessInfo220() {
		return this.businessInfo220;
	}

	/**
	 * 業務管理項目221を設定する.
	 * @param val 業務管理項目221
	 */
	public final void setBusinessInfo221(final String val) {
		this.businessInfo221 = val;
	}
	/**
	 * 業務管理項目221を取得する.
	 * @return 業務管理項目221
	 */
	public final String getBusinessInfo221() {
		return this.businessInfo221;
	}

	/**
	 * 業務管理項目222を設定する.
	 * @param val 業務管理項目222
	 */
	public final void setBusinessInfo222(final String val) {
		this.businessInfo222 = val;
	}
	/**
	 * 業務管理項目222を取得する.
	 * @return 業務管理項目222
	 */
	public final String getBusinessInfo222() {
		return this.businessInfo222;
	}

	/**
	 * 業務管理項目223を設定する.
	 * @param val 業務管理項目223
	 */
	public final void setBusinessInfo223(final String val) {
		this.businessInfo223 = val;
	}
	/**
	 * 業務管理項目223を取得する.
	 * @return 業務管理項目223
	 */
	public final String getBusinessInfo223() {
		return this.businessInfo223;
	}

	/**
	 * 業務管理項目224を設定する.
	 * @param val 業務管理項目224
	 */
	public final void setBusinessInfo224(final String val) {
		this.businessInfo224 = val;
	}
	/**
	 * 業務管理項目224を取得する.
	 * @return 業務管理項目224
	 */
	public final String getBusinessInfo224() {
		return this.businessInfo224;
	}

	/**
	 * 業務管理項目225を設定する.
	 * @param val 業務管理項目225
	 */
	public final void setBusinessInfo225(final String val) {
		this.businessInfo225 = val;
	}
	/**
	 * 業務管理項目225を取得する.
	 * @return 業務管理項目225
	 */
	public final String getBusinessInfo225() {
		return this.businessInfo225;
	}

	/**
	 * 業務管理項目226を設定する.
	 * @param val 業務管理項目226
	 */
	public final void setBusinessInfo226(final String val) {
		this.businessInfo226 = val;
	}
	/**
	 * 業務管理項目226を取得する.
	 * @return 業務管理項目226
	 */
	public final String getBusinessInfo226() {
		return this.businessInfo226;
	}

	/**
	 * 業務管理項目227を設定する.
	 * @param val 業務管理項目227
	 */
	public final void setBusinessInfo227(final String val) {
		this.businessInfo227 = val;
	}
	/**
	 * 業務管理項目227を取得する.
	 * @return 業務管理項目227
	 */
	public final String getBusinessInfo227() {
		return this.businessInfo227;
	}

	/**
	 * 業務管理項目228を設定する.
	 * @param val 業務管理項目228
	 */
	public final void setBusinessInfo228(final String val) {
		this.businessInfo228 = val;
	}
	/**
	 * 業務管理項目228を取得する.
	 * @return 業務管理項目228
	 */
	public final String getBusinessInfo228() {
		return this.businessInfo228;
	}

	/**
	 * 業務管理項目229を設定する.
	 * @param val 業務管理項目229
	 */
	public final void setBusinessInfo229(final String val) {
		this.businessInfo229 = val;
	}
	/**
	 * 業務管理項目229を取得する.
	 * @return 業務管理項目229
	 */
	public final String getBusinessInfo229() {
		return this.businessInfo229;
	}

	/**
	 * 業務管理項目230を設定する.
	 * @param val 業務管理項目230
	 */
	public final void setBusinessInfo230(final String val) {
		this.businessInfo230 = val;
	}
	/**
	 * 業務管理項目230を取得する.
	 * @return 業務管理項目230
	 */
	public final String getBusinessInfo230() {
		return this.businessInfo230;
	}

	/**
	 * 業務管理項目231を設定する.
	 * @param val 業務管理項目231
	 */
	public final void setBusinessInfo231(final String val) {
		this.businessInfo231 = val;
	}
	/**
	 * 業務管理項目231を取得する.
	 * @return 業務管理項目231
	 */
	public final String getBusinessInfo231() {
		return this.businessInfo231;
	}

	/**
	 * 業務管理項目232を設定する.
	 * @param val 業務管理項目232
	 */
	public final void setBusinessInfo232(final String val) {
		this.businessInfo232 = val;
	}
	/**
	 * 業務管理項目232を取得する.
	 * @return 業務管理項目232
	 */
	public final String getBusinessInfo232() {
		return this.businessInfo232;
	}

	/**
	 * 業務管理項目233を設定する.
	 * @param val 業務管理項目233
	 */
	public final void setBusinessInfo233(final String val) {
		this.businessInfo233 = val;
	}
	/**
	 * 業務管理項目233を取得する.
	 * @return 業務管理項目233
	 */
	public final String getBusinessInfo233() {
		return this.businessInfo233;
	}

	/**
	 * 業務管理項目234を設定する.
	 * @param val 業務管理項目234
	 */
	public final void setBusinessInfo234(final String val) {
		this.businessInfo234 = val;
	}
	/**
	 * 業務管理項目234を取得する.
	 * @return 業務管理項目234
	 */
	public final String getBusinessInfo234() {
		return this.businessInfo234;
	}

	/**
	 * 業務管理項目235を設定する.
	 * @param val 業務管理項目235
	 */
	public final void setBusinessInfo235(final String val) {
		this.businessInfo235 = val;
	}
	/**
	 * 業務管理項目235を取得する.
	 * @return 業務管理項目235
	 */
	public final String getBusinessInfo235() {
		return this.businessInfo235;
	}

	/**
	 * 業務管理項目236を設定する.
	 * @param val 業務管理項目236
	 */
	public final void setBusinessInfo236(final String val) {
		this.businessInfo236 = val;
	}
	/**
	 * 業務管理項目236を取得する.
	 * @return 業務管理項目236
	 */
	public final String getBusinessInfo236() {
		return this.businessInfo236;
	}

	/**
	 * 業務管理項目237を設定する.
	 * @param val 業務管理項目237
	 */
	public final void setBusinessInfo237(final String val) {
		this.businessInfo237 = val;
	}
	/**
	 * 業務管理項目237を取得する.
	 * @return 業務管理項目237
	 */
	public final String getBusinessInfo237() {
		return this.businessInfo237;
	}

	/**
	 * 業務管理項目238を設定する.
	 * @param val 業務管理項目238
	 */
	public final void setBusinessInfo238(final String val) {
		this.businessInfo238 = val;
	}
	/**
	 * 業務管理項目238を取得する.
	 * @return 業務管理項目238
	 */
	public final String getBusinessInfo238() {
		return this.businessInfo238;
	}

	/**
	 * 業務管理項目239を設定する.
	 * @param val 業務管理項目239
	 */
	public final void setBusinessInfo239(final String val) {
		this.businessInfo239 = val;
	}
	/**
	 * 業務管理項目239を取得する.
	 * @return 業務管理項目239
	 */
	public final String getBusinessInfo239() {
		return this.businessInfo239;
	}

	/**
	 * 業務管理項目240を設定する.
	 * @param val 業務管理項目240
	 */
	public final void setBusinessInfo240(final String val) {
		this.businessInfo240 = val;
	}
	/**
	 * 業務管理項目240を取得する.
	 * @return 業務管理項目240
	 */
	public final String getBusinessInfo240() {
		return this.businessInfo240;
	}

	/**
	 * 業務管理項目241を設定する.
	 * @param val 業務管理項目241
	 */
	public final void setBusinessInfo241(final String val) {
		this.businessInfo241 = val;
	}
	/**
	 * 業務管理項目241を取得する.
	 * @return 業務管理項目241
	 */
	public final String getBusinessInfo241() {
		return this.businessInfo241;
	}

	/**
	 * 業務管理項目242を設定する.
	 * @param val 業務管理項目242
	 */
	public final void setBusinessInfo242(final String val) {
		this.businessInfo242 = val;
	}
	/**
	 * 業務管理項目242を取得する.
	 * @return 業務管理項目242
	 */
	public final String getBusinessInfo242() {
		return this.businessInfo242;
	}

	/**
	 * 業務管理項目243を設定する.
	 * @param val 業務管理項目243
	 */
	public final void setBusinessInfo243(final String val) {
		this.businessInfo243 = val;
	}
	/**
	 * 業務管理項目243を取得する.
	 * @return 業務管理項目243
	 */
	public final String getBusinessInfo243() {
		return this.businessInfo243;
	}

	/**
	 * 業務管理項目244を設定する.
	 * @param val 業務管理項目244
	 */
	public final void setBusinessInfo244(final String val) {
		this.businessInfo244 = val;
	}
	/**
	 * 業務管理項目244を取得する.
	 * @return 業務管理項目244
	 */
	public final String getBusinessInfo244() {
		return this.businessInfo244;
	}

	/**
	 * 業務管理項目245を設定する.
	 * @param val 業務管理項目245
	 */
	public final void setBusinessInfo245(final String val) {
		this.businessInfo245 = val;
	}
	/**
	 * 業務管理項目245を取得する.
	 * @return 業務管理項目245
	 */
	public final String getBusinessInfo245() {
		return this.businessInfo245;
	}

	/**
	 * 業務管理項目246を設定する.
	 * @param val 業務管理項目246
	 */
	public final void setBusinessInfo246(final String val) {
		this.businessInfo246 = val;
	}
	/**
	 * 業務管理項目246を取得する.
	 * @return 業務管理項目246
	 */
	public final String getBusinessInfo246() {
		return this.businessInfo246;
	}

	/**
	 * 業務管理項目247を設定する.
	 * @param val 業務管理項目247
	 */
	public final void setBusinessInfo247(final String val) {
		this.businessInfo247 = val;
	}
	/**
	 * 業務管理項目247を取得する.
	 * @return 業務管理項目247
	 */
	public final String getBusinessInfo247() {
		return this.businessInfo247;
	}

	/**
	 * 業務管理項目248を設定する.
	 * @param val 業務管理項目248
	 */
	public final void setBusinessInfo248(final String val) {
		this.businessInfo248 = val;
	}
	/**
	 * 業務管理項目248を取得する.
	 * @return 業務管理項目248
	 */
	public final String getBusinessInfo248() {
		return this.businessInfo248;
	}

	/**
	 * 業務管理項目249を設定する.
	 * @param val 業務管理項目249
	 */
	public final void setBusinessInfo249(final String val) {
		this.businessInfo249 = val;
	}
	/**
	 * 業務管理項目249を取得する.
	 * @return 業務管理項目249
	 */
	public final String getBusinessInfo249() {
		return this.businessInfo249;
	}

	/**
	 * 業務管理項目250を設定する.
	 * @param val 業務管理項目250
	 */
	public final void setBusinessInfo250(final String val) {
		this.businessInfo250 = val;
	}
	/**
	 * 業務管理項目250を取得する.
	 * @return 業務管理項目250
	 */
	public final String getBusinessInfo250() {
		return this.businessInfo250;
	}

	/**
	 * ワークリスト表示開始年月日を取得する.
	 * @return ワークリスト表示開始年月日
	 */
	public final Date getWorklistDisplayStartDate() {
		return this.worklistDisplatyStartDate;
	}

	/**
	 * ワークリスト表示開始年月日を設定する.
	 * @param val ワークリスト表示開始年月日
	 */
	public final void setWorklistDisplayStartDate(final Date val) {
		this.worklistDisplatyStartDate = val;
	}

	/**
	 * 期限年月日を取得する.
	 * @return 期限年月日
	 */
	public final Timestamp getLimitDateProcess() {
		return this.limitDateProcess;
	}

	/**
	 * 期限年月日を設定する.
	 * @param val 期限年月日
	 */
	public final void setLimitDateProcess(final Timestamp val) {
		this.limitDateProcess = val;
	}


	/**
	 * プロセス処理区分を取得する.
	 * @return プロセス処理区分
	 */
	public final String getProcessRefUnitType() {
		return processRefUnitType;
	}
	/**
	 * プロセス処理区分を設定する.
	 * @param pProcessShoriKbn プロセス処理区分

	 */
	public final void setProcessRefUnitType(final String pProcessShoriKbn) {
		this.processRefUnitType = pProcessShoriKbn;
	}
	/**
	 * 更新日時(プロセスインスタンス)を取得する.
	 * @return 更新日時(プロセスインスタンス)
	 */
	public final Timestamp getTimestampUpdatedProcess() {
		return this.timestampUpdatedProcess;
	}
	/**
	 * 更新日時(プロセスインスタンス)を設定する.
	 * @param pTimestampUpdatedProcess 更新日時(プロセスインスタンス)
	 */
	public final void setTimestampUpdatedProcess(final Timestamp pTimestampUpdatedProcess) {
		this.timestampUpdatedProcess = pTimestampUpdatedProcess;
	}
	/**
	 * 起案担当者会社コードを取得する.
	 * @return corporationCodeProxyStart 起案担当者会社コード
	 */
	public final String getCorporationCodeProxyStart() {
		return corporationCodeProxyStart;
	}
	/**
	 * 起案担当者会社コードを設定する.
	 * @param pCorporationCodeProxyStart 起案担当者会社コード
	 */
	public final void setCorporationCodeProxyStart(final String pCorporationCodeProxyStart) {
		this.corporationCodeProxyStart = pCorporationCodeProxyStart;
	}
	/**
	 * 起案組織コードを取得する.
	 * @return corporationCodeProxyStart 起案組織コード
	 */
	public final String getOrganizationCodeStart() {
		return organizationCodeStart;
	}
	/**
	 * 起案組織コードを設定する.
	 * @param pOrganizationCodeStart 起案組織コード
	 */
	public final void setOrganizationCodeStart(final String pOrganizationCodeStart) {
		this.organizationCodeStart = pOrganizationCodeStart;
	}
	/**
	 * 起案役職コードを取得する.
	 * @return postCodeStart 起案役職コード
	 */
	public String getPostCodeStart() {
		return postCodeStart;
	}
	/**
	 * 起案役職コードを設定する.
	 * @param pPostCodeStart 起案役職コード
	 */
	public void setPostCodeStart(String pPostCodeStart) {
		this.postCodeStart = pPostCodeStart;
	}
	/**
	 * 起案担当者ユーザコードを取得する.
	 * @return userCodeProxyStart 起案担当者ユーザコード
	 */
	public final String getUserCodeProxyStart() {
		return userCodeProxyStart;
	}
	/**
	 * 起案担当者ユーザコードを設定する.
	 * @param pUserCodeProxyStart 起案担当者ユーザコード
	 */
	public final void setUserCodeProxyStart(final String pUserCodeProxyStart) {
		this.userCodeProxyStart = pUserCodeProxyStart;
	}
}
