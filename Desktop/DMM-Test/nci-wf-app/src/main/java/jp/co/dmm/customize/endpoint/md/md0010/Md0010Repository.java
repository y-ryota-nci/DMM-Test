package jp.co.dmm.customize.endpoint.md.md0010;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import jp.co.dmm.customize.component.DmmCodeBook.CrpPrsTp;
import jp.co.dmm.customize.component.DmmCodeBook.TrdStsTp;
import jp.co.dmm.customize.endpoint.md.MdExcelBook;
import jp.co.dmm.customize.endpoint.md.MdExcelOrgCrmEntity;
import jp.co.dmm.customize.endpoint.md.MdExcelRltPrtEntity;
import jp.co.dmm.customize.endpoint.md.MdExcelSplrAccEntity;
import jp.co.dmm.customize.endpoint.md.MdExcelSplrEntity;
import jp.co.dmm.customize.endpoint.md.MdMstCode;
import jp.co.dmm.customize.endpoint.md.MdSplrMstEntity;
import jp.co.dmm.customize.jpa.entity.mw.LndMst;
import jp.co.dmm.customize.jpa.entity.mw.OrgCrmInf;
import jp.co.dmm.customize.jpa.entity.mw.OrgCrmInfPK;
import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.RltPrtMst;
import jp.co.dmm.customize.jpa.entity.mw.RltPrtMstPK;
import jp.co.dmm.customize.jpa.entity.mw.SplrMst;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * MDM 取引先一覧のリポジトリ
 */
@ApplicationScoped
public class Md0010Repository extends BaseRepository {

	@Inject private SessionHolder sessionHolder;

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
	public int count(Md0010SearchRequest req) {
		StringBuilder sql = new StringBuilder("select count(*) from (" + getSql("MD0010_01"));

		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false, true);
		sql.append(")");

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 取引先一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Md0010SearchRequest req, Md0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder("select * from (" + getSql("MD0010_01"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true, true);

		// ページング
		sql.append(") offset ? rows fetch first ? rows only");
		params.add(toStartPosition(req.pageNo, req.pageSize));
		params.add(req.pageSize);


		return select(MdSplrMstEntity.class, sql, params.toArray());
	}

	/**
	 * 取引先一覧検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 * @param isSearchSplr 取引先検索か取引先口座検索か
	 */
	private void fillCondition(Md0010SearchRequest req, StringBuilder sql, List<Object> params, boolean isSort, boolean isSearchSplr) {
		if (isSearchSplr) {
			params.add(LoginInfo.get().getLocaleCode());
		}

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and s.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 取引先コード(事業部は20始まり固定)
		if (!"00053".equals(LoginInfo.get().getCorporationCode())){
			sql.append(" and (s.SPLR_CD like '20%' or s.SPLR_CD like '10%' or s.SPLR_CD like '0%') ");
		}
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
			sql.append(" and " + toInListSql("s.CRP_PRS_TP", crpPrsList.size()));
			params.addAll(crpPrsList);
		}

		// 取引状況区分
		List<String> trdStsList = new ArrayList<>();

		if (req.trdStsTp1) trdStsList.add(TrdStsTp.BEFORE_USE);
		if (req.trdStsTp2) trdStsList.add(TrdStsTp.USING);
		if (req.trdStsTp3) trdStsList.add(TrdStsTp.STOP_USING);

		if (trdStsList.size() != 0) {
			sql.append(" and " + toInListSql("s.TRD_STS_TP", trdStsList.size()));
			params.addAll(trdStsList);
		}

