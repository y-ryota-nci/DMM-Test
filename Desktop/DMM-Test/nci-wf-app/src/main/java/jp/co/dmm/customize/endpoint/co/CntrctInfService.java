package jp.co.dmm.customize.endpoint.co;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.jpa.entity.mw.RtnPayPlnMst;
import jp.co.dmm.customize.jpa.entity.mw.RtnPayPlnMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 契約情報サービス
 */
@ApplicationScoped
public class CntrctInfService extends BaseRepository{
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** セッション情報 */
	@Inject private SessionHolder sessionHolder;

	/**
	 * 契約Noをキーに契約情報を抽出し、それをUserDataServiceで使用できる形に整形して返す
	 * @param companyCd 会社CD
	 * @param cntrctNo 契約No
	 * @param rtnPayNo 支払予約No
	 * @param allFetch 支払予約情報を全件フェッチするか 全件フェッチしない場合、最大3件
	 * @return
	 */
	public Map<String, List<UserDataEntity>> getUserDataMap(String companyCd, String cntrctNo, String rtnPayNo, boolean allFetch) {
		final String locale = LoginInfo.get().getLocaleCode();

		// 契約情報をユーザデータとして読み込み
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();
		{
			final Object[] params = {locale, locale, locale, locale, companyCd, cntrctNo};
			final String tableName = "MWT_CNTRCT_REQUEST";
			final String sql = getSql("CO0020_07");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		// 契約取引先情報をユーザデータとして読み込み
		{
			final Object[] params = {companyCd, cntrctNo};
			final String tableName = "MWT_CNTRCT_SPLR_DETAIL";
			final String sql = getSql("CO0020_08");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}

		if (StringUtils.isNotEmpty(rtnPayNo)) {
			// 経常支払情報をユーザデータとして読み込み
			{
				final Object[] params = {locale, companyCd, rtnPayNo, cntrctNo};
				final String tableName = "MWT_CNTRCT_RTN_PAY";
				final String sql = getSql("CO0020_09");
				final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
				tables.put(tableName, userDataList);
			}
			// 経常支払明細情報をユーザデータとして読み込み
			{
				final Object[] params = {companyCd, rtnPayNo};
				final String tableName = "MWT_CNTRCT_RTN_PAY_DETAIL";
				final String sql = getSql("CO0020_10");
				final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
				tables.put(tableName, userDataList);
			}
			// 経常支払予定マスタをユーザデータとして読み込み
			{
				final Object[] params = {companyCd, rtnPayNo};
				final String tableName = "MWT_CNTRCT_RTN_PAY_PLN";
				// allFetchがfalseの場合（＝変更申請時）は最大3件分のみ取得する
				final String sql = allFetch ? getSql("CO0020_12") : getSql("CO0020_13");
				final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql.toString(), params);
				tables.put(tableName, userDataList);
			}
		}
		return tables;
	}

	/** 契約NoをWF支払予約情報へ転写 */
	public int syncCntrctNo(Long processId, String cntrctNo) {
		final Object[] params = { cntrctNo, processId };
		final String sql = getSql("CO0000_06");
		return execSql(sql, params);
	}

	/**
	 * 経常支払予定マスタをユーザデータで更新
	 * @param userDataMap
	 */
	public void updateRtnPayPlnMst(Map<String, List<UserDataEntity>> userDataMap) {
		// 経常支払マスタから会社コード、経常支払Noを取得
		final UserDataEntity header = userDataMap.get("MWT_CNTRCT_RTN_PAY").get(0);
		final String companyCd = header.corporationCode;
		final Long rtnPayNo = toLong(header.values.get("RTN_PAY_NO"));
		final LoginInfo login = sessionHolder.getLoginInfo();

		// 経常支払予定マスタ
		{
			// 経常支払予定マスタの既存レコードを抽出
			List<RtnPayPlnMst> list = getRtnPayPlnMstList(companyCd, rtnPayNo);
			// 連番をキーにMapに変換
			Map<Integer, RtnPayPlnMst> orgs = list.stream().collect(Collectors.toMap(e -> (int)e.getId().getSqno(), e -> e));

			int sqno = 1;
			// 入力レコードを取得
			final List<UserDataEntity> details = userDataMap.get("MWT_CNTRCT_RTN_PAY_PLN");
			for (UserDataEntity d: details) {
				final RtnPayPlnMst e = orgs.containsKey(sqno) ? orgs.remove(sqno) : new RtnPayPlnMst();
				// 各処理区分が"1"(処理済)以外であれば入力データの作成予定日をセット
				// 発注作成予定日
				if (!eq(CommonFlag.ON, e.getPurordBtchTp())) {
					e.setPurordBtchTp(CommonFlag.OFF);
					e.setPurordBtchPlnDt((Date)d.values.get("PURORD_BTCH_PLN_DT"));
				}
				// 検収作成予定日
				if (!eq(CommonFlag.ON, e.getRcvinspBtchTp())) {
					e.setRcvinspBtchTp(CommonFlag.OFF);
					e.setRcvinspBtchPlnDt((Date)d.values.get("RCVINSP_BTCH_PLN_DT"));
				}
				// 支払申請作成予定日
				if (!eq(CommonFlag.ON, e.getPaysbmtBtchTp())) {
					e.setPaysbmtBtchTp(CommonFlag.OFF);
					e.setPaysbmtBtchPlnDt((Date)d.values.get("PAYSBMT_BTCH_PLN_DT"));
				}

				// 削除フラグ等を設定
				e.setDltFg(DeleteFlag.OFF);
				e.setCorporationCodeUpdated(login.getCorporationCode());
				e.setUserCodeUpdated(login.getUserCode());
				e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
				e.setTimestampUpdated(timestamp());

				// PKがなければ新規登録
				if (e.getId() == null) {
					final RtnPayPlnMstPK pk = new RtnPayPlnMstPK();
					pk.setCompanyCd(companyCd);
					pk.setRtnPayNo(rtnPayNo);
					pk.setSqno(sqno);

					e.setId(pk);
					e.setCorporationCodeCreated(login.getCorporationCode());
					e.setUserCodeCreated(login.getUserCode());
					e.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
					e.setTimestampCreated(timestamp());

					em.persist(e);
				} else {
					em.merge(e);
				}
				sqno++;
			}

			// orgsに残ったものは不要なデータなので削除
			orgs.values().stream().forEach(e -> em.remove(e));

			em.flush();
		}
	}

	/** 経常支払予定マスタを抽出 */
	private List<RtnPayPlnMst> getRtnPayPlnMstList(String companyCd, Long rtnPayNo) {
		final String sql = getSql("CO0000_08");
		final Object[] params = { companyCd, rtnPayNo };
		return select(RtnPayPlnMst.class, sql, params);
	}
}
