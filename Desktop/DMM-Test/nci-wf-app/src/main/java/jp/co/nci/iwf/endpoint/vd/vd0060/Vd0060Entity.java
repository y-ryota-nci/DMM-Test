package jp.co.nci.iwf.endpoint.vd.vd0060;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 選択肢一覧エンティティ
 */
@Entity(name="MWM_OPTION")
@Access(AccessType.FIELD)
public class Vd0060Entity extends BaseJpaEntity {

	@Id
	@Column(name="OPTION_ID")
	public long optionId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="OPTION_CODE")
	public String optionCode;

	@Column(name="OPTION_NAME")
	public String optionName;

	@Column(name="VERSION")
	public Long version;

	/** この選択肢を使用中のコンテナ */
	@Transient
	public String containerUsingOption;
}
