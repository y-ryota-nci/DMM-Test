package jp.co.nci.iwf.endpoint.ti.ti0051;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.tableSearch.TableSearchRepository;
import jp.co.nci.iwf.designer.service.tableInfo.ColumnMetaData;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearch;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearchColumn;

/**
 * 汎用テーブル検索条件設定サービス
 */
@BizLogic
public class Ti0051Service extends BaseService {
	@Inject private Ti0051Repository repository;
	@Inject private TableSearchRepository tsRepository;
	@Inject private TableMetaDataService metaService;
	@Inject private MwmLookupService lookup;
	@Inject private WfmLookupService mwmLookup;
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0051Response init(Ti0051InitRequest req) {
		// 受信パラメータチェック
		if (req.tableId == null)
			throw new BadRequestException("テーブルIDが未指定です");
		if (req.tableSearchId == null && req.version != null)
			throw new BadRequestException("テーブル検索IDが未指定です");
		if (req.tableSearchId != null && req.version == null)
			throw new BadRequestException("ヴァージョンが未指定です");
		// テーブルの存在チェック
		final MwmTableEx tableDef = tsRepository.getMwmTable(req.tableId);
		if (tableDef == null)
			throw new NotFoundException("汎用テーブルが見つかりません 。tableId=" + req.tableId);
		// 権限チェック
		if (tsRepository.countMwvTableAuthority(req.tableId) == 0)
			throw new ForbiddenException("tableId=" + req.tableId);

		// テーブルのカラム定義のメタデータ
		final List<ColumnMetaData> colDefs = metaService.getColumns(tableDef.tableName);

		// 汎用テーブル検索の取得
		final MwmTableSearchEx table = getTableSearch(tableDef, req);
		// 排他チェック
		if (req.version != null && !eq(req.version, table.version))
			throw new AlreadyUpdatedException();

		final Ti0051Response res = createResponse(Ti0051Response.class, req);
		res.tableDef = tableDef;
		res.table = table;
		res.columns = getTableSearchColumns(colDefs, req);

		res.success = true;

		return res;
	}

	/** 汎用テーブル検索カラムの取得 */
	private List<MwmTableSearchColumnEx> getTableSearchColumns(List<ColumnMetaData> colDefs, Ti0051InitRequest req) {
		List<MwmTableSearchColumnEx> columns;
		if (req.tableSearchId == null) {
			// カラム定義メタ情報から新規データを生成
			columns = new ArrayList<>();
			for (ColumnMetaData c : colDefs)
				columns.add(toTableSearchColumn(c));
		} else {
			// 既存を抽出
			String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
			String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
			columns = tsRepository.getColumns(req.tableSearchId, corporationCode, localeCode);

			// 不足カラムを「カラムのメタデータ」から補てん
			final Map<String, MwmTableSearchColumnEx> columnMap = columns.stream()
					.collect(Collectors.toMap(c -> c.columnName, c -> c));
			for (Iterator<ColumnMetaData> it = colDefs.iterator(); it.hasNext(); ) {
				ColumnMetaData meta = it.next();
				if (columnMap.containsKey(meta.columnName))
//					columnMap.get(meta.columnName).sortOrder = meta.sortOrder;	// 並び順を反映
					;
				else
					columns.add(toTableSearchColumn(meta));
			}
		}
		return columns;
	}

	/** 汎用テーブル検索の取得 */
	private MwmTableSearchEx getTableSearch(MwmTableEx t, Ti0051InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		if (req.tableSearchId == null) {
			// テーブル定義／カラム定義から新規データを生成
			final MwmTableSearchEx s = new MwmTableSearchEx();
			s.corporationCode = corporationCode;
			s.defaultSearchFlag = CommonFlag.OFF;
			s.deleteFlag = DeleteFlag.OFF;
			s.deleteFlagName = mwmLookup.getName(LookupTypeCode.DELETE_FLAG, s.deleteFlag);
			s.tableId = t.tableId;
			return s;
		} else {
			// 既存を抽出
			final MwmTableSearchEx s = tsRepository.getMwmTableSearch(req.tableSearchId, localeCode);
			if (s == null)
				throw new ForbiddenException("汎用テーブル検索がありません。tableSearchId=" + req.tableSearchId);
			return s;
		}
	}

