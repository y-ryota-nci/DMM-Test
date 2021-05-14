package jp.co.nci.iwf.endpoint.mm.mm0110;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * システムプロパティ編集画面のエンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Mm0110Entity extends BaseJpaEntity {
	/** プロパティコード */
	@Id
	@Column(name="PROPERTY_CODE")
	public String propertyCode;

	/** デフォルト値 */
	@Column(name="DEFAULT_VALUE")
	public String defaultValue;

	/** デフォルト値の更新日時(排他用) */
	@Column(name="DEFAULT_TIMESTAMP_UPDATED")
	public Timestamp defaultTimestampUpdated;

	/** 説明 */
	@Column(name="PROPERTY_NAME")
	public String propertyName;

	/** プロパティ値 */
	@Column(name="PROPERTY_VALUE")
	public String propertyValue;

	/** 企業コード */
	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	/** 企業毎のプロパティ値 */
	@Column(name="CORPORATION_PROPERTY_VALUE")
	public String corporationPropertyValue;

	/** 企業毎のプロパティ値の更新日時（排他用） */
	@Column(name="CORPORATION_TIMESTAMP_UPDATED")
	public Timestamp corporationTimestampUpdated;
}
