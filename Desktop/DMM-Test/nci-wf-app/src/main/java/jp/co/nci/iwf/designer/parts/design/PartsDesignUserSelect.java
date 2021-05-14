package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsButtonSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsEvents;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsIoType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleUserSelect;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsUserSelect;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【デザイン時】ユーザ選択パーツ
 */
public class PartsDesignUserSelect extends PartsDesign implements RoleUserSelect, PartsEvents {


	/** パーツ関連定義の関連コード */
	public static final String RELATION_CODE = "partsIdOrgCondition";

	/** 関連付けするパーツID（絞込み条件として使用する組織選択パーツのパーツID） */
	public Long partsIdOrgCondition;
	/** ボタンサイズ */
	public int buttonSize;
	/** 表示項目と各項目毎の設定値 */
	// ユーザ名称
	public boolean userNameDisplay;
	public Integer userNameSortOrder;
	public Integer userNameColLg;
	public Integer userNameColMd;
	public Integer userNameColSm;
	public Integer userNameColXs;
	// ユーザ付加情報
	public boolean userAddedInfoDisplay;
	public Integer userAddedInfoSortOrder;
	public Integer userAddedInfoColLg;
	public Integer userAddedInfoColMd;
	public Integer userAddedInfoColSm;
	public Integer userAddedInfoColXs;
	// ユーザ略称
	public boolean userNameAbbrDisplay;
	public Integer userNameAbbrSortOrder;
	public Integer userNameAbbrColLg;
	public Integer userNameAbbrColMd;
	public Integer userNameAbbrColSm;
	public Integer userNameAbbrColXs;
	// 電話番号
	public boolean telNumDisplay;
	public Integer telNumSortOrder;
	public Integer telNumColLg;
	public Integer telNumColMd;
	public Integer telNumColSm;
	public Integer telNumColXs;
	// 携帯電話番号
	public boolean telNumCelDisplay;
	public Integer telNumCelSortOrder;
	public Integer telNumCelColLg;
	public Integer telNumCelColMd;
	public Integer telNumCelColSm;
	public Integer telNumCelColXs;
	// メールアドレス
	public boolean mailAddressDisplay;
	public Integer mailAddressSortOrder;
	public Integer mailAddressColLg;
	public Integer mailAddressColMd;
	public Integer mailAddressColSm;
	public Integer mailAddressColXs;

	/** ユーザ選択パーツ固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"buttonSize",
			"userNameDisplay",
			"userNameSortOrder",
			"userNameColLg",
			"userNameColMd",
			"userNameColSm",
			"userNameColXs",
			"userAddedInfoDisplay",
			"userAddedInfoSortOrder",
			"userAddedInfoColLg",
			"userAddedInfoColMd",
			"userAddedInfoColSm",
			"userAddedInfoColXs",
			"userNameAbbrDisplay",
			"userNameAbbrSortOrder",
			"userNameAbbrColLg",
			"userNameAbbrColMd",
			"userNameAbbrColSm",
			"userNameAbbrColXs",
			"telNumDisplay",
			"telNumSortOrder",
			"telNumColLg",
			"telNumColMd",
			"telNumColSm",
			"telNumColXs",
			"telNumCelDisplay",
			"telNumCelSortOrder",
			"telNumCelColLg",
			"telNumCelColMd",
			"telNumCelColSm",
			"telNumCelColXs",
			"mailAddressDisplay",
			"mailAddressSortOrder",
			"mailAddressColLg",
			"mailAddressColMd",
			"mailAddressColSm",
			"mailAddressColXs",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		// 新規時はユーザ名称のみ表示対象
		userNameDisplay = true;
		userNameColXs = 3;
		// 並び順は下記
		userNameSortOrder = 1;
		userAddedInfoSortOrder = 2;
		userNameAbbrSortOrder = 3;
		telNumSortOrder = 4;
		telNumCelSortOrder = 5;
		mailAddressSortOrder = 6;
		// ボタンサイズ
		buttonSize = PartsButtonSize.NORMAL;
	}

	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsUserSelect parts = new PartsUserSelect();
		parts.defaultRoleCode = USER_CODE;
		setPartsCommonValue(parts, container, rowId, ctx);
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
		// 必須：会社コード、ユーザコード
		// 任意：ユーザ名称、ユーザ付加情報、ユーザ略称、電話番号、携帯電話番号、メールアドレス
		// 任意のものはパーツのプロパティ設定において表示項目とした場合のみ定義可能
		// なおカラムサイズはユーザマスタ（WFM_USER）と同じにする
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
		// ユーザコード
		{
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_USER_CODE";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 25;
			col.roleCode = USER_CODE;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// ユーザ名称
		if (userNameDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_USER_NAME";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 300;
			col.roleCode = USER_NAME;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// ユーザ付加情報
		if (userAddedInfoDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_USER_ADDED_INFO";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 50;
			col.roleCode = USER_ADDED_INFO;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// ユーザ略称
		if (userNameAbbrDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_USER_NAME_ABBR";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 300;
			col.roleCode = USER_NAME_ABBR;
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
		if (telNumCelDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_TEL_NUM_CEL";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 20;
			col.roleCode = TEL_NUM_CEL;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		// メールアドレス
		if (mailAddressDisplay) {
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_MAIL_ADDRESS";
			col.columnType = ColumnType.VARCHAR;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.columnSize = 320;
			col.roleCode = MAIL_ADDRESS;
			col.columnTypeLabel = col.toString();
			list.add(col);
		}
		return list;
	}

	/** パーツ更新の前処理 */
	public void beforeSave() {
		// 関連付けするパーツIDをパーツ関連定義へ設定
		relations.clear();
		if (partsIdOrgCondition != null) {
			final PartsRelation pr = new PartsRelation();
			pr.partsIoType = PartsIoType.IN;
			pr.columnName = RELATION_CODE;
			pr.targetPartsId = partsIdOrgCondition;
			pr.sortOrder = 1;
			relations.add(pr);
		}
	}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {
		// 関連付けするパーツIDをパーツ関連定義から展開
		if (relations != null && !relations.isEmpty()) {
			for (PartsRelation pr : relations) {
				if (MiscUtils.eq(pr.columnName, RELATION_CODE)) {
					partsIdOrgCondition = pr.targetPartsId;
					break;
				}
			}
		}
		else {
			partsIdOrgCondition = null;
		};
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
