package jp.co.nci.iwf.endpoint.mm.mm0011;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;

/**
 * ルックアップグループ登録サービス
 */
@BizLogic
public class Mm0011Service extends BaseService {
	@Inject
	private Mm0011Repository repository;
	@Inject
	private NumberingService numbering;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0011Response init(Mm0011Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Mm0011Response res = createResponse(Mm0011Response.class, req);
		res.lookupGroup = new MwmLookupGroup();
		res.lookupGroup.setCorporationCode(req.corporationCode);
		res.lookupGroup.setDeleteFlag(DeleteFlag.OFF);
		res.success = (res.lookupGroup != null);
		return res;
	}

	/**
	 * ルックアップグループの登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0011Response insert(Mm0011InsertRequest req) {
		final Mm0011Response res = createResponse(Mm0011Response.class, req);

		if (repository.searchLookUpGroup(req.lookupGroup.getCorporationCode(), req.lookupGroup.getLookupGroupId(), sessionHolder.getLoginInfo().getLocaleCode()) != null) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.lookupGroupId, req.lookupGroup.getLookupGroupId()));
			res.success = false;
			return res;
		}

		MwmLookupGroup lookupGroup = new MwmLookupGroup();

		lookupGroup.setScreenLookupGroupId(numbering.newPK(MwmLookupGroup.class));
		lookupGroup.setCorporationCode(req.lookupGroup.getCorporationCode());
		lookupGroup.setLookupGroupId(req.lookupGroup.getLookupGroupId());
		lookupGroup.setLookupGroupName(req.lookupGroup.getLookupGroupName());
		lookupGroup.setSortOrder(req.lookupGroup.getSortOrder());
		lookupGroup.setLocaleCode(sessionHolder.getLoginInfo().getLocaleCode());
		lookupGroup.setDeleteFlag(DeleteFlag.OFF);

		repository.insert(lookupGroup);

		res.lookupGroup = lookupGroup;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.lookupGroup));
		res.success = true;
		return res;
	}
}
