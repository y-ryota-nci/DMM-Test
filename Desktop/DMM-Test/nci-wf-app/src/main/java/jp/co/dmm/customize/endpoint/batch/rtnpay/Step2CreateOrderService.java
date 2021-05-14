package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.dmm.customize.endpoint.batch.common.BatchBaseService;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.PurordInfPK;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInf;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInfPK;
import jp.co.dmm.customize.jpa.entity.mw.RtnPaydtlMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 発注マスタ作成サービス
 */
@ApplicationScoped
public class Step2CreateOrderService extends BatchBaseService {

	@Inject private RtnPayRepository repository;
	private Logger log = LoggerFactory.getLogger(Step1GetRtnpayInfoService.class);
	private static final String DATE_FORMAT = "yyyyMMdd";

	/** 発注ステータス：検収済み */
	private static String PURORD_STS_ACCEPTED = "20";
	/** 発注区分：経費 */
	private static String PURORD_TP_EXP = "4";
	/** 印鑑種別：不要 */
	private static String SMTP_TP_NO_NEED = "9";
	/** 前払区分 */
	private static String ADVPAY_TP_NO = "0";

	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public RtnPayResponse process(RtnPayRequest req) {
		final RtnPayResponse res = createResponse(RtnPayResponse.class, req);
		res.success = true;

		log.info("経常支払処理 - 発注情報作成処理を開始します。");

		// 経常支払単位で処理
		RtnPayData data = req.data;

		// 発注情報作成
		for (String companyCd : data.dtlListMap.keySet()) {

			for (RtnPayDtl dtl : data.dtlListMap.get(companyCd)) {

				// 発注対象かどうか
				if (!dtl.isPurcharseFlg) {
					log.error("すでに発注申請がおこなわれています。:{} {}", companyCd, dtl.rtnPayMst.getId().getCntrctNo());
					continue;
				}

				// 申請者
				WfUserRole user = repository.getWfUserRole(companyCd, dtl.rtnPayMst.getSbmtrCd());

				if (user == null) {
					log.error("申請ユーザが存在しません。:{} {}", companyCd, dtl.rtnPayMst.getSbmtrCd());
					user = new WfUserRoleImpl();
				}

				// 発注番号
				String purordNo = repository.getApplicationNo("PO", "YY", 27L);
				dtl.purordNo = purordNo;

				// 発注件名
				String purordNm = dtl.subject;

				// 検収月
				String acceptanceDate = null;
				Date acceptanceDateTrunc = null;
				BigDecimal intRto = null;

				// 支払予定日
				Date payPlnDate = null;

				int targetYear;
				int targetMonth;
				targetYear = Integer.valueOf(data.targetDate.substring(0, 4));
				targetMonth = Integer.valueOf(data.targetDate.substring(4));

				if (dtl.paySiteMst != null && dtl.paySiteMst.getPaySiteN() != null) {

					int targetDay = Integer.valueOf(dtl.paySiteMst.getPaySiteN());

					// 99の場合は月末
					if (targetDay == 99) {
						Calendar cal = Calendar.getInstance();
						cal.set(targetYear, targetMonth, 1);
						cal.add(Calendar.DATE, -1);
						targetDay = cal.get(Calendar.DATE);
					}

					Calendar requestCal = Calendar.getInstance();
					requestCal.set(targetYear, targetMonth, targetDay);

					String strMonth = "0" + (targetMonth + 1);
					if (strMonth.length() > 2) {
						strMonth = strMonth.substring(strMonth.length()-2);
					}
					acceptanceDate = targetYear + strMonth;
					try {
						acceptanceDateTrunc = DateUtils.parseDate(acceptanceDate + "01", DATE_FORMAT);
					} catch (ParseException e) {}
					intRto = repository.getInRate(companyCd, dtl.rtnPayMst.getMnyCd(), acceptanceDateTrunc);

					if (dtl.paySiteMst.getPaySiteM() != null) {
						int paySiteMonth = Integer.valueOf(dtl.paySiteMst.getPaySiteM());

						int targetMonthAjust = paySiteMonth;
						switch (paySiteMonth) {
						case 3:
							targetMonthAjust = -1;
							break;
						case 4:
							targetMonthAjust = 0;
							break;

						default:
							break;
						}

						Calendar payPldCal = Calendar.getInstance();
						payPldCal.set(targetYear, targetMonth, targetDay);
						payPldCal.add(Calendar.MONTH, targetMonthAjust);
						payPlnDate = payPldCal.getTime();
					}
				}

				// 取引先コード
				String splrCd = null;
				String splrNmKj = null;
				String splrNmkn = null;

				if (dtl.cntrctSplrInfList != null && dtl.cntrctSplrInfList.size() != 0) {
					splrCd = dtl.cntrctSplrInfList.get(0).getSplrCd();
					splrNmKj = dtl.cntrctSplrInfList.get(0).getSplrNmKj();
					splrNmkn = dtl.cntrctSplrInfList.get(0).getSplrNmKn();
				}

				// 支払方法名称
				String payMth = dtl.rtnPayMst.getRtnPayMth();

//				BigDecimal intRto = repository.getInRate(companyCd, dtl.rtnPayMst.getMnyCd(), acceptanceDateTrunc);

				Timestamp now = MiscUtils.timestamp();

				PurordInfPK pk = new PurordInfPK();
				pk.setCompanyCd(companyCd);
				pk.setPurordNo(purordNo);

				PurordInf order = new PurordInf();
				order.setId(pk);
				order.setPurordSts(PURORD_STS_ACCEPTED);
				order.setCntrctNo(dtl.rtnPayMst.getId().getCntrctNo());
				order.setPurordNm(purordNm);
				order.setPurordTp(PURORD_TP_EXP);
				order.setSbmtrCd(dtl.cntrctInf.getCntrctrCd());
				order.setSbmtDptCd(dtl.cntrctInf.getCntrctrDptCd());
				order.setSbmtDptNm(dtl.cntrctInf.getCntrctrDptNm());
				order.setSbmtDptDt(data.processingDate);
				order.setSplrCd(splrCd);
				order.setSplrNmKj(splrNmKj);
				order.setSplrNmKn(splrNmkn);

//				//発注金額（税抜）
//				if ("JPY".equals(dtl.rtnPayMst.getMnyTp()) || dtl.rtnPayMst.getMnyTp() == null) {
//					order.setPurordAmtExctax(dtl.rtnPayMst.getRtnPayAmtJpy());
//				} else {
//					order.setPurordAmtExctax(dtl.rtnPayMst.getRtnPayAmtFc());
//				}
//
//				//発注金額（税込）
//				if (order.getPurordAmtExctax() != null && dtl.rtnPayMst.getTaxCd() != null) {
//					// 消費税マスタ
//					TaxMst taxMst = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd());
//
//					if (!taxMst.getTaxRto().equals(new BigDecimal("0"))
//						&& StringUtils.isNotEmpty(taxMst.getFrcUnt()) && StringUtils.isNotEmpty(taxMst.getFrcTp())) {
//
//						// 税込額
//						BigDecimal srcVal = order.getPurordAmtExctax().multiply(taxMst.getTaxRto().divide(new BigDecimal("100")).add(new BigDecimal("1")));
//
//						// 四捨五入
//						if ("1".equals(taxMst.getFrcTp())) {
//							srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_HALF_UP);
//
//						// 切り捨て
//						} else if ("2".equals(taxMst.getFrcTp())) {
//							srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_DOWN);
//
//						// 切り上げ
//						} else if ("3".equals(taxMst.getFrcTp())) {
//							srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_UP);
//						}
//
//						order.setPurordAmtInctax(srcVal);
//					} else {
//						order.setPurordAmtInctax(order.getPurordAmtExctax());
//					}
//
//				} else {
//					order.setPurordAmtInctax(order.getPurordAmtExctax());
//				}

				order.setPurordRqstDt(data.processingDate);
				order.setDlvPlnDt(data.processingDate);
				order.setPayPlnDt(payPlnDate);
				order.setRcvinspYm(acceptanceDate);
				order.setRcvinspYmS(acceptanceDate);
				order.setRcvinspYmE(acceptanceDate);

				order.setDlvDt(data.processingDate);
				order.setPayMth(payMth);

				// 納入場所 DLV_LC 2回目以降は前回の場所
				if (dtl.previousPurordInf != null) {
					order.setDlvLc(dtl.previousPurordInf.getDlvLc());

				}
				order.setMnyCd(dtl.rtnPayMst.getMnyCd());
				order.setAddRto(intRto);
				order.setStmpTp(SMTP_TP_NO_NEED);
				order.setRtnPayNo(BigDecimal.valueOf(dtl.rtnPayMst.getId().getRtnPayNo()));

				// ５階層目のチーム取得
				if (StringUtils.isNotEmpty(dtl.cntrctInf.getCntrctrDptCd())) {
					String[] teamOrg = repository.getTeamOrganization(dtl.cntrctInf.getId().getCompanyCd(), dtl.cntrctInf.getCntrctrDptCd());
					order.setOrgnzCd(teamOrg[0]);
				}

				order.setPaySiteCd(dtl.rtnPayMst.getPaySiteCd());
				order.setPayApplCd(dtl.rtnPayMst.getPayApplCd());
				order.setAdvpayTp(ADVPAY_TP_NO);
				order.setTaxUnt(dtl.rtnPayMst.getTaxUnt());

				String taxFg = "0";

				if (StringUtils.isNoneEmpty(dtl.rtnPayMst.getTaxCd())
					&& data.taxMstMap.containsKey(companyCd)
					&& data.taxMstMap.get(companyCd).containsKey(dtl.rtnPayMst.getTaxCd())) {

					TaxMst taxMst = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd());

					if ("1".equals(taxMst.getTaxTp())) {
						taxFg = "2";

					} else if ("2".equals(taxMst.getTaxTp())) {
						taxFg = "1";
					}
				}

				order.setTaxFg(taxFg);

				order.setTaxCd(dtl.rtnPayMst.getTaxCd());
				order.setHldtaxTp(dtl.rtnPayMst.getHldtaxTp());
				order.setHldtaxRto(dtl.rtnPayMst.getHldtaxRto());
				order.setHldtaxAmt(dtl.rtnPayMst.getHldtaxAmt());
				order.setHldtaxSbjAmt(dtl.rtnPayMst.getHldtaxSbjAmt());
				order.setDltFg(DeleteFlag.OFF);
				order.setCorporationCodeCreated(user.getCorporationCode());
				order.setUserCodeCreated(user.getUserCode());
				order.setIpCreated(user.getIpAddress());
				order.setTimestampCreated(now);
				order.setCorporationCodeUpdated(user.getCorporationCode());
				order.setUserCodeUpdated(user.getUserCode());
				order.setIpUpdated(user.getIpAddress());
				order.setTimestampUpdated(now);

				repository.createPurchaseOrder(order);

				// 明細ソート順
				long dtlSortOrder = 1L;

				// 発注明細パーツデータ生成
				for (RtnPaydtlMst payDtl : dtl.rtnPaydtlMstList) {
					log.info("経常支払処理 - 発注明細情報作成処理を開始します： " + dtlSortOrder);

					// 明細登録
					PurorddtlInfPK dtlPk = new PurorddtlInfPK();
					dtlPk.setCompanyCd(companyCd);
					dtlPk.setPurordNo(purordNo);
					dtlPk.setPurordDtlNo(dtlSortOrder);

					PurorddtlInf orderDetail = new PurorddtlInf();
					orderDetail.setId(dtlPk);
					orderDetail.setOrgnzCd(order.getOrgnzCd());
					orderDetail.setItmexpsCd1(payDtl.getItmexpsCd1());
					orderDetail.setItmexpsCd2(payDtl.getItmexpsCd2());
					orderDetail.setItmCd(payDtl.getItmCd());
					orderDetail.setItmNm(payDtl.getItmNm());
					orderDetail.setBumonCd(payDtl.getBumonCd());
					orderDetail.setAnlysCd(payDtl.getAnlysCd());
					orderDetail.setAsstTp(payDtl.getAsstTp());
					orderDetail.setPurordAmtJpy(payDtl.getPayAmtJpy());
					orderDetail.setMnyCd(payDtl.getMnyCd());
					orderDetail.setPurordAmtFc(payDtl.getPayAmtFc());
					orderDetail.setTaxFg(payDtl.getTaxFg());
					orderDetail.setTaxCd(payDtl.getTaxCd());
					orderDetail.setPurordSmry(payDtl.getPaySmry());
					orderDetail.setApplCont(payDtl.getApplCont());
					orderDetail.setDltFg(DeleteFlag.OFF);
					orderDetail.setCorporationCodeCreated(user.getCorporationCode());
					orderDetail.setUserCodeCreated(user.getUserCode());
					orderDetail.setIpCreated(user.getIpAddress());
					orderDetail.setTimestampCreated(now);
					orderDetail.setCorporationCodeUpdated(user.getCorporationCode());
					orderDetail.setUserCodeUpdated(user.getUserCode());
					orderDetail.setIpUpdated(user.getIpAddress());
					orderDetail.setTimestampUpdated(now);

					repository.createPurchaseOrderDetail(orderDetail);

					log.info("経常支払処理 - 発注明細情報作成処理を終了します： " + dtlSortOrder);

					dtlSortOrder++;
				}
			}
		}

		String errorMsg = "経常支払処理 - 発注情報作成処理を終了します。";
		log.info(errorMsg);
		res.result = "SUCCESS";
		res.errorMessage = errorMsg;
		return res;
	}


}
