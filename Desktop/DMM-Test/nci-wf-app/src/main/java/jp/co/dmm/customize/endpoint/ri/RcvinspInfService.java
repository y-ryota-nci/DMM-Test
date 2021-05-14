package jp.co.dmm.customize.endpoint.ri;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import jp.co.dmm.customize.component.DmmCodeBook.AdvpaySts;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMst;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMstPK;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayInf;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayInfPK;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayMatInf;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayMatInfPK;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInf;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * DMM検収の共通サービス
 */
@ApplicationScoped
public class RcvinspInfService extends BaseRepository {

	private static final String REPLACE = quotePattern("${REPLACE}");

	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	@Inject private SessionHolder sessionHolder;

	/**
	 * 検収Noをキーに検収情報を抽出し、それをUserDataServiceで使用できる形に整形して返す
	 * @param companyCd 会社CD
	 * @param rcvinspNo 検収No
	 * @return
	 */
	public Map<String, List<UserDataEntity>> getUserDataMap(String companyCd, String rcvinspNo) {
		assert(isNotEmpty(companyCd));
		assert(isNotEmpty(rcvinspNo));

		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Object[] params = { localeCode, companyCd, rcvinspNo };
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();

		// 検収情報をユーザデータとして読み込み
		{
			final String tableName = "MWT_RCVINSP";	// ヘッダ部のコンテナのテーブル名
			final String sql = getSql("RI0031_01");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		// 検収明細情報をユーザデータとして読み込み
		{
			final String tableName = "MWT_RCVINSP_DETAIL";	// 明細部のコンテナのテーブル名
			final String sql = getSql("RI0031_02");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		return tables;
	}

	/** 会計カレンダーマスタ抽出 */
	public AccClndMst getAccClndMst(String companyCd, java.sql.Date dt) {
		final AccClndMstPK key = new AccClndMstPK();
		key.setCompanyCd(companyCd);
		key.setClndDt(dt);
		return em.find(AccClndMst.class, key);
	}

	/**
	 * 前払充当額が前払金額を超過していないこと。
	 * @param companyCd 会社コード
	 * @param rcvinspNo 検収No（今回申請する検収申請の検収No）
	 * @param advpayNo 前払金No
	 * @return 前払残高金額
	 */
	public BigDecimal getRmnPayAmt(String companyCd, String rcvinspNo, String advpayNo) {
		// 前払金情報
		BigDecimal payAmt = BigDecimal.ZERO;
		{
			final AdvpayInfPK pk = new AdvpayInfPK();
			pk.setCompanyCd(companyCd);
			pk.setAdvpayNo(advpayNo);
			final AdvpayInf entity = em.find(AdvpayInf.class, pk);
			if (entity != null) {
				payAmt = eq("JPY", entity.getMnyCd()) ? entity.getPayAmtJpy() : entity.getPayAmtFc();
			}
		}
		// 既存の前払金消込情報
		BigDecimal matAmt = BigDecimal.ZERO;
		{
			final Object[] params = { companyCd, advpayNo };
			final String sql = getSql("RI0000_11");
			final List<AdvpayMatInf> entities = select(AdvpayMatInf.class, sql, params);
			for (AdvpayMatInf e : entities) {
				// 今回の検収申請の検収Noの消込情報は除いて、消込金額を合算
				// 申請＞差戻＞再申請で消込金額が変更されることを想定してる
				if (isEmpty(e.getRcvinspNo()) || !eq(e.getRcvinspNo(), rcvinspNo)) {
					BigDecimal v = eq("JPY", e.getMnyCd()) ? e.getMatAmtJpy() : e.getMatAmtFc();
					matAmt = matAmt.add(v);
				}
			}
		}
		// 前払残高＝前払金額－既存の前払消込金額
		final BigDecimal rmnPayAmt = payAmt.add(matAmt.negate());
		return rmnPayAmt;
	}

	/**
	 * 前払充当を行う前払申請が完了していること。(現状運用で対応するため不要とのこと)
	 * @param companyCd 会社コード
	 * @param advpayNo 前払金No
	 * @return 支払Noとプロセスステータス
	 */
//	public String[] getAdvPayProcess(String companyCd,  String advpayNo) {
//		final Object[] params = { companyCd, advpayNo };
//		final String sqlBasis = getSql("RI0000_11_02");
//		StringBuilder sql = new StringBuilder();
//		sql.append(sqlBasis);
//		final List<AdvpayInfEX> entities = select(AdvpayInfEX.class, sql.toString(), params);
//		String[] resArray  = {null , null};
//		if(MiscUtils.isEmpty(entities)) {
//			return null;
//		}
//		for (AdvpayInfEX e : entities) {
//			  if(MiscUtils.eq(e.getProcessStatus() ,CodeMaster.ProcessStatus.END)
//					|| MiscUtils.eq(e.getProcessStatus() ,CodeMaster.ProcessStatus.END_R)) {
//				  resArray[1] = e.getProcessStatus();
//			  }
//			  resArray[0] = e.getPayNo();
//			  break ;
//		}
//		return resArray;
//	}

	/**
	 * 充当した前払申請が別の検収申請で引き当てられていないこと
	 * @param companyCd 会社コード
	 * @param advpayNo 前払金No
	 * @return 終了していない前払申請の数
	 */
	public String getRmnAdvPayProcess(String companyCd,  String advpayNo , String rcvNo) {
		//	 現在申請中の検収に該当の前払いが引き当てられている場合
		rcvNo = MiscUtils.isEmpty(rcvNo) ?  "error_no_number" : rcvNo; // 空白だとNULL扱いでバインド変数にセットされるため
		final Object[] params = { companyCd, advpayNo , rcvNo};
		final String sqlBasis = getSql("RI0000_11_03_01");
		final String tableName = "MWT_RCVINSP";
		final StringBuilder sql = new StringBuilder();
		sql.append(sqlBasis);
		final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql.toString(), params);
		for (Iterator<UserDataEntity> iterator = userDataList.iterator(); iterator.hasNext();) {
			UserDataEntity userDataEntity = (UserDataEntity) iterator.next();
			Map<String, Object> userDataEntityMap =  userDataEntity.values;
			if(userDataEntityMap == null) {
				//	こんなことはないとは思うが念のため
				break;
			}
			// 該当の前払いNoが含まれている検収申請を返す(別に複数個見つける必要はないため見つかった時点で返却する)
			//  該当の前払いNoが含まれている検収申請がない場合はnullを返す
			return MiscUtils.toStr(userDataEntityMap.get("RCVINSP_NO")) ;
		}
		// 該当の前払いNoが含まれている検収申請がない場合はnullを返す
		return null;
	}

	/**
	 * 充当した前払申請が別の検収申請で引き当てられていないこと
	 * @param companyCd 会社コード
	 * @param advpayNo 前払金No
	 * @return 終了していない前払申請の数
	 */
	public String getRmnAdvPayDeleteProcess(String companyCd,  String advpayNo , String rcvinspNo) {
		// 2パターン目 : 変更_検収申請において該当の前払いが削除されている場合
		rcvinspNo = MiscUtils.isEmpty(rcvinspNo) ?  "error_no_number" : rcvinspNo; // 空白だとNULL扱いでバインド変数にセットされるため
		final Object[] params2 = { companyCd, advpayNo , rcvinspNo };
		final String sqlBasis2 = getSql("RI0000_11_03_02");
		final String tableName2 = "WFT_PROCESS";
		StringBuilder sql = new StringBuilder();
		sql.append(sqlBasis2);
		// 現在完了済の申請で引き当てられているか
		List<UserDataEntity>  userDataList  = loader.getUserData(tableName2, sql.toString(), params2);
		Object  applicationNo = null;
		if(userDataList == null || userDataList.isEmpty()) {
			// そもそも現在完了済の申請で引き当てられていなかったらnullを返す
			return null;
		}
		// 1つ以上あることは↑のif文から確定済
		for(UserDataEntity userDataEntity : userDataList) {
			Map<String, Object> userDataEntityMap =  userDataEntity.values;
			if(userDataEntityMap == null) {
				//	こんなことはないとは思うが念のため
				return null;
			}
			applicationNo=userDataEntityMap.get("APPLICATION_NO");

			final Object[] params3 = { companyCd, applicationNo };
			final String sqlBasis3 = getSql("RI0000_11_03_03");
			sql = new StringBuilder();
			sql.append(sqlBasis3);
			int res = count(sql.toString(), params3);
			if(res > 0) {
				// 現在申請中のものがあればその検収番号を返す
				return applicationNo.toString();
			}
		}
		// 現在申請中のものがなければnullを返す
		return null;
	}

	/**
	 * 検収Noに紐付く前払消込情報を抽出
	 * @param companyCd
	 * @param rcvinspNo
	 * @return
	 */
	public List<AdvpayMatInf> getAdvpayMatInfByRcvinspNo(String companyCd, String rcvinspNo) {
		final String sql = getSql("RI0000_12");
		final Object[] params = { companyCd, rcvinspNo };
		return select(AdvpayMatInf.class, sql, params);
	}

	/** 前払金消込情報をインサート */
	public void insertAdvpayMat(String companyCd, String rcvinspNo, Integer rcvinspDtlNo, String advpayNo,
			BigDecimal matAmt, String mnyCd, String addRto) {

		final AdvpayMatInfPK key = new AdvpayMatInfPK();
		key.setCompanyCd(companyCd);
		key.setAdvpayNo(advpayNo);
		key.setAdvpayMatNo(nextAdvpayMatNo(companyCd, advpayNo));

		final AdvpayMatInf e = new AdvpayMatInf();
		e.setId(key);
		if (eq("JPY", mnyCd)) {
			e.setMatAmtJpy(matAmt);
			e.setMatAmtJpyInctax(matAmt);	// 前払は必ず税込
		}
		else {
			e.setMatAmtFc(matAmt);
		}
		e.setMnyCd(mnyCd);
		e.setAddRto(MiscUtils.toBD(addRto));
		e.setRcvinspNo(rcvinspNo);
		e.setRcvinspDtlNo(new BigDecimal(rcvinspDtlNo));
		e.setDltFg(DeleteFlag.OFF);

		final LoginInfo login = sessionHolder.getLoginInfo();
		e.setCorporationCodeCreated(login.getCorporationCode());
		e.setUserCodeCreated(login.getUserCode());
		e.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampCreated(timestamp());
		e.setCorporationCodeUpdated(login.getCorporationCode());
		e.setUserCodeUpdated(login.getUserCode());
		e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampUpdated(timestamp());

		em.persist(e);
	}

	/** 前払金消込情報をアップデート */
	public void updateAdvpayMap(AdvpayMatInf e, String rcvinspNo, Integer rcvinspDtlNo, String advpayNo,
			BigDecimal matAmt, String mnyCd, String addRto) {

		if (eq("JPY", mnyCd)) {
			e.setMatAmtJpy(matAmt);
			e.setMatAmtJpyInctax(matAmt);	// 前払は必ず税込なので同値
			e.setMatAmtFc(null);
		}
		else {
			e.setMatAmtJpy(null);
			e.setMatAmtJpyInctax(null);
			e.setMatAmtFc(matAmt);
		}
		e.setMnyCd(mnyCd);
		e.setAddRto(MiscUtils.toBD(addRto));
		e.setRcvinspNo(rcvinspNo);
		e.setRcvinspDtlNo(new BigDecimal(rcvinspDtlNo));
		e.setPayNo(null);
		e.setPayDtlNo(null);
		e.setJrnslpNo(null);
		e.setJrnslpDtlNo(null);
		e.setSsglSndNo(null);
		e.setDltFg(DeleteFlag.OFF);

		final LoginInfo login = sessionHolder.getLoginInfo();
		e.setCorporationCodeUpdated(login.getCorporationCode());
		e.setUserCodeUpdated(login.getUserCode());
		e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampUpdated(timestamp());

		em.merge(e);
	}

	/** 前払金消込情報.前払金消込Noを採番 */
	private int nextAdvpayMatNo(String companyCd, String advpayNo) {
		final String sql = getSql("RI0000_13");
		final Object[] params = { companyCd, advpayNo };
		return count(sql, params) + 1;
	}

	/** 前払金消込情報を削除 */
	public void delete(AdvpayMatInf advpayMat) {
		em.remove(advpayMat);
	}

	/** 前払情報の残高と前払いステータスの更新 */
	public void updateAdvpayInf(String companyCd, Set<String> advpayNoList, String mnyCd) {
		for (String advpayNo : advpayNoList) {
			// 前払金Noに紐付く前払消込情報を抽出し、その消込金額を合算
			final List<AdvpayMatInf> matList = getAdvpayMatInf(companyCd, advpayNo);
			BigDecimal sumMatAmt = BigDecimal.ZERO;
			for (AdvpayMatInf mat : matList) {
				sumMatAmt = sumMatAmt.add(eq("JPY", mnyCd) ? mat.getMatAmtJpy() : mat.getMatAmtFc());
			}

			// 更新対象である前払情報を抽出
			final AdvpayInfPK key = new AdvpayInfPK();
			key.setCompanyCd(companyCd);
			key.setAdvpayNo(advpayNo);
			final AdvpayInf advpay = em.find(AdvpayInf.class, key);
			if (advpay == null)
				throw new NotFoundException("前払情報が見つかりません。会社CD=" + companyCd + " 前払金No=" + advpayNo);

			// 支払金額
			final BigDecimal payAmt = eq("JPY", mnyCd) ? advpay.getPayAmtJpy() : advpay.getPayAmtFc();
			// 残高＝支払金額－sum(消込金額)
			final BigDecimal rmnAmt = payAmt.add(sumMatAmt.negate());

			// 前払ステータス
			if (compareTo(rmnAmt, BigDecimal.ZERO) == 0)
				advpay.setAdvpaySts(AdvpaySts.DONE);
			else if (compareTo(rmnAmt, payAmt) == 0)
				advpay.setAdvpaySts(AdvpaySts.INIT);
			else if (compareTo(rmnAmt, BigDecimal.ZERO) < 0)
				throw new InternalServerErrorException("前払充当額の合算値が前払金額を超過しています。会社CD=" + companyCd + " 前払金No=" + advpayNo);
			else
				advpay.setAdvpaySts(AdvpaySts.DOING);

			// 残高
			if (eq("JPY", mnyCd)) {
				advpay.setRmnAmtJpy(rmnAmt);
				advpay.setRmnAmtFc(null);
			}
			else {
				advpay.setRmnAmtJpy(null);
				advpay.setRmnAmtFc(rmnAmt);
			}
			advpay.setDltFg(DeleteFlag.OFF);
		}
	}

	/** 前払金Noをキーに前払消込情報を抽出 */
	private List<AdvpayMatInf> getAdvpayMatInf(String companyCd, String advpayNo) {
		final Object[] params = { companyCd, advpayNo };
		final String sql = getSql("RI0000_14");
		return select(AdvpayMatInf.class, sql, params);
	}

	/** 前払に紐づく検収Noをキーに件数を抽出 */
	public int getAdvpayToRmnCount(String companyCd, String rcvinspNo) {
		StringBuilder sql = new StringBuilder(
				getSql("RI0000_12_02").replaceFirst(REPLACE, "count(*)").replaceFirst(REPLACE, "RCVINSP_INF"));
		sql.append("and ADVPAY_TP = 1");
		Object[] params = {companyCd, rcvinspNo};
		return count(sql, params);
	}

	/** 前払に紐づく検収Noをキーに検収情報を抽出 */
	public List<RcvinspdtlInf> getAdvpayToRmn(String companyCd, String rcvinspNo) {
		StringBuilder sql = new StringBuilder(
				getSql("RI0000_12_02").replaceFirst(REPLACE, "*").replaceFirst(REPLACE, "RCVINSPDTL_INF"));
		Object[] params = {companyCd, rcvinspNo};
		return select(RcvinspdtlInf.class, sql.toString(), params);
	}
}
