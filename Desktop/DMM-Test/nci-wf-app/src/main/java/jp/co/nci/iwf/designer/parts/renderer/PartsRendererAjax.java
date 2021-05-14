package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;

/**
 * マスタ選択パーツや検索ボタンパーツなど、Ajaxリクエストから汎用テーブルを参照するパーツのレンダラーの基底クラス
 *
 * @param <P> 実行時パーツ
 * @param <D> パーツ定義
 */
public abstract class PartsRendererAjax<P extends PartsBase<D>, D  extends PartsDesign>
		extends PartsRenderer<P, D> {

	/** パーツ関連マスタ.パーツI/O区分で絞り込み条件に使えるもの */
	protected static final Set<String> conditions =
			new HashSet<>(Arrays.asList(PartsIoType.IN, PartsIoType.BOTH));

	/** パーツ関連マスタ.パーツI/O区分で検索結果に使えるもの */
	protected static final Set<String> results =
			new HashSet<>(Arrays.asList(PartsIoType.OUT, PartsIoType.BOTH));

	/**
	 * パーツ関連情報をAjaxの検索条件リストとしてJSON文字列化する
	 * @param relations パーツ関連情報
	 * @param parts ランタイムパーツ
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	protected String toJsonResults(List<PartsRelation> relations, P parts, DesignerContext ctx) {
		return toJson(relations.stream()
				.filter(pr -> results.contains(pr.partsIoType))
				.sorted((pr1, pr2) -> compareTo(pr1.sortOrder, pr1.sortOrder))
				.map(pr -> new MasterPartsColumn(pr, parts.htmlId, ctx))
				.collect(Collectors.toList()));
	}

	/**
	 * パーツ関連情報をAjaxの検索結果リストとして戻す.
	 * @param relations パーツ関連情報
	 * @param parts ランタイムパーツ
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public List<MasterPartsColumn> getResults(List<PartsRelation> relations, P parts, DesignerContext ctx) {
		return relations.stream()
					.filter(pr -> results.contains(pr.partsIoType))
					.sorted((pr1, pr2) -> compareTo(pr1.sortOrder, pr1.sortOrder))
					.map(pr -> new MasterPartsColumn(pr, parts.htmlId, ctx))
					.collect(Collectors.toList());
	}

	/**
	 * パーツ関連情報をAjaxの検索結果リストとしてJSON文字列化する
	 * @param relations パーツ関連情報
	 * @param parts ランタイムパーツ
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	protected String toJsonConditions(List<PartsRelation> relations, P parts, DesignerContext ctx) {
		return toJson(relations.stream()
				.filter(pr -> conditions.contains(pr.partsIoType))
				.sorted((pr1, pr2) -> compareTo(pr1.sortOrder, pr1.sortOrder))
				.map(pr -> new MasterPartsColumn(pr, parts.htmlId, ctx))
				.collect(Collectors.toList()));
	}

	/**
	 * パーツ関連情報をAjaxの検索条件リストとして戻す.
	 * @param relations パーツ関連情報
	 * @param parts ランタイムパーツ
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public List<MasterPartsColumn> getConditions(List<PartsRelation> relations, P parts, DesignerContext ctx) {
		return relations.stream()
					.filter(pr -> conditions.contains(pr.partsIoType))
					.sorted((pr1, pr2) -> compareTo(pr1.sortOrder, pr1.sortOrder))
					.map(pr -> new MasterPartsColumn(pr, parts.htmlId, ctx))
					.collect(Collectors.toList());
	}
}
