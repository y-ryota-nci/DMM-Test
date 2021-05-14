package jp.co.nci.iwf.endpoint.mm;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;

/**
 * マスタメンテナンスのサービス基底クラス
 *
 * @param <E>
 */
public abstract class MmBaseService<E> extends CmBaseService<E> {
	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	protected CorporationService corpService;

	public List<OptionItem> getAccessibleCorporations(boolean emptyLine) {
		return corpService.getMyCorporations(emptyLine);
	}
}
