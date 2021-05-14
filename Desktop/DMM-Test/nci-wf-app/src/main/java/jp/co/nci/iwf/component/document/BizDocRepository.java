package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BizDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BizDocInfoEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwtBizDocInfo;

@ApplicationScoped
public class BizDocRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/**
	 * 業務文書情報取得.
	 * @param corporationCode 企業コード
	 * @param docId 文書ID
	 * @param screenDocId 画面文書定義ID
	 * @param menuRoleCodes メニューロールコード一覧
	 * @param localeCode ロケールコード
	 */
	public BizDocInfoEntity getBizDocInfoEntity(Long docId, Long screenDocId, LoginInfo login) {
		final StringBuilder sql = new StringBuilder();
		final List<Object> params = new ArrayList<>();
		if (docId != null) {
			sql.append(getSql("DC0012"));
			params.add(login.getLocaleCode());
			params.add(docId);
		} else if (screenDocId != null) {
//			StringBuilder replace = new StringBuilder();
//			for (String menuRoleCd : login.getMenuRoleCodes()) {
//				replace.append(replace.length() == 0 ? "?" : ", ?");
//				params.add(menuRoleCd);
//			}
//			sql.append(getSql("DC0011").replaceFirst("###REPLACE###", replace.toString()));
			sql.append(getSql("DC0011"));
			params.add(screenDocId);
//			params.add(login.getCorporationCode());
			params.add(login.getLocaleCode());
		}
		final List<BizDocInfoEntity> list = select(BizDocInfoEntity.class, sql, params.toArray());
		return list.stream().findFirst().orElse(null);
	}

	/** 業務文書情報取得. */
	public MwtBizDocInfo getMwtBizDocInfo(long docId) {
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return select(MwtBizDocInfo.class, getSql("DC0100_09"), params.toArray()).stream().findFirst().orElse(null);
	}

	/** 業務文書情報登録. */
	public Long insert(MwtBizDocInfo entity) {
		entity.setBizDocId(numbering.newPK(MwtBizDocInfo.class));
		// トランザクションID(業務文書における文書内容のトランTBLのプロセスIDに該当)を設定
		// ただし文書管理側ではマイナス値にして使用する
		if (entity.getTranId() == null) {
			long tranId = numbering.next("MWT_BIZ_DOC_INFO", "TRAN_ID");
			entity.setTranId(-tranId);
		}
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		return entity.getTranId();
	}

	/** 業務文書情報更新. */
	public Long update(MwtBizDocInfo entity, BizDocInfo bizDocInfo, boolean isVersionUp) {
		// バージョンを更新するなら新しいトランザクションIDを設定
		// ただしWF→文書管理の連携時はWF側のトランザクションデータをそのまま利用するため新しいトランザクションIDは採番しない
		if (eq(CommonFlag.ON, bizDocInfo.cooperateWf2DocFlag)) {
			entity.setTranId(bizDocInfo.processId);
		} else if (isVersionUp) {
			// トランザクションID(業務文書における文書内容のトランTBLのプロセスIDに該当)を設定
			// ただし文書管理側ではマイナス値にして使用する
			long tranId = numbering.next("MWT_BIZ_DOC_INFO", "TRAN_ID");
			entity.setTranId(-tranId);
		}
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.merge(entity);
		return entity.getTranId();
	}

	/** 画面文書定義マスタ取得. */
	public MwvScreenDocDef getMwvScreenDocDef(String corporationCode, String screenDocCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0110_02"));
		sql.append(" and CORPORATION_CODE = ?");
		sql.append(" and SCREEN_DOC_CODE = ?");
		sql.append(" and VALID_START_DATE <= ?");
		sql.append(" and ? < VALID_END_DATE + 1");
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		params.add(screenDocCode);
		params.add(today());
		params.add(today());
		return selectOne(MwvScreenDocDef.class, sql, params.toArray());
	}
}
