package jp.co.dmm.customize.endpoint.mg.mg0331;

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
public class Mg0331Entity extends BaseJpaEntity implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LND_CD")
	public String lndCd;
	@Column(name="LND_NM")
	public String lndNm;
	@Column(name="LND_CD_DJII")
	public String lndCdDjii;
	@Column(name="SORT_ORDER")
	public String sortOrder;
	@Column(name="DLT_FG")
	public String dltFg;

}
