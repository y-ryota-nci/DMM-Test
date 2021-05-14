package jp.co.dmm.customize.endpoint.dc.dc1000;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * OCR状況一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Dc1000Entity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FILE_DATA_ID")
	public Long docFileDataId;
	@Column(name="APPLICATION_NO")
	public String applicationNo;
	@Column(name="FILE_NAME")
	public String fileName;
	@Column(name="OCR_FLAG")
	public String ocrFlag;
	@Column(name="OCR_FLAG_NM")
	public String ocrFlagNm;
	@Column(name="OCR_EXECUTION_DATE")
	public Timestamp ocrExecutionDate;

}

