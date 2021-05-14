package jp.co.nci.iwf.component.route;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import jp.co.nci.iwf.component.profile.UserInfo;

/**
 * 承認者情報.
 */
public class AssignedUserInfo implements Serializable {

	/** 会社コード */
	public String corporationCode;
	/** 会社名称 */
	public String corporationName;
	/** 会社付加情報 */
	public String corporationAddedInfo;
	/** 組織コード */
	public String organizationCode;
	/** 組織名称 */
	public String organizationName;
	/** 組織付加情報 */
	public String organizationAddedInfo;
	/** 役職コード */
	public String postCode;
	/** 役職名称 */
	public String postName;
	/** 役職付加情報 */
	public String postAddedInfo;
	/** ユーザコード */
	public String userCode;
	/** ユーザ名称 */
	public String userName;
	/** ユーザ付加情報 */
	public String userAddedInfo;
	/** 電話番号(内線番号) */
	public String telNum;
	/** 拡張項目01 */
	public String extendedInfo01;
	/** 拡張項目02 */
	public String extendedInfo02;
	/** 拡張項目03 */
	public String extendedInfo03;
	/** 拡張項目04 */
	public String extendedInfo04;
	/** 拡張項目05 */
	public String extendedInfo05;
	/** 参加者状態 */
	public String assignedStatus;
	/** ロールコード */
	public String assignRoleCode;
	/** ロール名 */
	public String assignRoleName;
	/** ロール所属区分 */
	public String belongType;
	/** 参加者表示/非表示フラグ */
	public boolean displayFlag;
	/** 承認待ち矢印を表示するかどうか */
	public boolean showArrow;
	/** 代理者のリスト */
	public List<UserInfo> proxyUserList;
	/** 代理者名一覧 */
	// なお画面表示用に使うため、代理者名を<br>タグにて連結しています
	public String proxyUsers;
	/** 承認日時 */
	public Timestamp executionDate;
	/** アクション名 */
	public String actionName;

	@Override
	public String toString() {
		return new StringBuilder(32)
				.append(corporationCode).append("/")
				.append(userCode).append(" ")
				.append(userName)
				.toString();
	}
}
