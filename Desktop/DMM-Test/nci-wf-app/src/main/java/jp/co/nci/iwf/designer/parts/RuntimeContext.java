package jp.co.nci.iwf.designer.parts;

import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.BaseContents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * デザイナー実行時/申請時のコンテキスト
 */
public class RuntimeContext extends DesignerContext {
	public String corporationCode;
	public Long processId;
	public String actionType;
	public WfmActivityDef activityDef;
	public Long copyProcessId;
	// 以下、文書管理用の変数
	public Long docId;
	public Long screenDocId;

	/**
	 * 【画面ロード時】VD0310コンテンツから実行時時のコンテキストをインスタンス化
	 * @param trayEntity トレイ情報
	 * @param trayType トレイタイプ
	 * @param viewWidth ブラウザの表示幅
	 * @param runtimeMap 既存の実行時パーツMap。新規にロードするならnull。
	 * @return
	 */
	public static RuntimeContext newInstance(BaseContents contents, ViewWidth viewWidth, Map<String, PartsBase<?>> runtimeMap) {
		final RuntimeContext ctx = new RuntimeContext();
		ctx.viewWidth = viewWidth;
		ctx.renderMode = RenderMode.RUNTIME;
		ctx.screenId = contents.screenId == null ? 0L : contents.screenId;
		ctx.corporationCode = contents.corporationCode;
		ctx.processId = contents.processId;
		ctx.trayType = contents.trayType;
		ctx.dcId = contents.dcId;
		ctx.startUserInfo = contents.startUserInfo;
		ctx.processUserInfo = contents.processUserInfo;
		if (MiscUtils.isNotEmpty(contents.stampMap)) {
			ctx.stampMap.clear();
			ctx.stampMap.putAll(contents.stampMap);
		}
		ctx.activityDef = contents.activityDef;
		ctx.screenCustomClass = contents.screenCustomClass;
		ctx.docId = contents.docId;
		ctx.screenDocId = contents.screenDocId;

		// 既存の実行時パーツMapがあれば置換
		if (runtimeMap != null) {
			ctx.runtimeMap = runtimeMap;
		}
		return ctx;
	}

	/**
	 * 【アクションボタン押下時】WF更新系リクエストから実行時時のコンテキストをインスタンス化
	 * @param req ボタンアクションのリクエスト
	 * @return
	 */
	public static RuntimeContext newInstance(Vd0310ExecuteRequest req) {
		final RuntimeContext ctx = newInstance(req.contents, req.viewWidth, req.runtimeMap);
		ctx.actionType = req.actionInfo.actionType;

		// 起案担当者はクライアントで変更可能
		ctx.startUserInfo = MiscUtils.defaults(req.startUserInfo, req.contents.startUserInfo);
		return ctx;
	}

	/**
	 * 【登録・更新ボタン押下時】文書管理(業務文書)系リクエストから実行時時のコンテキストをインスタンス化
	 * @param req ボタンアクションのリクエスト
	 * @return
	 */
	public static RuntimeContext newInstance(Dc0100ExecuteRequest req) {
		final RuntimeContext ctx = newInstance(req.contents, req.viewWidth, req.runtimeMap);
		return ctx;
	}
}
