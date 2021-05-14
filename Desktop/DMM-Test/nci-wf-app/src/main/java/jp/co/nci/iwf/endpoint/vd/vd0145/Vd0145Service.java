package jp.co.nci.iwf.endpoint.vd.vd0145;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.script.ScriptException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.service.EvaluateConditionService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * パーツ計算条件設定サービス.
 */
@BizLogic
public class Vd0145Service extends BaseService {

	@Inject
	private MwmLookupService lookup;
	@Inject
	private EvaluateConditionService service;

	/**
	 * 初期化.
	 * @param req
	 * @return
	 */
	public Vd0145Response init(Vd0145Request req) {
		// 論理演算子一覧
		final Map<String, String> ogiclOperators = lookup.getNameMap(LookupGroupId.LOGICAL_OPERATOR);
		// 括弧一覧
		final Map<String, String> parentheses = lookup.getNameMap(LookupGroupId.PARENTHESIS);
		// 比較演算子一覧
		final List<OptionItem> comparisonOperators = lookup.getOptionItems(LookupGroupId.COMPARISON_OPERATOR, false);

		final Vd0145Response res = createResponse(Vd0145Response.class, req);
		res.logiclOperators     = ogiclOperators;
		res.parentheses         = parentheses;
		res.comparisonOperators = comparisonOperators;
		res.success = true;
		return res;
	}

	/**
	 * 計算条件の評価式の検証.
	 * @param req
	 * @return
	 */
	public Vd0145Response verify(Vd0145Request req) {
		final Vd0145Response res = createResponse(Vd0145Response.class, req);
		res.success = true;
		// 設定された有効条件から評価式を生成
		final String formula = service.createEvaluateFormula(req.ecs);
		if (isNotEmpty(formula)) {
			try {
				// 評価式が式として正しいかを検証
				service.verify(formula, new HashMap<Long, String>());
			} catch (ScriptException e) {
				// Exceptionが発生した場合、評価式として正しくないのでエラーにする
				res.success = false;
				// 計算条件の設定内容に誤りがあります。条件式が正しいか確認してください。
				res.addAlerts(i18n.getText(MessageCd.MSG0128));
			}
		}
		return res;
	}
}
