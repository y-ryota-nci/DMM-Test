package jp.co.dmm.customize.component;

import java.sql.Date;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.jpa.entity.mw.CntrctInf;
import jp.co.dmm.customize.jpa.entity.mw.CntrctInfPK;
import jp.co.dmm.customize.jpa.entity.mw.SplrMst;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * DMM用帳票印刷ユーティリティ
 */
@ApplicationScoped
public class DmmPrintService extends BaseRepository {

	/**
	 * 取引先マスタを抽出
	 * @param companyCd 会社CD
	 * @param splrCd 取引先CD
	 * @param baseDate 基準日
	 * @return
	 */
	public SplrMst getSplrMst(String companyCd, String splrCd, Date baseDate) {
		final String sql = getSql("PO0000_14");
		final Object[] params = { companyCd, splrCd, baseDate };
		final SplrMst splr = selectOne(SplrMst.class, sql, params);
		em.detach(splr);
		return splr;
	}

	/**
	 * 契約情報を抽出
	 * @param companyCd 会社CD
	 * @param cntrctNo 契約No
	 * @return
	 */
	public CntrctInf getCntrctInf(String companyCd, String cntrctNo) {
		final CntrctInfPK pk = new CntrctInfPK();
		pk.setCompanyCd(companyCd);
		pk.setCntrctNo(cntrctNo);
		final CntrctInf cntrct = em.find(CntrctInf.class, pk);
		em.detach(cntrct);
		return cntrct;
	}
}
