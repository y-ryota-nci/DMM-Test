package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsIoType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsLabel;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【デザイン時】Labelパーツ
 */
public class PartsDesignLabel extends PartsDesign {

	/** 関連付けするパーツID（LABELタグの'for'属性用パーツID） */
	public Long partsIdFor;

	public static final String PARTS_ID_FOR = "partsIdFor";

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		renderingMethod = RenderingMethod.INLINE;
		grantTabIndexFlag = false;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsLabel newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsLabel parts = new PartsLabel();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = null;
		return parts;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 * @return
	 */
	@Override
	public String[] extFieldNames() {
		return null;
	}

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 * @return
	 */
	@Override
	public List<PartsColumn> newColumns() {
		return new ArrayList<>();	// ラベルは実行時データを保持しない
	}


	/** パーツ更新の前処理 */
	public void beforeSave() {
		// 関連付けするパーツIDをパーツ関連定義へ設定
		relations.clear();
		if (partsIdFor != null) {
			final PartsRelation pr = new PartsRelation();
			pr.partsIoType = PartsIoType.IN;
			pr.columnName = PARTS_ID_FOR;
			pr.targetPartsId = partsIdFor;
			pr.sortOrder = 1;
			relations.add(pr);
		}
	}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {
		// 関連付けするパーツIDをパーツ関連定義から展開
		if (relations != null && !relations.isEmpty()) {
			for (PartsRelation pr : relations) {
				if (MiscUtils.eq(pr.columnName, PARTS_ID_FOR)) {
					partsIdFor = pr.targetPartsId;
					break;
				}
			}
		}
		else {
			partsIdFor = null;
		};
	}
}
