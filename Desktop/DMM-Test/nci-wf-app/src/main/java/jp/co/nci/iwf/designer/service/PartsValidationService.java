package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツのバリデーションサービス
 */
@BizLogic
public class PartsValidationService extends MiscUtils {
	/**
	 * パーツのバリデーションを行う。また、バリデーションの結果としてエラーとなったパーツが親
	 * @param ctx デザイナーコンテキスト
	 * @param ecResults パーツ有効条件の判定結果
	 * @return
	 */
	public List<PartsValidationResult> validate(DesignerContext ctx, Map<String, EvaluateCondition> ecResults, boolean required) {
		final List<PartsValidationResult> results = new ArrayList<>();

		// ルートコンテナのパーツ
		final PartsRootContainer pc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);
		validate(pc, ctx, results, ecResults, required);

		// エラーのあったパーツを表示させるため、親コンテナのページ番号を変更
		for (PartsValidationResult e : results) {
			moveParsToFontPage(e.htmlId, ctx);
		}

		return results;
	}

	/**
	 * パーツのバリデーションを再帰的に行う
	 * @param c コンテナパーツ
	 * @param ctx デザイナーコンテキスト
	 * @param results 判定結果リスト
	 * @param ecResults パーツ有効条件の判定結果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void validate(
			PartsContainerBase<?> c,
			DesignerContext ctx,
			List<PartsValidationResult> results,
			Map<String, EvaluateCondition> ecResults,
			boolean required
	) {
		for (PartsContainerRow row : c.rows) {
			for (String htmlId : row.children) {
				final PartsBase p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);

				// 有効条件：この有効条件は親パーツと自パーツの有効条件のANDで判定済
				final boolean ecEnable = defaults(ecResults.get(htmlId).enabled, true);
				// 表示条件：この表示条件は親パーツと自パーツの表示条件のANDで判定済
				final int dcType = defaults(ctx.dcMap.get(p.partsId), DcType.INPUTABLE);
				if (ecEnable && dcType <= DcType.INPUTABLE) {
					final PartsValidationResult error = p.validate(d, ctx, required, ecResults);
					if (error != null) {
						results.add(error);
					}
				}

				// 子要素があれば再帰呼び出し
				if (p instanceof PartsContainerBase) {
					validate((PartsContainerBase)p, ctx, results, ecResults, required);
				}
			}
		}
	}

	/**
	 * 対象パーツが親コンテナで表示されるよう、親コンテナのページ番号を変更
	 * @param htmlId
	 * @param ctx
	 */
	private void moveParsToFontPage(String htmlId, DesignerContext ctx) {
		// 親コンテナのhtmlId
		String parentHtmlId = PartsUtils.getParentHtmlId(htmlId);
		if (parentHtmlId == null)
			return;

		// 自パーツのHtmlIdには、自パーツが親コンテナの何ページ目にいるのかが含まれているので
		// それを使って親コンテナのページ番号を変更
		final Integer rowId = PartsUtils.getParentRowId(htmlId);
		if (rowId != null) {
			// 親コンテナ
			final PartsContainerBase<?> c = (PartsContainerBase<?>)ctx.runtimeMap.get(parentHtmlId);
			if (c == null)
				return;
			final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(c.partsId);

			if (d.pageSize != null) {
				c.pageNo = PartsUtils.getPageNo(rowId, d.pageSize);
			}
		}

		// 親パーツよりさらに上のパーツがあれば再帰
		if (parentHtmlId.indexOf('_') >= 0)
			moveParsToFontPage(parentHtmlId, ctx);
	}
}
