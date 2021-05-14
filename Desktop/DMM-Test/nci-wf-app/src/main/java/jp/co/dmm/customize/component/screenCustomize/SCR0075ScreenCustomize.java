package jp.co.dmm.customize.component.screenCustomize;

import java.util.Map;

import javax.inject.Named;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRadio;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 新規_前払申請の画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0075ScreenCustomize extends SCR0050ScreenCustomize {

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
				// 前払
				ctx.runtimeMap.get("TXT0155").setValue(CommonFlag.ON);
				// 消費税処理単位
				ctx.runtimeMap.get("RAD0127").setValue(TaxUnt.NEW_VOUCHER);
//				// 消費税フラグ
//				ctx.runtimeMap.get("MST0129").setValue("9");
//				// 消費税率
//				ctx.runtimeMap.get("TXT0130").setValue("0");
//				// 税処理区分
//				ctx.runtimeMap.get("TXT0131").setValue("0");
//				// 消費税
//				ctx.runtimeMap.get("TXT0132").setValue("000");
			}
		}
		super.afterInitLoad(req, res, ctx);
	}
	/**
	 * ユーザデータのエラーチェック前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// 一時保存の場合は各追加チェック処理を行わない
		if (!ActionType.NORMAL.equals(req.actionInfo.actionType)) {
			return;
		}

		final PartsGrid grid = (PartsGrid)req.runtimeMap.get("GRD0080");
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			String taxSbjTp = req.runtimeMap.get(prefix + "TXT0067").getValue();
			if ("1".equals(taxSbjTp)) {
				throw new InvalidUserInputException("消費税対象の費目が選択されています");
			}
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
		// 親の書き換え処理実行
		super.modifyDesignContext(ctx, designCodeMap);

		// トレイタイプがワークリストの場合
		if (TrayType.WORKLIST == ctx.trayType || TrayType.NEW == ctx.trayType || TrayType.BATCH == ctx.trayType) {
			// 消費税処理単位
			{
				PartsDesignRadio design = (PartsDesignRadio)designCodeMap.get("RAD0127");
				design.defaultValue = TaxUnt.NEW_VOUCHER;
			}
//			// 消費税
//			{
//				PartsDesignMaster design = (PartsDesignMaster)designCodeMap.get("MST0129");
//				design.defaultValue = "000";
//			}
		}
		// 明細書き換え処理
		{
			PartsDesignGrid design = (PartsDesignGrid)designCodeMap.get("GRD0080");
			design.showAddEmptyButtonFlag = false;
			design.showDeleteButtonFlag = false;

			// 表示セルを変更
			for (PartsRelation pr : design.relations) {
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if ("TXT0008".equals(partsCode) || "TXT0014".equals(partsCode) || "MST0069".equals(partsCode) || "TXT0049".equals(partsCode) || "TXT0050".equals(partsCode)) {
					pr.width = 0;
				} else if ("DDL0044".equals(partsCode)) {
					pr.width = 3;
				} else if ("TXT0022".equals(partsCode)) {
					pr.width = 5;
				} else if (("BTN0025".equals(partsCode) || "BTN0048".equals(partsCode))
						&& DcType.HIDDEN == ctx.dcMap.get(pr.targetPartsId)) {
					ctx.dcMap.put(pr.targetPartsId, DcType.INPUTABLE);
				}
			}
		}
	}

}
