package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import jp.co.dmm.customize.component.DmmCodeBook.CrpPrsTp;
import jp.co.dmm.customize.component.DmmCodeBook.TrdStsTp;
import jp.co.dmm.customize.endpoint.sp.SpBnkaccMstEntity;
import jp.co.dmm.customize.endpoint.sp.SplrMstEntity;
import jp.co.dmm.customize.endpoint.sp.ZipMstEntity;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 取引先一覧のリポジトリ
 */
@ApplicationScoped
public class Sp0010Repository extends BaseRepository {

	/** TODO 法人区分（漢字名） */
	private List<String> CORP_TYPE_NM = Arrays.asList(
		"株式会社","合同会社","合資会社","合名会社","地方公共団体","独立行政法人","特殊法人","金融公庫",
		"NPO法人","一般社団法人","一般財団法人","社会福祉法人","信用金庫","商工会",
		"公益財団法人","弁護士法人","税理士法人","社会保険労務士法人","社団法人","学校法人","特定非営利活動法人","特許業務法人","医療法人",
		// 2019/03/15 追加
		"有限会社");
	// 残："有限責任事業組合","協同組合"

	/** TODO 法人区分（カナ） */
	private List<String> CORP_TYPE_KN = Arrays.asList(
		"カブシキガイシャ","ゴウドウガイシャ","ゴウシガイシャ","ゴウメイガイシャ","チホウコウキョウダンタイ","ドクリツギョウセイホウジン","トクシュホウジン","キンユウコウコ",
		"ＮＰＯホウジン","NPOホウジン","イッパンシャダンホウジン","イッパンザイダンホウジン","シャカイフクシホウジン","シンヨウキンコ","ショウコウカイ",
		"コウエキザイダンホウジン","ベンゴシホウジン","ゼイリシホウジン","シャカイホケンロウムシホウジン","シャダンホウジン","ガッコウホウジン","トクテイヒエイリカツドウホウジン","トッキョギョウムホウジン","イリョウホウジン",
		// 2019/03/15 追加
		"ユウゲンガイシャ",
		"ｶﾌﾞｼｷｶﾞｲｼｬ","ｺﾞｳﾄﾞｳｶﾞｲｼｬ","ｺﾞｳｼｶﾞｲｼｬ","ｺﾞｳﾒｲｶﾞｲｼｬ","ﾁﾎｳｺｳｷｮｳﾀﾞﾝﾀｲ","ﾄﾞｸﾘﾂｷﾞｮｳｾｲﾎｳｼﾞﾝ","ﾄｸｼｭﾎｳｼﾞﾝ","ｷﾝﾕｳｺｳｺ",
		"NPOﾎｳｼﾞﾝ","ｲｯﾊﾟﾝｼｬﾀﾞﾝﾎｳｼﾞﾝ","ｲｯﾊﾟﾝｻﾞｲﾀﾞﾝﾎｳｼﾞﾝ","ｼｬｶｲﾌｸｼﾎｳｼﾞﾝ","ｼﾝﾖｳｷﾝｺ","ｼｮｳｺｳｶｲ",
		"ｺｳｴｷｻﾞｲﾀﾞﾝﾎｳｼﾞﾝ","ﾍﾞﾝｺﾞｼﾎｳｼﾞﾝ","ｾﾞｲﾘｼﾎｳｼﾞﾝ","ｼｬｶｲﾎｹﾝﾛｳﾑｼﾎｳｼﾞﾝ","ｼｬﾀﾞﾝﾎｳｼﾞﾝ","ｶﾞｯｺｳﾎｳｼﾞﾝ","ﾄｸﾃｲﾋｴｲﾘｶﾂﾄﾞｳﾎｳｼﾞﾝ","ﾄｯｷｮｷﾞｮｳﾑﾎｳｼﾞﾝ","ｲﾘｮｳﾎｳｼﾞﾝ",
		// 2019/03/15 追加
		"ﾕｳｹﾞﾝｶﾞｲｼｬ");
	// 残 :"ユウゲンセキニンジギョウクミアイ","キョウドウクミアイ"
	//

	/** TODO 法人区分（英字） */
	private List<String> CORP_TYPE_E = Arrays.asList(" Co."," Ltd."," Inc.","Corp.");

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0' order by B.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();
		newItems.add(new OptionItem("", "--"));

		for (MwmOptionItem item : results) {
			newItems.add(new OptionItem(item.getCode(), item.getLabel()));
		}

