package jp.co.nci.iwf.endpoint.mm.mm0012;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;

/**
 * ルックアップグループ設定サービス
 */
@BizLogic
public class Mm0012Service extends MmBaseService<MwmLookupEx> {

	@Inject
	Mm0012Repository repository;

	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0012Response init(Mm0012Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.lookupGroupId))
			throw new BadRequestException("企業コードまたはルックアップグループIDが未指定です");

		final Mm0012Response res = createResponse(Mm0012Response.class, req);

		res.lookupGroup = repository.searchLookUpGroup(req.corporationCode, req.lookupGroupId, sessionHolder.getLoginInfo().getLocaleCode());
//		res.lookupList = repository.search(req, sessionHolder.getLoginInfo().getLocaleCode(), true);

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		// 排他
		if (req.timestampUpdated != null && !eq(req.timestampUpdated, res.lookupGroup.getTimestampUpdated().getTime())) {
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
		} else {
			res.success = true;
		}
		return res;
	}

	public Mm0012Response search(Mm0012Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.lookupGroupId)) {
			throw new BadRequestException("ルックアップグループIDが未指定です");
		}

		int allCount = repository.count(req, sessionHolder.getLoginInfo().getLocaleCode());
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0012Response res = createResponse(Mm0012Response.class, req, allCount);
		res.lookupGroup = repository.searchLookUpGroup(req.corporationCode, req.lookupGroupId, sessionHolder.getLoginInfo().getLocaleCode());

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		// 結果の抽出
		final List<MwmLookupEx> results;
		if (allCount == 0) {
			results = new ArrayList<>();
		} else {
			results = repository.search(req, sessionHolder.getLoginInfo().getLocaleCode(), true);
		}
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = results;

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = true;

		return res;
	}

	/**
	 * ルックアップグループの更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0012Response update(Mm0012UpdateRequest req) {
		MwmLookupGroup lookupGroup = repository.searchLookUpGroup(req.lookupGroup.getCorporationCode()
																, req.lookupGroup.getLookupGroupId()
																, sessionHolder.getLoginInfo().getLocaleCode());

		if (lookupGroup == null) {
			throw new AlreadyUpdatedException();
		}

		lookupGroup.setLookupGroupName(req.lookupGroup.getLookupGroupName());
		lookupGroup.setSortOrder(req.lookupGroup.getSortOrder());
		lookupGroup.setDeleteFlag(req.lookupGroup.getDeleteFlag());

		repository.update(lookupGroup);

		final Mm0012Response res = createResponse(Mm0012Response.class, req);

		res.lookupGroup = lookupGroup;

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.lookupGroup));
		res.success = true;
		return res;
	}

	/**
	 * ルックアップの削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0012Response delete(Mm0012Request req) {
		for (MwmLookup deleteLookup : req.deleteLookups) {
			MwmLookup lookup = repository.getLookup(deleteLookup.getCorporationCode(), deleteLookup.getLookupGroupId(), deleteLookup.getLookupId(), sessionHolder.getLoginInfo().getLocaleCode());
			lookup.setDeleteFlag(DeleteFlag.ON);
			repository.delete(lookup);
		}
		final Mm0012Response res = createResponse(Mm0012Response.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.lookup));

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = true;
		return res;
	}

}
