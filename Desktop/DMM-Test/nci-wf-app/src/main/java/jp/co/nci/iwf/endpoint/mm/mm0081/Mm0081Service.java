package jp.co.nci.iwf.endpoint.mm.mm0081;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.callback.CallbackFunctionIF;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmFunctionInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmFunctionInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmFunctionInParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmFunctionOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.integrated_workflow.param.input.SearchWfmFunctionInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmFunctionOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * アクション設定サービス
 */
@BizLogic
public class Mm0081Service extends BaseService {
	@Inject
	private WfmLookupService lookupService;

	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0081Response init(Mm0081Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Mm0081Response res = createResponse(Mm0081Response.class, req);

		if (CommonUtil.isEmpty(req.functionCode)) {
			res.function = new WfmFunction();
			res.function.setCorporationCode(req.corporationCode);
			res.function.setDeleteFlag(DeleteFlag.OFF);
		} else {
			// アクション機能取得
			res.function = this.getWfmFunction(req.corporationCode, req.functionCode);
		}

		// アクション区分の選択肢
		res.synchronousFunctionFlagList = lookupService.getOptionItems(true, LookupTypeCode.SYNCHRONOUS_FUNCTION_FLAG);
		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(true, LookupTypeCode.DELETE_FLAG);

		res.success = true;
		return res;
	}

	/**
	 * アクション登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0081Response insert(Mm0081InsertRequest req) {
		final Mm0081Response res = createResponse(Mm0081Response.class, req);

		// バリデーション
		String error = validate(req);
		if (isNotEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
		}
		else {
			// アクション取得
			SearchWfmFunctionInParam saIn = new SearchWfmFunctionInParam();
			saIn.setCorporationCode(req.function.getCorporationCode());
			saIn.setFunctionCode(req.function.getFunctionCode());
			saIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmFunctionOutParam saOut = wf.searchWfmFunction(saIn);

			if (!CommonUtil.isEmpty(saOut.getFunctions())) {
				res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.function, req.function.getFunctionCode()));
				res.success = false;
				return res;
			}

			InsertWfmFunctionInParam insertIn = new InsertWfmFunctionInParam();
			insertIn.setWfmFunction(req.function);
			insertIn.setWfUserRole(sessionHolder.getWfUserRole());
			wf.insertWfmFunction(insertIn);

			// 参加者ロール構成取得
			saIn.setCorporationCode(req.function.getCorporationCode());
			saIn.setFunctionCode(req.function.getFunctionCode());
			saIn.setWfUserRole(sessionHolder.getWfUserRole());
			saOut = wf.searchWfmFunction(saIn);
			res.function = saOut.getFunctions().get(0);

			// 削除区分の選択肢
			res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.function));
			res.success = true;
		}
		return res;
	}

	@Transactional
	public Mm0081Response update(Mm0081InsertRequest req) {
		final Mm0081Response res = createResponse(Mm0081Response.class, req);

		// バリデーション
		String error = validate(req);
		if (isNotEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
		}
		else {
			final WfmFunction function = this.getWfmFunction(req.function.getCorporationCode(), req.function.getFunctionCode());
			if (function == null)
				throw new AlreadyUpdatedException();

			// 更新内容をマージ
			function.setFunctionName(req.function.getFunctionName());
			function.setFunctionContents(req.function.getFunctionContents());
			function.setFunctionLibrary(req.function.getFunctionLibrary());
			function.setSynchronousFunctionFlag(req.function.getSynchronousFunctionFlag());
			function.setDescription(req.function.getDescription());
			function.setDeleteFlag(req.function.getDeleteFlag());

			UpdateWfmFunctionInParam inParam = new UpdateWfmFunctionInParam();
			inParam.setWfmFunction(function);
			inParam.setWfUserRole(sessionHolder.getWfUserRole());
			UpdateWfmFunctionOutParam outParam = wf.updateWfmFunction(inParam);

			// アクション機能を再取得
			res.function = this.getWfmFunction(outParam.getWfmFunction().getCorporationCode(), outParam.getWfmFunction().getFunctionCode());

			// 削除区分の選択肢
			res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.function));
			res.success = true;
		}
		return res;
	}

	/** バリデーション */
	private String validate(Mm0081InsertRequest req) {
		if (isNotEmpty(req.function.getFunctionContents())) {
			String functionContents = req.function.getFunctionContents();
			String fieldName = i18n.getText(MessageCd.functionContents);
			try {
				// インターフェースICallbackFunctionを実装しているか
				final Class<?> clazz = Class.forName(functionContents);
				if (!CallbackFunctionIF.class.isAssignableFrom(clazz)) {
					return i18n.getText(MessageCd.MSG0143, fieldName, functionContents, CallbackFunctionIF.class.getName());
				}
			} catch (ClassNotFoundException e) {
				// クラスが存在しない
				return i18n.getText(MessageCd.MSG0142, fieldName, functionContents);
			}
		}
		return null;
	}

	@Transactional
	public Mm0081Response delete(Mm0081InsertRequest req) {
		final Mm0081Response res = createResponse(Mm0081Response.class, req);

		DeleteWfmFunctionInParam deleteIn = new DeleteWfmFunctionInParam();
		deleteIn.setWfmFunction(req.function);
		deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmFunction(deleteIn);

		// アクション取得
		SearchWfmFunctionInParam saIn = new SearchWfmFunctionInParam();
		saIn.setCorporationCode(req.function.getCorporationCode());
		saIn.setFunctionCode(req.function.getFunctionCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmFunctionOutParam saOut = wf.searchWfmFunction(saIn);

		res.function = saOut.getFunctions().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.function));
		res.success = true;
		return res;
	}

	private WfmFunction getWfmFunction(String corporationCode, String functionCode) {
		// アクション機能取得
		final SearchWfmFunctionInParam saIn = new SearchWfmFunctionInParam();
		saIn.setCorporationCode(corporationCode);
		saIn.setFunctionCode(functionCode);
		saIn.setWfUserRole(sessionHolder.getWfUserRole());

		final List<WfmFunction> list = wf.searchWfmFunction(saIn).getFunctions();
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
}
