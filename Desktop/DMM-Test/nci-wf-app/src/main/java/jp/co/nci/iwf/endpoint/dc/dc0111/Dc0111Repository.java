package jp.co.nci.iwf.endpoint.dc.dc0111;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;

/**
 * 画面文書定義設定のリポジトリ
 */
@ApplicationScoped
public class Dc0111Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/**
	 * 画面一覧を抽出
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<MwvScreen> getScreens(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwvScreen.class, getSql("DC0111_01"), params);

	}

	/**
	 * 画面プロセス定義一覧を抽出
	 * @param corporationCode 企業コード
	 * @param screenId 画面ID
	 * @return
	 */
	public List<MwvScreenProcessDef> getScreenProcessDefs(Long screenId, String corporationCode, String localeCode) {
		final Object[] params = { localeCode, screenId, corporationCode };
		return select(MwvScreenProcessDef.class, getSql("DC0111_03"), params);
	}

	/**
	 * 画面文書定義をユニークキーで抽出
	 * @param screenDocId
	 * @param localeCode
	 * @return
	 */
	public MwvScreenDocDef get(Long screenDocId, String localeCode) {
		final Object[] params = { localeCode, screenDocId };
		return select(MwvScreenDocDef.class, getSql("DC0111_02"), params)
				.stream()
				.findFirst()
				.orElse(null);
	}

	/**
	 * 画面文書定義をユニークキーで抽出
	 * @param screenDocId
	 * @return
	 */
	public MwmScreenDocDef get(long screenDocId) {
		return em.find(MwmScreenDocDef.class, screenDocId);
	}

	/**
	 * 画面が存在するか
	 * @param screenId
	 * @return
	 */
	public boolean existScreen(Long screenId) {
		return em.find(MwmScreen.class, screenId) != null;
	}

	/**
	 * 画面文書定義が存在するか
	 * @param screenDocId 画面文書ID
	 * @param corporationCode 企業コード
	 * @param screenDocCode 画面文書定義コード
	 * @return
	 */
	public boolean existScreenDocDef(Long screenDocId, String corporationCode, String screenDocCode) {
		final Object[] params = { corporationCode, screenDocCode };
		final MwmScreenDocDef entity = select(MwmScreenDocDef.class, getSql("DC0111_04"), params).stream().findFirst().orElse(null);
		return (entity != null && !eq(screenDocId, entity.getScreenDocId()));
	}

	/** インサート */
	public long insert(MwmScreenDocDef input, String corporationCode) {
		long screenDocId = numbering.newPK(MwmScreenDocDef.class);
		input.setScreenDocId(screenDocId);
		input.setCorporationCode(corporationCode);
		input.setDeleteFlag(DeleteFlag.OFF);
		em.persist(input);

		return screenDocId;
	}

	/** 更新 */
	public long update(MwmScreenDocDef input, MwmScreenDocDef org) {
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setDescription(input.getDescription());
		org.setScreenId(input.getScreenId());
		org.setScreenDocName(input.getScreenDocName());
		org.setFolderCode(input.getFolderCode());
		org.setScreenProcessCode(input.getScreenProcessCode());
		org.setValidEndDate(input.getValidEndDate());
		org.setValidStartDate(input.getValidStartDate());
		org.setVersion(input.getVersion());
		em.merge(org);
		return org.getScreenDocId();
	}

}
