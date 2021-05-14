package jp.co.dmm.customize.endpoint.vd.vd0310;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.component.DmmCodeBook;
import jp.co.dmm.customize.endpoint.py.PayInfService;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * DMMカスタムサービス（VD0310用）
 */
@BizLogic
public class DmmCustomService extends BaseService {
	@Inject private DmmCustomRepository repository;
	@Inject private PayInfService payService;

	/** 基準月と支払サイトから支払予定日を算出 */
	public DmmCustomResponse getPayPlnDt(DmmCustomRequest req) {
		// 会社CD
		final String companyCd = (String)req.parameters.get("companyCd");
		if (isEmpty(companyCd))
			throw new BadRequestException("会社CDが未指定です");
		// 支払サイトCD
		final String paySiteCd = (String)req.parameters.get("paySiteCd");
		// 基準年月（発注：検収予定日の月初、支払：サービス月）
		final String baseYm = (String)req.parameters.get("baseYm");	// yyyy/MM形式

		// 基準月と支払サイトから支払予定日を算出
		final Date payPlnDt = calcPayPlnDt(baseYm, companyCd, paySiteCd);

		final DmmCustomResponse res = createResponse(DmmCustomResponse.class, req);
		res.results.put("payPlnDt", payPlnDt);
		res.success = true;
		return res;
	}

	/** 基準月と支払サイトから支払予定日を算出 */
	public Date calcPayPlnDt(final String baseYm, final String companyCd, final String paySiteCd) {
		// 計算根拠が揃っていなければ求めようがない
		if (isEmpty(companyCd) || isEmpty(paySiteCd) || isEmpty(baseYm)) {
			return null;
		}

		// 支払サイトマスタ
		final PaySiteMst psm = repository.getPaySiteMst(companyCd, paySiteCd);
		final String paySiteM = psm.getPaySiteM();

		Date payPlnDt = null;
		if (eq("9", paySiteM)) {	// その他→検収予定日は設定しない
			payPlnDt = null;
		}
		else {
			// 基準年月を支払サイト(月)で補正
			final Date base = toDate(baseYm + "/01", "yyyy/MM/dd");
			if (eq("1", paySiteM))		// 1:翌月
				payPlnDt = addMonth(base, 1);
			else if (eq("2", paySiteM))	// 2:翌々月
				payPlnDt = addMonth(base, 2);
			else if (eq("3", paySiteM))	// 3:前月
				payPlnDt = addMonth(base, -1);
			else if (eq("4", paySiteM))	// 4:当月
				payPlnDt = base;

			// 支払サイト(日)による補正
			final Calendar cal = Calendar.getInstance();
			cal.setTime(payPlnDt);
			int dd;
			if (eq(psm.getPaySiteN(), "99")) 	// 99:月末
				dd = cal.getActualMaximum(Calendar.DATE);
			else
				dd = Integer.valueOf(psm.getPaySiteN());

			// 支払予定日
			cal.set(Calendar.DATE, dd);
			payPlnDt = trunc(cal.getTime());
		}
		return payPlnDt;
	}

	public DmmCustomResponse getCstAddYm(DmmCustomRequest req) {
		// 会社CD
		final String companyCd = (String)req.parameters.get("companyCd");
		if (isEmpty(companyCd))
			throw new BadRequestException("会社CDが未指定です");
		// サービス利用月
		final String rcvinspYm = (String)req.parameters.get("rcvinspYm");
		// 検収からの呼出し
		final boolean rcvinsp = CommonFlag.ON.equals(req.parameters.get("rcvinsp"));

		// サービス利用月とカレンダーマスタから費用計上月を算出
		final String cstAddYm = getCstAddYm(companyCd, rcvinspYm, rcvinsp);
		// 費用計上月から計上レート基準日を取得
		final Date rtoBaseDt = getRtoBaseDt(toDate(cstAddYm + "/01", "yyyy/MM/dd"));

		final DmmCustomResponse res = createResponse(DmmCustomResponse.class, req);
		res.results.put("cstAddYm", cstAddYm);
		res.results.put("rtoBaseDt", toStr(rtoBaseDt, "yyyy/MM/dd"));
		res.success = true;
		return res;

	}

	public String getCstAddYm(String companyCd, String rcvinspYm, boolean rcvinsp) {
		final Date today = today();
		final String baseDate = toStr(today);
		final AccClndMst m = payService.getAccClndMst(companyCd, baseDate);
		final String systemYm = toStr(today, "yyyy/MM");
		final String lastYm = toStr(addMonth(today, -1), "yyyy/MM");
		if (m == null)
			throw new InvalidUserInputException(baseDate + "の会計カレンダーが存在しないため、処理を続行できません。");
		// システム日付(日) ＜＝ 15の場合（カレンダマスタの決済区分(検収：購買、支払：財務)="1"：決算＆当月）
		if (CommonFlag.ON.equals(rcvinsp ? m.getStlTpPur() : m.getStlTpFncobl())) {
			if (compareTo(rcvinspYm, systemYm) < 0)
				return lastYm;
			else
				return systemYm;
		} else {
			if (compareTo(rcvinspYm, systemYm) < 0)
				return systemYm;	// ※現状、条件分岐の意味が無いが確認中のため分岐しておく。
			else
				return systemYm;
		}
	}

	/** 計上レート基準日を取得 */
	public Date getRtoBaseDt(Date d) {
		if (d == null)
			return null;
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/** 支払方法に応じた振込元／引落先の銀行口座情報取得. */
	public DmmCustomResponse getBnkAccMst(DmmCustomRequest req) {
		// 会社CD
		final String companyCd = (String)req.parameters.get("companyCd");
		// 支払方法
		final String payMth = (String)req.parameters.get("payMth");
		// 取引先コード
		final String splrCd = (String)req.parameters.get("splrCd");

		if (isEmpty(companyCd) || isEmpty(payMth))
			throw new BadRequestException("会社CDもしくは支払方法が未指定です");

		// 支払方法に応じて振込元／引落先のデフォルトの銀行口座情報を取得
		Map<String, Object> map = null;
		if (eq(DmmCodeBook.PayMth.FURIKOMI, payMth)) {
			map = payService.getSrcBnkaccMst(companyCd, splrCd);
		} else {
			map = payService.getChrgBnkaccMst(companyCd, payMth);
		}

		final DmmCustomResponse res = createResponse(DmmCustomResponse.class, req);
		if (map != null) {
			for (String key: map.keySet()) {
				res.results.put(key, map.get(key));
			}
		}
		res.success = true;
		return res;
	}
}
