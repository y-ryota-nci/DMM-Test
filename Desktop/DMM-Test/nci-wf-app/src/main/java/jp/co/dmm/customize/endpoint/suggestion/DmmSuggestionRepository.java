package jp.co.dmm.customize.endpoint.suggestion;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMstPK;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMst;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/** 汎用のSuggestion用リポジトリ */
@ApplicationScoped
public class DmmSuggestionRepository extends BaseRepository {
	/** 定期発注予定マスタを抽出 */
	public List<PrdPurordPlnMst> getPrdPurordPlnMst(String companyCd, Long prdPurordNo) {
		String sql = getSql("PO0000_07");
		Object[] params = { companyCd, prdPurordNo };
		return select(PrdPurordPlnMst.class, sql, params);
	}

	/** 支払サイトマスタ抽出 */
	public PaySiteMst getPaySiteMst(String companyCd, String paySiteCd) {
		PaySiteMstPK pk = new PaySiteMstPK();
		pk.setCompanyCd(companyCd);
		pk.setPaySiteCd(paySiteCd);
		return em.find(PaySiteMst.class, pk);
	}
}
