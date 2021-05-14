package jp.co.nci.iwf.endpoint.mm.mm0020;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsDc;

/**
 * 表示条件設定のリポジトリ
 */
@ApplicationScoped
public class Mm0020Repository extends BaseRepository {
	@Inject
	private NumberingService numbering;

	/** コンテナ一覧を返す  */
	public List<MwmContainer> getContainers(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmContainer.class, getSql("MM0020_01"), params);
	}

	/** コンテナIDに紐付くパーツ表示条件一覧を返す */
	public List<Mm0020PartsDc> getPartsDcList(long containerId, String localeCode) {
		final Object[] params = { containerId, localeCode };
		return select(Mm0020PartsDc.class, getSql("MM0020_02"), params);
	}

	/** コンテナIDに紐付くパーツリストを返す */
	public List<Mm0020Parts> getPartsList(long containerId, String localeCode) {
		final Object[] params = { containerId, localeCode };
		return select(Mm0020Parts.class, getSql("MM0020_03"), params);
	}

	/** 表示条件マスタを返す */
	public List<Mm0020Dc> getDcList(String localeCode) {
		final Object[] params = { localeCode };
		return select(Mm0020Dc.class, getSql("MM0020_04"), params);
	}

	/** 表示条件マスタを返す */
	public List<MwmDc> getMwmDc(String localeCode) {
		final Object[] params = { localeCode };
		return select(MwmDc.class, getSql("MM0020_05"), params);
	}

	/** 表示条件マスタを更新 */
	public void update(Mm0020Dc input, MwmDc dc) {
		dc.setDcName(input.getDcName());
		dc.setDeleteFlag(DeleteFlag.OFF);
		em.merge(dc);
	}

	/** パーツ表示条件を物理削除 */
	public void delete(List<MwmPartsDc> removes) {
		em.flush();
		for (MwmPartsDc pdc : removes) {
			em.remove(pdc);
		}
	}

	/** コンテナIDに紐付くパーツ表示条件一覧を返す */
	public List<MwmPartsDc> getMwmPartsDc(long containerId, String localeCode) {
		final Object[] params = { containerId, localeCode };
		return select(MwmPartsDc.class, getSql("MM0020_02"), params);
	}

	/** パーツ表示条件をインサート */
	public void insert(Mm0020PartsDc input) {
		final MwmPartsDc pdc = new MwmPartsDc();
		pdc.setDcId(input.getDcId());
		pdc.setDcType(input.getDcType());
		pdc.setDeleteFlag(DeleteFlag.OFF);
		pdc.setPartsDcId(numbering.newPK(MwmPartsDc.class));
		pdc.setPartsId(input.getPartsId());
		em.persist(pdc);
	}

	/** パーツ表示条件を更新 */
	public void update(Mm0020PartsDc input, MwmPartsDc pdc) {
		pdc.setDcType(input.getDcType());
		em.merge(pdc);
	}
}
