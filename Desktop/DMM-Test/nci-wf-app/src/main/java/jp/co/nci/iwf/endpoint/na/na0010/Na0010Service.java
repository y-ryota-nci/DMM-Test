package jp.co.nci.iwf.endpoint.na.na0010;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.na.NaCodeBook;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;

/**
 * 新規申請画面サービス
 */
@BizLogic
public class Na0010Service extends BaseService implements CodeMaster, NaCodeBook {

	@Inject
	private Na0010Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Na0010InitResponse init(Na0010InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		final Na0010InitResponse res = createResponse(Na0010InitResponse.class, req);

		List<MwmScreenProcessLevel> screenProcessLevels = repository.getScreenProcessLevels(corporationCode, localeCode);
		Map<Long, MwmScreenProcessLevel> mapLevels = screenProcessLevels.stream()
				.collect(Collectors.toMap(MwmScreenProcessLevel::getScreenProcessLevelId, cd -> cd));

		Set<String> menuRoleCds = sessionHolder.getLoginInfo().getMenuRoleCodes();
		List<MwmScreenProcessDef> screenProcessDefs = repository.getScreenProcessDefs(corporationCode, menuRoleCds, localeCode);

		List<Na0010TreeItem> treeItems = new ArrayList<>();

		screenProcessDefs.stream().forEach(e -> {
			// フォルダが指定されていても、存在しないフォルダかもしれぬ（過去バージョンでは、画面定義アップロードでフォルダ構造が不整合となる可能性があった...）
			if (e.getScreenProcessLevelId() != null
					&& mapLevels.containsKey(e.getScreenProcessLevelId())) {
				// フォルダが存在するなら、そのフォルダ配下におく。
				String parent = mapLevels.get(e.getScreenProcessLevelId()).getLevelCode();
				treeItems.add(new Na0010TreeItem(e, parent));
			}
			else {
				treeItems.add(new Na0010TreeItem(e, null));
			}
		});

		int maxLevel = repository.getMaxLevel(corporationCode, localeCode);
		createScreenProcessLevels(screenProcessLevels, treeItems, 0, maxLevel);

		res.treeItems = treeItems;
		res.success = true;
		return res;
	}

	private void createScreenProcessLevels(List<MwmScreenProcessLevel> screenProcessLevels, List<Na0010TreeItem> treeItems, int level, int maxLevel) {
		if (isEmpty(screenProcessLevels) || level > maxLevel) {
			return;
		}
		for (Iterator<MwmScreenProcessLevel> iter = screenProcessLevels.listIterator(); iter.hasNext();) {
			MwmScreenProcessLevel e = iter.next();
			for (Na0010TreeItem i : treeItems) {
				if (i.parent.equals(e.getLevelCode())) {
					treeItems.add(new Na0010TreeItem(e));
					iter.remove();
					break;
				}
			}
		}
		createScreenProcessLevels(screenProcessLevels, treeItems, level + 1, maxLevel);
	}

//	public List<Na0010TreeItem> getScreenProcessDef(Long screenProcessLevelId) {
//		String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
//		Set<String> menuRoleCds = sessionHolder.getLoginInfo().getMenuRoleCodes();
//		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
//		List<Na0010TreeItem> treeItem = repository.getScreenProcessDefs(corporationCode, screenProcessLevelId, menuRoleCds, localeCode)
//				.stream()
//				.map(d -> new Na0010TreeItem(d))
//				.collect(Collectors.toList());
//		return treeItem;
//	}
}
