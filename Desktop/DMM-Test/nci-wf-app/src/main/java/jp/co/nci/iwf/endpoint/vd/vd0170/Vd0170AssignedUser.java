package jp.co.nci.iwf.endpoint.vd.vd0170;

import java.text.SimpleDateFormat;

import jp.co.nci.iwf.component.route.AssignedUserInfo;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 承認状況での参加者ユーザ情報
 */
public class Vd0170AssignedUser {
	/** ユーザコード */
	public String userCode;
	/** ユーザ名称 */
	public String userName;
	/** 承認日時 */
	public String approvedDate;


	public Vd0170AssignedUser() {
	}

	public Vd0170AssignedUser(AssignedUserInfo src) {
		MiscUtils.copyFields(src, this);

		if (src.executionDate != null) {
			approvedDate = new SimpleDateFormat("yyyy/MM/dd HH:mm")
					.format(src.executionDate);
		}
	}
}
