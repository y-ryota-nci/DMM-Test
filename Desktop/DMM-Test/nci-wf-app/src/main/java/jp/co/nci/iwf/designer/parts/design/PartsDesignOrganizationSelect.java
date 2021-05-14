package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.DefaultOrganizationSelect;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsButtonSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsEvents;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleOrganizationSelect;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsOrganizationSelect;

/**
 * 【デザイン時】組織選択パーツ
 */
public class PartsDesignOrganizationSelect extends PartsDesign
		implements RoleOrganizationSelect, DefaultOrganizationSelect, PartsEvents {

	/** ボタンサイズ */
	public int buttonSize;
	/** 表示項目と各項目毎の設定値 */
	// 組織名称
	public boolean organizationNameDisplay;
	public Integer organizationNameSortOrder;
	public Integer organizationNameColLg;
	public Integer organizationNameColMd;
	public Integer organizationNameColSm;
	public Integer organizationNameColXs;
	// 組織付加情報
	public boolean organizationAddedInfoDisplay;
	public Integer organizationAddedInfoSortOrder;
	public Integer organizationAddedInfoColLg;
	public Integer organizationAddedInfoColMd;
	public Integer organizationAddedInfoColSm;
	public Integer organizationAddedInfoColXs;
	// 組織略称
	public boolean organizationNameAbbrDisplay;
	public Integer organizationNameAbbrSortOrder;
	public Integer organizationNameAbbrColLg;
	public Integer organizationNameAbbrColMd;
	public Integer organizationNameAbbrColSm;
	public Integer organizationNameAbbrColXs;
	// 電話番号
	public boolean telNumDisplay;
	public Integer telNumSortOrder;
	public Integer telNumColLg;
	public Integer telNumColMd;
	public Integer telNumColSm;
	public Integer telNumColXs;
	// FAX番号
	public boolean faxNumDisplay;
	public Integer faxNumSortOrder;
	public Integer faxNumColLg;
	public Integer faxNumColMd;
	public Integer faxNumColSm;
	public Integer faxNumColXs;
	// メールアドレス
	public boolean addressDisplay;
	public Integer addressSortOrder;
	public Integer addressColLg;
	public Integer addressColMd;
	public Integer addressColSm;
	public Integer addressColXs;

	/** 組織選択パーツ固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"buttonSize",
			"organizationNameDisplay",
			"organizationNameSortOrder",
			"organizationNameColLg",
			"organizationNameColMd",
			"organizationNameColSm",
			"organizationNameColXs",
			"organizationAddedInfoDisplay",
			"organizationAddedInfoSortOrder",
			"organizationAddedInfoColLg",
			"organizationAddedInfoColMd",
			"organizationAddedInfoColSm",
			"organizationAddedInfoColXs",
			"organizationNameAbbrDisplay",
			"organizationNameAbbrSortOrder",
			"organizationNameAbbrColLg",
			"organizationNameAbbrColMd",
			"organizationNameAbbrColSm",
			"organizationNameAbbrColXs",
			"telNumDisplay",
			"telNumSortOrder",
			"telNumColLg",
			"telNumColMd",
			"telNumColSm",
			"telNumColXs",
			"faxNumDisplay",
			"faxNumSortOrder",
			"faxNumColLg",
			"faxNumColMd",
			"faxNumColSm",
			"faxNumColXs",
			"addressDisplay",
			"addressSortOrder",
			"addressColLg",
			"addressColMd",
			"addressColSm",
			"addressColXs",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		// 新規時は組織名称のみ表示対象
		organizationNameDisplay = true;
		organizationNameColXs = 3;
		// 並び順は下記
		organizationNameSortOrder = 1;
		organizationAddedInfoSortOrder = 2;
		organizationNameAbbrSortOrder = 3;
		telNumSortOrder = 4;
		faxNumSortOrder = 5;
		addressSortOrder = 6;
		// ボタンサイズ
		buttonSize = PartsButtonSize.NORMAL;
	}

	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsOrganizationSelect parts = new PartsOrganizationSelect();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = ORGANIZATION_CODE;
		parts.clearAndSetDefaultValue(this);
		return parts;
	}

	@Override
	public String[] extFieldNames() {
		return extFieldNames;
	}

	@Override
	public List<PartsColumn> newColumns() {
		// カラムとして定義できるのは以下の項目
		// 必須：会社コード、組織コード
		// 任意：組織名称、組織付加情報、組織略称、電話番号、携帯電話番号、メールアドレス
		// 任意のものはパーツのプロパティ設定において表示項目とした場合のみ定義可能
		// なおカラムサイズは組織マスタ（WFM_ORGANIZATION）と同じにする
		final List<PartsColumn> list = new ArrayList<>();
		// 会社コード
		{
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_CORPORATION_CODE";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 10;
			col.roleCode = CORPORATION_CODE;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// 組織コード
		{
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_ORGANIZATION_CODE";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 25;
			col.roleCode = ORGANIZATION_CODE;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// 組織名称
		if (organizationNameDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_ORGANIZATION_NAME";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 300;
			col.roleCode = ORGANIZATION_NAME;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// 組織付加情報
		if (organizationAddedInfoDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_ORG_ADDED_INFO";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 50;
			col.roleCode = ORGANIZATION_ADDED_INFO;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// 組織略称
		if (organizationNameAbbrDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_ORG_NAME_ABBR";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 300;
			col.roleCode = ORGANIZATION_NAME_ABBR;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// 電話番号
		if (telNumDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_TEL_NUM";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 20;
			col.roleCode = TEL_NUM;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// 携帯電話番号
		if (faxNumDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_TEL_NUM_CEL";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 20;
			col.roleCode = FAX_NUM;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// メールアドレス
		if (addressDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_MAIL_ADDRESS";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 320;
			col.roleCode = ADDRESS;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		return list;
	}

	/** パーツ更新の前処理 */
	public void beforeSave() {
	}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {
		// イベント
		int i = 0;
		if (events.isEmpty()) {
			events.add(new PartsEvent(BEFORE_SELECT, ++i));
			events.add(new PartsEvent(AFTER_SELECT, ++i));
			events.add(new PartsEvent(BEFORE_CLEAR, ++i));
			events.add(new PartsEvent(AFTER_CLEAR, ++i));
		}
		events.sort((e1, e2) -> compareTo(e1.sortOrder, e2.sortOrder));
	}
}
