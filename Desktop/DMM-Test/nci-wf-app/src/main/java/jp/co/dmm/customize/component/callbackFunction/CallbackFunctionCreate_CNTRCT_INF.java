package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import jp.co.dmm.customize.endpoint.vd.vd0310.DmmCustomService;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsRepeater;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 新規契約申請、新規契約申請（経常支払）の承認後に呼び出されるコールバックファンクション。
* WF新規取引先申請テーブル(MWT_CNTR020001、MWT_CNTR021001)からDMMの契約情報マスタテーブル(CNTRCT_INF)、経常支払マスタ(RTN_PAY_MST)へデータを吸い出す。
 * 【変更申請は対象ではない】
 */
public class CallbackFunctionCreate_CNTRCT_INF extends BaseCallbackFunction {
	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// 2019/02/03 部長決済条件を削除
//		// 契約部長承認＆利用雛形（変更し）以外の場合は処理をスキップ
//		String targetType = ctx.runtimeMap.get("RAD0010").getValue();
//
//		if ("0000000003".equals(contents.activityDefCode) && !"3".equals(targetType)) {
//			return;
//		}

		// 会社コード
		String companyCd = ctx.runtimeMap.get("TXT0305").getValue();
		// 契約No
		String cntrctNo = ctx.runtimeMap.get("NMB0003").getValue();
		// 経常支払設定区分
		String rtnPayTp = ctx.runtimeMap.get("TXT0304").getValue();
		// 申請者ユーザ情報を取得(第三階層組織)
		String orgnzCd = ctx.runtimeMap.get("TXT1054").getValue();

		// 経常支払設定がある場合は、経常支払Ｎｏを採番
		BigDecimal rtnPayNo = null;
		if (CommonFlag.ON.equals(rtnPayTp)) {
			rtnPayNo = getRTN_PAY_NO(companyCd);
		}

		// 契約情報INSERT
		int count = insertCNTRCT_INF(in, root.rows.get(0).runtimeId, rtnPayNo, orgnzCd);
		if (count == 0) {
			throw new InternalServerErrorException("契約情報の実インサート行数が0でした。");
		}

		// 契約先INSERT
		insertCNTRCT_SPLR_INF(in, ctx);

		// INSERT行数
		count = 0;

