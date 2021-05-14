package jp.co.dmm.customize.endpoint.sp.sp0011;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.endpoint.sp.PayeeBnkaccMstEntity;
import jp.co.dmm.customize.endpoint.sp.SplrMstEntity;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 取引先登録画面のリポジトリ
 */
@ApplicationScoped
public class Sp0011Repository extends BaseRepository {

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode, boolean isEmpty) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0' order by B.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}

		for (MwmOptionItem item : results) {
			newItems.add(new OptionItem(item.getCode(), item.getLabel()));
		}

		return newItems;
	}

	/**
	 * 対象の取引先マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public SplrMstEntity get(Sp0011GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("SP0010_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());
		params.add(req.companyCd);
		params.add(req.companyCd);
		params.add(LoginInfo.get().getLocaleCode());
		params.add(req.companyCd);

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and s.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and s.SPLR_CD = ? ");
			params.add(req.splrCd);
		}

		//検索
		List<SplrMstEntity> results = select(SplrMstEntity.class, sql, params.toArray());

		if (results.size() != 0) {
			return results.get(0);
		} else {
			return new SplrMstEntity();
		}
	}

	/**
	 * 対象の取引先口座明細件数抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int getAccountCount(Sp0011GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder("select count(*) from (" + getSql("SP0020_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		// 会社コード
		if (!"00053".equals(LoginInfo.get().getCorporationCode())){
			if (isNotEmpty(req.companyCd)) {
				sql.append(" and pbm.COMPANY_CD = ? ");
				params.add(req.companyCd);
			}
		}

		// 取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and pbm.SPLR_CD = ? ");
			params.add(req.splrCd);
		}

		sql.append(")");

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 対象の取引先口座明細抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public List<PayeeBnkaccMstEntity> getAccountList(Sp0011GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder("select * from (" + getSql("SP0020_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		// 会社コード
		if (!"00053".equals(LoginInfo.get().getCorporationCode())){
			if (isNotEmpty(req.companyCd)) {
				sql.append(" and pbm.COMPANY_CD = ? ");
				params.add(req.companyCd);
			}
		}

		// 取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and pbm.SPLR_CD = ? ");
			params.add(req.splrCd);
		}

		// ソート
		sql.append(" ORDER BY pbm.PAYEE_BNKACC_CD ASC ");

		// ページング
		sql.append(") offset ? rows fetch first ? rows only");
		params.add(toStartPosition(req.pageNo, req.pageSize));
		params.add(req.pageSize);

		return select(PayeeBnkaccMstEntity.class, sql, params.toArray());
	}

	/**
	 * 取引先情報登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Sp0011UpdateRequest req, WfUserRole userRole) {

		// 削除フラグ更新
		String updateSql = getSql("SP0001_01");
		execSql(updateSql, new Object[]{req.companyCd, req.splrCd});

		// INSERT
		// SQL
		StringBuilder sql = new StringBuilder(getSql("SP0001_03"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.splrCd);
		params.add(req.companyCd);
		params.add(req.splrCd);
		params.add(req.splrNmKj);
		params.add(req.splrNmKn);
		params.add(req.splrNmS);
		params.add(req.splrNmE);
		params.add(req.crpPrsTp);
		params.add(req.dmsAbrTp);
		params.add(req.lndNm);
		params.add(req.crpNo);
		params.add(req.zipCd);
		params.add(req.adrPrfCd);
		params.add(req.adr1);
		params.add(req.adr2);
		params.add(req.adr3);
		params.add(req.telNo);
		params.add(req.faxNo);
		params.add(req.affcmpTp);
		params.add(req.cptl);
		params.add(req.mladr1);
		params.add(req.mladr2);
		params.add(req.trdStsTp);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
		params.add(req.payBsnCd);
		params.add(req.rmk);
		params.add(req.payCondCd);
		params.add(req.payCondNm);

		params.add(req.subCntrctTp);
		params.add(req.splrPicNm);
		params.add(req.splrPstNm);
		params.add(req.splrDptNm);

		params.add(req.bumonCd);

		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return query.executeUpdate();
	}

}
