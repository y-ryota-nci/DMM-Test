package jp.co.dmm.customize.endpoint.po;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.PrdPaySts;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordMstPK;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * DMM定期発注情報用サービス
 */
@ApplicationScoped
public class PrdPurordInfService extends BaseRepository {
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** セッション情報 */
	@Inject private SessionHolder sessionHolder;

	/**
	 * 発注Noをキーに発注情報を抽出し、それをUserDataServiceで使用できる形に整形して返す
	 * @param companyCd 会社CD
	 * @param purordNo 発注No
	 * @return
	 */
	public Map<String, List<UserDataEntity>> getUserDataMap(String companyCd, String purordNo) {
		assert(isNotEmpty(companyCd));
		assert(isNotEmpty(purordNo));

		final Map<String, List<UserDataEntity>> tables = new HashMap<>();

		// 定期発注マスタ（定期発注申請すると定期発注マスタへ書き込むから）
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Object [] args = { localeCode, companyCd, purordNo };
		{
			final String tableName = "MWT_PRD_PURORD";	// 独立画面「定期発注」のヘッダ部のコンテナのテーブル名
			final String sql = getSql("PO0011_06");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, args);
			tables.put(tableName, userDataList);
		}
		// 定期発注予定マスタ（定期発注申請すると定期発注予定マスタへ書き込むから）
		{
			final String tableName = "MWT_PRD_PURORD_DETAIL";	// 独立画面「定期発注」の明細部のコンテナのテーブル名
			final String sql = getSql("PO0011_07");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, args);
			tables.put(tableName, userDataList);
		}
		return tables;
	}

	/**
	 * 定期発注マスタ／定期発注予定マスタをユーザデータで更新
	 * @param userDataMap
	 */
	public void updatePrdPurordInf(Map<String, List<UserDataEntity>> userDataMap) {
		// 定期発注マスタ
		final UserDataEntity header = userDataMap.get("MWT_PRD_PURORD").get(0);
		final String companyCd = header.corporationCode;
		final Long prdPurordNo = toLong(header.values.get("PRD_PURORD_NO"));
		final LoginInfo login = sessionHolder.getLoginInfo();
		String purordNo = null;
		{
			// 既存レコードの抽出
			final PrdPurordMstPK pk = new PrdPurordMstPK();
			pk.setCompanyCd(companyCd);
			pk.setPrdPurordNo(prdPurordNo);
			final PrdPurordMst e = em.find(PrdPurordMst.class, pk);

			// 定期支払区分	PRD_PAY_TP
			e.setPrdPayTp(toStr(header.values.get("PRD_PAY_TP")));
			// 定期支払方法	PRD_PAY_MTH
			e.setPrdPayMth(toStr(header.values.get("PRD_PAY_MTH")));
			// 月次計上区分	ML_ADD_TP
			e.setMlAddTp(toStr(header.values.get("ML_ADD_TP")));
			// 支払開始年月	PAY_START_TIME
			e.setPayStartTime(toStr(header.values.get("PAY_START_TIME")).replaceAll("/", ""));
			// 支払終了年月	PAY_END_TIME
			e.setPayEndTime(toStr(header.values.get("PAY_END_TIME")).replaceAll("/", ""));
			// 支払サイトコード	PAY_SITE_CD
			e.setPaySiteCd(toStr(header.values.get("PAY_SITE_CD")));
			e.setCorporationCodeUpdated(login.getCorporationCode());
			e.setUserCodeUpdated(login.getUserCode());
			e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
			e.setTimestampUpdated(timestamp());

			em.flush();

			purordNo = e.getPurordNo();
		}

		// 定期発注予定マスタ
		{
			// 既存レコードのうち、未自動起票のレコードを削除
			deletePrdPurordPlnMst(companyCd, prdPurordNo);
			// 連番の最大値
			int sqno = getMaxSqno(companyCd, prdPurordNo);
			// 定期支払回数の最大値
			int prdPayCnt = getMaxPrdPayCnt(companyCd, prdPurordNo);

			final List<UserDataEntity> details = userDataMap.get("MWT_PRD_PURORD_DETAIL");
			for (UserDataEntity d : details) {
				// ステータス＝20:自動起票済みは更新対象外
				final String prdPaySts = toStr(d.values.get("PRD_PAY_STS"));
				if (eq(PrdPaySts.COMPLATED, prdPaySts))
					continue;

				// プライマリキー
				final PrdPurordPlnMstPK pk = new PrdPurordPlnMstPK();
				pk.setCompanyCd(companyCd);
				pk.setPrdPurordNo(prdPurordNo);
				pk.setSqno(++sqno);

				final PrdPurordPlnMst e = new PrdPurordPlnMst();
				e.setId(pk);
				// 発注No	PURORD_NO
				e.setPurordNo(purordNo);
				// 定期支払回数	PRD_PAY_CNT
				e.setPrdPayCnt(new BigDecimal(++prdPayCnt));
				// 定期支払日	PRD_PAY_DT
				e.setPrdPayDt((Date)d.values.get("PRD_PAY_DT"));
				// 定期起票日（予定）	PRD_IOV_DT_P
				e.setPrdIovDtP((Date)d.values.get("PRD_IOV_DT_P"));
				// 定期起票日（実績）	PRD_IOV_DT_R
				e.setPrdIovDtR((Date)d.values.get("PRD_IOV_DT_R"));
				// 定期支払ステータス	PRD_PAY_STS
				e.setPrdPaySts(PrdPaySts.PRE);
				// 削除フラグ	DLT_FG
				e.setDltFg(DeleteFlag.OFF);
				e.setCorporationCodeCreated(login.getCorporationCode());
				e.setCorporationCodeUpdated(login.getCorporationCode());
				e.setUserCodeCreated(login.getUserCode());
				e.setUserCodeUpdated(login.getUserCode());
				e.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
				e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
				e.setTimestampCreated(timestamp());
				e.setTimestampUpdated(timestamp());

				em.persist(e);
			}
			em.flush();
		}
	}

	/** 定期発注予定マスタの定期支払回数の最大値 */
	private int getMaxPrdPayCnt(String companyCd, Long prdPurordNo) {
		final String sql = getSql("PO0000_17");
		final Object[] params = { companyCd, prdPurordNo };
		return count(sql, params);
	}

	/** 定期発注予定マスタの連番の最大値 */
	private int getMaxSqno(String companyCd, Long prdPurordNo) {
		final String sql = getSql("PO0000_16");
		final Object[] params = { companyCd, prdPurordNo };
		return count(sql, params);
	}

	/** ステータス＝未自動起票の定期発注予定マスタを削除 */
	private int deletePrdPurordPlnMst(String companyCd, Long prdPurordNo) {
		final String sql = getSql("PO0000_15");
		final Object[] params = { companyCd, prdPurordNo };
		return execSql(sql, params);
	}
}
