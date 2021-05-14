package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.dmm.customize.jpa.entity.mw.CntrctInf;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.RtnPayMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 経常支払情報取得サービス
 */
@ApplicationScoped
public class Step1GetRtnpayInfoService extends BaseService {

	@Inject private RtnPayRepository repository;
	private Logger log = LoggerFactory.getLogger(Step1GetRtnpayInfoService.class);
	private static final SimpleDateFormat SUBJECT_FORMAT = new SimpleDateFormat("yyMMdd");

	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public RtnPayResponse process(RtnPayRequest req) {

		log.info("経常支払処理 - 経常支払情報取得処理を開始します。");

		// レスポンス
		final RtnPayResponse res = createResponse(RtnPayResponse.class, req);
		res.success = true;

		// 受渡し用データ経常支払情報設定
		RtnPayData data = new RtnPayData();

		// 処理日の初期値を設定（バッチの実行日時）
		data.processingDate = new Date();

		// 対象年月
		String targetDate = req.targetDate;
		data.targetDate = targetDate;
		Long targetYearMonth = Long.valueOf(data.targetDate);

		// 起票者情報
		data.organizationCodeStart = req.organizationCodeStart;
		data.postCodeStart = req.postCodeStart;

		// 会社コードリスト
		List<String> companyCdList = req.companyCdList;

		data.targetPaymentMap = req.targetPaymentMap;

		// 消費税マップ
		Map<String, Map<String, TaxMst>> taxMstMap = new HashMap<String, Map<String, TaxMst>>();
		data.taxMstMap = taxMstMap;

		// 費目マップ
		Map<String, Map<String, ItmexpsMst>> itmexpsMap = new HashMap<String, Map<String, ItmexpsMst>>();
		data.itmexpsMap = itmexpsMap;

		// 費目関連マスタマップ
		Map<String, Map<String, Itmexps1Chrmst>> itmexpsChrMap = new HashMap<String, Map<String, Itmexps1Chrmst>>();
		data.itmexpsChrMap = itmexpsChrMap;

		// 品目マップ
		Map<String, Map<String, ItmMst>> itmMap = new HashMap<String, Map<String, ItmMst>>();
		data.itmMap = itmMap;

		// 支払業務コードマップ
		Map<String, Map<String, String>> payApplMap = new HashMap<String, Map<String, String>>();
		data.payApplMap = payApplMap;

		// 通貨マスタマップ
		Map<String, Map<String, MnyMst>> mnyMstMap = new HashMap<String, Map<String, MnyMst>>();
		data.mnyMstMap = mnyMstMap;

		// コード値取得
		Map<String, Map<String, Map<String, String>>> codeMap = new HashMap<String, Map<String, Map<String, String>>>();
		data.codeMap = codeMap;

		// 経常支払一覧取得（対象の会社のものだけ取得する）
		List<RtnPayMst> results = repository.getRtnPay(targetYearMonth, companyCdList);

		log.info("経常支払処理 - 経常支払マスタ取得件数:" + results.size() + "件");

		// 一覧が取得出来ない場合は処理終了
		if (results.size() == 0) {
			String errorMsg = "経常支払処理 - 処理件数が0件のため処理を終了します。";
			log.info(errorMsg);
			res.result = "ABORTED";
			res.errorMessage = errorMsg;
			return res;
		}

		//マスタ単位で明細を取得
		Map<String, List<RtnPayDtl>> dtlListMap = new HashMap<String, List<RtnPayDtl>>();
		data.dtlListMap = dtlListMap;

		for (RtnPayMst mst : results) {

			// 会社コード
			String companyCd = mst.getId().getCompanyCd();

			if (!dtlListMap.containsKey(companyCd)) {
				dtlListMap.put(companyCd, new ArrayList<RtnPayDtl>());
			}

			List<RtnPayDtl> dtlList = dtlListMap.get(companyCd);

			// 支払対象かどうかの判定 TODO とりあえず支払開始年月と支払サイクルから判定
			boolean isTarget = false;

			// 支払開始年月からの月数
			int year = Integer.valueOf(mst.getPayStartTime().substring(0,4));
			int month = Integer.valueOf(mst.getPayStartTime().substring(4));
			int allMonth = (year * 12) + month;

			int targetYear = Integer.valueOf(targetDate.substring(0,4));
			int targetMonth = Integer.valueOf(targetDate.substring(4));
			int targetAllMonth = (targetYear * 12) + targetMonth;

			// 経過月数
			int processMonthCnt = targetAllMonth - allMonth;

			// 経常支払区分（支払サイクル）
			String rtnPayTp = mst.getRtnPayTp();

			if (StringUtils.isNotEmpty(rtnPayTp)) {
				// 支払サイクルが１ヶ月または開始年月の場合は確定
				if ("1".equals(rtnPayTp) || processMonthCnt == 0) {
					isTarget = true;
				} else {
					// 開始月からの倍数の場合も確定
					if (processMonthCnt % Integer.valueOf(rtnPayTp) == 0) {
						isTarget = true;
					}
				}
			}

			if (isTarget) {
				// マスタデータ設定
				RtnPayDtl dtl = new RtnPayDtl();
				dtl.rtnPayMst = mst;

				// 契約取得
				List<CntrctInf> cntrctresults = repository.getCntrctInf(companyCd, mst.getId().getCntrctNo());

				// 契約設定
				if (cntrctresults.size() != 0) {
					dtl.cntrctInf = cntrctresults.get(0);
				} else {
					log.error("該当の契約情報を取得することが来ません出来た。:{} {}", companyCd, mst.getId().getCntrctNo());
					continue;
				}

				// 件名
				String subject = dtl.cntrctInf.getCntrctNm();
				if (dtl.cntrctInf.getCntrctDt() != null) {
					subject += "_" + SUBJECT_FORMAT.format(dtl.cntrctInf.getCntrctDt());
				}
				dtl.subject = subject;

				// 発注
				dtl.isPurcharseFlg = repository.checkPurchaseTarget(companyCd, mst.getId().getCntrctNo(), subject);
				// 検収
				dtl.isRcvinspFlg = repository.checkRcvinspTarget(companyCd, mst.getId().getCntrctNo(), subject);
				// 支払依頼
				dtl.isPayFlg = repository.checkPayTarget(companyCd, mst.getId().getCntrctNo(), subject);

				// 経常支払回数
				dtl.processMonthCnt = processMonthCnt;

				// 明細データ取得
				dtl.rtnPaydtlMstList = repository.getRtnPayDtl(companyCd, mst.getId().getRtnPayNo());

				// 消費税マスタ取得
				if (StringUtils.isNotEmpty(mst.getTaxCd())) {
					if (!taxMstMap.containsKey(companyCd)) {

						List<TaxMst> taxresults = repository.getTaxMst(companyCd);

						Map<String, TaxMst> taxMap = new HashMap<String, TaxMst>();
						taxMstMap.put(companyCd, taxMap);

						for (TaxMst tax : taxresults) {
							taxMap.put(tax.getId().getTaxCd(), tax);
						}
					}
				}

				// 費目情報取得
				if (!itmexpsMap.containsKey(companyCd)) {
					List<ItmexpsMst> itmresults = repository.getItemexpsMst(companyCd);
					Map<String, ItmexpsMst> comItemMap = new HashMap<String, ItmexpsMst>();

					for (ItmexpsMst itm : itmresults) {
						comItemMap.put(itm.getId().getItmexpsCd(), itm);
					}
					itmexpsMap.put(companyCd, comItemMap);
				}

				// 費目関連マスタ情報取得
				if (!itmexpsChrMap.containsKey(companyCd)) {
					List<Itmexps1Chrmst> itmresults = repository.getItemexpsChrMst(companyCd);
					Map<String, Itmexps1Chrmst> comItemMap = new HashMap<String, Itmexps1Chrmst>();

					for (Itmexps1Chrmst itm : itmresults) {
						comItemMap.put(itm.getId().getItmexpsCd1() + "-" + itm.getId().getItmexpsCd2(), itm);
					}
					itmexpsChrMap.put(companyCd, comItemMap);
				}

				// 品目情報取得
				if (!itmMap.containsKey(companyCd)) {
					List<ItmMst> itmresults = repository.getItemMst(companyCd);
					Map<String, ItmMst> comItemMap = new HashMap<String, ItmMst>();

					for (ItmMst itm : itmresults) {
						comItemMap.put(itm.getId().getItmCd(), itm);
					}
					itmMap.put(companyCd, comItemMap);
				}

				// 支払業務コード取得
				if (!payApplMap.containsKey(companyCd)) {
					List<PayApplMst> applresults = repository.getPayApplMst(companyCd);
					Map<String, String> comPayApplMap = new HashMap<String, String>();

					for (PayApplMst payAppl : applresults) {
						comPayApplMap.put(payAppl.getId().getPayApplCd(), payAppl.getPayApplNm());
					}
					payApplMap.put(companyCd, comPayApplMap);
				}

				// 通貨マスタ取得
				if (!mnyMstMap.containsKey(companyCd)) {
					List<MnyMst> mnyresults = repository.getMnyMst(companyCd);
					Map<String, MnyMst> comMnyMstMap = new HashMap<String, MnyMst>();

					for (MnyMst mny : mnyresults) {
						comMnyMstMap.put(mny.getId().getMnyCd(), mny);
					}
					mnyMstMap.put(companyCd, comMnyMstMap);
				}

				// コード値取得
				if (!codeMap.containsKey(companyCd)) {
					codeMap.put(companyCd, repository.getCodeMap(companyCd));
				}

				// 支払サイト取得
				if (StringUtils.isNotEmpty(mst.getPaySiteCd())) {

					List<PaySiteMst> paysiteresults = repository.getPaySiteMst(companyCd, mst.getPaySiteCd());

					if (paysiteresults.size() != 0) {
						dtl.paySiteMst = paysiteresults.get(0);
					}
				}

				// 源泉税区分マスタ取得
				if (StringUtils.isNotEmpty(mst.getHldtaxTp())) {

					// 源泉税区分マスタ一覧取得
					List<HldtaxMst> hldtaxresults = repository.getHldtaxMst(companyCd, mst.getHldtaxTp());

					if (hldtaxresults.size() != 0) {
						dtl.hldtaxMst = hldtaxresults.get(0);
					}
				}

				// 契約先取得
				dtl.cntrctSplrInfList = repository.getCntrctSplrInf(companyCd, mst.getId().getCntrctNo());

				// 取引先コード
				String targetSplrCd = mst.getSplrCd();

				if (StringUtils.isEmpty(targetSplrCd) && dtl.cntrctSplrInfList.size() != 0) {
					targetSplrCd = dtl.cntrctSplrInfList.get(0).getSplrCd();
				}

				// 取引先取得
				dtl.splrMstList = repository.getSplrMst(companyCd, targetSplrCd);

				// 振込先銀行口座マスタ
				dtl.payeeBnkaccMstList = repository.getPayeeBnkAccMst(companyCd, targetSplrCd);

				// 契約明細取得
				//dtl.cntrctdtlInfList = repository.getCntrctdtlInf(companyCd, mst.getId().getCntrctNo());

				// 発注情報取得
				if (dtl.processMonthCnt != 0) {
					List<PurordInf> purordList = repository.getPurordInf(companyCd, mst.getId().getCntrctNo());

					if (purordList.size() != 0) {
						dtl.previousPurordInf = purordList.get(0);
					}
				}

				dtlList.add(dtl);
			}
		}

		if (dtlListMap.size() == 0) {
			String errorMsg = "経常支払処理 - 処理件数が0件のため処理を終了します。";
			log.info(errorMsg);
			res.result = "ABORTED";
			res.errorMessage = errorMsg;
			return res;
		}

		//取得した計上支払情報をユーザデータとして設定
		req.data = data;
		res.data = data;

		String errorMsg = "経常支払処理 - 経常支払情報取得処理を終了します。";
		log.info(errorMsg);
		res.result = "SUCCESS";
		res.errorMessage = errorMsg;
		return res;
	}
}
