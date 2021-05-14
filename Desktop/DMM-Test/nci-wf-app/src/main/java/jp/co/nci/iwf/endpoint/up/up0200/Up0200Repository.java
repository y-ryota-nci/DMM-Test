package jp.co.nci.iwf.endpoint.up.up0200;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.common.CodeMaster.ChangeRequestFlag;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Organization;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Post;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200User;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200UserBelong;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * プロファイル情報アップロード画面のリポジトリ
 */
@ApplicationScoped
public class Up0200Repository extends BaseRepository {
	/** 汎用SQL置換文字 */
	private static final String REPLACE = quotePattern("${REPLACE}");
	/** SQL置換文字：テーブル名 */
	private static final String TABLE_NAME = quotePattern("${TABLE_NAME}");
	/** SQL置換文字：ワークテーブル名 */
	private static final String WORK_TABLE_NAME = quotePattern("${WORK_TABLE_NAME}");
	/** SQL置換文字：ワークフィールド名 */
	private static final String WORK_FIELD_NAME = quotePattern("${WORK_FIELD_NAME}");
	/** SQL置換文字：結合条件 */
	private static final String JOIN_CONDITION = quotePattern("${JOIN_CONDITION}");

	@Inject private SessionHolder sessionHolder;

	/** truncate文を実行 */
	public void truncateTable(String tblName) {
		final String sql = getSql("UP0200_01").replaceFirst(REPLACE, tblName);
		execSql(sql);
	}

	/** 企業別プロパティを抽出 */
	private WfmCorpPropMaster getWfmCorpPropMaster(String propertyCode) {
		WfmCorpPropMaster entity = em.find(WfmCorpPropMaster.class, propertyCode);
		return entity;
	}

