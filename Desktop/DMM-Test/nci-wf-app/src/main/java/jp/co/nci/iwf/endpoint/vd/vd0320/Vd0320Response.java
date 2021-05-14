package jp.co.nci.iwf.endpoint.vd.vd0320;

import java.util.List;

import jp.co.nci.iwf.designer.service.javascript.LoadFunction;
import jp.co.nci.iwf.designer.service.javascript.SubmitFunction;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 独立画面パーツ用ポップアップ画面Endpointの初期化レスポンス
 */
public class Vd0320Response extends BaseResponse {
	/** 独立画面パーツのHTML ID */
	public String html;
	/** 独立画面パーツを最上位としたときの外部Javascript */
	public List<Long> javascriptIds;
	/** 独立画面パーツを最上位としたときのロード時の呼び出し関数 */
	public List<LoadFunction> loadFunctions;
	/** 独立画面パーツを最上位としたときのSubmit時の呼び出し関数 */
	public List<SubmitFunction> submitFunctions;
	/** 独立画面パーツを最上位としたときのカスタムCSS */
	public String customCssStyleTag;
	/** コンテナ名 */
	public String containerName;
}
