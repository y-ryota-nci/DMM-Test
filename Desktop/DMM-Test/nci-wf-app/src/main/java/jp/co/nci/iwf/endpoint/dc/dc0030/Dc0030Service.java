package jp.co.nci.iwf.endpoint.dc.dc0030;

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
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocLevel;

/**
 * 新規申請画面サービス
 */
@BizLogic
public class Dc0030Service extends BaseService implements CodeMaster, NaCodeBook {

	@Inject
	private Dc0030Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0030InitResponse init(Dc0030InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		final Dc0030InitResponse res = createResponse(Dc0030InitResponse.class, req);

		List<MwmScreenDocLevel> screenDocLevels = repository.getScreenDocLevels(corporationCode, localeCode);
		Map<Long, MwmScreenDocLevel> mapLevels = screenDocLevels.stream()
				.collect(Collectors.toMap(MwmScreenDocLevel::getScreenDocLevelId, cd -> cd));

		Set<String> menuRoleCds = sessionHolder.getLoginInfo().getMenuRoleCodes();
		List<MwmScreenDocDef> screenDocDefs = repository.getScreenDocDefs(corporationCode, menuRoleCds, localeCode);

		List<Dc0030TreeItem> treeItems = new ArrayList<>();

		screenDocDefs.stream().forEach(e -> {
			// フォルダが指定されていても、存在しないフォルダかもしれぬ（過去バージョンでは、画面定義アップロードでフォルダ構造が不整合となる可能性があった...）
			if (e.getScreenDocLevelId() != null
					&& mapLevels.containsKey(e.getScreenDocLevelId())) {
				// フォルダが存在するなら、そのフォルダ配下におく。
				String parent = mapLevels.get(e.getScreenDocLevelId()).getLevelCode();
				treeItems.add(new Dc0030TreeItem(e, parent));
			}
			else {
				treeItems.add(new Dc0030TreeItem(e, null));
			}
		});

		int maxLevel = repository.getMaxLevel(corporationCode, localeCode);
		createScreenDocLevels(screenDocLevels, treeItems, 0, maxLevel);

		res.treeItems = treeItems;
		res.success = true;
		return res;
	}

	private void createScreenDocLevels(List<MwmScreenDocLevel> screenDocLevels, List<Dc0030TreeItem> treeItems, int level, int maxLevel) {
		if (isEmpty(screenDocLevels) || level > maxLevel) {
			return;
		}
		for (Iterator<MwmScreenDocLevel> iter = screenDocLevels.listIterator(); iter.hasNext();) {
			MwmScreenDocLevel e = iter.next();
			for (Dc0030TreeItem i : treeItems) {
				if (i.parent.equals(e.getLevelCode())) {
					treeItems.add(new Dc0030TreeItem(e));
					iter.remove();
					break;
				}
			}
		}
		createScreenDocLevels(screenDocLevels, treeItems, level + 1, maxLevel);
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
