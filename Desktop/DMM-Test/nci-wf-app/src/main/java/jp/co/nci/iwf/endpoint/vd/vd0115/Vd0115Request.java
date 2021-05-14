package jp.co.nci.iwf.endpoint.vd.vd0115;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プレビュー画面の初期化リクエスト
 */
public class Vd0115Request extends BaseRequest {

	/** デザイナコンテキスト */
	public DesignerContext ctx;
	/** 表示条件ID */
	public Long dcId;
	/** トレイタイプ */
	public TrayType trayType;
}
