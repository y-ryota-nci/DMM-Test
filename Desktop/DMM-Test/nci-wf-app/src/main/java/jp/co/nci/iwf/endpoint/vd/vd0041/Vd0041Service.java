package jp.co.nci.iwf.endpoint.vd.vd0041;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmProcessDefOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;

/**
 * 画面プロセス定義設定のサービス
 */
@BizLogic
public class Vd0041Service extends BaseService {
	/** IWF API */
	@Inject private WfInstanceWrapper wf;
	/** 画面プロセス定義設定のリポジトリ */
	@Inject private Vd0041Repository repository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @return
	 */
	public Vd0041InitResponse init(Vd0041InitRequest req) {
		final Vd0041InitResponse res = createResponse(Vd0041InitResponse.class, req);
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 画面プロセス定義の抽出
		if (req.screenProcessId == null) {
			res.entity = new MwvScreenProcessDef();
			res.entity.corporationCode = corporationCode;
			res.entity.validStartDate = today();
			res.entity.validEndDate = ENDDATE;
		}
		else {
			res.entity = repository.get(req.screenProcessId, localeCode);

			// 排他判定
			if (!eq(res.entity.version, req.version))
				throw new AlreadyUpdatedException();
		}

		// プロセス定義の選択肢
		res.processDefCodes = createProcessDefCodes(corporationCode);
		// プロセス定義明細の選択肢
		res.processDefDetailCodes = createProcessDefDetailCodes(corporationCode, res.entity.processDefCode);
		// 画面の選択肢
		res.screenIds = createScreens(corporationCode, localeCode);

		res.success = true;
		return res;
	}

	/** 画面の選択肢を生成 */
	private List<OptionItem> createScreens(String corporationCode, String localeCode) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll( repository.getScreens(corporationCode, localeCode)
				.stream()
				.map(s -> new OptionItem(s.screenId, s.screenCode + " " + s.screenName))
				.collect(Collectors.toList()));
		return items;
	}

	/** プロセス定義の選択肢生成 */
	private List<OptionItem> createProcessDefCodes(String corporationCode) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		// 最新の枝番のプロセス定義一覧を求める
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "PD." + WfmProcessDef.CORPORATION_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_NAME),
		});

		final SearchWfmProcessDefOutParam out = wf.searchWfmProcessDef(in);

		final Set<String> uniques = new HashSet<>();
		out.getProcessDefs().forEach(pd -> {
			String value = pd.getProcessDefCode();
			if (!uniques.contains(value)) {
				String label = pd.getProcessDefCode() + " " + pd.getProcessDefName();
				items.add( new OptionItem(value, label));
				uniques.add(value);
			}
		});
		return items;
	}

	private List<OptionItem> createProcessDefDetailCodes(String corporationCode, String processDefCode) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		if (isNotEmpty(processDefCode)) {
			final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
			in.setCorporationCode(corporationCode);
			in.setProcessDefCode(processDefCode);
			in.setDeleteFlag(DeleteFlag.OFF);
			in.setOrderBy(new OrderBy[] {
					new OrderBy(true, "PD." + WfmProcessDef.CORPORATION_CODE),
					new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_CODE),
					new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_DETAIL_CODE),
			});

			items.addAll(wf.searchWfmProcessDef(in).getProcessDefs()
					.stream()
					.map(pd -> new OptionItem(pd.getProcessDefDetailCode(), pd.getProcessDefDetailCode()))
					.collect(Collectors.toList()));
		}
		return items;
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Vd0041SaveRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final BaseResponse res = createResponse(BaseResponse.class, req);

		// エラー
		List<String> errors = validate(req);
		if (!errors.isEmpty()) {
			res.addAlerts(errors.toArray(new String[errors.size()]));
			res.success = false;
		}
		else {
			// 差分更新
			MwmScreenProcessDef input = req.entity;
			MwmScreenProcessDef org = repository.get(input.getScreenProcessId());
			long screenProcessId;
			if (org == null)
				screenProcessId = repository.insert(input, corporationCode);
			else
				screenProcessId = repository.update(input, org);

			multi.save("MWM_SCREEN_PROCESS_DEF", screenProcessId, "SCREEN_PROCESS_NAME", input.getScreenProcessName());
			multi.save("MWM_SCREEN_PROCESS_DEF", screenProcessId, "DESCRIPTION", input.getDescription());

			res.success = true;
		}

		return res;
	}

	/** バリデーション */
	private List<String> validate(Vd0041SaveRequest req) {
		final String corporationCode = req.entity.getCorporationCode();
		final String processDefCode = req.entity.getProcessDefCode();
		final String processDefDetailCode = req.entity.getProcessDefDetailCode();
		final List<String> errors = new ArrayList<>();

		// プロセス定義が設定されていること
		if (isEmpty(corporationCode) || isEmpty(processDefCode) || isEmpty(processDefDetailCode))
			errors.add(i18n.getText(MessageCd.MSG0001, MessageCd.processDef));

		// プロセス定義が存在すること
		if (!existProcessDef(corporationCode, processDefCode, processDefDetailCode))
			errors.add(i18n.getText(MessageCd.MSG0129, MessageCd.processDef));

		// 画面が設定されていること
		final Long screenId = req.entity.getScreenId();
		if (screenId == null)
			errors.add(i18n.getText(MessageCd.MSG0001, MessageCd.screen));

		// 画面が存在すること
		if (!repository.existScreen(screenId))
			errors.add(i18n.getText(MessageCd.MSG0129, MessageCd.screen));

		// 既存のプロセス定義＋画面の組み合わせがないこと
		if (repository.existScreenProcess(req.entity)) {
			String args = i18n.getText(MessageCd.screen) + " + " + i18n.getText(MessageCd.processDef);
			errors.add(i18n.getText(MessageCd.MSG0132, args));
		}
		return errors;
	}

	/** プロセス定義が存在するか */
	private boolean existProcessDef(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		return wf.searchWfmProcessDef(in).getProcessDefs().size() > 0;
	}

	/**
	 * プロセス定義コードに対するプロセス定義明細コードの選択肢を抽出
	 * @param req
	 * @return
	 */
	public Vd0041DetailResponse getProcessDefDetails(Vd0041DetailRequest req) {
		final Vd0041DetailResponse res = createResponse(Vd0041DetailResponse.class, req);
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		res.processDefDetailCodes = createProcessDefDetailCodes(corporationCode, req.processDefCode);
		res.success = true;
		return res;
	}
}
