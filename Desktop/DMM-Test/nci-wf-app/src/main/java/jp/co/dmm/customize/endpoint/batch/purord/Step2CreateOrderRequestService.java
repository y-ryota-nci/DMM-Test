package jp.co.dmm.customize.endpoint.batch.purord;

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
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInf;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.integrated_workflow.api.param.input.CreateProcessInstanceInParam;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 発注申請作成サービス
 */
@ApplicationScoped
public class Step2CreateOrderRequestService extends BatchBaseService {

	@Inject private PurOrdRepository repository;

	private Logger log = LoggerFactory.getLogger(Step2CreateOrderRequestService.class);
	private static final SimpleDateFormat FORMAT_YEAR_MONTH = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

	/** 発注区分：定期 */
	private static final String PURORD_TP_NORMAL = "1";
	/** 定期支払ステータス:自動起票済み */
	private static final String PRD_PAY_STS_AUTO_APPLIED = "20";
	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public PurOrdResponse process(PurOrdRequest req) {
		final PurOrdResponse res = createResponse(PurOrdResponse.class, req);
		res.success = true;

		log.info("発注予約処理 - 発注申請作成処理を開始します。");

		PurOrdData data = req.data;

		for (PurOrdEntity entity : data.purOrdEntities) {
			// プロセス定義コード
			String processDefCode = null;
			String screenProcessCode = null;
			final String companyCd = entity.companyCd;
			final PurordInf order = entity.purordInf;

			if (data.targetOrderMap.containsKey(order.getId().getCompanyCd())) {
				processDefCode = data.targetOrderMap.get(entity.companyCd)[0];
				screenProcessCode = data.targetOrderMap.get(entity.companyCd)[1];
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

						/*
						 * ------------------
						 * 予定の支払申請作成
						 * -------------------
						 */

						java.sql.Date targetDate = new java.sql.Date(new java.util.Date().getTime());
						Timestamp now = MiscUtils.timestamp();

						// 申請者
						WfUserRole user = repository.getWfUserRole(companyCd, order.getSbmtrCd());

						if (user == null) {
							log.error("申請ユーザが存在しないため申請することができません。:{} {}", companyCd, order.getSbmtrCd());
							continue;
						}

						if (!entity.isPreOrderFlg) {
							log.error("すでに発注予約申請が行われています。:{} {} {} {}",
									companyCd, order.getCntrctNo(), order.getPurordNm(),
									FORMAT_DATE.format(data.processingDate));
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
								if (belong.getOrganizationCode().equals(order.getSbmtrCd())) {
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
							log.error("申請ユーザの所属が存在しないため申請することができません。:{} {}", companyCd, order.getSbmtrCd());
							continue;
						}

						// 件名
						String subject = order.getPurordNm();

						// 申請番号
						String applicationNo = repository.getApplicationNo("PO", "YY", 27L);

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
						Long processId = repository.createRequest(in, subject, applicationNo, "定期発注申請作成", false);

						// 発注申請のランタイムID取得
						Long runtimeId = repository.nextNumber("USER_DATA","RUNTIME_ID", 1L);
						Long parentRuntimeId = -1L;
						Long sortOrder = 1L;

						//納品予定日（起票日付の月末日）
						Calendar dlvPlnDtCal = Calendar.getInstance();
						dlvPlnDtCal.setTime(data.processingDate);
						dlvPlnDtCal.set(Calendar.DATE, dlvPlnDtCal.getActualMaximum(Calendar.DATE));
						Date dlvPlnDt = dlvPlnDtCal.getTime();
						String rcvInspYm = FORMAT_YEAR_MONTH.format(dlvPlnDt);

						// 支払方法名称
						String payMthNm = null;
						String payMth = order.getPayMth();

						if (data.codeMap.containsKey(companyCd)
							&& data.codeMap.get(companyCd).containsKey("KbnOrdPayMth")
							&& data.codeMap.get(companyCd).get("KbnOrdPayMth").containsKey(payMth)) {
							payMthNm = data.codeMap.get(companyCd).get("KbnOrdPayMth").get(payMth);
						}

						// 消費税名称、消費税率、消費税区分
						String taxNm = null;
						BigDecimal taxRto = null;
						String taxTp = null;
						// 小数点桁数
						BigDecimal rdxpntGdt = null;

						if (order.getTaxCd() != null) {
							if (data.taxMstMap.containsKey(companyCd)
								&& data.taxMstMap.get(companyCd).containsKey(order.getTaxCd())) {
								//消費税マスタ
								TaxMst taxMst = data.taxMstMap.get(companyCd).get(order.getTaxCd());

								taxNm = taxMst.getTaxNm();
								taxRto = taxMst.getTaxRto();
								taxRto = taxRto.setScale(4, BigDecimal.ROUND_DOWN);
								taxTp = taxMst.getTaxTp();

								if (StringUtils.isNotEmpty(taxMst.getFrcUnt())){
									try{
										rdxpntGdt = new BigDecimal(taxMst.getFrcUnt());
									}catch(Exception e){}
								}
							}
						}

						// 消費税処理単位
						String taxUntNm = null;
						if (data.codeMap.containsKey(companyCd)
							&& data.codeMap.get(companyCd).containsKey("TaxUnit")
							&& data.codeMap.get(companyCd).get("TaxUnit").containsKey(order.getTaxUnt())) {
							taxUntNm = data.codeMap.get(companyCd).get("TaxUnit").get(order.getTaxUnt());
						}

						// 支払業務コード名称
						String payApplNm = null;
						if(order.getPayApplCd() != null
							&& data.payApplMap.containsKey(companyCd)
							&& data.payApplMap.get(companyCd).containsKey(order.getPayApplCd())) {
							payApplNm = data.payApplMap.get(companyCd).get(order.getPayApplCd());
						}

						// 通貨名称
						String mnyNm = null;

						if (StringUtils.isNotEmpty(order.getMnyCd())
							&& data.mnyMstMap.containsKey(companyCd)
							&& data.mnyMstMap.get(companyCd).containsKey(order.getMnyCd())) {
							mnyNm = data.mnyMstMap.get(companyCd).get(order.getMnyCd()).getMnyNm();
						}

						// 消費税フラグ
						String taxFg = order.getTaxFg();
						String taxFgNm = "1".equals(taxFg) ? "税込" : ("2".equals(taxFg) ? "税抜" : ("3".equals(taxFg) ? "リバース" : null));

						// 支払サイト名称
						String paySiteNm = null;
						if (entity.paySiteMst != null) {
							paySiteNm = entity.paySiteMst.getPaySiteNm();
						}

						// 源泉税区分
						String hldTaxNm = null;
						if (entity.hldtaxMst != null) {
							hldTaxNm = entity.hldtaxMst.getHldtaxNm();
						}

						// 発注申請パーツデータ生成
						List<Object> orderParams = new ArrayList<Object>();

						orderParams.add(runtimeId);
						orderParams.add(companyCd);
						orderParams.add(processId);
						orderParams.add(parentRuntimeId);
						orderParams.add(sortOrder);
						orderParams.add("0");
						orderParams.add(1L);
						orderParams.add(user.getCorporationCode());
						orderParams.add(user.getUserCode());
						orderParams.add(now);
						orderParams.add(user.getCorporationCode());
						orderParams.add(user.getUserCode());
						orderParams.add(now);
						orderParams.add(applicationNo);
						orderParams.add(order.getCntrctNo());
						orderParams.add(subject);
						orderParams.add(PURORD_TP_NORMAL);
						orderParams.add("NEW");
						orderParams.add(user.getUserCode());
						orderParams.add(user.getUserName());
						orderParams.add(order.getSbmtDptCd());
						orderParams.add(order.getSbmtDptNm());
						orderParams.add(sbmtAddrCdNow);
						orderParams.add(sbmtAddrNmNow);
						orderParams.add(data.processingDate);
						orderParams.add(order.getSplrCd());
						orderParams.add(order.getSplrNmKj());
						orderParams.add(order.getSplrNmKn());
						orderParams.add(null);
						orderParams.add(null);
						orderParams.add(rcvInspYm);
						orderParams.add(rcvInspYm);
						orderParams.add(payMth);
						orderParams.add(payMthNm);
						orderParams.add(order.getPurordRqstCont());
						orderParams.add(order.getDlvLc());
						orderParams.add(order.getSprtTp());
						orderParams.add(null);//SPRT_TP_NM
						orderParams.add(order.getSprtCnt());
						orderParams.add(order.getCmptEstmTp());
						orderParams.add(null);//CMPT_ESTM_TP_NM
						orderParams.add(order.getSlctRsn());
						orderParams.add(order.getStmpTp());
						orderParams.add(null);//STMP_TP_NM
						orderParams.add(order.getPrdPurordTp());
						orderParams.add(companyCd);
						orderParams.add(order.getOrgnzCd());
						orderParams.add(order.getPaySiteCd());
						orderParams.add(paySiteNm);
						orderParams.add(order.getPayApplCd());
						orderParams.add(order.getAdvpayTp());
						orderParams.add(null);//ADVPAY_NM
						orderParams.add(null);//PURORD_RCPTSHT_NM
						orderParams.add(order.getPurordRcptshtTp());
						orderParams.add(order.getPurordRcptshtRsn());
						orderParams.add(payApplNm);
						orderParams.add(order.getTaxUnt());
						orderParams.add(taxUntNm);
						orderParams.add(order.getTaxCd());
						orderParams.add(taxNm);
						orderParams.add(order.getTaxFg());
						orderParams.add(taxTp);
						orderParams.add(taxRto);
						orderParams.add(hldTaxNm);
						orderParams.add(order.getHldtaxTp());
						orderParams.add(order.getHldtaxRto());
						orderParams.add(order.getHldtaxSbjAmt());
						orderParams.add(order.getHldtaxAmt());
						orderParams.add(order.getMnyCd());
						orderParams.add(mnyNm);
						orderParams.add(order.getPurrqstNo());
						orderParams.add(null);//IMP_TAX_NM
						orderParams.add(order.getImpTaxTp());
						orderParams.add(order.getChrgBnkaccCd());
						orderParams.add(null);//CHRG_BNKACC_NM
						orderParams.add(rdxpntGdt);
						orderParams.add(null);//IN_RTO
						orderParams.add(dlvPlnDt);
						orderParams.add(order.getInspCompDt());
						orderParams.add(order.getPayApplTp());
						orderParams.add(null);//PAY_APPL_TP_NM
						orderParams.add(taxFgNm);

						repository.createOrderRequest(orderParams);

						/*
						 * --------------------------
						 * 予定の支払申請作成（明細）
						 * --------------------------
						 */

						// 明細ソート順
						long dtlSortOrder = 1L;

						// 発注明細パーツデータ生成
						for (PurorddtlInf purordDtl : entity.purorddtlInfList) {
							//子明細のruntimeId
							Long childRuntimeId = repository.nextNumber("USER_DATA","RUNTIME_ID", 1L);

							//費目コード名称
							String itmexpsNm1 = null;
							String itmexpsNm2 = null;

							if (purordDtl.getItmexpsCd1() != null) {
								if (data.itmMap.containsKey(companyCd) &&
										data.itmMap.get(companyCd).containsKey(purordDtl.getItmexpsCd1())) {
									itmexpsNm1 = data.itmMap.get(companyCd).get(purordDtl.getItmexpsCd1()).getItmNm();
								}
							}
							if (purordDtl.getItmexpsCd2() != null) {
								if (data.itmMap.containsKey(companyCd) &&
										data.itmMap.get(companyCd).containsKey(purordDtl.getItmexpsCd2())) {
									itmexpsNm2 = data.itmMap.get(companyCd).get(purordDtl.getItmexpsCd2()).getItmNm();
								}
							}

							// 明細登録
							List<Object> orderDtlParams = new ArrayList<Object>();

							orderDtlParams.add(childRuntimeId);
							orderDtlParams.add(companyCd);
							orderDtlParams.add(processId);
							orderDtlParams.add(runtimeId);
							orderDtlParams.add(dtlSortOrder);
							orderDtlParams.add("0");
							orderDtlParams.add(1L);
							orderDtlParams.add(user.getCorporationCode());
							orderDtlParams.add(user.getUserCode());
							orderDtlParams.add(now);
							orderDtlParams.add(user.getCorporationCode());
							orderDtlParams.add(user.getUserCode());
							orderDtlParams.add(now);
							orderDtlParams.add(companyCd);
							orderDtlParams.add(purordDtl.getOrgnzCd());
							orderDtlParams.add(purordDtl.getItmexpsCd1());
							orderDtlParams.add(itmexpsNm1);
							orderDtlParams.add(purordDtl.getItmexpsCd2());
							orderDtlParams.add(itmexpsNm2);
							orderDtlParams.add(purordDtl.getItmCd());
							orderDtlParams.add(purordDtl.getItmNm());
							orderDtlParams.add(null);//IPT_NM_FG
							orderDtlParams.add(purordDtl.getBumonCd());
							orderDtlParams.add(purordDtl.getAnlysCd());
							orderDtlParams.add(purordDtl.getAsstTp());
							orderDtlParams.add(purordDtl.getPurordAmtJpy());
							orderDtlParams.add(purordDtl.getPurordAmtJpy());
							orderDtlParams.add(purordDtl.getPurordAmtJpyInctax());
							orderDtlParams.add(purordDtl.getTaxCd());
							orderDtlParams.add(taxNm);
							orderDtlParams.add(taxTp);
							orderDtlParams.add(taxRto);
							orderDtlParams.add(purordDtl.getPurordSmry());
							orderDtlParams.add(purordDtl.getTaxFg());
							orderDtlParams.add(purordDtl.getInvCompanyCd());
							orderDtlParams.add(null);//INV_COMPANY_NM
							orderDtlParams.add(taxFgNm);
							orderDtlParams.add(null);//SLP_GRP_GL
							orderDtlParams.add(null);//CST_TP

							repository.createOrderDtlRequest(orderDtlParams);

							dtlSortOrder++;
						}

						/*
						 * --------------------------
						 * 定期起票日実績を設定
						 * --------------------------
						 */
						List<Object> params = new ArrayList<Object>();
						params.add(targetDate);
						params.add(PRD_PAY_STS_AUTO_APPLIED);
						params.add(user.getCorporationCode());
						params.add(user.getUserCode());
						params.add(user.getIpAddress());
						params.add(now);
						params.add(entity.prdPurordPlnMst.getId().getCompanyCd());
						params.add(entity.prdPurordPlnMst.getId().getPrdPurordNo());
						params.add(entity.prdPurordPlnMst.getId().getSqno());

						repository.updatePrdPurordPlnMst(params);
					}
				}
			}
		}

		String errorMsg = "発注予約処理 - 発注申請作成処理を終了します。";
		log.info(errorMsg);
		res.result = "SUCCESS";
		res.errorMessage = errorMsg;
		return res;
	}
}
