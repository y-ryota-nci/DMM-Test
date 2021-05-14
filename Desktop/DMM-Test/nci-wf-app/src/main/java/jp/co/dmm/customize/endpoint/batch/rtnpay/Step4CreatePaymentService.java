package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.dmm.customize.endpoint.batch.common.BatchBaseService;
import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.RtnPaydtlMst;
import jp.co.dmm.customize.jpa.entity.mw.SplrMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.integrated_workflow.api.param.input.CreateProcessInstanceInParam;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 支払申請作成サービス
 */
@ApplicationScoped
public class Step4CreatePaymentService extends BatchBaseService {

	@Inject private RtnPayRepository repository;
	private Logger log = LoggerFactory.getLogger(Step1GetRtnpayInfoService.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public RtnPayResponse process(RtnPayRequest req) {
		final RtnPayResponse res = createResponse(RtnPayResponse.class, req);
		res.success = true;

		log.info("経常支払処理 - 支払申請作成処理を開始します。");

		// 経常支払単位で処理
		RtnPayData data = req.data;

		// 支払依頼申請作成
		for (String companyCd : data.dtlListMap.keySet()) {

			// プロセス定義コード
			String processDefCode = null;
			String screenProcessCode = null;

			if (data.targetPaymentMap.containsKey(companyCd)) {
				processDefCode = data.targetPaymentMap.get(companyCd)[0];
				screenProcessCode = data.targetPaymentMap.get(companyCd)[1];
			}

			if (processDefCode != null) {
				// プロセス定義明細コード取得
				String processDefDetailCode = repository.getProcessDefDetailCode(companyCd, processDefCode);

				// 有効なプロセス定義がある場合だけ処理を行う
				if (processDefDetailCode == null) {
					log.error("有効なプロセス定義が存在しません。:{} {}", companyCd, processDefCode);

				} else {

					// 画面プロセスID
					BigDecimal screenProcessId = repository.getScreenProcessId(companyCd, processDefCode, processDefDetailCode, screenProcessCode);

					// 有効な画面プロセスが存在する場合のみ処理を行う
					if (screenProcessId == null) {
						log.error("有効な画面プロセスが存在しません。:{} {} {} {}", companyCd, processDefCode, processDefDetailCode, screenProcessCode);

					} else {
						for (RtnPayDtl dtl : data.dtlListMap.get(companyCd)) {

							java.sql.Date targetDate = new java.sql.Date(new java.util.Date().getTime());
							Timestamp now = MiscUtils.timestamp();

							// 申請者
							WfUserRole user = repository.getWfUserRole(companyCd, dtl.rtnPayMst.getSbmtrCd());

							if (user == null) {
								log.error("申請ユーザが存在しないため申請することができません。:{} {}", companyCd, dtl.rtnPayMst.getSbmtrCd());
								continue;
							}

							if (!dtl.isPayFlg) {
								log.error("すでに支払依頼申請が行われています。:{} {}", companyCd, dtl.rtnPayMst.getId().getCntrctNo());
								continue;
							}

							// 申請者所在地
							String sbmtAddrCdNow = null;
							String sbmtAddrNmNow = null;
							String userOrganizationCode = null;
							String userPostCode = null;

							List<WfvUserBelongImpl> belongList = repository.getUserBelong(user.getCorporationCode(), user.getUserCode());

							if (belongList.size() != 0) {
								WfvUserBelongImpl targetBelong = null;

								for (WfvUserBelongImpl belong : belongList) {
									if (belong.getOrganizationCode().equals(dtl.rtnPayMst.getSbmtDptCd())) {
										targetBelong = belong;
										break;
									}
								}

								if (targetBelong == null) {
									targetBelong = belongList.get(0);
								}

								sbmtAddrCdNow = targetBelong.getExtendedInfo01();
								sbmtAddrNmNow = targetBelong.getSbmtrAddr();
								userOrganizationCode = targetBelong.getOrganizationCode();
								userPostCode = targetBelong.getPostCode();
							} else {
								log.error("申請ユーザの所属が存在しないため申請することができません。:{} {}", companyCd, dtl.rtnPayMst.getSbmtrCd());
								continue;
							}

							// 件名
							String subject = dtl.subject;

							// 取引先コード
							String splrCd = null;
							String splrNmKj = null;
							String splrNmkn = null;

							if (dtl.cntrctSplrInfList != null && dtl.cntrctSplrInfList.size() != 0) {
								splrCd = dtl.cntrctSplrInfList.get(0).getSplrCd();
								splrNmKj = dtl.cntrctSplrInfList.get(0).getSplrNmKj();
								splrNmkn = dtl.cntrctSplrInfList.get(0).getSplrNmKn();
							}

							// 申請番号
							String applicationNo = repository.getApplicationNo("PY", "YY", 29L);

							// プロセス作成
							CreateProcessInstanceInParam in = new CreateProcessInstanceInParam();
							in.setCorporationCode(companyCd);
							in.setProcessDefCode(processDefCode);
							in.setProcessDefDetailCode(processDefDetailCode);
							in.setOrganizationCodeStart(userOrganizationCode);
							in.setPostCodeStart(userPostCode);
							in.setWfUserRole(user);
							in.setSkip(false);
							in.setScreenProcessId(screenProcessId.longValue());
							in.setStartDate(targetDate);

							// インスタンス生成
							Long processId = repository.createRequest(in, subject, applicationNo, "経常支払自動生成", true);
							if (processId == null) {
								// 申請インスタンス作成最中にエラー発生されるため、処理中断
								continue;
							}

							// 支払依頼申請のランタイムID取得
							Long runtimeId = repository.nextNumber("USER_DATA","RUNTIME_ID", 1L);
							Long parentRuntimeId = -1L;
							Long sortOrder = 1L;

							// 支払予定日
							Date payPlanDate = null;

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
									cal.set(targetYear, targetMonth, 1);
									cal.add(Calendar.DATE, -1);
									targetDay = cal.get(Calendar.DATE);
								}

								Calendar requestCal = Calendar.getInstance();
								requestCal.set(targetYear, targetMonth, targetDay);
								payPlanDate = requestCal.getTime();

								String strMonth = "0" + (targetMonth + 1);
								if (strMonth.length() > 2) {
									strMonth = strMonth.substring(strMonth.length()-2);
								}
								acceptanceDate = targetYear + strMonth;
							}

							// 取引先マスタ
							SplrMst splrMst = null;
							if (splrCd != null && dtl.splrMstList != null && dtl.splrMstList.size() != 0) {
								for (SplrMst mst : dtl.splrMstList) {
									if (mst.getId().getSplrCd().equals(splrCd)) {
										splrMst = mst;
										break;
									}
								}
							}

							// 法人個人、国内・海外
							String crpPrsTpNm = null;
							String dmsAbrTpNm = null;

							if (splrMst != null) {
								if (data.codeMap.containsKey(companyCd)
									&& data.codeMap.get(companyCd).containsKey("KbnSplrNominal")
									&& data.codeMap.get(companyCd).get("KbnSplrNominal").containsKey(splrMst.getCrpPrsTp())) {
									crpPrsTpNm = data.codeMap.get(companyCd).get("KbnSplrNominal").get(splrMst.getCrpPrsTp());
								}

								if (data.codeMap.containsKey(companyCd)
									&& data.codeMap.get(companyCd).containsKey("KbnGrobal")
									&& data.codeMap.get(companyCd).get("KbnGrobal").containsKey(splrMst.getDmsAbrTp())) {
									dmsAbrTpNm = data.codeMap.get(companyCd).get("KbnGrobal").get(splrMst.getDmsAbrTp());
								}
							}

							// 振込先銀行口座マスタ
							PayeeBnkaccMst payeeBnkaccMst = null;
							if (splrCd != null && dtl.payeeBnkaccMstList != null && dtl.payeeBnkaccMstList.size() != 0) {
								for (PayeeBnkaccMst mst : dtl.payeeBnkaccMstList) {
									if (mst.getSplrCd().equals(splrCd)) {
										payeeBnkaccMst = mst;
										break;
									}
								}
							}

							// 銀行名、支店名取得
							String bnkNm = null;
							String bnkbrcNm = null;
							String bnkaccNm = null;
							String bnkaccTpNm = null;
							String payCmmObjTp = null;
							String payCmmObjTpNm = null;
							String hldTrtTp = null;
							String hldTrtTpNm = null;

							if (payeeBnkaccMst != null) {
								String[] names = repository.getBnkNames(companyCd, payeeBnkaccMst.getId().getPayeeBnkaccCd());

								bnkNm = names[0];
								bnkbrcNm = names[1];

								bnkaccNm = payeeBnkaccMst.getBnkaccNm();

								if (data.codeMap.containsKey(companyCd)
									&& data.codeMap.get(companyCd).containsKey("KbnBnkaccTp")
									&& data.codeMap.get(companyCd).get("KbnBnkaccTp").containsKey(payeeBnkaccMst.getBnkaccTp())) {
									bnkaccTpNm = data.codeMap.get(companyCd).get("KbnBnkaccTp").get(payeeBnkaccMst.getBnkaccTp());
								}

								payCmmObjTp = payeeBnkaccMst.getPayCmmOblTp();

								if (data.codeMap.containsKey(companyCd)
									&& data.codeMap.get(companyCd).containsKey("KbnSplrFeeCharge")
									&& data.codeMap.get(companyCd).get("KbnSplrFeeCharge").containsKey(payCmmObjTp)) {
									payCmmObjTpNm = data.codeMap.get(companyCd).get("KbnSplrFeeCharge").get(payCmmObjTp);
								}

								hldTrtTp = payeeBnkaccMst.getHldTrtTp();

								if (data.codeMap.containsKey(companyCd)
									&& data.codeMap.get(companyCd).containsKey("KbnSplrHoliday")
									&& data.codeMap.get(companyCd).get("KbnSplrHoliday").containsKey(hldTrtTp)) {
									hldTrtTpNm = data.codeMap.get(companyCd).get("KbnSplrHoliday").get(hldTrtTp);
								}
							}

							// 銀行口座コード
//							String bnkaccCd = null;
//
//							if (payeeBnkaccMst != null
//								&& StringUtils.isNoneEmpty(payeeBnkaccMst.getBnkCd())
//								&& StringUtils.isNoneEmpty(payeeBnkaccMst.getBnkbrcCd())
//								&& StringUtils.isNoneEmpty(payeeBnkaccMst.getBnkaccNo())) {
//								List<BnkaccMst> bnkaccMstList = repository.getBnkaccMst(companyCd, payeeBnkaccMst.getBnkCd(), payeeBnkaccMst.getBnkbrcCd(), payeeBnkaccMst.getBnkaccNo());
//
//								if (bnkaccMstList.size() != 0) {
//									bnkaccCd = bnkaccMstList.get(0).getId().getBnkaccCd();
//								}
//							}

							// 支払方法名称
							String payMthNm = null;
							String payMth = dtl.rtnPayMst.getRtnPayMth();

							if (data.codeMap.containsKey(companyCd)
								&& data.codeMap.get(companyCd).containsKey("KbnOrdPayMth")
								&& data.codeMap.get(companyCd).get("KbnOrdPayMth").containsKey(payMth)) {
								payMthNm = data.codeMap.get(companyCd).get("KbnOrdPayMth").get(payMth);
							}

							// 支払業務コード名称
							String payApplNm = null;
							if(dtl.rtnPayMst.getPayApplCd() != null
								&& data.payApplMap.containsKey(companyCd)
								&& data.payApplMap.get(companyCd).containsKey(dtl.rtnPayMst.getPayApplCd())) {
								payApplNm = data.payApplMap.get(companyCd).get(dtl.rtnPayMst.getPayApplCd());
							}

							// 消費税名称、消費税率、消費税区分
//							String taxNm = null;
							BigDecimal taxRto = null;
							String taxTp = null;
							if (dtl.rtnPayMst != null && dtl.rtnPayMst.getTaxCd() != null) {
								if (data.taxMstMap.containsKey(companyCd)
									&& data.taxMstMap.get(companyCd).containsKey(dtl.rtnPayMst.getTaxCd())) {
//									taxNm = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd()).getTaxNm();
									taxRto = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd()).getTaxRto();
									taxRto = taxRto.setScale(4, BigDecimal.ROUND_DOWN);
									taxTp = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd()).getTaxTp();
								}
							}

							// 消費税処理単位
							String taxUnitNm = null;
							if (data.codeMap.containsKey(companyCd)
								&& data.codeMap.get(companyCd).containsKey("TaxUnit")
								&& data.codeMap.get(companyCd).get("TaxUnit").containsKey(dtl.rtnPayMst.getTaxUnt())) {
								taxUnitNm = data.codeMap.get(companyCd).get("TaxUnit").get(dtl.rtnPayMst.getTaxUnt());
							}

							//税込金額
							BigDecimal totalAllPay = dtl.rtnPayMst.getMnyCd() == null || "JPY".equals(dtl.rtnPayMst.getMnyCd()) ? dtl.rtnPayMst.getRtnPayAmtJpy()  : dtl.rtnPayMst.getRtnPayAmtFc() ;

							if (totalAllPay != null && dtl.rtnPayMst.getTaxCd() != null
								&& data.taxMstMap.containsKey(companyCd)
								&& data.taxMstMap.get(companyCd).containsKey(dtl.rtnPayMst.getTaxCd())) {
								// 消費税マスタ
								TaxMst taxMst = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd());

								if (!taxMst.getTaxRto().equals(new BigDecimal("0"))
									&& StringUtils.isNotEmpty(taxMst.getFrcUnt()) && StringUtils.isNotEmpty(taxMst.getFrcTp())) {

									// 税込額
									BigDecimal srcVal = totalAllPay.multiply(taxMst.getTaxRto().divide(new BigDecimal("100")).add(new BigDecimal("1")));

//									// 四捨五入
//									if ("1".equals(taxMst.getFrcTp())) {
//										srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_HALF_UP);
//
//									// 切り捨て
//									} else if ("2".equals(taxMst.getFrcTp())) {
//										srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_DOWN);
//
//									// 切り上げ
//									} else if ("3".equals(taxMst.getFrcTp())) {
//										srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_UP);
//									}
									// TODO 画面が0桁
									// 四捨五入
									if ("1".equals(taxMst.getFrcTp())) {
										srcVal = srcVal.setScale(0, BigDecimal.ROUND_HALF_UP);

									// 切り捨て
									} else if ("2".equals(taxMst.getFrcTp())) {
										srcVal = srcVal.setScale(0, BigDecimal.ROUND_DOWN);

									// 切り上げ
									} else if ("3".equals(taxMst.getFrcTp())) {
										srcVal = srcVal.setScale(0, BigDecimal.ROUND_UP);
									}
									totalAllPay = srcVal;
								}
							}

							// 小数点桁数
							BigDecimal rdxpntGdt = null;

							if (dtl.rtnPayMst.getTaxCd() != null
								&& data.taxMstMap.containsKey(companyCd)
								&& data.taxMstMap.get(companyCd).containsKey(dtl.rtnPayMst.getTaxCd())) {
								// 消費税マスタ
								TaxMst taxMst = data.taxMstMap.get(companyCd).get(dtl.rtnPayMst.getTaxCd());

								if (StringUtils.isNotEmpty(taxMst.getFrcUnt())){
									try{
										rdxpntGdt = new BigDecimal(taxMst.getFrcUnt());
									}catch(Exception e){}
								}
							}

							// 通貨名称
							String mnyNm = null;

							if (StringUtils.isNotEmpty(dtl.rtnPayMst.getMnyCd())
								&& data.mnyMstMap.containsKey(companyCd)
								&& data.mnyMstMap.get(companyCd).containsKey(dtl.rtnPayMst.getMnyCd())) {
								mnyNm = data.mnyMstMap.get(companyCd).get(dtl.rtnPayMst.getMnyCd()).getMnyNm();
							}

							// 消費税フラグ
							String taxFg = dtl.rtnPayMst.getTaxCd() == null ? "0" : "1";
							String taxFgNm = "1".equals(taxFg) ? "税込" : ("2".equals(taxFg) ? "税抜" : ("3".equals(taxFg) ? "リバース" : null));

							// 検収申請パーツデータ生成
							List<Object> payParams = new ArrayList<Object>();

							payParams.add(runtimeId);
							payParams.add(companyCd);
							payParams.add(processId);
							payParams.add(parentRuntimeId);
							payParams.add(sortOrder);
							payParams.add("0");
							payParams.add(1L);
							payParams.add(user.getCorporationCode());
							payParams.add(user.getUserCode());
							payParams.add(now);
							payParams.add(user.getCorporationCode());
							payParams.add(user.getUserCode());
							payParams.add(now);
							payParams.add(companyCd);
							payParams.add(applicationNo); //支払No 自動採番
							payParams.add("10"); //支払ステータス
							payParams.add(null);
							payParams.add(null);
							payParams.add(dtl.cntrctInf.getId().getCntrctNo());
							payParams.add(subject);
							payParams.add(null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(user.getUserCode());
							payParams.add(dtl.cntrctInf.getCntrctrDptCd());
							payParams.add(dtl.cntrctInf.getCntrctrDptNm());
							payParams.add(data.processingDate != null ? DATE_FORMAT.format(data.processingDate) : null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(splrCd);
							payParams.add(splrNmKj);
							payParams.add(splrNmkn);
							payParams.add(splrMst != null ? splrMst.getCrpPrsTp() : null);
							payParams.add(splrMst != null ? splrMst.getDmsAbrTp() : null);
							payParams.add(payeeBnkaccMst != null ? payeeBnkaccMst.getId().getPayeeBnkaccCd() : null);
							payParams.add(payeeBnkaccMst != null ? payeeBnkaccMst.getBnkCd() : null);
							payParams.add(payeeBnkaccMst != null ? payeeBnkaccMst.getBnkbrcCd() : null);
							payParams.add(payeeBnkaccMst != null ? payeeBnkaccMst.getBnkaccTp() : null);
							payParams.add(payeeBnkaccMst != null ? payeeBnkaccMst.getBnkaccNo() : null);
							payParams.add(payeeBnkaccMst != null ? payeeBnkaccMst.getBnkaccNm() : null);
							payParams.add(payeeBnkaccMst != null ? payeeBnkaccMst.getBnkaccNmKn() : null);
							payParams.add(dtl.paySiteMst != null ? dtl.paySiteMst.getId().getPaySiteCd() : null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(acceptanceDate);
							payParams.add(acceptanceDate);
							payParams.add(acceptanceDate);
							payParams.add(data.processingDate);
							payParams.add(payPlanDate);
							payParams.add(null);
							payParams.add(dtl.rtnPayMst.getMnyCd() == null || "JPY".equals(dtl.rtnPayMst.getMnyCd()) ? dtl.rtnPayMst.getRtnPayAmtJpy() : dtl.rtnPayMst.getRtnPayAmtFc());
							payParams.add(totalAllPay);
							payParams.add(null);
							payParams.add(payMth);
							payParams.add(null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(dtl.rtnPayMst.getId().getRtnPayNo());
							payParams.add(dtl.rtnPayMst.getOrgnzCd());
							payParams.add(null);
							payParams.add(null);
							payParams.add(dtl.rtnPayMst.getBnkaccCd());
							payParams.add(dtl.rtnPayMst.getMnyTp());
							payParams.add(dtl.rtnPayMst.getMnyCd());
							payParams.add(dtl.rtnPayMst.getAddRto());
							payParams.add(dtl.rtnPayMst.getPayApplCd());
							payParams.add(null);
							payParams.add(dtl.rtnPayMst.getTaxUnt());
							payParams.add(taxFg);
							payParams.add(dtl.rtnPayMst.getTaxCd());
							payParams.add(dtl.rtnPayMst.getHldtaxTp());
							payParams.add(dtl.rtnPayMst.getHldtaxRto() != null ? dtl.rtnPayMst.getHldtaxRto().setScale(2) : null);
							payParams.add(dtl.rtnPayMst.getHldtaxSbjAmt());
							payParams.add(dtl.rtnPayMst.getHldtaxAmt());
							payParams.add(dtl.rtnPayMst.getImpTaxTp());
							payParams.add(dtl.rtnPayMst.getChrgBnkaccCd());
							payParams.add("1");
							payParams.add(null);
							payParams.add(null);
							payParams.add(user.getUserName());
							payParams.add(crpPrsTpNm);
							payParams.add(dmsAbrTpNm);
							payParams.add(bnkNm);
							payParams.add(bnkbrcNm);
							payParams.add(bnkaccTpNm);
							payParams.add(payCmmObjTp);
							payParams.add(payCmmObjTpNm);
							payParams.add(hldTrtTp);
							payParams.add(hldTrtTpNm);
							payParams.add(hldTrtTpNm);
							payParams.add(dtl.paySiteMst != null ? dtl.paySiteMst.getPaySiteNm() : null);
							payParams.add(payMthNm);
							payParams.add(null);
							payParams.add(bnkaccNm);
							payParams.add(mnyNm);
							payParams.add(payApplNm);
							payParams.add(null);
							payParams.add(taxUnitNm);
							payParams.add(taxFgNm); //TAG_FG_NM 消費税フラグ
							payParams.add(taxTp);
							payParams.add(taxRto != null ? taxRto.setScale(2) : null);
							payParams.add(dtl.hldtaxMst != null ? dtl.hldtaxMst.getHldtaxNm() : null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(null);
							payParams.add(sbmtAddrCdNow);
							payParams.add(sbmtAddrNmNow);
							payParams.add(null); //費用計上月
							payParams.add(null); //支払通知書フラグ
							payParams.add(null); //支払通知書フラグ
							payParams.add(rdxpntGdt); //小数点桁数

							repository.createPayRequest(payParams);

							// 明細ソート順
							long dtlSortOrder = 1L;

							// 発注明細パーツデータ生成
							for (RtnPaydtlMst payDtl : dtl.rtnPaydtlMstList) {

								//子明細のruntimeId
								Long childRuntimeId = repository.nextNumber("USER_DATA","RUNTIME_ID", 1L);

								//費目コード名称
								String itmexpsNm1 = null;
								String itmexpsNm2 = null;

								if (payDtl.getItmexpsCd1() != null) {
									if (data.itmexpsMap.containsKey(companyCd) && data.itmexpsMap.get(companyCd).containsKey(payDtl.getItmexpsCd1())) {
										itmexpsNm1 = data.itmexpsMap.get(companyCd).get(payDtl.getItmexpsCd1()).getItmexpsNm();
									}
								}
								if (payDtl.getItmexpsCd2() != null) {
									if (data.itmexpsMap.containsKey(companyCd) && data.itmexpsMap.get(companyCd).containsKey(payDtl.getItmexpsCd2())) {
										itmexpsNm2 = data.itmexpsMap.get(companyCd).get(payDtl.getItmexpsCd2()).getItmexpsNm();
									}
								}

								//費目関連情報
								String slpGrpGl = null;
								String cstTp = null;

								if (payDtl.getItmexpsCd1() != null && payDtl.getItmexpsCd2() != null) {
									if (data.itmexpsChrMap.containsKey(companyCd) && data.itmexpsChrMap.get(companyCd).containsKey(payDtl.getItmexpsCd1() + "-" + payDtl.getItmexpsCd2())) {
										slpGrpGl = data.itmexpsChrMap.get(companyCd).get(payDtl.getItmexpsCd1() + "-" + payDtl.getItmexpsCd2()).getSlpGrpGl();
										cstTp = data.itmexpsChrMap.get(companyCd).get(payDtl.getItmexpsCd1() + "-" + payDtl.getItmexpsCd2()).getCstTp();
									}
								}

								//品目分類
//								if (payDtl.getItmCd() != null) {
//									if (data.itmMap.containsKey(companyCd) && data.itmMap.get(companyCd).containsKey(payDtl.getItmCd())) {
//									}
//								}

								//消費税コード
//								String dtlTaxNm = null;
								String dtlTaxTp = null;
								BigDecimal dtlTaxRto = null;

								if (payDtl.getTaxCd() != null &&
									data.taxMstMap.containsKey(companyCd) &&
									data.taxMstMap.get(companyCd).containsKey(payDtl.getTaxCd())) {
//									dtlTaxNm = data.taxMstMap.get(companyCd).get(payDtl.getTaxCd()).getTaxNm();
									dtlTaxTp = data.taxMstMap.get(companyCd).get(payDtl.getTaxCd()).getTaxTp();
									dtlTaxRto = data.taxMstMap.get(companyCd).get(payDtl.getTaxCd()).getTaxRto();
									dtlTaxRto = taxRto.setScale(4, BigDecimal.ROUND_DOWN);
								}

								//税込金額
								BigDecimal allPay = payDtl.getMnyCd() == null || "JPY".equals(payDtl.getMnyCd()) ? payDtl.getPayAmtJpy() : payDtl.getPayAmtFc();

								if (allPay != null && payDtl.getTaxCd() != null) {
									// 消費税マスタ
									TaxMst taxMst = data.taxMstMap.get(companyCd).get(payDtl.getTaxCd());

									if (!taxMst.getTaxRto().equals(new BigDecimal("0"))
										&& StringUtils.isNotEmpty(taxMst.getFrcUnt()) && StringUtils.isNotEmpty(taxMst.getFrcTp())) {

										// 税込額
										BigDecimal srcVal = allPay.multiply(taxMst.getTaxRto().divide(new BigDecimal("100")).add(new BigDecimal("1")));

//										// 四捨五入
//										if ("1".equals(taxMst.getFrcTp())) {
//											srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_HALF_UP);
	//
//										// 切り捨て
//										} else if ("2".equals(taxMst.getFrcTp())) {
//											srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_DOWN);
	//
//										// 切り上げ
//										} else if ("3".equals(taxMst.getFrcTp())) {
//											srcVal = srcVal.setScale(Integer.valueOf(taxMst.getFrcUnt()), BigDecimal.ROUND_UP);
//										}
										// TODO 画面が0桁
										// 四捨五入
										if ("1".equals(taxMst.getFrcTp())) {
											srcVal = srcVal.setScale(0, BigDecimal.ROUND_HALF_UP);

										// 切り捨て
										} else if ("2".equals(taxMst.getFrcTp())) {
											srcVal = srcVal.setScale(0, BigDecimal.ROUND_DOWN);

										// 切り上げ
										} else if ("3".equals(taxMst.getFrcTp())) {
											srcVal = srcVal.setScale(0, BigDecimal.ROUND_UP);
										}
										allPay = srcVal;
									}
								}

								// 明細登録
								List<Object> payDtlParams = new ArrayList<Object>();

								payDtlParams.add(childRuntimeId);
								payDtlParams.add(companyCd);
								payDtlParams.add(processId);
								payDtlParams.add(runtimeId);
								payDtlParams.add(dtlSortOrder);
								payDtlParams.add("0");
								payDtlParams.add(1L);
								payDtlParams.add(user.getCorporationCode());
								payDtlParams.add(user.getUserCode());
								payDtlParams.add(now);
								payDtlParams.add(user.getCorporationCode());
								payDtlParams.add(user.getUserCode());
								payDtlParams.add(now);
								payDtlParams.add(companyCd);
								payDtlParams.add(applicationNo);
								payDtlParams.add(dtlSortOrder);
								payDtlParams.add(dtl.purordNo);
								payDtlParams.add(dtl.rcvinspNo);
								payDtlParams.add(dtlSortOrder);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(payDtl.getOrgnzCd());
								payDtlParams.add(payDtl.getComChgOrgnzCd());
								payDtlParams.add(payDtl.getItmexpsCd1());
								payDtlParams.add(payDtl.getItmexpsCd2());
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(payDtl.getItmCd());
								payDtlParams.add(payDtl.getItmNm());
								payDtlParams.add(payDtl.getBumonCd());
								payDtlParams.add(payDtl.getAnlysCd());
								payDtlParams.add(payDtl.getAsstTp());
								payDtlParams.add(payDtl.getPayAmtJpy());
								payDtlParams.add(null); //PAY_AMT_JPY_INCTAX 支払金額（邦貨）(税込)
								payDtlParams.add(payDtl.getMnyCd());
								payDtlParams.add(payDtl.getAddRto());
								payDtlParams.add(payDtl.getPayAmtFc());
								payDtlParams.add(payDtl.getTaxFg());
								payDtlParams.add(payDtl.getTaxCd());
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(null);
								payDtlParams.add(payDtl.getCrdcrdTp());
								payDtlParams.add(payDtl.getCrdcrdInNo());
								payDtlParams.add(payDtl.getAdvpayNo());
								payDtlParams.add(companyCd);
								payDtlParams.add(dtl.rcvinspNo);
								payDtlParams.add(itmexpsNm1);
								payDtlParams.add(itmexpsNm2);
								payDtlParams.add(payDtl.getMnyCd() == null || "JPY".equals(payDtl.getMnyCd()) ? payDtl.getPayAmtJpy() : payDtl.getPayAmtFc());
								payDtlParams.add(payDtl.getMnyCd() == null || "JPY".equals(payDtl.getMnyCd()) ? payDtl.getPayAmtJpy() : payDtl.getPayAmtFc());
								payDtlParams.add(allPay);
								payDtlParams.add(dtlTaxTp);
								payDtlParams.add(dtlTaxRto != null ? dtlTaxRto.setScale(2) : null);
								payDtlParams.add(null);
								payDtlParams.add("99999999999999999999".equals(payDtl.getItmCd()) ? "1" : "0");
								payDtlParams.add(companyCd);
								payDtlParams.add("1".equals(payDtl.getTaxFg()) ? "税込" : ("2".equals(payDtl.getTaxFg()) ? "税抜" : ("3".equals(payDtl.getTaxFg()) ? "リバース" : null)));
								payDtlParams.add(slpGrpGl);
								payDtlParams.add(cstTp);
								payDtlParams.add(null); //ADVPAY_APLY_AMT 前払充当額
								payDtlParams.add(splrCd); //SPLR_CD 取引先コード

								repository.createPayDtlRequest(payDtlParams);

								dtlSortOrder++;
							}
						}
					}
				}
			}
		}

		String errorMsg = "経常支払処理 - 支払依頼申請作成処理を終了します。";
		log.info(errorMsg);
		res.result = "SUCCESS";
		res.errorMessage = errorMsg;
		return res;
	}


}
