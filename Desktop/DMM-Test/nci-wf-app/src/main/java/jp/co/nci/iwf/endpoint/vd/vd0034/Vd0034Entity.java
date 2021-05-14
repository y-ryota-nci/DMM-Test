package jp.co.nci.iwf.endpoint.vd.vd0034;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;


/** パーツ属性コピー画面のパーツ一覧エンティティ */
@Entity
@Access(AccessType.FIELD)
public class Vd0034Entity extends BaseJpaEntity {
	/** コンストラクタ */
	public Vd0034Entity() {
	}

	/** パーツID */
	@Id
	@Column(name="PARTS_ID")
	public long partsId;
	/** パーツの表示ラベル */
	@Column(name="LABEL_TEXT")
	public String labelText;
	@Column(name="PARTS_TYPE")
	public int partsType;
	@Column(name="SCREEN_ID")
	public Long screenId;
	@Column(name="SCREEN_NAME")
	public String screenName;
	@Column(name="CONTAINER_ID")
	public Long containerId;

	@Transient
	public String partsTypeName;
	/** パーツ条件をコピーするか */
	@Transient
	public boolean condFlag;
	/** パーツ計算式をコピーするか */
	@Transient
	public boolean calcFlag;
	/** 画面パーツ計算式数 */
	@Transient
	public boolean canCopyCalcs;
	/** 画面パーツ条件数 */
	@Transient
	public boolean canCopyConds;
	/** デザインコード */
	@Transient
	public String designCode;
}
