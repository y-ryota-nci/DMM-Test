package jp.co.nci.iwf.endpoint.vd.vd0110;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面コンテナ設定の初期化レスポンス
 */
public class Vd0110InitResponse extends BaseResponse {

	/** デザイナーコンテキスト */
	public DesignerContext ctx;
	/** HTML */
	public String html;
	/** 表示条件の選択肢 */
	public List<OptionItem> dcList;
	/** トレイタイプの選択肢 */
	public List<OptionItem> trayTypes;
	/** ルートコンテナ配下の全カスタムCSSスタイル */
	public String customCssStyleTag;
}
