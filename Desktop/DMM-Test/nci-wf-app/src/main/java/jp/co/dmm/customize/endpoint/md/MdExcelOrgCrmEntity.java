package jp.co.dmm.customize.endpoint.md;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 取引先マスタインポート用反社情報エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MdExcelOrgCrmEntity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NO")
	public long no;
	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 連番 */
	@Column(name="SQNO")
	public String sqno;
	/** プロファイルID */
	@Column(name="PEID")
	public String peid;
	/** 一致名称 */
	@Column(name="MTCH_NM")
	public String mtchNm;
	/** 国コード */
	@Column(name="LND_CD")
	public String lndCd;
	/** 性別 */
	@Column(name="GND_TP")
	public String gndTp;
	/** 生年月日 */
	@Column(name="BRTH_DT")
	public String brthDt;
	/** エラー */
	public String errorText;

}
