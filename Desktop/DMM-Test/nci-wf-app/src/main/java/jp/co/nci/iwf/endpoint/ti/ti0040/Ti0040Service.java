package jp.co.nci.iwf.endpoint.ti.ti0040;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.tableSearch.TableSearchRepository;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;

/**
 * 汎用テーブル定義画面サービス
 */
@BizLogic
public class Ti0040Service extends BaseService {
	@Inject private TableSearchRepository repository;
	@Inject private TableMetaDataService metaService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0040Response init(Ti0040InitRequest req) {
		if (req.tableId == null)
			throw new BadRequestException("テーブルIDが未指定です");
		if (req.version == null)
			throw new BadRequestException("バージョンが未指定です");

		final MwmTableEx table = repository.getMwmTable(req.tableId);
		if (table == null)
			throw new NotFoundException("汎用マスタがありません。 tableId=" + req.tableId);
		// 排他ロック
		if (!eq(req.version, table.version))
			throw new AlreadyUpdatedException();
		// 権限チェック
		if (repository.countMwvTableAuthority(req.tableId) == 0)
			throw new ForbiddenException("tableId=" + req.tableId);

		final Ti0040Response res = createResponse(Ti0040Response.class, req);
		res.table = table;
		res.columns = metaService.getColumns(table.tableName);
		res.success = true;
		return res;
	}

}
