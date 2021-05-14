package jp.co.dmm.customize.endpoint.py;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import jp.co.dmm.customize.component.DmmCodeBook.AdvpaySts;
import jp.co.dmm.customize.component.DmmCodeBook.RcvinspSts;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMst;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMstPK;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayInf;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayInfPK;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayMatInf;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayMatInfPK;
import jp.co.dmm.customize.jpa.entity.mw.PaydtlInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspInfPK;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInfPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.NativeSqlUtils;

@ApplicationScoped
public class PayInfService extends BaseRepository {

	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** セッション情報 */
	@Inject private SessionHolder sessionHolder;

	public Map<String, List<UserDataEntity>> getUserDataMap(String companyCd, String payNo, String advpayFg) {
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// ヘッダー
		{
			final Object[] params = { advpayFg, localeCode, companyCd, payNo };
			final String tableName = "MWT_PAY";	// ヘッダ部のコンテナのテーブル名
			final String sql = getSql("PY0031_01");
			final List<UserDataEntity> userDataList = loader.getUserData(tableName, sql, params);
			if (CommonFlag.ON.equals(advpayFg)) {
				final AdvpayInf e = getAdvpayInf(companyCd, payNo);
				userDataList.get(0).values.put("ADVPAY_NO", e.getId().getAdvpayNo());
			}
			tables.put(tableName, userDataList);
		}
		// 明細
		{
			final Object[] params = { localeCode, companyCd, payNo };
			final String tableName = "MWT_PAYDTL";
			final String sql = getSql("PY0031_02");
			final List<UserDataEntity> userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}

		return tables;
	}

	public boolean validatePayeeBnkaccCdSs(String companyCd, String payeeBnkaccCd, String payPlnDt) {
		final String sql = getSql("PY0031_03");
		final Object[] params = { companyCd, payeeBnkaccCd, payPlnDt};
		return count(sql, params) > 0;
	}

	/**
	 * 前払充当額が前払金額を超過していないこと。
	 * @param companyCd 会社コード
	 * @param payNo 支払No（今回申請する支払申請の支払No）
	 * @param advpayNo 前払金No
	 * @return 前払残高金額
	 */
	public BigDecimal getRmnPayAmt(String companyCd, String payNo, String advpayNo) {
		// 前払金情報
		BigDecimal payAmt = BigDecimal.ZERO;
		{
			final AdvpayInfPK pk = new AdvpayInfPK();
			pk.setCompanyCd(companyCd);
			pk.setAdvpayNo(advpayNo);
			final AdvpayInf entity = em.find(AdvpayInf.class, pk);
			if (entity != null) {
				payAmt = eq("JPY", entity.getMnyCd()) ? entity.getPayAmtJpy() : entity.getPayAmtFc();
			}
		}
		// 既存の前払金消込情報
		BigDecimal matAmt = BigDecimal.ZERO;
		{
			final Object[] params = { companyCd, advpayNo };
			final String sql = getSql("PY0031_04");
			final List<AdvpayMatInf> entities = select(AdvpayMatInf.class, sql, params);
			for (AdvpayMatInf e : entities) {
				// 今回の支払申請の検収Noの消込情報は除いて、消込金額を合算
				// 申請＞差戻＞再申請で消込金額が変更されることを想定してる
				if (isEmpty(e.getPayNo()) || !eq(e.getPayNo(), payNo)) {
					BigDecimal v = eq("JPY", e.getMnyCd()) ? e.getMatAmtJpy() : e.getMatAmtFc();
					matAmt = matAmt.add(v);
				}
			}
		}
		// 前払残高＝前払金額－既存の前払消込金額
		final BigDecimal rmnPayAmt = payAmt.add(matAmt.negate());
		return rmnPayAmt;
	}

	/**
	 * 前払金情報取得処理(支払Noから前払金情報を取得)
	 * @param companyCd
	 * @param payNo
	 * @return
	 */
	public AdvpayInf getAdvpayInf(String companyCd, String payNo) {
		return selectOne(AdvpayInf.class, getSql("PY0060_05"), new Object[] {companyCd, payNo});
	}

