package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.endpoint.batch.common.BatchBaseRepository;
import jp.co.dmm.customize.jpa.entity.mw.BnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.CntrctInf;
import jp.co.dmm.customize.jpa.entity.mw.CntrctSplrInf;
import jp.co.dmm.customize.jpa.entity.mw.CntrctdtlInf;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInf;
import jp.co.dmm.customize.jpa.entity.mw.RtnPayMst;
import jp.co.dmm.customize.jpa.entity.mw.RtnPaydtlMst;
import jp.co.dmm.customize.jpa.entity.mw.SplrMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;

/**
 * 経常支払バッチリポジトリ
 */
@ApplicationScoped
public class RtnPayRepository extends BatchBaseRepository {

	/**
	 * 経常支払一覧取得
	 * @param yearMonth 対象年月（yyyymm）
	 * @return 経常支払一覧
	 */
	public List<RtnPayMst> getRtnPay(Long yearMonth, List<String> companyCdList) {

		String sql = getSql("RTNPAY_RP0001");
		sql += " and " + toInListSql("pm.COMPANY_CD", companyCdList.size());

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(yearMonth);
		params.addAll(companyCdList);

		return select(RtnPayMst.class, sql, params.toArray());
	}

	/**
	 * 経常支払一覧取得
	 * @param yearMonth 対象年月（yyyymm）
	 * @return 経常支払一覧
	 */
	public List<RtnPayMst> check(Long yearMonth, List<String> companyCdList) {

		String sql = getSql("RTNPAY_RP0001");
		sql += " and " + toInListSql("pm.COMPANY_CD", companyCdList.size());

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(yearMonth);
		params.addAll(companyCdList);

		return select(RtnPayMst.class, sql, params.toArray());
	}

	/**
	 * 経常支払明細一覧取得
	 * @param companyCd 会社コード
	 * @param rtnPayNo 経常支払No
	 * @return 経常支払明細一覧
	 */
	public List<RtnPaydtlMst> getRtnPayDtl(String companyCd, Long rtnPayNo) {

		String sql = getSql("RTNPAY_RP0002");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(rtnPayNo);

		return select(RtnPaydtlMst.class, sql, params.toArray());
	}

	/**
	 * 消費税マスタ一覧取得
	 * @param companyCd 会社コード
	 * @return 消費税マスタ一覧
	 */
	public List<TaxMst> getTaxMst(String companyCd) {

		String sql = getSql("RTNPAY_RP0009");

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

		String sql = getSql("RTNPAY_RP0011");

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

		String sql = getSql("RTNPAY_RP0026");

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

		String sql = getSql("RTNPAY_RP0025");

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

		String sql = getSql("RTNPAY_RP0013");

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

		String sql = getSql("RTNPAY_RP0027");

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

		String sql = getSql("RTNPAY_RP0018");

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
	public List<PaySiteMst> getPaySiteMst(String companyCd, String paySiteCd) {

		String sql = getSql("RTNPAY_RP0003");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(paySiteCd);

		return select(PaySiteMst.class, sql, params.toArray());
	}

	/**
	 * 源泉税区分マスタ一覧取得
	 * @param companyCd 会社コード
	 * @param hldtaxTp 源泉税区分
	 * @return 源泉税区分マスタ一覧
	 */
	public List<HldtaxMst> getHldtaxMst(String companyCd, String hldtaxTp) {

		String sql = getSql("RTNPAY_RP0010");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(hldtaxTp);

		return select(HldtaxMst.class, sql, params.toArray());
	}

	/**
	 * 契約情報一覧取得
	 * @param companyCd 会社コード
	 * @param ｃntrctNo 契約No
	 * @return 契約情報一覧
	 */
	public List<CntrctInf> getCntrctInf(String companyCd, String ｃntrctNo) {

		String sql = getSql("RTNPAY_RP0004");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(ｃntrctNo);

		return select(CntrctInf.class, sql, params.toArray());
	}

	/**
	 * 契約先情報一覧取得
	 * @param companyCd 会社コード
	 * @param ｃntrctNo 契約No
	 * @return 契約先情報一覧
	 */
	public List<CntrctSplrInf> getCntrctSplrInf(String companyCd, String ｃntrctNo) {

		String sql = getSql("RTNPAY_RP0005");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(ｃntrctNo);

		return select(CntrctSplrInf.class, sql, params.toArray());
	}

	/**
	 * 銀行情報取得処理
	 * @param companyCd 会社コード
	 * @param payeeBnkaccCd 銀行口座コード
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String[] getBnkNames(String companyCd, String payeeBnkaccCd) {

		String[] values = {null, null};

		String sql = getSql("RTNPAY_RP0016");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, payeeBnkaccCd);

		List<Object[]> results = q.getResultList();

		if (results.size() != 0) {
			values[0] = results.get(0)[0] != null ? results.get(0)[0].toString() : null;
			values[1] = results.get(0)[1] != null ? results.get(0)[1].toString() : null;
		}

		return values;
	}

	/**
	 * 銀行口座マスタ取得処理
	 * @param companyCd 会社コード
	 * @param bnkCd 銀行コード
	 * @param bnkbrcCd 支店コード
	 * @param bnkaccNo 口座番号
	 * @return
	 */
	public List<BnkaccMst> getBnkaccMst(String companyCd, String bnkCd, String bnkbrcCd, String bnkaccNo) {
		String sql = getSql("RTNPAY_RP0028");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(bnkCd);
		params.add(bnkbrcCd);
		params.add(bnkaccNo);

		return select(BnkaccMst.class, sql, params.toArray());
	}

	/**
	 * 取引先情報一覧取得
	 * @param companyCd 会社コード
	 * @param ｃntrctNo 契約No
	 * @return 契約先情報一覧
	 */
	public List<SplrMst> getSplrMst(String companyCd, String splrCd) {

		String sql = getSql("RTNPAY_RP0014");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(splrCd);

		return select(SplrMst.class, sql, params.toArray());
	}

	/**
	 * 振込先銀行口座マスタ一覧取得
	 * @param companyCd 会社コード
	 * @param ｃntrctNo 契約No
	 * @return 振込先銀行口座一覧
	 */
	public List<PayeeBnkaccMst> getPayeeBnkAccMst(String companyCd, String splrCd) {

		String sql = getSql("RTNPAY_RP0015");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(splrCd);

		return select(PayeeBnkaccMst.class, sql, params.toArray());
	}

	/**
	 * 契約明細一覧取得
	 * @param companyCd 会社コード
	 * @param ｃntrctNo 契約No
	 * @return 契約明細一覧
	 */
	public List<CntrctdtlInf> getCntrctdtlInf(String companyCd, String ｃntrctNo) {

		String sql = getSql("RTNPAY_RP0006");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(ｃntrctNo);

		return select(CntrctdtlInf.class, sql, params.toArray());
	}

	/**
	 * 発注情報一覧取得
	 * @param companyCd 会社コード
	 * @param ｃntrctNo 契約No
	 * @return 契約情報一覧
	 */
	public List<PurordInf> getPurordInf(String companyCd, String ｃntrctNo) {

		String sql = getSql("RTNPAY_RP0023");

		// パラメータ設定
		List<Object> params = new ArrayList<Object>();
		params.add(companyCd);
		params.add(ｃntrctNo);

		return select(PurordInf.class, sql, params.toArray());
	}

	/**
	 * 発注対象かどうかのチェック
	 * @param companyCd 会社コード
	 * @param cntrctNo 契約Ｎｏ
	 * @param subject 件名
	 * @return チェック結果
	 */
	public boolean checkPurchaseTarget(String companyCd, String cntrctNo, String subject) {

		String sql = getSql("RTNPAY_RP0020");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, cntrctNo);
		q.setParameter(3, subject);

		Number count = (Number)q.getSingleResult();
		return count == null || count.intValue() == 0;
	}

