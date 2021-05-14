package jp.co.nci.iwf.endpoint.wl.wl0130;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmUserDisplayColumnEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmUserDisplayConditionEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmUserDisplay;

/**
 * 案件一覧リポジトリ
 */
@ApplicationScoped
public class Wl0130Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	public List<MwmUserDisplay> getUserDisplayList() {
		List<MwmUserDisplay> userDisplayList = new ArrayList<MwmUserDisplay>();

		// ワークリスト
		userDisplayList.add(userDisp(-1L, "デフォルト(TODO)", "1", CommonFlag.ON, 20L));
		// 自案件
		userDisplayList.add(userDisp(-2L, "デフォルト(自案件)", "2", CommonFlag.ON, 20L));
		// 汎用案件
		userDisplayList.add(userDisp(-3L, "デフォルト(汎用案件)", "3", CommonFlag.ON, 20L));

		// TODO:DB検索

		return userDisplayList;
	}

	public List<MwmUserDisplayConditionEx> getUserDisplayConditionList(String trayType, Long userDisplayId) {
		List<MwmUserDisplayConditionEx> condList = new ArrayList<MwmUserDisplayConditionEx>();

		if (-1L == userDisplayId) {
			if ("1".equals(trayType)) {
				condList.add(cond("APPLICATION_NO"				, "1", 1L, "申請番号"	, "applicationNo"));
				condList.add(cond("SUBJECT"						, "1", 2L, "件名"		, "subject"));
				condList.add(cond("BUSINESS_PROCESS_STATUS"		, "1", 3L, "ステータス"	, "businessProcessStatus"));
				condList.add(cond("USER_NAME_PROXY_PROCESS"		, "1", 4L, "起案者"		, "userNameProxyProcess"));
			}
		} else if (-2L == userDisplayId) {
			if ("2".equals(trayType)) {
				condList.add(cond("APPLICATION_NO"				, "1", 1L, "申請番号"	, "applicationNo"));
				condList.add(cond("SUBJECT"						, "1", 2L, "件名"		, "subject"));
				condList.add(cond("BUSINESS_PROCESS_STATUS"		, "1", 3L, "ステータス"	, "businessProcessStatus"));
				condList.add(cond("USER_NAME_PROXY_PROCESS"		, "1", 4L, "起案者"		, "userNameProxyProcess"));
			}
		} else if (-3L == userDisplayId) {
			if ("3".equals(trayType)) {
				condList.add(cond("APPLICATION_NO"				, "1", 1L, "申請番号"	, "applicationNo"));
				condList.add(cond("SUBJECT"						, "1", 2L, "件名"		, "subject"));
				condList.add(cond("BUSINESS_PROCESS_STATUS"		, "1", 3L, "ステータス"	, "businessProcessStatus"));
				condList.add(cond("USER_NAME_PROXY_PROCESS"		, "1", 4L, "起案者"		, "userNameProxyProcess"));
			}
		} else {
			// TODO DB検索
		}

		return condList;
	}

	public List<MwmUserDisplayColumnEx> getUserDisplayColumnList(String trayType, Long userDisplayId) {
		List<MwmUserDisplayColumnEx> colList = new ArrayList<MwmUserDisplayColumnEx>();

		if (-1L == userDisplayId) {
			if ("1".equals(trayType)) {
				colList.add(col("APPLICATION_NO"				, CommonFlag.ON, 1L, "申請番号"		, "applicationNo"));
				colList.add(col("SUBJECT"						, CommonFlag.ON, 2L, "件名"			, "subject"));
				colList.add(col("BUSINESS_PROCESS_STATUS_NAME"	, CommonFlag.ON, 3L, "ステータス"	, "businessProcessStatusName"));
				colList.add(col("ORGANIZATION_NAME_PROCESS"		, CommonFlag.ON, 4L, "起案部署"		, "organizationNameProcess"));
				colList.add(col("USER_NAME_PROXY_PROCESS"		, CommonFlag.ON, 5L, "起案者"		, "userNameProxyProcess"));
				colList.add(col("START_DATE"					, CommonFlag.ON, 6L, "起案日"		, "startDate"));
			}
		} else if (-2L == userDisplayId) {
			if ("2".equals(trayType)) {
				colList.add(col("APPLICATION_NO"				, CommonFlag.ON, 1L, "申請番号"		, "applicationNo"));
				colList.add(col("SUBJECT"						, CommonFlag.ON, 2L, "件名"			, "subject"));
				colList.add(col("BUSINESS_PROCESS_STATUS_NAME"	, CommonFlag.ON, 3L, "ステータス"	, "businessProcessStatusName"));
				colList.add(col("ORGANIZATION_NAME_PROCESS"		, CommonFlag.ON, 4L, "起案部署"		, "organizationNameProcess"));
				colList.add(col("USER_NAME_PROXY_PROCESS"		, CommonFlag.ON, 5L, "起案者"		, "userNameProxyProcess"));
				colList.add(col("START_DATE"					, CommonFlag.ON, 6L, "起案日"		, "startDate"));
			}
		} else if (-3L == userDisplayId) {
			if ("3".equals(trayType)) {
				colList.add(col("APPLICATION_NO"				, CommonFlag.ON, 1L, "申請番号"		, "applicationNo"));
				colList.add(col("SUBJECT"						, CommonFlag.ON, 2L, "件名"			, "subject"));
				colList.add(col("BUSINESS_PROCESS_STATUS_NAME"	, CommonFlag.ON, 3L, "ステータス"	, "businessProcessStatusName"));
				colList.add(col("ORGANIZATION_NAME_PROCESS"		, CommonFlag.ON, 4L, "起案部署"		, "organizationNameProcess"));
				colList.add(col("USER_NAME_PROXY_PROCESS"		, CommonFlag.ON, 5L, "起案者"		, "userNameProxyProcess"));
				colList.add(col("START_DATE"					, CommonFlag.ON, 6L, "起案日"		, "startDate"));
			}
		} else {
			// TODO DB検索
		}

		return colList;
	}

	private MwmUserDisplay userDisp(Long userDisplayId
									, String userDisplayName
									, String screenType
									, String defaultFlag
									, Long pageSize) {
		MwmUserDisplay userDisp = new MwmUserDisplay();
		userDisp.setUserDisplayId(userDisplayId);
		userDisp.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		userDisp.setUserCode(sessionHolder.getLoginInfo().getUserCode());
		userDisp.setUserDisplayName(userDisplayName);
		userDisp.setScreenType(screenType);
		userDisp.setDefaultFlag(defaultFlag);
		userDisp.setPageSize(pageSize);
		return userDisp;
	}

	private MwmUserDisplayConditionEx cond(String columnName
											, String conditionType
											, Long conditionIndex
											, String displayName
											, String attrName) {
		MwmUserDisplayConditionEx cond = new MwmUserDisplayConditionEx();
		cond.setColumnName(columnName);
		cond.setConditionType(conditionType);
		cond.setConditionIndex(conditionIndex);
		cond.setDisplayName(displayName);
		cond.setAttrName(attrName);
		return cond;
	}

	private MwmUserDisplayColumnEx col(String columnName
									, String displayFlag
									, Long columnIndex
									, String displayName
									, String attrName){
		MwmUserDisplayColumnEx col = new MwmUserDisplayColumnEx();
		col.setColumnName(columnName);
		col.setColumnDisplayFlag(displayFlag);
		col.setColumnIndex(columnIndex);
		col.setDisplayName(displayName);
		col.setAttrName(attrName);
		return col;
	}
}
