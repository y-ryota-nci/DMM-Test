package jp.co.dmm.customize.endpoint.batch.purord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.endpoint.batch.common.BatchBaseRepository;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMst;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.PurordInfPK;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInf;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;

/**
 * 定期発注バッチリポジトリ
 */
@ApplicationScoped
public class PurOrdRepository extends BatchBaseRepository {



	/**
	 * 実行日にマッチング発注予約予定日の情報取得
	 * @param executeDate 実行日
	 * @param companyCdList 対象企業コード一覧
	 * @return
	 */
	public List<PrdPurordPlnMst> getPrdPurordPln(Date executeDate, List<String> companyCdList) {
		String sql = getSql("PURORD_PO0001");
		sql += " and " + toInListSql("pm.COMPANY_CD", companyCdList.size());

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(executeDate);
		params.addAll(companyCdList);

		return select(PrdPurordPlnMst.class, sql, params.toArray());
	}


	public PrdPurordMst getPrdPurordMst(String companyCd, String purordNo) {
		String sql = getSql("PURORD_PO0002");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(purordNo);

		return selectOne(PrdPurordMst.class, sql, params.toArray());
	}

	public PurordInf getPurordInf(String companyCd, String purordNo) {

		PurordInfPK id = new PurordInfPK();
		id.setCompanyCd(companyCd);
		id.setPurordNo(purordNo);

		return em.find(PurordInf.class, id);
	}

	public List<PurorddtlInf> getPurorddtlInf(String companyCd, String purordNo) {
		String sql = getSql("PURORD_PO0003");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(purordNo);

		return select(PurorddtlInf.class, sql, params.toArray());
	}

	/**
	 * 発注申請作成処理
	 * @param params パラメータ
	 */
	public void createOrderRequest(List<Object> params) {
		String sql = getSql("PURORD_PO0007");
		execSql(sql, params.toArray());
	}

	/**
	 * 発注申請明細作成処理
	 * @param params パラメータ
	 */
	public void createOrderDtlRequest(List<Object> params) {
		String sql = getSql("PURORD_PO0008");
		execSql(sql, params.toArray());
	}

	/**
	 * 消費税マスタ一覧取得
	 * @param companyCd 会社コード
	 * @return 消費税マスタ一覧
	 */
	public List<TaxMst> getTaxMst(String companyCd) {

		String sql = getSql("PURORD_PO0009");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);

		return select(TaxMst.class, sql, params.toArray());
	}

	/**
	 * 費目マスタ一覧取得
	 * @param companyCd 会社コード
	 * @return 費目マスタ一覧
	 */
	public List<ItmexpsMst> getItemexpsMst(String companyCd) {

		String sql = getSql("PURORD_PO0010");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);

		return select(ItmexpsMst.class, sql, params.toArray());
	}

	/**
	 * 費目関連マスタ一覧取得
	 * @param companyCd 会社コード
	 * @return 費目マスタ一覧
	 */
	public List<Itmexps1Chrmst> getItemexpsChrMst(String companyCd) {

		String sql = getSql("PURORD_PO0011");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);

		return select(Itmexps1Chrmst.class, sql, params.toArray());
	}

	/**
	 * 品目マスタ一覧取得
	 * @param companyCd 会社コード
	 * @return 費目マスタ一覧
	 */
	public List<ItmMst> getItemMst(String companyCd) {

		String sql = getSql("PURORD_PO0012");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);

		return select(ItmMst.class, sql, params.toArray());
	}

	/**
	 * 支払業務コードマスタ一覧取得
	 * @param companyCd 会社コード
	 * @return 支払業務コードマスタ一覧
	 */
	public List<PayApplMst> getPayApplMst(String companyCd) {

		String sql = getSql("PURORD_PO0013");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);

		return select(PayApplMst.class, sql, params.toArray());
	}

	/**
	 * 通貨コードマスタ一覧取得
	 * @param companyCd 会社コード
	 * @return 通貨コードマスタ一覧
	 */
	public List<MnyMst> getMnyMst(String companyCd) {

		String sql = getSql("PURORD_PO0014");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);

		return select(MnyMst.class, sql, params.toArray());
	}

	/**
	 * コード一覧取得
	 * @param companyCd 会社コード
	 * @return コードマ一覧
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> getCodeMap(String companyCd) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		String sql = getSql("PURORD_PO0015");

		// アイテム取得
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);

		List<Object[]> results = q.getResultList();

		for (Object[] result : results) {
			String optionCode = result[0].toString();
			String code = result[1].toString();
			String label = result[2].toString();

			if (!map.containsKey(optionCode)) {
				map.put(optionCode, new HashMap<String, String>());
			}

			map.get(optionCode).put(code, label);
		}

		return map;
	}

	/**
	 * 支払サイト一覧取得
	 * @param companyCd 会社コード
	 * @param rtnPayNo 支払サイトコード
	 * @return 支払サイト一覧
	 */
	public PaySiteMst getPaySiteMst(String companyCd, String paySiteCd) {

		String sql = getSql("PURORD_PO0016");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(paySiteCd);

		return selectOne(PaySiteMst.class, sql, params.toArray());
	}

	/**
	 * 源泉税区分マスタ一覧取得
	 * @param companyCd 会社コード
	 * @param hldtaxTp 源泉税区分
	 * @return 源泉税区分マスタ一覧
	 */
	public HldtaxMst getHldtaxMst(String companyCd, String hldtaxTp) {

		String sql = getSql("PURORD_PO0017");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(hldtaxTp);

		return selectOne(HldtaxMst.class, sql, params.toArray());
	}

	/**
	 * 発注対象かどうかのチェック
	 * @param companyCd 会社コード
	 * @param cntrctNo 契約Ｎｏ
	 * @param subject 件名
	 * @return チェック結果(true:申請可能)
	 */
	public boolean checkPreOrderStatus(String companyCd, String cntrctNo, String subject, Date applyDate) {

		String sql = getSql("PURORD_PO0018");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, cntrctNo);
		q.setParameter(3, subject);
		q.setParameter(4, applyDate);

		Number count = (Number) q.getSingleResult();
		return count == null || count.intValue() == 0;
	}

	public void updatePrdPurordPlnMst(List<Object> params) {
		String sql = getSql("PURORD_PO0019");
		execSql(sql, params.toArray());
	}
}
