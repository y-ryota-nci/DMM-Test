package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaTemplateDetailEx;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocMetaInfoEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDef;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocMetaInfo;

@ApplicationScoped
public class DocAttributeExRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/** 文書メタ情報一覧取得. */
	public List<MwtDocMetaInfoEx> getMwtDocMetaInfoList(Long docId, Long metaTemplateId, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0100_20"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(docId);
		if (metaTemplateId != null) {
			sql.append(" and A.META_TEMPLATE_ID = ?");
			params.add(metaTemplateId);
		}
		// ソート順
		sql.append(toSortSql("B.SORT_ORDER", true));
		return select(MwtDocMetaInfoEx.class, sql, params.toArray());
	}

	/** 文書メタ情報一覧を文書メタIDをKeyにMap形式で取得 */
	public Map<Long, MwtDocMetaInfo> getMwtDocMetaInfoMap(long docId, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(docId);
		return select(MwtDocMetaInfo.class, getSql("DC0100_20"), params.toArray())
					.stream()
					.collect(Collectors.toMap(MwtDocMetaInfo::getDocMetaId, e -> e));
	}

	/** メタテンプレート定義一覧取得. */
	public List<MwmMetaTemplateDef> getMwmMetaTemplateDefList(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0070_02"));
		sql.append(" and A.CORPORATION_CODE = ?");
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		return select(MwmMetaTemplateDef.class, sql, params.toArray());
	}

	/** メタ情報テンプレート明細一覧取得. */
	public List<MwmMetaTemplateDetailEx> getMwmMetaTemplateDetailList(Long metaTemplateId, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0072_03"));
		sql.append(toSortSql("B.SORT_ORDER, B.META_TEMPLATE_DETAIL_ID", true));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(metaTemplateId);
		return select(MwmMetaTemplateDetailEx.class, sql, params.toArray());
	}

	/** 文書メタ情報登録. */
	public void insert(long docId, DocAttributeExInfo inputed) {
		final MwtDocMetaInfo entity = new MwtDocMetaInfo();
		entity.setDocId(docId);
		entity.setMetaTemplateId(inputed.metaTemplateId);
		entity.setMetaTemplateDetailId(inputed.metaTemplateDetailId);
		entity.setMetaValue1(inputed.metaValue1);
		entity.setMetaValue2(inputed.metaValue2);
		entity.setMetaValue3(inputed.metaValue3);
		entity.setMetaValue4(inputed.metaValue4);
		entity.setMetaValue5(inputed.metaValue5);
		entity.setDeleteFlag(DeleteFlag.OFF);
		// 文書メタIDを採番
		entity.setDocMetaId(numbering.newPK(MwtDocMetaInfo.class));
		em.persist(entity);
	}

	/** 文書メタ情報更新. */
	public void update(MwtDocMetaInfo org, DocAttributeExInfo inputed) {
		org.setMetaValue1(inputed.metaValue1);
		org.setMetaValue2(inputed.metaValue2);
		org.setMetaValue3(inputed.metaValue3);
		org.setMetaValue4(inputed.metaValue4);
		org.setMetaValue5(inputed.metaValue5);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 文書メタ情報の一括削除. */
	public void deleteMwtDocMetaInfo(Set<Long> deleteIds) {
		if (!deleteIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("DC0100_21"));
			sql.append(toInListSql("DOC_META_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}
}
