package jp.co.nci.iwf.endpoint.mm.mm0003;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.OutParamBase;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmUserBelongInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmUserInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmUserBelongInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmUserBelongInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmUserInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmUserBelongOutParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmUserOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmUserOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.JobType;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmUserBelong;
import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserBelongInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 【プロファイル管理】ユーザ編集画面サービス
 */
@BizLogic
public class Mm0003Service extends BaseService {
	@Inject private WfInstanceWrapper wf;
	@Inject Mm0003Validator validator;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0003InitResponse init(Mm0003InitRequest req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.userCode))
			throw new BadRequestException("企業コードまたはユーザコードが未指定です");
		if (req.timestampUpdated == null)
			throw new BadRequestException("最終更新日時が未指定です");

		final Mm0003InitResponse res = createResponse(Mm0003InitResponse.class, req);

		// 基準日
		final Date baseDate = req.baseDate != null ? req.baseDate : today();
		res.baseDate = baseDate;
		// 言語の選択肢
		res.localeCodes = localeService.getSelectableLocaleCodeOptions();

		// ユーザ情報
		res.user = getWfmUser(req.corporationCode, req.userCode);
		// ユーザ所属情報
		res.belongs = getWfmUserBelong(req.corporationCode, req.userCode);

		// 排他チェック
		if (!eq(req.timestampUpdated, res.user.getTimestampUpdated().getTime()))
			throw new AlreadyUpdatedException();

		// TODO 所有予定のロール
		// TODO 代理するユーザ
		// TODO 代理してもらうユーザ

		res.success = true;
		return res;
	}

	/** ユーザ所属マスタ抽出 */
	private List<WfcUserBelong> getWfmUserBelong(String corporationCode, String userCode) {
		final SearchWfmUserBelongInParam in = new SearchWfmUserBelongInParam();
		in.setCorporationCode(corporationCode);
		in.setUserCode(userCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		in.setOrderBy(new OrderBy[] {
			new OrderBy(true, "A." + WfvUserBelong.CORPORATION_CODE),
			new OrderBy(true, "A." + WfvUserBelong.USER_CODE),
			new OrderBy(true, "A." + WfvUserBelong.SEQ_NO_USER_BELONG),
		});
		return wf.searchWfmUserBelong(in).getUserBelongList();
	}

	/** ユーザマスタ抽出 */
	private WfmUser getWfmUser(String corporationCode, String userCode) {
		if (isEmpty(corporationCode) || isEmpty(userCode))
			throw new BadRequestException("corporationCode=" + corporationCode + " userCode=" + userCode);

		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(corporationCode);
		in.setUserCode(userCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);

		final List<WfmUser> list = wf.searchWfmUser(in).getUserList();
		if (list == null || list.isEmpty()) {
			throw new BadRequestException(String.format("企業コード=%s ユーザコード=%s のユーザ情報がありません", corporationCode, userCode));
		}
		return list.get(0);
	}

	/**
	 * ユーザ更新
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse update(Mm0003UpdateUserRequest req) {

		// バリデーション
		final List<String> alerts = new ArrayList<>();
		alerts.addAll( validator.validateUpdateUser(req.user) );
		alerts.addAll( validator.validateUpdateUserBelong(req) );

		BaseResponse res;
		if (alerts.isEmpty()) {
			// ユーザ情報の更新
			jp.co.nci.integrated_workflow.model.base.WfmUser newUser = updateUser(req);

			// ユーザ所属の更新
			updateUserBelong(req);

			// すべて正常終了したので、読み直し
			final Mm0003InitRequest newReq = new Mm0003InitRequest();
			newReq.baseDate = req.baseDate;
			newReq.corporationCode = newUser.getCorporationCode();
			newReq.userCode = newUser.getUserCode();
			newReq.timestampUpdated = newUser.getTimestampUpdated().getTime();
			res = init(newReq);
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.user));
			res.success = true;
		}
		else {
			// エラーあり
			res = createResponse(BaseResponse.class, req);
			res.alerts = alerts;
			res.success = false;
		}
		return res;
	}

	/** ユーザ所属の差分更新 */
	private void updateUserBelong(Mm0003UpdateUserRequest req) {
		// 既存データの読み込み
		final List<WfcUserBelong> currents = getWfmUserBelong(req.user.getCorporationCode(), req.user.getUserCode());

		// 入力内容に対して、既存データで消込を行う
		final List<WfcUserBelong> inputs = req.belongs;
		for (Iterator<WfcUserBelong> it = inputs.iterator(); it.hasNext(); ) {
			WfcUserBelong ub = it.next();

			if (!JobType.MAIN.equals(ub.getJobType())) {
				ub.setJobType(JobType.SUB);
			}
			if (!CommonFlag.ON.equals(ub.getImmediateManagerFlag())) {
				ub.setImmediateManagerFlag(CommonFlag.OFF);
			}

			// ユーザ所属連番のマイナス値はDB書き込み前の方便として仮のユニークキーを
			// 発行しているだけなので、採番できるようNULLで上書き
			if (ub.getSeqNoUserBelong() < 0L) {
				ub.setSeqNoUserBelong(null);
			}

			OutParamBase out = null;
			if (ub.getSeqNoUserBelong() == null) {
				// 新規
				final InsertWfmUserBelongInParam in = new InsertWfmUserBelongInParam();
				in.setWfmUserBelong(ub);
				in.setWfUserRole(sessionHolder.getWfUserRole());
				out = wf.insertWfmUserBelong(in);
			}
			else if (currents.contains(ub)) {
				// 更新
				final WfmUserBelong current = currents.get(currents.indexOf(ub));
				current.setJobType(ub.getJobType());
				current.setOrganizationCode(ub.getOrganizationCode());
				current.setPostCode(ub.getPostCode());
				current.setDirectorFlag(ub.getDirectorFlag());
				current.setImmediateManagerFlag(ub.getImmediateManagerFlag());
				current.setValidStartDate(ub.getValidStartDate());
				current.setValidEndDate(ub.getValidEndDate());

				final UpdateWfmUserBelongInParam in = new UpdateWfmUserBelongInParam();
				in.setWfmUserBelong(current);
				in.setWfUserRole(sessionHolder.getWfUserRole());
				out = wf.updateWfmUserBelong(in);

				// 使用した現行データを消込
				currents.remove(ub);
			}
			if (out != null && !ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}

		// 既存データで消し込まれなかった残余は削除
		for (WfcUserBelong cur : currents) {
			final DeleteWfmUserBelongInParam in = new DeleteWfmUserBelongInParam();
			in.setWfmUserBelong(cur);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final DeleteWfmUserBelongOutParam out = wf.deleteWfmUserBelong(in);

			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}
	}

	/** ユーザマスタの更新 */
	private jp.co.nci.integrated_workflow.model.base.WfmUser updateUser(Mm0003UpdateUserRequest req) {
		// 既存データの読み込み
		final WfmUser user = getWfmUser(req.user.getCorporationCode(), req.user.getUserCode());

		// 画面内容の反映
		user.setUserAddedInfo(req.user.getUserAddedInfo());
		user.setUserName(req.user.getUserName());
		user.setUserNameAbbr(req.user.getUserNameAbbr());
		user.setSealName(req.user.getSealName());
		user.setPostNum(req.user.getPostNum());
		user.setAddress(req.user.getAddress());
		user.setTelNum(req.user.getTelNum());
		user.setTelNumCel(req.user.getTelNumCel());
		user.setMailAddress(req.user.getMailAddress());
		user.setExtendedInfo01(req.user.getExtendedInfo01());
		user.setExtendedInfo02(req.user.getExtendedInfo02());
		user.setExtendedInfo03(req.user.getExtendedInfo03());
		user.setExtendedInfo04(req.user.getExtendedInfo04());
		user.setExtendedInfo05(req.user.getExtendedInfo05());
		user.setValidStartDate(req.user.getValidStartDate());
		user.setValidEndDate(req.user.getValidEndDate());
		user.setDeleteFlag(CommonFlag.OFF);
		user.setTimestampUpdated(req.user.getTimestampUpdated());
		user.setDefaultLocaleCode(req.user.getDefaultLocaleCode());

		// ユーザ情報の更新
		final UpdateWfmUserInParam in = new UpdateWfmUserInParam();
		in.setWfmUser(user);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final UpdateWfmUserOutParam out = wf.updateWfmUser(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		return out.getWfmUser();
	}

	/**
	 * ユーザの削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Mm0003UpdateUserRequest req) {
		if (req.user == null)
			throw new BadRequestException("ユーザが指定されていません");

		final String corporationCode = req.user.getCorporationCode();
		final String userCode = req.user.getUserCode();
		WfmUser user = getWfmUser(corporationCode, userCode);
		user.setDeleteFlag(DeleteFlag.ON);
		user.setTimestampUpdated(req.user.getTimestampUpdated());

		// バリデーション
		String error = validator.validateDelete(user);
		if (isNotEmpty(error)) {
			BaseResponse res = createResponse(BaseResponse.class, req);
			res.addAlerts(error);
			res.success = false;
			return res;
		}

		// ユーザ情報の削除
		final DeleteWfmUserInParam in = new DeleteWfmUserInParam();
		in.setWfmUser(user);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final DeleteWfmUserOutParam out = wf.deleteWfmUser(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		// 再表示時の排他ロック回避用に、最終更新日時を読み直し
		user = getWfmUser(corporationCode, userCode);

		// すべて正常終了したので、読み直し
		final Mm0003InitRequest newReq = new Mm0003InitRequest();
		newReq.baseDate = defaults(req.baseDate, today());
		newReq.corporationCode = user.getCorporationCode();
		newReq.userCode = user.getUserCode();
		newReq.timestampUpdated = user.getTimestampUpdated().getTime();

		final BaseResponse res = init(newReq);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.user));
		res.success = true;
		return res;
	}

}
