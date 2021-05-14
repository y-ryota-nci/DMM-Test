package jp.co.dmm.customize.component.dowjones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.jpa.entity.mw.LndMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 国マスタのリポジトリ
 */
@ApplicationScoped
public class DowjonesRepository extends BaseRepository {

	private static final String REPLACE = quotePattern("${REPLACE}");
	/** ログイン者情報 */
	@Inject private SessionHolder sessionHolder;

	/**
	 * 国マスタ取得
	 * @return
	 */
	public Map<String, List<LndMst>> select() {
		final StringBuilder replace = new StringBuilder();
		replace.append("  L.COMPANY_CD");
		replace.append(", L.LND_CD");
		replace.append(", L.LND_NM");
		replace.append(", L.LND_CD_DJII");
		replace.append(", L.DLT_FG");
		replace.append(", O.LABEL DLT_FG_NM ");
		final StringBuilder sql = new StringBuilder(getSql("MG0330_01").replaceFirst(REPLACE, replace.toString()));
		final List<Object> params = new ArrayList<>();
		// ロケールコード
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		// 会社コード：「00020」を固定に設定
		params.add(CorporationCodes.DMM_COM);
		sql.append(toSortSql("L.SORT_ORDER", true));
		return select(LndMst.class, sql.toString(), params.toArray()).stream().collect(Collectors.groupingBy(r -> r.getLndCdDjii()));
	}

}
