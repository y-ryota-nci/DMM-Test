package jp.co.dmm.customize.endpoint.md.md0011;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.endpoint.md.MdPayeeBnkaccMstEntity;
import jp.co.dmm.customize.endpoint.md.MdSplrMstEntity;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 取引先登録画面のリポジトリ
 */
@ApplicationScoped
public class Md0011Repository extends BaseRepository {

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
	public MdSplrMstEntity get(Md0011GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MD0010_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

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
		List<MdSplrMstEntity> results = select(MdSplrMstEntity.class, sql, params.toArray());

		if (results.size() != 0) {
			return results.get(0);
		} else {
			return new MdSplrMstEntity();
		}
	}

	/**
	 * 対象の取引先口座明細件数抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int getAccountCount(Md0011GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder("select count(1) from (" + getSql("MD0020_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and pbm.COMPANY_CD = ? ");
			params.add(req.companyCd);
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
	public List<MdPayeeBnkaccMstEntity> getAccountList(Md0011GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder("select * from (" + getSql("MD0020_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and pbm.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and pbm.SPLR_CD = ? ");
			params.add(req.splrCd);
		}

		// ソート
		sql.append(" ORDER BY pbm.PAYEE_BNKACC_CD ASC");

		// ページング
		sql.append(") offset ? rows fetch first ? rows only");
		params.add(toStartPosition(req.pageNo, req.pageSize));
		params.add(req.pageSize);

		return select(MdPayeeBnkaccMstEntity.class, sql, params.toArray());
	}
}
