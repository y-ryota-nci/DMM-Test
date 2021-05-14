package jp.co.dmm.customize.component.screenCustomize;

import java.util.Map;

import javax.inject.Named;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;

/**
 * 新規_支払依頼申請（経費）の画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0079ScreenCustomize extends SCR0050ScreenCustomize {

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
				// 発注区分
				ctx.runtimeMap.get("TXT0147").setValue(PurordTp.EXPENSE);
			}
		}
		super.afterInitLoad(req, res, ctx);
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

		// 明細書き換え処理
		{
			PartsDesignGrid design = (PartsDesignGrid)designCodeMap.get("GRD0080");
			// 各ボタンを表示
			design.showAddEmptyButtonFlag = true;
			design.showDeleteButtonFlag = true;
			design.showCopyButtonFlag = true;

			// 表示セルを変更
			for (PartsRelation pr : design.relations) {
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if ("TXT0008".equals(partsCode) || "TXT0014".equals(partsCode)) {
					pr.width = 0;
				} else if ("TXT0032".equals(partsCode) || "DDL0044".equals(partsCode)) {
					pr.width = 2;
				} else if ("TXT0022".equals(partsCode)) {
					pr.width = 12;
				} else if ("TXT0049".equals(partsCode) || "BTN0052".equals(partsCode)) {
					pr.width = 1;
					pr.columnName = "前払No";
					if ("TXT0049".equals(partsCode)) {
						ctx.designMap.get(pr.targetPartsId).colMd = 6;
						ctx.designMap.get(pr.targetPartsId).colMd = 7;
					}
//				} else if (("BTN0025".equals(partsCode) || "BTN0048".equals(partsCode))
//						&& ctx != null && ctx.dcMap != null && ctx.dcMap.containsKey(pr.targetPartsId)
//						&& DcType.HIDDEN == ctx.dcMap.get(pr.targetPartsId)) {
//					ctx.dcMap.put(pr.targetPartsId, DcType.INPUTABLE);
				}
			}
		}
		// 前払区分による明細の表示列の書き換え
		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()){
			// 前払区分による明細の表示列の書き換え
			final String advpayTp = ctx.runtimeMap.get("RAD0115").getValue();
			final PartsDesignGrid grid = (PartsDesignGrid)designCodeMap.get("GRD0080");
			for (PartsRelation pr : grid.relations) {
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if (in(partsCode, "TXT0049", "BTN0052")) {		// 前払No／前払検索
					pr.width = eq(advpayTp, CommonFlag.ON) ? 1 : 0;
				} else if (in(partsCode, "TXT0050")) {		// 前払金額
					pr.width = eq(advpayTp, CommonFlag.ON) ? 2 : 0;
				} else if (eq(partsCode, "DDL0044")) {		// 検収摘要
					pr.width = eq(advpayTp, CommonFlag.ON) ? 2 : 6;
				}
			}
		}
	}

}
