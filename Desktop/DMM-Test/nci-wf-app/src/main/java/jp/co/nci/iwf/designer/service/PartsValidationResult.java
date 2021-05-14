package jp.co.nci.iwf.designer.service;

import java.util.Objects;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツのバリデーション結果エンティティ
 */
public class PartsValidationResult {
	/** バルーン表示対象のパーツHTML ID */
	public String htmlId;
	/** バルーン用の表示メッセージ */
	public String message;
	/** 役割コード（パーツ内の特定エレメントにバルーンを表示したいときに利用） */
	public String role;
	/** パーツ表示ラベル（エラーをバルーンで表示できない時にメッセージとしてエラー表示する際のパーツ特定に利用） */
	public String labelText;

	/** デフォルトコンストラクタ */
	public PartsValidationResult() { }

	/**
	 * コンストラクタ
	 * @param p パーツ
	 * @param labelText パーツの表示ラベル
	 * @param message メッセージ
	 */
	public PartsValidationResult(PartsBase<?> p, String labelText, String message) {
		this.htmlId = p.htmlId;
		this.role = p.defaultRoleCode;
		this.labelText = labelText;
		this.message = message;
	}

	/**
	 * コンストラクタ
	 * @param htmlId パーツのhtmlId
	 * @param role パーツが複数エレメントで構成されているときにどのエレメントでエラーがあったかを特定するためのロール
	 * @param labelText パーツの表示ラベル
	 * @param messageCd メッセージコード定数
	 * @param args メッセージコードのパラメータ
	 */
	public PartsValidationResult(String htmlId, String role, String labelText, MessageCd messageCd, Object...args) {
		this.htmlId = htmlId;
		this.role = role;
		this.labelText = labelText;
		this.message = i18n().getText(messageCd, args);
	}

	/**
	 * コンストラクタ
	 * @param htmlId パーツのhtmlId
	 * @param role パーツが複数エレメントで構成されているときにどのエレメントでエラーがあったかを特定するためのロール
	 * @param labelText パーツの表示ラベル
	 * @param messageCd メッセージコード定数
	 * @param args メッセージコードのパラメータ定数
	 */
	public PartsValidationResult(String htmlId, String role, String labelText, MessageCd messageCd, MessageCd...args) {
		this.htmlId = htmlId;
		this.role = role;
		this.labelText = labelText;
		this.message = i18n().getText(messageCd, args);
	}

	/**
	 * コンストラクタ
	 * @param htmlId パーツのhtmlId
	 * @param role パーツが複数エレメントで構成されているときにどのエレメントでエラーがあったかを特定するためのロール
	 * @param labelText パーツの表示ラベル
	 * @param messageCd メッセージコード定数
	 */
	public PartsValidationResult(String htmlId, String role, String labelText, MessageCd messageCd) {
		this.htmlId = htmlId;
		this.role = role;
		this.labelText = labelText;
		this.message = i18n().getText(messageCd);
	}

	/** 国際化対応サービスを返す */
	private I18nService i18n() {
		return CDI.current().select(I18nService.class).get();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PartsValidationResult) {
			PartsValidationResult o = (PartsValidationResult)obj;
			return MiscUtils.eq(htmlId, o.htmlId)
					&& MiscUtils.eq(role, o.role);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(htmlId, role);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(htmlId)
				.append("[").append(role).append("]=")
				.append(message)
				.toString();
	}
}