		// ソート
		if (isSort && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}
	}

	/**
	 * マスタチェック用のコート一覧取得
	 * @param book Excel Book
	 */
	public void getUploadMasterCdInfo (MdExcelBook book) {
		boolean isDGHD = eq("00053", sessionHolder.getLoginInfo().getCorporationCode());

		// 会社コード
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CORPORATION_CODE AS CODE_VALUE FROM WFM_CORPORATION WHERE DELETE_FLAG = ? ");
		List<Object> params = new ArrayList<>();
		params.add(CommonFlag.OFF);

		List<MdMstCode> results = select(MdMstCode.class, sql, params.toArray());
		book.existCompanyCodes = new HashSet<String>();
		for (MdMstCode code : results) {
			book.existCompanyCodes.add(code.codeValue);
		}

		// 取引先コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || SPLR_CD AS CODE_VALUE FROM SPLR_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MdMstCode.class, sql, params.toArray());
		book.existSplrCodes = new HashSet<String>();
		for (MdMstCode code : results) {
			book.existSplrCodes.add(code.codeValue);
		}

		// 部門コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || BUMON_CD AS CODE_VALUE FROM BUMON_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MdMstCode.class, sql, params.toArray());
		book.existBumonCodes = new HashSet<String>();
		for (MdMstCode code : results) {
			book.existBumonCodes.add(code.codeValue);
		}

		//振込先銀行口座コード(SuperStream)
		sql = new StringBuilder();
		sql.append("select DISTINCT COMPANY_CD || '_' || SPLR_CD || '_' || PAYEE_BNKACC_CD_SS as CODE_VALUE from PAYEE_BNKACC_MST where DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MdMstCode.class, sql, params.toArray());
		book.existBnkaccSSCodes = new HashSet<String>();
		for (MdMstCode code : results) {
			book.existBnkaccSSCodes.add(code.codeValue);
		}

		// 国コード
		book.existLndCds = select(LndMst.class, "select * from LND_MST where COMPANY_CD = ? and DLT_FG = '0' order by SORT_ORDER", new Object[] {CorporationCodes.DMM_COM})
				.stream().map(l -> l.getId().getLndCd())
				.collect(Collectors.toSet());
	}

	/**
	 * アップロードされたファイルをＤＢに登録
	 * @param book Excel Book
	 */
	public void uploadRegist (MdExcelBook book, WfUserRole userRole) {

		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 取引先
		StringBuilder selectSql = new StringBuilder(getSql("MD0010_02"));
		StringBuilder insertSql = new StringBuilder(getSql("MD0000_01"));
		StringBuilder deleteSql = new StringBuilder(getSql("MD0001_01"));

		// まずは削除
		for (MdExcelSplrEntity splr : book.sheetSplr.splrs) {
			if ("D".equals(splr.processType)) {
				List<Object> deleteParams = new ArrayList<>();
				deleteParams.add(splr.companyCd);
				deleteParams.add(splr.splrCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, deleteParams.toArray());
				query.executeUpdate();
			}
		}

		// INSERT or UPDATE分生成
		for (MdExcelSplrEntity splr : book.sheetSplr.splrs) {

			if ("A".equals(splr.processType) || "U".equals(splr.processType) || "C".equals(splr.processType)) {

				// 存在チェック
				List<SplrMst> splrMstList = select(SplrMst.class, selectSql, new Object[]{splr.companyCd,splr.splrCd});

				// 更新
				if (splrMstList.size() != 0) {
					SplrMst updateMst = splrMstList.get(0);

					// 取引先名称（漢字）
					updateMst.setSplrNmKj(splr.splrNmKj);
					// 取引先名称（半角ｶﾅ）
					updateMst.setSplrNmKn(splr.splrNmKn);
					// 取引先名称（略称）
					updateMst.setSplrNmS(splr.splrNmS);
					// 取引先名称（英名）
					updateMst.setSplrNmE(splr.splrNmE);
					// 法人・個人区分
					updateMst.setCrpPrsTp(splr.crpPrsTp);
					// 国内・海外区分
					updateMst.setDmsAbrTp(splr.dmsAbrTp);
					// 国名
					updateMst.setLndNm(splr.lndNm);
					// 法人番号
					updateMst.setCrpNo(splr.crpNo);
					// 郵便番号
					updateMst.setZipCd(splr.zipCd);
					// 住所（都道府県）コード
					updateMst.setAdrPrfCd(splr.adrPrfCd);
					// 住所（都道府県）
					updateMst.setAdrPrf(splr.adrPrf);
					// 住所（市区町村）
					updateMst.setAdr1(splr.adr1);
					// 住所（町名番地）
					updateMst.setAdr2(splr.adr2);
					// 住所（建物名）
					updateMst.setAdr3(splr.adr3);
					// 電話番号
					updateMst.setTelNo(splr.telNo);
					// FAX番号
					updateMst.setFaxNo(splr.faxNo);
					// 関係会社区分
					updateMst.setAffcmpTp(splr.affcmpTp);
					// 取引状況区分
					updateMst.setTrdStsTp(splr.trdStsTp);
					// 有効期間（開始）
					updateMst.setVdDtS(splr.vdDtS);
					// 有効期間（終了）
					updateMst.setVdDtE(splr.vdDtE);
					// 備考
					updateMst.setRmk(splr.rmk);

					// 部門コード
					updateMst.setBumonCd(splr.bumonCd);
					// 適格請求書発行事業者
					updateMst.setCmptEtrNo(splr.cmptEtrNo);
					// 生年月日
					updateMst.setBrthDt(splr.brthDt);
					// 国コード
					updateMst.setLndCd(splr.lndCd);
					// 最終判定区分
					updateMst.setLastJdgTp(splr.lastJdgTp);
					// 最終判定備考
					updateMst.setLastJdgRmk(splr.lastJdgRmk);
					// システム
					updateMst.setCorporationCodeUpdated(corporationCode);
					updateMst.setUserCodeUpdated(userCode);
					updateMst.setIpUpdated(ipAddr);
					updateMst.setTimestampUpdated(now);

					// 更新
					em.merge(updateMst);

				} else {
					// 登録日を
					List<Object> insertParams = new ArrayList<>();

					// 会社コード
					insertParams.add(splr.companyCd);
					// 取引先コード
					insertParams.add(splr.splrCd);
					// 会社コード
					insertParams.add(splr.companyCd);
					// 取引先コード
					insertParams.add(splr.splrCd);
					// 取引先名称（漢字）
					insertParams.add(splr.splrNmKj);
					// 取引先名称（半角ｶﾅ）
					insertParams.add(splr.splrNmKn);
					// 取引先名称（略称）
					insertParams.add(splr.splrNmS);
					// 取引先名称（英名）
					insertParams.add(splr.splrNmE);
					// 法人・個人区分
					insertParams.add(splr.crpPrsTp);
					// 国内・海外区分
					insertParams.add(splr.dmsAbrTp);
					// 国名
					insertParams.add(splr.lndNm);
					// 法人番号
					insertParams.add(splr.crpNo);
					// 郵便番号
					insertParams.add(splr.zipCd);
					// 住所（都道府県）コード
					insertParams.add(splr.adrPrfCd);
					// 住所（都道府県）
					insertParams.add(splr.adrPrf);
					// 住所（市区町村）
					insertParams.add(splr.adr1);
					// 住所（町名番地）
					insertParams.add(splr.adr2);
					// 住所（建物名）
					insertParams.add(splr.adr3);
					// 電話番号
					insertParams.add(splr.telNo);
					// FAX番号
					insertParams.add(splr.faxNo);
					// 関係会社区分
					insertParams.add(splr.affcmpTp);
					// 取引状況区分
					insertParams.add(splr.trdStsTp);
					// 有効期間（開始）
					insertParams.add(splr.vdDtS);
					// 有効期間（終了）
					insertParams.add(splr.vdDtE);
					// 備考
					insertParams.add(splr.rmk);
					// 部門コード
					insertParams.add(splr.bumonCd);
					// 適格請求書発行事業者
					insertParams.add(splr.cmptEtrNo);
					// 生年月日
					insertParams.add(splr.brthDt);
					// 国コード
					insertParams.add(splr.lndCd);
					// 最終判定区分
					insertParams.add(splr.lastJdgTp);
					// 最終判定備考
					insertParams.add(splr.lastJdgRmk);

					insertParams.add(corporationCode);
					insertParams.add(userCode);
					insertParams.add(ipAddr);
					insertParams.add(now);
					insertParams.add(corporationCode);
					insertParams.add(userCode);
					insertParams.add(ipAddr);
					insertParams.add(now);

					Query query = em.createNativeQuery(insertSql.toString());
					putParams(query, insertParams.toArray());
					query.executeUpdate();
				}
			}
		}

		// 振込先
		selectSql = new StringBuilder(getSql("MD0020_02"));
		insertSql = new StringBuilder(getSql("MD0000_02"));
		deleteSql = new StringBuilder(getSql("MD0001_02"));

		for (MdExcelSplrAccEntity splrAcc : book.sheetAcc.accs) {
			// まずは削除
			if (eq("D", splrAcc.processType)) {
				List<Object> deleteParams = new ArrayList<>();
				deleteParams.add(splrAcc.companyCd);
				deleteParams.add(splrAcc.splrCd);
				deleteParams.add(splrAcc.payeeBnkaccCdSs);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, deleteParams.toArray());
				query.executeUpdate();
			} else if (in(splrAcc.processType, "A", "U", "C")) {

				// 存在チェック
				List<PayeeBnkaccMst> payeeBnkMstList = select(PayeeBnkaccMst.class, selectSql, new Object[]{splrAcc.companyCd, splrAcc.splrCd, splrAcc.payeeBnkaccCdSs});

				// 更新
				if (payeeBnkMstList.size() != 0) {
					PayeeBnkaccMst updateMst = payeeBnkMstList.get(0);

					// 仕入先社員区分
					updateMst.setBuyeeStfTp(splrAcc.buyeeStfTp);
					// 銀行コード
					updateMst.setBnkCd(splrAcc.bnkCd);
					// 銀行支店コード
					updateMst.setBnkbrcCd(splrAcc.bnkbrcCd);
					// 銀行口座種別
					updateMst.setBnkaccTp(splrAcc.bnkaccTp);
					// 銀行口座番号
					updateMst.setBnkaccNo(splrAcc.bnkaccNo);
					// 銀行口座名称
					updateMst.setBnkaccNm(splrAcc.bnkaccNm);
					// 銀行口座名称（半角ｶﾅ）
					updateMst.setBnkaccNmKn(splrAcc.bnkaccNmKn);
					// 振込手数料負担区分
					updateMst.setPayCmmOblTp(splrAcc.payCmmOblTp);
					// 休日処理区分
					updateMst.setHldTrtTp(splrAcc.hldTrtTp);
					// 有効期間（開始）
					updateMst.setVdDtS(splrAcc.vdDtS);
					// 有効期間（終了）
					updateMst.setVdDtE(splrAcc.vdDtE);
					// 振込元銀行口座コード
					updateMst.setBnkaccCd(splrAcc.srcBnkaccCd);
					// 備考
					updateMst.setRmk(splrAcc.rmk);

					// システム
					updateMst.setCorporationCodeUpdated(corporationCode);
					updateMst.setUserCodeUpdated(userCode);
					updateMst.setIpUpdated(ipAddr);
					updateMst.setTimestampUpdated(now);

					// 更新
					em.merge(updateMst);

				} else {
					List<Object> insertParams = new ArrayList<>();

					// 会社コード
					insertParams.add(splrAcc.companyCd);
					// 会社コード
					insertParams.add(splrAcc.companyCd);
					// 会社コード
					insertParams.add(splrAcc.splrCd);
					// 振込先銀行口座コード(SS)
					insertParams.add(splrAcc.payeeBnkaccCdSs);
					// 仕入先社員区分
					insertParams.add(splrAcc.buyeeStfTp);
					// 取引先コード
					insertParams.add(splrAcc.splrCd);
					// 銀行コード
					insertParams.add(splrAcc.bnkCd);
					// 銀行支店コード
					insertParams.add(splrAcc.bnkbrcCd);
					// 銀行口座種別
					insertParams.add(splrAcc.bnkaccTp);
					// 銀行口座番号
					insertParams.add(splrAcc.bnkaccNo);
					// 銀行口座名称
					insertParams.add(splrAcc.bnkaccNm);
					// 銀行口座名称（半角ｶﾅ）
					insertParams.add(splrAcc.bnkaccNmKn);
					// 振込手数料負担区分
					insertParams.add(splrAcc.payCmmOblTp);
					// 休日処理区分
					insertParams.add(splrAcc.hldTrtTp);
					// 有効期間（開始）
					insertParams.add(splrAcc.vdDtS);
					// 有効期間（終了）
					insertParams.add(splrAcc.vdDtE);
					// 振込元銀行口座コード
					insertParams.add(splrAcc.srcBnkaccCd);
					// 振込先銀行口座コード(SuperStream)
					insertParams.add(splrAcc.payeeBnkaccCdSs);
					// 備考
					insertParams.add(splrAcc.rmk);

					insertParams.add(corporationCode);
					insertParams.add(userCode);
					insertParams.add(ipAddr);
					insertParams.add(now);
					insertParams.add(corporationCode);
					insertParams.add(userCode);
					insertParams.add(ipAddr);
					insertParams.add(now);

					Query query = em.createNativeQuery(insertSql.toString());
					putParams(query, insertParams.toArray());
					query.executeUpdate();
				}
			}
		}

		// 該当の関係先マスタ及び反社情報を削除する
		// ※取引先の処理区分が「A」、「U」、「C」のレコードを削除
		final Set<String> splrTargetsSet = book.sheetSplr.splrs.stream().filter(s -> in(s.processType, "A", "U", "C")).map(s -> String.format("%s_%s", s.companyCd, s.splrCd)).collect(Collectors.toSet());
		final String deleteRltPrtSql = "delete from RLT_PRT_MST where COMPANY_CD = ? and SPLR_CD = ?";
		final String deleteOrgCrmSql = "delete from ORG_CRM_INF where COMPANY_CD = ? and SPLR_CD = ?";
		splrTargetsSet.forEach(k -> {
			final List<Object> params = new ArrayList<>();
			final String companyCd = k.split("_")[0];
			final String splrCd = k.split("_")[1];
			params.add(companyCd);
			params.add(splrCd);

			execSql(deleteRltPrtSql, params.toArray());
			execSql(deleteOrgCrmSql, params.toArray());
		});

		for (MdExcelRltPrtEntity rlt : book.sheetRlt.rlts) {
			if (!splrTargetsSet.contains(String.format("%s_%s", rlt.companyCd, rlt.splrCd))) {
				continue;
			}
			final RltPrtMst entity = new RltPrtMst();
			entity.setId(new RltPrtMstPK());
			entity.getId().setCompanyCd(rlt.companyCd);
			entity.getId().setSplrCd(rlt.splrCd);
			entity.getId().setSqno(Long.parseLong(rlt.sqno));

			entity.setRltPrtNm(rlt.rltPrtNm);
			entity.setCrpPrsTp(rlt.crpPrsTp);
			entity.setLndCd(rlt.lndCd);
			entity.setBrthDt(rlt.brthDt);
			entity.setMtchCnt(isEmpty(rlt.mtchCnt) ? 0 : Integer.parseInt(rlt.mtchCnt));
			entity.setMtchPeid(rlt.mtchPeid);
			entity.setJdgTp(rlt.jdgTp);
			entity.setRltPrtRmk(rlt.rltPrtRmk);

			entity.setDltFg(DeleteFlag.OFF);
			entity.setCorporationCodeCreated(corporationCode);
			entity.setUserCodeCreated(userCode);
			entity.setIpCreated(ipAddr);
			entity.setTimestampCreated(now);
			entity.setCorporationCodeUpdated(corporationCode);
			entity.setUserCodeUpdated(userCode);
			entity.setIpUpdated(ipAddr);
			entity.setTimestampUpdated(now);
			em.persist(entity);
		}

		final Map<String, Long> brnos = new LinkedHashMap<>();
		for (MdExcelOrgCrmEntity org : book.sheetOrg.orgs) {
			if (!splrTargetsSet.contains(String.format("%s_%s", org.companyCd, org.splrCd))) {
				continue;
			}

			final String key = String.format("%s_%s_%s", org.companyCd, org.splrCd, org.sqno);
			if (!brnos.containsKey(key)) {
				brnos.put(key, 0L);
			}
			brnos.put(key, brnos.get(key) + 1);

			final OrgCrmInf entity = new OrgCrmInf();
			entity.setId(new OrgCrmInfPK());
			entity.getId().setCompanyCd(org.companyCd);
			entity.getId().setSplrCd(org.splrCd);
			entity.getId().setSqno(Long.parseLong(org.sqno));
			entity.getId().setBrno(brnos.get(key));

			entity.setPeid(org.peid);
			entity.setMtchNm(org.mtchNm);
			entity.setLndCd(org.lndCd);
			entity.setGndTp(org.gndTp);
			entity.setBrthDt(org.brthDt);

			entity.setDltFg(DeleteFlag.OFF);
			entity.setCorporationCodeCreated(corporationCode);
			entity.setUserCodeCreated(userCode);
			entity.setIpCreated(ipAddr);
			entity.setTimestampCreated(now);
			entity.setCorporationCodeUpdated(corporationCode);
			entity.setUserCodeUpdated(userCode);
			entity.setIpUpdated(ipAddr);
			entity.setTimestampUpdated(now);

			em.persist(entity);
		}
	}

	/** 取引先マスタ抽出 */
	public List<MdExcelSplrEntity> getSplr(Md0010SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MD0010_01"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false, true);

		return select(MdExcelSplrEntity.class, sql, params.toArray());
	}

	/**
	 * 対象の取引先口座明細抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public List<MdExcelSplrAccEntity> getAcc(Md0010SearchRequest req) {

		StringBuilder sql = new StringBuilder("select pbm.* from (" + getSql("MD0020_01"));

		// ソート
		sql.append(" ORDER BY pbm.PAYEE_BNKACC_CD ASC");

		sql.append(") pbm ");
		sql.append("left join SPLR_MST s ");
		sql.append(" on pbm.COMPANY_CD = s.COMPANY_CD ");
		sql.append(" and pbm.SPLR_CD = s.SPLR_CD ");
		sql.append("where s.DLT_FG = '0' ");

		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		fillCondition(req, sql, params, false, false);

		//検索
		return select(MdExcelSplrAccEntity.class, sql, params.toArray());
	}

	/**
	 * 対象の関係先マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public List<MdExcelRltPrtEntity> getRlt(Md0010SearchRequest req) {
		final StringBuilder sql = new StringBuilder();
		sql.append("select ROW_NUMBER() OVER (ORDER BY pbm.COMPANY_CD, pbm.SPLR_CD, pbm.SQNO) AS NO, pbm.* from RLT_PRT_MST pbm ");
		sql.append("inner join SPLR_MST s ");
		sql.append(" on (pbm.COMPANY_CD = s.COMPANY_CD and pbm.SPLR_CD = s.SPLR_CD) ");
		sql.append("where s.DLT_FG = '0' ");

		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false, false);

		// ソート
		sql.append(" ORDER BY pbm.COMPANY_CD, pbm.SPLR_CD, pbm.SQNO ASC");

		//検索
		return select(MdExcelRltPrtEntity.class, sql, params.toArray());
	}

	/**
	 * 対象の反社情報抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public List<MdExcelOrgCrmEntity> getOrg(Md0010SearchRequest req) {
		final StringBuilder sql = new StringBuilder();
		sql.append("select ROW_NUMBER() OVER (ORDER BY pbm.COMPANY_CD, pbm.SPLR_CD, pbm.SQNO, pbm.BRNO) AS NO, pbm.* from ORG_CRM_INF pbm ");
		sql.append("inner join SPLR_MST s ");
		sql.append(" on (pbm.COMPANY_CD = s.COMPANY_CD and pbm.SPLR_CD = s.SPLR_CD) ");
		sql.append("where s.DLT_FG = '0' ");

		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false, false);

		// ソート
		sql.append(" ORDER BY pbm.COMPANY_CD, pbm.SPLR_CD, pbm.SQNO, pbm.BRNO ASC");

		//検索
		return select(MdExcelOrgCrmEntity.class, sql, params.toArray());
	}

}