	/** プロパティマスタに対して、プロファイル情報アップロードのエントリを行ロック */
	public WfmCorpPropMaster lock() {
		final String propertyCode = CorporationProperty.UPLOAD_PROFILE.toString();
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			conn.setAutoCommit(false);

			final String sql = getSql("UP0200_02");
			final Object[] params = { propertyCode };
			final int count = NativeSqlUtils.execSql(conn, sql, params);
			if (count == 1) {
				conn.commit();
				return getWfmCorpPropMaster(propertyCode);
			}
			conn.rollback();
			return null;
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** プロパティマスタに対して、プロファイル情報アップロードのエントリを行ロック解除 */
	public boolean unlock(WfmCorpPropMaster entity) {
		// 未ロックならロック中として更新
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			conn.setAutoCommit(false);

			final String sql = getSql("UP0200_03");
			final Object[] params = { entity.getPropertyCode(), entity.getTimestampUpdated() };
			final int count = NativeSqlUtils.execSql(conn, sql, params);
			conn.commit();

			boolean result = count > 0;
			return result;
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 組織マスタをWORKへ登録 */
	public void bulkInsertOrg(List<Up0200Organization> organizations) {
		// このConnectionはJPAが管理しているので勝手にCloseしてはダメ
		final Connection conn = em.unwrap(Connection.class);

		final String sql = getSql("UP0200_04");
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();

		NativeSqlUtils.debugSql(sql);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int r = 0; r < organizations.size(); r++) {
				final Timestamp timestamp = timestamp();
				final Up0200Organization o = organizations.get(r);
				final Object[] params = {
					o.corporationCode,
					o.organizationCode,
					o.organizationNameJa,
					defaults(o.organizationNameEn, o.organizationNameJa),
					defaults(o.organizationNameZh, o.organizationNameJa),
					o.organizationAddedInfo,
					o.organizationNameAbbrJa,
					defaults(o.organizationNameAbbrEn, o.organizationNameAbbrJa),
					defaults(o.organizationNameAbbrZh, o.organizationNameAbbrJa),
					o.organizationCodeUp,
					o.postNum,
					o.addressJa,
					defaults(o.addressEn, o.addressJa),
					defaults(o.addressZh, o.addressJa),
					o.telNum,
					o.faxNum,
					o.organizationLevel,
					o.sortOrder,
					o.extendedInfo01,
					o.extendedInfo02,
					o.extendedInfo03,
					o.extendedInfo04,
					o.extendedInfo05,
					o.extendedInfo06,
					o.extendedInfo07,
					o.extendedInfo08,
					o.extendedInfo09,
					o.extendedInfo10,
					o.validStartDate,
					o.validEndDate,
					o.deleteFlag,
					corporationCode,
					userCode,
					ipAddr,
					timestamp,
				};
				NativeSqlUtils.setParams(ps, params);

				ps.addBatch();

				// 一定行ごと、あるいは最終行なら一括バッチ処理を実施
				if (r % 100 == 0 || r == organizations.size() - 1) {
					ps.executeBatch();
				}
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 組織マスタをワークとの差分で更新 */
	public void upsertOrg() {
		final String sql = getSql("UP0200_05");
		execSql(sql);
	}

	/** 組織マスタ抽出 */
	public List<Up0200Organization> getOrganizations(String corporationCode, String localeCode) {
		final String sql = getSql("UP0200_06");
		final Object[] params = { corporationCode };
		final List<Up0200Organization> list = select(Up0200Organization.class, sql, params);
		list.forEach(entity -> em.detach(entity));
		return list;
	}

	/** 役職マスタ抽出 */
	public List<Up0200Post> getPosts(String corporationCode, String localeCode) {
		final String sql = getSql("UP0200_07");
		final Object[] params = { corporationCode };
		final List<Up0200Post> list = select(Up0200Post.class, sql, params);
		list.forEach(entity -> em.detach(entity));
		return list;
	}

	/** ユーザマスタ抽出 */
	public List<Up0200User> getUsers(String corporationCode, String localeCode) {
		final String sql = getSql("UP0200_08");
		final Object[] params = { corporationCode };
		final List<Up0200User> list = select(Up0200User.class, sql, params);
		list.forEach(entity -> em.detach(entity));
		return list;
	}

	/** ユーザ所属抽出 */
	public List<Up0200UserBelong> getUserBelongs(String corporationCode, String localeCode) {
		final String sql = getSql("UP0200_09");
		final Object[] params = { corporationCode };
		List<Up0200UserBelong> list = select(Up0200UserBelong.class, sql, params);
		list.forEach(entity -> em.detach(entity));
		return list;
	}

	/** 役職マスタ(ワーク)への一括インサート */
	public void bulkInsertPost(List<Up0200Post> posts) {
		// このConnectionはJPAが管理しているので勝手にCloseしてはダメ
		final Connection conn = em.unwrap(Connection.class);

		final String sql = getSql("UP0200_10");
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();

		NativeSqlUtils.debugSql(sql);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int r = 0; r < posts.size(); r++) {
				final Timestamp timestamp = timestamp();
				final Up0200Post p = posts.get(r);
				final Object[] params = {
					p.corporationCode,
					p.postCode,
					p.postNameJa,
					p.postNameEn,
					p.postNameZh,
					p.postAddedInfo,
					p.postNameAbbrJa,
					p.postNameAbbrEn,
					p.postNameAbbrZh,
					p.postLevel,
					p.validStartDate,
					p.validEndDate,
					p.uppperPostSettingsFlag,
					p.deleteFlag,
					corporationCode,
					userCode,
					ipAddr,
					timestamp,
				};
				NativeSqlUtils.setParams(ps, params);

				ps.addBatch();

				// 一定行ごと、あるいは最終行なら一括バッチ処理を実施
				if (r % 100 == 0 || r == posts.size() - 1) {
					ps.executeBatch();
				}
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 役職マスタを差分更新 */
	public void upsertPost() {
		final String sql = getSql("UP0200_11");
		execSql(sql);
	}

	/** ユーザマスタ(ワーク)への一括インサート */
	public void bulkInsertUser(List<Up0200User> users) {
		// このConnectionはJPAが管理しているので勝手にCloseしてはダメ
		final Connection conn = em.unwrap(Connection.class);

		final String sql = getSql("UP0200_12");
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();

		NativeSqlUtils.debugSql(sql);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int r = 0; r < users.size(); r++) {
				final Timestamp timestamp = timestamp();
				final Up0200User u = users.get(r);
				final Object[] params = {
						u.corporationCode,
						u.userCode,
						u.userNameJa,
						u.userNameEn,
						u.userNameZh,
						u.userNameAbbrJa,
						u.userNameAbbrEn,
						u.userNameAbbrZh,
						u.postNum,
						u.addressJa,
						u.addressEn,
						u.addressZh,
						u.telNum,
						u.telNumCel,
						u.mailAddress,
						u.userAddedInfo,
						u.sealName,
						u.administratorType,
						u.defaultLocaleCode,
						u.extendedInfo01,
						u.extendedInfo02,
						u.extendedInfo03,
						u.extendedInfo04,
						u.extendedInfo05,
						u.extendedInfo06,
						u.extendedInfo07,
						u.extendedInfo08,
						u.extendedInfo09,
						u.extendedInfo10,
						u.validStartDate,
						u.validEndDate,
						u.deleteFlag,
						SecurityUtils.DEFAULT_HASH_PASSWORD,
						corporationCode,
						userCode,
						ipAddr,
						timestamp,
				};
				NativeSqlUtils.setParams(ps, params);

				ps.addBatch();

				// 一定行ごと、あるいは最終行なら一括バッチ処理を実施
				if (r % 100 == 0 || r == users.size() - 1) {
					ps.executeBatch();
				}
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** ユーザマスタを差分更新 */
	public void upsertUser() {
		final String sql = getSql("UP0200_13");
		execSql(sql);
	}

	/** ユーザ所属(ワーク)の一括インサート */
	public void bulkInsertUserBelong(List<Up0200UserBelong> userBelongs) {
		// このConnectionはJPAが管理しているので勝手にCloseしてはダメ
		final Connection conn = em.unwrap(Connection.class);

		final String sql = getSql("UP0200_14");
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();

		NativeSqlUtils.debugSql(sql);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int r = 0; r < userBelongs.size(); r++) {
				final Timestamp timestamp = timestamp();
				final Up0200UserBelong ub = userBelongs.get(r);
				final Object[] params = {
						ub.corporationCode,
						ub.userCode,
						ub.seqNoUserBelong,
						ub.organizationCode,
						ub.postCode,
						ub.jobType,
						ub.immediateManagerFlag,
						ub.directorFlag,
						ub.validStartDate,
						ub.validEndDate,
						ub.deleteFlag,
						corporationCode,
						userCode,
						ipAddr,
						timestamp,
				};
				NativeSqlUtils.setParams(ps, params);

				ps.addBatch();

				// 一定行ごと、あるいは最終行なら一括バッチ処理を実施
				if (r % 100 == 0 || r == userBelongs.size() - 1) {
					ps.executeBatch();
				}
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** ユーザ所属の差分更新 */
	public void upsertUserBelong() {
		final String sql = getSql("UP0200_15");
		execSql(sql);
	}

	/**
	 * ユーザマスタ(ワーク)にいるがパスワードなしのユーザに、パスワードマスタを生成
	 * @param changeRequestFlag trueならパスワード変更要求する
	 */
	public void insertInitPassword(boolean changeRequestFlag) {
		final String sql = getSql("UP0200_16");
		final Object[] params = { changeRequestFlag ? ChangeRequestFlag.ON : ChangeRequestFlag.OFF };
		execSql(sql, params);
	}

	/**
	 * ワークと多言語マスタを差分更新
	 * @param tblName マスタテーブル名（WFM_USERとか)
	 * @param columnName 多言語化するカラム名（USER_NAMEとか）
	 * @param localeCode 言語コード（jaとか）
	 * @param pkFieldNames プライマリキーの配列（{"CORPORATION_CODE", "USER_CODE"}とか）。
	 * @return
	 */
	public int upsertNameLookup(String tblName, String columnName, String localeCode, String[] pkFieldNames) {
		final String workTableName = tblName + "_WORK";
		final String workFieldName = columnName + "_" + localeCode.toUpperCase();
		final String joinCondition = new MessageFormat("W.{0} = O.{0} and W.{1} = O.{1}").format(pkFieldNames);

		final String sql = getSql("UP0200_17")
				.replaceAll(TABLE_NAME, tblName)
				.replaceAll(WORK_TABLE_NAME, workTableName)
				.replaceAll(WORK_FIELD_NAME, workFieldName)
				.replaceAll(JOIN_CONDITION, joinCondition);
		final Object[] params = { tblName, columnName, localeCode };

		final int count = execSql(sql, params);
		return count;
	}

	/**
	 * ワークにないならテーブルから論理削除
	 * @param tblName マスタテーブル名（WFM_USERとか)
	 * @param pkColNames プライマリキーの配列（{"CORPORATION_CODE", "USER_CODE"}とか）。
	 * @return
	 */
	public int deleteIfNotUse(String tblName, String[] pkColNames) {
		final String workTblName = tblName + "_WORK";
		final String joinCondition;
		if (pkColNames.length == 2)
			joinCondition = new MessageFormat("W.{0} = O.{0} and W.{1} = O.{1}").format(pkColNames);
		else
			joinCondition = new MessageFormat("W.{0} = O.{0} and W.{1} = O.{1} and W.{2} = O.{2}").format(pkColNames);

		final WfUserRole ur = sessionHolder.getWfUserRole();
		final Object[] params = { ur.getCorporationCode(), ur.getUserCode(), ur.getIpAddress(), ur.getCorporationCode() };
		final String sql = getSql("UP0200_18")
				.replaceAll(TABLE_NAME, tblName)
				.replaceAll(WORK_TABLE_NAME, workTblName)
				.replaceAll(JOIN_CONDITION, joinCondition);
		return execSql(sql, params);
	}

	/** 企業コード＋ユーザコード一覧を抽出 */
	public Set<String> getAllUserCodes(String corporationCode) {
		final Object[] params = { corporationCode };
		final String sql = getSql("UP0200_19");
		final Set<String> values = new HashSet<>(1024);
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			List<Map<String, Object>> rows = NativeSqlUtils.select(conn, sql, params);
			for (Map<String, Object> row : rows) {
				values.add(Up0200Service.toKey(row.get("CORPORATION_CODE"), row.get("USER_CODE")));
			}
			return values;
		}
		catch(SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 企業コード＋組織コード一覧を抽出 */
	public Set<String> getAllOrganizationCodes(String corporationCode) {
		final Object[] params = { corporationCode };
		final String sql = getSql("UP0200_20");
		final Set<String> values = new HashSet<>(128);
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			List<Map<String, Object>> rows = NativeSqlUtils.select(conn, sql, params);
			for (Map<String, Object> row : rows) {
				values.add(Up0200Service.toKey(row.get("CORPORATION_CODE"), row.get("ORGANIZATION_CODE")));
			}
			return values;
		}
		catch(SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
