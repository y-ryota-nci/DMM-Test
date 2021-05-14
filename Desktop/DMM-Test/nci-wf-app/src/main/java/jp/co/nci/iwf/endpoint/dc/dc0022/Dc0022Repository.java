package jp.co.nci.iwf.endpoint.dc.dc0022;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.DocHelper;
import jp.co.nci.iwf.component.tray.TrayConditionDef;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocInfoEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 拡張項目一覧リポジトリ.
 */
@ApplicationScoped
public class Dc0022Repository extends BaseRepository {

	private static final String REPLACE = "###REPLACE###";

	@Inject
	private DocHelper helper;

	/** アクセス可能な文書情報一覧件数取得. */
	public int count(TraySearchRequest req, Map<String, TrayConditionDef> defs, LoginInfo login, boolean isIgnoreAuth) {
		// 条件
		final List<Object> params = new ArrayList<>();

		final StringBuilder sqlDoc = isIgnoreAuth ? new StringBuilder(getSql("DC0017")) :new StringBuilder(getSql("DC0016"));
		fillConditionDoc(req, defs, sqlDoc, params, login, isIgnoreAuth);

		final StringBuilder sql = new StringBuilder(getSql("DC0014").replaceFirst(REPLACE, sqlDoc.toString()));
		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** アクセス可能な文書情報一覧取得. */
	public List<DocInfoEntity> select(TraySearchRequest req, Map<String, TrayConditionDef> defs, LoginInfo login, boolean isIgnoreAuth) {
		final List<Object> params = new ArrayList<>();

		final StringBuilder sqlDoc = isIgnoreAuth ? new StringBuilder(getSql("DC0017")) :new StringBuilder(getSql("DC0016"));
		fillConditionDoc(req, defs, sqlDoc, params, login, isIgnoreAuth);

		final StringBuilder sql = new StringBuilder(getSql("DC0015").replaceFirst(REPLACE, sqlDoc.toString()));
		fillCondition(req, sql, params, true);

		return select(DocInfoEntity.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記(文書) */
	private void fillConditionDoc(TraySearchRequest req, Map<String, TrayConditionDef> defs, StringBuilder sql, List<Object> params, LoginInfo login, boolean isIgnoreAuth) {
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());

		// ログイン者のログイン情報より文書権限のハッシュ値を取得
		if (!isIgnoreAuth) {
			final Set<String> hashValues = helper.toHashValues(login);
			StringBuilder replace = new StringBuilder();
			for (String hashValue: hashValues) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(hashValue);
			}
			int start = sql.indexOf(REPLACE);
			int end = start + REPLACE.length();
			sql.replace(start, end, replace.toString());
		}

		params.add(login.getLocaleCode());

		sql.append(" and MDI.DELETE_FLAG = '0'");
		sql.append(" and MDI.CORPORATION_CODE = ?");
		params.add(login.getCorporationCode());

		for (String businessInfoCode : defs.keySet()) {
			final TrayConditionDef d = defs.get(businessInfoCode);
			final String htmlId = toCamelCase(businessInfoCode);
			final Object val = req.get(htmlId);
			if (isNotEmpty(val)) {
				final String expression = toExpression(d);
				final Object value = toValue(val, d);	// %を付与するとかの補正処理
				sql.append(expression);
				params.add(value);
			}
		}
	}

	/** SQL用に値の加工 */
	private Object toValue(Object val, TrayConditionDef d) {
		if (isEmpty(val))
			return val;
		if (eq(d.conditionMatchType, ConditionMatchType.FRONT))
			return escapeLikeFront(val.toString());
		if (eq(d.conditionMatchType, ConditionMatchType.BOTH))
			return escapeLikeBoth(val.toString());
		return val;
	}

	/** SQL式に加工 */
	private String toExpression(TrayConditionDef d) {
		final String colName = toColName(d);
		final StringBuilder sb = new StringBuilder(32)
				.append(" and ").append(colName);

		if (in(d.conditionMatchType, ConditionMatchType.FRONT, ConditionMatchType.BOTH))
			sb.append(" like ? escape '~'");
		else if (eq(d.conditionMatchType, ConditionMatchType.FULL))
			sb.append(" = ?");
		else if (eq(d.conditionMatchType, ConditionMatchType.GT))
			sb.append(" > ?");
		else if (eq(d.conditionMatchType, ConditionMatchType.GTE))
			sb.append(" >= ?");
		else if (eq(d.conditionMatchType, ConditionMatchType.LT))
			sb.append(" < ?");
		else if (eq(d.conditionMatchType, ConditionMatchType.LTE))
			sb.append(" <= ?");
		else if (eq(d.conditionMatchType, ConditionMatchType.NOT_EQUAL))
			sb.append(" <> ?");
		else if (eq(d.conditionMatchType, ConditionMatchType.RANGE))
			sb.append(" between ? and ?");

		return sb.toString();
	}

	/** カラム名に変換 */
	private String toColName(TrayConditionDef d) {
		return "MDI." + d.businessInfoCode;
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(TraySearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		// ページングおよびソート
		if (paging) {
			// ソート
			String sortColumn = (String)req.get("sortColumn");
			Boolean sortAsc = (Boolean)req.get("sortAsc");
			if (isNotEmpty(sortColumn)) {
				sql.append(toSortSql(sortColumn, sortAsc));
			}
			// ページング
			Integer pageSize = (Integer)req.get("pageSize");
			Integer pageNo = (Integer)req.get("pageNo");
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(pageNo, pageSize));
			params.add(pageSize);
		}
	}
}
