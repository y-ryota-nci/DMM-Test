package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.DesignerCodeBook.ItemClass;
import jp.co.nci.iwf.designer.DesignerCodeBook.Operator;
import jp.co.nci.iwf.designer.DesignerCodeBook.StaticJavascript;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 条件評価サービス.
 */
@BizLogic
public class EvaluateConditionService extends BaseRepository {

	/** ScriptEngine */
	private ScriptEngine engine;
	/** 画面Javascriptサービス*/
	@Inject private JavascriptService js;

	/** ロガー */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(EvaluateConditionService.class);

	/**
	 * イニシャライザ
	 */
	@PostConstruct
	public void init() {
		try {
			engine = new ScriptEngineManager().getEngineByName("nashorn");
			engine.eval(js.getStaticJavascript(StaticJavascript.EnabledEvaluate).contents);
		} catch (ScriptException e) {
			throw new InternalServerErrorException("enabledEvaluate.jsの読込みでエラーが発生しました。", e);
		}
	}

	/**
	 * 条件の評価式の生成.
	 * @param items 有効条件一覧
	 * @return 評価式
	 */
	public String createEvaluateFormula(List<? extends PartsCondItem> items) {
		StringBuilder formula = new StringBuilder();
		if (items != null) {
			for (PartsCondItem item : items) {
				if (eq(ItemClass.LOGICAL_OPERATOR, item.itemClass) || eq(ItemClass.PARENTHESIS, item.itemClass)) {
					formula.append(item.condType);
				} else if (eq(ItemClass.PARTS, item.itemClass)) {
					boolean isNumeric = isNotEmpty(item.numericFlag) && toBool(item.numericFlag);
					String func = Operator.toFunction(item.operator);
					String val1 = "'{" + item.condType + "}'";
					String val2 = isNotEmpty(item.targetPartsId) ? "'{" + item.targetPartsId + "}'" : "'"+escapeLiteral(item.targetLiteralVal)+"'";
					formula.append(func)
						   .append("(")
						   .append(val1)
						   .append(",")
						   .append(val2)
						   .append(",")
						   .append(isNumeric)
						   .append(")");
				}
			}
		}
		return formula.toString();
	}

	/**
	 * 条件の評価式と入力値を受け取り、評価を行う
	 * @param formula 評価式
	 * @param values 入力値
	 * @return true：有効
	 */
	public boolean evaluate(String formula, Map<Long, String> values) {
		try {
			if (isNotEmpty(formula) && isNotEmpty(values)) {
				Object result = this.verify(formula, values);
				return toBool(result.toString());
			}
			return true;
		} catch (ScriptException e) {
			throw new InternalServerErrorException("有効条件判定処理にてスクリプトエラー発生！", e);
		}
	}

	/**
	 * 条件式の評価.
	 * @param formula 評価式
	 * @param values 入力値
	 * @return 評価結果
	 * @throws ScriptException スクリプトエラーが発生した場合
	 */
	public Object verify(String formula, Map<Long, String> values) throws ScriptException {
		final List<String> targets = new ArrayList<String>();
		final List<String> replace = new ArrayList<String>();
		values.keySet().stream().forEach(k -> {
			targets.add("{" + k + "}");
			replace.add(escapeLiteral(values.get(k)));
		});
		String function = StringUtils.replaceEach(formula, targets.toArray(new String[0]), replace.toArray(new String[0]));
		Object result = engine.eval(function);
		return result;
	}

	/**
	 * 条件判定先パーツIDからみた条件判定元パーツのパーツID一覧Map。
	 * （条件のトリガーパーツIDとそのパーツが変更されたことで影響を受けるパーツIDでMap化）
	 * @param designMap
	 * @return
	 */
	public Map<Long, Set<Long>> createTargetMap(Map<Long, PartsDesign> designMap) {
		final Map<Long, Set<Long>> result = new HashMap<Long, Set<Long>>();
		for (PartsDesign design : designMap.values()) {
			for (PartsCond cond : design.partsConds) {
				for (PartsCondItem item : cond.items) {
					if (eq(ItemClass.PARTS, item.itemClass)) {
						Long partsId = Long.valueOf(item.condType);
						if (!result.containsKey(partsId)) {
							result.put(partsId, new HashSet<Long>());
						}
						result.get(partsId).add(cond.partsId);
						if (item.targetPartsId != null) {
							if (!result.containsKey(item.targetPartsId)) {
								result.put(item.targetPartsId, new HashSet<Long>());
							}
							result.get(item.targetPartsId).add(cond.partsId);
						}
					}
				}
			}
		}
		return result;
	}

	private String escapeLiteral(String literal) {
		if (isEmpty(literal)) return StringUtils.defaultString(literal);
		// 「'」「\」をエスケープ
		return literal.replace("\\", "\\\\").replace("\'", "\\'");
	}
}
