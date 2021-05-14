package jp.co.dmm.customize.endpoint.sp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import jp.co.dmm.customize.jpa.entity.mw.ZipMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 郵便番号マスタEntity
 *
 */
@Entity
@Access(AccessType.FIELD)
public class ZipMstEntity extends BaseJpaEntity {

	@EmbeddedId
	private ZipMstPK id;

	/** 郵便番号 **/
	@Column(name="ZIP_CD")
	public String zipCd;
	/** 都道府県コード */
	@Column(name="ADR_PRF_CD")
	public String adrPrfCd;
	/** 都道府県 */
	@Column(name="ADR_PRF")
	public String adrPrf;
	/** 市区町村 */
	@Column(name="ADR1")
	public String adr1;
	/** 町名/番地 */
	@Column(name="ADR2")
	public String adr2;

}
