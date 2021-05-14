package jp.co.dmm.customize.endpoint.mg.mg0330;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 国マスタ一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Mg0330Entity extends BaseJpaEntity implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	public String companyCd;
	@Id
	@Column(name="LND_CD")
	public String lndCd;
	@Column(name="LND_NM")
	public String lndNm;
	@Column(name="LND_CD_DJII")
	public String lndCdDjii;
	@Column(name="DLT_FG")
	public String dltFg;
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

}
