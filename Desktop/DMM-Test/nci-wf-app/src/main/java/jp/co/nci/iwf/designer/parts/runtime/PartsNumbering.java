package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.CDI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleNumbering;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignNumbering;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.designer.service.numbering.PartsNumberingService;

/**
 * 【実行時】採番パーツ
 */
public class PartsNumbering extends PartsBase<PartsDesignNumbering> implements RoleNumbering {
	private static final Logger log = LoggerFactory.getLogger(PartsNumbering.class);

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignNumbering d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		return null;
	}

	/** パーツからユーザデータへ値を抜き出し */
	@Override
	public void toUserData(PartsDesignNumbering d, Map<String, Object> userData, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		if (shouldNumbering(d, ctx, ecResults)) {
			// 採番
			final String newNumber = getNewNumber(d, ctx);
			values.put(NUMBERING, newNumber);
		}
		super.toUserData(d, userData, ctx, ecResults);
	}

	/** 採番する */
	private String getNewNumber(PartsDesignNumbering d, RuntimeContext ctx) {
		// 採番するうえでの前提条件は shouldNumbering()でチェック済みである
		final PartsNumberingService service = CDI.current().select(PartsNumberingService.class).get();
		final String newNumber = service.getNumber(d.partsNumberingFormatId);

		log.debug("採番パーツ={} 採番値={}", htmlId, newNumber);

		return newNumber;
	}

	/** 採番すべきか */
	private boolean shouldNumbering(PartsDesignNumbering d, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		// 文書管理ではアクティビティ定義がないため、未採番であれば常に採番する
		// RuntimeContextにdocIdがあれば文書管理として判断する
		// 表示条件が入力可 有効条件が有効となっていること
		if (ctx.docId != null) {
			return (values.isEmpty() || isEmpty(values.get(NUMBERING)))	// 未採番
					&& DcType.isInputable(dcType)							// 表示条件＝入力可
					&& ecResults.get(htmlId).enabled;						// 有効条件＝有効
		}
		else if (ctx.activityDef != null) {
			// アクティビティ定義の採番コード（カンマ区切り）
			final String numberingCode = defaults(ctx.activityDef.getNumberingCode(), "");
			final Set<String> numberingCodes = new HashSet<>(Arrays.asList(numberingCode.split(",[\\s]*")));

			// 採番可能なアクションか
			boolean isActionNumberable =
					(d.fireIfNormalAction && eq(ActionType.NORMAL, ctx.actionType))
					|| (!d.fireIfNormalAction);

			return (values.isEmpty() || isEmpty(values.get(NUMBERING)))	// 未採番
					&& RenderMode.RUNTIME == ctx.renderMode				// 実行時のみ
					&& DcType.isInputable(dcType)						// 表示条件＝入力可
					&& ecResults.get(htmlId).enabled					// 有効条件＝有効
					&& !numberingCodes.isEmpty()						// アクティビティ定義で採番コードあり
					&& numberingCodes.contains(d.numberingCode)			// パーツとアクティビティ定義で採番コードが一致
					&& isActionNumberable;								// 採番可能なアクションか
		}
		return false;
	}
}
