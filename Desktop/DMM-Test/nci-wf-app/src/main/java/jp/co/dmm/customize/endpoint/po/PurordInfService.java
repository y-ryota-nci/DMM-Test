package jp.co.dmm.customize.endpoint.po;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.PurordSts;
import jp.co.dmm.customize.jpa.entity.mw.PaydtlInf;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.PurordInfPK;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInf;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInfPK;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInfPK;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * DMM発注情報用サービス
 */
@ApplicationScoped
public class PurordInfService extends BaseRepository {
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** DMM定期発注情報サービス */
	@Inject private PrdPurordInfService prdPurordInfService;

	/**
	 * 発注Noをキーに発注情報を抽出し、それをUserDataServiceで使用できる形に整形して返す
	 * @param companyCd 会社CD
	 * @param purordNo 発注No
	 * @return
	 */
	public Map<String, List<UserDataEntity>> getUserDataMap(String companyCd, String purordNo) {
		assert(isNotEmpty(companyCd));
		assert(isNotEmpty(purordNo));

		final String localeCode = LoginInfo.get().getLocaleCode();
		final Object[] params = { companyCd, purordNo };
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();

		// 発注情報をユーザデータとして読み込み
		{
			final String tableName = "MWT_PURCHASE_ORDER";	// ヘッダ部のコンテナのテーブル名
			final String sql = getSql("PO0011_01");
			final Object[] args = { localeCode, companyCd, purordNo };
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, args);
			tables.put(tableName, userDataList);
		}
		// 発注明細情報をユーザデータとして読み込み
		{
			final String tableName = "MWT_PURCHASE_ORDER_DETAIL";	// 明細部のコンテナのテーブル名
			final String sql = getSql("PO0011_02");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		// 定期発注マスタ（定期発注申請すると定期発注マスタへ書き込むから）
		// 定期発注予定マスタ（定期発注申請すると定期発注予定マスタへ書き込むから）
		tables.putAll(prdPurordInfService.getUserDataMap(companyCd, purordNo));
		return tables;
	}

	/**
	 * 検収明細単位で発注ステータスを更新。
	 * （この処理の前に検収金額や支払金額の更新がある前提です）
	 *
	 * @param rcvKeys 検収明細情報のキーリスト（複数明細
	 */
	public void updatePurordSts(Set<RcvinspdtlInfPK> rcvKeys) {
		// 検収明細に紐付く発注Noを抽出し、Setに入れて同一発注Noを集約
		final Set<PurordInfPK> poKeys = new HashSet<>();
		for (RcvinspdtlInfPK t : rcvKeys) {
			RcvinspdtlInf r = em.find(RcvinspdtlInf.class, t);
			if (isNotEmpty(r.getPurordNo())) {
				PurordInfPK k = new PurordInfPK();
				k.setCompanyCd(r.getId().getCompanyCd());
				k.setPurordNo(r.getPurordNo());
				poKeys.add(k);
			}
		}

		// 発注No単位に発注ステータスを更新
		for (PurordInfPK key : poKeys) {
			// 発注情報抽出
			final PurordInf po = em.find(PurordInf.class, key);
			final String purordSts = po.getPurordSts();

			// 現在の発注ステータスが発注済 or 検収済なら、検収金額／支払金額によって変わる可能性がある
			if (in(purordSts, PurordSts.PURORD_FIXED, PurordSts.RCVINSP_FIXED)) {
				final List<String> sts = new ArrayList<>();
				final List<PurorddtlInf> details = getPurordDtlInf(key);
				for (PurorddtlInf d : details) {
					// 明細単位発注金額と支払金額／検収金額を比較
					//  ・発注明細.発注金額≦支払明細.支払金額なら、この発注明細は支払済とみなす
					//  ・発注明細.発注金額≦検収明細.検収金額なら、この発注明細は検収済とみなす
					//  ・その他は発注済のまま
					final BigDecimal purAmt = getPurordAmt(d, po.getMnyCd());
					final BigDecimal rcvAmt = getRcvAmt(d.getId(), po.getMnyCd());	// sum(発注明細Noに紐付く検収金額)
					final BigDecimal payAmt = getPayAmt(d.getId(), po.getMnyCd());	// sum(発注明細Noに紐付く支払金額)

					if (compareTo(purAmt, new BigDecimal(0)) >= 0) {
						//明細の金額がプラスのケース
						if (compareTo(purAmt, payAmt) <= 0) {
							sts.add(PurordSts.PAY_FIXED);
						} else if (compareTo(purAmt, rcvAmt) <= 0) {
							sts.add(PurordSts.RCVINSP_FIXED);
						} else {
							// 発注済が１つでもある時点で、発注ステータスが検収済以降にならない
							sts.add(PurordSts.PURORD_FIXED);
							break;
						}
					} else {
						//明細の金額がマイナスのケース
						if (compareTo(purAmt, payAmt) >= 0) {
							sts.add(PurordSts.PAY_FIXED);
						} else if (compareTo(purAmt, rcvAmt) >= 0) {
							sts.add(PurordSts.RCVINSP_FIXED);
						} else {
							// 発注済が１つでもある時点で、発注ステータスが検収済以降にならない
							sts.add(PurordSts.PURORD_FIXED);
							break;
						}
					}
				}

				// 全明細が支払済みなら、発注ステータス＝支払済で更新
				// 全明細が検収以降なら、発注ステータス＝検収済で更新
				if (sts.stream().allMatch(s -> eq(s, PurordSts.PAY_FIXED)))
					po.setPurordSts(PurordSts.PAY_FIXED);
				else if (sts.stream().allMatch(s -> in(s, PurordSts.RCVINSP_FIXED, PurordSts.PAY_FIXED)))
					po.setPurordSts(PurordSts.RCVINSP_FIXED);
			}
		}
	}

