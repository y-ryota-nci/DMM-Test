package jp.co.dmm.customize.endpoint.po.po0040;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.dmm.customize.jpa.entity.mw.BumonMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps2Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.dmm.customize.jpa.entity.mw.VTaxFgChg;
import jp.co.nci.integrated_workflow.common.CodeMaster.ApplicationStatus;
import jp.co.nci.integrated_workflow.common.CodeMaster.ApprovalStatus;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * kintoneデータ取込のリポジトリ
 */
@ApplicationScoped
public class Po0040Repository extends BaseRepository {

	@Inject private SessionHolder sessionHolder;

	/** 部門マスタ一覧の取得. */
	public List<BumonMst> getBumonMstList(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(BumonMst.class, getSql("PO0040_01"), params);
	}

//	/** 部門コード一覧の取得. */
//	public Set<String> getBumonCds(String corporationCode) {
//		final Object[] params = { corporationCode };
//		return select(BumonMst.class, getSql("PO0040_01"), params).stream().map(e -> e.getId().getBumonCd()).collect(Collectors.toSet());
//	}

	/** 消費税マスタ一覧の取得. */
	public List<TaxMst> getTaxMstList(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(TaxMst.class, getSql("PO0040_02"), params);
	}

	/** 費目コード一覧の取得. */
	public Set<String> getItmExpsCds(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(ItmexpsMst.class, getSql("PO0040_31"), params).stream().map(e -> e.getId().getItmexpsCd()).collect(Collectors.toSet());
	}

	/**
	 * 費目関連2マスタ一覧取得.
	 * 引数の組織コードは第三組織コード(部・室)
	 * @param corporationCode 会社コード
	 * @param orgnzCd 組織コード
	 * @return 費目関連2マスタ一覧
	 */
	public List<Itmexps2Chrmst> getItmexps2ChrmstList(String corporationCode, String organizationCode) {
		final Object[] params = { corporationCode, organizationCode };
		return select(Itmexps2Chrmst.class, getSql("PO0040_32"), params);
	}


	/** 費目関連1マスタ一覧取得. */
	public List<Itmexps1Chrmst> getItmexps1ChrmstList(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(Itmexps1Chrmst.class, getSql("PO0040_33"), params);
	}

	/** 取引先マスタの件数取得. */
	public int countSplrMst(String corporationCode, String splrCd) {
		final Object[] params = { corporationCode, splrCd };
		return count(getSql("PO0040_03"), params);
	}

	/** 振込先銀行口座マスタ情報取得. */
	public List<PayeeBnkaccMst> getPayeeBnkaccMst(String corporationCode, String splrCd, Date exhbDt) {
		final Object[] params = { corporationCode, splrCd, exhbDt };
		return select(PayeeBnkaccMst.class, getSql("PO0040_04"), params);
	}

	/** 消費税フラグビュー一覧取得. */
	public List<VTaxFgChg> getVTaxFgChg(String corporationCode, String localeCode) {
		final Object[] params = { corporationCode, localeCode };
		return select(VTaxFgChg.class, getSql("PO0040_46"), params);
	}

	/** kintone情報の件数取得. */
	public int countKntnInf(String corporationCode, String lotNo) {
		final Object[] params = { corporationCode, lotNo };
		return count(getSql("PO0040_05"), params);
	}

	/** システム日付に対する会計カレンダマスタの存在チェック. */
	public boolean isExistsAccClndMst(String companyCd) {
		final Object[] params = { companyCd };
		return count(getSql("PO0040_37"), params) > 0;
	}

	/** 支払依頼申請WFの件数取得. */
	public int countPayReqWf(String corporationCode, String lotNo, boolean isApproved) {
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(lotNo);
		final StringBuilder sql = new StringBuilder(getSql("PO0040_06"));
		if (isApproved) {
			sql.append(" AND A.PROCESS_STATUS in ('end', 'end_r') ");
			sql.append(" AND A.APPROVAL_STATUS = ? ");
			params.add(ApprovalStatus.APPROVED);
		} else {
			sql.append(" AND A.PROCESS_STATUS in ('start', 'wait', 'run') ");
			sql.append(" AND A.APPLICATION_STATUS = ? ");
			params.add(ApplicationStatus.APPLIED);
		}
		return count(sql.toString(), params.toArray());
	}

