package jp.co.nci.iwf.util;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;

/**
 * フィールドの一括バリデーター
 */
public class FieldValidators {
	private I18nService i18n;
	private List<FieldValidator<?>> fields;

	/** コンストラクタ */
	public FieldValidators(I18nService i18n) {
		this.i18n = i18n;
		this.fields = new ArrayList<>();
	}

	/** 新しいフィールドを生成し、追加 */
	public <V extends Comparable<V>> FieldValidator<V> addField(V value, MessageCd...messageCds) {
		final FieldValidator<V> field = new FieldValidator<>(i18n, value, messageCds);
		this.fields.add(field);
		return field;
	}

	/** 新しいフィールドを生成し、追加 */
	public <V extends Comparable<V>> FieldValidator<V> addField(V value, String fieldName) {
		final FieldValidator<V> field = new FieldValidator<>(i18n, value, fieldName);
		this.fields.add(field);
		return field;
	}

	/**
	 * バリデーション
	 * @return
	 */
	public List<String> validate() {
		final List<String> errors = new ArrayList<>();
		for (FieldValidator<?> field : fields) {
			final String error = field.validate();
			if (MiscUtils.isNotEmpty(error)) {
				errors.add(error);
			}
		}
		return errors;
	}
}
