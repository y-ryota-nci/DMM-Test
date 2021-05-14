package jp.co.nci.iwf.endpoint.mm.mm0052;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmPartsSequenceSpecEx;

/**
 * 通し番号一覧のサービス
 */
@BizLogic
public class Mm0052Service extends MmBaseService<MwmPartsSequenceSpecEx> {
	@Inject
	private WfmLookupService lookup;

	@Inject
	private Mm0052Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0052Response init(Mm0052Request req) {
		final Mm0052Response res = createResponse(Mm0052Response.class, req);
		res.corporations = getAccessibleCorporations(false);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0052Response search(Mm0052Request req) {
		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final int allCount = repository.count(req);
		final Mm0052Response res = createResponse(Mm0052Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		// 抽出
		res.results = repository.search(req);
		res.success = true;
		return res;
	}

	/**
	 * 追加
	 * @param req
	 * @return
	 */
	public Mm0052Response add(Mm0052Request req) {
		final MwmPartsSequenceSpecEx sequence = new MwmPartsSequenceSpecEx();
		sequence.setCorporationCode(req.corporationCode);
		sequence.setDeleteFlag(DeleteFlag.OFF);

		final Mm0052Response res = createResponse(Mm0052Response.class, req);
		res.sequence = sequence;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.post));
		return res;
	}
}
