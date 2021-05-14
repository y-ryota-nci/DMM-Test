package jp.co.nci.iwf.endpoint.ti.ti0011;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.endpoint.ti.ti0010.Ti0010Category;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;

/**
 * カテゴリ編集画面リポジトリ
 */
@ApplicationScoped
public class Ti0011Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** カテゴリを挿入 */
	public long insert(Ti0010Category input) {
		final long categoryId = numbering.newPK(MwmCategory.class);
		final MwmCategory c = new MwmCategory();
		c.setCategoryCode(input.categoryCode);
		c.setCategoryName(input.categoryName);
		c.setCorporationCode(input.corporationCode);
		c.setDeleteFlag(DeleteFlag.OFF);
		c.setCategoryId(categoryId);
		em.persist(c);
		return categoryId;
	}

	/** カテゴリを更新 */
	public Long update(MwmCategory c, Ti0010Category input) {
		c.setCategoryCode(input.categoryCode);
		c.setCategoryName(input.categoryName);
		c.setDeleteFlag(DeleteFlag.OFF);
		em.merge(c);
		return c.getCategoryId();
	}

	/** カテゴリ配下の汎用マスタの件数を返す */
	public int countTableUnderCategory(String corporationCode, Long categoryId) {
		final Object[] params = { corporationCode, categoryId };
		return count(getSql("TI0010_09"), params);
	}

	/** カテゴリを削除 */
	public void delete(MwmCategory c) {
		em.remove(c);
	}

	/** カテゴリを削除 */
	public void delete(Long categoryId) {
		MwmCategory c = em.find(MwmCategory.class, categoryId);
		if (c != null)
			em.remove(c);
	}

}
