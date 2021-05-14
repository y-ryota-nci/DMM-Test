package jp.co.nci.iwf.endpoint.vd.vd0091;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessMenu;

/**
 * 新規申請メニュー割当設定リポジトリ
 */
@ApplicationScoped
public class Vd0091Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** 新規申請メニュー割当設定（画面プロセス／メニュー連携IDなし） */
	public MwvScreenProcessMenu get(Long menuId, String corporationCode, String localeCode) {
		final Object[] params = { menuId, corporationCode, localeCode };
		return selectOne(MwvScreenProcessMenu.class, getSql("VD0091_01"), params);
	}

	/** 新規申請メニュー割当設定（画面プロセス／メニュー連携IDあり） */
	public MwvScreenProcessMenu get(Long screenProcessMenuId, String localeCode) {
		final Object[] params = { screenProcessMenuId, localeCode };
		return selectOne(MwvScreenProcessMenu.class, getSql("VD0091_02"), params);
	}

	/** 画面プロセス定義一覧 */
	public List<MwvScreenProcessDef> getScreenProcessDefs(String corporationCode, String localeCode) {
		final Object[] params = { corporationCode, localeCode };
		return select(MwvScreenProcessDef.class, getSql("VD0091_03"), params);
	}

	/** 画面プロセス／メニュー連携マスタ抽出 */
	public MwmScreenProcessMenu get(Long screenProcessMenuId) {
		if (screenProcessMenuId == null)
			return null;
		return em.find(MwmScreenProcessMenu.class, screenProcessMenuId);
	}

	/** インサート */
	public long insert(MwvScreenProcessMenu entity) {
		long screenProcessMenuId = numbering.newPK(MwmScreenProcessMenu.class);
		MwmScreenProcessMenu e = new MwmScreenProcessMenu();
		e.setScreenProcessMenuId(screenProcessMenuId);
		e.setCorporationCode(entity.corporationCode);
		e.setMenuId(entity.menuId);
		e.setScreenProcessId(entity.screenProcessId);
		e.setDeleteFlag(DeleteFlag.OFF);
		em.persist(e);

		return screenProcessMenuId;
	}

	/** 更新 */
	public long update(MwvScreenProcessMenu entity, MwmScreenProcessMenu current) {
		current.setScreenProcessId(entity.screenProcessId);

		return entity.screenProcessMenuId;
	}
}
