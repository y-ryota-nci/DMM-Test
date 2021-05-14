package jp.co.nci.iwf.endpoint.vd.vd0320;

import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.endpoint.vd.vd0310.BaseContents;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 独立画面パーツ用ポップアップ画面Endpointの初期化リクエスト
 */
public class Vd0320Request extends BaseRequest {
	/** 独立画面パーツのHTML ID */
	public String htmlId;
	/** コンテンツ */
	public BaseContents contents;
	/** HtmlIdをキーとした実行時パーツMap */
	public Map<String, PartsBase<?>> runtimeMap;
	/** デザイナーコンテキスト（プレビュー時のみ） */
	public DesignerContext previewContext;
}
