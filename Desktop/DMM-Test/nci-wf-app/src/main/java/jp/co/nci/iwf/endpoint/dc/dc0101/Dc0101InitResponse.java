package jp.co.nci.iwf.endpoint.dc.dc0101;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書ファイルの更新画面用の初期化レスポンス
 */
public class Dc0101InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 文書ファイル情報 */
	public DocFileInfo docFileInfo;
	/** 文書ファイル情報履歴 */
	public List<DocFileInfo> histories;

	/** バージョン更新区分の選択肢 */
	public List<OptionItem> updateVersionTypes;
}
