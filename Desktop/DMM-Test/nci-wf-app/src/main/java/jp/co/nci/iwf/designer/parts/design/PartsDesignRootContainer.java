package jp.co.nci.iwf.designer.parts.design;

import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;

/**
 * 【デザイン時】ルートコンテナパーツ
 */
public class PartsDesignRootContainer extends PartsDesignContainer {

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	public void setInitValue() {
		super.setInitValue();

		partsType = PartsType.ROOT_CONTAINER;
		partsId = -1;
		minRowCount = 1;
		initRowCount = 1;
	}

	/**
	 * 【実行時】新しいパーツインスタンスを返す
	 */
	@Override
	public PartsRootContainer newParts(PartsContainerBase<?> parent, Integer rowId, DesignerContext ctx) {
		final PartsRootContainer root = new PartsRootContainer();

		// 【実行時】パーツの初期値
		setPartsCommonValue(root, parent, rowId, ctx);
		root.defaultRoleCode = null;

		// 【実行時】子要素を生成
		fillRows(root, ctx);

		// ルートコンテナの表示条件は入力可であり不変
		root.dcType = DcType.INPUTABLE;

		return root;
	}

	/**
	 * 新規パーツを作成した場合の共通初期値を、パーツ定義から転写
	 * @param parts
	 * @param ctx
	 */
	protected <P extends PartsBase<?>> void setPartsCommonValue(P parts, PartsContainerBase<?> parent, Integer rowId, DesignerContext ctx) {
		super.setPartsCommonValue(parts, parent, rowId, ctx);
		parts.htmlId = containerCode;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 * @return
	 */
	@Override
	public String[] extFieldNames() {
		return new String[0];
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {}
}
