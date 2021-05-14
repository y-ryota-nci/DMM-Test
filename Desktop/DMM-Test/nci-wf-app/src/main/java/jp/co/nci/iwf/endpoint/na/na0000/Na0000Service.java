package jp.co.nci.iwf.endpoint.na.na0000;

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
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.endpoint.na.NaCodeBook;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;

/**
 * 新規申請フォルダ設定画面サービス
 */
@BizLogic
public class Na0000Service extends MmBaseService<Object> implements NaCodeBook {

	@Inject
	private Na0000Repository repository;
	@Inject
	private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Na0000InitResponse init(Na0000InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		final Na0000InitResponse res = createResponse(Na0000InitResponse.class, req);

		List<MwmScreenProcessLevel> screenProcessLevels = repository.getScreenProcessLevels(corporationCode, localeCode);
		res.treeItems = screenProcessLevels
				.stream()
				.map(l -> new Na0000TreeItem(l))
				.collect(Collectors.toList());
		res.treeItems.add(0, new Na0000TreeItem(i18n.getText(MessageCd.MSG0206)));

		Map<Long, MwmScreenProcessLevel> mapLevels = screenProcessLevels.stream()
				.collect(Collectors.toMap(MwmScreenProcessLevel::getScreenProcessLevelId, cd -> cd));

		List<MwmScreenProcessDef> screenProcessDefs = repository.getScreenProcessDefs(corporationCode, localeCode);
		screenProcessDefs.stream().forEach(e -> {
			// フォルダが指定されていても、存在しないフォルダかもしれぬ（過去バージョンでは、画面定義アップロードでフォルダ構造が不整合となる可能性があった...）
			if (e.getScreenProcessLevelId() != null
					&& mapLevels.containsKey(e.getScreenProcessLevelId())) {
				// フォルダが存在するなら、そのフォルダ配下におく。
				String parent = mapLevels.get(e.getScreenProcessLevelId()).getLevelCode();
				res.treeItems.add(new Na0000TreeItem(e, parent));
			}
			else {
				res.treeItems.add(new Na0000TreeItem(e, null));
			}
		});
		res.success = true;
		return res;
	}

	@Transactional
	public Na0000SaveResponse save(Na0000SaveRequest req) {
		final Na0000SaveResponse res = createResponse(Na0000SaveResponse.class, req);

		if (isEmpty(req.screenProcessLevelId) && isEmpty(req.screenProcessId)) {
			res.treeItem = insert(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.folderInfo));
		} else if (isNotEmpty(req.screenProcessLevelId) && isNotEmpty(req.levelName)) {
			res.treeItem = update(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.folderInfo));
		} else if (isNotEmpty(req.screenProcessLevelId)) {
			delete(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.folderInfo));
		} else {
			res.treeItem = detach(req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.associateWithFolder));
		}

		res.success = true;
		return res;
	}

	private Na0000TreeItem insert(Na0000SaveRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final MwmScreenProcessLevel spl = new MwmScreenProcessLevel();
		spl.setCorporationCode(corporationCode);
		spl.setParentLevelCode(req.parentLevelCode);
		spl.setLevelName(req.levelName);
		spl.setExpansionFlag(req.expansionFlag);
		spl.setSortOrder(req.sortOrder);
		spl.setDeleteFlag(DeleteFlag.OFF);
		repository.insert(spl);
		multi.save("MWM_SCREEN_PROCESS_LEVEL", spl.getScreenProcessLevelId(), "LEVEL_NAME", spl.getLevelName());
		return new Na0000TreeItem(spl);
	}

	private Na0000TreeItem update(Na0000SaveRequest req) {
		final MwmScreenProcessLevel spl = repository.getLevel(req.screenProcessLevelId);
		if (spl == null) {
			throw new NotFoundException("画面プロセス定義階層情報が取得できませんでした -> screenProcessLevelId=" + req.screenProcessLevelId);
		}
		spl.setLevelName(req.levelName);
		spl.setExpansionFlag(req.expansionFlag);
		spl.setSortOrder(req.sortOrder);
		repository.update(spl);
		multi.save("MWM_SCREEN_PROCESS_LEVEL", spl.getScreenProcessLevelId(), "LEVEL_NAME", spl.getLevelName());
		return new Na0000TreeItem(spl);
	}

	private void delete(Na0000SaveRequest req) {
		final MwmScreenProcessLevel spl = repository.getLevel(req.screenProcessLevelId);
		if (spl == null) {
			throw new NotFoundException("画面プロセス定義階層情報が取得できませんでした -> screenProcessLevelId=" + req.screenProcessLevelId);
		}
		repository.delete(spl);
		multi.physicalDelete("MWM_SCREEN_PROCESS_LEVEL", spl.getScreenProcessLevelId());
	}

	private Na0000TreeItem detach(Na0000SaveRequest req) {
		final MwmScreenProcessDef spd = repository.getDef(req.screenProcessId);
		if (spd == null) {
			throw new NotFoundException("画面プロセス定義情報が取得できませんでした -> screenProcessId=" + req.screenProcessId);
		}
		spd.setScreenProcessLevelId(null);
		repository.update(spd);
		return new Na0000TreeItem(spd, null);
	}

	@Transactional
	public Na0000MoveResponse move(Na0000MoveRequest req) {
		final Na0000MoveResponse res = createResponse(Na0000MoveResponse.class, req);
		req.levels.stream().forEach(l -> {
			final MwmScreenProcessLevel spl = repository.getLevel(l.getScreenProcessLevelId());
			if (spl == null) {
				throw new NotFoundException("画面プロセス定義階層情報が取得できませんでした -> screenProcessLevelId=" + l.getScreenProcessLevelId());
			}
			spl.setParentLevelCode(l.getParentLevelCode());
			spl.setSortOrder(l.getSortOrder());
			repository.update(spl);
		});
		req.defs.stream().forEach(d -> {
			final MwmScreenProcessDef spd = repository.getDef(d.getScreenProcessId());
			if (spd == null) {
				throw new NotFoundException("画面プロセス定義情報が取得できませんでした -> screenProcessId=" + d.getScreenProcessId());
			}
			spd.setScreenProcessLevelId(d.getScreenProcessLevelId());
			spd.setSortOrder(d.getSortOrder());
			repository.update(spd);
		});
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.folderScreenProcessDef));

		res.success = true;
		return res;
	}

	@Transactional
	public Na0000AssociateResponse associate(Na0000AssociateRequest req) {
		final Na0000AssociateResponse res = createResponse(Na0000AssociateResponse.class, req);
		res.treeItems = new ArrayList<>();
		req.defs.stream().forEach(d -> {
			final MwmScreenProcessDef spd = repository.getDef(d.getScreenProcessId());
			if (spd == null) {
				throw new NotFoundException("画面プロセス定義情報が取得できませんでした -> screenProcessId=" + d.getScreenProcessId());
			}
			spd.setScreenProcessLevelId(d.getScreenProcessLevelId());
			repository.update(spd);
			res.treeItems.add(new Na0000TreeItem(spd, req.parentLevelCode));
		});
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.associateWithFolder));
		res.success = true;
		return res;
	}

}