		return newItems;
	}

	/**
	 * 取引先一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Sp0010SearchRequest req) {

		StringBuilder sql = new StringBuilder("select count(*) from (" + getSql("SP0010_01"));

		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params);
		sql.append(")");

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 取引先一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<SplrMstEntity> select(Sp0010SearchRequest req, Sp0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder("select * from (" + getSql("SP0010_01"));

		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params);

		// ページング
		sql.append(") offset ? rows fetch first ? rows only");
		params.add(toStartPosition(req.pageNo, req.pageSize));
		params.add(req.pageSize);

		return select(SplrMstEntity.class, sql, params.toArray());
	}

	/**
	 * 取引先一覧検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Sp0010SearchRequest req, StringBuilder sql, List<Object> params) {

		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getCorporationCode());
		params.add(LoginInfo.get().getCorporationCode());
		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getCorporationCode());

		// 取引先コード制約
		if (!"00053".equals(LoginInfo.get().getCorporationCode())) {
			sql.append(" and (s.SPLR_CD like '20%' or s.SPLR_CD like '10%' or s.SPLR_CD like '0%')");
		}

		// 取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and s.SPLR_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.splrCd));
		}

		// 取引先名称（漢字）
		if (isNotEmpty(req.splrNmKj)) {
//			sql.append(" and UTL_I18N.TRANSLITERATE(UPPER(TO_MULTI_BYTE(s.SPLR_NM_KJ)),'kana_fwkatakana') like '%' || UTL_I18N.TRANSLITERATE(UPPER(TO_MULTI_BYTE(?)),'kana_fwkatakana') || '%' escape '~'");
//			params.add(req.splrNmKj);
			// -> レスポンスが悪いため元に戻す
			sql.append(" and s.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}

		// 取引先名称（カタカナ）
		if (isNotEmpty(req.splrNmKn)) {
			sql.append(" and to_single_width_kana(s.SPLR_NM_KN) like to_single_width_kana(?) escape '~'");
			params.add(escapeLikeBoth(req.splrNmKn));
		}

		// 取引先名称（英名）
		if (isNotEmpty(req.splrNmE)) {
			sql.append(" and s.SPLR_NM_E like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmE));
		}

		// 住所（都道府県）
		if (isNotEmpty(req.adrPrfCd)) {
			sql.append(" and s.ADR_PRF_CD = ? ");
			params.add(req.adrPrfCd);
		}

		// 法人・個人区分
		List<String> crpPrsList = new ArrayList<>();

		if (req.crpPrsTp1) crpPrsList.add(CrpPrsTp.CORPORATION);
		if (req.crpPrsTp2) crpPrsList.add(CrpPrsTp.PERSONAL);

		if (crpPrsList.size() != 0) {
			sql.append(" and " + toInListSql("S.CRP_PRS_TP", crpPrsList.size()));
			params.addAll(crpPrsList);
		}

		// 取引状況区分
		List<String> trdStsList = new ArrayList<>();

		if (req.trdStsTp1) trdStsList.add(TrdStsTp.BEFORE_USE);
		if (req.trdStsTp2) trdStsList.add(TrdStsTp.USING);
		if (req.trdStsTp3) trdStsList.add(TrdStsTp.STOP_USING);

		if (trdStsList.size() != 0) {
			sql.append(" and " + toInListSql("S.TRD_STS_TP", trdStsList.size()));
			params.addAll(trdStsList);
		}

		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		} else {
			sql.append(" order by S.SPLR_CD ");
		}
	}

	/**
	 * 取引先コードチェック
	 * @param req
	 * @return
	 */
	public String researchSplrCdCheck(Sp0010ResearchCheckRequest req) {

		String existsCompanyCds = "";

		//取引先名
		StringBuilder sql = new StringBuilder(getSql("SP0101_01"));
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		if ("00053".equals(req.companyCd)) {

			// 取引先コード
			if (isNotEmpty(req.splrCd)) {
				sql.append(" and sm.SPLR_CD = ? ");
				params.add(req.splrCd);
			}

			List<SplrMstEntity> results = select(SplrMstEntity.class, sql, params.toArray());

			if (results.size() != 0) {
				for (SplrMstEntity result : results) {
					if ("".equals(existsCompanyCds)) {
						existsCompanyCds += result.companyCd;
					} else {
						existsCompanyCds += "," + result.companyCd;
					}
				}
			}

		} else {

			// 会社コード
			if (isNotEmpty(req.companyCd)) {
				sql.append(" and sm.COMPANY_CD = ? ");
				params.add(req.companyCd);
			}

			// 取引先コード
			if (isNotEmpty(req.splrCd)) {
				sql.append(" and sm.SPLR_CD = ? ");
				params.add(req.splrCd);
			}

			Query query = em.createNativeQuery(sql.toString());
			putParams(query, params.toArray());

			if (query.getResultList().size() != 0) {
				existsCompanyCds += req.companyCd;
			}
		}

		return existsCompanyCds;
	}


	/**
	 * 取引先口座情報チェック
	 * @param req
	 * @return
	 */
	public boolean accountCheck(Sp0010AccountCheckRequest req) {

		//振込先口座マスタ
		StringBuilder sql = new StringBuilder("select count(*) from (" + getSql("SP0020_01"));
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		// 会社コード
		sql.append(" and pbm.COMPANY_CD = ? ");
		params.add(req.companyCd);

		if (req.payeeBnkaccCdList != null && !req.payeeBnkaccCdList.isEmpty()) {
			sql.append(" and " + toInListSql("PAYEE_BNKACC_CD", req.payeeBnkaccCdList.size()));
			params.addAll(req.payeeBnkaccCdList);
		}

		sql.append(")");

		return count(sql.toString(), params.toArray()) == 0;
	}

	/**
	 * DGHD会社取得
	 * @param corporationCode 会社コード
	 * @param localCode 言語コード
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCompany(String corporationCode, String localCode) {

		StringBuilder sql = new StringBuilder(getSql("SP0020_07"));
		final List<Object> params = new ArrayList<>();

		params.add(corporationCode);
		params.add(localCode);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());

		List<Object[]> results = query.getResultList();

		List<String> companyList = new ArrayList<String>();
		for (Object[] result : results) {
			companyList.add(result[0] + "|" + result[1]);
		}

		return companyList;
	}

	/**
	 * DGHD会社登録済み取得
	 * @param companyCd 会社コード
	 * @param adrPrfCd 郵便番号
	 * @return
	 */
	public String getDGHDCompanys(String splrCd) {

		StringBuilder sql = new StringBuilder(getSql("SP0020_08"));
		final List<Object> params = new ArrayList<>();
		params.add(splrCd);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		String companyList = "";
		for (Object[] result : results) {
			String companyCd = result[0].toString();
			if (isEmpty(companyList)) {
				companyList += companyCd;
			} else {
				companyList += "," + companyCd;
			}
		}
		return companyList;
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	public int getScreenProcessId(String corporationCode, String loginUserCorporationCode) {

		//画面プロセスID取得
		StringBuilder sql = new StringBuilder(getSql("SP0020_03"));
		final List<Object> params = new ArrayList<>();

		//HDユーザの場合
		if ("00053".equals(loginUserCorporationCode)) {
			params.add("201808220002");
			params.add("00001");
			params.add(loginUserCorporationCode);
		} else {
			params.add("201807121905");
			params.add("00001");
			params.add(corporationCode);
		}

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 住所取得
	 * @param companyCd 会社コード
	 * @param adrPrfCd 郵便番号
	 * @return
	 */
	public List<ZipMstEntity> getAddressInfo(String companyCd, String zipCd) {

		if (StringUtils.isEmpty(zipCd)) {
			return new ArrayList<ZipMstEntity>();
		}

		//TODO:暫定対応：会社コードを固定の「00020」を利用。後で見直す必要
		companyCd = "00020";

		//住所取得
		StringBuilder sql = new StringBuilder(getSql("SP0101_02"));
		final List<Object> params = new ArrayList<>();
		params.add(companyCd);
		params.add(zipCd);

		return select(ZipMstEntity.class, sql, params.toArray());
	}

	/**
	 * 名寄せチェック
	 * @param req
	 * @param res
	 * @return
	 */
	public List<SplrMstEntity> researchCheck(Sp0010ResearchCheckRequest req, Sp0010ResearchCheckResponse res) {

		//取引先名
		StringBuilder sql = new StringBuilder(getSql("SP0101_01"));
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		if (!"00053".equals(LoginInfo.get().getCorporationCode())){
			sql.append(" and sm.SPLR_CD like '20%' ");
		}

		sql.append(" and (");
		int searchItemCnt = 0;
		boolean accountFlag = false;

		// 振込先口座番号
		String bnkaccStr = "";
		if (req.bnkaccList != null && req.bnkaccList.size() != 0) {
			int index = 0;
			for (String bnkacc : req.bnkaccList) {
				if (index != 0) {
					bnkaccStr += " or ";
				}
				bnkaccStr += "((pbm.BNK_CD || '-' || pbm.BNKBRC_CD || '-' || pbm.BNKACC_NO) = ?)";
				params.add(bnkacc);
				index++;
			}
			sql.append("(" + bnkaccStr + ")  or ((1=2");
			accountFlag = true;

		} else {
			sql.append("((1=2");
		}

		// 取引先名称（漢字）
		if (isNotEmpty(req.splrNmKj)) {

			sql.append(" or ");

			// 名寄せ用の名称に変換
			String targetSplrNmKj = req.splrNmKj.toUpperCase().replaceAll(" ", "");

			for (String corpNm : CORP_TYPE_NM) {
				targetSplrNmKj = targetSplrNmKj.replaceAll(corpNm.toUpperCase(), "");
			}

			// 5文字以下の場合(完全一致）
			if (targetSplrNmKj.length() <= 5) {
				sql.append(" TO_MULTI_BYTE(sm.SPLR_NM_KJ) = TO_MULTI_BYTE(?) ");
				params.add(req.splrNmKj);

			// 5文字超は一部一致（4文字単位）
			} else {
				for (int i=0; i<targetSplrNmKj.length()-3; i++) {
					String replaceKnSql = "%' || TO_MULTI_BYTE('";
					for (int j=i; j<i+4; j++) {
						char ch = targetSplrNmKj.charAt(j);
						replaceKnSql += ch;
					}
					replaceKnSql += "') || '%";
					if (i != 0) {
						sql.append(" or");
					}
					sql.append(" TO_MULTI_BYTE(sm.SPLR_NM_KJ) like '" + replaceKnSql + "' escape '~'");
				}
			}

			searchItemCnt++;
		}

		// 取引先名称（カタカナ）
		if (isNotEmpty(req.splrNmKn)) {

			sql.append(" or ");

			// 名寄せ用の名称に変換
			String targetSplrNmKn = req.splrNmKn.toUpperCase();

			for (String corpNm : CORP_TYPE_KN) {
				targetSplrNmKn = targetSplrNmKn.replaceAll(corpNm.toUpperCase(), "");
			}

			// 5文字以下の場合(完全一致）
			if (targetSplrNmKn.length() <= 5) {
				sql.append(" sm.SPLR_NM_KN = ? ");
				params.add(req.splrNmKn);

			// 5文字超は一部一致（4文字単位）
			} else {
				for (int i=0; i<targetSplrNmKn.length()-3; i++) {
					String replaceKnSql = "%";
					for (int j=i; j<i+4; j++) {
						char ch = targetSplrNmKn.charAt(j);
						replaceKnSql += ch;
					}
					replaceKnSql += "%";
					if (i != 0) {
						sql.append(" or");
					}
					sql.append(" UPPER(sm.SPLR_NM_KN) like '" + replaceKnSql + "' escape '~'");
				}
			}

			searchItemCnt++;
		}

		if (isNotEmpty(req.splrNmE)) {

			sql.append(" or ");

			//名寄せ用の名称に変換
			String targetSplrNmE = req.splrNmE.toUpperCase();

			for (String corpNm : CORP_TYPE_E) {
				targetSplrNmE = targetSplrNmE.replaceAll(corpNm.toUpperCase(), "");
			}

			sql.append(" SM.SPLR_NM_E like ? escape '~'");
			params.add("%" + req.splrNmE + "%");
			searchItemCnt++;
		}

		// 住所（都道府県）
		if (isNotEmpty(req.adrPrfCd)) {

			if (searchItemCnt == 0) {
				sql.append(") or sm.ADR_PRF_CD = ?) ");
			} else {
				sql.append(") and sm.ADR_PRF_CD = ?) ");
			}

			params.add(req.adrPrfCd);
			searchItemCnt++;

		} else {
			sql.append(")) ");
		}

		sql.append(" )");

		// 入力ない場合は無視
		if (searchItemCnt == 0 && !accountFlag) {
			return new ArrayList<SplrMstEntity>();
		}

		return select(SplrMstEntity.class, sql, params.toArray());
	}

	/**
	 * 変更申請_取引先申請中件数
	 * @param req
	 * @return
	 */
	public int countSplr(Sp0010GetScreenProcessIdRequest req) {
		final String sql = getSql("SP0010_02");
		final Object[] args = {
				req.companyCd, req.splrCd
		};

		return count(sql, args);
	}

	/**
	 * 銀行口座名称取得
	 * @return 会社コード、銀行口座コードより取得
	 */
	public String getDefaultBnkacc(String companyCd, String bnkaccCd) {
		final String sql = getSql("SP0010_03");
		final Object[] args = {
				companyCd, bnkaccCd
		};

		List<SpBnkaccMstEntity> bnkaccList = select(SpBnkaccMstEntity.class, sql, args);
		if (bnkaccList != null && bnkaccList.size() > 0) {
			return bnkaccList.get(0).bnkaccNm;
		}

		return null;
	}

}
