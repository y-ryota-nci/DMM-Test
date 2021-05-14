package jp.co.nci.iwf.endpoint.ti.ti0050;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.tableSearch.TableSearchRepository;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;

/**
 * 汎用テーブル検索条件一覧サービス
 */
@BizLogic
public class Ti0050Service extends BasePagingService {
	@Inject private Ti0050Repository repository;
	@Inject private TableSearchRepository tsRepository;
	@Inject private WfmLookupService lookup;


	public Ti0050Response init(Ti0050InitRequest req) {
		if (req.tableId == null)
			throw new BadRequestException("テーブルIDが未指定です");

		final MwmTableEx table = tsRepository.getMwmTable(req.tableId);
		if (table == null)
			throw new NotFoundException("汎用マスタがありません。 tableId=" + req.tableId);
		// 権限なし
		if (tsRepository.countMwvTableAuthority(req.tableId) == 0)
			throw new ForbiddenException("tableId=" + req.tableId);

		final Ti0050Response res = createResponse(Ti0050Response.class, req);
		res.table = table;
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Ti0050Response search(Ti0050Request req) {
		if (req.tableId == null)
			throw new BadRequestException("テーブルIDが未指定です");

		final int allCount = repository.count(req);
		final Ti0050Response res = createResponse(Ti0050Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Ti0050DeleteRequest req) {
		// 汎用テーブル検索条件IDに紐付くレコードを論理削除
		for (Long tableSearchId : req.tableSearchIds) {
			if (tableSearchId != null) {
				repository.logicalDelete(tableSearchId);
			}
		}

		BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.tableSearchCondition));
		res.success = true;
		return res;
	}

}
