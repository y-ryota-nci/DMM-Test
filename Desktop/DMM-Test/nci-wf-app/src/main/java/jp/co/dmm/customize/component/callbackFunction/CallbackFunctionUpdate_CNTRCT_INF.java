package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
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
 * 変更契約申請、変更契約申請（経常支払）の承認後に呼び出されるコールバックファンクション。
 * WF変更契約申請テーブル(MWT_CNTR020002、MWT_CNTR021001)からDMMの契約情報マスタテーブル(CNTRCT_INF)、経常支払マスタ(RTN_PAY_MST)へデータを吸い出す。
 * 【変更契約申請のみ対象】
 */
public class CallbackFunctionUpdate_CNTRCT_INF extends BaseCallbackFunction {
	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// 会社コード
		String companyCd = ctx.runtimeMap.get("TXT0305").getValue();
		// 契約No
		String cntrctNo = ctx.runtimeMap.get("NMB0003").getValue();
		// 経常支払設定区分
		String rtnPayTp = ctx.runtimeMap.get("TXT0304").getValue();
		// 申請者ユーザ情報を取得(第三階層組織)
		String orgnzCd = ctx.runtimeMap.get("TXT1054").getValue();

		if (StringUtils.isEmpty(rtnPayTp)) {
			rtnPayTp = "0";
		}

		//経常支払Ｎｏ
		BigDecimal rtnPayNo = null;
		boolean rtnUpdateFlag = false;
		String rtnPayNoStr = ctx.runtimeMap.get("SAS0401-1_TXT0101").getValue();

		// 経常支払Noが未採番の場合は採番
		if (CommonFlag.ON.equals(rtnPayTp) && StringUtils.isEmpty(rtnPayNoStr)) {
			rtnPayNo = getRTN_PAY_NO(companyCd);
		} else if (CommonFlag.ON.equals(rtnPayTp)){
			rtnPayNo = new BigDecimal(rtnPayNoStr);
			rtnUpdateFlag = true;
		}

		// 契約情報UPDATAE
		int count = updateSelectCNTRCT_INF(in, ctx, root.rows.get(0).runtimeId, companyCd, cntrctNo, rtnPayTp, rtnPayNo, orgnzCd);
		if (count == 0) {
			throw new InternalServerErrorException("契約情報の更新件数が0件でした。");
		}

		// 契約先DELETE&INSERT
		upsertCNTRCT_SPLR_INF(in, ctx, companyCd, cntrctNo);

