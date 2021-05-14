package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.dmm.customize.endpoint.batch.common.BatchBaseService;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspInfPK;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInfPK;
import jp.co.dmm.customize.jpa.entity.mw.RtnPaydtlMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 検収マスタ作成サービス
 */
@ApplicationScoped
public class Step3CreateAcceptanceService extends BatchBaseService {

	@Inject private RtnPayRepository repository;
	private Logger log = LoggerFactory.getLogger(Step1GetRtnpayInfoService.class);

	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public RtnPayResponse process(RtnPayRequest req) {
		final RtnPayResponse res = createResponse(RtnPayResponse.class, req);
		res.success = true;

		log.info("経常支払処理 - 検収情報作成処理を開始します。");

		// 経常支払単位で処理
		RtnPayData data = req.data;

		// 検収申請作成
		for (String companyCd : data.dtlListMap.keySet()) {

			for (RtnPayDtl dtl : data.dtlListMap.get(companyCd)) {

				// 検収対象かどうか
				if (!dtl.isRcvinspFlg) {
					log.error("すでに検収申請が行われています。:{} {}", companyCd, dtl.rtnPayMst.getId().getCntrctNo());
					continue;
				}

				if (!dtl.isPurcharseFlg) {
					log.error("すでに発注申請が行われており、発注情報が未作成であるため検収情報を作成することはできません。:{} {}", companyCd, dtl.rtnPayMst.getId().getCntrctNo());
					continue;
				}

				Timestamp now = MiscUtils.timestamp();

				// 申請者
				WfUserRole user = repository.getWfUserRole(companyCd, dtl.rtnPayMst.getSbmtrCd());

				// 申請者所在地
				if (user == null) {
					log.error("申請ユーザが存在しません。:{} {}", companyCd, dtl.rtnPayMst.getSbmtrCd());
					user = new WfUserRoleImpl();
				}

				// 件名
				String subject = dtl.subject;

				// 申請番号
				String applicationNo = repository.getApplicationNo("RI", "YY", 28L);
				dtl.rcvinspNo = applicationNo;

				// 検収月
				String acceptanceDate = null;

				if (dtl.paySiteMst != null && dtl.paySiteMst.getPaySiteN() != null) {
					int targetYear;
					int targetMonth;
					int targetDay = Integer.valueOf(dtl.paySiteMst.getPaySiteN());


					targetYear = Integer.valueOf(data.targetDate.substring(0, 4));
					targetMonth = Integer.valueOf(data.targetDate.substring(4));

					// 99の場合は月末
					if (targetDay == 99) {
						Calendar cal = Calendar.getInstance();
						cal.set(targetYear, targetMonth-1, 1);
						cal.add(Calendar.MONTH, 1);
						cal.add(Calendar.DATE, -1);
						targetDay = cal.get(Calendar.DATE);
					}

					String strMonth = "0" + (targetMonth + 1);
					if (strMonth.length() > 2) {
						strMonth = strMonth.substring(strMonth.length()-2);
					}
					acceptanceDate = targetYear + strMonth;
				}

				// 取引先コード
				String splrCd = null;
				String splrNmKj = null;
				String splrNmKn = null;

				if (dtl.cntrctSplrInfList != null && dtl.cntrctSplrInfList.size() != 0) {
					splrCd = dtl.cntrctSplrInfList.get(0).getSplrCd();
					splrNmKj = dtl.cntrctSplrInfList.get(0).getSplrNmKj();
					splrNmKn = dtl.cntrctSplrInfList.get(0).getSplrNmKn();
				}

				// 検収情報
				RcvinspInfPK pk = new RcvinspInfPK();
				pk.setCompanyCd(companyCd);
				pk.setRcvinspNo(applicationNo);

				RcvinspInf inf = new RcvinspInf();
				inf.setId(pk);
				inf.setRcvinspSts("10");
				inf.setBuyNoRRcnt(null);
				inf.setBuyNoBRcnt(null);
				inf.setRcvinspNm(subject);
				inf.setPurordTp("1");
				inf.setSbmtrCd(user.getUserCode());
				inf.setSbmtDptCd(dtl.cntrctInf.getCntrctrDptCd());
				inf.setSbmtDptNm(dtl.cntrctInf.getCntrctrDptNm());
				inf.setSbmtDptDt(data.processingDate);
				inf.setSplrCd(splrCd);
				inf.setSplrNmKj(splrNmKj);
				inf.setSplrNmKn(splrNmKn);

				//検収金額（税抜）
				if ("JPY".equals(dtl.rtnPayMst.getMnyTp()) || dtl.rtnPayMst.getMnyTp() == null) {
					inf.setRcvinspAmtExctax(dtl.rtnPayMst.getRtnPayAmtJpy());
				} else {
					inf.setRcvinspAmtExctax(dtl.rtnPayMst.getRtnPayAmtFc());
				}

				// 消費税マスタ
				TaxMst taxMst = null;

				//検収金額（税込）
				if (inf.getRcvinspAmtExctax() != null && dtl.rtnPayMst.getTaxCd() != null) {

					taxMst = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd());

					if (!taxMst.getTaxRto().equals(new BigDecimal("0"))
						&& StringUtils.isNotEmpty(taxMst.getFrcUnt()) && StringUtils.isNotEmpty(taxMst.getFrcTp())) {

						// 税込額
						BigDecimal srcVal = inf.getRcvinspAmtExctax().multiply(taxMst.getTaxRto().divide(new BigDecimal("100")).add(new BigDecimal("1")));

						// 四捨五入
						if ("1".equals(taxMst.getFrcTp())) {
							srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_HALF_UP);

						// 切り捨て
						} else if ("2".equals(taxMst.getFrcTp())) {
							srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_DOWN);

						// 切り上げ
						} else if ("3".equals(taxMst.getFrcTp())) {
							srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_UP);
						}

						inf.setRcvinspAmtInctax(srcVal);
					} else {
						inf.setRcvinspAmtInctax(inf.getRcvinspAmtExctax());
					}

				} else {
					inf.setRcvinspAmtInctax(inf.getRcvinspAmtExctax());
				}

				inf.setRcvinspYmS(acceptanceDate);
				inf.setRcvinspYmE(acceptanceDate);
				inf.setRcvinspYm(acceptanceDate);
				inf.setPayMth(dtl.rtnPayMst.getRtnPayMth());
				inf.setRmk(null);
				inf.setMnyTp(dtl.rtnPayMst.getMnyTp() == null ? "1" : dtl.rtnPayMst.getMnyTp());
				inf.setMnyCd(dtl.rtnPayMst.getMnyCd());
				inf.setAddRto(dtl.rtnPayMst.getAddRto());
				inf.setRtnPayNo(new BigDecimal(dtl.rtnPayMst.getId().getRtnPayNo()));
				inf.setOrgnzCd(dtl.rtnPayMst.getOrgnzCd() == null ? dtl.cntrctInf.getCntrctrDptCd() : dtl.rtnPayMst.getOrgnzCd());
				inf.setPaySiteCd(dtl.rtnPayMst.getPaySiteCd());
				inf.setPayApplCd(dtl.rtnPayMst.getPayApplCd());
				inf.setTaxUnt(dtl.rtnPayMst.getTaxUnt());
				inf.setTaxFg(taxMst != null && ("1".equals(taxMst.getTaxTp()) || "2".equals(taxMst.getTaxTp())) ? taxMst.getTaxTp() : null);
				inf.setTaxCd(dtl.rtnPayMst.getTaxCd());
				inf.setHldtaxTp(dtl.rtnPayMst.getHldtaxTp());
				inf.setHldtaxRto(dtl.rtnPayMst.getHldtaxRto());
				inf.setHldtaxSbjAmt(dtl.rtnPayMst.getHldtaxSbjAmt());
				inf.setHldtaxAmt(dtl.rtnPayMst.getHldtaxAmt());
				inf.setImpTaxTp(dtl.rtnPayMst.getImpTaxTp());
				inf.setChrgBnkaccCd(null);
				inf.setPayApplTp(null);
				inf.setDltFg(DeleteFlag.OFF);
				inf.setCorporationCodeCreated(user.getCorporationCode());
				inf.setUserCodeCreated(user.getUserCode());
				inf.setIpCreated(user.getIpAddress());
				inf.setTimestampCreated(now);
				inf.setCorporationCodeUpdated(user.getCorporationCode());
				inf.setUserCodeUpdated(user.getUserCode());
				inf.setIpUpdated(user.getIpAddress());
				inf.setTimestampUpdated(now);

				repository.createRcvinsp(inf);

				// 明細ソート順
				long dtlSortOrder = 1L;

				// 発注明細パーツデータ生成
				for (RtnPaydtlMst payDtl : dtl.rtnPaydtlMstList) {

					// 明細登録
					RcvinspdtlInfPK id = new RcvinspdtlInfPK();
					id.setCompanyCd(companyCd);
					id.setRcvinspNo(applicationNo);
					id.setRcvinspDtlNo(dtlSortOrder);

					RcvinspdtlInf infDtl = new RcvinspdtlInf();
					infDtl.setId(id);
					infDtl.setPurordNo(dtl.purordNo);
					infDtl.setPurordDtlNo(new BigDecimal(dtlSortOrder));
					infDtl.setOrgnzCd(payDtl.getOrgnzCd());
					infDtl.setComChgOrgnzCd(payDtl.getComChgOrgnzCd());
					infDtl.setItmexpsCd1(payDtl.getItmexpsCd1());
					infDtl.setItmexpsCd2(payDtl.getItmexpsCd2());
					infDtl.setItmCd(payDtl.getItmCd());
					infDtl.setItmNm(payDtl.getItmNm());
					infDtl.setBumonCd(payDtl.getBumonCd());
					infDtl.setAnlysCd(payDtl.getAnlysCd());
					infDtl.setAsstTp(payDtl.getAsstTp());
					infDtl.setPurordQnt(null);
					infDtl.setRcvinspQnt(null);
					infDtl.setUntCd(null);
					infDtl.setMnyCd(payDtl.getMnyCd());
					infDtl.setAddRto(payDtl.getAddRto());
					infDtl.setPurordAmtJpy(payDtl.getPayAmtJpy());
					infDtl.setPurordAmtFc(payDtl.getPayAmtFc());
					infDtl.setTaxFg(payDtl.getTaxFg());
					infDtl.setTaxCd(payDtl.getTaxCd());
					infDtl.setHldtaxTp(dtl.rtnPayMst.getHldtaxTp());
					infDtl.setDltFg(DeleteFlag.OFF);
					infDtl.setCorporationCodeCreated(user.getCorporationCode());
					infDtl.setUserCodeCreated(user.getUserCode());
					infDtl.setIpCreated(user.getIpAddress());
					infDtl.setTimestampCreated(now);
					infDtl.setCorporationCodeUpdated(user.getCorporationCode());
					infDtl.setUserCodeUpdated(user.getUserCode());
					infDtl.setIpUpdated(user.getIpAddress());
					infDtl.setTimestampUpdated(now);

					repository.createRcvinspDetail(infDtl);

					dtlSortOrder++;
				}
			}
		}

		String errorMsg = "経常支払処理 - 検収情報作成処理を終了します。";
		log.info(errorMsg);
		res.result = "SUCCESS";
		res.errorMessage = errorMsg;
		return res;
	}
}
