package jp.co.nci.iwf.endpoint.vd.vd0144;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.script.ScriptException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.designer.DesignerCodeBook.CalcItemType;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.service.CalculateService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 計算式設定サービス.
 */
@BizLogic
public class Vd0144Service extends BaseService {

	@Inject
	private MwmLookupService lookup;
	@Inject
	private CalculateService calculate;

	/**
	 * 初期化.
	 * @param req
	 * @return
	 */
	public Vd0144Response init(Vd0144Request req) {
		// 算術演算子一覧
		final Map<String, String> arithmeticOperators = lookup.getNameMap(LookupGroupId.ARITHMETIC_OPERATOR);
		// 括弧一覧
		final Map<String, String> parentheses = lookup.getNameMap(LookupGroupId.PARENTHESIS);

		final Vd0144Response res = createResponse(Vd0144Response.class, req);
		res.arithmeticOperators = arithmeticOperators;
		res.parentheses = parentheses;
		res.success = true;
		return res;
	}

	/**
	 * 計算式の検証.
	 * @param req
	 * @return
	 */
	public Vd0144Response verify(Vd0144Request req) {
		final Vd0144Response res = createResponse(Vd0144Response.class, req);
		res.success = false;
		if (isEmpty(req.partsCalcName)) {
			// 計算式名が未入力です。
			res.addAlerts("計算式名が未入力です。");
		} else if (isEmpty(req.items)) {
			// 計算式がありません。
			res.addAlerts("計算式がありません。");
		} else {
			res.success = true;
			// 計算項目一覧から計算式を生成
			final String formula = calculate.createCalculateFormula(req.items);
			if (isNotEmpty(formula)) {
				try {
					// 計算処理に必要なダミーの入力値を生成
					Map<Long, String> values = this.createDummyValues(req.items);
					// 計算式が式として正しいかを検証
					Object result = calculate.verify(formula, values);
					System.out.println(result.toString());
				} catch (ScriptException e) {
					// Exceptionが発生した場合、計算式として正しくないのでエラーにする
					res.success = false;
					// 計算式の設定内容に誤りがあります。計算式が正しいか確認してください。
					res.addAlerts(i18n.getText(MessageCd.MSG0128));
				}
			}
		}
		return res;
	}

	/**
	 * 計算式評価用のダミーの入力値を生成.
	 * 入力値はなんでもいいんだけど"0"だと「0除算」の可能性もあるから"1"にしているよ
	 * @param items
	 * @return
	 */
	private Map<Long, String> createDummyValues(List<PartsCalcItem> items) {
		return items.stream().filter(i -> eq(CalcItemType.PARTS, i.calcItemType))
				.collect(Collectors.toMap(
						i -> Long.valueOf(i.calcItemValue),
						i -> "1",
						(k1, k2) -> k2	/* しょせんただのダミーなので、キーが重複したら後勝ちとしておく */
				));
	}
}