		// 経常支払マスタDELETE&INSERT
		if (CommonFlag.ON.equals(rtnPayTp)) {

			//取引先情報
			PartsRepeater repeater = (PartsRepeater)ctx.runtimeMap.get("RPT1000");	// 取引先のリピーター
			PartsContainerRow targetRow = null;

			if (repeater.rows.size() != 0) {
				targetRow = repeater.rows.get(0);
			}

			upsertRTN_PAY_MST(in, ctx, rtnUpdateFlag, companyCd, cntrctNo, rtnPayNo, targetRow);

			// 経常支払明細マスタDELETE&INSERT
			upsertRTN_PAYDTL_MST(in, ctx, rtnUpdateFlag, companyCd, rtnPayNo, targetRow);

			// 2019/02/18. 経常支払予定マスタは更新しない。
			// 柚木さん曰く、新規で一度作ったらもうWF経由では変更できないとのこと。それはスクラッチ画面の仕事とするそうだ。
		}

	}

	/** 契約情報テーブルをUPDATE */
	private int updateSelectCNTRCT_INF(MoveActivityInstanceInParam in,  RuntimeContext ctx, Long runtimeId, String companyCd, String cntrctNo, String rtnPayTp, BigDecimal rtnPayNo, String orgnzCd) {

		String updateSql = getSql("CO0001_01");

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 経常支払い
		final String cntrctSts = "1";
		final String cntrctNm = ctx.runtimeMap.get("TXT0001").values.get("text");
		final String cntrctrCd = null;
		final String cntrctrDptCd = null;
		final String cntrctrDptNm = null;
		final String cntrctPrdSDt = ctx.runtimeMap.get("TXT0008").values.get("text");
		final String cntrctPrdEDt = ctx.runtimeMap.get("TXT0009").values.get("text");
		final String cntrctTp = null;
		final String cntrctshtFrmt = ctx.runtimeMap.get("RAD0010").values.get("radioCode");
		final String cntrctDt = ctx.runtimeMap.get("TXT0007").values.get("text");
		final String sbmtrCd = ctx.runtimeMap.get("TXT0301").values.get("text");
		final String sbmtDptCd = ctx.runtimeMap.get("TXT0302").values.get("text");
		final String sbmtDptNm = ctx.runtimeMap.get("TXT0021").values.get("text");
		final String sbmtDptDt = ctx.runtimeMap.get("TXT0022").values.get("text");
		final String cntrctDtlcnd = ctx.runtimeMap.get("TXT0011").values.get("text");
		final String cmpnOccPrs = null;
		final String trdEstAmtExctax = ctx.runtimeMap.get("TXT0024").values.get("text");
		final String trdEstAmtRmk = ctx.runtimeMap.get("TXT0025").values.get("text");
		final String payCycPlnTp = ctx.runtimeMap.get("RAD0026").values.get("radioCode");
		final String payCycPlnRmk = ctx.runtimeMap.get("TXT0027").values.get("text");
		final String rnwPrs = null;
		final String rnwDtlcnd = null;
		final String bkbndChgTp = ctx.runtimeMap.get("DDL1052").getValue();
		final String stmpAppTp = null;
		final String rmk = null;
		final String lglrCd = ctx.runtimeMap.get("TXT0308").values.get("text");
		final String cntrctShtNm = ctx.runtimeMap.get("TXT0002").values.get("text");
		final String cntrctCnclDt = null;
		final String cntrctDtS = null;
		final String cntrctDtE = null;
		final String rvnstmpPrs = null;
		final String rvnstmpAmt = ctx.runtimeMap.get("DDL0030").values.get("dropdownCode");
		final String rnwMthTpAuto = ctx.runtimeMap.get("CHK0012").values.get("check");
		final String rnwMthTpAutoInfMt = ctx.runtimeMap.get("TXT0013").values.get("text");
		final String rnwMthTpAutoPstpMt = ctx.runtimeMap.get("TXT0014").values.get("text");
		final String rnwMthTpNego = ctx.runtimeMap.get("CHK0015").values.get("check");
		final String rnwMthTpNegoInfMt = ctx.runtimeMap.get("TXT0016").values.get("text");
		final String rnwMthTpExpr = null;
		final String rnwMthTpIndf = null;
		final String rnwMthTp = ctx.runtimeMap.get("CHK1027").getValue();
		final String lglRmk = ctx.runtimeMap.get("TXT0033").values.get("text");
		final String paySiteCd = null;
		final String mnyCd = null;
		final String nCmptOblgTp = ctx.runtimeMap.get("RAD0031").values.get("radioCode");
		final String toApprvRmk = ctx.runtimeMap.get("TXT0032").values.get("text");
		final String cntrctshtRqstTp = null;
		final String toAdr = null;
		final String mailTp = ctx.runtimeMap.get("DDL1053").getValue();
		final String mailRmk = ctx.runtimeMap.get("TXT1038").getValue();
		final String shtToZipCd = ctx.runtimeMap.get("TXT1022").getValue();
		final String shtAdrPrfCd = ctx.runtimeMap.get("DDL1026").values.get("dropdownCode");
		final String shtAdrPrf = ctx.runtimeMap.get("DDL1026").values.get("dropdownLabel");
		final String shtAdr1 = ctx.runtimeMap.get("TXT1023").getValue();
		final String shtAdr2 = ctx.runtimeMap.get("TXT1024").getValue();
		final String shtAdr3 = ctx.runtimeMap.get("TXT1025").getValue();
		final String shtPicNm = ctx.runtimeMap.get("TXT1045").getValue();
		final String shtDptNm = ctx.runtimeMap.get("TXT1047").getValue();
		final String shtMladr = ctx.runtimeMap.get("TXT1049").getValue();
		final String shtTelNo = ctx.runtimeMap.get("TXT1051").getValue();
		final String bumonCd = ctx.runtimeMap.get("TXT1042").getValue();
		final String dltFg = "0";

		final Object[] args = {
				cntrctSts,
				cntrctNm,
				cntrctrCd,
				cntrctrDptCd,
				cntrctrDptNm,
				cntrctPrdSDt,
				cntrctPrdEDt,
				cntrctTp,
				cntrctshtFrmt,
				cntrctDt,
				sbmtrCd,
				sbmtDptCd,
				sbmtDptNm,
				sbmtDptDt,
				rtnPayTp,
				rtnPayNo,
				cntrctDtlcnd,
				cmpnOccPrs,
				trdEstAmtExctax,
				trdEstAmtRmk,
				payCycPlnTp,
				payCycPlnRmk,
				rnwPrs,
				rnwDtlcnd,
				bkbndChgTp,
				stmpAppTp,
				rmk,
				lglrCd,
				cntrctShtNm,
				cntrctCnclDt,
				cntrctDtS,
				cntrctDtE,
				rvnstmpPrs,
				StringUtils.isNotEmpty(rvnstmpAmt) ? new BigDecimal(rvnstmpAmt) : null,
				rnwMthTpAuto,
				rnwMthTpAutoInfMt,
				rnwMthTpAutoPstpMt,
				rnwMthTpNego,
				rnwMthTpNegoInfMt,
				rnwMthTpExpr,
				rnwMthTpIndf,
				rnwMthTp,
				lglRmk,
				paySiteCd,
				mnyCd,
				nCmptOblgTp,
				toApprvRmk,
				cntrctshtRqstTp,
				mailTp,
				mailRmk,
				toAdr,
				shtToZipCd,
				shtAdrPrfCd,
				shtAdrPrf,
				shtAdr1,
				shtAdr2,
				shtAdr3,
				shtPicNm,
				shtDptNm,
				shtMladr,
				shtTelNo,
				bumonCd,
				dltFg,
				corporationCode,
				userCode,
				ipAddr,
				now,
				orgnzCd,
				companyCd,
				cntrctNo,
		};
		return execSql(updateSql, args);
	}

	/** 契約先マスタテーブルをDELETE&INSERT */
	private void upsertCNTRCT_SPLR_INF(MoveActivityInstanceInParam in,  RuntimeContext ctx, String companyCd, String cntrctCd) {

		// 現登録を論理削除
		final String deleteSql = getSql("CO0001_02");
		final Object[] deleteArgs = {companyCd, cntrctCd};
		execSql(deleteSql, deleteArgs);

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

	/** 経常支払マスタテーブルをDELETE&INSERT */
	private int upsertRTN_PAY_MST(MoveActivityInstanceInParam in, RuntimeContext ctx, boolean updateFlag, String companyCd, String cntrctCd, BigDecimal rtnPayNo, PartsContainerRow splr) {

		// 現登録を物理削除
		if (updateFlag) {
			final String deleteSql = getSql("CO0002_01");
			final Object[] deleteArgs = {companyCd, cntrctCd};
			execSql(deleteSql, deleteArgs);
		}

		if (splr != null) {
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
		} else {
			return 0;
		}

	}

	/** 経常支払明細マスタテーブルをDELETE&INSERT */
	private void upsertRTN_PAYDTL_MST(MoveActivityInstanceInParam in,  RuntimeContext ctx, boolean updateFlag, String companyCd, BigDecimal rtnPayNo, PartsContainerRow splr) {

		// 現登録を物理削除
		if (updateFlag) {
			final String deleteSql = getSql("CO0002_02");
			final Object[] deleteArgs = {companyCd, rtnPayNo};
			execSql(deleteSql, deleteArgs);
		}

		if (splr != null) {
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
		} else {
			return;
		}
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

	/**
	 * JPA経由でQueryへパラメータをセット
	 * @param q Query
	 * @param params パラメータList
	 */
	protected void putParams(Query q, Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				final Object value = params[i];
				q.setParameter(i + 1, value);
			}
		}
	}

}
