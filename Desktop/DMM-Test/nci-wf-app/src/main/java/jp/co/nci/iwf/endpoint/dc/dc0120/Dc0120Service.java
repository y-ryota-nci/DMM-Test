package jp.co.nci.iwf.endpoint.dc.dc0120;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocLevel;

/**
 * 業務文書メニュー編集画面サービス
 */
@BizLogic
public class Dc0120Service extends BaseService implements DcCodeBook {

	@Inject
	private Dc0120Repository repository;
	@Inject
	private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0120InitResponse init(Dc0120InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		final Dc0120InitResponse res = createResponse(Dc0120InitResponse.class, req);

		List<MwmScreenDocLevel> screenDocLevels = repository.getScreenDocLevels(corporationCode, localeCode);
		res.treeItems = screenDocLevels
				.stream()
				.map(l -> new Dc0120TreeItem(l))
				.collect(Collectors.toList());
		res.treeItems.add(0, new Dc0120TreeItem(i18n.getText(MessageCd.MSG0206)));

		Map<Long, MwmScreenDocLevel> mapLevels = screenDocLevels.stream()
				.collect(Collectors.toMap(MwmScreenDocLevel::getScreenDocLevelId, cd -> cd));

		List<MwmScreenDocDef> screenDocDefs = repository.getScreenDocDefs(corporationCode, localeCode);
		screenDocDefs.stream().forEach(e -> {
			// フォルダが指定されていても、存在しないフォルダかもしれぬ（過去バージョンでは、画面定義アップロードでフォルダ構造が不整合となる可能性があった...）
			if (e.getScreenDocLevelId() != null
					&& mapLevels.containsKey(e.getScreenDocLevelId())) {
				// フォルダが存在するなら、そのフォルダ配下におく。
				String parent = mapLevels.get(e.getScreenDocLevelId()).getLevelCode();
				res.treeItems.add(new Dc0120TreeItem(e, parent));
			}
			else {
				res.treeItems.add(new Dc0120TreeItem(e, null));
			}
		});
		res.success = true;
		return res;
	}

	@Transactional
	public Dc0120SaveResponse save(Dc0120SaveRequest req) {
		final Dc0120SaveResponse res = createResponse(Dc0120SaveResponse.class, req);

		if (isEmpty(req.screenDocLevelId) && isEmpty(req.screenDocId)) {
			res.treeItem = insert(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.folderInfo));
		} else if (isNotEmpty(req.screenDocLevelId) && isNotEmpty(req.levelName)) {
			res.treeItem = update(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.folderInfo));
		} else if (isNotEmpty(req.screenDocLevelId)) {
			delete(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.folderInfo));
		} else {
			res.treeItem = detach(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.associateWithFolder));
		}

		res.success = true;
		return res;
	}

	private Dc0120TreeItem insert(Dc0120SaveRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final MwmScreenDocLevel spl = new MwmScreenDocLevel();
		spl.setCorporationCode(corporationCode);
		spl.setParentLevelCode(req.parentLevelCode);
		spl.setLevelName(req.levelName);
		spl.setExpansionFlag(req.expansionFlag);
		spl.setSortOrder(req.sortOrder);
		spl.setDeleteFlag(DeleteFlag.OFF);
		repository.insert(spl);
		multi.save("MWM_SCREEN_DOC_LEVEL", spl.getScreenDocLevelId(), "LEVEL_NAME", spl.getLevelName());
		return new Dc0120TreeItem(spl);
	}

	private Dc0120TreeItem update(Dc0120SaveRequest req) {
		final MwmScreenDocLevel spl = repository.getLevel(req.screenDocLevelId);
		if (spl == null) {
			throw new NotFoundException("画面文書定義階層情報が取得できませんでした -> screenDocLevelId=" + req.screenDocLevelId);
		}
		spl.setLevelName(req.levelName);
		spl.setExpansionFlag(req.expansionFlag);
		spl.setSortOrder(req.sortOrder);
		repository.update(spl);
		multi.save("MWM_SCREEN_DOC_LEVEL", spl.getScreenDocLevelId(), "LEVEL_NAME", spl.getLevelName());
		return new Dc0120TreeItem(spl);
	}

	private void delete(Dc0120SaveRequest req) {
		final MwmScreenDocLevel spl = repository.getLevel(req.screenDocLevelId);
		if (spl == null) {
			throw new NotFoundException("画面文書定義階層情報が取得できませんでした -> screenDocLevelId=" + req.screenDocLevelId);
		}
		repository.delete(spl);
		multi.physicalDelete("MWM_SCREEN_DOC_LEVEL", spl.getScreenDocLevelId());
	}

	private Dc0120TreeItem detach(Dc0120SaveRequest req) {
		final MwmScreenDocDef spd = repository.getDef(req.screenDocId);
		if (spd == null) {
			throw new NotFoundException("画面文書定義情報が取得できませんでした -> screenDocId=" + req.screenDocId);
		}
		spd.setScreenDocLevelId(null);
		repository.update(spd);
		return new Dc0120TreeItem(spd, null);
	}

	@Transactional
	public Dc0120MoveResponse move(Dc0120MoveRequest req) {
		final Dc0120MoveResponse res = createResponse(Dc0120MoveResponse.class, req);
		req.levels.stream().forEach(l -> {
			final MwmScreenDocLevel spl = repository.getLevel(l.getScreenDocLevelId());
			if (spl == null) {
				throw new NotFoundException("画面文書定義階層情報が取得できませんでした -> screenDocLevelId=" + l.getScreenDocLevelId());
			}
			spl.setParentLevelCode(l.getParentLevelCode());
			spl.setSortOrder(l.getSortOrder());
			repository.update(spl);
		});
		req.defs.stream().forEach(d -> {
			final MwmScreenDocDef spd = repository.getDef(d.getScreenDocId());
			if (spd == null) {
				throw new NotFoundException("画面文書定義情報が取得できませんでした -> screenDocId=" + d.getScreenDocId());
			}
			spd.setScreenDocLevelId(d.getScreenDocLevelId());
			spd.setSortOrder(d.getSortOrder());
			repository.update(spd);
		});
//		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.folderScreenDocDef));

		res.success = true;
		return res;
	}

	@Transactional
	public Dc0120AssociateResponse associate(Dc0120AssociateRequest req) {
		final Dc0120AssociateResponse res = createResponse(Dc0120AssociateResponse.class, req);
		res.treeItems = new ArrayList<>();
		req.defs.stream().forEach(d -> {
			final MwmScreenDocDef spd = repository.getDef(d.getScreenDocId());
			if (spd == null) {
				throw new NotFoundException("画面文書定義情報が取得できませんでした -> screenDocId=" + d.getScreenDocId());
			}
			spd.setScreenDocLevelId(d.getScreenDocLevelId());
			repository.update(spd);
			res.treeItems.add(new Dc0120TreeItem(spd, req.parentLevelCode));
		});
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.associateWithFolder));
		res.success = true;
		return res;
	}

}
