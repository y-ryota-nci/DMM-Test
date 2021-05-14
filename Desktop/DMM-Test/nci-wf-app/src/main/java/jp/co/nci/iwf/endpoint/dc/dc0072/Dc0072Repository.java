package jp.co.nci.iwf.endpoint.dc.dc0072;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaTemplateDetailEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDetail;

/**
 * 拡張項目登録リポジトリ.
 */
@ApplicationScoped
public class Dc0072Repository extends BaseRepository {

	@Inject private NumberingService numbering;

	/** メタテンプレート定義取得. */
	public MwmMetaTemplateDef getByPk(long metaId) {
		return em.find(MwmMetaTemplateDef.class, metaId);
	}

	/** メタテンプレート定義項目取得. */
	public MwmMetaTemplateDef get(String corporationCode, String metaTemplateCode) {
		final Object[] params = { corporationCode, metaTemplateCode };
		return selectOne(MwmMetaTemplateDef.class, getSql("DC0072_01"), params);
	}

	/**
	 * メタ項目登録.
	 * @param inputed
	 */
	public MwmMetaTemplateDef insert(MwmMetaTemplateDef inputed) {
		inputed.setMetaTemplateId(numbering.newPK(MwmMetaTemplateDef.class));
		inputed.setDeleteFlag(DeleteFlag.OFF);
		em.persist(inputed);
		em.flush();
		return inputed;
	}

	/**
	 * メタ項目更新.
	 * @param org
	 * @param inputed
	 */
	public MwmMetaTemplateDef update(MwmMetaTemplateDef org, MwmMetaTemplateDef inputed) {
		org.setMetaTemplateName(inputed.getMetaTemplateName());
		org.setDeleteFlag(inputed.getDeleteFlag());
		em.merge(org);
		em.flush();
		return org;
	}

	/**
	 * テンプレート明細件数抽出
	 * @param req
	 * @return
	 */
	public int count(Dc0072Request req) {
		StringBuilder sql = new StringBuilder(getSql("DC0072_03"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		final StringBuilder countSql = new StringBuilder(getSql("DC0072_02").replaceFirst("###DC0072_03###", sql.toString()));

		return count(countSql.toString(), params.toArray());
	}

	/**
	 * テンプレート明細抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<MwmMetaTemplateDetailEx> select(Dc0072Request req) {
		final StringBuilder sql = new StringBuilder(getSql("DC0072_03"));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, true);

		return select(MwmMetaTemplateDetailEx.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Dc0072Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(LoginInfo.get().getLocaleCode());
		params.add(req.metaTemplateId);

		// ページングおよびソート
		if (paging) {
			// ソート
			if (isNotEmpty(req.sortColumn)) {
				sql.append(toSortSql(req.sortColumn, req.sortAsc));
			}
			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	/** メタテンプレート明細一覧取得.(テンプレート明細IDをKeyにMap形式で抽出) */
	public Map<Long, MwmMetaTemplateDetail> getMwmMetaTemplateDetailList(Long metaTemplateId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0072_04"));
		final Object[] params = { metaTemplateId };
		return select(MwmMetaTemplateDetail.class, sql, params).stream().collect(Collectors.toMap(MwmMetaTemplateDetail::getMetaTemplateDetailId, e -> e));
	}

	/** メタテンプレート明細の一括削除. */
	public void deleteMwmMetaTemplateDetails(List<Long> deleteIds) {
		if (!deleteIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deleteIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("DC0072_05"));
			sql.append(toInListSql("META_TEMPLATE_DETAIL_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** メタテンプレート明細更新. */
	public void update(MwmMetaTemplateDetail entity) {
		em.merge(entity);
	}
}