	/** 企業別プロパティを抽出 */
	private WfmCorpPropMaster getWfmCorpPropMaster(String propertyCode) {
		WfmCorpPropMaster entity = em.find(WfmCorpPropMaster.class, propertyCode);
		return entity;
	}

	/** プロパティマスタに対して、kintoneデータアップロードのエントリを行ロック */
	public WfmCorpPropMaster lock() {
		final String propertyCode = CorporationProperty.UPLOAD_PROFILE.toString();
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			conn.setAutoCommit(false);

			final String sql = getSql("PO0040_08");
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

	/** プロパティマスタに対して、kintoneデータアップロードのエントリを行ロック解除 */
	public boolean unlock(WfmCorpPropMaster entity) {
		// 未ロックならロック中として更新
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			conn.setAutoCommit(false);

			final String sql = getSql("PO0040_09");
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

	/** パーツ採番形式IDの取得. */
	public Long getPartsNumberingFormatId(String corporationCode, String partsNumberingFormatCode) {
		final Object[] params = { corporationCode, partsNumberingFormatCode };
		final MwmPartsNumberingFormat entity = selectOne(MwmPartsNumberingFormat.class, getSql("PO0040_10"), params);
		if (entity != null) {
			em.detach(entity);
			return entity.getPartsNumberingFormatId();
		}
		return null;
	}

	/** kintoneデータをkintone情報へ登録 */
	public void bulkInsertKntnInf(List<Po0040KntnInf> datas, final String companyCd, final String lotNo, final String purordNo, final String rcvinspNo, final String buyNo) {
		// このConnectionはJPAが管理しているので勝手にCloseしてはダメ
		final Connection conn = em.unwrap(Connection.class);

		final String sql = getSql("PO0040_11");
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();

		NativeSqlUtils.debugSql(sql);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (Po0040KntnInf d : datas) {
				final Object[] params = {companyCd, lotNo, d.sqno,
						d.recNo, d.evntNo, d.kntnSts, d.exhbDt, d.evntMngNo, d.evntCont,
						d.kntnHllId, d.hllNm, d.prdctId, d.prdctNm, d.tlntNm, d.splrCd,
						d.baseAmt, d.adjBaseAmt, d.trnspExpAmt, d.adjTrnspExpAmt, d.mnscrExpAmt, d.invAmt,
						d.bumonCd, d.anlysCd, d.smry, d.taxUnt, d.taxFg, d.taxCd,
						purordNo, rcvinspNo, buyNo, d.exhbDt, d.itmExpsCd1, d.itmExpsCd2,
						d.taxSbjTp, d.taxKndCd, d.taxFgChg,
						corporationCode, userCode, ipAddr,
						corporationCode, userCode, ipAddr
					};
				NativeSqlUtils.setParams(ps, params);
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 発注情報の一括登録. */
	public void bulkInsertPurordInf(final String companyCd, final String lotNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();
		final String sbmtDptCd = login.getOrganizationCode();	//申請部署コード
		final String sbmtDptNm = login.getOrganizationName();	//申請部署コード
		final String orgnzCd = login.getOrganizationCodeUp3();	//組織コード(組織階層=チーム)

		final Object[] args = {
			userCode, sbmtDptCd, sbmtDptNm, orgnzCd,
			corporationCode, userCode, ipAddr,
			corporationCode, userCode, ipAddr,
			companyCd, lotNo,
			corporationCode, login.getOrganizationCode()};
		execSql(getSql("PO0040_12"), args);
	}

	/** 発注明細情報の一括登録. */
	public void bulkInsertPurorddtlInf(final String companyCd, final String lotNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();
		final String orgnzCd = login.getOrganizationCodeUp3();	//組織コード(組織階層=チーム)

		final Object[] args = {
				orgnzCd,
				corporationCode, userCode, ipAddr,
				corporationCode, userCode, ipAddr,
				companyCd, lotNo};
		execSql(getSql("PO0040_13"), args);
	}

	/** 検収情報の一括登録. */
	public void bulkInsertRcvinspInf(final String companyCd, final String lotNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();
		final String sbmtDptCd = login.getOrganizationCode();	//申請部署コード
		final String sbmtDptNm = login.getOrganizationName();	//申請部署コード
		final String orgnzCd = login.getOrganizationCodeUp3();	//組織コード(組織階層=チーム)

		final Object[] args = {
			userCode, sbmtDptCd, sbmtDptNm, orgnzCd,
			corporationCode, userCode, ipAddr,
			corporationCode, userCode, ipAddr,
			companyCd, lotNo,
			corporationCode, login.getOrganizationCode()};
		execSql(getSql("PO0040_14"), args);
	}

	/** 検収明細情報の一括登録. */
	public void bulkInsertRcvinspdtlInf(final String companyCd, final String lotNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();
		final String orgnzCd = login.getOrganizationCodeUp3();	//組織コード(組織階層=チーム)

		final Object[] args = {
				orgnzCd,
				corporationCode, userCode, ipAddr,
				corporationCode, userCode, ipAddr,
				companyCd, lotNo};
		execSql(getSql("PO0040_15"), args);
	}

	/** 仕入情報の一括登録. */
	public void bulkInsertBuyInf(final String companyCd, final String lotNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();
		final String orgnzCd = login.getOrganizationCodeUp3();	//組織コード(組織階層=チーム)

		final Object[] args = {
			orgnzCd,
			corporationCode, userCode, ipAddr,
			corporationCode, userCode, ipAddr,
			companyCd, lotNo,
			corporationCode, login.getOrganizationCode()};
		execSql(getSql("PO0040_16"), args);
	}

	/** 仕入明細情報の一括登録. */
	public void bulkInsertBuydtlInf(final String companyCd, final String lotNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = ur.getCorporationCode();
		final String userCode = ur.getUserCode();
		final String ipAddr = ur.getIpAddress();
		final String orgnzCd = login.getOrganizationCodeUp3();	//組織コード(組織階層=チーム)

		final Object[] args = {
				orgnzCd,
				corporationCode, userCode, ipAddr,
				corporationCode, userCode, ipAddr,
				companyCd, lotNo};
		execSql(getSql("PO0040_17"), args);
	}

	/** 新規_支払依頼申請の画面プロセス定義取得. */
	public MwmScreenProcessDef getMwmScreenProcessDef(final String corporationCode, final String screenProcessCode) {
		final Object[] params = {corporationCode, screenProcessCode};
		return selectOne(MwmScreenProcessDef.class, getSql("PO0040_20"), params);
	}

	/** 仕入Noの採番処理. */
	public String getBuyNo(final String companyCd) {
		final String sql = getSql("PO0040_34");
		NativeSqlUtils.debugSql(sql, new Object[]{companyCd});

		// 独立したコネクションを取得してSQL発行＆コミット
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			// 元のautoCommitを退避
			boolean original = conn.getAutoCommit();
			conn.setAutoCommit(false);

			try (CallableStatement cstmt = conn.prepareCall(sql)) {
				cstmt.registerOutParameter(1, Types.VARCHAR);
				cstmt.setString(2, companyCd);
				cstmt.registerOutParameter(3, Types.NUMERIC);
				cstmt.registerOutParameter(4, Types.VARCHAR);
				// プロシージャ実行
				cstmt.executeUpdate();
				// OUTパラメータの処理結果を取得
				final BigDecimal rtnCode = cstmt.getBigDecimal(3);
				if (!eq(ReturnCode.SUCCESS, rtnCode)) {
					final String errMsg = cstmt.getString(4);
					throw new InternalServerErrorException(errMsg);
				}
				return cstmt.getString(1);
			}
			finally {
				// 元のautoCommitを復元
				conn.commit();
				conn.setAutoCommit(original);
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 発注情報の一括削除. */
	public void deletePurordInf(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_21"));
	}

	/** 発注明細情報の一括削除. */
	public void deleteOrdDtlInf(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_22"));
	}

	/** WF発注情報の一括削除 */
	public void deleteWfPurord(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_40"));
	}

	/** WF発注明細情報の一括削除 */
	public void deleteWfPurordDtl(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_41"));
	}

	/** WF発注申請プロセスインスタンスの一括削除 */
	public void deleteWfPurordProcess(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_44"));
	}

	/** 検収情報の一括削除. */
	public void deleteRcvInspInf(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_23"));
	}

	/** 検収明細情報の一括削除. */
	public void deleteRcvInspDtlInf(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_24"));
	}

	/** WF検収情報の一括削除 */
	public void deleteWfRcvInsp(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_42"));
	}

	/** WF検収明細情報の一括削除 */
	public void deleteWfRcvInspDtl(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_43"));
	}

	/** WF検収申請プロセスインスタンスの一括削除 */
	public void deleteWfRcvInspProcess(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_45"));
	}

	/** 仕入情報の一括削除. */
	public void deleteBuyInf(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_25"));
	}

	/** 仕入明細情報の一括削除. */
	public void deleteBuyDtlInf(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_26"));
	}

	/** kintone情報の一括削除 */
	public void deleteKntnInf(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_27"));
	}

	/** プロセスインスタンスの一括削除 */
	public void deleteWftProcess(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_28"));
	}

	/** WF支払情報の一括削除 */
	public void deleteMwtPay(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_35"));
	}

	/** WF支払情報の一括削除 */
	public void deleteMwtPaydtl(final String companyCd, final String lotNo) {
		delete(companyCd, lotNo, getSql("PO0040_36"));
	}

	private void delete(final String companyCd, final String lotNo, final String sql) {
		final Object[] params = { companyCd, lotNo };
		execSql(sql, params);
	}

	/** SS-GL連携処理 */
	public void ssSendRcv(final String companyCd, final String lotNo) {
		execute(getSql("PO0040_29"), companyCd, lotNo);
	}

	/**
	 * SS-GL連携の取消処理.
	 * 検収取消を実行するとプロシージャ内部でSS-GL連携の取消処理まで行ってくれる
	 */
	public void cancelRcvInsp(final String companyCd, final String lotNo) {
		execute(getSql("PO0040_30"), companyCd, lotNo);
	}

	/**
	 * WF発注申請生成処理
	 * @param companyCd 企業コード
	 * @param purordNo 発注No
	 */
	public void createWfPurord(final String companyCd, final String purordNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final Object[] params = {companyCd, purordNo, ur.getCorporationCode(), ur.getUserCode(), ur.getIpAddress()};
		executeCallableStatement(getSql("PO0040_38"), params);
	}

	/**
	 * WF検収申請生成処理
	 * @param companyCd 企業コード
	 * @param rcvinspNo 検収No
	 */
	public void createWfRcvinsp(final String companyCd, final String rcvinspNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final Object[] params = {companyCd, rcvinspNo, ur.getCorporationCode(), ur.getUserCode(), ur.getIpAddress()};
		executeCallableStatement(getSql("PO0040_39"), params);
	}

	private void execute(final String sql, final String companyCd, final String lotNo) {
		final WfUserRole ur = sessionHolder.getWfUserRole();
		final List<Map<String, Object>> maps = getRcvinspNoList(companyCd, lotNo);
		for (Map<String, Object> map: maps) {
			final Object[] params = {
				map.get("COMPANY_CD"), map.get("RCVINSP_NO"), ur.getCorporationCode(), ur.getUserCode(), ur.getIpAddress()
			};
			executeCallableStatement(sql, params);
		}
	}

	/** kintone情報TBLから会社コード、検収Noを抽出. */
	private List<Map<String, Object>> getRcvinspNoList(final String companyCd, final String lotNo) {
		final Object[] params = { companyCd, lotNo };
		final String sql = getSql("PO0040_07");
		try {
			// このConnectionはJPAが管理しているので勝手にCloseしてはダメ
			final Connection conn = em.unwrap(Connection.class);
			return NativeSqlUtils.select(conn, sql, params);
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** プロシージャを実行 */
	private void executeCallableStatement(final String sql, final Object[] params) {
		try (CallableStatement cstmt = em.unwrap(Connection.class).prepareCall(sql)) {
			// INパラメータの設定
			int i = 1;
			for (Object param: params) {
				cstmt.setObject(i++, param);
			}
			// OUTパラメータの設定
			int j = i;
			cstmt.registerOutParameter((j + 0), Types.NUMERIC);		//処理結果
			cstmt.registerOutParameter((j + 1), Types.VARCHAR);		//エラーメッセージ
			// SQLログ出力
			NativeSqlUtils.debugSql(sql, params);
			// プロシージャ実行
			cstmt.executeUpdate();
			// OUTパラメータの処理結果を取得
			final BigDecimal rtnCode = cstmt.getBigDecimal((j + 0));
			if (!eq(ReturnCode.SUCCESS, rtnCode)) {
				final String errMsg = cstmt.getString((j + 1));
				throw new InternalServerErrorException(errMsg);
			}

		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
