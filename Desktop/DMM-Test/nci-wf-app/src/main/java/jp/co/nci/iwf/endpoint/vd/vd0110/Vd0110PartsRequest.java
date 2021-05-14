package jp.co.nci.iwf.endpoint.vd.vd0110;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面コンテナ設定のパーツ追加/削除/編集/コピーリクエスト
 */
public class Vd0110PartsRequest extends BaseRequest {
	/** コピーor削除の対象パーツID */
	public Long partsId;
	/** パーツ編集した際の対象パーツ定義 */
	public PartsDesign design;
	/** パーツ追加するときの対象パーツタイプ */
	public Integer partsType;
	/** デザイナーコンテキスト */
	public DesignerContext ctx;
	/** 背景HTMLセル番号 */
	public Integer bgHtmlCellNo;
}
