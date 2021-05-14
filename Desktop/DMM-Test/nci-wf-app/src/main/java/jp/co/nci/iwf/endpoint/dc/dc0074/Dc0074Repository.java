package jp.co.nci.iwf.endpoint.dc.dc0074;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaItemEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaTemplateDetailEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDetail;

/**
 * テンプレート明細登録リポジトリ.
 */
@ApplicationScoped
public class Dc0074Repository extends BaseRepository {

	@Inject private NumberingService numbering;

	/** テンプレート明細取得. */
	public MwmMetaTemplateDetail getByPk(long metaTemplateDetailId) {
		return em.find(MwmMetaTemplateDetail.class, metaTemplateDetailId);
	}

	/** テンプレート明細取得. */
	public MwmMetaTemplateDetail get(Long metaTemplateId, Long metaId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0072_04"));
		sql.append(" and META_ID = ?");
		final Object[] params = { metaTemplateId, metaId };
		return selectOne(MwmMetaTemplateDetail.class, sql, params);
	}

	/**
	 * テンプレート明細抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public MwmMetaTemplateDetailEx getMwmMetaTemplateDetailEx(long metaTemplateId, long metaTemplateDetailId, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0072_03"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(metaTemplateId);
		sql.append(" and B.META_TEMPLATE_DETAIL_ID = ?");
		params.add(metaTemplateDetailId);

		return selectOne(MwmMetaTemplateDetailEx.class, sql.toString(), params.toArray());
	}

	/**
	 * 並び順の選択肢取得.
	 * @param metaTemplateId
	 * @param localeCode
	 * @return
	 */
	public List<OptionItem> getSortOrders(long metaTemplateId, String localeCode) {
		final Object[] params = { metaTemplateId };
		int count = count(getSql("DC0074_03"), params);
		final List<OptionItem> sortOrders = new ArrayList<>();
		IntStream.range(1, count + 1).forEach(i -> sortOrders.add( new OptionItem(i, toStr(i)) ));
		return sortOrders;
	}

	/** 拡張項目一覧取得. */
	public List<MwmMetaItemEx> getMwmMetaItemList(String corporationCode, Long metaId, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0060_02"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		sql.append(" and A.CORPORATION_CODE = ?");
		params.add(corporationCode);
		sql.append(" and A.DELETE_FLAG = ?");
		params.add(DeleteFlag.OFF);
		if (metaId != null) {
			sql.append(" and A.META_ID = ?");
			params.add(metaId);
		}
		return select(MwmMetaItemEx.class, sql, params.toArray());
	}

	/**
	 * テンプレート明細登録.
	 * @param inputed
	 */
	public Long insert(MwmMetaTemplateDetailEx inputed) {
		final MwmMetaTemplateDetail entity = new MwmMetaTemplateDetail();
		entity.setMetaTemplateDetailId(numbering.newPK(MwmMetaTemplateDetail.class));
		entity.setMetaTemplateId(inputed.getMetaTemplateId());
		entity.setMetaId(inputed.getMetaId());
		entity.setRequiredFlag(isEmpty(inputed.getRequiredFlag()) ? CommonFlag.OFF : CommonFlag.ON);
		entity.setMaxLengths(inputed.getMaxLengths());
		entity.setInitialValue1(inputed.getInitialValue1());
		entity.setInitialValue2(inputed.getInitialValue2());
		entity.setInitialValue3(inputed.getInitialValue3());
		entity.setInitialValue4(inputed.getInitialValue4());
		entity.setInitialValue5(inputed.getInitialValue5());
		entity.setSortOrder(inputed.getSortOrder());
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		em.flush();
		return entity.getMetaTemplateDetailId();
	}

	/**
	 * テンプレート明細更新.
	 * @param org
	 * @param inputed
	 */
	public void update(MwmMetaTemplateDetail org, MwmMetaTemplateDetailEx inputed) {
		org.setRequiredFlag(isEmpty(inputed.getRequiredFlag()) ? CommonFlag.OFF : CommonFlag.ON);
		org.setMaxLengths(inputed.getMaxLengths());
		org.setInitialValue1(inputed.getInitialValue1());
		org.setInitialValue2(inputed.getInitialValue2());
		org.setInitialValue3(inputed.getInitialValue3());
		org.setInitialValue4(inputed.getInitialValue4());
		org.setInitialValue5(inputed.getInitialValue5());
		org.setSortOrder(inputed.getSortOrder());
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
		em.flush();
	}

	/**
	 * テンプレート明細削除.
	 * @param org
	 * @param inputed
	 */
	public void delete(MwmMetaTemplateDetail org) {
		em.remove(org);
		em.flush();
	}

	/**
	 * 並び順一括更新
	 */
	public void updateSortOrder(long metaTemplateId, long metaTemplateDetailId,  int orgSortOrder, int newSortOrder, LoginInfo login) {
		final List<Object> params = new ArrayList<>();
		params.add(orgSortOrder);
		params.add(newSortOrder);
		params.add(orgSortOrder);
		params.add(newSortOrder);
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(timestamp());
		params.add(metaTemplateId);
		params.add(metaTemplateDetailId);
		execSql(getSql("DC0074_02"), params.toArray());
	}
}
