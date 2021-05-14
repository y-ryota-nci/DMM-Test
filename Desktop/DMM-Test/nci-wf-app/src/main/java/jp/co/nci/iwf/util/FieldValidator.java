package jp.co.nci.iwf.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;

/**
 * フィールドのバリデーター
 *
 * @param <V> フィールドの値の型
 */
public class FieldValidator<V extends Comparable<V>> {
	private I18nService i18n;

	/** フィールド名（エラー項目表示用） */
	private String fieldName;
	/** チェック対象となる値 */
	private V value;
	/** 必須チェック有無 */
	private boolean required = false;
	/** 最大文字数（バイト数ではない） */
	private int maxLength = Integer.MAX_VALUE;
	/** 最大文字数（バイト数） */
	private int maxByteLength = Integer.MAX_VALUE;
	/** 最大値 */
	private V max;
	/** 最小値 */
	private V min;
	/** 半角英数チェック有無 */
	private boolean alphaNumeric;
	/** 半角英数記号チェック有無 */
	private boolean alphaNumericSymbol;
	/** 半角のみ */
	private boolean halfWidthOnly;
	/** 数値チェック有無 */
	private boolean numeric;
	/** 整数チェック有無 */
	private boolean integer;
	/** 日付チェック有無 */
	private boolean date;
	/** マスタ存在チェック用コレクション */
	private Collection<V> masters;
	/** マスタ存在チェックの除外条件値 */
	private V exclusion;
	/** マスタ存在チェックするときの実際の値 */
	private V mastersValue;
	/** 各種比較バリデーターの比較対象値 */
	private V compareValue;
	/** 比較対象バリデーターの比較対象フィールド名（エラー項目表示用） */
	private String compareFieldName;
	/** 比較値以上チェック有無 */
	private boolean gte;
	/** 比較値以下チェック有無 */
	boolean lte;
	/** 自社企業コード */
	private String myCorporationCode;
	/** 重複チェック用コレクション */
	private Collection<V> duplicates;
	/** 重複チェック時の実際の比較値 */
	private V duplicateValue;
	/** コード定義コレクション */
	private Collection<V> includes;

	/**
	 * コンストラクタ
	 * @param value 値
	 * @param fieldName フィールド名（エラー項目表示用）
	 */
	public FieldValidator(I18nService i18n, V value, String fieldName) {
		assert MiscUtils.isNotEmpty(fieldName) : "フィールド名がNULL";
		assert i18n != null : "多言語サービスがNULL";

		this.fieldName = fieldName;
		this.value = value;
		this.i18n = i18n;
	}

	/**
	 * コンストラクタ
	 * @param value 値
	 * @param messageCd フィールド名のメッセージ定数Enum
	 */
	public FieldValidator(I18nService i18n, V value, MessageCd... messageCds) {
		assert i18n != null : "多言語サービスがNULL";
		assert messageCds != null && messageCds.length > 0 : "MessageCdが空";

		this.fieldName = i18n.getText(messageCds);
		this.value = value;
		this.i18n = i18n;
	}

	/** 必須チェックを行う */
	public FieldValidator<V> required() {
		this.required = true;
		return this;
	}

