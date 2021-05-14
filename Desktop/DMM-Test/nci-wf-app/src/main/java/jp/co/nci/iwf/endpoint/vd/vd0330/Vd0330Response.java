package jp.co.nci.iwf.endpoint.vd.vd0330;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.endpoint.vd.vd0310.BaseContents;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 申請画面(参照)のレスポンス
 */
public class Vd0330Response extends BaseResponse {
	/** パーツHTML */
	public String html;
	/** パーツ実行時Map */
	public Map<String, PartsBase<?>> runtimeMap;

	/** 画面ID */
	public long screenId;
	/** 画面名 */
	public String screenName;
	/** コンテンツ */
	public BaseContents contents;
	/** カスタムCSS */
	public String customCssStyleTag;
	/** 外部Javascript */
	public List<Long> javascriptIds;
	/** 戻り先URL */
	public String backUrl;
}
