package jp.co.nci.iwf.endpoint.mm.mm0020;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 表示条件設定でのパーツエンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Mm0020Parts extends BaseJpaEntity {
	@Id
	@Column(name="PARTS_ID")
	public long partsId;

	@Column(name="PARTS_CODE")
	public String partsCode;

	@Column(name="LABEL_TEXT")
	public String labelText;
}
