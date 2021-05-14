package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.api.param.input.AppendProcessInformationInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessInformationInParam;
import jp.co.nci.integrated_workflow.api.param.output.AppendProcessInformationOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessMemoInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ブロック：メモ　サービス
 */
@BizLogic
public class Bl0011Service extends BaseService implements CodeMaster, CodeBook {
	@Inject private WfInstanceWrapper wf;

	private static List<String> ProcessAppendedTypes = Arrays.asList(ProcessAppendedType.MEMO);

	/**
	 * プロセスIDに紐付くメモ情報を返す
	 * @param req
	 * @return
	 */
	public Bl0011Response getProcessMemo(Bl0011Request req) {
		final Bl0011Response res = createResponse(Bl0011Response.class, req);
		res.processMemoList = getMemo(req.corporationCode, req.processId);
		res.success = true;
		return res;
	}

	/**
	 * プロセスIDに紐付くメモ情報を返す
	 * @param corporationCode 企業コード
	 * @param processId プロセスID
	 * @return
	 */
	public List<ProcessMemoInfo> getMemo(String corporationCode, Long processId) {
		if (isEmpty(corporationCode) || processId == null)
			return null;

		final GetProcessInformationInParam in = new GetProcessInformationInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessId(processId);
		in.setProcessAppendedTypeList(ProcessAppendedTypes);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		return wf.getProcessInformation(in).getWftProcessAppendedList()
				.stream()
				.map(e -> new ProcessMemoInfo(e))
				.collect(Collectors.toList());
	}

	/**
	 * プロセスIDに紐付くメモ情報を追加
	 * @param req
	 * @return
	 */
	@Transactional
	public Bl0011Response submitProcessMemo(Bl0011Request req) {
		final Bl0011Response res = createResponse(Bl0011Response.class, req);

		String error = validate(req);
		if (isEmpty(error)) {
			final AppendProcessInformationInParam in = new AppendProcessInformationInParam();
			in.setCorporationCode(req.corporationCode);
			in.setProcessId(req.processId);
			in.setMemorandom(req.memo);
			in.setWfUserRole(sessionHolder.getWfUserRole());

			final AppendProcessInformationOutParam out = wf.appendProcessInformation(in);
			if (!eq(ReturnCode.SUCCESS, out.getReturnCode()))
				throw new WfException(out);

			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.memo));
			res.processMemoList = getMemo(req.corporationCode, req.processId);
			res.success = true;
		} else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	private String validate(Bl0011Request req) {
		if (isEmpty(req.memo))
			return i18n.getText(MessageCd.MSG0001, MessageCd.memo);

		return null;
	}


}