	/**
	 * 必須チェックを行う
	 * @param required 必須チェックするならtrue
	 * @return
	 */
	public FieldValidator<V> required(boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * 最大値チェック
	 * @param max 最大値
	 * @return
	 */
	public FieldValidator<V> max(V max) {
		this.max = max;
		return this;
	}

	/**
	 * 最小値チェック
	 * @param min 最小値
	 * @return
	 */
	public FieldValidator<V> min(V min) {
		this.min = min;
		return this;
	}

	/**
	 * 範囲チェック
	 * @param min 最小値
	 * @param max 最大値
	 * @return
	 */
	public FieldValidator<V> range(V min, V max) {
		this.min = min;
		this.max = max;
		return this;
	}

	/**
	 * 半角英数チェック（最大文字数チェックあり）
	 * @param maxLength 最大文字数（バイト数ではない）
	 * @return
	 */
	public FieldValidator<V> alphaNumeric(int maxLength) {
		this.alphaNumeric = true;
		this.maxLength = maxLength;
		return this;
	}

	/**
	 * 半角英数記号チェック（最大文字数チェックあり）
	 * @param maxLength 最大文字数（バイト数ではない）
	 * @return
	 */
	public FieldValidator<V> alphaNumericSymbol(int maxLength) {
		this.alphaNumericSymbol = true;
		this.maxLength = maxLength;
		return this;
	}

	/**
	 * 半角のみ（最大文字数チェックあり）
	 * @param maxLength 最大文字数（バイト数ではない）
	 * @return
	 */
	public FieldValidator<V> halfWidthOnly(int maxLength) {
		this.halfWidthOnly = true;
		this.maxLength = maxLength;
		return this;
	}

	/** 半角数字チェック */
	public FieldValidator<V> numeric() {
		this.numeric = true;
		return this;
	}

	/** 半角整数チェック */
	public FieldValidator<V> integer() {
		this.integer = true;
		return this;
	}

	/**
	 * 型チェックなし（最大文字数チェックあり）
	 * @param maxLength 最大文字数（バイト数ではない）
	 * @return
	 */
	public FieldValidator<V> anyString(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	/**
	 * 型チェックなし（最大バイト数チェックあり）
	 * @param maxLength 最大バイト数
	 * @return
	 */
	public FieldValidator<V> anyByteString(int maxLength) {
		this.maxByteLength = maxLength;
		return this;
	}

	/** 日付チェック */
	public FieldValidator<V> date() {
		this.date = true;
		return this;
	}

	/**
	 * 値がマスタに存在するか（除外条件なし）【マスタ存在チェック用】
	 * （include()との違いは、エラーメッセージが異なるか、除外条件が指定できるのみ）
	 * @param masters マスタ存在チェック対象のコレクション。このコレクション内に値があればOK
	 * @return
	 */
	public FieldValidator<V> master(Collection<V> masters) {
		return master(masters, null, this.value);
	}

	/**
	 * 値がマスタに存在するか（除外条件あり）【マスタ存在チェック用】
	 * （include()との違いは、エラーメッセージが異なるか、除外条件が指定できるのみ）
	 * @param masters マスタ存在チェック対象のコレクション。このコレクション内に値があればOK
	 * @param exclusion 除外条件する値。存在チェックする値＝除外条件する値ならチェックしない（例えば自分自身をチェック対象から外したいときに使う）
	 * @return
	 */
	public FieldValidator<V> master(Collection<V> masters, V exclusion) {
		return master(masters, exclusion, this.value);
	}

	/**
	 * 値がマスタに存在するか（除外条件あり）。
	 * （include()との違いは、エラーメッセージが異なるか、除外条件が指定できるのみ）
	 * @param masters マスタ存在チェック対象のコレクション。このコレクション内に値があればOK
	 * @param exclusion 除外条件する値。存在チェックする値＝除外条件する値ならチェックしない（例えば自分自身をチェック対象から外したいときに使う）
	 * @param value 存在チェックするときの実際の値(例えば組織CDでチェックするのではなく企業CD＋組織CDでチェックしたいときに フィールド値だけだと比較できないケースに使用する)
	 * @return
	 */
	public FieldValidator<V> master(Collection<V> masters, V exclusion, V value) {
		this.masters = masters;
		this.mastersValue = value;
		this.exclusion = exclusion;
		return this;
	}

	/**
	 * 値がコード定義に存在するか
	 * （master()との違いは、エラーメッセージが異なるか、除外条件が指定できるのみ）
	 * @param includes コード定義を示すコレクション。このコレクション内に値があればOK
	 * @return
	 */
	public FieldValidator<V> include(Collection<V> includes) {
		this.includes = includes;
		return this;
	}

	/**
	 * 比較値以上か（値≧比較値）。日付なら比較日以降か
	 * @param compareValue 比較値
	 * @param fieldNameCd フィールド名
	 * @return
	 */
	public FieldValidator<V> gte(V compareValue, MessageCd fieldNameCd) {
		this.compareValue = compareValue;
		this.compareFieldName = i18n.getText(fieldNameCd);
		this.gte = true;
		return this;
	}

	/**
	 * 比較値以下か（値≦比較値）。日付なら比較日以前か
	 * @param compareValue 比較値
	 * @param fieldNameCd フィールド名
	 * @return
	 */
	public FieldValidator<V> lte(V compareValue, MessageCd fieldNameCd) {
		this.compareValue = compareValue;
		this.compareFieldName = i18n.getText(fieldNameCd);
		this.lte = true;
		return this;
	}

	/** 企業コードが自企業のものか */
	public FieldValidator<V> myCorporationCode(String corporationCode) {
		this.myCorporationCode = corporationCode;
		return this;
	}

	/**
	 * 重複レコードの有無チェック
	 * @param duplicates チェックした結果を格納するコレクション
	 * @param duplicateValue 重複チェックするときの実際の値
	 * @return
	 */
	public FieldValidator<V> duplicate(Collection<V> duplicates, V duplicateValue) {
		this.duplicates = duplicates;
		this.duplicateValue = duplicateValue;
		return this;
	}

	/**
	 * バリデーション
	 * @return エラー文字列のリスト。エラー無しなら空リスト
	 */
	public String validate() {
		final List<String> errors = new ArrayList<>();

		// 必須
		if (MiscUtils.isEmpty(value)) {
			if (required)
				errors.add(i18n.getText(MessageCd.MSG0001, fieldName));
		} else {
			// 最大文字数（バイト数でない）
			if (maxLength > 0 && value.toString().length() > maxLength) {
				errors.add(i18n.getText(MessageCd.MSG0023, fieldName, maxLength));
			}
			// 最大バイト数
			if (maxByteLength > 0 && value.toString().getBytes().length > maxByteLength) {
				errors.add(i18n.getText(MessageCd.MSG0283, fieldName, maxByteLength));
			}
			// 半角英数アンダースコア
			if (alphaNumeric && !ValidatorUtils.isAlphaNumberUnderscore(value.toString())) {
				errors.add(i18n.getText(MessageCd.MSG0260, fieldName));
			}
			// 半角英数記号
			if (alphaNumericSymbol && !ValidatorUtils.isAlphaNumberSymbol(value.toString())) {
				errors.add(i18n.getText(MessageCd.MSG0084, fieldName));
			}
			// 半角のみ
			if (halfWidthOnly && !ValidatorUtils.isHalfWidthOnly(value.toString())) {
				errors.add(i18n.getText(MessageCd.MSG0282, fieldName));
			}
			// 日付
			if (date) {
				if (value instanceof String) {
					if (!ValidatorUtils.isYMD(value.toString()))
						errors.add(i18n.getText(MessageCd.MSG0009, fieldName));
				} else if (!(value instanceof java.util.Date)) {
					errors.add(i18n.getText(MessageCd.MSG0009, fieldName));
				}
			}
			// 整数
			if (integer) {
				// 半角整数
				if (value instanceof String) {
					if (!ValidatorUtils.isInteger(value.toString()))
						errors.add(i18n.getText(MessageCd.MSG0261, fieldName));
				} else if (!(value instanceof Number)) {
					errors.add(i18n.getText(MessageCd.MSG0261, fieldName));
				}
			}

			// 数値
			if (numeric) {
				// 半角数字
				if (value instanceof String) {
					if (!ValidatorUtils.isNumeric(value.toString()))
						errors.add(i18n.getText(MessageCd.MSG0261, fieldName));
				} else if (!(value instanceof Number)) {
					errors.add(i18n.getText(MessageCd.MSG0261, fieldName));
				}
			}
			// 範囲
			if (max != null && MiscUtils.compareTo(value, max) > 0) {
				// 最大値
				errors.add(i18n.getText(MessageCd.MSG0019, fieldName, max));
			} else if (min != null && MiscUtils.compareTo(value, min) < 0) {
				// 最小値
				errors.add(i18n.getText(MessageCd.MSG0017, fieldName, min));
			}
			// マスタへの存在チェック
			if (masters != null && !masters.contains(mastersValue)) {
				// 存在チェックの除外条件がない、または除外条件はあるが該当しないなら存在しないとみなす
				if (exclusion == null || !MiscUtils.eq(mastersValue, exclusion))
					errors.add(i18n.getText(MessageCd.MSG0156, fieldName));
			}
			// コード定義への存在チェック
			if (includes != null && !includes.contains(value)) {
				if (includes.size() <= 10) // 定義が10以下なら値を直接表示してやる。それ以上ならメッセージが長すぎるのでしない
					errors.add(i18n.getText(MessageCd.MSG0266, fieldName, MiscUtils.toStr(includes)));
				else
					errors.add(i18n.getText(MessageCd.MSG0136, fieldName));
			}
			// 以上 or 以降
			if (gte && MiscUtils.compareTo(value, compareValue) < 0) {
				if (date)
					errors.add(i18n.getText(MessageCd.MSG0095, fieldName, compareFieldName));
				else
					errors.add(i18n.getText(MessageCd.MSG0017, fieldName, compareFieldName));
			}
			// 以下 or 以前
			if (lte && MiscUtils.compareTo(value, compareValue) > 0) {
				if (date)
					errors.add(i18n.getText(MessageCd.MSG0094, fieldName, compareFieldName));
				else
					errors.add(i18n.getText(MessageCd.MSG0019, fieldName, compareFieldName));
			}
			// 自社の企業コードか
			if (myCorporationCode != null && !myCorporationCode.equals(value)) {
				errors.add(i18n.getText(MessageCd.MSG0264, MessageCd.corporationCode));
			}
			// 重複レコードがないか
			if (duplicates != null && duplicates.contains(duplicateValue)) {
				errors.add(i18n.getText(MessageCd.MSG0265));
			}
		}
		// ウザいから、最初のエラーだけを返す
		return (errors.isEmpty() ? null : errors.get(0));
	}
}