	/**
	 * 会計カレンダマスタ取得
	 * @param companyCd
	 * @param clndDt
	 * @return
	 */
	public AccClndMst getAccClndMst(String companyCd, String clndDt) {
		final AccClndMstPK pk = new AccClndMstPK();
		pk.setCompanyCd(companyCd);
		pk.setClndDt(toDate(clndDt, FORMAT_DATE));
		return em.find(AccClndMst.class, pk);
	}

	/**
	 * 支払Noに紐付く前払消込情報を抽出
	 * @param companyCd
	 * @param payNo
	 * @return
	 */
	public List<AdvpayMatInf> getAdvpayMatInfByPayNo(String companyCd, String payNo) {
		final String sql = getSql("PY0000_13");
		final Object[] params = { companyCd, payNo };
		return select(AdvpayMatInf.class, sql, params);
	}

	/** 前払金消込情報をインサート */
	public void insertAdvpayMat(String companyCd, String payNo, BigDecimal payDtlNo, String advpayNo,
			BigDecimal matAmt, String mnyCd, String addRto) {

		final AdvpayMatInfPK key = new AdvpayMatInfPK();
		key.setCompanyCd(companyCd);
		key.setAdvpayNo(advpayNo);
		key.setAdvpayMatNo(nextAdvpayMatNo(companyCd, advpayNo));

		final AdvpayMatInf e = new AdvpayMatInf();
		e.setId(key);
		if (eq("JPY", mnyCd)) {
			e.setMatAmtJpy(matAmt);
			e.setMatAmtJpyInctax(matAmt);	// 前払は必ず税込
		}
		else {
			e.setMatAmtFc(matAmt);
		}
		e.setMnyCd(mnyCd);
		e.setAddRto(MiscUtils.toBD(addRto));
		e.setPayNo(payNo);;
		e.setPayDtlNo(payDtlNo);
		e.setDltFg(DeleteFlag.OFF);

		final LoginInfo login = sessionHolder.getLoginInfo();
		e.setCorporationCodeCreated(login.getCorporationCode());
		e.setUserCodeCreated(login.getUserCode());
		e.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampCreated(timestamp());
		e.setCorporationCodeUpdated(login.getCorporationCode());
		e.setUserCodeUpdated(login.getUserCode());
		e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampUpdated(timestamp());

		em.persist(e);
	}

	/** 前払金消込情報をアップデート */
	public void updateAdvpayMat(AdvpayMatInf e, String payNo, BigDecimal payDtlNo, String advpayNo,
			BigDecimal matAmt, String mnyCd, String addRto) {

		if (eq("JPY", mnyCd)) {
			e.setMatAmtJpy(matAmt);
			e.setMatAmtJpyInctax(matAmt);	// 前払は必ず税込なので同値
			e.setMatAmtFc(null);
		}
		else {
			e.setMatAmtJpy(null);
			e.setMatAmtJpyInctax(null);
			e.setMatAmtFc(matAmt);
		}
		e.setMnyCd(mnyCd);
		e.setAddRto(MiscUtils.toBD(addRto));
		e.setRcvinspNo(null);
		e.setRcvinspDtlNo(null);
		e.setPayNo(payNo);
		e.setPayDtlNo(payDtlNo);
		e.setJrnslpNo(null);
		e.setJrnslpDtlNo(null);
		e.setSsglSndNo(null);
		e.setDltFg(DeleteFlag.OFF);

		final LoginInfo login = sessionHolder.getLoginInfo();
		e.setCorporationCodeUpdated(login.getCorporationCode());
		e.setUserCodeUpdated(login.getUserCode());
		e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampUpdated(timestamp());

		em.merge(e);
	}

	/** 前払金消込情報.前払金消込Noを採番 */
	private int nextAdvpayMatNo(String companyCd, String advpayNo) {
		final String sql = getSql("PY0000_14");
		final Object[] params = { companyCd, advpayNo };
		return count(sql, params) + 1;
	}

