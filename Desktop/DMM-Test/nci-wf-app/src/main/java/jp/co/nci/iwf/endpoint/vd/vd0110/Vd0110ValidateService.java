package jp.co.nci.iwf.endpoint.vd.vd0110;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAjax;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.service.ContainerEndlessLoopEntity;
import jp.co.nci.iwf.designer.service.ContainerLoadRepository;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * コンテナを更新する際の整合性チェックサービス
 */
@BizLogic
public class Vd0110ValidateService extends BaseService {
	@Inject
	private ContainerLoadRepository repository;

	/** （単一パーツ）バリデーション */
	public String validate(PartsDesign d) {
		String error = null;
		// コンテナパーツなら子コンテナを選択済みであること
		if (error == null)
			error = validateChildContainerId(d);

		return error;
	}

	/** コンテナパーツなら子コンテナを選択済みであること */
	private String validateChildContainerId(PartsDesign d) {
		//
		if (d instanceof PartsDesignChildHolder) {
			PartsDesignChildHolder child = (PartsDesignChildHolder)d;
			if (child.childContainerId == null)
				// パーツコード＝XXXの子コンテナを選択して下さい。
				return i18n.getText(MessageCd.MSG0191,
						i18n.getText(MessageCd.partsCode) + "=" + child.partsCode,
						i18n.getText(MessageCd.childContainer));
		}
		return null;
	}

	/**
	 * （複数パーツ）バリデーション
	 * @param ctx
	 * @return
	 */
	public List<String> validate(DesignerContext ctx) {
		final List<PartsDesign> designs = toDesigns(ctx);
		final List<String> errors = new ArrayList<>();

		// カラム名の重複
		String error = validateColumnNames(designs);
		if (error != null)
			errors.add(error);

		// パーツコードの重複チェック
		error = validatePartsCodes(designs);
		if (error != null)
			errors.add(error);

		// 子コンテナを選択済みかのチェック
		error = validateChildContainerId(ctx);
		if (error != null)
			errors.add(error);

		// コンテナ間でのテーブル名重複チェック
		error = validateSameTableName(ctx);
		if (error != null)
			errors.add(error);

		// 子コンテナIDの循環参照
		error = validateCircularReference(ctx);
		if (error != null)
			errors.add(error);

		// 汎用テーブルを使用するパーツで、テーブルIDが指定されているか
		error = validateTableId(ctx);
		if (error != null)
			errors.add(error);

		return errors;
	}

	/** 汎用テーブルを使用するパーツで、テーブルIDが指定されているか */
	private String validateTableId(DesignerContext ctx) {
		for (Long partsId : ctx.root.childPartsIds) {
			PartsDesign d = ctx.designMap.get(partsId);
			if (d instanceof PartsDesignAjax) {
				PartsDesignAjax button = (PartsDesignAjax)d;
				if (button.tableId == null) {
					// パーツコード＝XXXのテーブル名を選択して下さい。
					return i18n.getText(MessageCd.MSG0191,
							i18n.getText(MessageCd.partsCode) + "=" + button.partsCode,
							i18n.getText(MessageCd.tableName));
				}
			}
		}
		return null;
	}

	/** 子コンテナを選択済みかのチェック */
	private String validateChildContainerId(DesignerContext ctx) {
		// 子コンテナIDを選択済みか
		// 子要素があれば再帰チェック
		for (Long partsId : ctx.root.childPartsIds) {
			PartsDesign d = ctx.designMap.get(partsId);
			String error = validateChildContainerId(d);
			if (error != null)
				return error;
		}
		return null;
	}

	/** コンテナ間でのテーブル名重複チェック */
	private String validateSameTableName(DesignerContext ctx) {
		Set<String> uniques = new HashSet<>();
		return validateDuplicatedTableName(ctx.root, ctx, uniques);
	}

	/** コンテナ間でのテーブル名重複チェック */
	private String validateDuplicatedTableName(PartsDesignContainer c, DesignerContext ctx, Set<String> uniques) {
		// テーブル名の重複チェック
		if (isNotEmpty(c.tableName) && uniques.contains(c.tableName))
			return i18n.getText(MessageCd.MSG0133, c.tableName);

		uniques.add(c.tableName);

		// 子要素があれば再帰チェック
		for (Long partsId : c.childPartsIds) {
			PartsDesign d = ctx.designMap.get(partsId);

			if (d instanceof PartsDesignContainer) {
				final String error = validateDuplicatedTableName((PartsDesignContainer)d, ctx, uniques);
				if (isNotEmpty(error))
					return error;
			}
		}
		return null;
	}

	/** 子コンテナIDの循環参照 */
	private String validateCircularReference(DesignerContext ctx) {
		// コンテナ配下の子孫のパーツで、当コンテナを使用しているものを抽出。
		// 当コンテナを使用していれば、それが循環参照だ
		for (Long partsId : ctx.root.childPartsIds) {
			final PartsDesign d = ctx.designMap.get(partsId);
			if (d instanceof PartsDesignChildHolder) {
				final PartsDesignChildHolder ch = (PartsDesignChildHolder)d;
				final List<ContainerEndlessLoopEntity> list =
						repository.getCircularReference(ch.childContainerId);
				for (ContainerEndlessLoopEntity row : list) {
					if (row.childContainerId == ch.containerId) {
						return i18n.getText(MessageCd.MSG0123, ch.partsCode, ch.labelText);
					}
				}
			}
		}
		return null;
	}

	/** パーツコードの重複チェック */
	private String validatePartsCodes(List<PartsDesign> designs) {
		final Map<String, PartsDesign> uniques = new HashMap<>(64);
		for (PartsDesign design : designs) {
			if (uniques.containsKey(design.partsCode)) {
				final PartsDesign other = uniques.get(design.partsCode);
				return i18n.getText(MessageCd.MSG0122, design.labelText, other.labelText);
			}
			uniques.put(design.partsCode, design);
		}
		return null;
	}

	/** カラム名の重複チェック */
	private String validateColumnNames(List<PartsDesign> designs) {
		final Set<String> names = new HashSet<>(64);
		for (PartsDesign design : designs) {
			final List<PartsColumn> pcs = design.columns;
			for (PartsColumn pc : pcs) {
				if (names.contains(pc.columnName)) {
					return i18n.getText(MessageCd.MSG0121, i18n.getText(MessageCd.dbColumnName), pc.columnName);
				}
				names.add(pc.columnName);
			}
		}
		return null;
	}

	/** コンテナの直接の配下パーツを列挙 */
	private List<PartsDesign> toDesigns(DesignerContext ctx) {
		final List<PartsDesign> designs = new ArrayList<>(ctx.root.childPartsIds.size());
		for (Long partsId : ctx.root.childPartsIds) {
			final PartsDesign design = ctx.designMap.get(partsId);
			if (design != null)
				designs.add(design);
		}
		return designs;
	}
}
