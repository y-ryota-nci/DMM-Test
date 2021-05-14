package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面：リピーターパーツのレスポンス
 */
public class Vd0114ResponseRepeater extends BaseResponse {
	/** 子コンテナの選択肢 */
	public List<OptionItem> childContainers;
	/** ページ制御(ページサイズ)の選択肢 */
	public List<OptionItem> pageSizes;
	/** 入力済み判定対象パーツの選択肢 */
	public List<OptionItem> targetParts;
}
