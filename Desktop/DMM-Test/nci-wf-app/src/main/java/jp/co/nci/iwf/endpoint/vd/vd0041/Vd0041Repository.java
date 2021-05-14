package jp.co.nci.iwf.endpoint.vd.vd0041;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;

/**
 * 画面プロセス定義設定のリポジトリ
 */
@ApplicationScoped
public class Vd0041Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/**
	 * 画面一覧を抽出
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<MwvScreen> getScreens(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwvScreen.class, getSql("VD0041_01"), params);

	}

	/**
	 * 画面プロセス定義をユニークキーで抽出
	 * @param screenProcessId
	 * @param localeCode
	 * @return
	 */
	public MwvScreenProcessDef get(Long screenProcessId, String localeCode) {
		final Object[] params = { localeCode, screenProcessId };
		return select(MwvScreenProcessDef.class, getSql("VD0041_02"), params)
				.stream()
				.findFirst()
				.orElse(null);
	}

	/**
	 * 画面プロセス定義をユニークキーで抽出
	 * @param screenProcessId
	 * @return
	 */
	public MwmScreenProcessDef get(long screenProcessId) {
		return em.find(MwmScreenProcessDef.class, screenProcessId);
	}

	/**
	 * 画面が存在するか
	 * @param screenId
	 * @return
	 */
	public boolean existScreen(Long screenId) {
		return em.find(MwmScreen.class, screenId) != null;
	}

	/** インサート */
	public long insert(MwmScreenProcessDef input, String corporationCode) {
		long screenProcessId = numbering.newPK(MwmScreenProcessDef.class);
		input.setScreenProcessId(screenProcessId);
		input.setScreenProcessCode(String.format("%010d", screenProcessId));
		input.setCorporationCode(corporationCode);
		input.setDeleteFlag(DeleteFlag.OFF);
		em.persist(input);

		return screenProcessId;
	}

	/** 更新 */
	public long update(MwmScreenProcessDef input, MwmScreenProcessDef org) {
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setDescription(input.getDescription());
		org.setProcessDefCode(input.getProcessDefCode());
		org.setProcessDefDetailCode(input.getProcessDefDetailCode());
		org.setScreenId(input.getScreenId());
		org.setScreenProcessName(input.getScreenProcessName());
		org.setValidEndDate(input.getValidEndDate());
		org.setValidStartDate(input.getValidStartDate());
		org.setVersion(input.getVersion());
		em.merge(org);
		return org.getScreenProcessId();
	}

	/** 画面プロセス定義でプロセス定義＋画面の組み合わせが存在するか */
	public boolean existScreenProcess(MwmScreenProcessDef entity) {
		final List<Object> params = new ArrayList<>();
		params.add(entity.getCorporationCode());
		params.add(entity.getProcessDefCode());
		params.add(entity.getProcessDefDetailCode());
		params.add(entity.getScreenId());
		params.add(entity.getScreenProcessId());

		return count(getSql("VD0041_03"), params.toArray()) > 0;
	}


}
