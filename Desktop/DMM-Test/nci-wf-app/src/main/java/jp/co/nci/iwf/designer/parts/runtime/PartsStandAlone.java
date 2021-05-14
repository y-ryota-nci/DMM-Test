package jp.co.nci.iwf.designer.parts.runtime;

import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStandAlone;

/**
 * 独立画面パーツ。子コンテナをポップアップ画面として開く
 */
public class PartsStandAlone extends PartsContainerBase<PartsDesignStandAlone> {
	/** 独立した画面としてレンダリングするか。trueなら独立画面モード、falseなら通常パーツモード */
	public boolean renderAsStandAlone;
	/** 描画時のブラウザの表示幅 */
	public ViewWidth viewWidth;
}
