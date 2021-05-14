package jp.co.nci.iwf.endpoint.mm.mm0060;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * ブロック表示順設定での表示条件マスタ
 */
@Entity
public class Mm0060Dc extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DC_ID")
	private Long dcId;

	@Column(name="DC_NAME")
	private String dcName;

	/** 使用するか */
	@Transient
	private boolean selected;

	public Long getDcId() {
		return dcId;
	}
	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}

	public String getDcName() {
		return dcName;
	}
	public void setDcName(String dcName) {
		this.dcName = dcName;
	}

	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
