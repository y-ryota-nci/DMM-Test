package jp.co.nci.iwf.endpoint.vd.vd0115;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * プレビュー画面のレスポンス
 */
public class Vd0115Response extends BaseResponse {

	/** デザイナーのレンダリングしたHTML */
	public String html;
	/** デザイナーコンテキスト */
	public DesignerContext ctx;
	/** コンテナ固有のカスタムCSSスタイル */
	public String customCssStyleTag;
	/** 業務管理項目Map */
	public Map<String, String> businessInfoMap;
	/** パーツのバリデーション結果 */
	public List<PartsValidationResult> errors;

	// 再描画用に使用
	// 再描画時のリクエストはBaseContentsなのでリクエストデータを吸い上げる際に
	// 生成するクラスをVd0310ContentsなのかDc0100Contentsなのかの判断している
	public Vd0310Contents contents;
}
