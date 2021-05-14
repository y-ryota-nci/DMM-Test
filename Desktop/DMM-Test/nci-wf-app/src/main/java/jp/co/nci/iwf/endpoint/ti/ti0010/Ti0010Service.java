package jp.co.nci.iwf.endpoint.ti.ti0010;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.StringTruncator;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaData;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTable;

/**
 * マスタ取込設定画面サービス
 */
@BizLogic
public class Ti0010Service extends BaseService {
	@Inject private TableMetaDataService metaService;
	@Inject private Ti0010Repository repository;
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0010Response init(Ti0010Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// カテゴリ一覧とデフォルトのカテゴリ
		final List<Ti0010Category> categories = repository.getCategories(corporationCode, localeCode)
				.stream()
				.map(c -> new Ti0010Category(c))
				.collect(Collectors.toList());
		req.categoryId = (categories.isEmpty() ? null : categories.get(0).categoryId);

		// 全テーブル一覧とカテゴリ配下の取込済みテーブルを読み直し
		final Ti0010Response res = refresh(req);
		res.categories = categories;
		res.categoryId = req.categoryId;

		return res;
	}

	/**
	 * テーブルカテゴリID配下の取込済みテーブルを再読み込み
	 * @param req
	 * @return
	 */
	public Ti0010Response refresh(Ti0010Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Ti0010Response res = createResponse(Ti0010Response.class, req);

		// カテゴリ内の取込済みテーブル名とTableID
		String entityType = null;
		final Map<String, MwmTable> imports =
				repository.getMwmTable(corporationCode, localeCode, req.categoryId, entityType);

		// 全テーブル・ビュー一覧
		final List<TableMetaData> metaTables = getTableMetaDataList(req.entityType);

		final List<Ti0010Table> allTables = metaTables.stream()
				.map(m -> new Ti0010Table(m, imports))
				.collect(Collectors.toList());

		res.allTables = allTables;
		res.success = true;
		return res;
	}

	private List<TableMetaData> getTableMetaDataList(String entityType) {
		final List<TableMetaData> metaTables;
		if (eq(EntityType.TABLE, entityType))
			metaTables = metaService.getTables();
		else if (eq(EntityType.VIEW, entityType))
			metaTables = metaService.getViews();
		else
			metaTables = metaService.getTableAndViews();
		return metaTables;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Ti0010Response save(Ti0010SaveRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		// バリデーション
		List<String> errors = validate(req);
		if (!errors.isEmpty()) {
			Ti0010Response res = createResponse(Ti0010Response.class, req);
			res.alerts = errors;
			res.success = false;
			return res;
		}

		// テーブル名とコメントのMap
		final Map<String, TableMetaData> metaTables = getTableMetaDataList(req.entityType)
				.stream()
				.collect(Collectors.toMap(t -> t.tableName, t -> t));

		// 既存の汎用テーブル構成
		Map<Long, MwmCategoryConfig> ccMap =
				repository.getMwmCategoryConfig(corporationCode);

		final int COMMENT_SIZE = 900;
		for (Ti0010Table imp : req.inputs) {
			final String tableName = imp.tableName;
			final TableMetaData meta = metaTables.get(tableName);

			// 長すぎる論理名をカット
			final String comment = StringTruncator.trunc(imp.comment, COMMENT_SIZE, StringTruncator.UTF8);;

			// 汎用テーブル
			final MwmTable current = repository.getMwmTableByName(tableName);
			Long tableId = null;
			if (current == null)
				tableId = repository.insertMwmTable(tableName, comment, meta);
			else
				tableId = repository.updateMwmTable(current, comment, meta);
			multi.save("MWM_TABLE", tableId, "TABLE_NAME", comment);

			// 汎用テーブルカテゴリ構成
			MwmCategoryConfig cc = ccMap.remove(tableId);
			if (cc == null)
				repository.insertMwmCategoryConfig(tableId, req.categoryId, corporationCode);
			else if (!eq(cc.getCategoryConfigId(), req.categoryId))
				repository.updateMwmCategoryConfig(cc, req.categoryId);
		}

		// 使用されなくなった汎用テーブル構成を削除
		for (MwmCategoryConfig cc : ccMap.values()) {
			// 汎用テーブルおよび汎用テーブルカラムは他企業でも使っている可能性がため削除できないので
			// 汎用テーブルカテゴリ構成と汎用テーブル検索と汎用テーブル検索カラムだけが対象となる
			if (cc.getCategoryId() == req.categoryId) {
				repository.deleteMwmCategoryConfig(cc);
				repository.deleteMwmTableSearch(cc.getTableId());
				repository.deleteMwmTableSearchColumn(cc.getTableId());
			}
		}

		// 読み直すためにダミーリクエストを生成して再読み込み処理を行ったうえで、処理結果をセット
		final Ti0010Request dummy = new Ti0010Request();
		dummy.categoryId = req.categoryId;
		Ti0010Response res = refresh(dummy);
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.universalMaster));
		return res;
	}

	/** バリデーション */
	private List<String> validate(Ti0010SaveRequest req) {
		List<String> errors = new ArrayList<>();

		// カテゴリ
		if (req.categoryId == null) {
			errors.add(i18n.getText(MessageCd.MSG0003, MessageCd.category));
		}
		return errors;
	}

}
