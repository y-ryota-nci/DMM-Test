package jp.co.dmm.customize.endpoint.batch.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.api.param.input.CreateProcessInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.SetAdditionalInfoInParam;
import jp.co.nci.integrated_workflow.api.param.output.CreateProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SetAdditionalInfoOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ProcessRefUnitType;
import jp.co.nci.integrated_workflow.model.base.WftActivity;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfvUserBelongOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * Batch処理の共通クラス
 *
 */
public abstract class BatchBaseRepository extends BaseRepository {
	protected Logger log = LoggerFactory.getLogger(BatchBaseRepository.class);
	protected WfInstanceWrapper wf = CDI.current().select(WfInstanceWrapper.class).get();

	protected SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yy");
	protected SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MM");

	/**
	 * プロセス情報作成
	 * @param in INパラメータ
	 * @return OUT
	 */
	public Long createRequest(CreateProcessInstanceInParam in, String subject, String applicationNo, String comment, boolean skipMoveAcitivity) {

		try {
			// プロセス作成
			CreateProcessInstanceOutParam out = wf.createProcessInstance(in);

			// 付加情報更新
			SetAdditionalInfoInParam adIn = new SetAdditionalInfoInParam();
			adIn.setProcessId(out.getProcess().getProcessId());
			adIn.setCorporationCode(out.getProcess().getCorporationCode());
			adIn.setProcessRefUnitType(ProcessRefUnitType.PROCESS);
			adIn.setWfUserRole(in.getWfUserRole());
			adIn.setTimestampUpdatedProcess(out.getProcess().getTimestampUpdated());
			adIn.setSubject(subject);
			adIn.setApplicationNo(applicationNo);

			SetAdditionalInfoOutParam adOut = wf.setAdditionalInfoByForce(adIn);

			// アクティビティ
			WftActivity activity = out.getActivity();

			// 申請処理
			MoveActivityInstanceInParam acIn = new MoveActivityInstanceInParam();
			acIn.setCorporationCode(out.getProcess().getCorporationCode());
			acIn.setProcessId(out.getProcess().getProcessId());
			acIn.setActivityId(activity.getActivityId());
			acIn.setActionCode("1");
			acIn.setActionComment(comment);
			acIn.setSkip(true);
			acIn.setTimestampUpdatedProcess(adOut.getProcess().getTimestampUpdated());
			acIn.setWfUserRole(in.getWfUserRole());

			if (!skipMoveAcitivity) {
				wf.moveActivityInstanceByForce(acIn);
			}

			return out.getProcess().getProcessId();
		} catch(Exception e) {
			log.error("申請情報の作成処理に失敗しました。:{} {} {} {}", in.getWfUserRole().getCorporationCode(), in.getWfUserRole().getUserCode(), subject, applicationNo);
			return null;
		}

	}

	/**
	 * ユーザロール取得処理
	 * @param corporationCode 会社コード
	 * @param userCode ユーザコード
	 * @return
	 */
	public WfUserRole getWfUserRole(String corporationCode, String userCode) {

		if (StringUtils.isEmpty(userCode)) {
			return null;
		}

		// 申請者
		final SearchWfmUserInParam userIn = new SearchWfmUserInParam();
		userIn.setCorporationCode(corporationCode);
		userIn.setUserCode(userCode);
		userIn.setDeleteFlag(DeleteFlag.OFF);
		userIn.setValidStartDate(new java.sql.Date(System.currentTimeMillis()));

		List<WfmUser> wfmUserList = wf.searchWfmUser(userIn).getUserList();

		WfUserRole user = null;

		if (wfmUserList.size() != 0) {
			WfmUser wfmUser = wf.searchWfmUser(userIn).getUserList().get(0);

			user = new WfUserRoleImpl();
			user.setCorporationCode(wfmUser.getCorporationCode());
			user.setUserCode(wfmUser.getUserCode());
			user.setUserName(wfmUser.getUserName());
			user.setIpAddress("0:0:0:0:0:0:0:1");
			user.setProxy(false);
		}


		return user;
	}

