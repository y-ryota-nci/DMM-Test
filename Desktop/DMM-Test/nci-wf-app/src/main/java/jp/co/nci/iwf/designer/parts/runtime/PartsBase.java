package jp.co.nci.iwf.designer.parts.runtime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.InternalServerErrorException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.CalculateCondition;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【実行時】パーツの基底クラス
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)	// クラスのFQCNをJSONフィールドに出力。クライアントから書き戻す際に必要
public abstract class PartsBase<D extends PartsDesign> extends MiscUtils implements Serializable, Comparable<PartsBase<D>> {
	/** パーツ種別 */
	public int partsType;
	/** パーツID */
	public long partsId;
	/** HTMLレンダリングする際のID。子画面を含めてユニークとなる */
	public String htmlId;
	/** 表示条件設定 */
	public int dcType;
	/** 役割コードをキーとしたパーツ値の格納用Map */
	public Map<String, String> values = new HashMap<>();
	/** 役割コードのデフォルト。この役割コードに対応した値がこのパーツの値 */
	public String defaultRoleCode;
	/** 条件評価データ */
	public EvaluateCondition evaluateCondition;
	/** 計算式データ */
	public CalculateCondition calculateCondition;
	/** 汎用マスタ検索パーツ（自身がトリガーパーツの時に設定される） */
	public Set<String> ajaxTriggers;

	/** 業務管理項目用に値を抜き出す  */
	public String toBusinessInfoValue(D d, DesignerContext ctx) {
		if (isEmpty(defaultRoleCode))
			return null;
		return values.get(defaultRoleCode);
	}

	/** バリデーション */
	public abstract PartsValidationResult validate(D d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults);

	/**
	 * パーツの値の取得。
	 *
	 */
	@JsonIgnore
	public String getValue() {
		// 「パーツの値」は、代表値としてデフォルト役割コードの示す値とする。
		// デフォルト役割コードが未定義なら、そもそもそのパーツは値を持たないとみなす
		if (defaultRoleCode == null)
			return null;
		return defaults(values.get(defaultRoleCode), "");
	}

	/**
	 * パーツの値の取得。
	 * 引数の役割コードに応じた値を戻す
	 * @param roleCode 役割コード
	 * @return
	 */
	@JsonIgnore
	public String getValue(String roleCode) {
		return defaults(values.get(roleCode), "");
	}

	@Override
	public int compareTo(PartsBase<D> o) {
		return MiscUtils.compareTo(o.htmlId, o.htmlId);
	}

	/** パーツにユーザデータを反映 */
	public void fromUserData(D d, Map<String, Object> userData) {
		// ユーザデータがあるなら、ユーザデータを正として既存のデフォルト値は消す
		values.clear();

		for (PartsColumn pc : d.columns) {
			final Object o = userData.get(pc.columnName);
			values.put(pc.roleCode, (o == null ? "" : o.toString()));
		}
	}

	/** パーツからユーザデータへ値を抜き出し  */
	public void toUserData(D d, Map<String, Object> userData, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		for (PartsColumn pc : d.columns) {
			userData.put(pc.columnName, values.get(pc.roleCode));
		}
	}

	/** ユーザデータ更新後の、パーツ固有の後処理 */
	public void afterUpdateUserData(D d, RuntimeContext ctx, PartsContainerBase<?> pc, PartsContainerRow row, Map<String, EvaluateCondition> ecCache) {
	}

	/** 現状の値をクリアしてデフォルト値をセット */
	public void clearAndSetDefaultValue(PartsDesign d) {
		if (defaultRoleCode != null) {
			if (values == null)
				values = new HashMap<>();
			else
				values.clear();

			values.put(defaultRoleCode, d.defaultValue);
		}
	}

	/**
	 * パーツへ値を設定（デフォルト役割コードへ対して）
	 * @param val 設定する値
	 */
	public void setValue(String val) {
		if (isEmpty(defaultRoleCode)) {
			throw new InternalServerErrorException("デフォルトの役割コードが定義されていない（すなわち複数値を取り得る、または値を持たないパーツ）に対して setValue()しようとしました");
		}
		values.put(defaultRoleCode, val);
	}

	/**
	 * 役割コードを指定してパーツへ値を設定
	 * @param roleCode 役割コード
	 * @param val 設定する値
	 */
	public void setValue(String roleCode, String val) {
		if (isEmpty(roleCode)) {
			throw new InternalServerErrorException("役割コードが未指定のまま setValue()しようとしました");
		}
		values.put(roleCode, val);
	}

	/**
	 * パーツへ値を設定（既存値を破棄して、役割コードをキーとしたMapで全置換）
	 * @param  newValues 役割コードをキーとしたMap
	 */
	public void setValue(Map<String, String> newValues) {
		values.clear();
		if (newValues != null) {
			values.putAll(newValues);
		}
	}
}
