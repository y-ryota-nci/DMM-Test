package jp.co.dmm.customize.component.screenCustomize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.po.PurordInfService;
import jp.co.dmm.customize.endpoint.po.po0010.Po0010Service;
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
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 新規_発注申請の画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0038ScreenCustomize extends DmmScreenCustomize {

	@Inject private Po0010Service service;
	/** DMM発注情報共通サービス */
	@Inject private PurordInfService purordInfService;
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
			final UserInfo startUserInfo = res.contents.startUserInfo;
			String companyCd = res.contents.startUserInfo.getCorporationCode();
			String payApplCd = res.contents.startUserInfo.getPayApplCd();

			if (req.processId == null) {

				// 支払業務コードと名称には起案担当者の所属組織の支払業務コード（（組織マスタ）.[拡張情報01]）を設定
				PayApplMst pam = service.getPayApplMst(companyCd, payApplCd);
				if (pam != null) {
					ctx.runtimeMap.get("TXT0132").setValue(pam.getId().getPayApplCd());
					ctx.runtimeMap.get("TXT0133").setValue(pam.getPayApplNm());
				}

				// 起案担当者情報
				ctx.runtimeMap.get("TXT0091").setValue(startUserInfo.getCorporationCode());
				ctx.runtimeMap.get("TXT0068").setValue(startUserInfo.getUserCode());
				ctx.runtimeMap.get("TXT0072").setValue(startUserInfo.getUserName());
				ctx.runtimeMap.get("TXT0069").setValue(startUserInfo.getOrganizationCode());
				ctx.runtimeMap.get("TXT0073").setValue(startUserInfo.getOrganizationName());
				ctx.runtimeMap.get("TXT0071").setValue(startUserInfo.getExtendedInfo01()); // 所在地コード
				ctx.runtimeMap.get("TXT0087").setValue(startUserInfo.getSbmtrAddr());
				ctx.runtimeMap.get("TXT0160").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
				// 明細の起案担当者情報
				PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0074");
				for (PartsContainerRow row : grid.rows) {
					String prefix = grid.htmlId + "-" + row.rowId + "_";
					ctx.runtimeMap.get(prefix + "TXT0091").setValue(startUserInfo.getCorporationCode());
					ctx.runtimeMap.get(prefix + "TXT0048").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
				}
			}
			else if (eq("0000000001", res.contents.activityDefCode)) {
				// 申請アクティビティなら常に会社と組織コード（部・室）を正しいものに書き換える
				ctx.runtimeMap.get("TXT0091").setValue(startUserInfo.getCorporationCode());
				ctx.runtimeMap.get("TXT0069").setValue(startUserInfo.getOrganizationCode());
				ctx.runtimeMap.get("TXT0073").setValue(startUserInfo.getOrganizationName());
				ctx.runtimeMap.get("TXT0160").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
				// 明細の申請者情報
				PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0074");
				for (PartsContainerRow row : grid.rows) {
					String prefix = grid.htmlId + "-" + row.rowId + "_";
					ctx.runtimeMap.get(prefix + "TXT0091").setValue(startUserInfo.getCorporationCode());
					ctx.runtimeMap.get(prefix + "TXT0048").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
				}
			}
			// 申請日：申請するまではシステム日付、申請後は申請日
			PartsBase<?> applyDate = ctx.runtimeMap.get("TXT0070");
			if (res.contents.applicationDate == null) {
				applyDate.setValue(toStr(today())); // 申請日
			} else {
				applyDate.setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
			}
			// 社内レート基準日：部長承認まではシステム日付、部長承認後は変更なし
			if (in(res.contents.activityDefCode, "0000000001", "0000000003")) {
				final PartsBase<?> inRtoBaseDt = ctx.runtimeMap.get("TXT0188");
				if (in(inRtoBaseDt.dcType, DcType.INPUTABLE, DcType.UKNOWN)) {
					Date endOfLastMonth = dmmCustomService.getRtoBaseDt(today());
					inRtoBaseDt.setValue(toStr(endOfLastMonth, "yyyy/MM/dd"));

					// 通貨コードに対応した社内レートと小数点桁数を最新のマスタから反映
					final PartsMaster currency = (PartsMaster)ctx.runtimeMap.get("MST0159");
					partsMasterService.distributeMasterValues(currency, ctx);
				}
			}

		}
	}

	/**
	 * バリデーション前処理
	 */
	@Override
	public void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {

		// 所在地のバリデーション
		validSbmtAddr(req, "TXT0071");

		// 発注明細
		PartsGrid grid = (PartsGrid)req.runtimeMap.get("GRD0074");
		String slpGrpGl = null, cstTp = null;
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			// 費目１コードが入力済みならアクティブ行とみなす
			String itmexpsCd1 = req.runtimeMap.get(prefix + "TXT0038").getValue();
			if (isNotEmpty(itmexpsCd1)) {
				// 全明細行で伝票グループ（GL）が同一であること
				String group = req.runtimeMap.get(prefix + "TXT0097").getValue();
				if (isNotEmpty(slpGrpGl) && isNotEmpty(group) && !eq(slpGrpGl, group)) {
					throw new InvalidUserInputException("伝票グループが異なる費目が選択されています");
				}
				slpGrpGl = group;
				// 全明細行で経費区分が同一であること
				String type = req.runtimeMap.get(prefix + "TXT0098").getValue();
				if (isNotEmpty(cstTp) && isNotEmpty(type) && !eq(cstTp, type)) {
					throw new InvalidUserInputException("経費区分が異なる費目が選択されています");
				}
				cstTp = type;
			}
		}
	}
	/**
	 * バリデーション後処理
	 */
	@Override
	public void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {

		if (res.errors == null || res.errors.isEmpty()) {
			// 申請日を設定
			setRequestDate(req, "TXT0070");
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
			final PartsRadio parts = (PartsRadio)req.runtimeMap.get("RAD0049");
			res.bizInfos.put("PROCESS_BUSINESS_INFO_013", parts.getLabel((PartsDesignRadio)res.ctx.designMap.get(parts.partsId)));
		}
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
		final String purordNo = keys.get("purordNo");
		return purordInfService.getUserDataMap(companyCd, purordNo);
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
		final PartsBase<?> rdxpntGdt = ctx.runtimeMap.get("TXT0171");
		if (rdxpntGdt != null) {
			final Integer decimalPlaces = MiscUtils.toInt(rdxpntGdt.getValue());
			final String[] designCodes = {
					"TXT0056",	// [ヘッダ部]合計金額(税抜)
					"TXT0146",	// [ヘッダ部]合計金額(税込)
					"TXT0155",	// [ヘッダ部]源泉対象額
					"TXT0157",	// [ヘッダ部]源泉徴収額
					"TXT0187",	// [ヘッダ部]支払額
					"TXT0227",	// [ヘッダ部]合計税額
					"TXT0229",	// [ヘッダ部]税抜金額(10%対象)
					"TXT0231",	// [ヘッダ部]税額(10%対象)
					"TXT0233",	// [ヘッダ部]税込金額(10%対象)
					"TXT0235",	// [ヘッダ部]税抜金額(軽減8%対象)
					"TXT0237",	// [ヘッダ部]税額(軽減8%対象)
					"TXT0239",	// [ヘッダ部]税込金額(軽減8%対象)
					"TXT0241",	// [ヘッダ部]税抜金額(8%対象)
					"TXT0243",	// [ヘッダ部]税額(8%対象)
					"TXT0245",	// [ヘッダ部]税込金額(8%対象)
					"GRD0074_TXT0017",	// [明細部]発注金額(入力値)
					"GRD0074_TXT0054",	// [明細部]発注金額(税抜)
					"GRD0074_TXT0045",	// [明細部]発注金額(税込)
			};
			// 「通貨に対応した小数点桁数」を「金額欄の小数点桁数」として設定
			for (String designCode : designCodes) {
				final PartsDesignTextbox d = (PartsDesignTextbox)designCodeMap.get(designCode);
				d.decimalPlaces = MiscUtils.defaults(decimalPlaces, 0);
			}
		}

		// 明細の申請者情報
		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()) {
			// 新たな行を追加するたびに、操作者情報を自動設定できるよう、初期値を書き換え
			final String prefix = "GRD0074_";
			designCodeMap.get(prefix + "TXT0091").defaultValue = ctx.runtimeMap.get("TXT0091").getValue();	// 企業コード
			designCodeMap.get(prefix + "TXT0048").defaultValue = ctx.runtimeMap.get("TXT0160").getValue(); 	// 第三階層（部・室）の組織コード
			final String mnyCd = ctx.runtimeMap.get("MST0159").getValue();
			designCodeMap.get(prefix + "TXT0113").defaultValue = isEmpty(mnyCd) || eq("JPY", mnyCd) ? "1" : "2";	//通貨区分

			final String advcstMrk = ctx.runtimeMap.get("RAD0220").getValue();
			final PartsDesignGrid grid = (PartsDesignGrid)designCodeMap.get("GRD0074");
			for (PartsRelation pr : grid.relations) {
				// 広告費(マーケ専用)による明細の表示列の書き換え
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if (in(partsCode, "DDL0104", "BTN0106", "TXT0107", "BTN0108")) {
					// デバイスコード, 結合フロア選択ボタン, メディアID, メディアID選択ボタン
					pr.width = eq(advcstMrk, CommonFlag.ON) ? 1 : 0;
				} else if (in(partsCode, "DDL0102", "DDL0103")) {
					// 成果地点コード, 計測ツールコード
					pr.width = eq(advcstMrk, CommonFlag.ON) ? 2 : 0;
				} else if (in(partsCode, "TXT0109")) {
					// 結合フロア名称
					pr.width = eq(advcstMrk, CommonFlag.ON) ? 4 : 0;
				} else if (in(partsCode, "TXT0105")) {
					// 結合フロアコード
					pr.width = 0;
				}
			}
		}

		// 請求先会社から起案担当者の会社CDを除外
		final PartsDesignDropdown dd = (PartsDesignDropdown)designCodeMap.get("GRD0074_DDL0095");
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
