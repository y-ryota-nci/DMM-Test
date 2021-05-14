package jp.co.nci.iwf.endpoint.mm.mm0013;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;

/**
 * ルックアップ設定サービス
 */
@BizLogic
public class Mm0013Service extends BaseService {
	@Inject
	private Mm0013Repository repository;
	@Inject
	private NumberingService numbering;
	@Inject
	private WfmLookupService lookupService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0013Response init(Mm0013Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.lookupGroupId))
			throw new BadRequestException("企業コードが未指定です");

		final Mm0013Response res = createResponse(Mm0013Response.class, req);

		MwmLookupEx lookupEx = new MwmLookupEx();

		MwmLookup lookup = repository.getLookup(req.corporationCode, req.lookupGroupId, req.lookupId, sessionHolder.getLoginInfo().getLocaleCode());
		MwmLookupGroup lookupGroup = repository.getLookupGroup(req.corporationCode, req.lookupGroupId, sessionHolder.getLoginInfo().getLocaleCode());

		if (lookup == null) {
			lookupEx.setCorporationCode(req.corporationCode);
			lookupEx.setLookupGroupId(req.lookupGroupId);
			lookupEx.setDeleteFlag(DeleteFlag.OFF);
		} else {
			try {
				BeanUtils.copyProperties(lookupEx, lookup);
			} catch (InvocationTargetException ite) {
			} catch (IllegalAccessException iae) {
			}
		}
		lookupEx.setLookupGroupName(lookupGroup.getLookupGroupName());
		res.lookup = lookupEx;

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		// 排他
		if (lookup != null && !eq(req.timestampUpdated, lookupEx.getTimestampUpdated().getTime())) {
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
		} else {
			res.success = true;
		}
		return res;
	}

	/**
	 * ルックアップ登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0013Response insert(Mm0013InsertRequest req) {
		final Mm0013Response res = createResponse(Mm0013Response.class, req);
		if (repository.getLookup(req.lookup.getCorporationCode(), req.lookup.getLookupGroupId(), req.lookup.getLookupId(), sessionHolder.getLoginInfo().getLocaleCode()) != null) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.lookupId, req.lookup.getLookupId()));
			res.success = false;
			return res;
		}

		MwmLookup lookup = new MwmLookup();

		lookup.setScreenLookupId(numbering.newPK(MwmLookup.class));
		lookup.setCorporationCode(req.lookup.getCorporationCode());
		lookup.setLookupGroupId(req.lookup.getLookupGroupId());
		lookup.setLookupId(req.lookup.getLookupId());
		lookup.setLookupName2(req.lookup.getLookupName2());
		lookup.setLookupName(req.lookup.getLookupName());
		lookup.setSortOrder(req.lookup.getSortOrder());
		lookup.setLocaleCode(sessionHolder.getLoginInfo().getLocaleCode());
		lookup.setDeleteFlag(DeleteFlag.OFF);

		repository.insert(lookup);

		MwmLookupEx lookupEx = new MwmLookupEx();
		try {
			BeanUtils.copyProperties(lookupEx, lookup);
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}
		res.lookup = lookupEx;

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.lookup));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0013Response update(Mm0013InsertRequest req) {
		MwmLookup lookup = repository.getLookup(req.lookup.getCorporationCode(), req.lookup.getLookupGroupId(), req.lookup.getLookupId(), sessionHolder.getLoginInfo().getLocaleCode());

		lookup.setLookupName2(req.lookup.getLookupName2());
		lookup.setLookupName(req.lookup.getLookupName());
		lookup.setSortOrder(req.lookup.getSortOrder());
		lookup.setDeleteFlag(req.lookup.getDeleteFlag());
		lookup.setLocaleCode(sessionHolder.getLoginInfo().getLocaleCode());

		repository.update(lookup);

		final Mm0013Response res = createResponse(Mm0013Response.class, req);

		MwmLookupEx lookupEx = new MwmLookupEx();
		try {
			BeanUtils.copyProperties(lookupEx, lookup);
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}
		MwmLookupGroup lookupGroup = repository.getLookupGroup(req.lookup.getCorporationCode(), req.lookup.getLookupGroupId(), sessionHolder.getLoginInfo().getLocaleCode());
		lookupEx.setLookupGroupName(lookupGroup.getLookupGroupName());

		res.lookup = lookupEx;

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.lookup));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0013Response delete(Mm0013InsertRequest req) {
		MwmLookup lookup = repository.getLookup(req.lookup.getCorporationCode(), req.lookup.getLookupGroupId(), req.lookup.getLookupId(), sessionHolder.getLoginInfo().getLocaleCode());

		lookup.setLookupName2(req.lookup.getLookupName2());
		lookup.setLookupName(req.lookup.getLookupName());
		lookup.setSortOrder(req.lookup.getSortOrder());
		lookup.setDeleteFlag(DeleteFlag.ON);

		repository.delete(lookup);

		final Mm0013Response res = createResponse(Mm0013Response.class, req);

		MwmLookupEx lookupEx = new MwmLookupEx();
		try {
			BeanUtils.copyProperties(lookupEx, lookup);
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}
		MwmLookupGroup lookupGroup = repository.getLookupGroup(req.lookup.getCorporationCode(), req.lookup.getLookupGroupId(), sessionHolder.getLoginInfo().getLocaleCode());
		lookupEx.setLookupGroupName(lookupGroup.getLookupGroupName());

		res.lookup = lookupEx;

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.lookup));
		res.success = true;
		return res;
	}

}
