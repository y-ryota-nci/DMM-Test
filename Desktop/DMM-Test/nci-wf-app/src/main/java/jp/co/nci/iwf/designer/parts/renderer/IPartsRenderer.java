package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.EvaluateCondition;

/**
 * 実行時のパーツレンダラーI/F
 */
public interface IPartsRenderer<P extends PartsBase<D>, D  extends PartsDesign> {

	/**
	 * HTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @return
	 */
	String render(P parts, D design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache);

	/**
	 * パーツの印刷用の値を返す。パーツによっては、例えば画像パーツだとバイト配列を返すし、スタンプパーツだとSVG文字列を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return パーツの印刷用の値
	 */
	Object getPrintValue(P p, D d, DesignerContext ctx);

	/**
	 * パーツの表示用の値を返す
	 * （例：ラジオボタンやドロップダウンは選択値のLABELであったり、組織選択やユーザ選択は組織名やユーザ名を返す）
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	String getDisplayValue(P p, D d, DesignerContext ctx);
}
