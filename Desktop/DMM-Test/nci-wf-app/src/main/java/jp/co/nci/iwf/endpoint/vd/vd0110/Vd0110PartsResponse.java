package jp.co.nci.iwf.endpoint.vd.vd0110;

import java.util.Collection;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面コンテナ設定のパーツ追加・編集レスポンス
 */
public class Vd0110PartsResponse extends BaseResponse {
	/** HTML */
	public String html;
	/** パーツデザイン定義Map */
	public Map<Long, PartsDesign> designMap;
	/** 新しいパーツのパーツID */
	public Long newPartsId;
	/** ルートコンテナの子パーツリスト */
	public Collection<Long> childPartsIds;
	/** コンテナのカスタムCSS */
	public String customCssStyleTag;
	/** パーツコード連番 */
	public int partsCodeSeq;
}
