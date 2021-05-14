package jp.co.dmm.customize.endpoint.bd.bd0803;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 予算履歴メンテナンスのリポジトリ
 */
@ApplicationScoped
public class Bd0803Repository extends BaseRepository {
	@Inject private SessionHolder sessionHolder;

	/** 予算履歴バージョンの抽出 */
	public List<Bd0803Entity> getBgtPlnHstver(Bd0803Request req) {
		final Object[] params = {
				sessionHolder.getLoginInfo().getLocaleCode(),
				CorporationCodes.DMM_COM, req.yrCd, req.organizationCodeLv3, req.rcvCostPayTp, req.bsplTp
		};
		final StringBuilder sql = new StringBuilder()
				.append(getSql("BD0803_01"))
				.append(toSortSql(req.sortColumn, req.sortAsc));

		return select(Bd0803Entity.class, sql, params);
	}

	/** 予算履歴バージョンの削除 */
	public void delete(Bd0803Entity src) {
		// 予算履歴バージョン
		{
			final Object[] params = { CorporationCodes.DMM_COM, src.yrCd, src.organizationCode, src.rcvCostPayTp, src.hstVersion };
			final String sql = getSql("BD0803_02");
			execSql(sql, params);
		}
		// 予算履歴
		{
			final Object[] params = { CorporationCodes.DMM_COM, src.yrCd, src.organizationCode, src.rcvCostPayTp, src.hstVersion };
			final String sql = getSql("BD0803_03");
			execSql(sql, params);
		}
	}

}
