package jp.co.nci.iwf.designer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsIoType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.renderer.MasterPartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * マスタ検索パーツ／マスタ選択パーツ用のヘルパー
 */
public class PartsMasterHelper extends MiscUtils {
	/** パーツ関連マスタ.パーツI/O区分で絞り込み条件に使えるもの */
	public static final Set<String> conditions =
			new HashSet<>(Arrays.asList(PartsIoType.IN, PartsIoType.BOTH));

	/** パーツ関連マスタ.パーツI/O区分で検索結果に使えるもの */
	public static final Set<String> results =
			new HashSet<>(Arrays.asList(PartsIoType.OUT, PartsIoType.BOTH));


	/**
	 * パーツ関連情報をAjaxの検索条件リストとして戻す.
	 * @param relations パーツ関連情報
	 * @param htmlId ランタイムパーツのHTML ID
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public static List<MasterPartsColumn> toConditions(List<PartsRelation> relations, PartsBase<?> parts, DesignerContext ctx) {
		return relations.stream()
					.filter(pr -> conditions.contains(pr.partsIoType))
					.sorted((pr1, pr2) -> compareTo(pr1.sortOrder, pr1.sortOrder))
					.map(pr -> new MasterPartsColumn(pr, parts.htmlId, ctx))
					.collect(Collectors.toList());
	}

	/**
	 * パーツ関連情報をAjaxの検索結果リストとして戻す.
	 * @param relations パーツ関連情報
	 * @param htmlId ランタイムパーツのHTML ID
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public static List<MasterPartsColumn> toResults(List<PartsRelation> relations, PartsBase<?> parts, DesignerContext ctx) {
		return relations.stream()
					.filter(pr -> results.contains(pr.partsIoType))
					.sorted((pr1, pr2) -> compareTo(pr1.sortOrder, pr1.sortOrder))
					.map(pr -> new MasterPartsColumn(pr, parts.htmlId, ctx))
					.collect(Collectors.toList());
	}
}
