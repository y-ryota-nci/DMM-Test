package jp.co.dmm.customize.endpoint.batch.purord;

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

import jp.co.dmm.customize.endpoint.batch.common.BatchBaseService;
import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;

/**
 * 定期発注情報取得サービス
 */
@ApplicationScoped
public class Step1GetOrderInfoService extends BatchBaseService {

	@Inject private PurOrdRepository repository;
	private Logger log = LoggerFactory.getLogger(Step1GetOrderInfoService.class);

	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public PurOrdResponse process(PurOrdRequest req) {

		log.info("発注予約処理 - 発注予約情報取得処理を開始します。");

		// レスポンス
		final PurOrdResponse res = createResponse(PurOrdResponse.class, req);
		res.success = true;

		// 受渡し用データ経常支払情報設定
		PurOrdData data = new PurOrdData();

		// 処理日の初期値を設定（バッチの実行日時）
		if (req.processingDate != null) {
			data.processingDate = req.processingDate;
		} else {
			data.processingDate = new Date();
		}

		// 発注情報
		data.targetOrderMap = req.targetOrderMap;

		// 会社コードリスト
		List<String> companyCdList = req.companyCdList;

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

		// 処理単位として発注関連情報を初期化
		data.purOrdEntities = new ArrayList<PurOrdEntity>();

		// 実行日に応じる発注予約予定の対象情報取得
		List<PrdPurordPlnMst> planningList = repository.getPrdPurordPln(data.processingDate, companyCdList);

		for (PrdPurordPlnMst planning : planningList) {
			PurOrdEntity entity = new PurOrdEntity();
			data.purOrdEntities.add(entity);

			entity.companyCd = planning.getId().getCompanyCd();
			final String companyCd = entity.companyCd;

			// 発注予約予定情報
			entity.prdPurordPlnMst = planning;

			// 発注予約登録情報
			entity.prdPurordMst = repository.getPrdPurordMst(companyCd, planning.getPurordNo());

			// 発注情報
			entity.purordInf = repository.getPurordInf(companyCd, planning.getPurordNo());

			// 発注明細情報
			entity.purorddtlInfList = repository.getPurorddtlInf(companyCd, planning.getPurordNo());

			entity.isPreOrderFlg = repository.checkPreOrderStatus(
					companyCd,
					entity.purordInf.getCntrctNo(),
					entity.purordInf.getPurordNm(),
					data.processingDate);

			// 消費税マスタ取得
			if (StringUtils.isNotEmpty(entity.purordInf.getTaxCd())) {
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
			if (StringUtils.isNotEmpty(entity.purordInf.getPaySiteCd())) {

				entity.paySiteMst = repository.getPaySiteMst(companyCd, entity.purordInf.getPaySiteCd());
			}

			// 源泉税区分マスタ取得
			if (StringUtils.isNotEmpty(entity.purordInf.getHldtaxTp())) {

				// 源泉税区分マスタ一覧取得
				entity.hldtaxMst = repository.getHldtaxMst(companyCd, entity.purordInf.getHldtaxTp());
			}
		}

		//取得した計上支払情報をユーザデータとして設定
		req.data = data;
		res.data = data;

		String errorMsg = "発注予約処理 - 発注予約情報取得処理を終了します。";
		log.info(errorMsg);
		res.result = "SUCCESS";
		res.errorMessage = errorMsg;
		return res;
	}
}