	/** 前払金消込情報を削除 */
	public void delete(AdvpayMatInf advpayMat) {
		em.remove(advpayMat);
	}

	/** 前払情報の残高と前払いステータスの更新 */
	public void updateAdvpayInf(String companyCd, Set<String> advpayNoList, String mnyCd) {
		for (String advpayNo : advpayNoList) {
			// 前払金Noに紐付く前払消込情報を抽出し、その消込金額を合算
			final List<AdvpayMatInf> matList = getAdvpayMatInf(companyCd, advpayNo);
			BigDecimal sumMatAmt = BigDecimal.ZERO;
			for (AdvpayMatInf mat : matList) {
				sumMatAmt = sumMatAmt.add(eq("JPY", mnyCd) ? mat.getMatAmtJpy() : mat.getMatAmtFc());
			}

			// 更新対象である前払情報を抽出
			final AdvpayInfPK key = new AdvpayInfPK();
			key.setCompanyCd(companyCd);
			key.setAdvpayNo(advpayNo);
			final AdvpayInf advpay = em.find(AdvpayInf.class, key);
			if (advpay == null)
				throw new NotFoundException("前払情報が見つかりません。会社CD=" + companyCd + " 前払金No=" + advpayNo);

			// 支払金額
			final BigDecimal payAmt = eq("JPY", mnyCd) ? advpay.getPayAmtJpy() : advpay.getPayAmtFc();
			// 残高＝支払金額－sum(消込金額)
			final BigDecimal rmnAmt = payAmt.add(sumMatAmt.negate());

			// 前払ステータス
			if (compareTo(rmnAmt, BigDecimal.ZERO) == 0)
				advpay.setAdvpaySts(AdvpaySts.DONE);
			else if (compareTo(rmnAmt, payAmt) == 0)
				advpay.setAdvpaySts(AdvpaySts.INIT);
			else if (compareTo(rmnAmt, BigDecimal.ZERO) < 0)
				throw new InternalServerErrorException("前払充当額の合算値が前払金額を超過しています。会社CD=" + companyCd + " 前払金No=" + advpayNo);
			else
				advpay.setAdvpaySts(AdvpaySts.DOING);

			// 残高
			if (eq("JPY", mnyCd)) {
				advpay.setRmnAmtJpy(rmnAmt);
				advpay.setRmnAmtFc(null);
			}
			else {
				advpay.setRmnAmtJpy(null);
				advpay.setRmnAmtFc(rmnAmt);
			}
			advpay.setDltFg(DeleteFlag.OFF);
		}
	}

	/** 前払金Noをキーに前払消込情報を抽出 */
	private List<AdvpayMatInf> getAdvpayMatInf(String companyCd, String advpayNo) {
		final Object[] params = { companyCd, advpayNo };
		final String sql = getSql("PY0000_15");
		return select(AdvpayMatInf.class, sql, params);
	}

	/** 検収No、検収明細Noをキーに前払金消込情報をアップデート */
	public void updateAdvpayMat(String companyCd, String rcvinspNo, BigDecimal rcvinspDtlNo, String payNo, BigDecimal payDtlNo) {
		final String sql = getSql("PY0000_16");
		final Object[] params = { companyCd, rcvinspNo, rcvinspDtlNo};
		select(AdvpayMatInf.class, sql, params).forEach(e -> {
			e.setPayNo(payNo);
			e.setPayDtlNo(payDtlNo);
			em.merge(e);
		});
	}

	/** 前払金消込情報の支払No紐付を削除 */
	public void update(AdvpayMatInf advpayMat) {
		advpayMat.setPayNo(null);
		advpayMat.setPayDtlNo(null);
		em.merge(advpayMat);
	}