	/** 発注Noに紐付く発注明細情報を抽出 */
	private List<PurorddtlInf> getPurordDtlInf(PurordInfPK key) {
		final Object[] params = { key.getCompanyCd(), key.getPurordNo() };
		final String sql = getSql("PO0000_13");
		return select(PurorddtlInf.class, sql, params);
	}

	/** 発注明細単位の支払金額を合算して返す */
	private BigDecimal getPayAmt(PurorddtlInfPK key, String mnyCd) {
		final Object[] params = { key.getCompanyCd(), key.getPurordNo(), key.getPurordDtlNo() };
		final String sql = getSql("PO0000_12");

		BigDecimal sum = BigDecimal.ZERO;
		for (PaydtlInf r : select(PaydtlInf.class, sql, params)) {
			if (eq("JPY", mnyCd) && r.getPayAmtJpy() != null)
				sum = sum.add(r.getPayAmtJpy());
			else if (!eq("JPY", mnyCd) && r.getPayAmtFc() != null)
				sum = sum.add(r.getPayAmtFc());
		}
		return sum;
	}

	/** 発注明細単位の検収金額を合算して返す */
	private BigDecimal getRcvAmt(PurorddtlInfPK key, String mnyCd) {
		final Object[] params = { key.getCompanyCd(), key.getPurordNo(), key.getPurordDtlNo() };
		final String sql = getSql("PO0000_11");

		BigDecimal sum = BigDecimal.ZERO;
		for (RcvinspdtlInf r : select(RcvinspdtlInf.class, sql, params)) {
			if (eq("JPY", mnyCd) && r.getRcvinspAmtJpy() != null)
				sum = sum.add(r.getRcvinspAmtJpy());
			else if (!eq("JPY", mnyCd) && r.getRcvinspAmtFc() != null)
				sum = sum.add(r.getRcvinspAmtFc());
		}
		return sum;
	}

	/** 発注明細.発注金額(税抜) */
	private BigDecimal getPurordAmt(PurorddtlInf dtl, String mnyCd) {
		if (eq("JPY", mnyCd) && dtl.getPurordAmtJpy() != null)
			return dtl.getPurordAmtJpy();
		else if (!eq("JPY", mnyCd) && dtl.getPurordAmtFc() != null)
			return dtl.getPurordAmtFc();
		return BigDecimal.ZERO;
	}
}
