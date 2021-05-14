package jp.co.nci.iwf.designer.parts;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsRelation;

/**
 * パーツ間の関連
 */
public class PartsRelation {
	/** 対象パーツID */
	public Long targetPartsId;
	/** パーツ間の関係を示すコード */
	public String columnName;
	/** パーツI/O区分 */
	public String partsIoType;
	/** 並び順 */
	public Integer sortOrder;
	/** 幅 */
	public Integer width;
	/** 変更非通知フラグ */
	public boolean noChangeEventFlag;

	/** コンストラクタ */
	public PartsRelation(String partsIoType) {
		this.partsIoType = partsIoType;
		this.sortOrder = 1;
	}

	/** コンストラクタ */
	public PartsRelation(String columnName, int width, String partsIoType, int order, boolean noChangeEventFlag) {
		this.columnName = columnName;
		this.partsIoType = partsIoType;
		this.sortOrder = order;
		this.width = width;
		this.noChangeEventFlag = noChangeEventFlag;
	}

	/** コンストラクタ */
	public PartsRelation() {
	}

	/** コンストラクタ */
	public PartsRelation(MwmPartsRelation src) {
		targetPartsId = src.getTargetPartsId();
		columnName = src.getColumnName();
		partsIoType = src.getPartsIoType();
		sortOrder = src.getSortOrder();
		width = src.getWidth();
		noChangeEventFlag = CommonFlag.ON.equals(src.getNoChangeEventFlag());
	}
}
