package jp.co.dmm.customize.endpoint.ri.ri0020;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 検収明細分割のリポジトリ
 */
@ApplicationScoped
public class Ri0020Repository extends BaseRepository {

	/** 消費税コードリストの取得 */
	public List<TaxMst> getTaxCdList(String companyCd) {
		final Object[] params = { companyCd };
		return select(TaxMst.class, getSql("RI0020_03"), params);
	}
}
