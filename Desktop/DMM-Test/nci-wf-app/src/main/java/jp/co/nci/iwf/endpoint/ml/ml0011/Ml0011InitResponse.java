package jp.co.nci.iwf.endpoint.ml.ml0011;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * メールテンプレート編集の初期化レスポンス
 */
public class Ml0011InitResponse extends BaseResponse {
	/** メールテンプレートのヘッダ情報 */
	public Ml0011EntityHeader header;
	/** メールテンプレートの本文情報 */
	public List<Ml0011EntityBody> bodies;
	/** メール変数の選択肢 */
	public List<OptionItem> variables;
	/** 標準に戻せるか */
	public boolean canBackToStandard;
}
