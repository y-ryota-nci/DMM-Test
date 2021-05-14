package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;

/**
 * コンテナ定義のEXCELダウンロード用カラム情報
 */
public class Vd0010Column {
	public Vd0010Column(PartsDesign design, PartsColumn pc) {
		partsCode = design.partsCode;
		labelText = design.labelText;

		if (pc != null) {
			this.columnName = pc.columnName;
			this.columnType = pc.columnTypeLabel;
			this.columnRole = pc.roleCode;
			this.sortOrder = pc.sortOrder;
		}
	}

	/** パーツコード */
	public String partsCode;
	/** ラベル文言 */
	public String labelText;
	/** カラム名 */
	public String columnName;
	/** カラム型 */
	public String columnType;
	/** カラム役割 */
	public String columnRole;
	/** カラム並び順 */
	public Integer sortOrder;
}
