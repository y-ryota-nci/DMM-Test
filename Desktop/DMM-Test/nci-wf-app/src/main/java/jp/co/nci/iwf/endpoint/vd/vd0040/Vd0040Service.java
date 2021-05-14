package jp.co.nci.iwf.endpoint.vd.vd0040;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmProcessDefOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面プロセス定義一覧のサービス
 */
@BizLogic
public class Vd0040Service extends BasePagingService {
	/** 画面プロセス定義一覧のリポジトリ */
	@Inject private Vd0040Repository repository;
	/** 企業サービス */
	@Inject private CorporationService corp;
	/** IWF API */
	@Inject private WfInstanceWrapper wf;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;
	/** 画面Javascriptサービス */
	@Inject private JavascriptService jsService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0040InitResponse init(BaseRequest req) {
		final Vd0040InitResponse res = createResponse(Vd0040InitResponse.class, req);

		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		// プロセス定義の選択肢
		res.processDefs = createProcessDefs();

		res.success = true;
		return res;
	}

	/** プロセス定義の選択肢生成 */
	private List<OptionItem> createProcessDefs() {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		// 最新の枝番のプロセス定義一覧を求める
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "PD." + WfmProcessDef.CORPORATION_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_NAME),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_DETAIL_CODE),
		});

		final SearchWfmProcessDefOutParam out = wf.searchWfmProcessDef(in);

		final Set<String> uniques = new HashSet<>();
		out.getProcessDefs().forEach(pd -> {
			String value = pd.getProcessDefCode();
			if (!uniques.contains(value)) {
				String label = pd.getProcessDefName();
				items.add( new OptionItem(value, label));
				uniques.add(value);
			}
		});

		return items;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Vd0040SearchResponse search(Vd0040SearchRequest req) {
		final int allCount = repository.count(req);
		final Vd0040SearchResponse res = createResponse(Vd0040SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Vd0040DeleteRequest req) {
		@SuppressWarnings("unused")
		int count = 0;
		for (Long screenProcessId : req.screenProcessIds) {
			count += repository.delete(screenProcessId);
			multi.physicalDelete("MWM_SCREEN_PROCESS_DEF", screenProcessId);

			// 画面に紐付くJavascriptキャッシュをクリア
			jsService.clear();
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.screenProcessInfo));
		res.success = true;
		return res;
	}

}
