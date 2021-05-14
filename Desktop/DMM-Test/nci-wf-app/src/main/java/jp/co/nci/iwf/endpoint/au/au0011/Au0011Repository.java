package jp.co.nci.iwf.endpoint.au.au0011;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.model.base.WfmUserPassword;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.MwtResetPassword;
import jp.co.nci.iwf.jpa.entity.mw.WfvUserPassword;
import jp.co.nci.iwf.jpa.entity.wm.WfvCorporationProperty;

/**
 * パスワード変更リポジトリ
 */
@ApplicationScoped
public class Au0011Repository extends BaseRepository implements CodeBook {
	/**
	 * 現在のパスワードを抽出
	 * @param corporationCode
	 * @param userCode
	 * @param localeCode
	 * @return
	 */
	public WfvUserPassword get(String corporationCode, String userCode, String localeCode) {
		List<String> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(userCode);
		params.add(localeCode);

		// 生きているパスワードは０～１件しかない
		return selectOne(WfvUserPassword.class, getSql("AU0004"), params.toArray());
	}

	/**
	 * 過去および現在のパスワードを抽出
	 * @param corporationCode
	 * @param userCode
	 * @param localeCode
	 * @return
	 */
	public List<WfvUserPassword> getHistories(String corporationCode, String userCode, String localeCode) {
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(userCode);
		params.add(localeCode);
		params.add(today());

		// 過去のパスワード（＝死んでるパスワード）
		return select(WfvUserPassword.class, getSql("AU0005"), params.toArray());
	}

	/**
	 * 企業属性マスタを抽出
	 * @param corporationCode
	 * @param props
	 * @return
	 */
	public List<WfvCorporationProperty> getCorpProperties(String corporationCode, List<String> props, String localeCode) {
		final List<String> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(localeCode);

		final StringBuilder sql = new StringBuilder(getSql("AU0006"));
		if (props != null) {
			sql.append(" and PROPERTY_CODE in (");
			for (int i = 0; i < props.size(); i++) {
				if (i > 0) sql.append(", ");
				sql.append("?");
				params.add(props.get(i));
			}
			sql.append(")");
		}
		sql.append(" order by SORT_ORDER, PROPERTY_CODE");
		return select(WfvCorporationProperty.class, sql.toString(), params.toArray());
	}

	/**
	 * 既存パスワードを論理削除
	 * @param tableCorporationCode
	 * @param userCode
	 */
	public int disableOldPassword(WfmUserPassword pswd, WfUserRole ur) {
		final List<String> params = new ArrayList<>();
		params.add(ur.getCorporationCode());
		params.add(ur.getUserCode());
		params.add(ur.getIpAddress());
		params.add(pswd.getCorporationCode());
		params.add(pswd.getUserCode());

		return execSql(getSql("AU0007"), params.toArray());
	}

	/** リセットパスワード情報を取得 */
	public MwtResetPassword getMwtResetPassword(long resetPasswordId) {
		return em.find(MwtResetPassword.class, resetPasswordId);
	}
}
