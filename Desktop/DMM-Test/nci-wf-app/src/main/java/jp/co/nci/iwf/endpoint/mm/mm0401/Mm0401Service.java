package jp.co.nci.iwf.endpoint.mm.mm0401;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmCorporationGroupInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmCorporationGroupInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmCorporationGroupInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmCorporationInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmCorporationGroupOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmCorporationGroupOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmCorporationGroupOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmCorporationOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.base.WfmCorporationGroup;
import jp.co.nci.integrated_workflow.model.custom.WfcCorporationGroup;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationGroupInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 企業グループマスタ設定サービス
 */
@BizLogic
public class Mm0401Service extends BaseService {
	@Inject private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0401InitResponse init(Mm0401InitRequest req) {
		final Mm0401InitResponse res = createResponse(Mm0401InitResponse.class, req);
		if (isNotEmpty(req.corporationGroupCode) && req.timestampUpdated == null)
			throw new BadRequestException("最終更新日時が未指定です");

		if (isEmpty(req.corporationGroupCode)) {
			res.corporationGroup = new WfcCorporationGroup();
		} else {
			// 既存レコード
			res.corporationGroup = getCorporationGroup(req.corporationGroupCode);

			// 排他チェック
			if (res.corporationGroup.getTimestampUpdated().getTime() != req.timestampUpdated) {
				throw new AlreadyUpdatedException();
			}
		}
		res.success = true;
		return res;
	}

	/** 企業グループマスタを抽出 */
	private WfcCorporationGroup getCorporationGroup(String corporationGroupCode) {
		if (isEmpty(corporationGroupCode))
			return null;

		final SearchWfmCorporationGroupInParam in = new SearchWfmCorporationGroupInParam();
		in.setLoginCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setCorporationGroupCode(corporationGroupCode);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		List<WfcCorporationGroup> list = wf.searchWfmCorporationGroup(in).getCorporationGroupList();
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0401InitResponse save(Mm0401Request req) {
		WfcCorporationGroup inputed = req.corporationGroup;
		if (inputed == null)
			throw new BadRequestException("企業グループが未指定です");
		if (inputed.getCorporationGroupCode() == null)
			throw new BadRequestException("企業グループコードが未指定です");


		final WfmCorporationGroup current = getCorporationGroup(
				inputed.getCorporationGroupCode());

		Mm0401InitResponse res = createResponse(Mm0401InitResponse.class, req);
		if (current == null) {
			// 入力内容を新規レコードにする
			final InsertWfmCorporationGroupInParam in = new InsertWfmCorporationGroupInParam();
			in.setWfmCorporationGroup(inputed);
			in.setWfUserRole(sessionHolder.getWfUserRole());

			final InsertWfmCorporationGroupOutParam out = wf.insertWfmCorporationGroup(in);
			if (!eq(ReturnCode.SUCCESS, out.getReturnCode())) {
				throw new WfException(out);
			}

			res.corporationGroup = out.getWfmCorporationGroup();
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.corporationGroup));
			res.success = true;
		} else {
			// 入力内容を反映
			current.setCorporationGroupAddedInfo(inputed.getCorporationGroupAddedInfo());
			current.setCorporationGroupName(inputed.getCorporationGroupName());
			current.setTimestampUpdated(inputed.getTimestampUpdated());
			current.setDeleteFlag(DeleteFlag.OFF);

			final UpdateWfmCorporationGroupInParam in = new UpdateWfmCorporationGroupInParam();
			in.setWfmCorporationGroup(current);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final UpdateWfmCorporationGroupOutParam out = wf.updateWfmCorporationGroup(in);
			if (!eq(ReturnCode.SUCCESS, out.getReturnCode())) {
				throw new WfException(out);
			}
			res.corporationGroup = getCorporationGroup(current.getCorporationGroupCode());
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.corporationGroup));
			res.success = true;
		}
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0401InitResponse delete(Mm0401Request req) {
		if (req.corporationGroup == null)
			throw new BadRequestException("企業グループが未指定です");
		if (req.corporationGroup.getCorporationGroupCode() == null)
			throw new BadRequestException("企業グループコードが未指定です");
		if (req.corporationGroup.getTimestampUpdated() == null)
			throw new BadRequestException("最終更新日時が未指定です");

		final WfmCorporationGroup current = getCorporationGroup(
				req.corporationGroup.getCorporationGroupCode());
		if (current == null)
			throw new NotFoundException("対象レコードは存在しません");

		// 企業グループの削除
		{
			final DeleteWfmCorporationGroupInParam in = new DeleteWfmCorporationGroupInParam();
			in.setWfmCorporationGroup(req.corporationGroup);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final DeleteWfmCorporationGroupOutParam out = wf.deleteWfmCorporationGroup(in);
			if (!eq(ReturnCode.SUCCESS, out.getReturnCode()))
					throw new WfException(out);
		}
		// 企業グループに属していた企業から、企業グループコードを除去
		List<WfmCorporation> corps = getWfmCorporationByCorporationGroup(req.corporationGroup.getCorporationGroupCode());
		for (WfmCorporation c : corps) {
			c.setCorporationGroupCode(null);
			final UpdateWfmCorporationInParam in = new UpdateWfmCorporationInParam();
			in.setWfmCorporation(c);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final UpdateWfmCorporationOutParam out = wf.updateWfmCorporation(in);
			if (!eq(ReturnCode.SUCCESS, out.getReturnCode()))
				throw new WfException(out);
		}

		// 読み直し
		final Mm0401InitResponse res = createResponse(Mm0401InitResponse.class, req);
		res.corporationGroup = getCorporationGroup(
				req.corporationGroup.getCorporationGroupCode());
		res.addSuccesses(i18n.getText(MessageCd.MSG0197));
		res.success = true;
		return res;
	}

	/** 企業グループに属する企業を抽出 */
	private List<WfmCorporation> getWfmCorporationByCorporationGroup(String corporationGroupCode) {
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setLoginCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setCorporationGroupCode(corporationGroupCode);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		return wf.searchWfmCorporation(in).getCorporations();
	}
}
