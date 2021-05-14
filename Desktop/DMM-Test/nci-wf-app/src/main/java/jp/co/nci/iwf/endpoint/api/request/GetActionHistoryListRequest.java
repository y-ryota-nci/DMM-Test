package jp.co.nci.iwf.endpoint.api.request;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.WfSortOrder;

public class GetActionHistoryListRequest extends ApiBaseRequest {
	/**
	 * 検索モード.
	 */
	public static final class Mode {
		/** コンストラクタ. */
		private Mode() { }
		/** 処理ユーザー承認履歴. */
		public static final String USER_A_HISTORY = "01";
		/** 処理グループ承認履歴. */
		public static final String GROUP_A_HISTORY = "02";
		/** 集約トレイ承認履歴. */
		public static final String P_AGGREGATION_A_HISTORY = "03";
		/** 指定伝票承認履歴. */
		public static final String PROCESS_A_HISTORY = "04";
		/** 情報共有ユーザー承認履歴. */
		public static final String INFO_SHARER_A_HISTORY = "05";
		/** 処理ユーザ + 情報共有ユーザー承認履歴. */
		public static final String USER_INFO_SHARER_HISTORY = "06";
		/** 全社履歴. */
		public static final String CORPORATION_HISTORY = "07";
	}

	/**
	 * 並び替え指定.
	 */
	public static final class SortType {
		/** コンストラクタ. */
		private SortType() { }
		/** プロセスID昇順. */
		public static final String PROCESS_ID_ASC 			= "0";
		/** プロセスID降順. */
		public static final String PROCESS_ID_DESC			= "1";
		/** 識別キー01～10⇒プロセスID昇順. */
		public static final String IDENTIFICATION_KEY_ASC		= "2";
		/** 識別キー01～10⇒プロセスID降順. */
		public static final String IDENTIFICATION_KEY_DESC	= "3";
	}

	/** データ抽出モード. */
	private String selectMode = SelectMode.BOTH;

	/**
	 * ステータス指定.
	 */
	public static final class SelectMode {
		/** 両方(データ＋カウント). */
		public static final String BOTH = "1";
		/** データのみ */
		public static final String DATA = "2";
		/** カウントのみ */
		public static final String COUNT = "3";
	}

	/** 検索モード. */
	private String mode;
	/** 伝票種類コードのリスト. */
	private List<String> processCategoryDefCodeList;
	/** プロセスID. */
	private Long processId;
	/** プロセス集約ID. */
	private Long processIdAggregation;
	/** 業務アクティビティ状態. */
	private String buinsessActivityStatus;
	/** 実行中指定. */
	private boolean isExecuting = false;
	/** 並び替え指定. */
	private String sortType;
	/** 業務管理情報01. */
	private String businessInfo01;
	/** 業務管理情報02. */
	private String businessInfo02;
	/** 業務管理情報03. */
	private String businessInfo03;
	/** 業務管理情報04. */
	private String businessInfo04;
	/** 業務管理情報05. */
	private String businessInfo05;
	/** 業務管理情報06. */
	private String businessInfo06;
	/** 業務管理情報07. */
	private String businessInfo07;
	/** 業務管理情報08. */
	private String businessInfo08;
	/** 業務管理情報09. */
	private String businessInfo09;
	/** 業務管理情報10. */
	private String businessInfo10;
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
	/** 文書ID. */
	private Long docId;
	/** メジャーバージョン. */
	private Long majorVersion;
	/** マイナーバージョン. */
	private Long minorVersion;
	/** 検索条件. */
	private List<WfSearchCondition<?>> searchConditionList;
	/** ソート順. */
	private List<WfSortOrder> sortOrderList;
	/** 業務プロセス状態リスト. */
	private List<String> businessProcessStatusList;
	/** パージされたデータとマージするかどうか. */
	private boolean isMerge = false;
	/** row no. */
	private Long rowNo;
	/** row count. */
	private Long rowCount;
	/** 添付ファイル検索文字列. */
	private String attachFileSearchCondition;
	/** JOIN. */
	private String joinParams;
	/** . */
	private String joinTables;
	/** . */
	private String joinWhere;