	/**
	 * 検収明細単位で検収ステータスを更新。
	 * （この処理の前に検収金額や支払金額の更新がある前提です）
	 *
	 * @param rcvKeys 検収明細情報のキーリスト（複数明細
	 */
	public void updateRcvinspSts(Set<RcvinspdtlInfPK> rcvKeys) {
		// 検収明細に紐付く発注Noを抽出し、Setに入れて同一発注Noを集約
		final Set<RcvinspInfPK> riKeys = new HashSet<>();
		for (RcvinspdtlInfPK t : rcvKeys) {
			RcvinspInfPK k = new RcvinspInfPK();
			k.setCompanyCd(t.getCompanyCd());
			k.setRcvinspNo(t.getRcvinspNo());
			riKeys.add(k);
		}

		// 発注No単位に発注ステータスを更新
		for (RcvinspInfPK key : riKeys) {
			// 発注情報抽出
			final RcvinspInf ri = em.find(RcvinspInf.class, key);
			final String rcvinspSts = ri.getRcvinspSts();

			// 現在の発注ステータスが発注済 or 検収済なら、検収金額／支払金額によって変わる可能性がある
			if (in(rcvinspSts, RcvinspSts.RCVINSP_FIXED)) {
				final BigDecimal rcvAmt = getRcvAmt(key, ri.getMnyCd());
				final BigDecimal payAmt = getPayAmt(key, ri.getMnyCd());	// sum(発注明細Noに紐付く支払金額)

				if (compareTo(rcvAmt, payAmt) <= 0) {
					ri.setRcvinspSts(RcvinspSts.PAY_FIXED);
				} else {
					ri.setRcvinspSts(RcvinspSts.RCVINSP_FIXED);
				}

			}
		}
	}

	/** 振込元銀行口座マスタ情報を取得 */
	public Map<String, Object> getSrcBnkaccMst(String companyCd, String splrCd) {
		if (isEmpty(companyCd) || isEmpty(splrCd)) {
			return null;
		}
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Object[] params = { companyCd, splrCd, localeCode };
		final String sql = getSql("PY0000_19");
		try (Connection conn = NativeSqlUtils.getConnectionSA()){
			return NativeSqlUtils.selectOne(conn, sql, params);
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 引落先銀行口座マスタ情報を取得 */
	public Map<String, Object> getChrgBnkaccMst(String companyCd, String payMth) {
		if (isEmpty(companyCd) || isEmpty(payMth)) {
			return null;
		}
		final Object[] params = { companyCd, payMth };
		final String sql = getSql("PY0000_20");
		try (Connection conn = NativeSqlUtils.getConnectionSA()){
			return NativeSqlUtils.selectOne(conn, sql, params);
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 検収単位の検収金額を合算して返す */
	private BigDecimal getRcvAmt(RcvinspInfPK key, String mnyCd) {
		final Object[] params = { key.getCompanyCd(), key.getRcvinspNo() };
		final String sql = getSql("PY0000_08");

		BigDecimal sum = BigDecimal.ZERO;
		final List<RcvinspdtlInf> results = select(RcvinspdtlInf.class, sql, params);
		for (RcvinspdtlInf r : results) {
			if (eq("JPY", mnyCd) && r.getRcvinspAmtJpy() != null)
				sum = sum.add(r.getRcvinspAmtJpy());
			else if (!eq("JPY", mnyCd) && r.getRcvinspAmtFc() != null)
				sum = sum.add(r.getRcvinspAmtFc());
		}
		return sum;
	}

	/** 検収単位の支払金額を合算して返す */
	private BigDecimal getPayAmt(RcvinspInfPK key, String mnyCd) {
		final Object[] params = { key.getCompanyCd(), key.getRcvinspNo() };
		final String sql = getSql("PY0000_09");
		BigDecimal sum = BigDecimal.ZERO;
		final List<PaydtlInf> results = select(PaydtlInf.class, sql, params);
		for (PaydtlInf r : results) {
			if (eq("JPY", mnyCd) && r.getPayAmtJpy() != null)
				sum = sum.add(r.getPayAmtJpy());
			else if (!eq("JPY", mnyCd) && r.getPayAmtFc() != null)
				sum = sum.add(r.getPayAmtFc());
		}
		return sum;
	}

}