	/**
	 * 検収対象かどうかのチェック
	 * @param companyCd 会社コード
	 * @param cntrctNo 契約Ｎｏ
	 * @param subject 件名
	 * @return チェック結果
	 */
	public boolean checkRcvinspTarget(String companyCd, String cntrctNo, String subject) {

		String sql = getSql("RTNPAY_RP0021");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, cntrctNo);
		q.setParameter(3, subject);

		Number count = (Number)q.getSingleResult();
		return count == null || count.intValue() == 0;
	}

	/**
	 * 支払依頼対象かどうかのチェック
	 * @param companyCd 会社コード
	 * @param cntrctNo 契約Ｎｏ
	 * @param subject 件名
	 * @return チェック結果
	 */
	public boolean checkPayTarget(String companyCd, String cntrctNo, String subject) {

		String sql = getSql("RTNPAY_RP0022");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, cntrctNo);
		q.setParameter(3, subject);

		Number count = (Number)q.getSingleResult();
		return count == null || count.intValue() == 0;
	}

	/**
	 * 組織：チーム取得
	 * @param companyCd 会社コード
	 * @param cntrctNo 契約Ｎｏ
	 * @param subject 件名
	 * @return チェック結果
	 */
	@SuppressWarnings("unchecked")
	public String[] getTeamOrganization(String corporationCode, String organizationCode) {

		String[] returnVal = {null, null};

		String sql = getSql("RTNPAY_RP0024");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, corporationCode);
		q.setParameter(2, organizationCode);

		List<Object[]> results = q.getResultList();

		if (results.size() != 0 && results.get(0).length >= 2) {
			returnVal[0] = results.get(0)[0].toString();
			returnVal[1] = results.get(0)[1].toString();
		}

		return returnVal;
	}

	/**
	 * 支払依頼申請作成処理
	 * @param params パラメータ
	 */
	public void createPayRequest(List<Object> params) {
		String sql = getSql("RTNPAY_RP0105");
		execSql(sql, params.toArray());
	}

	/**
	 * 支払依頼申請明細作成処理
	 * @param params パラメータ
	 */
	public void createPayDtlRequest(List<Object> params) {
		String sql = getSql("RTNPAY_RP0106");
		execSql(sql, params.toArray());
	}

	/**
	 * @param companyCd 会社コード
	 * @param mnyCd 通貨コード
	 * @return 社内レート
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getInRate(String companyCd, String mnyCd, Date acceptanceDate) {
		BigDecimal inRto = null;

		String sql = getSql("RTNPAY_RP0019");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, mnyCd);
		q.setParameter(3, acceptanceDate);

		List<Object> results = q.getResultList();

		if (results.size() != 0) {
			inRto = (BigDecimal)results.get(0);
		}

		return inRto;
	}

	/**
	 * 経常支払マスタから発注情報作成
	 * @param purord 発注情報
	 */
	public void createPurchaseOrder(PurordInf purord) {
		em.persist(purord);
	}

	/**
	 * 経常支払明細マスタから発注明細情報作成
	 * @param purordDtl 発注情報
	 */
	public void createPurchaseOrderDetail(PurorddtlInf purordDtl) {
		em.persist(purordDtl);
	}

	/**
	 * 検収作成
	 * @param entity 検収情報
	 */
	public void createRcvinsp(RcvinspInf entity) {
		em.persist(entity);
	}

	/**
	 * 検収明細作成
	 * @param purordDtl 検収明細
	 */
	public void createRcvinspDetail(RcvinspdtlInf entity) {
		em.persist(entity);
	}
}
