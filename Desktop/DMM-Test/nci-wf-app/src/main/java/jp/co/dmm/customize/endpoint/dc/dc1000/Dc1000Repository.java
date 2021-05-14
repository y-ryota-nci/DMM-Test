package jp.co.dmm.customize.endpoint.dc.dc1000;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.DocHelper;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * OCR状況一覧のリポジトリ
 */
@ApplicationScoped
public class Dc1000Repository extends BaseRepository {

	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private DocHelper helper;

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getOptionItems(String corporationCode, String localeCode, boolean isEmpty) {
		String query = getSql("DC1000_01");
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(localeCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}
		results.stream().forEach(i -> newItems.add(new OptionItem(i.getCode(), i.getLabel())));

		return newItems;
	}

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Dc1000Request req) {
		StringBuilder sql = new StringBuilder(getSql("DC1000_02").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Dc1000Request req, Dc1000Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("DC1000_02").replaceFirst(REPLACE, getSql("DC1000_03")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Dc1000Entity.class, sql, params.toArray());
	}

	private void fillCondition(Dc1000Request req, StringBuilder sql, List<Object> params, boolean paging) {
		final LoginInfo login = LoginInfo.get();

		params.add(login.getLocaleCode());
		params.add(login.isCorpAdmin() ? CommonFlag.ON : CommonFlag.OFF);
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());

		// ログイン者のログイン情報より文書権限のハッシュ値を取得
		{
			final Set<String> hashValues = helper.toHashValues(login);
			StringBuilder replace = new StringBuilder();
			for (String hashValue: hashValues) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(hashValue);
			}

			int start = sql.indexOf("###REPLACE###");
			int end = start + "###REPLACE###".length();
			sql.replace(start, end, replace.toString());
		}

		params.add(LoginInfo.get().getCorporationCode());

		// OCR状況
		if (isNotEmpty(req.ocrFlag)) {
			sql.append(" and F.OCR_FLAG = ? ");
			params.add(req.ocrFlag);
		}
		// OCR実行日
		if (isNotEmpty(req.ocrExecutionDateFrom)) {
			sql.append(" and trunc(?) <= trunc(OD.TIMESTAMP_UPDATED)");
			params.add(req.ocrExecutionDateFrom);
		}
		if (isNotEmpty(req.ocrExecutionDateTo)) {
			sql.append(" and trunc(OD.TIMESTAMP_UPDATED) <= trunc(?)");
			params.add(req.ocrExecutionDateTo);
		}

		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}

		if (paging) {
			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

}
