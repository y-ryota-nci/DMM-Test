package jp.co.dmm.customize.component.screenCustomize;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.po.po0010.Po0010Service;
import jp.co.dmm.customize.endpoint.ri.RcvinspInfService;
import jp.co.dmm.customize.endpoint.vd.vd0310.DmmCustomService;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignDropdown;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRadio;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsMaster;
import jp.co.nci.iwf.designer.parts.runtime.PartsRadio;
import jp.co.nci.iwf.designer.service.PartsMasterService;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * WF検収の画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0049ScreenCustomize extends DmmScreenCustomize {
	@Inject private Po0010Service service;
	/** DMM検収共通サービス */
	@Inject private RcvinspInfService rcvinspInfService;
	/** マスタ選択パーツで、汎用マスタの検索結果を他パーツへばらまき処理を行うためのサービス */
	@Inject private PartsMasterService partsMasterService;
	@Inject private DmmCustomService dmmCustomService;

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		if (in(req.trayType, TrayType.NEW, TrayType.WORKLIST)) {
			if (req.processId == null) {
				// 起案担当者情報
				final UserInfo startUserInfo = res.contents.startUserInfo;
				ctx.runtimeMap.get("TXT0068").setValue(startUserInfo.getCorporationCode());	// 会社コード
				ctx.runtimeMap.get("TXT0053").setValue(startUserInfo.getUserCode());	// 申請者コード
				ctx.runtimeMap.get("TXT0054").setValue(startUserInfo.getUserName());	// 申請者
				ctx.runtimeMap.get("TXT0055").setValue(startUserInfo.getOrganizationCode());
				ctx.runtimeMap.get("TXT0058").setValue(startUserInfo.getOrganizationName());
				ctx.runtimeMap.get("TXT0057").setValue(startUserInfo.getExtendedInfo01()); 	// 所在地コード
				ctx.runtimeMap.get("TXT0066").setValue(startUserInfo.getSbmtrAddr());		// 所在地
				ctx.runtimeMap.get("TXT0079").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
				// 明細の起案担当者情報
				PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0059");
				for (PartsContainerRow row : grid.rows) {
					String prefix = grid.htmlId + "-" + row.rowId + "_";
					ctx.runtimeMap.get(prefix + "TXT0070").setValue(startUserInfo.getCorporationCode());	// 会社コード
					ctx.runtimeMap.get(prefix + "TXT0062").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
				}
				// 支払業務コードと名称には申請者の所属組織の支払業務コード（（組織マスタ）.[拡張情報01]）を設定
				String companyCd = startUserInfo.getCorporationCode();
				String payApplCd = startUserInfo.getPayApplCd();
				PayApplMst pam = service.getPayApplMst(companyCd, payApplCd);
				if (pam != null) {
					ctx.runtimeMap.get("TXT0098").setValue(pam.getId().getPayApplCd());
					ctx.runtimeMap.get("TXT0099").setValue(pam.getPayApplNm());
				}
			}
			// 申請日：申請するまではシステム日付、申請後は申請日
			PartsBase<?> applyDate = ctx.runtimeMap.get("TXT0056");
			if (res.contents.applicationDate == null) {
				applyDate.setValue(toStr(today())); // 申請日
			} else {
				applyDate.setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
			}

			// 社内レート基準日：費用計上月の末日（費用計上月が未設定ならシステム日付の末日）
			final PartsBase<?> $cstAddYm = ctx.runtimeMap.get("TXT0131");	// 検収予定日
			final PartsBase<?> $inRtoBaseDt = ctx.runtimeMap.get("TXT0135");
			if (eq($cstAddYm.dcType, DcType.INPUTABLE) && eq($inRtoBaseDt.dcType, DcType.INPUTABLE)) {
				if (!isAdvpaySetting(ctx.runtimeMap)) {
					final Date cstAddDt = isEmpty($cstAddYm.getValue()) ? today() : toDate($cstAddYm.getValue() + "/01", "yyyy/MM/dd");
					final Date endOfLastM = dmmCustomService.getRtoBaseDt(cstAddDt);
					$inRtoBaseDt.setValue(toStr(endOfLastM, "yyyy/MM/dd"));
				}

				// 通貨コードに対応した社内レートと小数点桁数を最新のマスタから反映
				final PartsMaster currency = (PartsMaster)ctx.runtimeMap.get("MST0104");
				if (in(currency.dcType, DcType.INPUTABLE, DcType.UKNOWN)) {
					partsMasterService.distributeMasterValues(currency, ctx);
				}
			}
		}
	}

	protected boolean isAdvpaySetting(Map<String, PartsBase<?>> runtimeMap) {
		PartsGrid grid = (PartsGrid)runtimeMap.get("GRD0059");
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			final PartsBase<?> advpayNo = runtimeMap.get(prefix + "TXT0085");
			if (isNotEmpty(advpayNo) && isNotEmpty(advpayNo.getValue())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * バリデーション前処理
	 */
	@Override
	public void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// 所在地のバリデーション
		validSbmtAddr(req, "TXT0057");

		// 画面上の前払Noに対する前払消込額を集計
		PartsGrid grid = (PartsGrid)req.runtimeMap.get("GRD0059");
		Map<String, BigDecimal> matAmtMap = new HashMap<>();
		String splrCd = req.runtimeMap.get("TXT0003").getValue();
		String mnyCd = req.runtimeMap.get("MST0104").getValue();
		String slpGrpGl = null, cstTp = null;
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			// 費目１コードが入力済みならアクティブ行とみなす
			String itmexpsCd1 = req.runtimeMap.get(prefix + "TXT0041").getValue();
			if (isNotEmpty(itmexpsCd1)) {
				// 全明細行で伝票グループ（GL）が同一であること
				String group = req.runtimeMap.get(prefix + "TXT0080").getValue();
				if (isNotEmpty(slpGrpGl) && isNotEmpty(group) && !eq(slpGrpGl, group)) {
					throw new InvalidUserInputException("伝票グループが異なる費目が選択されています");
				}
				slpGrpGl = group;
				// 全明細行で経費区分が同一であること
				String type = req.runtimeMap.get(prefix + "TXT0081").getValue();
				if (isNotEmpty(cstTp) && isNotEmpty(type) && !eq(cstTp, type)) {
					throw new InvalidUserInputException("経費区分が異なる費目が選択されています");
				}
				cstTp = type;
			}
			// 取引先CD
			String splrCdDtl = req.runtimeMap.get(prefix + "TXT0091").getValue();
			if (isNotEmpty(splrCd) && !eq(splrCd, splrCdDtl)) {
				throw new InvalidUserInputException("取引先の異なる明細は検収できません");
			}
			// 通貨CD
			String mnyCdDtl = req.runtimeMap.get(prefix + "TXT0092").getValue();
			if (isNotEmpty(mnyCd) && !eq(mnyCd, mnyCdDtl)) {
				throw new InvalidUserInputException("通貨の異なる明細は検収できません");
			}
			// 検収金額（税込）
			String rcvinspAmtIntax = req.runtimeMap.get(prefix + "TXT0057").getValue();
			// 前払No
			String advpayNo = req.runtimeMap.get(prefix + "TXT0085").getValue();
			// 前払消込額
			PartsBase<?> $matAmt = req.runtimeMap.get(prefix + "TXT0084");
			String matAmt = $matAmt.getValue();
			if (isNotEmpty(advpayNo)) {
				if (isNotEmpty(rcvinspAmtIntax) && toBD(rcvinspAmtIntax).compareTo(toBD(matAmt)) < 0) {
					res.errors.add(new PartsValidationResult($matAmt, "前払消込額", "前払消込額は検収金額（税込）以下にしてください。"));
				}

				// 今回の申請分の前払Noに対する前払消込額を合算
				BigDecimal value = matAmtMap.get(advpayNo);
				if (value == null)
					matAmtMap.put(advpayNo, new BigDecimal(matAmt));
				else
					matAmtMap.put(advpayNo, value.add(new BigDecimal(matAmt)));
			}
		}
		// 前払No単位で前払消込額≦前払金残高であること
		final String companyCd = req.runtimeMap.get("TXT0068").getValue();
		final String rcvinspNo = req.runtimeMap.get("NMB0047").getValue();
		for (String advpayNo : matAmtMap.keySet()) {
			//	現状運用で対応するため不要とのこと
//  			// 充当した前払申請が完了していない場合
//			final String[] payNoAdvpayNo = rcvinspInfService.getAdvPayProcess(companyCd, advpayNo);
//			if (MiscUtils.isEmpty(payNoAdvpayNo)) {
//				throw new InvalidUserInputException("前払No「" + advpayNo + "」は存在しません。前払申請が差し戻された可能性があります");
//			}
//			if(payNoAdvpayNo.length < 2) {
//				throw new InvalidUserInputException("不正なプログラムです");
//			}
//			if (MiscUtils.isEmpty(payNoAdvpayNo[1])) {
//				throw new InvalidUserInputException("前払No「" + advpayNo + "」は前払申請が完了していません( 支払No「" + payNoAdvpayNo[0] + "」)");
//			}
			// （検収申請内での）前払Noに対する今回充当額
			final BigDecimal matAmt = matAmtMap.get(advpayNo);
			// （DBの）既存の消込金額（＝残高）
			final BigDecimal remain = rcvinspInfService.getRmnPayAmt(companyCd, rcvinspNo, advpayNo);
			if (remain.compareTo(matAmt) < 0) {
				String rmnAmt = NumberFormat.getNumberInstance().format(remain);
				throw new InvalidUserInputException("前払No「" + advpayNo + "」に対する前払消込額が残高（" + rmnAmt + "）を超過しています");
			}
			// 充当した前払申請が別の検収申請で引き当てられている場合
			String rmnNo = rcvinspInfService.getRmnAdvPayProcess(companyCd, advpayNo, rcvinspNo);
			if (rmnNo != null) {
				throw new InvalidUserInputException("前払No「" + advpayNo + "」は検収中の検収No「" + rmnNo + "」で充当されています");
			}
			// 申請中の変更_検収申請で明細が削除されている場合
			rmnNo = rcvinspInfService.getRmnAdvPayDeleteProcess(companyCd, advpayNo, rcvinspNo);
			if (rmnNo != null) {
				throw new InvalidUserInputException("前払No「" + advpayNo + "」は検収中の検収No「" + rmnNo + "」で充当されています");
			}
		}
		// 費用計上月（申請～決裁まで。決裁以降は書き換えない）
		if (in(req.contents.activityDefCode, "0000000001", "0000000003")) {
			// 検収申請アクティビティ以外で毎回書き込む。これは同一承認者がスキップされるよう設定されているからしょうがない
			setCstAddYm(req.runtimeMap);
		}
	}

	/**
	 * ワークフロー更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		if (isNotEmpty(res.bizInfos)) {
			// 支払方法の名称
			final PartsRadio parts = (PartsRadio)req.runtimeMap.get("RAD0019");
			res.bizInfos.put("PROCESS_BUSINESS_INFO_013", parts.getLabel((PartsDesignRadio)res.ctx.designMap.get(parts.partsId)));
		}
	}

	/**
	 * 費用計上月設定処理
	 * @param runtimeMap
	 * @param baseDate 対象日付
	 */
	protected void setCstAddYm(Map<String, PartsBase<?>> runtimeMap) {
		final String companyCd = runtimeMap.get("TXT0068").getValue();	// 会社CD
		final String rcvinspYm = runtimeMap.get("TXT0129").getValue();	// サービス利用月
		final PartsBase<?> cstAddYm = runtimeMap.get("TXT0131");		// 費用計上月
		cstAddYm.setValue(dmmCustomService.getCstAddYm(companyCd, rcvinspYm, true));
	}

	/**
	 * 抽出キーをもとにユーザデータを抽出する。
	 * （UserDataLoaderService.fillUserData()を呼び出すためのユーザデータMapを生成する）
	 * @param keys 抽出キーMap
	 * @return
	 */
	@Override
	public Map<String, List<UserDataEntity>> createUserDataMap(Map<String, String> keys) {
		final String companyCd = keys.get("companyCd");
		final String rcvinspNo = keys.get("rcvinspNo");
		return rcvinspInfService.getUserDataMap(companyCd, rcvinspNo);
	}

	/**
	 * ロード直後のデザインコンテキストに対して、パーツデザインの修正を行う。
	 * レスポンスに対しては直接影響はしないが、designMapやdcMap等を書き換えることで間接的にレスポンスへ影響を与える。
	 * サーバ側処理のすべてに対して直接的に影響があるので、特にパフォーマンスに厳重注意すること。
	 * 	・他の何物よりも先に実行される
	 * 	・どんなサーバ側処理でも必ず呼び出される（再描画やパーツ固有イベントなどすべて）
	 * @param ctx
	 * @param designCodeMap PartsDesign.designCodeをキーとしたMap
	 */
	@Override
	public void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap) {
		// 通貨に対応した小数点桁数
		final PartsBase<?> rdxpntGdt = ctx.runtimeMap.get("TXT0124");
		if (rdxpntGdt != null) {
			final Integer decimalPlaces = MiscUtils.toInt(rdxpntGdt.getValue());
			final String[] designCodes = {
					"TXT0023",	// [ヘッダ部]合計金額(税抜)
					"TXT0112",	// [ヘッダ部]合計金額(税込)
					"TXT0118",	// [ヘッダ部]源泉対象額
					"TXT0120",	// [ヘッダ部]源泉徴収額
					"TXT0139",	// [ヘッダ部]支払額
					"TXT0176",	// [ヘッダ部]合計税額
					"TXT0178",	// [ヘッダ部]税抜金額(10%対象)
					"TXT0180",	// [ヘッダ部]税額(10%対象)
					"TXT0182",	// [ヘッダ部]税込金額(10%対象)
					"TXT0184",	// [ヘッダ部]税抜金額(軽減8%対象)
					"TXT0186",	// [ヘッダ部]税額(軽減8%対象)
					"TXT0188",	// [ヘッダ部]税込金額(軽減8%対象)
					"TXT0190",	// [ヘッダ部]税抜金額(8%対象)
					"TXT0192",	// [ヘッダ部]税額(8%対象)
					"TXT0194",	// [ヘッダ部]税込金額(8%対象)
					"GRD0059_TXT0048",	// [明細部]発注金額
					"GRD0059_TXT0047",	// [明細部]発注残金額
					"GRD0059_TXT0021",	// [明細部]検収金額(入力項目)
					"GRD0059_TXT0074",	// [明細部]検収金額(税抜)
					"GRD0059_TXT0057",	// [明細部]検収金額(税込)
					"GRD0059_TXT0084",	// [明細部]前払消込額
			};
			// 「通貨に対応した小数点桁数」を「金額欄の小数点桁数」として設定
			for (String designCode : designCodes) {
				final PartsDesignTextbox d = (PartsDesignTextbox)designCodeMap.get(designCode);
				d.decimalPlaces = MiscUtils.defaults(decimalPlaces, 0);
			}
		}

		// 明細の申請者情報
		// 新たな行を追加するたびに、操作者情報を自動設定できるよう、初期値を書き換え
		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()) {
			final String prefix = "GRD0059_";
			designCodeMap.get(prefix + "TXT0070").defaultValue = ctx.runtimeMap.get("TXT0068").getValue();	// 企業コード
			designCodeMap.get(prefix + "TXT0062").defaultValue = ctx.runtimeMap.get("TXT0079").getValue(); 	// 第三階層（部・室）の組織コード
			designCodeMap.get(prefix + "TXT0091").defaultValue = ctx.runtimeMap.get("TXT0003").getValue(); 	// 取引先CD
			final String mnyCd = ctx.runtimeMap.get("MST0104").getValue();
			designCodeMap.get(prefix + "TXT0092").defaultValue = mnyCd; 	// 通貨CD
			designCodeMap.get(prefix + "TXT0108").defaultValue = isEmpty(mnyCd) || eq("JPY", mnyCd) ? "1" : "2";	//通貨区分

			final String advcstMrk = ctx.runtimeMap.get("RAD0170").getValue();
			final String advpayTp = ctx.runtimeMap.get("RAD0095").getValue();
			final PartsDesignGrid grid = (PartsDesignGrid)designCodeMap.get("GRD0059");
			for (PartsRelation pr : grid.relations) {
				// 前払区分による明細の表示列の書き換え
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if (in(partsCode, "TXT0085", "BTN0090")) {	// 前払No／前払選択ボタン
					pr.width = eq(advpayTp, CommonFlag.ON) ? 1 : 0;
				} else if (in(partsCode, "TXT0084")) {		// 前払金額
					pr.width = eq(advpayTp, CommonFlag.ON) ? 2 : 0;
				} else if (eq(partsCode, "TXT0061")) {		// 検収摘要
					pr.width = eq(advpayTp, CommonFlag.ON) ? 6 : 10;
				}
				// 広告費(マーケ専用)による明細の表示列の書き換え
				if (in(partsCode, "DDL0097", "BTN0095", "TXT0100", "BTN0101")) {
					// デバイスコード, 結合フロア選択ボタン, メディアID, メディアID選択ボタン
					pr.width = eq(advcstMrk, CommonFlag.ON) ? 1 : 0;
				} else if (in(partsCode, "DDL0098", "DDL0099")) {
					// 成果地点コード, 計測ツールコード
					pr.width = eq(advcstMrk, CommonFlag.ON) ? 2 : 0;
				} else if (in(partsCode, "TXT0102")) {
					// 結合フロア名称
					pr.width = eq(advcstMrk, CommonFlag.ON) ? 4 : 0;
				} else if (in(partsCode, "TXT0094")) {
					// 結合フロアコード
					pr.width = 0;
				}
			}
		}

		// 請求先会社から起案担当者の会社CDを除外
		final PartsDesignDropdown dd = (PartsDesignDropdown)designCodeMap.get("GRD0059_DDL0078");
		for (Iterator<PartsOptionItem> it = dd.optionItems.iterator(); it.hasNext(); ) {
			PartsOptionItem item = it.next();
			if (ctx.startUserInfo != null && eq(item.value, ctx.startUserInfo.getCorporationCode()))
				it.remove();
		}
	}

	/**
	 * ダウンロードコンテンツをoutputへ書き込む
	 * @param req リクエスト(含むアクション）
	 * @param res（含むデザイナーコンテキスト）
	 * @param in API呼出し時のINパラメータ
	 * @param out API呼出し時のOUTパラメータ
	 * @param functionDef アクション機能
	 * @param output 書き込み先ストリーム（close()不要）
	 * @return ファイルダウンロード用のファイル名
	 */
	@Override
	public String doDownload(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res,
			InParamCallbackBase in, OutParamCallbackBase out,
			WfvFunctionDef functionDef, OutputStream output) throws IOException {
		final ScreenCustomPrint ScreenCustomPrint = get(ScreenCustomPrint.class);
		return ScreenCustomPrint.doDownload(req, res, in, out, functionDef, output);
	}
}
