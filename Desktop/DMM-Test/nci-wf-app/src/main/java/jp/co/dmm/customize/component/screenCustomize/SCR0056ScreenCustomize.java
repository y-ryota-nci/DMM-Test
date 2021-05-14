package jp.co.dmm.customize.component.screenCustomize;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.co.CntrctInfService;
import jp.co.dmm.customize.endpoint.po.po0010.Po0010Service;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.profile.UserInfo;
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
import jp.co.nci.iwf.designer.parts.runtime.PartsRadio;
import jp.co.nci.iwf.designer.parts.runtime.PartsRepeater;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 新規契約申請画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0056ScreenCustomize extends DmmScreenCustomize {

	@Inject private Po0010Service service;
	/** 契約情報サービス */
	@Inject private CntrctInfService cntrctInfService;

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

				PartsStandAlone standAlone = (PartsStandAlone)ctx.runtimeMap.get("SAS0401");

				// 支払業務コードと名称には起案担当者の所属組織の支払業務コード（（組織マスタ）.[拡張情報01]）を設定
				final UserInfo startUserInfo = res.contents.startUserInfo;
				String companyCd = startUserInfo.getCorporationCode();
				String payApplCd = startUserInfo.getPayApplCd();

				PayApplMst pam = service.getPayApplMst(companyCd, payApplCd);
				if (pam != null) {
					for (PartsContainerRow row : standAlone.rows) {
						String prefix = standAlone.htmlId + "-" + row.rowId + "_";
						ctx.runtimeMap.get(prefix + "TXT0126").setValue(pam.getId().getPayApplCd());
						ctx.runtimeMap.get(prefix + "TXT0127").setValue(pam.getPayApplNm());
					}
				}

				// 起案担当者情報
				ctx.runtimeMap.get("TXT0305").setValue(startUserInfo.getCorporationCode());
				ctx.runtimeMap.get("TXT0301").setValue(startUserInfo.getUserCode());
				ctx.runtimeMap.get("TXT0020").setValue(startUserInfo.getUserName());
				ctx.runtimeMap.get("TXT0302").setValue(startUserInfo.getOrganizationCode()); // 起案担当者の所属組織名（本務）そのまま
				ctx.runtimeMap.get("TXT0021").setValue(startUserInfo.getOrganizationName()); // 起案担当者の所属組織名（本務）そのまま
				ctx.runtimeMap.get("TXT0303").setValue(startUserInfo.getExtendedInfo01()); // 所在地コード
				ctx.runtimeMap.get("TXT0023").setValue(startUserInfo.getSbmtrAddr());
				ctx.runtimeMap.get("TXT1054").setValue(startUserInfo.getOrganizationCodeUp3());	// 起案担当者の第三階層組織コード(部・室)

				// 明細の起案担当者情報
				for (PartsContainerRow row : standAlone.rows) {
					String prefix = standAlone.htmlId + "-" + row.rowId + "_";
					ctx.runtimeMap.get(prefix + "TXT0301").setValue(startUserInfo.getCorporationCode());
					ctx.runtimeMap.get(prefix + "TXT0128").setValue(startUserInfo.getUserName());
					ctx.runtimeMap.get(prefix + "TXT0130").setValue(startUserInfo.getOrganizationName()); // 申請者の所属組織名（本務）そのまま
					ctx.runtimeMap.get(prefix + "TXT1104").setValue(startUserInfo.getOrganizationCodeUp3());
					ctx.runtimeMap.get(prefix + "TXT1129").setValue(startUserInfo.getExtendedInfo01()); // 所在地コード

					// 支払予約の申請日
					if (res.contents.applicationDate == null) {
						ctx.runtimeMap.get(prefix + "TXT1106").setValue(toStr(today())); // 申請日
					} else {
						ctx.runtimeMap.get(prefix + "TXT1106").setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
					}
				}

				//取引先明細
				PartsRepeater splrGrid = (PartsRepeater) ctx.runtimeMap.get("RPT1000");
				for (PartsContainerRow row : splrGrid.rows) {
					String prefix = splrGrid.htmlId + "-" + row.rowId + "_";
					ctx.runtimeMap.get(prefix + "TXT1301").setValue(startUserInfo.getCorporationCode());
				}
			}

			// 申請日：申請するまではシステム日付、申請後は申請日
			PartsBase<?> applyDate = ctx.runtimeMap.get("TXT0022");

			if (res.contents.applicationDate == null) {
				applyDate.setValue(toStr(today())); // 申請日
			} else {
				applyDate.setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
			}
		}
	}

	/**
	 * バリデーション前処理
	 */
	@Override
	public void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {

		// 所在地のバリデーション
		validSbmtAddr(req, "TXT0303");

		// 支払予約明細
		PartsGrid grid = (PartsGrid)req.runtimeMap.get("SAS0401-1_GRD1100");
		String slpGrpGl = null, cstTp = null;
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			// 費目１コードが入力済みならアクティブ行とみなす
			String itmexpsCd1 = req.runtimeMap.get(prefix + "TXT1301").getValue();
			if (isNotEmpty(itmexpsCd1)) {
				// 全明細行で伝票グループ（GL）が同一であること
				String group = req.runtimeMap.get(prefix + "TXT1306").getValue();
				if (isNotEmpty(slpGrpGl) && isNotEmpty(group) && !eq(slpGrpGl, group)) {
					throw new InvalidUserInputException("伝票グループが異なる費目が選択されています");
				}
				slpGrpGl = group;
				// 全明細行で経費区分が同一であること
				String type = req.runtimeMap.get(prefix + "TXT1307").getValue();
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
			setRequestDate(req, "TXT0022");
		}
	}

	@Override
	public void afterUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		super.afterUpdateUserData(req, res);

		String cntrctNo = res.ctx.runtimeMap.get("NMB0003").getValue();
		if (isNotEmpty(cntrctNo) && res.processId != null) {
			// 契約NoをWF支払予約情報へ転写
			int x = cntrctInfService.syncCntrctNo(res.processId, cntrctNo);
			log.debug("X={}", x);
		}
	}

	/**
	 * ロード直後のデザインコンテキストに対して、パーツデザインの修正を行う。
	 * レスポンスに対しては直接影響はしないが、RuntimeMapやDesignMapを書き換えることで間接的にレスポンスへ影響を与える。
	 * サーバ側処理のすべてに対して直接的に影響があるので、特にパフォーマンスに厳重注意すること。
	 * 	・他の何物よりも先に実行される
	 * 	・どんなサーバ側処理でも必ず呼び出される（再描画やパーツ固有イベントなどすべて）
	 * @param ctx
	 * @param designCodeMap PartsDesign.designCodeをキーとしたMap
	 */
	@Override
	public void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap) {
		// 通貨に対応した小数点桁数
		final PartsBase<?> rdxpntGdt = ctx.runtimeMap.get("SAS0401-1_TXT1116");
		if (rdxpntGdt != null) {
			final Integer decimalPlaces = MiscUtils.toInt(rdxpntGdt.getValue());
			final String[] designCodes = {
					"SAS0401_TXT1132",	// [ヘッダ部]合計金額(税額)
					"SAS0401_TXT0133",	// [ヘッダ部]合計金額(税抜)
					"SAS0401_TXT0134",	// [ヘッダ部]合計金額(税込)
					"SAS0401_TXT0137",	// [ヘッダ部]源泉対象額
					"SAS0401_TXT0138",	// [ヘッダ部]源泉徴収額
					"SAS0401_TXT1119",	// [ヘッダ部]支払額
					"SAS0401_TXT1152",	// [ヘッダ部]税抜金額(10%対象)
					"SAS0401_TXT1154",	// [ヘッダ部]税額(10%対象)
					"SAS0401_TXT1156",	// [ヘッダ部]税込金額(10%対象)
					"SAS0401_TXT1158",	// [ヘッダ部]税抜金額(軽減8%対象)
					"SAS0401_TXT1160",	// [ヘッダ部]税額(軽減8%対象)
					"SAS0401_TXT1162",	// [ヘッダ部]税込金額(軽減8%対象)
					"SAS0401_TXT1164",	// [ヘッダ部]税抜金額(8%対象)
					"SAS0401_TXT1166",	// [ヘッダ部]税額(8%対象)
					"SAS0401_TXT1168",	// [ヘッダ部]税込金額(8%対象)
					"SAS0401_GRD1100_TXT1007",	// [明細部]発注金額(入力値)
					"SAS0401_GRD1100_TXT1008",	// [明細部]発注金額(税抜)
					"SAS0401_GRD1100_TXT1009",	// [明細部]発注金額(税込)
			};
			// 「通貨に対応した小数点桁数」を「金額欄の小数点桁数」として設定
			for (String designCode : designCodes) {
				final PartsDesignTextbox d = (PartsDesignTextbox)designCodeMap.get(designCode);
				d.decimalPlaces = MiscUtils.defaults(decimalPlaces, 0);
			}
		}

		// 前払ありなら明細の前払NO/前払消込金額を隠す
		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()) {
			String advpayTp = ctx.runtimeMap.get("SAS0401-1_RAD1113").getValue();
			final PartsDesignGrid grid = (PartsDesignGrid)designCodeMap.get("SAS0401_GRD1100");
			for (PartsRelation pr : grid.relations) {
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if (in(partsCode, "TXT1510", "BTN1512")) {	// 前払No／前払選択ボタン
					pr.width = eq(advpayTp, CommonFlag.ON) ? 1 : 0;
				}
				else if (eq(partsCode, "TXT1511")) {	// 前払充当額
					pr.width = eq(advpayTp, CommonFlag.ON) ? 2 : 0;
				}
				else if (eq(partsCode, "TXT1012")) {	// 支払摘要
					pr.width = eq(advpayTp, CommonFlag.ON) ? 8 : 12;
				}
			}
		}

		// 明細の申請者情報
		// 新たな行を追加するたびに、操作者情報を自動設定
		final String prefix = "SAS0401_GRD1100_";
		final UserInfo startUserInfo = ctx.startUserInfo;
		if (startUserInfo != null) {
			designCodeMap.get(prefix + "TXT1304").defaultValue = startUserInfo.getCorporationCode();		// 企業コード
			designCodeMap.get(prefix + "TXT1303").defaultValue = startUserInfo.getOrganizationCodeUp3(); 	// 第三階層（部・室）の組織コード

			// 請求先会社から起案担当者の会社CDを除外
			final PartsDesignDropdown dd = (PartsDesignDropdown)designCodeMap.get(prefix + "DDL1509");
			if (isNotEmpty(dd) && isNotEmpty(dd.optionItems) && isNotEmpty(ctx.startUserInfo)) {
				for (Iterator<PartsOptionItem> it = dd.optionItems.iterator(); it.hasNext(); ) {
					PartsOptionItem item = it.next();
					if (eq(item.value, ctx.startUserInfo.getCorporationCode()))
						it.remove();
				}
			}
		}

		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()) {
			final String mnyCd = ctx.runtimeMap.get("SAS0401-1_MST0112").getValue();
			designCodeMap.get(prefix + "TXT1514").defaultValue = mnyCd;											// 通貨CD
			designCodeMap.get(prefix + "TXT1520").defaultValue = isEmpty(mnyCd) || eq("JPY", mnyCd) ? "1": "2";	// 通貨区分
		}

		// 支払予約のチェックをON⇒OFF⇒ONと変更した際に支払業務コード、名称がクリアされたままとなる不具合の対応
		// 初期値の設定が必要なその他の項目も同様なのだが、これらは支払予約設定画面起動時に親画面からデータを吸い上げて
		// 設定しているためセーフとなっている
		// 支払業務コード、名称についてはマスタデータの取得が必要なため、サーバ側のmodifyDesignContext内で設定するようにした
		// ただし余計な負荷をかけたくないので値が設定済みであれば不要となるようにしている
		// また支払予約設定画面における「申請日」も上で記載したように親画面側からデータを吸い上げているのだが、
		// 「通貨」のドロップダウンリストが表示されない問題が発生している（画面のレンダリング時点で申請日が空のためマスタ情報を取得できない）
		// なので「申請日」も同様にレンダリング前に値を設定しておく必要がある
		// なおそうすると「通貨」の初期値が設定されないのでここで「通貨パーツ」に対して値を設定しておく（設定する値はパーツに設定されているデフォルト値）
		// ただし通貨に値が設定済みならやらない
		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()) {
			// 支払業務コード、名称の設定
			if (startUserInfo != null) {
				final PartsBase<?> partsPayApplCd = ctx.runtimeMap.get("SAS0401-1_TXT0126");
				final PartsBase<?> partsPayApplNm = ctx.runtimeMap.get("SAS0401-1_TXT0127");
				if ((isNotEmpty(partsPayApplCd) && isEmpty(partsPayApplCd.getValue())) || (isNotEmpty(partsPayApplNm) && isEmpty(partsPayApplNm.getValue()))) {
					String companyCd = startUserInfo.getCorporationCode();
					String payApplCd = startUserInfo.getPayApplCd();
					PayApplMst pam = service.getPayApplMst(companyCd, payApplCd);
					if (pam != null) {
						partsPayApplCd.setValue(pam.getId().getPayApplCd());	// 支払業務コード
						partsPayApplNm.setValue(pam.getPayApplNm());; 			// 支払業務名
					}
				}
			}
			// 申請日、会社コードの設定（通貨のドロップダウンリスト生成に必要）
			{
				final PartsBase<?> partsCompanyCd = ctx.runtimeMap.get("SAS0401-1_TXT0301");
				final PartsBase<?> partsApplyDate = ctx.runtimeMap.get("SAS0401-1_TXT1106");
				if (isNotEmpty(partsCompanyCd) && isEmpty(partsCompanyCd.getValue())) {
					partsCompanyCd.setValue(ctx.runtimeMap.get("TXT0305").getValue());
				}
				if (isNotEmpty(partsApplyDate) && isEmpty(partsApplyDate.getValue())) {
					partsApplyDate.setValue(ctx.runtimeMap.get("TXT0022").getValue());
				}
			}
			// 通貨の設定
			// 通貨パーツに設定されているデフォルト値を設定する
			{
				final PartsBase<?> partsMnyCd = ctx.runtimeMap.get("SAS0401-1_MST0112");
				if (isNotEmpty(partsMnyCd) && isEmpty(partsMnyCd.getValue())) {
					final String defaultMnyCd = designCodeMap.get("SAS0401_MST0112").defaultValue;
					partsMnyCd.setValue(defaultMnyCd);
				}
			}
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
			final PartsRadio parts = (PartsRadio)req.runtimeMap.get("SAS0401-1_RAD0121");
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
		final String cntrctNo = keys.get("cntrctNo");
		final String rtnPayNo = keys.get("rtnPayNo");
		return cntrctInfService.getUserDataMap(companyCd, cntrctNo, rtnPayNo, true);
	}
}