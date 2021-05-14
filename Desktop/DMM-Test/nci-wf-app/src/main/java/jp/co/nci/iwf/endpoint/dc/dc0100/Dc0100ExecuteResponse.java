package jp.co.nci.iwf.endpoint.dc.dc0100;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 申請・承認画面のアクション実行レスポンス
 */
public class Dc0100ExecuteResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public Long docId;
	public Long docHistoryId;

	/** パーツのバリデーション結果 */
	public List<PartsValidationResult> errors;
	/** パーツのバリデーション結果によるエラー表示用HTML */
	public String html;
	/** バーツのバリデーション結果を反映したランタイムMap */
	public Map<String, PartsBase<?>> runtimeMap;
	/** 文書管理項目Map */
	public Map<String, String> docBizInfos = new HashMap<>();
	/** デザイナーコンテキスト　※JSON化の対象外※ */
	@JsonIgnore
	public RuntimeContext ctx;
}
