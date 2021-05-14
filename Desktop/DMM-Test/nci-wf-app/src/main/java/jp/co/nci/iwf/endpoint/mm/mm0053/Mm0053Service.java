package jp.co.nci.iwf.endpoint.mm.mm0053;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmPartsSequenceSpecEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;

/**
 * ルックアップ設定サービス
 */
@BizLogic
public class Mm0053Service extends BaseService {
	@Inject
	private Mm0053Repository repository;
	@Inject
	private NumberingService numbering;
	@Inject
	private WfmLookupService wfmlookupService;
	@Inject
	private MwmLookupService lookupService;
	@Inject
	private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0053Response init(Mm0053Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Mm0053Response res = createResponse(Mm0053Response.class, req);

		MwmPartsSequenceSpecEx sequenceEx = new MwmPartsSequenceSpecEx();
		MwmPartsSequenceSpec sequence = repository.getSequence(req.corporationCode, req.partsSequenceSpecCode, sessionHolder.getLoginInfo().getLocaleCode());

		// 新規登録
		if (sequence == null) {
			sequenceEx.setCorporationCode(req.corporationCode);
			sequenceEx.setDeleteFlag(DeleteFlag.OFF);
			sequenceEx.setStartValue(1L);
			res.success = true;
		// 更新
		} else {
			try {
				BeanUtils.copyProperties(sequenceEx, sequence);
			} catch (InvocationTargetException ite) {
			} catch (IllegalAccessException iae) {
			}
			// 排他
			if (!eq(req.version, sequenceEx.getVersion())) {
				res.success = false;
				res.addAlerts(i18n.getText(MessageCd.MSG0050));
			} else {
				res.success = true;
			}
		}
		res.sequence = sequenceEx;

		// プルダウン
		res.deleteFlagList = wfmlookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.resetTypeList = lookupService.getOptionItems(LookupGroupId.RESET_TYPE, true);
		res.sequenceLengthList = lookupService.getOptionItems(LookupGroupId.SEQUENCE_LENGTH, true);

		return res;
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0053Response insert(Mm0053InsertRequest req) {
		final Mm0053Response res = createResponse(Mm0053Response.class, req);
		if (repository.getSequence(req.sequence.getCorporationCode(), req.sequence.getPartsSequenceSpecCode(), sessionHolder.getLoginInfo().getLocaleCode()) != null) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.sequenceCode, req.sequence.getPartsSequenceSpecCode()));
			res.success = false;
			return res;
		}

		MwmPartsSequenceSpec sequence = new MwmPartsSequenceSpec();
		try {
			BeanUtils.copyProperties(sequence, req.sequence);
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}
		sequence.setPartsSequenceSpecId(numbering.newPK(MwmPartsSequenceSpec.class));
		repository.insert(sequence);

		// 多言語対応
		multi.save("MWM_PARTS_SEQUENCE_SPEC", sequence.getPartsSequenceSpecId(), "PARTS_SEQUENCE_SPEC_NAME", sequence.getPartsSequenceSpecName());

		MwmPartsSequenceSpecEx sequenceEx = new MwmPartsSequenceSpecEx();
		try {
			BeanUtils.copyProperties(sequenceEx, sequence);
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}
		res.sequence = sequenceEx;

		// プルダウン
		res.deleteFlagList = wfmlookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.sequenceLengthList = lookupService.getOptionItems(LookupGroupId.SEQUENCE_LENGTH, true);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.sequence));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0053Response update(Mm0053InsertRequest req) {
		MwmPartsSequenceSpec sequence = repository.getSequence(req.sequence.getCorporationCode(), req.sequence.getPartsSequenceSpecCode(), sessionHolder.getLoginInfo().getLocaleCode());
		sequence.setPartsSequenceSpecName(req.sequence.getPartsSequenceSpecName());
		sequence.setResetType(req.sequence.getResetType());
		sequence.setSequenceLength(req.sequence.getSequenceLength());
		sequence.setStartValue(req.sequence.getStartValue());
		sequence.setDeleteFlag(req.sequence.getDeleteFlag());
		repository.update(sequence);

		// 多言語対応
		multi.save("MWM_PARTS_SEQUENCE_SPEC", sequence.getPartsSequenceSpecId(), "PARTS_SEQUENCE_SPEC_NAME", sequence.getPartsSequenceSpecName());

		final Mm0053Response res = createResponse(Mm0053Response.class, req);

		MwmPartsSequenceSpecEx sequenceEx = new MwmPartsSequenceSpecEx();
		try {
			BeanUtils.copyProperties(sequenceEx, sequence);
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}

		res.sequence = sequenceEx;

		// プルダウン
		res.deleteFlagList = wfmlookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.sequenceLengthList = lookupService.getOptionItems(LookupGroupId.SEQUENCE_LENGTH, true);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.sequence));
		res.success = true;
		return res;
	}
}