	/**
	 * 検索モードを取得する.
	 * @return 検索モード
	 */
	public final String getMode() {
		return mode;
	}
	/**
	 * 検索モードを設定する.
	 * @param pMode 検索モード

	 */
	public final void setMode(final String pMode) {
		this.mode = pMode;
	}
	/**
	 * 伝票種類コードのリストを取得する.
	 * @return 伝票種類コードのリスト
	 */
	public final List<String> getProcessCategoryDefCodeList() {
		return processCategoryDefCodeList;
	}
	/**
	 * 伝票種類コードのリストを設定する.
	 * @param pProcessCategoryDefCodeList 設定する伝票種類コードのリスト

	 */
	public final void setProcessCategoryDefCodeList(final List<String> pProcessCategoryDefCodeList) {
		this.processCategoryDefCodeList = pProcessCategoryDefCodeList;
	}
	/**
	 * プロセス集約IDを取得する.
	 * @return プロセス集約ID
	 */
	public final Long getProcessIdAggregation() {
		return processIdAggregation;
	}
	/**
	 * プロセス集約IDを設定する.
	 * @param pProcessIdAggregation 設定するプロセス集約ID
	 */
	public final void setProcessIdAggregation(final Long pProcessIdAggregation) {
		this.processIdAggregation = pProcessIdAggregation;
	}
	/**
	 * 実行中指定を取得する.
	 * @return 実行中指定
	 */
	public final boolean isExecuting() {
		return isExecuting;
	}
	/**
	 * 実行中指定を設定する.
	 * @param pExecFlg 設定する pExecFlg
	 */
	public final void setExecuting(final boolean pExecFlg) {
		this.isExecuting = pExecFlg;
	}
	/**
	 * 並び替え指定を取得する.
	 * @return 並び替え指定
	 */
	public final String getSortType() {
		return sortType;
	}
	/**
	 * 並び替え指定を設定する.
	 * @param pSortType 設定する sortType
	 */
	public final void setSortType(final String pSortType) {
		this.sortType = pSortType;
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
	 * @param pProcessId 設定するプロセスID
	 */
	public final void setProcessId(final Long pProcessId) {
		this.processId = pProcessId;
	}
	/**
	 * 業務管理項目01を取得する.
	 * @return 業務管理項目01
	 */
	public final String getBusinessInfo01() {
		return businessInfo01;
	}
	/**
	 * 業務管理項目01を設定する.
	 * @param pBusinessInfo01 設定する業務管理項目01
	 */
	public final void setBusinessInfo01(final String pBusinessInfo01) {
		this.businessInfo01 = pBusinessInfo01;
	}
	/**
	 * 業務管理項目02を取得する.
	 * @return businessInfo02
	 */
	public final String getBusinessInfo02() {
		return businessInfo02;
	}
	/**
	 * 業務管理項目02を設定する.
	 * @param pBusinessInfo02 設定する業務管理項目02
	 */
	public final void setBusinessInfo02(final String pBusinessInfo02) {
		this.businessInfo02 = pBusinessInfo02;
	}
	/**
	 * 業務管理項目03を取得する.
	 * @return 業務管理項目03
	 */
	public final String getBusinessInfo03() {
		return businessInfo03;
	}
	/**
	 * 業務管理項目03を設定する.
	 * @param pBusinessInfo03 設定する業務管理項目03
	 */
	public final void setBusinessInfo03(final String pBusinessInfo03) {
		this.businessInfo03 = pBusinessInfo03;
	}
	/**
	 * 業務管理項目04を取得する.
	 * @return 業務管理項目04
	 */
	public final String getBusinessInfo04() {
		return businessInfo04;
	}
	/**
	 * 業務管理項目04を設定する.
	 * @param pBusinessInfo04 設定する業務管理項目04
	 */
	public final void setBusinessInfo04(final String pBusinessInfo04) {
		this.businessInfo04 = pBusinessInfo04;
	}
	/**
	 * 業務管理項目05を取得する.
	 * @return 業務管理項目05
	 */
	public final String getBusinessInfo05() {
		return businessInfo05;
	}
	/**
	 * 業務管理項目05を設定する.
	 * @param pBusinessInfo05 設定する業務管理項目05
	 */
	public final void setBusinessInfo05(final String pBusinessInfo05) {
		this.businessInfo05 = pBusinessInfo05;
	}
	/**
	 * 業務管理項目06を取得する.
	 * @return 業務管理項目06
	 */
	public final String getBusinessInfo06() {
		return businessInfo06;
	}
	/**
	 * 業務管理項目06を設定する.
	 * @param pBusinessInfo06 設定する業務管理項目06
	 */
	public final void setBusinessInfo06(final String pBusinessInfo06) {
		this.businessInfo06 = pBusinessInfo06;
	}
	/**
	 * 業務管理項目07を取得する.
	 * @return 業務管理項目07
	 */
	public final String getBusinessInfo07() {
		return businessInfo07;
	}
	/**
	 * 業務管理項目07を設定する.
	 * @param pBusinessInfo07 設定する業務管理項目07
	 */
	public final void setBusinessInfo07(final String pBusinessInfo07) {
		this.businessInfo07 = pBusinessInfo07;
	}
	/**
	 * 業務管理項目08を取得する.
	 * @return 業務管理項目08
	 */
	public final String getBusinessInfo08() {
		return businessInfo08;
	}
	/**
	 * 業務管理項目08を設定する.
	 * @param pBusinessInfo08 設定する業務管理項目08
	 */
	public final void setBusinessInfo08(final String pBusinessInfo08) {
		this.businessInfo08 = pBusinessInfo08;
	}
	/**
	 * 業務管理項目09を取得する.
	 * @return 業務管理項目09
	 */
	public final String getBusinessInfo09() {
		return businessInfo09;
	}
	/**
	 * 業務管理項目09を設定する.
	 * @param pBusinessInfo09 設定する業務管理項目09
	 */
	public final void setBusinessInfo09(final String pBusinessInfo09) {
		this.businessInfo09 = pBusinessInfo09;
	}
	/**
	 * 業務管理項目10を取得する.
	 * @return 業務管理項目10
	 */
	public final String getBusinessInfo10() {
		return businessInfo10;
	}
	/**
	 * 業務管理項目10を設定する.
	 * @param pBusinessInfo10 設定する業務管理項目10
	 */
	public final void setBusinessInfo10(final String pBusinessInfo10) {
		this.businessInfo10 = pBusinessInfo10;
	}

	/**
	 * 業務管理項目011を取得する.
	 * @return 業務管理項目011
	 */
	public final String getBusinessInfo011() {
		return this.businessInfo011;
	}
	/**
	 * 業務管理項目011を設定する.
	 * @param pBusinessInfo011 業務管理項目011
	 */
	public final void setBusinessInfo011(final String pBusinessInfo011) {
		this.businessInfo011 = pBusinessInfo011;
	}

	/**
	 * 業務管理項目012を取得する.
	 * @return 業務管理項目012
	 */
	public final String getBusinessInfo012() {
		return this.businessInfo012;
	}
	/**
	 * 業務管理項目012を設定する.
	 * @param pBusinessInfo012 業務管理項目012
	 */
	public final void setBusinessInfo012(final String pBusinessInfo012) {
		this.businessInfo012 = pBusinessInfo012;
	}

	/**
	 * 業務管理項目013を取得する.
	 * @return 業務管理項目013
	 */
	public final String getBusinessInfo013() {
		return this.businessInfo013;
	}
	/**
	 * 業務管理項目013を設定する.
	 * @param pBusinessInfo013 業務管理項目013
	 */
	public final void setBusinessInfo013(final String pBusinessInfo013) {
		this.businessInfo013 = pBusinessInfo013;
	}

	/**
	 * 業務管理項目014を取得する.
	 * @return 業務管理項目014
	 */
	public final String getBusinessInfo014() {
		return this.businessInfo014;
	}
	/**
	 * 業務管理項目014を設定する.
	 * @param pBusinessInfo014 業務管理項目014
	 */
	public final void setBusinessInfo014(final String pBusinessInfo014) {
		this.businessInfo014 = pBusinessInfo014;
	}

	/**
	 * 業務管理項目015を取得する.
	 * @return 業務管理項目015
	 */
	public final String getBusinessInfo015() {
		return this.businessInfo015;
	}
	/**
	 * 業務管理項目015を設定する.
	 * @param pBusinessInfo015 業務管理項目015
	 */
	public final void setBusinessInfo015(final String pBusinessInfo015) {
		this.businessInfo015 = pBusinessInfo015;
	}

	/**
	 * 業務管理項目016を取得する.
	 * @return 業務管理項目016
	 */
	public final String getBusinessInfo016() {
		return this.businessInfo016;
	}
	/**
	 * 業務管理項目016を設定する.
	 * @param pBusinessInfo016 業務管理項目016
	 */
	public final void setBusinessInfo016(final String pBusinessInfo016) {
		this.businessInfo016 = pBusinessInfo016;
	}

	/**
	 * 業務管理項目017を取得する.
	 * @return 業務管理項目017
	 */
	public final String getBusinessInfo017() {
		return this.businessInfo017;
	}
	/**
	 * 業務管理項目017を設定する.
	 * @param pBusinessInfo017 業務管理項目017
	 */
	public final void setBusinessInfo017(final String pBusinessInfo017) {
		this.businessInfo017 = pBusinessInfo017;
	}

	/**
	 * 業務管理項目018を取得する.
	 * @return 業務管理項目018
	 */
	public final String getBusinessInfo018() {
		return this.businessInfo018;
	}
	/**
	 * 業務管理項目018を設定する.
	 * @param pBusinessInfo018 業務管理項目018
	 */
	public final void setBusinessInfo018(final String pBusinessInfo018) {
		this.businessInfo018 = pBusinessInfo018;
	}

	/**
	 * 業務管理項目019を取得する.
	 * @return 業務管理項目019
	 */
	public final String getBusinessInfo019() {
		return this.businessInfo019;
	}
	/**
	 * 業務管理項目019を設定する.
	 * @param pBusinessInfo019 業務管理項目019
	 */
	public final void setBusinessInfo019(final String pBusinessInfo019) {
		this.businessInfo019 = pBusinessInfo019;
	}

	/**
	 * 業務管理項目020を取得する.
	 * @return 業務管理項目020
	 */
	public final String getBusinessInfo020() {
		return this.businessInfo020;
	}
	/**
	 * 業務管理項目020を設定する.
	 * @param pBusinessInfo020 業務管理項目020
	 */
	public final void setBusinessInfo020(final String pBusinessInfo020) {
		this.businessInfo020 = pBusinessInfo020;
	}

	/**
	 * 業務管理項目021を取得する.
	 * @return 業務管理項目021
	 */
	public final String getBusinessInfo021() {
		return this.businessInfo021;
	}
	/**
	 * 業務管理項目021を設定する.
	 * @param pBusinessInfo021 業務管理項目021
	 */
	public final void setBusinessInfo021(final String pBusinessInfo021) {
		this.businessInfo021 = pBusinessInfo021;
	}

	/**
	 * 業務管理項目022を取得する.
	 * @return 業務管理項目022
	 */
	public final String getBusinessInfo022() {
		return this.businessInfo022;
	}
	/**
	 * 業務管理項目022を設定する.
	 * @param pBusinessInfo022 業務管理項目022
	 */
	public final void setBusinessInfo022(final String pBusinessInfo022) {
		this.businessInfo022 = pBusinessInfo022;
	}

	/**
	 * 業務管理項目023を取得する.
	 * @return 業務管理項目023
	 */
	public final String getBusinessInfo023() {
		return this.businessInfo023;
	}
	/**
	 * 業務管理項目023を設定する.
	 * @param pBusinessInfo023 業務管理項目023
	 */
	public final void setBusinessInfo023(final String pBusinessInfo023) {
		this.businessInfo023 = pBusinessInfo023;
	}

	/**
	 * 業務管理項目024を取得する.
	 * @return 業務管理項目024
	 */
	public final String getBusinessInfo024() {
		return this.businessInfo024;
	}
	/**
	 * 業務管理項目024を設定する.
	 * @param pBusinessInfo024 業務管理項目024
	 */
	public final void setBusinessInfo024(final String pBusinessInfo024) {
		this.businessInfo024 = pBusinessInfo024;
	}

	/**
	 * 業務管理項目025を取得する.
	 * @return 業務管理項目025
	 */
	public final String getBusinessInfo025() {
		return this.businessInfo025;
	}
	/**
	 * 業務管理項目025を設定する.
	 * @param pBusinessInfo025 業務管理項目025
	 */
	public final void setBusinessInfo025(final String pBusinessInfo025) {
		this.businessInfo025 = pBusinessInfo025;
	}

	/**
	 * 業務管理項目026を取得する.
	 * @return 業務管理項目026
	 */
	public final String getBusinessInfo026() {
		return this.businessInfo026;
	}
	/**
	 * 業務管理項目026を設定する.
	 * @param pBusinessInfo026 業務管理項目026
	 */
	public final void setBusinessInfo026(final String pBusinessInfo026) {
		this.businessInfo026 = pBusinessInfo026;
	}

	/**
	 * 業務管理項目027を取得する.
	 * @return 業務管理項目027
	 */
	public final String getBusinessInfo027() {
		return this.businessInfo027;
	}
	/**
	 * 業務管理項目027を設定する.
	 * @param pBusinessInfo027 業務管理項目027
	 */
	public final void setBusinessInfo027(final String pBusinessInfo027) {
		this.businessInfo027 = pBusinessInfo027;
	}

	/**
	 * 業務管理項目028を取得する.
	 * @return 業務管理項目028
	 */
	public final String getBusinessInfo028() {
		return this.businessInfo028;
	}
	/**
	 * 業務管理項目028を設定する.
	 * @param pBusinessInfo028 業務管理項目028
	 */
	public final void setBusinessInfo028(final String pBusinessInfo028) {
		this.businessInfo028 = pBusinessInfo028;
	}

	/**
	 * 業務管理項目029を取得する.
	 * @return 業務管理項目029
	 */
	public final String getBusinessInfo029() {
		return this.businessInfo029;
	}
	/**
	 * 業務管理項目029を設定する.
	 * @param pBusinessInfo029 業務管理項目029
	 */
	public final void setBusinessInfo029(final String pBusinessInfo029) {
		this.businessInfo029 = pBusinessInfo029;
	}

	/**
	 * 業務管理項目030を取得する.
	 * @return 業務管理項目030
	 */
	public final String getBusinessInfo030() {
		return this.businessInfo030;
	}
	/**
	 * 業務管理項目030を設定する.
	 * @param pBusinessInfo030 業務管理項目030
	 */
	public final void setBusinessInfo030(final String pBusinessInfo030) {
		this.businessInfo030 = pBusinessInfo030;
	}

	/**
	 * 業務管理項目031を取得する.
	 * @return 業務管理項目031
	 */
	public final String getBusinessInfo031() {
		return this.businessInfo031;
	}
	/**
	 * 業務管理項目031を設定する.
	 * @param pBusinessInfo031 業務管理項目031
	 */
	public final void setBusinessInfo031(final String pBusinessInfo031) {
		this.businessInfo031 = pBusinessInfo031;
	}

	/**
	 * 業務管理項目032を取得する.
	 * @return 業務管理項目032
	 */
	public final String getBusinessInfo032() {
		return this.businessInfo032;
	}
	/**
	 * 業務管理項目032を設定する.
	 * @param pBusinessInfo032 業務管理項目032
	 */
	public final void setBusinessInfo032(final String pBusinessInfo032) {
		this.businessInfo032 = pBusinessInfo032;
	}

	/**
	 * 業務管理項目033を取得する.
	 * @return 業務管理項目033
	 */
	public final String getBusinessInfo033() {
		return this.businessInfo033;
	}
	/**
	 * 業務管理項目033を設定する.
	 * @param pBusinessInfo033 業務管理項目033
	 */
	public final void setBusinessInfo033(final String pBusinessInfo033) {
		this.businessInfo033 = pBusinessInfo033;
	}

	/**
	 * 業務管理項目034を取得する.
	 * @return 業務管理項目034
	 */
	public final String getBusinessInfo034() {
		return this.businessInfo034;
	}
	/**
	 * 業務管理項目034を設定する.
	 * @param pBusinessInfo034 業務管理項目034
	 */
	public final void setBusinessInfo034(final String pBusinessInfo034) {
		this.businessInfo034 = pBusinessInfo034;
	}

	/**
	 * 業務管理項目035を取得する.
	 * @return 業務管理項目035
	 */
	public final String getBusinessInfo035() {
		return this.businessInfo035;
	}
	/**
	 * 業務管理項目035を設定する.
	 * @param pBusinessInfo035 業務管理項目035
	 */
	public final void setBusinessInfo035(final String pBusinessInfo035) {
		this.businessInfo035 = pBusinessInfo035;
	}

	/**
	 * 業務管理項目036を取得する.
	 * @return 業務管理項目036
	 */
	public final String getBusinessInfo036() {
		return this.businessInfo036;
	}
	/**
	 * 業務管理項目036を設定する.
	 * @param pBusinessInfo036 業務管理項目036
	 */
	public final void setBusinessInfo036(final String pBusinessInfo036) {
		this.businessInfo036 = pBusinessInfo036;
	}

	/**
	 * 業務管理項目037を取得する.
	 * @return 業務管理項目037
	 */
	public final String getBusinessInfo037() {
		return this.businessInfo037;
	}
	/**
	 * 業務管理項目037を設定する.
	 * @param pBusinessInfo037 業務管理項目037
	 */
	public final void setBusinessInfo037(final String pBusinessInfo037) {
		this.businessInfo037 = pBusinessInfo037;
	}

	/**
	 * 業務管理項目038を取得する.
	 * @return 業務管理項目038
	 */
	public final String getBusinessInfo038() {
		return this.businessInfo038;
	}
	/**
	 * 業務管理項目038を設定する.
	 * @param pBusinessInfo038 業務管理項目038
	 */
	public final void setBusinessInfo038(final String pBusinessInfo038) {
		this.businessInfo038 = pBusinessInfo038;
	}

	/**
	 * 業務管理項目039を取得する.
	 * @return 業務管理項目039
	 */
	public final String getBusinessInfo039() {
		return this.businessInfo039;
	}
	/**
	 * 業務管理項目039を設定する.
	 * @param pBusinessInfo039 業務管理項目039
	 */
	public final void setBusinessInfo039(final String pBusinessInfo039) {
		this.businessInfo039 = pBusinessInfo039;
	}

	/**
	 * 業務管理項目040を取得する.
	 * @return 業務管理項目040
	 */
	public final String getBusinessInfo040() {
		return this.businessInfo040;
	}
	/**
	 * 業務管理項目040を設定する.
	 * @param pBusinessInfo040 業務管理項目040
	 */
	public final void setBusinessInfo040(final String pBusinessInfo040) {
		this.businessInfo040 = pBusinessInfo040;
	}

	/**
	 * 業務管理項目041を取得する.
	 * @return 業務管理項目041
	 */
	public final String getBusinessInfo041() {
		return this.businessInfo041;
	}
	/**
	 * 業務管理項目041を設定する.
	 * @param pBusinessInfo041 業務管理項目041
	 */
	public final void setBusinessInfo041(final String pBusinessInfo041) {
		this.businessInfo041 = pBusinessInfo041;
	}

	/**
	 * 業務管理項目042を取得する.
	 * @return 業務管理項目042
	 */
	public final String getBusinessInfo042() {
		return this.businessInfo042;
	}
	/**
	 * 業務管理項目042を設定する.
	 * @param pBusinessInfo042 業務管理項目042
	 */
	public final void setBusinessInfo042(final String pBusinessInfo042) {
		this.businessInfo042 = pBusinessInfo042;
	}

	/**
	 * 業務管理項目043を取得する.
	 * @return 業務管理項目043
	 */
	public final String getBusinessInfo043() {
		return this.businessInfo043;
	}
	/**
	 * 業務管理項目043を設定する.
	 * @param pBusinessInfo043 業務管理項目043
	 */
	public final void setBusinessInfo043(final String pBusinessInfo043) {
		this.businessInfo043 = pBusinessInfo043;
	}

	/**
	 * 業務管理項目044を取得する.
	 * @return 業務管理項目044
	 */
	public final String getBusinessInfo044() {
		return this.businessInfo044;
	}
	/**
	 * 業務管理項目044を設定する.
	 * @param pBusinessInfo044 業務管理項目044
	 */
	public final void setBusinessInfo044(final String pBusinessInfo044) {
		this.businessInfo044 = pBusinessInfo044;
	}

	/**
	 * 業務管理項目045を取得する.
	 * @return 業務管理項目045
	 */
	public final String getBusinessInfo045() {
		return this.businessInfo045;
	}
	/**
	 * 業務管理項目045を設定する.
	 * @param pBusinessInfo045 業務管理項目045
	 */
	public final void setBusinessInfo045(final String pBusinessInfo045) {
		this.businessInfo045 = pBusinessInfo045;
	}

	/**
	 * 業務管理項目046を取得する.
	 * @return 業務管理項目046
	 */
	public final String getBusinessInfo046() {
		return this.businessInfo046;
	}
	/**
	 * 業務管理項目046を設定する.
	 * @param pBusinessInfo046 業務管理項目046
	 */
	public final void setBusinessInfo046(final String pBusinessInfo046) {
		this.businessInfo046 = pBusinessInfo046;
	}

	/**
	 * 業務管理項目047を取得する.
	 * @return 業務管理項目047
	 */
	public final String getBusinessInfo047() {
		return this.businessInfo047;
	}
	/**
	 * 業務管理項目047を設定する.
	 * @param pBusinessInfo047 業務管理項目047
	 */
	public final void setBusinessInfo047(final String pBusinessInfo047) {
		this.businessInfo047 = pBusinessInfo047;
	}

	/**
	 * 業務管理項目048を取得する.
	 * @return 業務管理項目048
	 */
	public final String getBusinessInfo048() {
		return this.businessInfo048;
	}
	/**
	 * 業務管理項目048を設定する.
	 * @param pBusinessInfo048 業務管理項目048
	 */
	public final void setBusinessInfo048(final String pBusinessInfo048) {
		this.businessInfo048 = pBusinessInfo048;
	}

	/**
	 * 業務管理項目049を取得する.
	 * @return 業務管理項目049
	 */
	public final String getBusinessInfo049() {
		return this.businessInfo049;
	}
	/**
	 * 業務管理項目049を設定する.
	 * @param pBusinessInfo049 業務管理項目049
	 */
	public final void setBusinessInfo049(final String pBusinessInfo049) {
		this.businessInfo049 = pBusinessInfo049;
	}

	/**
	 * 業務管理項目050を取得する.
	 * @return 業務管理項目050
	 */
	public final String getBusinessInfo050() {
		return this.businessInfo050;
	}
	/**
	 * 業務管理項目050を設定する.
	 * @param pBusinessInfo050 業務管理項目050
	 */
	public final void setBusinessInfo050(final String pBusinessInfo050) {
		this.businessInfo050 = pBusinessInfo050;
	}

	/**
	 * 業務管理項目051を取得する.
	 * @return 業務管理項目051
	 */
	public final String getBusinessInfo051() {
		return this.businessInfo051;
	}
	/**
	 * 業務管理項目051を設定する.
	 * @param pBusinessInfo051 業務管理項目051
	 */
	public final void setBusinessInfo051(final String pBusinessInfo051) {
		this.businessInfo051 = pBusinessInfo051;
	}

	/**
	 * 業務管理項目052を取得する.
	 * @return 業務管理項目052
	 */
	public final String getBusinessInfo052() {
		return this.businessInfo052;
	}
	/**
	 * 業務管理項目052を設定する.
	 * @param pBusinessInfo052 業務管理項目052
	 */
	public final void setBusinessInfo052(final String pBusinessInfo052) {
		this.businessInfo052 = pBusinessInfo052;
	}

	/**
	 * 業務管理項目053を取得する.
	 * @return 業務管理項目053
	 */
	public final String getBusinessInfo053() {
		return this.businessInfo053;
	}
	/**
	 * 業務管理項目053を設定する.
	 * @param pBusinessInfo053 業務管理項目053
	 */
	public final void setBusinessInfo053(final String pBusinessInfo053) {
		this.businessInfo053 = pBusinessInfo053;
	}

	/**
	 * 業務管理項目054を取得する.
	 * @return 業務管理項目054
	 */
	public final String getBusinessInfo054() {
		return this.businessInfo054;
	}
	/**
	 * 業務管理項目054を設定する.
	 * @param pBusinessInfo054 業務管理項目054
	 */
	public final void setBusinessInfo054(final String pBusinessInfo054) {
		this.businessInfo054 = pBusinessInfo054;
	}

	/**
	 * 業務管理項目055を取得する.
	 * @return 業務管理項目055
	 */
	public final String getBusinessInfo055() {
		return this.businessInfo055;
	}
	/**
	 * 業務管理項目055を設定する.
	 * @param pBusinessInfo055 業務管理項目055
	 */
	public final void setBusinessInfo055(final String pBusinessInfo055) {
		this.businessInfo055 = pBusinessInfo055;
	}

	/**
	 * 業務管理項目056を取得する.
	 * @return 業務管理項目056
	 */
	public final String getBusinessInfo056() {
		return this.businessInfo056;
	}
	/**
	 * 業務管理項目056を設定する.
	 * @param pBusinessInfo056 業務管理項目056
	 */
	public final void setBusinessInfo056(final String pBusinessInfo056) {
		this.businessInfo056 = pBusinessInfo056;
	}

	/**
	 * 業務管理項目057を取得する.
	 * @return 業務管理項目057
	 */
	public final String getBusinessInfo057() {
		return this.businessInfo057;
	}
	/**
	 * 業務管理項目057を設定する.
	 * @param pBusinessInfo057 業務管理項目057
	 */
	public final void setBusinessInfo057(final String pBusinessInfo057) {
		this.businessInfo057 = pBusinessInfo057;
	}

	/**
	 * 業務管理項目058を取得する.
	 * @return 業務管理項目058
	 */
	public final String getBusinessInfo058() {
		return this.businessInfo058;
	}
	/**
	 * 業務管理項目058を設定する.
	 * @param pBusinessInfo058 業務管理項目058
	 */
	public final void setBusinessInfo058(final String pBusinessInfo058) {
		this.businessInfo058 = pBusinessInfo058;
	}

	/**
	 * 業務管理項目059を取得する.
	 * @return 業務管理項目059
	 */
	public final String getBusinessInfo059() {
		return this.businessInfo059;
	}
	/**
	 * 業務管理項目059を設定する.
	 * @param pBusinessInfo059 業務管理項目059
	 */
	public final void setBusinessInfo059(final String pBusinessInfo059) {
		this.businessInfo059 = pBusinessInfo059;
	}

	/**
	 * 業務管理項目060を取得する.
	 * @return 業務管理項目060
	 */
	public final String getBusinessInfo060() {
		return this.businessInfo060;
	}
	/**
	 * 業務管理項目060を設定する.
	 * @param pBusinessInfo060 業務管理項目060
	 */
	public final void setBusinessInfo060(final String pBusinessInfo060) {
		this.businessInfo060 = pBusinessInfo060;
	}

	/**
	 * 業務管理項目061を取得する.
	 * @return 業務管理項目061
	 */
	public final String getBusinessInfo061() {
		return this.businessInfo061;
	}
	/**
	 * 業務管理項目061を設定する.
	 * @param pBusinessInfo061 業務管理項目061
	 */
	public final void setBusinessInfo061(final String pBusinessInfo061) {
		this.businessInfo061 = pBusinessInfo061;
	}

	/**
	 * 業務管理項目062を取得する.
	 * @return 業務管理項目062
	 */
	public final String getBusinessInfo062() {
		return this.businessInfo062;
	}
	/**
	 * 業務管理項目062を設定する.
	 * @param pBusinessInfo062 業務管理項目062
	 */
	public final void setBusinessInfo062(final String pBusinessInfo062) {
		this.businessInfo062 = pBusinessInfo062;
	}

	/**
	 * 業務管理項目063を取得する.
	 * @return 業務管理項目063
	 */
	public final String getBusinessInfo063() {
		return this.businessInfo063;
	}
	/**
	 * 業務管理項目063を設定する.
	 * @param pBusinessInfo063 業務管理項目063
	 */
	public final void setBusinessInfo063(final String pBusinessInfo063) {
		this.businessInfo063 = pBusinessInfo063;
	}

	/**
	 * 業務管理項目064を取得する.
	 * @return 業務管理項目064
	 */
	public final String getBusinessInfo064() {
		return this.businessInfo064;
	}
	/**
	 * 業務管理項目064を設定する.
	 * @param pBusinessInfo064 業務管理項目064
	 */
	public final void setBusinessInfo064(final String pBusinessInfo064) {
		this.businessInfo064 = pBusinessInfo064;
	}

	/**
	 * 業務管理項目065を取得する.
	 * @return 業務管理項目065
	 */
	public final String getBusinessInfo065() {
		return this.businessInfo065;
	}
	/**
	 * 業務管理項目065を設定する.
	 * @param pBusinessInfo065 業務管理項目065
	 */
	public final void setBusinessInfo065(final String pBusinessInfo065) {
		this.businessInfo065 = pBusinessInfo065;
	}

	/**
	 * 業務管理項目066を取得する.
	 * @return 業務管理項目066
	 */
	public final String getBusinessInfo066() {
		return this.businessInfo066;
	}
	/**
	 * 業務管理項目066を設定する.
	 * @param pBusinessInfo066 業務管理項目066
	 */
	public final void setBusinessInfo066(final String pBusinessInfo066) {
		this.businessInfo066 = pBusinessInfo066;
	}

	/**
	 * 業務管理項目067を取得する.
	 * @return 業務管理項目067
	 */
	public final String getBusinessInfo067() {
		return this.businessInfo067;
	}
	/**
	 * 業務管理項目067を設定する.
	 * @param pBusinessInfo067 業務管理項目067
	 */
	public final void setBusinessInfo067(final String pBusinessInfo067) {
		this.businessInfo067 = pBusinessInfo067;
	}

	/**
	 * 業務管理項目068を取得する.
	 * @return 業務管理項目068
	 */
	public final String getBusinessInfo068() {
		return this.businessInfo068;
	}
	/**
	 * 業務管理項目068を設定する.
	 * @param pBusinessInfo068 業務管理項目068
	 */
	public final void setBusinessInfo068(final String pBusinessInfo068) {
		this.businessInfo068 = pBusinessInfo068;
	}

	/**
	 * 業務管理項目069を取得する.
	 * @return 業務管理項目069
	 */
	public final String getBusinessInfo069() {
		return this.businessInfo069;
	}
	/**
	 * 業務管理項目069を設定する.
	 * @param pBusinessInfo069 業務管理項目069
	 */
	public final void setBusinessInfo069(final String pBusinessInfo069) {
		this.businessInfo069 = pBusinessInfo069;
	}

	/**
	 * 業務管理項目070を取得する.
	 * @return 業務管理項目070
	 */
	public final String getBusinessInfo070() {
		return this.businessInfo070;
	}
	/**
	 * 業務管理項目070を設定する.
	 * @param pBusinessInfo070 業務管理項目070
	 */
	public final void setBusinessInfo070(final String pBusinessInfo070) {
		this.businessInfo070 = pBusinessInfo070;
	}

	/**
	 * 業務管理項目071を取得する.
	 * @return 業務管理項目071
	 */
	public final String getBusinessInfo071() {
		return this.businessInfo071;
	}
	/**
	 * 業務管理項目071を設定する.
	 * @param pBusinessInfo071 業務管理項目071
	 */
	public final void setBusinessInfo071(final String pBusinessInfo071) {
		this.businessInfo071 = pBusinessInfo071;
	}

	/**
	 * 業務管理項目072を取得する.
	 * @return 業務管理項目072
	 */
	public final String getBusinessInfo072() {
		return this.businessInfo072;
	}
	/**
	 * 業務管理項目072を設定する.
	 * @param pBusinessInfo072 業務管理項目072
	 */
	public final void setBusinessInfo072(final String pBusinessInfo072) {
		this.businessInfo072 = pBusinessInfo072;
	}

	/**
	 * 業務管理項目073を取得する.
	 * @return 業務管理項目073
	 */
	public final String getBusinessInfo073() {
		return this.businessInfo073;
	}
	/**
	 * 業務管理項目073を設定する.
	 * @param pBusinessInfo073 業務管理項目073
	 */
	public final void setBusinessInfo073(final String pBusinessInfo073) {
		this.businessInfo073 = pBusinessInfo073;
	}

	/**
	 * 業務管理項目074を取得する.
	 * @return 業務管理項目074
	 */
	public final String getBusinessInfo074() {
		return this.businessInfo074;
	}
	/**
	 * 業務管理項目074を設定する.
	 * @param pBusinessInfo074 業務管理項目074
	 */
	public final void setBusinessInfo074(final String pBusinessInfo074) {
		this.businessInfo074 = pBusinessInfo074;
	}

	/**
	 * 業務管理項目075を取得する.
	 * @return 業務管理項目075
	 */
	public final String getBusinessInfo075() {
		return this.businessInfo075;
	}
	/**
	 * 業務管理項目075を設定する.
	 * @param pBusinessInfo075 業務管理項目075
	 */
	public final void setBusinessInfo075(final String pBusinessInfo075) {
		this.businessInfo075 = pBusinessInfo075;
	}

	/**
	 * 業務管理項目076を取得する.
	 * @return 業務管理項目076
	 */
	public final String getBusinessInfo076() {
		return this.businessInfo076;
	}
	/**
	 * 業務管理項目076を設定する.
	 * @param pBusinessInfo076 業務管理項目076
	 */
	public final void setBusinessInfo076(final String pBusinessInfo076) {
		this.businessInfo076 = pBusinessInfo076;
	}

	/**
	 * 業務管理項目077を取得する.
	 * @return 業務管理項目077
	 */
	public final String getBusinessInfo077() {
		return this.businessInfo077;
	}
	/**
	 * 業務管理項目077を設定する.
	 * @param pBusinessInfo077 業務管理項目077
	 */
	public final void setBusinessInfo077(final String pBusinessInfo077) {
		this.businessInfo077 = pBusinessInfo077;
	}

	/**
	 * 業務管理項目078を取得する.
	 * @return 業務管理項目078
	 */
	public final String getBusinessInfo078() {
		return this.businessInfo078;
	}
	/**
	 * 業務管理項目078を設定する.
	 * @param pBusinessInfo078 業務管理項目078
	 */
	public final void setBusinessInfo078(final String pBusinessInfo078) {
		this.businessInfo078 = pBusinessInfo078;
	}

	/**
	 * 業務管理項目079を取得する.
	 * @return 業務管理項目079
	 */
	public final String getBusinessInfo079() {
		return this.businessInfo079;
	}
	/**
	 * 業務管理項目079を設定する.
	 * @param pBusinessInfo079 業務管理項目079
	 */
	public final void setBusinessInfo079(final String pBusinessInfo079) {
		this.businessInfo079 = pBusinessInfo079;
	}

	/**
	 * 業務管理項目080を取得する.
	 * @return 業務管理項目080
	 */
	public final String getBusinessInfo080() {
		return this.businessInfo080;
	}
	/**
	 * 業務管理項目080を設定する.
	 * @param pBusinessInfo080 業務管理項目080
	 */
	public final void setBusinessInfo080(final String pBusinessInfo080) {
		this.businessInfo080 = pBusinessInfo080;
	}

	/**
	 * 業務管理項目081を取得する.
	 * @return 業務管理項目081
	 */
	public final String getBusinessInfo081() {
		return this.businessInfo081;
	}
	/**
	 * 業務管理項目081を設定する.
	 * @param pBusinessInfo081 業務管理項目081
	 */
	public final void setBusinessInfo081(final String pBusinessInfo081) {
		this.businessInfo081 = pBusinessInfo081;
	}

	/**
	 * 業務管理項目082を取得する.
	 * @return 業務管理項目082
	 */
	public final String getBusinessInfo082() {
		return this.businessInfo082;
	}
	/**
	 * 業務管理項目082を設定する.
	 * @param pBusinessInfo082 業務管理項目082
	 */
	public final void setBusinessInfo082(final String pBusinessInfo082) {
		this.businessInfo082 = pBusinessInfo082;
	}

	/**
	 * 業務管理項目083を取得する.
	 * @return 業務管理項目083
	 */
	public final String getBusinessInfo083() {
		return this.businessInfo083;
	}
	/**
	 * 業務管理項目083を設定する.
	 * @param pBusinessInfo083 業務管理項目083
	 */
	public final void setBusinessInfo083(final String pBusinessInfo083) {
		this.businessInfo083 = pBusinessInfo083;
	}

	/**
	 * 業務管理項目084を取得する.
	 * @return 業務管理項目084
	 */
	public final String getBusinessInfo084() {
		return this.businessInfo084;
	}
	/**
	 * 業務管理項目084を設定する.
	 * @param pBusinessInfo084 業務管理項目084
	 */
	public final void setBusinessInfo084(final String pBusinessInfo084) {
		this.businessInfo084 = pBusinessInfo084;
	}

	/**
	 * 業務管理項目085を取得する.
	 * @return 業務管理項目085
	 */
	public final String getBusinessInfo085() {
		return this.businessInfo085;
	}
	/**
	 * 業務管理項目085を設定する.
	 * @param pBusinessInfo085 業務管理項目085
	 */
	public final void setBusinessInfo085(final String pBusinessInfo085) {
		this.businessInfo085 = pBusinessInfo085;
	}

	/**
	 * 業務管理項目086を取得する.
	 * @return 業務管理項目086
	 */
	public final String getBusinessInfo086() {
		return this.businessInfo086;
	}
	/**
	 * 業務管理項目086を設定する.
	 * @param pBusinessInfo086 業務管理項目086
	 */
	public final void setBusinessInfo086(final String pBusinessInfo086) {
		this.businessInfo086 = pBusinessInfo086;
	}

	/**
	 * 業務管理項目087を取得する.
	 * @return 業務管理項目087
	 */
	public final String getBusinessInfo087() {
		return this.businessInfo087;
	}
	/**
	 * 業務管理項目087を設定する.
	 * @param pBusinessInfo087 業務管理項目087
	 */
	public final void setBusinessInfo087(final String pBusinessInfo087) {
		this.businessInfo087 = pBusinessInfo087;
	}

	/**
	 * 業務管理項目088を取得する.
	 * @return 業務管理項目088
	 */
	public final String getBusinessInfo088() {
		return this.businessInfo088;
	}
	/**
	 * 業務管理項目088を設定する.
	 * @param pBusinessInfo088 業務管理項目088
	 */
	public final void setBusinessInfo088(final String pBusinessInfo088) {
		this.businessInfo088 = pBusinessInfo088;
	}

	/**
	 * 業務管理項目089を取得する.
	 * @return 業務管理項目089
	 */
	public final String getBusinessInfo089() {
		return this.businessInfo089;
	}
	/**
	 * 業務管理項目089を設定する.
	 * @param pBusinessInfo089 業務管理項目089
	 */
	public final void setBusinessInfo089(final String pBusinessInfo089) {
		this.businessInfo089 = pBusinessInfo089;
	}

	/**
	 * 業務管理項目090を取得する.
	 * @return 業務管理項目090
	 */
	public final String getBusinessInfo090() {
		return this.businessInfo090;
	}
	/**
	 * 業務管理項目090を設定する.
	 * @param pBusinessInfo090 業務管理項目090
	 */
	public final void setBusinessInfo090(final String pBusinessInfo090) {
		this.businessInfo090 = pBusinessInfo090;
	}

	/**
	 * 業務管理項目091を取得する.
	 * @return 業務管理項目091
	 */
	public final String getBusinessInfo091() {
		return this.businessInfo091;
	}
	/**
	 * 業務管理項目091を設定する.
	 * @param pBusinessInfo091 業務管理項目091
	 */
	public final void setBusinessInfo091(final String pBusinessInfo091) {
		this.businessInfo091 = pBusinessInfo091;
	}

	/**
	 * 業務管理項目092を取得する.
	 * @return 業務管理項目092
	 */
	public final String getBusinessInfo092() {
		return this.businessInfo092;
	}
	/**
	 * 業務管理項目092を設定する.
	 * @param pBusinessInfo092 業務管理項目092
	 */
	public final void setBusinessInfo092(final String pBusinessInfo092) {
		this.businessInfo092 = pBusinessInfo092;
	}

	/**
	 * 業務管理項目093を取得する.
	 * @return 業務管理項目093
	 */
	public final String getBusinessInfo093() {
		return this.businessInfo093;
	}
	/**
	 * 業務管理項目093を設定する.
	 * @param pBusinessInfo093 業務管理項目093
	 */
	public final void setBusinessInfo093(final String pBusinessInfo093) {
		this.businessInfo093 = pBusinessInfo093;
	}

	/**
	 * 業務管理項目094を取得する.
	 * @return 業務管理項目094
	 */
	public final String getBusinessInfo094() {
		return this.businessInfo094;
	}
	/**
	 * 業務管理項目094を設定する.
	 * @param pBusinessInfo094 業務管理項目094
	 */
	public final void setBusinessInfo094(final String pBusinessInfo094) {
		this.businessInfo094 = pBusinessInfo094;
	}

	/**
	 * 業務管理項目095を取得する.
	 * @return 業務管理項目095
	 */
	public final String getBusinessInfo095() {
		return this.businessInfo095;
	}
	/**
	 * 業務管理項目095を設定する.
	 * @param pBusinessInfo095 業務管理項目095
	 */
	public final void setBusinessInfo095(final String pBusinessInfo095) {
		this.businessInfo095 = pBusinessInfo095;
	}

	/**
	 * 業務管理項目096を取得する.
	 * @return 業務管理項目096
	 */
	public final String getBusinessInfo096() {
		return this.businessInfo096;
	}
	/**
	 * 業務管理項目096を設定する.
	 * @param pBusinessInfo096 業務管理項目096
	 */
	public final void setBusinessInfo096(final String pBusinessInfo096) {
		this.businessInfo096 = pBusinessInfo096;
	}

	/**
	 * 業務管理項目097を取得する.
	 * @return 業務管理項目097
	 */
	public final String getBusinessInfo097() {
		return this.businessInfo097;
	}
	/**
	 * 業務管理項目097を設定する.
	 * @param pBusinessInfo097 業務管理項目097
	 */
	public final void setBusinessInfo097(final String pBusinessInfo097) {
		this.businessInfo097 = pBusinessInfo097;
	}

	/**
	 * 業務管理項目098を取得する.
	 * @return 業務管理項目098
	 */
	public final String getBusinessInfo098() {
		return this.businessInfo098;
	}
	/**
	 * 業務管理項目098を設定する.
	 * @param pBusinessInfo098 業務管理項目098
	 */
	public final void setBusinessInfo098(final String pBusinessInfo098) {
		this.businessInfo098 = pBusinessInfo098;
	}

	/**
	 * 業務管理項目099を取得する.
	 * @return 業務管理項目099
	 */
	public final String getBusinessInfo099() {
		return this.businessInfo099;
	}
	/**
	 * 業務管理項目099を設定する.
	 * @param pBusinessInfo099 業務管理項目099
	 */
	public final void setBusinessInfo099(final String pBusinessInfo099) {
		this.businessInfo099 = pBusinessInfo099;
	}

	/**
	 * 業務管理項目100を取得する.
	 * @return 業務管理項目100
	 */
	public final String getBusinessInfo100() {
		return this.businessInfo100;
	}
	/**
	 * 業務管理項目100を設定する.
	 * @param pBusinessInfo100 業務管理項目100
	 */
	public final void setBusinessInfo100(final String pBusinessInfo100) {
		this.businessInfo100 = pBusinessInfo100;
	}

	/**
	 * 業務管理項目101を取得する.
	 * @return 業務管理項目101
	 */
	public final String getBusinessInfo101() {
		return this.businessInfo101;
	}
	/**
	 * 業務管理項目101を設定する.
	 * @param pBusinessInfo101 業務管理項目101
	 */
	public final void setBusinessInfo101(final String pBusinessInfo101) {
		this.businessInfo101 = pBusinessInfo101;
	}

	/**
	 * 業務管理項目102を取得する.
	 * @return 業務管理項目102
	 */
	public final String getBusinessInfo102() {
		return this.businessInfo102;
	}
	/**
	 * 業務管理項目102を設定する.
	 * @param pBusinessInfo102 業務管理項目102
	 */
	public final void setBusinessInfo102(final String pBusinessInfo102) {
		this.businessInfo102 = pBusinessInfo102;
	}

	/**
	 * 業務管理項目103を取得する.
	 * @return 業務管理項目103
	 */
	public final String getBusinessInfo103() {
		return this.businessInfo103;
	}
	/**
	 * 業務管理項目103を設定する.
	 * @param pBusinessInfo103 業務管理項目103
	 */
	public final void setBusinessInfo103(final String pBusinessInfo103) {
		this.businessInfo103 = pBusinessInfo103;
	}

	/**
	 * 業務管理項目104を取得する.
	 * @return 業務管理項目104
	 */
	public final String getBusinessInfo104() {
		return this.businessInfo104;
	}
	/**
	 * 業務管理項目104を設定する.
	 * @param pBusinessInfo104 業務管理項目104
	 */
	public final void setBusinessInfo104(final String pBusinessInfo104) {
		this.businessInfo104 = pBusinessInfo104;
	}

	/**
	 * 業務管理項目105を取得する.
	 * @return 業務管理項目105
	 */
	public final String getBusinessInfo105() {
		return this.businessInfo105;
	}
	/**
	 * 業務管理項目105を設定する.
	 * @param pBusinessInfo105 業務管理項目105
	 */
	public final void setBusinessInfo105(final String pBusinessInfo105) {
		this.businessInfo105 = pBusinessInfo105;
	}

	/**
	 * 業務管理項目106を取得する.
	 * @return 業務管理項目106
	 */
	public final String getBusinessInfo106() {
		return this.businessInfo106;
	}
	/**
	 * 業務管理項目106を設定する.
	 * @param pBusinessInfo106 業務管理項目106
	 */
	public final void setBusinessInfo106(final String pBusinessInfo106) {
		this.businessInfo106 = pBusinessInfo106;
	}

	/**
	 * 業務管理項目107を取得する.
	 * @return 業務管理項目107
	 */
	public final String getBusinessInfo107() {
		return this.businessInfo107;
	}
	/**
	 * 業務管理項目107を設定する.
	 * @param pBusinessInfo107 業務管理項目107
	 */
	public final void setBusinessInfo107(final String pBusinessInfo107) {
		this.businessInfo107 = pBusinessInfo107;
	}

	/**
	 * 業務管理項目108を取得する.
	 * @return 業務管理項目108
	 */
	public final String getBusinessInfo108() {
		return this.businessInfo108;
	}
	/**
	 * 業務管理項目108を設定する.
	 * @param pBusinessInfo108 業務管理項目108
	 */
	public final void setBusinessInfo108(final String pBusinessInfo108) {
		this.businessInfo108 = pBusinessInfo108;
	}

	/**
	 * 業務管理項目109を取得する.
	 * @return 業務管理項目109
	 */
	public final String getBusinessInfo109() {
		return this.businessInfo109;
	}
	/**
	 * 業務管理項目109を設定する.
	 * @param pBusinessInfo109 業務管理項目109
	 */
	public final void setBusinessInfo109(final String pBusinessInfo109) {
		this.businessInfo109 = pBusinessInfo109;
	}

	/**
	 * 業務管理項目110を取得する.
	 * @return 業務管理項目110
	 */
	public final String getBusinessInfo110() {
		return this.businessInfo110;
	}
	/**
	 * 業務管理項目110を設定する.
	 * @param pBusinessInfo110 業務管理項目110
	 */
	public final void setBusinessInfo110(final String pBusinessInfo110) {
		this.businessInfo110 = pBusinessInfo110;
	}

	/**
	 * 業務管理項目111を取得する.
	 * @return 業務管理項目111
	 */
	public final String getBusinessInfo111() {
		return this.businessInfo111;
	}
	/**
	 * 業務管理項目111を設定する.
	 * @param pBusinessInfo111 業務管理項目111
	 */
	public final void setBusinessInfo111(final String pBusinessInfo111) {
		this.businessInfo111 = pBusinessInfo111;
	}

	/**
	 * 業務管理項目112を取得する.
	 * @return 業務管理項目112
	 */
	public final String getBusinessInfo112() {
		return this.businessInfo112;
	}
	/**
	 * 業務管理項目112を設定する.
	 * @param pBusinessInfo112 業務管理項目112
	 */
	public final void setBusinessInfo112(final String pBusinessInfo112) {
		this.businessInfo112 = pBusinessInfo112;
	}

	/**
	 * 業務管理項目113を取得する.
	 * @return 業務管理項目113
	 */
	public final String getBusinessInfo113() {
		return this.businessInfo113;
	}
	/**
	 * 業務管理項目113を設定する.
	 * @param pBusinessInfo113 業務管理項目113
	 */
	public final void setBusinessInfo113(final String pBusinessInfo113) {
		this.businessInfo113 = pBusinessInfo113;
	}

	/**
	 * 業務管理項目114を取得する.
	 * @return 業務管理項目114
	 */
	public final String getBusinessInfo114() {
		return this.businessInfo114;
	}
	/**
	 * 業務管理項目114を設定する.
	 * @param pBusinessInfo114 業務管理項目114
	 */
	public final void setBusinessInfo114(final String pBusinessInfo114) {
		this.businessInfo114 = pBusinessInfo114;
	}

	/**
	 * 業務管理項目115を取得する.
	 * @return 業務管理項目115
	 */
	public final String getBusinessInfo115() {
		return this.businessInfo115;
	}
	/**
	 * 業務管理項目115を設定する.
	 * @param pBusinessInfo115 業務管理項目115
	 */
	public final void setBusinessInfo115(final String pBusinessInfo115) {
		this.businessInfo115 = pBusinessInfo115;
	}

	/**
	 * 業務管理項目116を取得する.
	 * @return 業務管理項目116
	 */
	public final String getBusinessInfo116() {
		return this.businessInfo116;
	}
	/**
	 * 業務管理項目116を設定する.
	 * @param pBusinessInfo116 業務管理項目116
	 */
	public final void setBusinessInfo116(final String pBusinessInfo116) {
		this.businessInfo116 = pBusinessInfo116;
	}

	/**
	 * 業務管理項目117を取得する.
	 * @return 業務管理項目117
	 */
	public final String getBusinessInfo117() {
		return this.businessInfo117;
	}
	/**
	 * 業務管理項目117を設定する.
	 * @param pBusinessInfo117 業務管理項目117
	 */
	public final void setBusinessInfo117(final String pBusinessInfo117) {
		this.businessInfo117 = pBusinessInfo117;
	}

	/**
	 * 業務管理項目118を取得する.
	 * @return 業務管理項目118
	 */
	public final String getBusinessInfo118() {
		return this.businessInfo118;
	}
	/**
	 * 業務管理項目118を設定する.
	 * @param pBusinessInfo118 業務管理項目118
	 */
	public final void setBusinessInfo118(final String pBusinessInfo118) {
		this.businessInfo118 = pBusinessInfo118;
	}

	/**
	 * 業務管理項目119を取得する.
	 * @return 業務管理項目119
	 */
	public final String getBusinessInfo119() {
		return this.businessInfo119;
	}
	/**
	 * 業務管理項目119を設定する.
	 * @param pBusinessInfo119 業務管理項目119
	 */
	public final void setBusinessInfo119(final String pBusinessInfo119) {
		this.businessInfo119 = pBusinessInfo119;
	}

	/**
	 * 業務管理項目120を取得する.
	 * @return 業務管理項目120
	 */
	public final String getBusinessInfo120() {
		return this.businessInfo120;
	}
	/**
	 * 業務管理項目120を設定する.
	 * @param pBusinessInfo120 業務管理項目120
	 */
	public final void setBusinessInfo120(final String pBusinessInfo120) {
		this.businessInfo120 = pBusinessInfo120;
	}

	/**
	 * 業務管理項目121を取得する.
	 * @return 業務管理項目121
	 */
	public final String getBusinessInfo121() {
		return this.businessInfo121;
	}
	/**
	 * 業務管理項目121を設定する.
	 * @param pBusinessInfo121 業務管理項目121
	 */
	public final void setBusinessInfo121(final String pBusinessInfo121) {
		this.businessInfo121 = pBusinessInfo121;
	}

	/**
	 * 業務管理項目122を取得する.
	 * @return 業務管理項目122
	 */
	public final String getBusinessInfo122() {
		return this.businessInfo122;
	}
	/**
	 * 業務管理項目122を設定する.
	 * @param pBusinessInfo122 業務管理項目122
	 */
	public final void setBusinessInfo122(final String pBusinessInfo122) {
		this.businessInfo122 = pBusinessInfo122;
	}

	/**
	 * 業務管理項目123を取得する.
	 * @return 業務管理項目123
	 */
	public final String getBusinessInfo123() {
		return this.businessInfo123;
	}
	/**
	 * 業務管理項目123を設定する.
	 * @param pBusinessInfo123 業務管理項目123
	 */
	public final void setBusinessInfo123(final String pBusinessInfo123) {
		this.businessInfo123 = pBusinessInfo123;
	}

	/**
	 * 業務管理項目124を取得する.
	 * @return 業務管理項目124
	 */
	public final String getBusinessInfo124() {
		return this.businessInfo124;
	}
	/**
	 * 業務管理項目124を設定する.
	 * @param pBusinessInfo124 業務管理項目124
	 */
	public final void setBusinessInfo124(final String pBusinessInfo124) {
		this.businessInfo124 = pBusinessInfo124;
	}

	/**
	 * 業務管理項目125を取得する.
	 * @return 業務管理項目125
	 */
	public final String getBusinessInfo125() {
		return this.businessInfo125;
	}
	/**
	 * 業務管理項目125を設定する.
	 * @param pBusinessInfo125 業務管理項目125
	 */
	public final void setBusinessInfo125(final String pBusinessInfo125) {
		this.businessInfo125 = pBusinessInfo125;
	}

	/**
	 * 業務管理項目126を取得する.
	 * @return 業務管理項目126
	 */
	public final String getBusinessInfo126() {
		return this.businessInfo126;
	}
	/**
	 * 業務管理項目126を設定する.
	 * @param pBusinessInfo126 業務管理項目126
	 */
	public final void setBusinessInfo126(final String pBusinessInfo126) {
		this.businessInfo126 = pBusinessInfo126;
	}

	/**
	 * 業務管理項目127を取得する.
	 * @return 業務管理項目127
	 */
	public final String getBusinessInfo127() {
		return this.businessInfo127;
	}
	/**
	 * 業務管理項目127を設定する.
	 * @param pBusinessInfo127 業務管理項目127
	 */
	public final void setBusinessInfo127(final String pBusinessInfo127) {
		this.businessInfo127 = pBusinessInfo127;
	}

	/**
	 * 業務管理項目128を取得する.
	 * @return 業務管理項目128
	 */
	public final String getBusinessInfo128() {
		return this.businessInfo128;
	}
	/**
	 * 業務管理項目128を設定する.
	 * @param pBusinessInfo128 業務管理項目128
	 */
	public final void setBusinessInfo128(final String pBusinessInfo128) {
		this.businessInfo128 = pBusinessInfo128;
	}

	/**
	 * 業務管理項目129を取得する.
	 * @return 業務管理項目129
	 */
	public final String getBusinessInfo129() {
		return this.businessInfo129;
	}
	/**
	 * 業務管理項目129を設定する.
	 * @param pBusinessInfo129 業務管理項目129
	 */
	public final void setBusinessInfo129(final String pBusinessInfo129) {
		this.businessInfo129 = pBusinessInfo129;
	}

	/**
	 * 業務管理項目130を取得する.
	 * @return 業務管理項目130
	 */
	public final String getBusinessInfo130() {
		return this.businessInfo130;
	}
	/**
	 * 業務管理項目130を設定する.
	 * @param pBusinessInfo130 業務管理項目130
	 */
	public final void setBusinessInfo130(final String pBusinessInfo130) {
		this.businessInfo130 = pBusinessInfo130;
	}

	/**
	 * 業務管理項目131を取得する.
	 * @return 業務管理項目131
	 */
	public final String getBusinessInfo131() {
		return this.businessInfo131;
	}
	/**
	 * 業務管理項目131を設定する.
	 * @param pBusinessInfo131 業務管理項目131
	 */
	public final void setBusinessInfo131(final String pBusinessInfo131) {
		this.businessInfo131 = pBusinessInfo131;
	}

	/**
	 * 業務管理項目132を取得する.
	 * @return 業務管理項目132
	 */
	public final String getBusinessInfo132() {
		return this.businessInfo132;
	}
	/**
	 * 業務管理項目132を設定する.
	 * @param pBusinessInfo132 業務管理項目132
	 */
	public final void setBusinessInfo132(final String pBusinessInfo132) {
		this.businessInfo132 = pBusinessInfo132;
	}

	/**
	 * 業務管理項目133を取得する.
	 * @return 業務管理項目133
	 */
	public final String getBusinessInfo133() {
		return this.businessInfo133;
	}
	/**
	 * 業務管理項目133を設定する.
	 * @param pBusinessInfo133 業務管理項目133
	 */
	public final void setBusinessInfo133(final String pBusinessInfo133) {
		this.businessInfo133 = pBusinessInfo133;
	}

	/**
	 * 業務管理項目134を取得する.
	 * @return 業務管理項目134
	 */
	public final String getBusinessInfo134() {
		return this.businessInfo134;
	}
	/**
	 * 業務管理項目134を設定する.
	 * @param pBusinessInfo134 業務管理項目134
	 */
	public final void setBusinessInfo134(final String pBusinessInfo134) {
		this.businessInfo134 = pBusinessInfo134;
	}

	/**
	 * 業務管理項目135を取得する.
	 * @return 業務管理項目135
	 */
	public final String getBusinessInfo135() {
		return this.businessInfo135;
	}
	/**
	 * 業務管理項目135を設定する.
	 * @param pBusinessInfo135 業務管理項目135
	 */
	public final void setBusinessInfo135(final String pBusinessInfo135) {
		this.businessInfo135 = pBusinessInfo135;
	}

	/**
	 * 業務管理項目136を取得する.
	 * @return 業務管理項目136
	 */
	public final String getBusinessInfo136() {
		return this.businessInfo136;
	}
	/**
	 * 業務管理項目136を設定する.
	 * @param pBusinessInfo136 業務管理項目136
	 */
	public final void setBusinessInfo136(final String pBusinessInfo136) {
		this.businessInfo136 = pBusinessInfo136;
	}

	/**
	 * 業務管理項目137を取得する.
	 * @return 業務管理項目137
	 */
	public final String getBusinessInfo137() {
		return this.businessInfo137;
	}
	/**
	 * 業務管理項目137を設定する.
	 * @param pBusinessInfo137 業務管理項目137
	 */
	public final void setBusinessInfo137(final String pBusinessInfo137) {
		this.businessInfo137 = pBusinessInfo137;
	}

	/**
	 * 業務管理項目138を取得する.
	 * @return 業務管理項目138
	 */
	public final String getBusinessInfo138() {
		return this.businessInfo138;
	}
	/**
	 * 業務管理項目138を設定する.
	 * @param pBusinessInfo138 業務管理項目138
	 */
	public final void setBusinessInfo138(final String pBusinessInfo138) {
		this.businessInfo138 = pBusinessInfo138;
	}

	/**
	 * 業務管理項目139を取得する.
	 * @return 業務管理項目139
	 */
	public final String getBusinessInfo139() {
		return this.businessInfo139;
	}
	/**
	 * 業務管理項目139を設定する.
	 * @param pBusinessInfo139 業務管理項目139
	 */
	public final void setBusinessInfo139(final String pBusinessInfo139) {
		this.businessInfo139 = pBusinessInfo139;
	}

	/**
	 * 業務管理項目140を取得する.
	 * @return 業務管理項目140
	 */
	public final String getBusinessInfo140() {
		return this.businessInfo140;
	}
	/**
	 * 業務管理項目140を設定する.
	 * @param pBusinessInfo140 業務管理項目140
	 */
	public final void setBusinessInfo140(final String pBusinessInfo140) {
		this.businessInfo140 = pBusinessInfo140;
	}

	/**
	 * 業務管理項目141を取得する.
	 * @return 業務管理項目141
	 */
	public final String getBusinessInfo141() {
		return this.businessInfo141;
	}
	/**
	 * 業務管理項目141を設定する.
	 * @param pBusinessInfo141 業務管理項目141
	 */
	public final void setBusinessInfo141(final String pBusinessInfo141) {
		this.businessInfo141 = pBusinessInfo141;
	}

	/**
	 * 業務管理項目142を取得する.
	 * @return 業務管理項目142
	 */
	public final String getBusinessInfo142() {
		return this.businessInfo142;
	}
	/**
	 * 業務管理項目142を設定する.
	 * @param pBusinessInfo142 業務管理項目142
	 */
	public final void setBusinessInfo142(final String pBusinessInfo142) {
		this.businessInfo142 = pBusinessInfo142;
	}

	/**
	 * 業務管理項目143を取得する.
	 * @return 業務管理項目143
	 */
	public final String getBusinessInfo143() {
		return this.businessInfo143;
	}
	/**
	 * 業務管理項目143を設定する.
	 * @param pBusinessInfo143 業務管理項目143
	 */
	public final void setBusinessInfo143(final String pBusinessInfo143) {
		this.businessInfo143 = pBusinessInfo143;
	}

	/**
	 * 業務管理項目144を取得する.
	 * @return 業務管理項目144
	 */
	public final String getBusinessInfo144() {
		return this.businessInfo144;
	}
	/**
	 * 業務管理項目144を設定する.
	 * @param pBusinessInfo144 業務管理項目144
	 */
	public final void setBusinessInfo144(final String pBusinessInfo144) {
		this.businessInfo144 = pBusinessInfo144;
	}

	/**
	 * 業務管理項目145を取得する.
	 * @return 業務管理項目145
	 */
	public final String getBusinessInfo145() {
		return this.businessInfo145;
	}
	/**
	 * 業務管理項目145を設定する.
	 * @param pBusinessInfo145 業務管理項目145
	 */
	public final void setBusinessInfo145(final String pBusinessInfo145) {
		this.businessInfo145 = pBusinessInfo145;
	}

	/**
	 * 業務管理項目146を取得する.
	 * @return 業務管理項目146
	 */
	public final String getBusinessInfo146() {
		return this.businessInfo146;
	}
	/**
	 * 業務管理項目146を設定する.
	 * @param pBusinessInfo146 業務管理項目146
	 */
	public final void setBusinessInfo146(final String pBusinessInfo146) {
		this.businessInfo146 = pBusinessInfo146;
	}

	/**
	 * 業務管理項目147を取得する.
	 * @return 業務管理項目147
	 */
	public final String getBusinessInfo147() {
		return this.businessInfo147;
	}
	/**
	 * 業務管理項目147を設定する.
	 * @param pBusinessInfo147 業務管理項目147
	 */
	public final void setBusinessInfo147(final String pBusinessInfo147) {
		this.businessInfo147 = pBusinessInfo147;
	}

	/**
	 * 業務管理項目148を取得する.
	 * @return 業務管理項目148
	 */
	public final String getBusinessInfo148() {
		return this.businessInfo148;
	}
	/**
	 * 業務管理項目148を設定する.
	 * @param pBusinessInfo148 業務管理項目148
	 */
	public final void setBusinessInfo148(final String pBusinessInfo148) {
		this.businessInfo148 = pBusinessInfo148;
	}

	/**
	 * 業務管理項目149を取得する.
	 * @return 業務管理項目149
	 */
	public final String getBusinessInfo149() {
		return this.businessInfo149;
	}
	/**
	 * 業務管理項目149を設定する.
	 * @param pBusinessInfo149 業務管理項目149
	 */
	public final void setBusinessInfo149(final String pBusinessInfo149) {
		this.businessInfo149 = pBusinessInfo149;
	}

	/**
	 * 業務管理項目150を取得する.
	 * @return 業務管理項目150
	 */
	public final String getBusinessInfo150() {
		return this.businessInfo150;
	}
	/**
	 * 業務管理項目150を設定する.
	 * @param pBusinessInfo150 業務管理項目150
	 */
	public final void setBusinessInfo150(final String pBusinessInfo150) {
		this.businessInfo150 = pBusinessInfo150;
	}

	/**
	 * 業務管理項目151を取得する.
	 * @return 業務管理項目151
	 */
	public final String getBusinessInfo151() {
		return this.businessInfo151;
	}
	/**
	 * 業務管理項目151を設定する.
	 * @param pBusinessInfo151 業務管理項目151
	 */
	public final void setBusinessInfo151(final String pBusinessInfo151) {
		this.businessInfo151 = pBusinessInfo151;
	}

	/**
	 * 業務管理項目152を取得する.
	 * @return 業務管理項目152
	 */
	public final String getBusinessInfo152() {
		return this.businessInfo152;
	}
	/**
	 * 業務管理項目152を設定する.
	 * @param pBusinessInfo152 業務管理項目152
	 */
	public final void setBusinessInfo152(final String pBusinessInfo152) {
		this.businessInfo152 = pBusinessInfo152;
	}

	/**
	 * 業務管理項目153を取得する.
	 * @return 業務管理項目153
	 */
	public final String getBusinessInfo153() {
		return this.businessInfo153;
	}
	/**
	 * 業務管理項目153を設定する.
	 * @param pBusinessInfo153 業務管理項目153
	 */
	public final void setBusinessInfo153(final String pBusinessInfo153) {
		this.businessInfo153 = pBusinessInfo153;
	}

	/**
	 * 業務管理項目154を取得する.
	 * @return 業務管理項目154
	 */
	public final String getBusinessInfo154() {
		return this.businessInfo154;
	}
	/**
	 * 業務管理項目154を設定する.
	 * @param pBusinessInfo154 業務管理項目154
	 */
	public final void setBusinessInfo154(final String pBusinessInfo154) {
		this.businessInfo154 = pBusinessInfo154;
	}

	/**
	 * 業務管理項目155を取得する.
	 * @return 業務管理項目155
	 */
	public final String getBusinessInfo155() {
		return this.businessInfo155;
	}
	/**
	 * 業務管理項目155を設定する.
	 * @param pBusinessInfo155 業務管理項目155
	 */
	public final void setBusinessInfo155(final String pBusinessInfo155) {
		this.businessInfo155 = pBusinessInfo155;
	}

	/**
	 * 業務管理項目156を取得する.
	 * @return 業務管理項目156
	 */
	public final String getBusinessInfo156() {
		return this.businessInfo156;
	}
	/**
	 * 業務管理項目156を設定する.
	 * @param pBusinessInfo156 業務管理項目156
	 */
	public final void setBusinessInfo156(final String pBusinessInfo156) {
		this.businessInfo156 = pBusinessInfo156;
	}

	/**
	 * 業務管理項目157を取得する.
	 * @return 業務管理項目157
	 */
	public final String getBusinessInfo157() {
		return this.businessInfo157;
	}
	/**
	 * 業務管理項目157を設定する.
	 * @param pBusinessInfo157 業務管理項目157
	 */
	public final void setBusinessInfo157(final String pBusinessInfo157) {
		this.businessInfo157 = pBusinessInfo157;
	}

	/**
	 * 業務管理項目158を取得する.
	 * @return 業務管理項目158
	 */
	public final String getBusinessInfo158() {
		return this.businessInfo158;
	}
	/**
	 * 業務管理項目158を設定する.
	 * @param pBusinessInfo158 業務管理項目158
	 */
	public final void setBusinessInfo158(final String pBusinessInfo158) {
		this.businessInfo158 = pBusinessInfo158;
	}

	/**
	 * 業務管理項目159を取得する.
	 * @return 業務管理項目159
	 */
	public final String getBusinessInfo159() {
		return this.businessInfo159;
	}
	/**
	 * 業務管理項目159を設定する.
	 * @param pBusinessInfo159 業務管理項目159
	 */
	public final void setBusinessInfo159(final String pBusinessInfo159) {
		this.businessInfo159 = pBusinessInfo159;
	}

	/**
	 * 業務管理項目160を取得する.
	 * @return 業務管理項目160
	 */
	public final String getBusinessInfo160() {
		return this.businessInfo160;
	}
	/**
	 * 業務管理項目160を設定する.
	 * @param pBusinessInfo160 業務管理項目160
	 */
	public final void setBusinessInfo160(final String pBusinessInfo160) {
		this.businessInfo160 = pBusinessInfo160;
	}

	/**
	 * 業務管理項目161を取得する.
	 * @return 業務管理項目161
	 */
	public final String getBusinessInfo161() {
		return this.businessInfo161;
	}
	/**
	 * 業務管理項目161を設定する.
	 * @param pBusinessInfo161 業務管理項目161
	 */
	public final void setBusinessInfo161(final String pBusinessInfo161) {
		this.businessInfo161 = pBusinessInfo161;
	}

	/**
	 * 業務管理項目162を取得する.
	 * @return 業務管理項目162
	 */
	public final String getBusinessInfo162() {
		return this.businessInfo162;
	}
	/**
	 * 業務管理項目162を設定する.
	 * @param pBusinessInfo162 業務管理項目162
	 */
	public final void setBusinessInfo162(final String pBusinessInfo162) {
		this.businessInfo162 = pBusinessInfo162;
	}

	/**
	 * 業務管理項目163を取得する.
	 * @return 業務管理項目163
	 */
	public final String getBusinessInfo163() {
		return this.businessInfo163;
	}
	/**
	 * 業務管理項目163を設定する.
	 * @param pBusinessInfo163 業務管理項目163
	 */
	public final void setBusinessInfo163(final String pBusinessInfo163) {
		this.businessInfo163 = pBusinessInfo163;
	}

	/**
	 * 業務管理項目164を取得する.
	 * @return 業務管理項目164
	 */
	public final String getBusinessInfo164() {
		return this.businessInfo164;
	}
	/**
	 * 業務管理項目164を設定する.
	 * @param pBusinessInfo164 業務管理項目164
	 */
	public final void setBusinessInfo164(final String pBusinessInfo164) {
		this.businessInfo164 = pBusinessInfo164;
	}

	/**
	 * 業務管理項目165を取得する.
	 * @return 業務管理項目165
	 */
	public final String getBusinessInfo165() {
		return this.businessInfo165;
	}
	/**
	 * 業務管理項目165を設定する.
	 * @param pBusinessInfo165 業務管理項目165
	 */
	public final void setBusinessInfo165(final String pBusinessInfo165) {
		this.businessInfo165 = pBusinessInfo165;
	}

	/**
	 * 業務管理項目166を取得する.
	 * @return 業務管理項目166
	 */
	public final String getBusinessInfo166() {
		return this.businessInfo166;
	}
	/**
	 * 業務管理項目166を設定する.
	 * @param pBusinessInfo166 業務管理項目166
	 */
	public final void setBusinessInfo166(final String pBusinessInfo166) {
		this.businessInfo166 = pBusinessInfo166;
	}

	/**
	 * 業務管理項目167を取得する.
	 * @return 業務管理項目167
	 */
	public final String getBusinessInfo167() {
		return this.businessInfo167;
	}
	/**
	 * 業務管理項目167を設定する.
	 * @param pBusinessInfo167 業務管理項目167
	 */
	public final void setBusinessInfo167(final String pBusinessInfo167) {
		this.businessInfo167 = pBusinessInfo167;
	}

	/**
	 * 業務管理項目168を取得する.
	 * @return 業務管理項目168
	 */
	public final String getBusinessInfo168() {
		return this.businessInfo168;
	}
	/**
	 * 業務管理項目168を設定する.
	 * @param pBusinessInfo168 業務管理項目168
	 */
	public final void setBusinessInfo168(final String pBusinessInfo168) {
		this.businessInfo168 = pBusinessInfo168;
	}

	/**
	 * 業務管理項目169を取得する.
	 * @return 業務管理項目169
	 */
	public final String getBusinessInfo169() {
		return this.businessInfo169;
	}
	/**
	 * 業務管理項目169を設定する.
	 * @param pBusinessInfo169 業務管理項目169
	 */
	public final void setBusinessInfo169(final String pBusinessInfo169) {
		this.businessInfo169 = pBusinessInfo169;
	}

	/**
	 * 業務管理項目170を取得する.
	 * @return 業務管理項目170
	 */
	public final String getBusinessInfo170() {
		return this.businessInfo170;
	}
	/**
	 * 業務管理項目170を設定する.
	 * @param pBusinessInfo170 業務管理項目170
	 */
	public final void setBusinessInfo170(final String pBusinessInfo170) {
		this.businessInfo170 = pBusinessInfo170;
	}

	/**
	 * 業務管理項目171を取得する.
	 * @return 業務管理項目171
	 */
	public final String getBusinessInfo171() {
		return this.businessInfo171;
	}
	/**
	 * 業務管理項目171を設定する.
	 * @param pBusinessInfo171 業務管理項目171
	 */
	public final void setBusinessInfo171(final String pBusinessInfo171) {
		this.businessInfo171 = pBusinessInfo171;
	}

	/**
	 * 業務管理項目172を取得する.
	 * @return 業務管理項目172
	 */
	public final String getBusinessInfo172() {
		return this.businessInfo172;
	}
	/**
	 * 業務管理項目172を設定する.
	 * @param pBusinessInfo172 業務管理項目172
	 */
	public final void setBusinessInfo172(final String pBusinessInfo172) {
		this.businessInfo172 = pBusinessInfo172;
	}

	/**
	 * 業務管理項目173を取得する.
	 * @return 業務管理項目173
	 */
	public final String getBusinessInfo173() {
		return this.businessInfo173;
	}
	/**
	 * 業務管理項目173を設定する.
	 * @param pBusinessInfo173 業務管理項目173
	 */
	public final void setBusinessInfo173(final String pBusinessInfo173) {
		this.businessInfo173 = pBusinessInfo173;
	}

	/**
	 * 業務管理項目174を取得する.
	 * @return 業務管理項目174
	 */
	public final String getBusinessInfo174() {
		return this.businessInfo174;
	}
	/**
	 * 業務管理項目174を設定する.
	 * @param pBusinessInfo174 業務管理項目174
	 */
	public final void setBusinessInfo174(final String pBusinessInfo174) {
		this.businessInfo174 = pBusinessInfo174;
	}

	/**
	 * 業務管理項目175を取得する.
	 * @return 業務管理項目175
	 */
	public final String getBusinessInfo175() {
		return this.businessInfo175;
	}
	/**
	 * 業務管理項目175を設定する.
	 * @param pBusinessInfo175 業務管理項目175
	 */
	public final void setBusinessInfo175(final String pBusinessInfo175) {
		this.businessInfo175 = pBusinessInfo175;
	}

	/**
	 * 業務管理項目176を取得する.
	 * @return 業務管理項目176
	 */
	public final String getBusinessInfo176() {
		return this.businessInfo176;
	}
	/**
	 * 業務管理項目176を設定する.
	 * @param pBusinessInfo176 業務管理項目176
	 */
	public final void setBusinessInfo176(final String pBusinessInfo176) {
		this.businessInfo176 = pBusinessInfo176;
	}

	/**
	 * 業務管理項目177を取得する.
	 * @return 業務管理項目177
	 */
	public final String getBusinessInfo177() {
		return this.businessInfo177;
	}
	/**
	 * 業務管理項目177を設定する.
	 * @param pBusinessInfo177 業務管理項目177
	 */
	public final void setBusinessInfo177(final String pBusinessInfo177) {
		this.businessInfo177 = pBusinessInfo177;
	}

	/**
	 * 業務管理項目178を取得する.
	 * @return 業務管理項目178
	 */
	public final String getBusinessInfo178() {
		return this.businessInfo178;
	}
	/**
	 * 業務管理項目178を設定する.
	 * @param pBusinessInfo178 業務管理項目178
	 */
	public final void setBusinessInfo178(final String pBusinessInfo178) {
		this.businessInfo178 = pBusinessInfo178;
	}

	/**
	 * 業務管理項目179を取得する.
	 * @return 業務管理項目179
	 */
	public final String getBusinessInfo179() {
		return this.businessInfo179;
	}
	/**
	 * 業務管理項目179を設定する.
	 * @param pBusinessInfo179 業務管理項目179
	 */
	public final void setBusinessInfo179(final String pBusinessInfo179) {
		this.businessInfo179 = pBusinessInfo179;
	}

	/**
	 * 業務管理項目180を取得する.
	 * @return 業務管理項目180
	 */
	public final String getBusinessInfo180() {
		return this.businessInfo180;
	}
	/**
	 * 業務管理項目180を設定する.
	 * @param pBusinessInfo180 業務管理項目180
	 */
	public final void setBusinessInfo180(final String pBusinessInfo180) {
		this.businessInfo180 = pBusinessInfo180;
	}

	/**
	 * 業務管理項目181を取得する.
	 * @return 業務管理項目181
	 */
	public final String getBusinessInfo181() {
		return this.businessInfo181;
	}
	/**
	 * 業務管理項目181を設定する.
	 * @param pBusinessInfo181 業務管理項目181
	 */
	public final void setBusinessInfo181(final String pBusinessInfo181) {
		this.businessInfo181 = pBusinessInfo181;
	}

	/**
	 * 業務管理項目182を取得する.
	 * @return 業務管理項目182
	 */
	public final String getBusinessInfo182() {
		return this.businessInfo182;
	}
	/**
	 * 業務管理項目182を設定する.
	 * @param pBusinessInfo182 業務管理項目182
	 */
	public final void setBusinessInfo182(final String pBusinessInfo182) {
		this.businessInfo182 = pBusinessInfo182;
	}

	/**
	 * 業務管理項目183を取得する.
	 * @return 業務管理項目183
	 */
	public final String getBusinessInfo183() {
		return this.businessInfo183;
	}
	/**
	 * 業務管理項目183を設定する.
	 * @param pBusinessInfo183 業務管理項目183
	 */
	public final void setBusinessInfo183(final String pBusinessInfo183) {
		this.businessInfo183 = pBusinessInfo183;
	}

	/**
	 * 業務管理項目184を取得する.
	 * @return 業務管理項目184
	 */
	public final String getBusinessInfo184() {
		return this.businessInfo184;
	}
	/**
	 * 業務管理項目184を設定する.
	 * @param pBusinessInfo184 業務管理項目184
	 */
	public final void setBusinessInfo184(final String pBusinessInfo184) {
		this.businessInfo184 = pBusinessInfo184;
	}

	/**
	 * 業務管理項目185を取得する.
	 * @return 業務管理項目185
	 */
	public final String getBusinessInfo185() {
		return this.businessInfo185;
	}
	/**
	 * 業務管理項目185を設定する.
	 * @param pBusinessInfo185 業務管理項目185
	 */
	public final void setBusinessInfo185(final String pBusinessInfo185) {
		this.businessInfo185 = pBusinessInfo185;
	}

	/**
	 * 業務管理項目186を取得する.
	 * @return 業務管理項目186
	 */
	public final String getBusinessInfo186() {
		return this.businessInfo186;
	}
	/**
	 * 業務管理項目186を設定する.
	 * @param pBusinessInfo186 業務管理項目186
	 */
	public final void setBusinessInfo186(final String pBusinessInfo186) {
		this.businessInfo186 = pBusinessInfo186;
	}

	/**
	 * 業務管理項目187を取得する.
	 * @return 業務管理項目187
	 */
	public final String getBusinessInfo187() {
		return this.businessInfo187;
	}
	/**
	 * 業務管理項目187を設定する.
	 * @param pBusinessInfo187 業務管理項目187
	 */
	public final void setBusinessInfo187(final String pBusinessInfo187) {
		this.businessInfo187 = pBusinessInfo187;
	}

	/**
	 * 業務管理項目188を取得する.
	 * @return 業務管理項目188
	 */
	public final String getBusinessInfo188() {
		return this.businessInfo188;
	}
	/**
	 * 業務管理項目188を設定する.
	 * @param pBusinessInfo188 業務管理項目188
	 */
	public final void setBusinessInfo188(final String pBusinessInfo188) {
		this.businessInfo188 = pBusinessInfo188;
	}

	/**
	 * 業務管理項目189を取得する.
	 * @return 業務管理項目189
	 */
	public final String getBusinessInfo189() {
		return this.businessInfo189;
	}
	/**
	 * 業務管理項目189を設定する.
	 * @param pBusinessInfo189 業務管理項目189
	 */
	public final void setBusinessInfo189(final String pBusinessInfo189) {
		this.businessInfo189 = pBusinessInfo189;
	}

	/**
	 * 業務管理項目190を取得する.
	 * @return 業務管理項目190
	 */
	public final String getBusinessInfo190() {
		return this.businessInfo190;
	}
	/**
	 * 業務管理項目190を設定する.
	 * @param pBusinessInfo190 業務管理項目190
	 */
	public final void setBusinessInfo190(final String pBusinessInfo190) {
		this.businessInfo190 = pBusinessInfo190;
	}

	/**
	 * 業務管理項目191を取得する.
	 * @return 業務管理項目191
	 */
	public final String getBusinessInfo191() {
		return this.businessInfo191;
	}
	/**
	 * 業務管理項目191を設定する.
	 * @param pBusinessInfo191 業務管理項目191
	 */
	public final void setBusinessInfo191(final String pBusinessInfo191) {
		this.businessInfo191 = pBusinessInfo191;
	}

	/**
	 * 業務管理項目192を取得する.
	 * @return 業務管理項目192
	 */
	public final String getBusinessInfo192() {
		return this.businessInfo192;
	}
	/**
	 * 業務管理項目192を設定する.
	 * @param pBusinessInfo192 業務管理項目192
	 */
	public final void setBusinessInfo192(final String pBusinessInfo192) {
		this.businessInfo192 = pBusinessInfo192;
	}

	/**
	 * 業務管理項目193を取得する.
	 * @return 業務管理項目193
	 */
	public final String getBusinessInfo193() {
		return this.businessInfo193;
	}
	/**
	 * 業務管理項目193を設定する.
	 * @param pBusinessInfo193 業務管理項目193
	 */
	public final void setBusinessInfo193(final String pBusinessInfo193) {
		this.businessInfo193 = pBusinessInfo193;
	}

	/**
	 * 業務管理項目194を取得する.
	 * @return 業務管理項目194
	 */
	public final String getBusinessInfo194() {
		return this.businessInfo194;
	}
	/**
	 * 業務管理項目194を設定する.
	 * @param pBusinessInfo194 業務管理項目194
	 */
	public final void setBusinessInfo194(final String pBusinessInfo194) {
		this.businessInfo194 = pBusinessInfo194;
	}

	/**
	 * 業務管理項目195を取得する.
	 * @return 業務管理項目195
	 */
	public final String getBusinessInfo195() {
		return this.businessInfo195;
	}
	/**
	 * 業務管理項目195を設定する.
	 * @param pBusinessInfo195 業務管理項目195
	 */
	public final void setBusinessInfo195(final String pBusinessInfo195) {
		this.businessInfo195 = pBusinessInfo195;
	}

	/**
	 * 業務管理項目196を取得する.
	 * @return 業務管理項目196
	 */
	public final String getBusinessInfo196() {
		return this.businessInfo196;
	}
	/**
	 * 業務管理項目196を設定する.
	 * @param pBusinessInfo196 業務管理項目196
	 */
	public final void setBusinessInfo196(final String pBusinessInfo196) {
		this.businessInfo196 = pBusinessInfo196;
	}

	/**
	 * 業務管理項目197を取得する.
	 * @return 業務管理項目197
	 */
	public final String getBusinessInfo197() {
		return this.businessInfo197;
	}
	/**
	 * 業務管理項目197を設定する.
	 * @param pBusinessInfo197 業務管理項目197
	 */
	public final void setBusinessInfo197(final String pBusinessInfo197) {
		this.businessInfo197 = pBusinessInfo197;
	}

	/**
	 * 業務管理項目198を取得する.
	 * @return 業務管理項目198
	 */
	public final String getBusinessInfo198() {
		return this.businessInfo198;
	}
	/**
	 * 業務管理項目198を設定する.
	 * @param pBusinessInfo198 業務管理項目198
	 */
	public final void setBusinessInfo198(final String pBusinessInfo198) {
		this.businessInfo198 = pBusinessInfo198;
	}

	/**
	 * 業務管理項目199を取得する.
	 * @return 業務管理項目199
	 */
	public final String getBusinessInfo199() {
		return this.businessInfo199;
	}
	/**
	 * 業務管理項目199を設定する.
	 * @param pBusinessInfo199 業務管理項目199
	 */
	public final void setBusinessInfo199(final String pBusinessInfo199) {
		this.businessInfo199 = pBusinessInfo199;
	}

	/**
	 * 業務管理項目200を取得する.
	 * @return 業務管理項目200
	 */
	public final String getBusinessInfo200() {
		return this.businessInfo200;
	}
	/**
	 * 業務管理項目200を設定する.
	 * @param pBusinessInfo200 業務管理項目200
	 */
	public final void setBusinessInfo200(final String pBusinessInfo200) {
		this.businessInfo200 = pBusinessInfo200;
	}

	/**
	 * 業務管理項目201を取得する.
	 * @return 業務管理項目201
	 */
	public final String getBusinessInfo201() {
		return this.businessInfo201;
	}
	/**
	 * 業務管理項目201を設定する.
	 * @param pBusinessInfo201 業務管理項目201
	 */
	public final void setBusinessInfo201(final String pBusinessInfo201) {
		this.businessInfo201 = pBusinessInfo201;
	}

	/**
	 * 業務管理項目202を取得する.
	 * @return 業務管理項目202
	 */
	public final String getBusinessInfo202() {
		return this.businessInfo202;
	}
	/**
	 * 業務管理項目202を設定する.
	 * @param pBusinessInfo202 業務管理項目202
	 */
	public final void setBusinessInfo202(final String pBusinessInfo202) {
		this.businessInfo202 = pBusinessInfo202;
	}

	/**
	 * 業務管理項目203を取得する.
	 * @return 業務管理項目203
	 */
	public final String getBusinessInfo203() {
		return this.businessInfo203;
	}
	/**
	 * 業務管理項目203を設定する.
	 * @param pBusinessInfo203 業務管理項目203
	 */
	public final void setBusinessInfo203(final String pBusinessInfo203) {
		this.businessInfo203 = pBusinessInfo203;
	}

	/**
	 * 業務管理項目204を取得する.
	 * @return 業務管理項目204
	 */
	public final String getBusinessInfo204() {
		return this.businessInfo204;
	}
	/**
	 * 業務管理項目204を設定する.
	 * @param pBusinessInfo204 業務管理項目204
	 */
	public final void setBusinessInfo204(final String pBusinessInfo204) {
		this.businessInfo204 = pBusinessInfo204;
	}

	/**
	 * 業務管理項目205を取得する.
	 * @return 業務管理項目205
	 */
	public final String getBusinessInfo205() {
		return this.businessInfo205;
	}
	/**
	 * 業務管理項目205を設定する.
	 * @param pBusinessInfo205 業務管理項目205
	 */
	public final void setBusinessInfo205(final String pBusinessInfo205) {
		this.businessInfo205 = pBusinessInfo205;
	}

	/**
	 * 業務管理項目206を取得する.
	 * @return 業務管理項目206
	 */
	public final String getBusinessInfo206() {
		return this.businessInfo206;
	}
	/**
	 * 業務管理項目206を設定する.
	 * @param pBusinessInfo206 業務管理項目206
	 */
	public final void setBusinessInfo206(final String pBusinessInfo206) {
		this.businessInfo206 = pBusinessInfo206;
	}

	/**
	 * 業務管理項目207を取得する.
	 * @return 業務管理項目207
	 */
	public final String getBusinessInfo207() {
		return this.businessInfo207;
	}
	/**
	 * 業務管理項目207を設定する.
	 * @param pBusinessInfo207 業務管理項目207
	 */
	public final void setBusinessInfo207(final String pBusinessInfo207) {
		this.businessInfo207 = pBusinessInfo207;
	}

	/**
	 * 業務管理項目208を取得する.
	 * @return 業務管理項目208
	 */
	public final String getBusinessInfo208() {
		return this.businessInfo208;
	}
	/**
	 * 業務管理項目208を設定する.
	 * @param pBusinessInfo208 業務管理項目208
	 */
	public final void setBusinessInfo208(final String pBusinessInfo208) {
		this.businessInfo208 = pBusinessInfo208;
	}

	/**
	 * 業務管理項目209を取得する.
	 * @return 業務管理項目209
	 */
	public final String getBusinessInfo209() {
		return this.businessInfo209;
	}
	/**
	 * 業務管理項目209を設定する.
	 * @param pBusinessInfo209 業務管理項目209
	 */
	public final void setBusinessInfo209(final String pBusinessInfo209) {
		this.businessInfo209 = pBusinessInfo209;
	}

	/**
	 * 業務管理項目210を取得する.
	 * @return 業務管理項目210
	 */
	public final String getBusinessInfo210() {
		return this.businessInfo210;
	}
	/**
	 * 業務管理項目210を設定する.
	 * @param pBusinessInfo210 業務管理項目210
	 */
	public final void setBusinessInfo210(final String pBusinessInfo210) {
		this.businessInfo210 = pBusinessInfo210;
	}

	/**
	 * 業務管理項目211を取得する.
	 * @return 業務管理項目211
	 */
	public final String getBusinessInfo211() {
		return this.businessInfo211;
	}
	/**
	 * 業務管理項目211を設定する.
	 * @param pBusinessInfo211 業務管理項目211
	 */
	public final void setBusinessInfo211(final String pBusinessInfo211) {
		this.businessInfo211 = pBusinessInfo211;
	}

	/**
	 * 業務管理項目212を取得する.
	 * @return 業務管理項目212
	 */
	public final String getBusinessInfo212() {
		return this.businessInfo212;
	}
	/**
	 * 業務管理項目212を設定する.
	 * @param pBusinessInfo212 業務管理項目212
	 */
	public final void setBusinessInfo212(final String pBusinessInfo212) {
		this.businessInfo212 = pBusinessInfo212;
	}

	/**
	 * 業務管理項目213を取得する.
	 * @return 業務管理項目213
	 */
	public final String getBusinessInfo213() {
		return this.businessInfo213;
	}
	/**
	 * 業務管理項目213を設定する.
	 * @param pBusinessInfo213 業務管理項目213
	 */
	public final void setBusinessInfo213(final String pBusinessInfo213) {
		this.businessInfo213 = pBusinessInfo213;
	}

	/**
	 * 業務管理項目214を取得する.
	 * @return 業務管理項目214
	 */
	public final String getBusinessInfo214() {
		return this.businessInfo214;
	}
	/**
	 * 業務管理項目214を設定する.
	 * @param pBusinessInfo214 業務管理項目214
	 */
	public final void setBusinessInfo214(final String pBusinessInfo214) {
		this.businessInfo214 = pBusinessInfo214;
	}

	/**
	 * 業務管理項目215を取得する.
	 * @return 業務管理項目215
	 */
	public final String getBusinessInfo215() {
		return this.businessInfo215;
	}
	/**
	 * 業務管理項目215を設定する.
	 * @param pBusinessInfo215 業務管理項目215
	 */
	public final void setBusinessInfo215(final String pBusinessInfo215) {
		this.businessInfo215 = pBusinessInfo215;
	}

	/**
	 * 業務管理項目216を取得する.
	 * @return 業務管理項目216
	 */
	public final String getBusinessInfo216() {
		return this.businessInfo216;
	}
	/**
	 * 業務管理項目216を設定する.
	 * @param pBusinessInfo216 業務管理項目216
	 */
	public final void setBusinessInfo216(final String pBusinessInfo216) {
		this.businessInfo216 = pBusinessInfo216;
	}

	/**
	 * 業務管理項目217を取得する.
	 * @return 業務管理項目217
	 */
	public final String getBusinessInfo217() {
		return this.businessInfo217;
	}
	/**
	 * 業務管理項目217を設定する.
	 * @param pBusinessInfo217 業務管理項目217
	 */
	public final void setBusinessInfo217(final String pBusinessInfo217) {
		this.businessInfo217 = pBusinessInfo217;
	}

	/**
	 * 業務管理項目218を取得する.
	 * @return 業務管理項目218
	 */
	public final String getBusinessInfo218() {
		return this.businessInfo218;
	}
	/**
	 * 業務管理項目218を設定する.
	 * @param pBusinessInfo218 業務管理項目218
	 */
	public final void setBusinessInfo218(final String pBusinessInfo218) {
		this.businessInfo218 = pBusinessInfo218;
	}

	/**
	 * 業務管理項目219を取得する.
	 * @return 業務管理項目219
	 */
	public final String getBusinessInfo219() {
		return this.businessInfo219;
	}
	/**
	 * 業務管理項目219を設定する.
	 * @param pBusinessInfo219 業務管理項目219
	 */
	public final void setBusinessInfo219(final String pBusinessInfo219) {
		this.businessInfo219 = pBusinessInfo219;
	}

	/**
	 * 業務管理項目220を取得する.
	 * @return 業務管理項目220
	 */
	public final String getBusinessInfo220() {
		return this.businessInfo220;
	}
	/**
	 * 業務管理項目220を設定する.
	 * @param pBusinessInfo220 業務管理項目220
	 */
	public final void setBusinessInfo220(final String pBusinessInfo220) {
		this.businessInfo220 = pBusinessInfo220;
	}

	/**
	 * 業務管理項目221を取得する.
	 * @return 業務管理項目221
	 */
	public final String getBusinessInfo221() {
		return this.businessInfo221;
	}
	/**
	 * 業務管理項目221を設定する.
	 * @param pBusinessInfo221 業務管理項目221
	 */
	public final void setBusinessInfo221(final String pBusinessInfo221) {
		this.businessInfo221 = pBusinessInfo221;
	}

	/**
	 * 業務管理項目222を取得する.
	 * @return 業務管理項目222
	 */
	public final String getBusinessInfo222() {
		return this.businessInfo222;
	}
	/**
	 * 業務管理項目222を設定する.
	 * @param pBusinessInfo222 業務管理項目222
	 */
	public final void setBusinessInfo222(final String pBusinessInfo222) {
		this.businessInfo222 = pBusinessInfo222;
	}

	/**
	 * 業務管理項目223を取得する.
	 * @return 業務管理項目223
	 */
	public final String getBusinessInfo223() {
		return this.businessInfo223;
	}
	/**
	 * 業務管理項目223を設定する.
	 * @param pBusinessInfo223 業務管理項目223
	 */
	public final void setBusinessInfo223(final String pBusinessInfo223) {
		this.businessInfo223 = pBusinessInfo223;
	}

	/**
	 * 業務管理項目224を取得する.
	 * @return 業務管理項目224
	 */
	public final String getBusinessInfo224() {
		return this.businessInfo224;
	}
	/**
	 * 業務管理項目224を設定する.
	 * @param pBusinessInfo224 業務管理項目224
	 */
	public final void setBusinessInfo224(final String pBusinessInfo224) {
		this.businessInfo224 = pBusinessInfo224;
	}

	/**
	 * 業務管理項目225を取得する.
	 * @return 業務管理項目225
	 */
	public final String getBusinessInfo225() {
		return this.businessInfo225;
	}
	/**
	 * 業務管理項目225を設定する.
	 * @param pBusinessInfo225 業務管理項目225
	 */
	public final void setBusinessInfo225(final String pBusinessInfo225) {
		this.businessInfo225 = pBusinessInfo225;
	}

	/**
	 * 業務管理項目226を取得する.
	 * @return 業務管理項目226
	 */
	public final String getBusinessInfo226() {
		return this.businessInfo226;
	}
	/**
	 * 業務管理項目226を設定する.
	 * @param pBusinessInfo226 業務管理項目226
	 */
	public final void setBusinessInfo226(final String pBusinessInfo226) {
		this.businessInfo226 = pBusinessInfo226;
	}

	/**
	 * 業務管理項目227を取得する.
	 * @return 業務管理項目227
	 */
	public final String getBusinessInfo227() {
		return this.businessInfo227;
	}
	/**
	 * 業務管理項目227を設定する.
	 * @param pBusinessInfo227 業務管理項目227
	 */
	public final void setBusinessInfo227(final String pBusinessInfo227) {
		this.businessInfo227 = pBusinessInfo227;
	}

	/**
	 * 業務管理項目228を取得する.
	 * @return 業務管理項目228
	 */
	public final String getBusinessInfo228() {
		return this.businessInfo228;
	}
	/**
	 * 業務管理項目228を設定する.
	 * @param pBusinessInfo228 業務管理項目228
	 */
	public final void setBusinessInfo228(final String pBusinessInfo228) {
		this.businessInfo228 = pBusinessInfo228;
	}

	/**
	 * 業務管理項目229を取得する.
	 * @return 業務管理項目229
	 */
	public final String getBusinessInfo229() {
		return this.businessInfo229;
	}
	/**
	 * 業務管理項目229を設定する.
	 * @param pBusinessInfo229 業務管理項目229
	 */
	public final void setBusinessInfo229(final String pBusinessInfo229) {
		this.businessInfo229 = pBusinessInfo229;
	}

	/**
	 * 業務管理項目230を取得する.
	 * @return 業務管理項目230
	 */
	public final String getBusinessInfo230() {
		return this.businessInfo230;
	}
	/**
	 * 業務管理項目230を設定する.
	 * @param pBusinessInfo230 業務管理項目230
	 */
	public final void setBusinessInfo230(final String pBusinessInfo230) {
		this.businessInfo230 = pBusinessInfo230;
	}

	/**
	 * 業務管理項目231を取得する.
	 * @return 業務管理項目231
	 */
	public final String getBusinessInfo231() {
		return this.businessInfo231;
	}
	/**
	 * 業務管理項目231を設定する.
	 * @param pBusinessInfo231 業務管理項目231
	 */
	public final void setBusinessInfo231(final String pBusinessInfo231) {
		this.businessInfo231 = pBusinessInfo231;
	}

	/**
	 * 業務管理項目232を取得する.
	 * @return 業務管理項目232
	 */
	public final String getBusinessInfo232() {
		return this.businessInfo232;
	}
	/**
	 * 業務管理項目232を設定する.
	 * @param pBusinessInfo232 業務管理項目232
	 */
	public final void setBusinessInfo232(final String pBusinessInfo232) {
		this.businessInfo232 = pBusinessInfo232;
	}

	/**
	 * 業務管理項目233を取得する.
	 * @return 業務管理項目233
	 */
	public final String getBusinessInfo233() {
		return this.businessInfo233;
	}
	/**
	 * 業務管理項目233を設定する.
	 * @param pBusinessInfo233 業務管理項目233
	 */
	public final void setBusinessInfo233(final String pBusinessInfo233) {
		this.businessInfo233 = pBusinessInfo233;
	}

	/**
	 * 業務管理項目234を取得する.
	 * @return 業務管理項目234
	 */
	public final String getBusinessInfo234() {
		return this.businessInfo234;
	}
	/**
	 * 業務管理項目234を設定する.
	 * @param pBusinessInfo234 業務管理項目234
	 */
	public final void setBusinessInfo234(final String pBusinessInfo234) {
		this.businessInfo234 = pBusinessInfo234;
	}

	/**
	 * 業務管理項目235を取得する.
	 * @return 業務管理項目235
	 */
	public final String getBusinessInfo235() {
		return this.businessInfo235;
	}
	/**
	 * 業務管理項目235を設定する.
	 * @param pBusinessInfo235 業務管理項目235
	 */
	public final void setBusinessInfo235(final String pBusinessInfo235) {
		this.businessInfo235 = pBusinessInfo235;
	}

	/**
	 * 業務管理項目236を取得する.
	 * @return 業務管理項目236
	 */
	public final String getBusinessInfo236() {
		return this.businessInfo236;
	}
	/**
	 * 業務管理項目236を設定する.
	 * @param pBusinessInfo236 業務管理項目236
	 */
	public final void setBusinessInfo236(final String pBusinessInfo236) {
		this.businessInfo236 = pBusinessInfo236;
	}

	/**
	 * 業務管理項目237を取得する.
	 * @return 業務管理項目237
	 */
	public final String getBusinessInfo237() {
		return this.businessInfo237;
	}
	/**
	 * 業務管理項目237を設定する.
	 * @param pBusinessInfo237 業務管理項目237
	 */
	public final void setBusinessInfo237(final String pBusinessInfo237) {
		this.businessInfo237 = pBusinessInfo237;
	}

	/**
	 * 業務管理項目238を取得する.
	 * @return 業務管理項目238
	 */
	public final String getBusinessInfo238() {
		return this.businessInfo238;
	}
	/**
	 * 業務管理項目238を設定する.
	 * @param pBusinessInfo238 業務管理項目238
	 */
	public final void setBusinessInfo238(final String pBusinessInfo238) {
		this.businessInfo238 = pBusinessInfo238;
	}

	/**
	 * 業務管理項目239を取得する.
	 * @return 業務管理項目239
	 */
	public final String getBusinessInfo239() {
		return this.businessInfo239;
	}
	/**
	 * 業務管理項目239を設定する.
	 * @param pBusinessInfo239 業務管理項目239
	 */
	public final void setBusinessInfo239(final String pBusinessInfo239) {
		this.businessInfo239 = pBusinessInfo239;
	}

	/**
	 * 業務管理項目240を取得する.
	 * @return 業務管理項目240
	 */
	public final String getBusinessInfo240() {
		return this.businessInfo240;
	}
	/**
	 * 業務管理項目240を設定する.
	 * @param pBusinessInfo240 業務管理項目240
	 */
	public final void setBusinessInfo240(final String pBusinessInfo240) {
		this.businessInfo240 = pBusinessInfo240;
	}

	/**
	 * 業務管理項目241を取得する.
	 * @return 業務管理項目241
	 */
	public final String getBusinessInfo241() {
		return this.businessInfo241;
	}
	/**
	 * 業務管理項目241を設定する.
	 * @param pBusinessInfo241 業務管理項目241
	 */
	public final void setBusinessInfo241(final String pBusinessInfo241) {
		this.businessInfo241 = pBusinessInfo241;
	}

	/**
	 * 業務管理項目242を取得する.
	 * @return 業務管理項目242
	 */
	public final String getBusinessInfo242() {
		return this.businessInfo242;
	}
	/**
	 * 業務管理項目242を設定する.
	 * @param pBusinessInfo242 業務管理項目242
	 */
	public final void setBusinessInfo242(final String pBusinessInfo242) {
		this.businessInfo242 = pBusinessInfo242;
	}

	/**
	 * 業務管理項目243を取得する.
	 * @return 業務管理項目243
	 */
	public final String getBusinessInfo243() {
		return this.businessInfo243;
	}
	/**
	 * 業務管理項目243を設定する.
	 * @param pBusinessInfo243 業務管理項目243
	 */
	public final void setBusinessInfo243(final String pBusinessInfo243) {
		this.businessInfo243 = pBusinessInfo243;
	}

	/**
	 * 業務管理項目244を取得する.
	 * @return 業務管理項目244
	 */
	public final String getBusinessInfo244() {
		return this.businessInfo244;
	}
	/**
	 * 業務管理項目244を設定する.
	 * @param pBusinessInfo244 業務管理項目244
	 */
	public final void setBusinessInfo244(final String pBusinessInfo244) {
		this.businessInfo244 = pBusinessInfo244;
	}

	/**
	 * 業務管理項目245を取得する.
	 * @return 業務管理項目245
	 */
	public final String getBusinessInfo245() {
		return this.businessInfo245;
	}
	/**
	 * 業務管理項目245を設定する.
	 * @param pBusinessInfo245 業務管理項目245
	 */
	public final void setBusinessInfo245(final String pBusinessInfo245) {
		this.businessInfo245 = pBusinessInfo245;
	}

	/**
	 * 業務管理項目246を取得する.
	 * @return 業務管理項目246
	 */
	public final String getBusinessInfo246() {
		return this.businessInfo246;
	}
	/**
	 * 業務管理項目246を設定する.
	 * @param pBusinessInfo246 業務管理項目246
	 */
	public final void setBusinessInfo246(final String pBusinessInfo246) {
		this.businessInfo246 = pBusinessInfo246;
	}

	/**
	 * 業務管理項目247を取得する.
	 * @return 業務管理項目247
	 */
	public final String getBusinessInfo247() {
		return this.businessInfo247;
	}
	/**
	 * 業務管理項目247を設定する.
	 * @param pBusinessInfo247 業務管理項目247
	 */
	public final void setBusinessInfo247(final String pBusinessInfo247) {
		this.businessInfo247 = pBusinessInfo247;
	}

	/**
	 * 業務管理項目248を取得する.
	 * @return 業務管理項目248
	 */
	public final String getBusinessInfo248() {
		return this.businessInfo248;
	}
	/**
	 * 業務管理項目248を設定する.
	 * @param pBusinessInfo248 業務管理項目248
	 */
	public final void setBusinessInfo248(final String pBusinessInfo248) {
		this.businessInfo248 = pBusinessInfo248;
	}

	/**
	 * 業務管理項目249を取得する.
	 * @return 業務管理項目249
	 */
	public final String getBusinessInfo249() {
		return this.businessInfo249;
	}
	/**
	 * 業務管理項目249を設定する.
	 * @param pBusinessInfo249 業務管理項目249
	 */
	public final void setBusinessInfo249(final String pBusinessInfo249) {
		this.businessInfo249 = pBusinessInfo249;
	}

	/**
	 * 業務管理項目250を取得する.
	 * @return 業務管理項目250
	 */
	public final String getBusinessInfo250() {
		return this.businessInfo250;
	}
	/**
	 * 業務管理項目250を設定する.
	 * @param pBusinessInfo250 業務管理項目250
	 */
	public final void setBusinessInfo250(final String pBusinessInfo250) {
		this.businessInfo250 = pBusinessInfo250;
	}

	/**
	 * 文書IDを取得する．
	 * @return 文書ID
	 */
	public Long getDocId() {
		return docId;
	}
	/**
	 * 文書IDを設定する．
	 * @param docId 文書ID
	 */
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	/**
	 * メジャーバージョンを取得する．
	 * @return メジャーバージョン
	 */
	public Long getMajorVersion() {
		return majorVersion;
	}
	/**
	 * メジャーバージョンを設定する．
	 * @param majorVersion メジャーバージョン
	 */
	public void setMajorVersion(Long majorVersion) {
		this.majorVersion = majorVersion;
	}
	/**
	 * マイナーバージョンを取得する．
	 * @return マイナーバージョン
	 */
	public Long getMinorVersion() {
		return minorVersion;
	}
	/**
	 * マイナーバージョンを設定する．
	 * @param minorVersion マイナーバージョン
	 */
	public void setMinorVersion(Long minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * 業務アクティビティ状態を取得する.
	 * @return 業務アクティビティ状態
	 */
	public final String getBuinsessActivityStatus() {
		return buinsessActivityStatus;
	}
	/**
	 * 業務アクティビティ状態を設定する.
	 * @param pBuinsessActivityStatus 設定する業務アクティビティ状態

	 */
	public final void setBuinsessActivityStatus(final String pBuinsessActivityStatus) {
		this.buinsessActivityStatus = pBuinsessActivityStatus;
	}
	/**
	 * @return searchConditionList
	 */
	public final List<WfSearchCondition<?>> getSearchConditionList() {
		return searchConditionList;
	}
	/**
	 * @param pSearchConditionList セットする searchConditionList
	 */
	public final void setSearchConditionList(final List<WfSearchCondition<?>> pSearchConditionList) {
		this.searchConditionList = pSearchConditionList;
	}
	/**
	 * @return sortOrderList
	 */
	public final List<WfSortOrder> getSortOrderList() {
		return sortOrderList;
	}
	/**
	 * @param pSortOrderList セットする sortOrderList
	 */
	public final void setSortOrderList(final List<WfSortOrder> pSortOrderList) {
		this.sortOrderList = pSortOrderList;
	}
	/**
	 * @return businessProcessStatusList
	 */
	public final List<String> getBusinessProcessStatusList() {
		return businessProcessStatusList;
	}
	/**
	 * @param pBusinessProcessStatusList セットする businessProcessStatusList
	 */
	public final void setBusinessProcessStatusList(final List<String> pBusinessProcessStatusList) {
		this.businessProcessStatusList = pBusinessProcessStatusList;
	}
	/**
	 * マージモードを取得する.
	 * @return isMerge マージ
	 */
	public final boolean isMerge() {
		return isMerge;
	}
	/**
	 * マージモードを設定する.
	 * @param pIsMerge セットする isMerge
	 */
	public final void setMerge(final boolean pIsMerge) {
		this.isMerge = pIsMerge;
	}
	/**
	 * @return rowNo
	 */
	public final Long getRowNo() {
		return rowNo;
	}
	/**
	 * @param pRowNo セットする rowNo
	 */
	public final void setRowNo(final Long pRowNo) {
		this.rowNo = pRowNo;
	}
	/**
	 * @return rowCount
	 */
	public final Long getRowCount() {
		return rowCount;
	}
	/**
	 * 添付ファイル全文検索フラグ.
	 * @param pRowCount セットする rowCount
	 */
	public final void setRowCount(final Long pRowCount) {
		this.rowCount = pRowCount;
	}
	/**
	 * 添付ファイル検索文字列.
	 * @param pAttachFileSearchCondition セットする attachFileSearchCondition
	 */
	public final void setAttachFileSearchCondition(final String pAttachFileSearchCondition) {
		this.attachFileSearchCondition = pAttachFileSearchCondition;
	}
	/**
	 * 添付ファイル検索文字列.
	 * @return attachFileSearchCondition
	 */
	public final String getAttachFileSearchCondition() {
		return attachFileSearchCondition;
	}

	/**
	 * @return joinParams
	 */
	public final String getJoinParams() {
		return joinParams;
	}
	/**
	 * @param pJoinParams セットする joinParams
	 */
	public final void setJoinParams(final String pJoinParams) {
		this.joinParams = pJoinParams;
	}
	/**
	 * @return joinTables
	 */
	public final String getJoinTables() {
		return joinTables;
	}
	/**
	 * @param pJoinTables セットする joinTables
	 */
	public final void setJoinTables(final String pJoinTables) {
		this.joinTables = pJoinTables;
	}
	/**
	 * @return joinWhere
	 */
	public final String getJoinWhere() {
		return joinWhere;
	}
	/**
	 * @param pJoinWhere セットする joinWhere
	 */
	public final void setJoinWhere(final String pJoinWhere) {
		this.joinWhere = pJoinWhere;
	}

	/**
	 * @return selectMode
	 */
	public final String getSelectMode() {
		return selectMode;
	}

	/**
	 * @param selectMode セットする selectMode
	 */
	public final void setSelectMode(String selectMode) {
		this.selectMode = selectMode;
	}

}
