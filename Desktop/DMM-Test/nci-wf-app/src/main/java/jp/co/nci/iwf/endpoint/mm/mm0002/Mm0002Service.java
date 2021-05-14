package jp.co.nci.iwf.endpoint.mm.mm0002;

import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmOrganizationOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmOrganizationOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.base.impl.WfmOrganizationImpl;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 組織編集画面サービス
 */
@BizLogic
public class Mm0002Service extends BaseService {

	@Inject @SuppressWarnings("unused") private Mm0002Repository repository;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0002InitResponse init(Mm0002InitRequest req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.organizationCode))
			throw new BadRequestException("企業コードまたは組織コードが未指定です");
		if (req.timestampUpdated == null)
			throw new BadRequestException("最終更新日時が未指定です");

		final Mm0002InitResponse res = createResponse(Mm0002InitResponse.class, req);
		res.org = getOrganization(req.corporationCode, req.organizationCode);

		// 排他チェック
		if (!eq(req.timestampUpdated, res.org.getTimestampUpdated().getTime()))
			throw new AlreadyUpdatedException();

		res.success = (res.org != null);
		return res;
	}

	private WfcOrganization getOrganization(String corporationCode, String organizationCode) {
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCode(organizationCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		List<WfcOrganization> list = wf.searchWfmOrganization(in).getOrganizationList();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * 組織の更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0002UpdateResponse update(Mm0002UpdateRequest req) {
		final String corporationCode = req.org.getCorporationCode();
		final String organizationCode = req.org.getOrganizationCode();
		if (isEmpty(corporationCode) || isEmpty(organizationCode))
			throw new BadRequestException("企業コードまたは組織コードが未指定です");

		final WfcOrganization inputed = req.org;
		final WfcOrganization org = getOrganization(corporationCode, organizationCode);
		org.setOrganizationName(inputed.getOrganizationName());
		org.setOrganizationNameAbbr(inputed.getOrganizationNameAbbr());
		org.setOrganizationAddedInfo(inputed.getOrganizationAddedInfo());
		org.setOrganizationCodeUp(inputed.getOrganizationCodeUp());
		org.setOrganizationLevel(inputed.getOrganizationLevel());
		org.setTelNum(inputed.getTelNum());
		org.setAddress(inputed.getAddress());
		org.setFaxNum(inputed.getFaxNum());
		org.setValidStartDate(inputed.getValidStartDate());
		org.setValidEndDate(inputed.getValidEndDate());
		org.setDeleteFlag(DeleteFlag.OFF);	// 更新したら常に有効にする
		org.setExtendedInfo01(inputed.getExtendedInfo01());
		org.setExtendedInfo02(inputed.getExtendedInfo02());
		org.setExtendedInfo03(inputed.getExtendedInfo03());
		org.setExtendedInfo04(inputed.getExtendedInfo04());
		org.setExtendedInfo05(inputed.getExtendedInfo05());
		org.setExtendedInfo06(inputed.getExtendedInfo06());
		org.setExtendedInfo07(inputed.getExtendedInfo07());
		org.setExtendedInfo08(inputed.getExtendedInfo08());
		org.setExtendedInfo09(inputed.getExtendedInfo09());
		org.setExtendedInfo10(inputed.getExtendedInfo10());

		final UpdateWfmOrganizationInParam in = new UpdateWfmOrganizationInParam();
		in.setWfmOrganization(org);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		final UpdateWfmOrganizationOutParam out = wf.updateWfmOrganization(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		final Mm0002UpdateResponse res = createResponse(Mm0002UpdateResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, i18n.getText(MessageCd.organization)));
		res.success = (out.getWfmOrganization() != null);
		res.org = getOrganization(corporationCode, organizationCode);
		return res;
	}

	/**
	 * 組織の削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0002UpdateResponse delete(Mm0002UpdateRequest req) {
		final String corporationCode = req.org.getCorporationCode();
		final String organizationCode = req.org.getOrganizationCode();
		final Long timestampUpdated = req.org.getTimestampUpdated() == null ? null : req.org.getTimestampUpdated().getTime();

		String error = deleteOrg(corporationCode, organizationCode, timestampUpdated);

		final Mm0002UpdateResponse res = createResponse(Mm0002UpdateResponse.class, req);
		if (isEmpty(error)) {
			res.org = getOrganization(corporationCode, organizationCode);
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, i18n.getText(MessageCd.organization)));
			res.success = true;
		} else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	/**
	 * 組織の削除
	 * @param corporationCode 企業コード
	 * @param organizationCode 組織コード
	 * @param timestampUpdated 組織の最終更新日時(Long)
	 * @return エラーがあればそのエラー内容。エラーナシなら null
	 */
	@Transactional
	public String deleteOrg(String corporationCode, String organizationCode, Long timestampUpdated) {
		if (isEmpty(corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(organizationCode))
			throw new BadRequestException("組織コードが未指定です");
		if (timestampUpdated == null)
			throw new BadRequestException("最終更新日時が未指定です");

//		// 組織に所属しているユーザがいないか？
//		int count = repository.countUser(corporationCode, organizationCode, baseDate);
//		if (count > 0) {
//			// {0}には基準日={1}時点で配下に{2}がいるため、削除できません。
//			String dt = toStr(baseDate);
//			return i18n.getText(MessageCd.MSG0194, MessageCd.organization, dt, MessageCd.user);
//		}

		final WfmOrganizationImpl org = new WfmOrganizationImpl();
		org.setCorporationCode(corporationCode);
		org.setOrganizationCode(organizationCode);
		org.setTimestampUpdated(new Timestamp(timestampUpdated));
		final DeleteWfmOrganizationInParam in = new DeleteWfmOrganizationInParam();
		in.setWfmOrganization(org);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		// 削除
		final DeleteWfmOrganizationOutParam out = wf.deleteWfmOrganization(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		return null;
	}

}
