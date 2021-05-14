package jp.co.nci.iwf.endpoint.mm.mm0060;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBlockDisplay;
import jp.co.nci.iwf.jpa.entity.mw.MwmDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmDefaultBlockDisplay;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;

/**
 * ブロック表示順設定リポジトリ
 */
@ApplicationScoped
public class Mm0060Repository extends BaseRepository implements CodeBook {

	@Inject
	private NumberingService numbering;

	/** 画面プロセス定義一覧を返す */
	public List<MwmScreenProcessDef> getScreenProcesses(String corporationCode, String localeCode) {
		final Object[] params = {localeCode, corporationCode};
		return select(MwmScreenProcessDef.class, getSql("MM0060_01"), params);
	}

	/** 画面プロセス定義IDに紐付くブロック表示条件一覧を返す */
	public List<Mm0060BlockDisplay> getBlockDisplays(String corporationCode, Long screenProcessId) {
		List<Mm0060BlockDisplay> results;
		if (eq(DEFAULT_SCREEN_PROCESS_ID, screenProcessId)) {
			// デフォルトブロック表示条件マスタを抽出
			final Object[] params = { corporationCode };
			results = select(Mm0060BlockDisplay.class, getSql("MM0060_06"), params);
		}
		else {
			// ブロック表示条件マスタを抽出
			final Object[] params = {screenProcessId};
			results = select(Mm0060BlockDisplay.class, getSql("MM0060_02"), params);
		}
		results.forEach(e -> em.detach(e));
		return results;
	}

	/** ルックアップマスタよりブロックリストを返す */
	public List<Mm0060Block> getBlocks(String corporationCode, String localeCode) {
		final Object[] params = {localeCode, corporationCode};
		List<Mm0060Block> entities = select(Mm0060Block.class, getSql("MM0060_03"), params);
		for (Mm0060Block entity : entities)
			em.detach(entity);
		return entities;
	}

	/** 表示条件マスタを返す */
	public List<Mm0060Dc> getDcs(String localeCode) {
		final Object[] params = { localeCode };
		return select(Mm0060Dc.class, getSql("MM0060_04"), params);
	}

	/** 表示条件マスタを返す */
	public List<MwmDc> getMwmDc(String localeCode) {
		final Object[] params = { localeCode };
		return select(MwmDc.class, getSql("MM0060_05"), params);
	}

	/** 表示条件マスタを更新 */
	public void update(Mm0060Dc input, MwmDc dc) {
		dc.setDcName(input.getDcName());
		dc.setDeleteFlag(DeleteFlag.OFF);
		em.merge(dc);
	}

	/** パーツ表示条件を物理削除 */
	public void delete(List<MwmBlockDisplay> removes) {
		em.flush();
		for (MwmBlockDisplay bd : removes) {
			em.remove(bd);
		}
	}

	/** 画面プロセス定義IDに紐付くブロック表示条件一覧を返す */
	public List<MwmBlockDisplay> getMwmBlockDisplay(String corporationCode, Long screenProcessId, String localeCode) {
		final Object[] params = { screenProcessId };
		return select(MwmBlockDisplay.class, getSql("MM0060_02"), params);
	}

	/** パーツ表示条件をインサート */
	public void insert(Long screenProcessId, Mm0060BlockDisplay input) {
		final MwmBlockDisplay bd = new MwmBlockDisplay();
		bd.setBlockDisplayId(numbering.newPK(MwmBlockDisplay.class));
		bd.setDcId(input.getDcId());
		bd.setScreenProcessId(screenProcessId);
		bd.setBlockId(input.getBlockId());
		bd.setDisplayFlag(input.getDisplayFlag());
		bd.setExpansionFlag(input.getExpansionFlag());
		bd.setSortOrder(input.getSortOrder());
		bd.setDeleteFlag(DeleteFlag.OFF);
		em.persist(bd);
	}

	/** パーツ表示条件を更新 */
	public void update(Mm0060BlockDisplay input, MwmBlockDisplay bd) {
		bd.setDisplayFlag(input.getDisplayFlag());
		bd.setExpansionFlag(input.getExpansionFlag());
		bd.setSortOrder(input.getSortOrder());
		em.merge(bd);
	}

	/** デフォルトブロック表示条件マスタを抽出 */
	public List<MwmDefaultBlockDisplay> getDefaultBlockDisplay(String corporationCode) {
		final Object[] params = { corporationCode } ;
		final String sql = getSql("MM0060_07");
		return select(MwmDefaultBlockDisplay.class, sql, params);
	}

	/** デフォルトブロック表示条件マスタをインサート */
	public void insertDefault(Mm0060BlockDisplay input, String corporationCode) {
		final long defaultBlockDisplayId = numbering.newPK(MwmDefaultBlockDisplay.class);
		final MwmDefaultBlockDisplay e = new MwmDefaultBlockDisplay();
		e.setDefaultBlockDisplayId(defaultBlockDisplayId);
		e.setBlockId(input.getBlockId());
		e.setCorporationCode(corporationCode);
		e.setDcId(input.getDcId());
		e.setDeleteFlag(DeleteFlag.OFF);
		e.setDisplayFlag(input.getDisplayFlag());
		e.setExpansionFlag(input.getExpansionFlag());
		e.setSortOrder(input.getSortOrder());
		e.setVersion(1L);
		em.persist(e);
		em.flush();
	}

	/** デフォルトブロック表示条件マスタを更新 */
	public void updateDefault(MwmDefaultBlockDisplay current, Mm0060BlockDisplay input) {
		current.setDisplayFlag(input.getDisplayFlag());
		current.setExpansionFlag(input.getExpansionFlag());
		current.setSortOrder(input.getSortOrder());
		current.setDeleteFlag(DeleteFlag.OFF);
		em.merge(current);
	}

	public void delete(MwmDefaultBlockDisplay old) {
		em.remove(old);
	}
}
