package jp.co.nci.iwf.endpoint.mm.mm0051;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 採番形式設定サービス
 */
@BizLogic
public class Mm0051Service extends BaseService {
	@Inject
	private Mm0051Repository repository;
	@Inject
	private WfmLookupService wfmLookupService;
	@Inject
	private MwmLookupService lookupService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0051Response init(Mm0051InitRequest req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}

		final Mm0051Response res = createResponse(Mm0051Response.class, req);

		Mm0051Entity entity = null;
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		if (isEmpty(req.partsNumberingFormatCode)) {
			entity = new Mm0051Entity();
			entity.corporationCode = req.corporationCode;
			entity.deleteFlag = DeleteFlag.OFF;
			res.success = true;
		}
		else {
			String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
			entity = repository.get(req.corporationCode, req.partsNumberingFormatCode, localeCode);

			if (entity == null) {
				res.success = false;
				res.addAlerts(i18n.getText(MessageCd.MSG0047));
			} else if (!eq(req.version, entity.version)) {
				// 排他
				res.success = false;
				res.addAlerts(i18n.getText(MessageCd.MSG0050));
			} else {
				res.success = true;
			}
		}

		res.entity = entity;

		// プルダウン
		createOptionLists(req.corporationCode, res);

		return res;
	}

	private void createOptionLists(String corporationCode, final Mm0051Response res) {
		res.deleteFlagList = wfmLookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.numberingFormatTypes = lookupService.getOptionItems(LookupGroupId.NUMBERING_FORMAT_TYPE, true);
		res.numberingFormatDates = lookupService.getOptionItems(LookupGroupId.NUMBERING_FORMAT_DATE, true);
		res.numberingFormatOrgs = lookupService.getOptionItems(LookupGroupId.NUMBERING_FORMAT_ORG, true);
		// 連番
		List<OptionItem> sequenceList = new ArrayList<>();
		sequenceList.add(OptionItem.EMPTY);
		repository.getSequenceList(corporationCode).forEach(seq -> {
			sequenceList.add(new OptionItem(seq.getPartsSequenceSpecId(), seq.getPartsSequenceSpecName()));
		});
		res.sequenceList = sequenceList;
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0051Response insert(Mm0051Request req) {

		final Mm0051Response res = createResponse(Mm0051Response.class, req);

		// 既存パーツ採番コードの重複チェック
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		if (repository.get(req.input.corporationCode, req.input.partsNumberingFormatCode, localeCode) != null) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.numberFormat, req.input.partsNumberingFormatCode));
			res.success = false;
			return res;
		}

		// インサート
		repository.insert(req.input);

		// 読み直し
		res.entity = repository.get(req.input.corporationCode, req.input.partsNumberingFormatCode, localeCode);

		// プルダウン
		createOptionLists(req.input.corporationCode, res);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.numberingFormat));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0051Response update(Mm0051Request req) {
		final Mm0051Response res = createResponse(Mm0051Response.class, req);

		// 更新
		repository.update(req.input);

		// 読み直し
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		res.entity = repository.get(req.input.corporationCode, req.input.partsNumberingFormatCode, localeCode);

		// プルダウン
		createOptionLists(req.input.corporationCode, res);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.numberingFormat));
		res.success = true;
		return res;
	}

	/**  連番設定の選択肢を返す */
	public Mm0051Response getSequenceList(Mm0051InitRequest req) {
		Mm0051Response res = createResponse(Mm0051Response.class, null);
		createOptionLists(req.corporationCode, res);
		res.success = true;
		return res;
	}
}
