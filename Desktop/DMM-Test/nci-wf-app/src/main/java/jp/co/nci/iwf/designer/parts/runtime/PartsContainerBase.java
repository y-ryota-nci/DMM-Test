package jp.co.nci.iwf.designer.parts.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;

/**
 * 【実行時】コンテナパーツの抽象基底クラス
 *
 * @param <D>
 */
public abstract class PartsContainerBase<D extends PartsDesignContainer> extends PartsBase<D> implements IPartsPaging {
	/** 行の要素 */
	public List<PartsContainerRow> rows = new ArrayList<>();
	/** ページ番号 */
	public Integer pageNo;
	/** ページあたりの行数 */
	public Integer pageSize;

	/**
	 * ページ数を返す
	 * @return ページ制御するなら総ページ数、ページ制御なしなら -1
	 */
	@JsonIgnore
	public int calcPageCount() {
		return PartsUtils.getPageNo(rows.size(), pageSize);
	}

	/**
	 * 新しいページ番号を総ページ数で補正してセット
	 */
	@JsonIgnore
	public void adjustNewPageNo(int newPageNo) {
		int pageCount = calcPageCount();
		pageNo = PartsUtils.adjustPageNo(pageCount, newPageNo);
	}

	/** バリデーション */
	@Override
	public PartsValidationResult validate(D d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		// 最低入力行数
		if (checkRequired && d.requiredFlag && d.minRowCount != null && d.minRowCount > 0) {
			// 子の各行のうち、入力されているフィールドが１つでもある行の数
			int inputedRow = countInputedRow(ctx, ecResults);

			if (inputedRow < d.minRowCount) {
				// コンテナは直接的に表示用のエレメントを持たないので、子パーツの先頭要素にエラーをセットしよう。
				// もし見つからない場合は、子パーツがすべて有効条件として無効な場合が考えられるが、
				// その場合は無効なエレメントに対してエラーを表示してもしょうがないから、無視してよい
				for (PartsContainerRow row : rows) {
					for (String childHtmlId : row.children) {
						final EvaluateCondition ec = ecResults.get(childHtmlId);
						if (ec != null && ec.enabled) {
							PartsBase<?> pc = ctx.runtimeMap.get(childHtmlId);
							PartsDesign dc = ctx.designMap.get(pc.partsId);
							if (!dc.columns.isEmpty()) {
								String role = dc.columns.get(0).roleCode;
								return new PartsValidationResult(childHtmlId, role, d.labelText, MessageCd.MSG0135, d.minRowCount);
							}
						}
					}
				}
			}
		}
		return null;
	};

	/** 子の各行のうち、入力されているフィールドが１つでもある行の数 */
	@SuppressWarnings("unchecked")
	private int countInputedRow(DesignerContext ctx, Map<String, EvaluateCondition> ecResults) {
		final D design = (D)ctx.designMap.get(partsId);
		final int inputedRow = (int)rows.stream()
				.filter(row -> row.isInputed(design.inputedJudgePartsIds, ctx, ecResults))
				.count();
		return inputedRow;
	}
}
