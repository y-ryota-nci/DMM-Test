package jp.co.dmm.customize.endpoint.vd.vd0310;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMstPK;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * DMMカスタムリポジトリ（VD0310用）
 */
@ApplicationScoped
public class DmmCustomRepository extends BaseRepository {
	/**
	 * 支払サイトマスタを抽出
	 * @param companyCd
	 * @param paySiteCd
	 * @return
	 */
	public PaySiteMst getPaySiteMst(String companyCd, String paySiteCd) {
		final PaySiteMstPK pk = new PaySiteMstPK();
		pk.setCompanyCd(companyCd);
		pk.setPaySiteCd(paySiteCd);
		return em.find(PaySiteMst.class, pk);
	}

}