		// 経常支払マスタ登録
		if (CommonFlag.ON.equals(rtnPayTp)) {

			//取引先情報
			PartsRepeater repeater = (PartsRepeater)ctx.runtimeMap.get("RPT1000");	// 取引先のリピーター

			//取引先の登録が無い場合は処理終了
			if (repeater.rows.size() == 0) {
				return;
			}

			count = insertRTN_PAY_MST(in, ctx, rtnPayNo, repeater.rows.get(0));
			if (count == 0) {
				throw new InternalServerErrorException("経常支払情報の実インサート行数が0でした。");
			}

			// 経常支払明細登録
			insertRTN_PAYDTL_MST(in, ctx, rtnPayNo);

			// 経常支払予定マスタを登録
			count = insertRTN_PAY_PLN_MST(in.getWfUserRole(), companyCd, cntrctNo, ctx);
			if (count == 0) {
				throw new InternalServerErrorException("経常支払予定マスタの実インサート行数が0でした。");
			}

			// ワークフロー画面に経常支払番号を設定
			ctx.runtimeMap.get("SAS0401-1_TXT0101").setValue(rtnPayNo.toString());
		}
	}

	/**
	 * 日付計算
	 * @param base 基準日
	 * @param modifyM 月の補正
	 * @param modifyD 日の補正
	 * @return
	 */
	private Date calcDate(Date base, int modifyM, int modifyD) {
		Calendar c = Calendar.getInstance();
		c.setTime(base);
		c.add(Calendar.MONTH, modifyM);
		if (modifyD > 31)	// 31日以降は月末日
			c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		else
			c.set(Calendar.DATE, modifyD);
		return MiscUtils.trunc(c.getTime());
	}

	/** 経常支払予定マスタを登録 */
	private int insertRTN_PAY_PLN_MST(WfUserRole ur, String companyCd, String cntrctNo, RuntimeContext ctx) {
		final DmmCustomService svc = get(DmmCustomService.class);
		final String sql = getSql("CO0000_07");

		// 支払サイト
		final String paySiteCd = ctx.runtimeMap.get("SAS0401-1_TXT0124").getValue();
		// 役務収受開始
		final Date start = MiscUtils.toDate(ctx.runtimeMap.get("SAS0401-1_TXT0119").getValue(), "yyyy/MM");
		// 役務収受終了
		final Date end = MiscUtils.toDate(ctx.runtimeMap.get("SAS0401-1_TXT0120").getValue(), "yyyy/MM");
		// 経常支払区分→支払サイクル
		String rtnPayTp = ctx.runtimeMap.get("SAS0401-1_RAD0118").getValue();
		int cycle = MiscUtils.toInt(rtnPayTp);

		// 役務収受開始から支払サイクル間隔で役務収受終了まで続ける
		int sqno = 0;
		Date d = start;
		while (d.compareTo(end) <= 0) {
			sqno++;
			// 支払予定日：基準月と支払サイトから導出
			final Date payPlnDt = svc.calcPayPlnDt(MiscUtils.toStr(d, "yyyy/MM"), companyCd, paySiteCd);
			// 発注作成予定日：[サービス利用年月日] の 前月末日
			final Date purordBtchPlnDt = calcDate(d, -1, 99);
			// 検収作成予定日：[サービス利用年月日] の 当月末日
			final Date rcvinspBtchPlnDt = calcDate(d, 0, 99);
			// 支払申請作成予定日：[支払予定日] の30日前
			// ただし検収作成予定日 ≧ 支払申請作成予定日となった場合、検収作成予定日の翌日を支払申請作成予定日とする
			final Date tmpPaysbmtBtchPlnDt = MiscUtils.addDay(payPlnDt, -30);
			final Date paysbmtBtchPlnDt =
					MiscUtils.compareTo(rcvinspBtchPlnDt, tmpPaysbmtBtchPlnDt) >= 0 ? MiscUtils.addDay(rcvinspBtchPlnDt, 1) : tmpPaysbmtBtchPlnDt;

			final Object[] params = {
					// select句
					sqno, purordBtchPlnDt, rcvinspBtchPlnDt, paysbmtBtchPlnDt,
					ur.getCorporationCode(), ur.getUserCode(), ur.getIpAddress(),
					ur.getCorporationCode(), ur.getUserCode(), ur.getIpAddress(),
					// where句
					companyCd, cntrctNo
			};
			execSql(sql, params);

			d = MiscUtils.addMonth(d, cycle);
		}
		return sqno;
	}

	/** 経常支払Ｎｏを採番 */
	private BigDecimal getRTN_PAY_NO(String companyCode) {
		String sql = getSql("CO0020_01");
		EntityManager em = get(EntityManager.class);
		Query q = em.createNativeQuery(sql.toString());
		q.setParameter(1, companyCode);
		Number targetNumber = (Number)q.getSingleResult();

		return new BigDecimal(targetNumber.toString());
	}

	/** 契約情報テーブルをINSERT */
	private int insertCNTRCT_INF(
			MoveActivityInstanceInParam in, Long runtimeId, BigDecimal rtnPayNo, String orgnzCd) {

		String sql = getSql("CO0000_01");

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final Object[] args = {
				rtnPayNo,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				orgnzCd,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 契約先マスタテーブルをINSERT */
	private void insertCNTRCT_SPLR_INF(MoveActivityInstanceInParam in,  RuntimeContext ctx) {
		final String sql = getSql("CO0000_02");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		PartsRepeater repeater = (PartsRepeater)ctx.runtimeMap.get("RPT1000");	// 契約取引先

		for (PartsContainerRow detail : repeater.rows) {
			final Object[] args = {
					corporationCode, userCode, ipAddr, now,
					corporationCode, userCode, ipAddr, now,
					detail.runtimeId
			};
			execSql(sql, args);
		}
	}

	/** 経常支払マスタテーブルをINSERT */
	private int insertRTN_PAY_MST(MoveActivityInstanceInParam in, RuntimeContext ctx, BigDecimal rtnPayNo, PartsContainerRow splr) {
		final String sql = getSql("CO0000_03");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		PartsStandAlone standAlone = (PartsStandAlone)ctx.runtimeMap.get("SAS0401");

		String splrCdId = splr.children.stream().filter(id -> id.endsWith("TXT1001")).findFirst().orElseGet(null);
		String splrNmKjId = splr.children.stream().filter(id -> id.endsWith("TXT1002")).findFirst().orElseGet(null);
		String splrNmKnId = splr.children.stream().filter(id -> id.endsWith("TXT1003")).findFirst().orElseGet(null);
		String crpPrsTpId = splr.children.stream().filter(id -> id.endsWith("TXT1410")).findFirst().orElseGet(null);
		String dmsAbrTpId = splr.children.stream().filter(id -> id.endsWith("TXT1411")).findFirst().orElseGet(null);

		String splrCd = ctx.runtimeMap.get(splrCdId).getValue();
		String splrNmKj = ctx.runtimeMap.get(splrNmKjId).getValue();
		String splrNmKn = ctx.runtimeMap.get(splrNmKnId).getValue();
		String crpPrsTp = ctx.runtimeMap.get(crpPrsTpId).getValue();
		String dmsAbrTpKn = ctx.runtimeMap.get(dmsAbrTpId).getValue();

		Long runtimeId = standAlone.rows.get(0).runtimeId;

		final Object[] args = {
				rtnPayNo,
				splrCd,
				splrNmKj,
				splrNmKn,
				crpPrsTp,
				dmsAbrTpKn,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 経常支払明細マスタテーブルをINSERT */
	private void insertRTN_PAYDTL_MST(MoveActivityInstanceInParam in,  RuntimeContext ctx, BigDecimal rtnPayNo) {
		final String sql = getSql("CO0000_04");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		PartsGrid repeater = (PartsGrid)ctx.runtimeMap.get("SAS0401-1_GRD1100");

		for (PartsContainerRow detail : repeater.rows) {
			final Object[] args = {
					rtnPayNo,
					rtnPayNo,
					corporationCode, userCode, ipAddr, now,
					corporationCode, userCode, ipAddr, now,
					detail.runtimeId
			};
			execSql(sql, args);
		}
	}

}
