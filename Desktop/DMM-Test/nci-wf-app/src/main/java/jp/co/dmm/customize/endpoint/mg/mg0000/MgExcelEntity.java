package jp.co.dmm.customize.endpoint.mg.mg0000;

import javax.persistence.Column;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

public abstract class MgExcelEntity extends BaseJpaEntity {

	/** 処理区分 */
	@Column(name="PROCESS_TYPE")
	public String processType;

	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;

	/** エラー */
	public String errorText;
}