	/**
	 * ユーザ所属取得処理
	 * @param corporationCode 会社コード
	 * @param userCode ユーザコード
	 * @return
	 */
	public List<WfvUserBelongImpl> getUserBelong(String corporationCode, String userCode) {
		SearchWfvUserBelongInParam userBelongIn = new SearchWfvUserBelongInParam();
		userBelongIn.setCorporationCode(corporationCode);
		userBelongIn.setUserCode(userCode);

		SearchWfvUserBelongOutParam userBelongOut = wf.searchWfvUserBelong(userBelongIn);

		return userBelongOut.getUserBelongList();
	}

	/**
	 * プロセス定義明細コード取得
	 * @param companyCd 会社コード
	 * @param processDefCode プロセス定義コード
	 * @return プロセス定義明細コード
	 */
	@SuppressWarnings("unchecked")
	public String getProcessDefDetailCode(String companyCd, String processDefCode) {
		String processDefDetailCode = null;

		String sql = getSql("BATCH_CO0001");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, processDefCode);

		List<Object> results = q.getResultList();

		if (results.size() != 0 && results.get(0) != null) {
			processDefDetailCode = results.get(0).toString();
		}

		return processDefDetailCode;
	}

	/**
	 * 画面プロセスID取得
	 * @param companyCd 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義明細コード
	 * @return 画面プロセスID
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getScreenProcessId(String companyCd, String processDefCode, String processDefDetailCode, String screenProcessCode) {
		BigDecimal screenProcessId = null;

		String sql = getSql("BATCH_CO0003");
		Query q = em.createNativeQuery(sql);
		q.setParameter(1, companyCd);
		q.setParameter(2, processDefCode);
		q.setParameter(3, processDefDetailCode);
		q.setParameter(4, screenProcessCode);

		List<Object> results = q.getResultList();

		if (results.size() != 0) {
			screenProcessId = (BigDecimal)results.get(0);
		}

		return screenProcessId;
	}



	/**
	 * 採番.
	 * @param tableName テーブル名
	 * @param columnName カラム名
	 * @param count 現在値から進めるカウント数。
	 * 				通常は1つずつ採番するが、複数個を採番することが事前に分かっているなら、
	 * 				パフォーマンスを向上させるためにその分を一度に採番させたい場合に2以上を指定する。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(value = TxType.REQUIRES_NEW)
	public long nextNumber(String tableName, String columnName, long count) {

		String sql = getSql("BATCH_CO0004");

		try {
           	Query q = em.createNativeQuery(sql);
			q.setParameter(1, tableName);
			q.setParameter(2, columnName);
			q.setParameter(3, count);

			List<Object> results = q.getResultList();

			return ((BigDecimal)results.get(0)).longValue();

       } catch(Exception e) {
          throw new InternalServerErrorException("採番できませんでした");
       }
	}

	/**
	 * 申請番号取得処理
	 * @param headerFormat ヘッダ付加文字
	 * @param yearFormat 年度フォーマット
	 * @param partsSequenceSpecId スペックID
	 * @return
	 */
	@Transactional(value = TxType.REQUIRES_NEW)
	public String getApplicationNo(String headerFormat, String yearFormat, Long partsSequenceSpecId) {

		//ヘッダ文字＋年度２桁＋月２桁＋連番６桁
		//現在の年月
		Date currentDate = new Date();

		String currentYear = YEAR_FORMAT.format(currentDate);
		String currentMonth = MONTH_FORMAT.format(currentDate);

		//年度、月
		String strYear = "";
		String strMonth = currentMonth;

		if ("YY".equals(yearFormat)) {
			strYear = currentYear;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.add(Calendar.MONTH, -3);

			strYear = YEAR_FORMAT.format(cal.getTime());
		}

		String groupingKey = currentYear + "." + currentMonth;

		//連番取得
		String seqSql = getSql("BATCH_CO0005");
		Query q = em.createNativeQuery(seqSql);

		q.setParameter(1, partsSequenceSpecId);
		q.setParameter(2, groupingKey);
		q.setParameter(3, "4");
		q.setParameter(4, timestamp());
		q.setParameter(5, 1L);

		Long number = ((Number)q.getSingleResult()).longValue();

		String strNuber = String.format("%06d", number.intValue());

		return headerFormat + strYear + strMonth + strNuber;
	}
}
