package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 汎用テーブル検索条件エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MwmTableSearchEx extends BaseJpaEntity {
	@Id
	@Column(name="TABLE_SEARCH_ID")
	public long tableSearchId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DEFAULT_SEARCH_FLAG")
	public String defaultSearchFlag;

	@Column(name="DISPLAY_NAME")
	public String displayName;

	@Column(name="TABLE_ID")
	public Long tableId;

	@Column(name="TABLE_SEARCH_CODE")
	public String tableSearchCode;

	@Column(name="TABLE_SEARCH_NAME")
	public String tableSearchName;

	@Column(name="TABLE_NAME")
	public String tableName;

	@Version
	@Column(name="VERSION")
	public Long version;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="DELETE_FLAG_NAME")
	public String deleteFlagName;
}