	/** カラムメタ情報を汎用テーブル検索カラムへ変換 */
	private MwmTableSearchColumnEx toTableSearchColumn(ColumnMetaData meta) {
		final MwmTableSearchColumnEx c = new MwmTableSearchColumnEx();
		c.columnName = meta.columnName;
		c.deleteFlag = DeleteFlag.OFF;
		// コメント： A5MK2でのコメントは、論理名のあとにコロン区切りで列のコメントが設定されるので、論理名だけを抜き出す
		if (isNotEmpty(meta.comment) && meta.comment.indexOf(':') > 0)
			c.logicaColumnName = meta.comment.substring(0, meta.comment.indexOf(':'));
		else
			c.logicaColumnName = meta.comment;
		c.sortOrder = meta.sortOrder;
		c.conditionDisplayType = ConditionDisplayType.NON_USE;
		c.conditionDisplayTypeName = lookup.getName(LookupGroupId.CONDITION_DISPLAY_TYPE, c.conditionDisplayType);
		c.conditionTrimFlag = ConditionTrimFlag.OFF;
		c.conditionTrimFlagName = lookup.getName(LookupGroupId.CONDITION_TRIM_FLAG, c.conditionTrimFlag);
		c.conditionKanaConvertType = ConditionKanaConvertType.NONE;
		c.conditionKanaConvertTypeName = lookup.getName(LookupGroupId.CONDITION_KANA_CONVERT_TYPE, c.conditionKanaConvertType);
		c.resultDisplayType = ResultDisplayType.NON_USE;
		c.resultDisplayTypeName = lookup.getName(LookupGroupId.RESULT_DISPLAY_TYPE, c.resultDisplayType);
		final String type = meta.columnType.toUpperCase();
		if (metaService.isString(type))
			c.searchColumnType = SearchColumnType.STRING;
		else if (metaService.isNumeric(type))
			c.searchColumnType = SearchColumnType.NUMBER;
		else if (metaService.isDate(type))
			c.searchColumnType = SearchColumnType.DATE;
		else if (metaService.isTimestamp(type))
			c.searchColumnType = SearchColumnType.TIMESTAMP;
		else
			c.searchColumnType = SearchColumnType.STRING;
		c.searchColumnTypeName = lookup.getName(LookupGroupId.SEARCH_COLUMN_TYPE, c.searchColumnType);
		return c;
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Ti0051Response save(Ti0051SaveRequest req) {
		// バリデーション
		Ti0051Response res;
		String error = validate(req);

		if (error == null) {
			// 更新処理
			MwmTableSearch ts = execUpdate(req);

			// レスポンス
			final Ti0051InitRequest dummy = new Ti0051InitRequest();
			dummy.tableId = ts.getTableId();
			dummy.tableSearchId = ts.getTableSearchId();
			dummy.version = ts.getVersion();
			res = init(dummy);
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.tableSearchCondition));
		}
		else {
			res = createResponse(Ti0051Response.class, req);
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	/** バリデーション */
	private String validate(Ti0051SaveRequest req) {
		MwmTableSearch t = req.table;
		// すでに同じ検索条件コードが存在しないこと
		int count = repository.count(t.getCorporationCode(), t.getTableId(), t.getTableSearchCode(), t.getTableSearchId());
		if (count > 0)
			return i18n.getText(MessageCd.MSG0130, MessageCd.tableSearchCode);

		final Set<Integer> condPositions = new HashSet<>();
		final Set<Integer> resultPositions = new HashSet<>();
		final Set<Integer> orderByPositions = new HashSet<>();
		int colWidth = 0;
		final String cond = i18n.getText(MessageCd.searchCondition);
		final String result = i18n.getText(MessageCd.searchResult);
		for (MwmTableSearchColumnEx c : req.columns) {
			// 検索条件表示位置がユニークであること
			if (c.conditionDisplayPosition != null && !condPositions.add(c.conditionDisplayPosition)) {
				return i18n.getText(MessageCd.MSG0121, cond + i18n.getText(MessageCd.displayPosition), c.conditionDisplayPosition);
			}

			// 検索結果表示位置がユニークであること
			if (c.resultDisplayPosition != null && !resultPositions.add(c.resultDisplayPosition))
				return i18n.getText(MessageCd.MSG0121, result + i18n.getText(MessageCd.displayPosition), c.resultDisplayPosition);

			// デフォルトソート順がユニークであること
			if (c.resultOrderByPosition != null && !orderByPositions.add(c.resultOrderByPosition))
				return i18n.getText(MessageCd.MSG0121, i18n.getText(MessageCd.defaultOrderPosition), c.resultOrderByPosition);

			// 列幅の合計（検索結果表示区分＝表示のみ）
			if (eq(ResultDisplayType.DISPLAY, c.resultDisplayType) && c.resultDisplayWidth != null) {
				colWidth += c.resultDisplayWidth;
			}
		}
		// 列幅の合計が12以下であること（Bootstrapのグリッド的に）
		final int maxColCount = 12;
		if (colWidth > 0 && colWidth != maxColCount) {
			String columnWidth = i18n.getText(MessageCd.columnWidth);
			return i18n.getText(MessageCd.MSG0140, columnWidth, maxColCount, colWidth);
		}
		return null;
	}

	private MwmTableSearch execUpdate(Ti0051SaveRequest req) {
		// 汎用テーブル検索条件
		long tableSearchId;
		{
			MwmTableSearch current = repository.getMwmTableSearch(req.table.getTableSearchId());
			if (current == null)
				tableSearchId = repository.insert(req.table);
			else
				tableSearchId = repository.update(current, req.table);

			// 多言語
			multi.save("MWM_TABLE_SEARCH", tableSearchId, "TABLE_SEARCH_NAME", req.table.getTableSearchName());
			multi.save("MWM_TABLE_SEARCH", tableSearchId, "DISPLAY_NAME", req.table.getDisplayName());
		}
		// 汎用テーブル検索条件カラム
		{
			Map<Long, MwmTableSearchColumn> currents = repository.getMwmTableSearchColumns(tableSearchId)
					.stream()
					.collect(Collectors.toMap(MwmTableSearchColumn::getTableSearchColumnId, c -> c));
			for (MwmTableSearchColumnEx input : req.columns) {
				MwmTableSearchColumn current = currents.remove(input.tableSearchColumnId);
				long tableSearchColumnId;
				if (current == null) {
					tableSearchColumnId = repository.insert(tableSearchId, input);
				} else {
					repository.update(current, input);
					tableSearchColumnId = current.getTableSearchColumnId();
				}

				// 多言語
				multi.save("MWM_TABLE_SEARCH_COLUMN", tableSearchColumnId, "LOGICA_COLUMN_NAME", input.logicaColumnName);
			}
			repository.delete(currents);
		}
		return repository.getMwmTableSearch(tableSearchId);
	}
}
